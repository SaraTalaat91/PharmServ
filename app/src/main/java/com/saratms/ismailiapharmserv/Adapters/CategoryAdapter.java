package com.saratms.ismailiapharmserv.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.saratms.ismailiapharmserv.Activities.VacanciesActivity;
import com.saratms.ismailiapharmserv.Models.Category;
import com.saratms.ismailiapharmserv.R;
import com.saratms.ismailiapharmserv.Utilities.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VacancyViewHolder> {
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_IMAGE_PATH = "category_image_path";
    public static final String CATEGORY_NAME = "category_name";
    Context mContext;
    List<Category> mCategoryList;

    public CategoryAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public VacancyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new VacancyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VacancyViewHolder holder, int position) {
        Category currentCategory = mCategoryList.get(position);
        holder.categoryTitleTextView.setText(currentCategory.getCategoryName());
        GlideApp.with(mContext).load(currentCategory.getCategoryImagePath()).transition(DrawableTransitionOptions.withCrossFade()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.categoryTitleTextView.setVisibility(View.GONE);
                holder.imageLoadingProgressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.categoryTitleTextView.setVisibility(View.VISIBLE);
                holder.imageLoadingProgressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.categoryImageView);
    }

    @Override
    public int getItemCount() {
        if (mCategoryList != null)
            return mCategoryList.size();
        else return 0;
    }

    class VacancyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.category_image_view)
        ImageView categoryImageView;
        @BindView(R.id.category_title_text_view)
        TextView categoryTitleTextView;
        @BindView(R.id.image_loading_progress_bar)
        ProgressBar imageLoadingProgressBar;
        @BindView(R.id.category_item)
        RelativeLayout categoryItem;

        public VacancyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, VacanciesActivity.class);
            Category currentCategory = mCategoryList.get(getAdapterPosition());

            handleDataTransition(currentCategory, intent);

            View sharedView = categoryItem;
            handleAnimatedTransition(intent, sharedView);
        }
    }

    private void handleAnimatedTransition(Intent intent, View sharedView) {
        String transitionName = mContext.getString(R.string.shared_element);
        ActivityOptions transitionActivityOptions = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, sharedView, transitionName);
            mContext.startActivity(intent, transitionActivityOptions.toBundle());
        } else {
            mContext.startActivity(intent);
        }
    }

    private void handleDataTransition(Category category, Intent intent) {
        String categoryId = category.getCategoryId();
        String categoryImagePath = category.getCategoryImagePath();
        String categoryName = category.getCategoryName();
        intent.putExtra(CATEGORY_ID, categoryId);
        intent.putExtra(CATEGORY_IMAGE_PATH, categoryImagePath);
        intent.putExtra(CATEGORY_NAME, categoryName);
    }

    public void setCategoriesList(List<Category> categoryList) {
        mCategoryList = categoryList;
    }
}
