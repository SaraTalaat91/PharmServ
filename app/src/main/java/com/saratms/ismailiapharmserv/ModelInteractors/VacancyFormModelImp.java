package com.saratms.ismailiapharmserv.ModelInteractors;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saratms.ismailiapharmserv.Contracts.VacancyFormContract;
import com.saratms.ismailiapharmserv.Models.JobOffer;

/**
 * Created by Sarah Al-Shamy on 06/07/2019.
 */

public class VacancyFormModelImp implements VacancyFormContract.VacancyFormModel, OnSuccessListener<Void>, OnFailureListener {

    private DatabaseReference mDatabaseReference;
    OnPostOfferListener mOnPostOfferListener;

    public static final String JOB_OFFERS_KEY = "job_offers";


    public VacancyFormModelImp(OnPostOfferListener onPostOfferListener){
        mOnPostOfferListener = onPostOfferListener;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(JOB_OFFERS_KEY);
    };

    @Override
    public void postOfferToDatabase(JobOffer jobOffer) {
            mDatabaseReference.push().setValue(jobOffer).addOnSuccessListener(this).addOnFailureListener(this);
    }

    @Override
    public void onSuccess(Void aVoid) {
        mOnPostOfferListener.onPostSuccess();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        mOnPostOfferListener.onPostFailed(e.getMessage());
    }

    public interface OnPostOfferListener{
        void onPostSuccess();
        void onPostFailed(String errorMsg);
    }
}
