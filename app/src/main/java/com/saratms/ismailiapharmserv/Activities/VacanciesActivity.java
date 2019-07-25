package com.saratms.ismailiapharmserv.Activities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.saratms.ismailiapharmserv.Fragments.VacanciesFragment;
import com.saratms.ismailiapharmserv.R;
import com.saratms.ismailiapharmserv.Utilities.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.saratms.ismailiapharmserv.Adapters.CategoryAdapter.CATEGORY_ID;
import static com.saratms.ismailiapharmserv.Adapters.CategoryAdapter.CATEGORY_IMAGE_PATH;
import static com.saratms.ismailiapharmserv.Adapters.CategoryAdapter.CATEGORY_NAME;

/**
 * Created by Sarah Al-Shamy on 15/06/2019.
 */

public class VacanciesActivity extends AppCompatActivity {

    @BindView(R.id.category_image_view)
    ImageView categoryImageView;
    @BindView(R.id.category_title_text_view)
    TextView categoryTextView;
    @BindView(R.id.image_loading_progress_bar)
    ProgressBar imageLoadingProgressBar;

    public static final String VACANCY_FRAGMENT_TAG = "vacancy_fragment_tag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setupBannerData();
        setupVacanciesFragment(savedInstanceState);
    }

    private void setupVacanciesFragment(Bundle savedInstanceState) {
        String categoryId = getIntent().getStringExtra(CATEGORY_ID);
        //On configuration changes, the fragment created is not destroyed, just detached
        //So to avoid creating many unnecessary instances, we check if there's already one created
        VacanciesFragment vacanciesFragment = (VacanciesFragment) getSupportFragmentManager().findFragmentByTag(VACANCY_FRAGMENT_TAG);
        if (vacanciesFragment == null) {
            vacanciesFragment = new VacanciesFragment();
            vacanciesFragment.setCategoryId(Integer.valueOf(categoryId));
            getSupportFragmentManager().beginTransaction().add(R.id.vacancy_container, vacanciesFragment, VACANCY_FRAGMENT_TAG).commit();
        } else{
            vacanciesFragment.setCategoryId(Integer.valueOf(categoryId));
            getSupportFragmentManager().beginTransaction().replace(R.id.vacancy_container, vacanciesFragment, VACANCY_FRAGMENT_TAG).commit();
        }
    }

    private void setupBannerData() {
        String categoryImageUri = getIntent().getStringExtra(CATEGORY_IMAGE_PATH);
        String categoryTitle = getIntent().getStringExtra(CATEGORY_NAME);
        displayBannerData(categoryTitle, categoryImageUri);
    }

    private void displayBannerData(String categoryTitle, String categoryImageUri) {
        loadBannerImage(categoryImageUri);
        categoryTextView.setText(categoryTitle);
    }

    private void loadBannerImage(String uri) {
        GlideApp.with(this).load(uri).transition(DrawableTransitionOptions.withCrossFade()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                categoryTextView.setVisibility(View.GONE);
                imageLoadingProgressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                categoryTextView.setVisibility(View.VISIBLE);
                imageLoadingProgressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(categoryImageView);
    }
}
