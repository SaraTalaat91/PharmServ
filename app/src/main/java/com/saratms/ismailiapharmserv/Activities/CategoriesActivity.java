package com.saratms.ismailiapharmserv.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.saratms.ismailiapharmserv.Adapters.CategoryAdapter;
import com.saratms.ismailiapharmserv.Contracts.CategoriesContract;
import com.saratms.ismailiapharmserv.Models.Category;
import com.saratms.ismailiapharmserv.Presenters.CategoriesPresenterImp;
import com.saratms.ismailiapharmserv.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.NORMAL_LOADING;

/**
 * Created by Sarah Al-Shamy on 11/06/2019.
 */

public class CategoriesActivity extends AppCompatActivity implements CategoriesContract.CategoryView {

    @BindView(R.id.vacancy_categories_recycler)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.categories_progressbar)
    ProgressBar categoriesProgressBar;
    @BindView(R.id.categories_no_connection_layout)
    RelativeLayout categoriesNoConnectionLayout;
    @BindView(R.id.categories_swipe_refresh_layout)
    SwipeRefreshLayout categoriesSwipeRefreshLayout;

    CategoryAdapter mCategoryAdapter;
    CategoriesContract.CategoryPresenter categoryPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Makes status bar transparent
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setupCategoriesRecycler();
        setupPresenter();

        categoriesSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //This will only refresh categories if they never loaded for some reason, else it does nothing
                categoryPresenter.refreshCategories(getApplicationContext());
            }
        });
    }

    private void setupCategoriesRecycler() {
        mCategoryAdapter = new CategoryAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void setupPresenter() {
        categoryPresenter = (CategoriesContract.CategoryPresenter) getLastCustomNonConfigurationInstance();
        if (categoryPresenter == null) {
            categoryPresenter = new CategoriesPresenterImp();
        }
        categoryPresenter.attachView(this);
        categoryPresenter.requestCategories(getApplicationContext(), NORMAL_LOADING);
    }

    @Override
    public void showProgressBar() {
        categoriesProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAllProgressBars() {
        categoriesProgressBar.setVisibility(View.GONE);
        categoriesSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoConnectionLayout() {
        categoriesRecyclerView.setVisibility(View.GONE);
        categoriesNoConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoConnectionSnack() {
        final Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    @Override
    public void showCategoriesRecycler() {
        categoriesNoConnectionLayout.setVisibility(View.GONE);
        categoriesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorToast(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateCategoriesRecycler(List<Category> vacancyCategories) {
        mCategoryAdapter.setCategoriesList(vacancyCategories);
        categoriesRecyclerView.setAdapter(mCategoryAdapter);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return categoryPresenter;
    }

    @Override
    protected void onDestroy() {
        categoryPresenter.detachView();
        categoryPresenter.releaseResources();
        super.onDestroy();
    }
}
