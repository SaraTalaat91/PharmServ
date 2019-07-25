package com.saratms.ismailiapharmserv.Presenters;

import android.content.Context;

import com.saratms.ismailiapharmserv.Contracts.CategoriesContract;
import com.saratms.ismailiapharmserv.ModelInteractors.CategoriesModelImp;
import com.saratms.ismailiapharmserv.Models.Category;
import com.saratms.ismailiapharmserv.Utilities.Networking;

import java.util.List;

import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.NORMAL_LOADING;
import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.SWIPE_TO_REFRESH;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class CategoriesPresenterImp implements CategoriesContract.CategoryPresenter, CategoriesModelImp.OnFetchCategoriesListener {

    private CategoriesContract.CategoryView mCategoryView;
    private CategoriesContract.CategoryModel mCategoryModel;

    List<Category> mVacancyCategories;
    Boolean mIsLoading;

    public CategoriesPresenterImp() {
        mCategoryModel = new CategoriesModelImp(this);
    }

    @Override
    public boolean handleConnectionState(Context context) {
        if (Networking.isConnected(context)) {
            return true;
        } else {
            //This will hide the recycler and show the "no connection" layout
            mCategoryView.showNoConnectionLayout();
            mCategoryView.showNoConnectionSnack();
            mCategoryView.hideAllProgressBars();
            mIsLoading = false;
            return false;
        }
    }

    @Override
    public void requestCategories(Context context, int loadingAction) {
        //Check if we already fetched data before, then we just retrieve it
        //When the view attached get destroyed on configuration change, we retain the presenter
        //through onRetainCustomNonConfigurationInstance() method, this is why the presenter stays alive and so is the list
        if (mVacancyCategories == null) {
            mIsLoading = true;
            handleLoadingBars(loadingAction);
            boolean isConnected = handleConnectionState(context);
            if (isConnected) {
                mCategoryModel.fetchCategories();
            }
        } else {
            mCategoryView.updateCategoriesRecycler(mVacancyCategories);
        }
    }

    @Override
    public void refreshCategories(Context context) {
        //Refresh only in case the categories aren't previously loaded
        if (!mIsLoading) {
            if (mVacancyCategories == null) {
                requestCategories(context, SWIPE_TO_REFRESH);
            } else {
                mCategoryView.hideAllProgressBars();
            }
        }
    }

    @Override
    public void handleLoadingBars(int loadingAction) {
        switch (loadingAction) {
            case NORMAL_LOADING:
                mCategoryView.showProgressBar();
                break;
            case SWIPE_TO_REFRESH:
                //When the layout is swiped to refresh, it shows its own progress bar, so no need to implement one
                break;
        }
    }

    @Override
    public void attachView(CategoriesContract.CategoryView categoryView) {
        mCategoryView = categoryView;
    }

    @Override
    public void detachView() {
        mCategoryView = null;
    }

    @Override
    public void releaseResources() {
        mCategoryModel.detachListeners();
    }

    @Override
    public void onFetchCategories(List<Category> vacancyCategories) {
        if (mCategoryView != null) {
            mVacancyCategories = vacancyCategories;
            mCategoryView.showCategoriesRecycler();
            mCategoryView.updateCategoriesRecycler(vacancyCategories);
            mCategoryView.hideAllProgressBars();
            mIsLoading = false;
        }
    }

    @Override
    public void onFailed(String errorMsg) {
        mCategoryView.showErrorToast(errorMsg);
        mCategoryView.hideAllProgressBars();
        mIsLoading = false;
    }
}
