package com.saratms.ismailiapharmserv.Contracts;

import android.content.Context;

import com.saratms.ismailiapharmserv.Models.Vacancy;

import java.util.List;

/**
 * Created by Sarah Al-Shamy on 11/06/2019.
 */

public interface VacanciesContract {

    interface VacancyModel {
        void resetVacancies();
        void fetchVacancies(int categoryId);
        void detachListeners();
    }

    interface VacancyView {
        void setupPresenter();
        void showProgressBar();
        void hideAllProgressBars();
        void hideRefreshProgressBar();
        void showNoDataLayout();
        void hideBrowseCategoriesLayout();
        void showVacanciesRecycler();
        void showNoConnectionLayout();
        void showNoConnectionSnack();
        void showErrorToast(String errorMsg);
        void resetVacancyList();
        void updateVacanciesRecycler(List<Vacancy> newVacancies);
    }

    interface VacancyPresenter {
        boolean handleConnectionState(Context context);
        void requestVacancies(Context context, int loadingAction);
        void loadMoreVacancies(Context context);
        void refreshVacancies(Context context);
        void handleLoadingBars(int loadingAction);
        void checkForLoading();
        void attachView(VacancyView vacancyView, int categoryId);
        void detachView();
        void releaseResources();
    }

}
