package com.saratms.ismailiapharmserv.ModelInteractors;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saratms.ismailiapharmserv.Contracts.CategoriesContract;
import com.saratms.ismailiapharmserv.Models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class CategoriesModelImp implements CategoriesContract.CategoryModel, ValueEventListener {

    private static final String VACANCY_CATEGORIES = "vacancies";
    OnFetchCategoriesListener onFetchCategoriesListener;
    List<Category> vacancyCategories;
    private DatabaseReference mCategoriesReference;

    public CategoriesModelImp(OnFetchCategoriesListener onFetchCategoriesListener) {
        this.onFetchCategoriesListener = onFetchCategoriesListener;
        vacancyCategories = new ArrayList<>();
    }

    @Override
    public void fetchCategories() {
        mCategoriesReference = FirebaseDatabase.getInstance().getReference(VACANCY_CATEGORIES);
        mCategoriesReference.addListenerForSingleValueEvent(this);
    }

    @Override
    public void detachListeners() {
        if(mCategoriesReference != null)
            mCategoriesReference.removeEventListener(this);
    }

    private Category instantiateCategory(DataSnapshot d) {
        String id = d.getKey();
        String name = d.child("name").getValue().toString();
        String imagePath = d.child("image").getValue().toString();
        return new Category(id, name, imagePath);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            vacancyCategories.clear();
            //Iterate through each children to fetch id, name, imagePath of each category to create a category object
            //then add it to vacancyCategories list
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                Category category = instantiateCategory(d);
                vacancyCategories.add(category);
            }
            onFetchCategoriesListener.onFetchCategories(vacancyCategories);
        }else{
            onFetchCategoriesListener.onFailed("Server is busy, try again later!");
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        onFetchCategoriesListener.onFailed(databaseError.getMessage());
    }

    public interface OnFetchCategoriesListener {
        void onFetchCategories(List<Category> vacancyCategories);
        void onFailed(String errorMsg);
    }
}
