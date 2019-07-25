package com.saratms.ismailiapharmserv.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.saratms.ismailiapharmserv.Activities.CategoriesActivity;
import com.saratms.ismailiapharmserv.Activities.VacancyDetailsActivity;
import com.saratms.ismailiapharmserv.Adapters.VacancyAdapter;
import com.saratms.ismailiapharmserv.Contracts.VacanciesContract;
import com.saratms.ismailiapharmserv.Models.Vacancy;
import com.saratms.ismailiapharmserv.Presenters.VacanciesPresenterImp;
import com.saratms.ismailiapharmserv.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class VacanciesFragment extends Fragment implements VacanciesContract.VacancyView, VacancyAdapter.OnVacancyItemClickListener {

    public static final String VACANCY_INTENT_EXTRA = "vacancy";
    @BindView(R.id.vacancies_recycler)
    RecyclerView vacanciesRecycler;
    @BindView(R.id.no_connection_layout)
    RelativeLayout noConnectionLayout;
    @BindView(R.id.vacancies_progress_bar)
    ProgressBar vacanciesProgressBar;
    @BindView(R.id.vacancies_swipe_refresh_layout)
    SwipeRefreshLayout vacanciesSwipeRefreshLayout;
    @BindView(R.id.browse_categories_layout)
    LinearLayout browseCategoriesLayout;
    @BindView(R.id.no_data_layout)
    RelativeLayout noDataAvailableLayout;

    private VacancyAdapter vacancyAdapter;
    private VacanciesContract.VacancyPresenter mVacancyPresenter;
    private LinearLayoutManager mLinearLayoutManager;

    boolean isPreviouslyStopped;

    public static final int NO_CATEGORY_ID = 0;
    private int mCategoryId = NO_CATEGORY_ID;

    //These constants will determine the mode of the progress bar
    public static final int NORMAL_LOADING = 0;
    public static final int SWIPE_TO_REFRESH = 1;
    public static final int LOAD_MORE = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacancy, container, false);
        ButterKnife.bind(this, view);
        setupVacanciesRecycler();
        setupPresenter();

        vacanciesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //When the recycler hits the end,
                if (!recyclerView.canScrollVertically(1)) {
                    mVacancyPresenter.loadMoreVacancies(getContext().getApplicationContext());
                }
            }
        });

        vacanciesSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVacancyPresenter.refreshVacancies(getContext().getApplicationContext());
            }
        });
        Log.d("fragmentvacancy", "onCreateView: fragment recreated");
        return view;
    }

    private void setupVacanciesRecycler() {
        vacancyAdapter = new VacancyAdapter(getContext());
        vacancyAdapter.setVacancyClickListener(this);
        vacanciesRecycler.setAdapter(vacancyAdapter);
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        vacanciesRecycler.setLayoutManager(mLinearLayoutManager);
        vacanciesRecycler.setItemAnimator(null);
    }

    @Override
    public void setupPresenter() {
        if (mVacancyPresenter == null) {
            mVacancyPresenter = new VacanciesPresenterImp();
        }
        mVacancyPresenter.attachView(this, mCategoryId);
        //Passes the application context to check for internet connection
        mVacancyPresenter.requestVacancies(getContext().getApplicationContext(), NORMAL_LOADING);
    }

    @Override
    public void showProgressBar() {
        vacanciesProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAllProgressBars() {
        vacanciesProgressBar.setVisibility(View.GONE);
        vacanciesSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideRefreshProgressBar() {
        vacanciesSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoDataLayout() {
        noDataAvailableLayout.setVisibility(View.VISIBLE);
        noConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideBrowseCategoriesLayout() {
        browseCategoriesLayout.setVisibility(View.GONE);
    }

    @Override
    public void showVacanciesRecycler() {
        noConnectionLayout.setVisibility(View.GONE);
        noDataAvailableLayout.setVisibility(View.GONE);
        vacanciesRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoConnectionLayout() {
        vacanciesRecycler.setVisibility(View.GONE);
        noDataAvailableLayout.setVisibility(View.GONE);
        noConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoConnectionSnack() {
        final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    @Override
    public void showErrorToast(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resetVacancyList() {
        vacancyAdapter.resetVacancies();
    }

    @Override
    public void updateVacanciesRecycler(List<Vacancy> vacancies) {
        vacancyAdapter.addVacancies(vacancies);
    }

    @Override
    public void onDestroy() {
        mVacancyPresenter.detachView();
        mVacancyPresenter.releaseResources();
        super.onDestroy();
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    @OnClick(R.id.browse_categories_tv)
    void transitionToCategories() {
        Intent intent = new Intent(getContext(), CategoriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        //For some unknown reason, when the activity stopped and resumed, the progress bar reshow,
        //even though visibility is set to GONE, so we persist its GONE state by checking from the presenter if there's still
        //data loading, if not hide it
        if (isPreviouslyStopped) {
            mVacancyPresenter.checkForLoading();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isPreviouslyStopped = true;
    }

    @Override
    public void onVacancyItemClick(Vacancy vacancy) {
        Intent intent = new Intent(getContext(), VacancyDetailsActivity.class);
        intent.putExtra(VACANCY_INTENT_EXTRA, vacancy);
        startActivity(intent);
    }

    //The boolean returned.. If a scroll happened, return true.. if not (in case the scroll position was 0 initially), return false..
    public boolean scrollToTop() {
        boolean hasScrolled;
        int recyclerPosition = 0;
        if (mLinearLayoutManager != null && vacancyAdapter != null) {
            if (vacancyAdapter.getItemCount() > 0) //check if there's a list to scroll in the first place
                recyclerPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        }

        if (recyclerPosition == 0) {
            hasScrolled = false;
        } else {
            vacanciesRecycler.smoothScrollToPosition(0);
            hasScrolled = true;
        }
        return hasScrolled;
    }
}
