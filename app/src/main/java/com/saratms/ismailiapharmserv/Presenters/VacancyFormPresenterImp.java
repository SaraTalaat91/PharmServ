package com.saratms.ismailiapharmserv.Presenters;

import android.content.Context;
import android.text.TextUtils;

import com.saratms.ismailiapharmserv.Contracts.VacancyFormContract;
import com.saratms.ismailiapharmserv.ModelInteractors.VacancyFormModelImp;
import com.saratms.ismailiapharmserv.Models.JobOffer;
import com.saratms.ismailiapharmserv.Utilities.Networking;

/**
 * Created by Sarah Al-Shamy on 06/07/2019.
 */

public class VacancyFormPresenterImp implements VacancyFormContract.VacancyFormPresenter, VacancyFormModelImp.OnPostOfferListener {

    VacancyFormContract.VacancyFormView mVacancyFormView;
    VacancyFormContract.VacancyFormModel mVacancyFormModel;

    public VacancyFormPresenterImp() {
        mVacancyFormModel = new VacancyFormModelImp(this);
    }

    @Override
    public void attachView(VacancyFormContract.VacancyFormView vacancyFormView) {
        mVacancyFormView = vacancyFormView;
    }

    @Override
    public void detachView() {
        mVacancyFormView = null;
    }

    @Override
    public void handleEntries(Context context, int categoryId, String companyName, String phone, String address, String vacancyDetails) {
        boolean isValid = checkValidation(companyName, phone, address, vacancyDetails);
        if (isValid) {
            boolean isConnected = checkInternetConnection(context);
            if (isConnected) {
                JobOffer jobOffer = new JobOffer(categoryId, companyName, phone, address, vacancyDetails, System.currentTimeMillis());
                mVacancyFormModel.postOfferToDatabase(jobOffer);
            }
        } else {
            mVacancyFormView.showInvalidInputsToast();
            mVacancyFormView.enableSubmitButton();
        }
    }

    @Override
    public boolean checkInternetConnection(Context context) {
        mVacancyFormView.showProgressBar();
        if (Networking.isConnected(context)) {
            return true;
        } else {
            mVacancyFormView.hideProgressBar();
            mVacancyFormView.showNoInternetSnackbar();
            mVacancyFormView.enableSubmitButton();
            return false;
        }
    }

    @Override
    public boolean checkValidation(String companyName, String phone, String address, String vacancyDetails) {
        if (!TextUtils.isEmpty(companyName) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address)
                && !TextUtils.isEmpty(vacancyDetails)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPostSuccess() {
        mVacancyFormView.hideProgressBar();
        mVacancyFormView.showSuccessToast();
        mVacancyFormView.finishActivity();
    }

    @Override
    public void onPostFailed(String errorMsg) {
        mVacancyFormView.hideProgressBar();
        mVacancyFormView.enableSubmitButton();
        mVacancyFormView.showServerErrorToast(errorMsg);
    }
}
