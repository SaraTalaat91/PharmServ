package com.saratms.ismailiapharmserv.Contracts;

import android.content.Context;

import com.saratms.ismailiapharmserv.Models.Category;

import java.util.List;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public interface CategoriesContract {

    interface  CategoryModel{
        void fetchCategories();
        void detachListeners();
    }

    interface CategoryView{
        void setupPresenter();
        void showProgressBar();
        void hideAllProgressBars();
        void showNoConnectionLayout();
        void showNoConnectionSnack();
        void showCategoriesRecycler();
        void showErrorToast(String errorMsg);
        void updateCategoriesRecycler(List<Category> vacancyCategories);
    }

    interface CategoryPresenter{
        boolean handleConnectionState(Context context);
        void handleLoadingBars(int loadingAction);
        void requestCategories(Context context,int loadingAction);
        void refreshCategories(Context context);
        void attachView(CategoryView view);
        void detachView();
        void releaseResources();
    }
}
