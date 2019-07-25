package com.saratms.ismailiapharmserv.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saratms.ismailiapharmserv.Models.Vacancy;
import com.saratms.ismailiapharmserv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sarah Al-Shamy on 12/06/2019.
 */

public class VacancyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Vacancy> mVacancyList;
    private LayoutInflater mLayoutInflater;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOAD_MORE = 1;
    private OnVacancyItemClickListener mOnVacancyItemClickListener;


    public VacancyAdapter(Context context) {
        mVacancyList = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = mLayoutInflater.inflate(R.layout.vacancy_item, parent, false);
            return new VacancyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOAD_MORE) {
            View view = mLayoutInflater.inflate(R.layout.load_more_layout, parent, false);
            return new LoadMoreViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VacancyViewHolder) {
            Vacancy currentVacancy = mVacancyList.get(position);
            VacancyViewHolder vacancyViewHolder = (VacancyViewHolder) holder;
            if(Locale.getDefault().getLanguage() == "ar"){
                setArabicAttributes(vacancyViewHolder,currentVacancy);
            }else{
                setDefaultAttributes(vacancyViewHolder, currentVacancy);
            }
            if (currentVacancy.getCreatedAt() != 0) {
                vacancyViewHolder.createdAtTextView.setText(DateUtils.getRelativeTimeSpanString(currentVacancy.getCreatedAt()));
            }
        } else if (holder instanceof LoadMoreViewHolder) {
            LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
            loadMoreViewHolder.loadMoreProgressBar.setIndeterminate(true);
        }
    }

    private void setDefaultAttributes(VacancyViewHolder vacancyViewHolder, Vacancy currentVacancy) {
        vacancyViewHolder.vacancyTitleTextView.setText(currentVacancy.getJobTitleEN());
        vacancyViewHolder.companyNameTextView.setText(currentVacancy.getCompanyNameEN());
        vacancyViewHolder.companyAddressTextView.setText(currentVacancy.getAddressEN());
        vacancyViewHolder.workingHoursTextView.setText(currentVacancy.getWorkingHoursEN());
    }

    private void setArabicAttributes(VacancyViewHolder vacancyViewHolder, Vacancy currentVacancy) {
        vacancyViewHolder.vacancyTitleTextView.setText(currentVacancy.getJobTitleAR());
        vacancyViewHolder.companyNameTextView.setText(currentVacancy.getCompanyNameAR());
        vacancyViewHolder.companyAddressTextView.setText(currentVacancy.getAddressAR());
        vacancyViewHolder.workingHoursTextView.setText(currentVacancy.getWorkingHoursAR());
    }



    @Override
    public int getItemCount() {
        if (mVacancyList != null) {
            return mVacancyList.size();
        } else {
            return 0;
        }
    }

    class VacancyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView((R.id.vacancy_title_text_view))
        TextView vacancyTitleTextView;
        @BindView(R.id.company_name_text_view)
        TextView companyNameTextView;
        @BindView(R.id.company_address_text_view)
        TextView companyAddressTextView;
        @BindView(R.id.working_hours_text_view)
        TextView workingHoursTextView;
        @BindView(R.id.created_at_text_view)
        TextView createdAtTextView;

        public VacancyViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Vacancy clickedVacancy = mVacancyList.get(getAdapterPosition());
            mOnVacancyItemClickListener.onVacancyItemClick(clickedVacancy);
        }
    }

    //The viewholder for the load more progress bar that shows at the end of the recycler when there are more items to load
    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loading_more_progress_bar)
        ProgressBar loadMoreProgressBar;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //If last item is equal to null (which indicates there are more items to load), we return the progress bar view type
        //else, we return the vacancy item view type
        return mVacancyList.get(position) == null ? VIEW_TYPE_LOAD_MORE : VIEW_TYPE_ITEM;
    }

    public void addVacancies(List<Vacancy> vacancies) {
        int previousCount = getItemCount();
        if (previousCount != 0) {
            //check if last item in the list is null, if so remove it to hide the load more progress bar and add the new list
            if (mVacancyList.get(previousCount - 1) == null) {
                mVacancyList.remove(previousCount - 1);
            }
        }
        //We start adding new items at the index after last index at the list (which equals to count)
        int startPosition = getItemCount();
        mVacancyList.addAll(vacancies);
        notifyItemRangeInserted(startPosition, vacancies.size());

    }

    public void resetVacancies() {
        mVacancyList.clear();
        notifyDataSetChanged();
    }

    public void setVacancyClickListener(OnVacancyItemClickListener onVacancyItemClickListener){
        mOnVacancyItemClickListener = onVacancyItemClickListener;
    }

    public interface OnVacancyItemClickListener{
        void onVacancyItemClick(Vacancy vacancy);
    }
}
