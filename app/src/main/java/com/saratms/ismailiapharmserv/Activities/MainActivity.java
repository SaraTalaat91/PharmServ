package com.saratms.ismailiapharmserv.Activities;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.saratms.ismailiapharmserv.Adapters.MainPagerAdapter;
import com.saratms.ismailiapharmserv.Contracts.MainContract;
import com.saratms.ismailiapharmserv.Fragments.VacanciesFragment;
import com.saratms.ismailiapharmserv.Presenters.MainPresenterImp;
import com.saratms.ismailiapharmserv.R;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.MainView, ViewPager.OnPageChangeListener {
    public static final int VACANCIES_FRAGMENT = 0;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner_image_view)
    ImageView bannerImageView;

    MainContract.MainPresenter mainPresenter;
    Stack<Integer> mFragmentsStack;
    boolean shouldSaveToStack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mFragmentsStack = new Stack<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setupPresenter();
    }

    @Override
    public void setupPresenter() {
        mainPresenter = new MainPresenterImp();
        mainPresenter.attachView(this, getSupportFragmentManager());
    }

    @Override
    public void updateUI(MainPagerAdapter mainPagerAdapter) {
        mViewPager.setAdapter(mainPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText(getString(R.string.tab_text_1));
        mTabLayout.getTabAt(1).setText(getString(R.string.tab_text_2));
        mTabLayout.getTabAt(2).setText(getString(R.string.tab_text_3));
        mFragmentsStack.push(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                animateTransition(R.drawable.pharmacist_final);
                break;
            case 1:
                animateTransition(R.drawable.med_banner);
                break;
            case 2:
                animateTransition(R.drawable.contact_banner);
                break;
        }

        //In case we navigated to this fragment by pressing back, we set this boolean to false to avoid pushing it to stack
        if (shouldSaveToStack) {
            if (mFragmentsStack.contains(position)) {
                mFragmentsStack.remove(mFragmentsStack.indexOf(position));
            }
            mFragmentsStack.push(position);
        } else {
            shouldSaveToStack = true;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void animateTransition(int imageResId) {
        Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        animFadeOut.reset();
        bannerImageView.clearAnimation();
        bannerImageView.startAnimation(animFadeOut);

        bannerImageView.setImageResource(imageResId);

        animFadeIn.reset();
        bannerImageView.clearAnimation();
        bannerImageView.startAnimation(animFadeIn);
    }

    @Override
    protected void onDestroy() {
        mainPresenter.detachView();
        bannerImageView.clearAnimation();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentsStack.size() > 1) {
            shouldSaveToStack = false; //to avoid adding the fragment we navigate back to, to the back stack
            mFragmentsStack.pop();
            mViewPager.setCurrentItem(mFragmentsStack.lastElement());
        } else if (mFragmentsStack.size() == 1 && mViewPager.getCurrentItem() == VACANCIES_FRAGMENT) {
            //In this case we reset the scroll position to top, but if it was the case already, finish the application
            MainPagerAdapter mainPagerAdapter = (MainPagerAdapter) mViewPager.getAdapter();
            VacanciesFragment vacanciesFragment = (VacanciesFragment) mainPagerAdapter.getItem(VACANCIES_FRAGMENT);
            boolean hasScrolled = vacanciesFragment.scrollToTop();
            if (!hasScrolled) { //meaning it didn't scroll because it was already on top
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
