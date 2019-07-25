package com.saratms.ismailiapharmserv.Contracts;

import android.support.v4.app.FragmentManager;

import com.saratms.ismailiapharmserv.Adapters.MainPagerAdapter;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public interface MainContract {

    interface MainView {
        void setupPresenter();
        void updateUI(MainPagerAdapter mainPagerAdapter);
    }

    interface MainPresenter{
        void attachView(MainContract.MainView mainView, FragmentManager fragmentManager);
        void detachView();
        void setupPagerAdapter();
    }
}
