package com.saratms.ismailiapharmserv.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.saratms.ismailiapharmserv.Models.Vacancy;
import com.saratms.ismailiapharmserv.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saratms.ismailiapharmserv.Fragments.VacanciesFragment.VACANCY_INTENT_EXTRA;

public class VacancyDetailsActivity extends AppCompatActivity {
    @BindView(R.id.detail_title_text_view)
    TextView vacancyTitleTextView;
    @BindView(R.id.detail_company_name_tv)
    TextView companyNameTextView;
    @BindView(R.id.detail_created_at_tv)
    TextView createdAtTextView;
    @BindView(R.id.detail_address_text_view)
    TextView companyAddressTextView;
    @BindView(R.id.detail_phone_text_view)
    TextView companyPhoneTextView;
    @BindView(R.id.detail_desc_text_view)
    TextView vacancyDescTextView;
    @BindView(R.id.detail_work_hours_text_view)
    TextView workingHoursTextView;
    @BindView(R.id.detail_salary_text_view)
    TextView salaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_details);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Vacancy vacancy = getIntent().getParcelableExtra(VACANCY_INTENT_EXTRA);
        if (vacancy != null) {
            updateUI(vacancy);
        }
    }

    private void updateUI(Vacancy vacancy) {
        if (Locale.getDefault().getLanguage() == "ar") {
            setArabicValues(vacancy);
        } else {
            setEnglishValues(vacancy);
        }
    }

    private void setEnglishValues(Vacancy vacancy) {
        vacancyTitleTextView.setText(vacancy.getJobTitleEN());
        companyNameTextView.setText(vacancy.getCompanyNameEN());
        createdAtTextView.setText(DateUtils.getRelativeTimeSpanString(vacancy.getCreatedAt()));
        companyAddressTextView.setText(vacancy.getAddressEN());
        companyPhoneTextView.setText(vacancy.getCompanyTel());
        vacancyDescTextView.setText(vacancy.getJobDescEN());
        workingHoursTextView.setText(vacancy.getWorkingHoursEN());
        salaryTextView.setText(vacancy.getSalaryEN());
    }

    private void setArabicValues(Vacancy vacancy) {
        vacancyTitleTextView.setText(vacancy.getJobTitleAR());
        companyNameTextView.setText(vacancy.getCompanyNameAR());
        createdAtTextView.setText(DateUtils.getRelativeTimeSpanString(vacancy.getCreatedAt()));
        companyAddressTextView.setText(vacancy.getAddressAR());
        companyPhoneTextView.setText(vacancy.getCompanyTel());
        vacancyDescTextView.setText(vacancy.getJobDescAR());
        workingHoursTextView.setText(vacancy.getWorkingHoursAR());
        salaryTextView.setText(vacancy.getSalaryAR());
    }

    @OnClick(R.id.call_now_text_view)
    public void makeCall() {
        Uri phoneNumber = Uri.parse("tel:" + companyPhoneTextView.getText().toString());
        Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
        startActivity(intent);
    }

    @OnClick(R.id.back_arrow_image_view)
    public void goBack() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}
