package com.saratms.ismailiapharmserv.Contracts;

import android.content.Context;

import com.saratms.ismailiapharmserv.Models.JobOffer;

/**
 * Created by Sarah Al-Shamy on 06/07/2019.
 */

public interface VacancyFormContract {

    interface VacancyFormView{
        void setupPresenter();
        void showInvalidInputsToast();
        void showServerErrorToast(String errorMsg);
        void showSuccessToast();
        void finishActivity();
        void submitEntries();
        void showProgressBar();
        void hideProgressBar();
        void showNoInternetSnackbar();
        void enableSubmitButton();
    }

    interface VacancyFormPresenter{
        void attachView(VacancyFormView vacancyFormView);
        void detachView();
        void handleEntries(Context context, int categoryId, String companyName, String phone, String address, String vacancyDetails);
        boolean checkInternetConnection(Context context);
        boolean checkValidation(String companyName, String phone, String address, String vacancyDetails);
    }

    interface VacancyFormModel{
        void postOfferToDatabase(JobOffer jobOffer);
    }
}
