package com.saratms.ismailiapharmserv.ModelInteractors;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.saratms.ismailiapharmserv.Contracts.VacanciesContract;
import com.saratms.ismailiapharmserv.Models.Vacancy;

import java.util.ArrayList;
import java.util.List;

import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.NO_CATEGORY_ID;

/**
 * Created by Sarah Al-Shamy on 11/06/2019.
 */

public class VacanciesModelImp implements VacanciesContract.VacancyModel, ValueEventListener {

    private static final String OPEN_VACANCIES = "open_vacancies";
    private static final String CATEGORIZED_VACANCIES = "categorized_vacancies";

    public static final int VACANCIES_PER_LOAD = 20;
    private int mCategoryId = NO_CATEGORY_ID;
    private String mLastVacancyUploaded;

    //used to track fetching categorized vacancies
    private int vacancyFetchedCount = 0;

    private OnFetchVacanciesListener mOnFetchVacanciesListener;
    private FirebaseDatabase mFirebaseDatabase;
    private ValueEventListener mCategorizedVacancyListener;
    private Query mQuery;
    private DatabaseReference mCorrespondingVacancyToIdReference;

    public VacanciesModelImp(OnFetchVacanciesListener onFetchVacanciesListener) {
        mOnFetchVacanciesListener = onFetchVacanciesListener;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void resetVacancies() {
        mLastVacancyUploaded = null;
    }

    @Override
    public void fetchVacancies(int categoryId) {
        mCategoryId = categoryId;
        DatabaseReference vacanciesReference = null;
        if (categoryId == NO_CATEGORY_ID) {
            vacanciesReference = mFirebaseDatabase.getReference().child(OPEN_VACANCIES);
        } else {
            vacanciesReference = mFirebaseDatabase.getReference().child(CATEGORIZED_VACANCIES).child(String.valueOf(categoryId));
        }
        mQuery = queryingData(vacanciesReference);
        mQuery.addListenerForSingleValueEvent(this);
    }

    private Query queryingData(DatabaseReference vacanciesReference) {
        if (isFirstTimeLoading()) {
            //We load more than the count we need to fetch by 1, to get the id for the next offset (in case there are more data
            //to load), but after retrieving this id, we should set the last item to null to show instead a small progress bar (handled in the adapter)
            return vacanciesReference.orderByKey().limitToLast(VACANCIES_PER_LOAD + 1);
        } else {
            return vacanciesReference.orderByKey().endAt(mLastVacancyUploaded).limitToLast(VACANCIES_PER_LOAD + 1);
        }
    }

    public boolean isFirstTimeLoading() {
        return (mLastVacancyUploaded == null);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Vacancy> newVacancies = new ArrayList<>();
        if (dataSnapshot.exists()) {
            if (mCategoryId == NO_CATEGORY_ID) {
                //The dataSnaphot here will contain list of vacancies
                newVacancies = getVacancies(dataSnapshot);
                processLoadedData(newVacancies);
            } else {
                //In this case the snapshot will contain vacancies Ids related to the category chosen
                //So we fetch those Ids then iterate through each of them to get the corresponding vacancy
                List<String> categorizedVacanciesIds = getCategorizedVacanciesIds(dataSnapshot);
                vacancyFetchedCount = categorizedVacanciesIds.size();
                fetchCorrespondingVacancy(categorizedVacanciesIds, newVacancies);
            }
        } else {
            //This will return empty list of size 0
            mOnFetchVacanciesListener.onFetchVacancies(newVacancies, false);
        }
    }

    private List<Vacancy> getVacancies(DataSnapshot dataSnapshot) {
        List<Vacancy> newVacancies = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Vacancy vacancy = (Vacancy) ds.getValue(Vacancy.class);
            vacancy.setVacancyId(ds.getKey());
            newVacancies.add(0, vacancy);
        }
        return newVacancies;
    }

    private List<String> getCategorizedVacanciesIds(DataSnapshot dataSnapshot) {
        List<String> categorizedIds = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String vacancyId = ds.getKey();
            categorizedIds.add(vacancyId);
        }
        return categorizedIds;
    }

    private void fetchCorrespondingVacancy(final List<String> categorizedVacanciesIds, final List<Vacancy> newVacancies) {
        final String categorizedVacancyId = categorizedVacanciesIds.get(vacancyFetchedCount - 1);
        mCorrespondingVacancyToIdReference = mFirebaseDatabase.getReference().child(OPEN_VACANCIES).child(categorizedVacancyId);
        mCategorizedVacancyListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Vacancy vacancy = dataSnapshot.getValue(Vacancy.class);
                vacancy.setVacancyId(categorizedVacancyId);
                newVacancies.add(vacancy);
                vacancyFetchedCount--;
                if (vacancyFetchedCount > 0) {
                    fetchCorrespondingVacancy(categorizedVacanciesIds, newVacancies);
                } else {
                    processLoadedData(newVacancies);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mOnFetchVacanciesListener.onFailed(databaseError.getMessage());
            }
        };
        mCorrespondingVacancyToIdReference.addListenerForSingleValueEvent(mCategorizedVacancyListener);
    }

    private void processLoadedData(List<Vacancy> newVacancies) {
        int vacancyListCount = newVacancies.size();
        if (vacancyListCount > 0) {
            Vacancy lastVacancy = newVacancies.get(vacancyListCount - 1);
            //This will be the offset for later fetching
            mLastVacancyUploaded = lastVacancy.getVacancyId();

            //This will check if there's still more data to load, if so, set the last item to null
            if (vacancyListCount > VACANCIES_PER_LOAD) {
                //Set last vacancy item to null, to indicate having more items to load and to show a load more progress bar
                newVacancies.set(vacancyListCount - 1, null);
                mOnFetchVacanciesListener.onFetchVacancies(newVacancies, true);
            } else {
                mOnFetchVacanciesListener.onFetchVacancies(newVacancies, false);
            }
        } else {
            mOnFetchVacanciesListener.onFetchVacancies(newVacancies, false);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        mOnFetchVacanciesListener.onFailed(databaseError.getMessage());
    }

    public interface OnFetchVacanciesListener {
        void onFetchVacancies(List<Vacancy> vacancies, boolean hasMoreToLoad);

        void onFailed(String errorMsg);
    }

    @Override
    public void detachListeners() {
        if (mQuery != null)
            mQuery.removeEventListener(this);

        if (mCorrespondingVacancyToIdReference != null && mCategorizedVacancyListener != null)
            mCorrespondingVacancyToIdReference.removeEventListener(mCategorizedVacancyListener);
    }

}
