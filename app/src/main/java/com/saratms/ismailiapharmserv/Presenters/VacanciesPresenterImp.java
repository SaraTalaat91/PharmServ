package com.saratms.ismailiapharmserv.Presenters;

import android.content.Context;
import android.os.Handler;

import com.saratms.ismailiapharmserv.Contracts.VacanciesContract;
import com.saratms.ismailiapharmserv.ModelInteractors.VacanciesModelImp;
import com.saratms.ismailiapharmserv.Models.Vacancy;
import com.saratms.ismailiapharmserv.Utilities.Networking;

import java.util.List;

import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.NO_CATEGORY_ID;
import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.LOAD_MORE;
import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.NORMAL_LOADING;
import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.SWIPE_TO_REFRESH;

/**
 * Created by Sarah Al-Shamy on 11/06/2019.
 */

public class VacanciesPresenterImp implements VacanciesContract.VacancyPresenter, VacanciesModelImp.OnFetchVacanciesListener {
    VacanciesContract.VacancyModel mVacancyModel;
    VacanciesContract.VacancyView mVacancyView;
    boolean mIsLoading;
    Handler mHandler;
    private int mCategoryId = NO_CATEGORY_ID;

    //This boolean represents if there's already been data retrieved once at least
    //To avoid showing the empty view layout in case later we found no more data to retrieve
    boolean hasRetrievedDataBefore = false;
    boolean mHasMoreToLoad = true;

    public VacanciesPresenterImp() {
        mVacancyModel = new VacanciesModelImp(this);
        mHandler = new Handler();
    }

    @Override
    public boolean handleConnectionState(Context context) {
        if (Networking.isConnected(context)) {
            return true;
        } else {
            //This will hide the recycler and show the "no connection" layout
            mVacancyView.showNoConnectionLayout();
            mVacancyView.showNoConnectionSnack();
            mVacancyView.hideAllProgressBars();
            mIsLoading = false;
            return false;
        }
    }

    @Override
    public void requestVacancies(Context context, int loadingAction) {
        mIsLoading = true;
        handleLoadingBars(loadingAction);
        boolean isConnected = handleConnectionState(context);
        if (isConnected) {
            //We delay fetching data a little to allow nice transition
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVacancyModel.fetchVacancies(mCategoryId);
                }
            }, 1500);
        }
    }

    @Override
    public void loadMoreVacancies(Context context) {
        if (!mIsLoading && mHasMoreToLoad) {
            requestVacancies(context, LOAD_MORE);
        }
    }

    @Override
    public void refreshVacancies(Context context) {
        if (!mIsLoading) {
            mVacancyModel.resetVacancies();
            mVacancyView.resetVacancyList();
            hasRetrievedDataBefore = false;
            requestVacancies(context, SWIPE_TO_REFRESH);
        } else {
            mVacancyView.hideRefreshProgressBar();
        }
    }

    @Override
    public void handleLoadingBars(int loadingAction) {
        switch (loadingAction) {
            case NORMAL_LOADING:
                mVacancyView.showProgressBar();
                break;
            case SWIPE_TO_REFRESH:
                //When the layout is swiped to refresh, it shows its own progress bar, so no need to implement one
                break;
            case LOAD_MORE:
                //Recycler Adapter shows its own progress bar when the scrolling hits the end and find there are more items to load
                break;
        }
    }

    @Override
    public void checkForLoading() {
        if (mIsLoading && !hasRetrievedDataBefore) {
            mVacancyView.showProgressBar();
        } else {
            mVacancyView.hideAllProgressBars();
        }
    }

    @Override
    public void attachView(VacanciesContract.VacancyView vacancyView, int categoryId) {
        mVacancyView = vacancyView;
        mCategoryId = categoryId;

        //Default Id represents non categorized vacancies
        //If categoryId is not the default, it means that those are categorized, so we hide "browse categories" layout
        if (categoryId != NO_CATEGORY_ID) {
            mVacancyView.hideBrowseCategoriesLayout();
        }
    }

    @Override
    public void detachView() {
        mVacancyView = null;
    }

    @Override
    public void releaseResources() {
        mVacancyModel.detachListeners();
    }

    @Override
    public void onFetchVacancies(List<Vacancy> vacancies, boolean hasMoreToLoad) {
        mHasMoreToLoad = hasMoreToLoad;
        //Sometimes, the data is retrieved after the view has already been destroyed (like when screen rotates or we navigate to previous activity),
        //So, we should make sure the view is not null when the callback takes place
        if (mVacancyView != null) {
            if (vacancies.size() > 0 || hasRetrievedDataBefore) {
                hasRetrievedDataBefore = true;
                mVacancyView.showVacanciesRecycler();
                mVacancyView.updateVacanciesRecycler(vacancies);
            } else {
                mVacancyView.showNoDataLayout();
            }
            mVacancyView.hideAllProgressBars();
            mIsLoading = false;
        }
    }

    @Override
    public void onFailed(String errorMsg) {
        mVacancyView.hideAllProgressBars();
        mVacancyView.showErrorToast(errorMsg);
        mIsLoading = false;
    }
}
