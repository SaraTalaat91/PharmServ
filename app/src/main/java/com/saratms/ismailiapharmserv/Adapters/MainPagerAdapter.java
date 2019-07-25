package com.saratms.ismailiapharmserv.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragmentsList = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList.size();
    }

    public void addFragment(Fragment fragment){
        mFragmentsList.add(fragment);
    }
}
