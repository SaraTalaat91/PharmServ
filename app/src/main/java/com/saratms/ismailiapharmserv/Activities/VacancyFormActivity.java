package com.saratms.ismailiapharmserv.Activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.saratms.ismailiapharmserv.Contracts.VacancyFormContract;
import com.saratms.ismailiapharmserv.Presenters.VacancyFormPresenterImp;
import com.saratms.ismailiapharmserv.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarah Al-Shamy on 04/07/2019.
 */

public class VacancyFormActivity extends AppCompatActivity implements VacancyFormContract.VacancyFormView {

    @BindView(R.id.company_name_edit_text)
    EditText companyNameEditText;
    @BindView(R.id.address_edit_text)
    EditText addressEditText;
    @BindView(R.id.phone_number_edit_text)
    EditText phoneNumberEditText;
    @BindView(R.id.vacancy_details_edit_text)
    EditText vacancyDetailsEditText;
    @BindView(R.id.category_spinner)
    Spinner categorySpinner;
    @BindView(R.id.vacancy_form_progress_bar)
    ProgressBar vacancyFormProgressBar;
    @BindView(R.id.submit_form_button)
    Button submitFormButton;

    VacancyFormContract.VacancyFormPresenter mVacancyFormPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_form);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        setupPresenter();
    }

    @Override
    public void setupPresenter() {
        mVacancyFormPresenter = (VacancyFormContract.VacancyFormPresenter) getLastCustomNonConfigurationInstance();
        if (mVacancyFormPresenter == null) {
            mVacancyFormPresenter = new VacancyFormPresenterImp();
        }
        mVacancyFormPresenter.attachView(this);
    }

    @Override
    public void submitEntries() {
        //Because in Firebase, category Ids start from 1
        int categoryId = categorySpinner.getSelectedItemPosition() + 1;

        String companyName = companyNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneNumberEditText.getText().toString().trim();
        String vacancyDetails = vacancyDetailsEditText.getText().toString().trim();

        mVacancyFormPresenter.handleEntries(getApplicationContext(), categoryId, companyName, phone, address, vacancyDetails);
    }

    @Override
    public void showInvalidInputsToast() {
        Toast.makeText(this, R.string.complete_fields_toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerErrorToast(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessToast() {
        Toast.makeText(this, R.string.successful_form_submit_toast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showProgressBar() {
        vacancyFormProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        vacancyFormProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoInternetSnackbar() {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    @Override
    public void enableSubmitButton() {
        submitFormButton.setEnabled(true);
    }

    @OnClick(R.id.submit_form_button)
    void submitJobOffer(View view) {
        submitFormButton.setEnabled(false);
        submitEntries();
    }

    @OnClick(R.id.back_button)
    void navigateBack(View view) {
        finish();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mVacancyFormPresenter;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}
