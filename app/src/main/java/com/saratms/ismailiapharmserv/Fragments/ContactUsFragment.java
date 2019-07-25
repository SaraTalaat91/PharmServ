package com.saratms.ismailiapharmserv.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.saratms.ismailiapharmserv.Activities.VacancyFormActivity;
import com.saratms.ismailiapharmserv.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Sarah Al-Shamy on 28/04/2019.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Element versionElement = new Element();
        versionElement.setTitle(getString(R.string.application_version));

        Element licenseElement = new Element();
        licenseElement.setTitle(getString(R.string.licenses_title)).setOnClickListener(this);

        Element fillFormElement = new Element();
        fillFormElement.setIconDrawable(R.drawable.ic_form_24dp).setIconTint(R.color.colorAccent);
        fillFormElement.setTitle(getString(R.string.fill_form_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VacancyFormActivity.class);
                startActivity(intent);
            }
        });


        View aboutPage = new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.nqaba_logo_small)
                .setDescription(getString(R.string.about_app))
                .addGroup(getString(R.string.post_job_text))
                .addItem(fillFormElement)
//                .addEmail("")
//                .addPlayStore("")
                .addItem(versionElement)
                .addItem(licenseElement)
                .create();

        return  aboutPage;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), OssLicensesMenuActivity.class));
        OssLicensesMenuActivity.setActivityTitle(getString(R.string.licenses_title));
    }
}
