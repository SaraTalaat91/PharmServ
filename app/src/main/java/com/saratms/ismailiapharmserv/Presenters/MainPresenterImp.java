package com.saratms.ismailiapharmserv.Presenters;

import android.support.v4.app.FragmentManager;

import com.saratms.ismailiapharmserv.Adapters.MainPagerAdapter;
import com.saratms.ismailiapharmserv.Contracts.MainContract;
import com.saratms.ismailiapharmserv.Fragments.ContactUsFragment;
import com.saratms.ismailiapharmserv.Fragments.RareDrugsFragment;
import com.saratms.ismailiapharmserv.Fragments.VacanciesFragment;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class MainPresenterImp implements MainContract.MainPresenter {

    private MainContract.MainView mainView;
    private FragmentManager fragmentManager;
    private MainPagerAdapter mainPagerAdapter;

    @Override
    public void attachView(MainContract.MainView mainView, FragmentManager fragmentManager) {
        this.mainView = mainView;
        this.fragmentManager = fragmentManager;
        setupPagerAdapter();
    }

    @Override
    public void detachView() {
        this.mainView = null;
    }

    @Override
    public void setupPagerAdapter() {
        if(mainPagerAdapter == null){
        mainPagerAdapter = new MainPagerAdapter(fragmentManager);
        mainPagerAdapter.addFragment(new VacanciesFragment());
        mainPagerAdapter.addFragment(new RareDrugsFragment());
        mainPagerAdapter.addFragment(new ContactUsFragment());
        }
        mainView.updateUI(mainPagerAdapter);
    }
}
