package com.saratms.ismailiapharmserv.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sarah Al-Shamy on 11/06/2019.
 */

public class Vacancy implements Parcelable {

    private String vacancyId;
    private int mCategoryId;
    private String mJobTitleEN;
    private String mJobTitleAR;
    private String mCompanyNameEN;
    private String mCompanyNameAR;
    private String mAddressEN;
    private String mAddressAR;
    private String mCompanyTel;
    private String mJobDescEN;
    private String mJobDescAR;
    private String mWorkingHoursEN;
    private String mWorkingHoursAR;
    private String mSalaryEN;
    private String mSalaryAR;
    private long mCreatedAt;

    public Vacancy() {

    }

    public Vacancy(int categoryId, String jobTitleEN, String jobTitleAR, String companyNameEN, String companyNameAR,
                   String addressEN, String addressAR, String tel, String descEN, String descAR, String workingHoursEN,
                   String workingHoursAR, String salaryEN, String salaryAR) {

        mCategoryId = categoryId;
        mJobTitleEN = jobTitleEN;
        mJobTitleAR = jobTitleAR;
        mCompanyNameEN = companyNameEN;
        mCompanyNameAR = companyNameAR;
        mAddressEN = addressEN;
        mAddressAR = addressAR;
        mCompanyTel = tel;
        mJobDescEN = descEN;
        mJobDescAR = descAR;
        mWorkingHoursEN = workingHoursEN;
        mWorkingHoursAR = workingHoursAR;
        mSalaryEN = salaryEN;
        mSalaryAR = salaryAR;
    }

    protected Vacancy(Parcel in) {
        vacancyId = in.readString();
        mCategoryId = in.readInt();
        mJobTitleEN = in.readString();
        mJobTitleAR = in.readString();
        mCompanyNameEN = in.readString();
        mCompanyNameAR = in.readString();
        mAddressEN = in.readString();
        mAddressAR = in.readString();
        mCompanyTel = in.readString();
        mJobDescEN = in.readString();
        mJobDescAR = in.readString();
        mWorkingHoursEN = in.readString();
        mWorkingHoursAR = in.readString();
        mSalaryEN = in.readString();
        mSalaryAR = in.readString();
        mCreatedAt = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vacancyId);
        dest.writeInt(mCategoryId);
        dest.writeString(mJobTitleEN);
        dest.writeString(mJobTitleAR);
        dest.writeString(mCompanyNameEN);
        dest.writeString(mCompanyNameAR);
        dest.writeString(mAddressEN);
        dest.writeString(mAddressAR);
        dest.writeString(mCompanyTel);
        dest.writeString(mJobDescEN);
        dest.writeString(mJobDescAR);
        dest.writeString(mWorkingHoursEN);
        dest.writeString(mWorkingHoursAR);
        dest.writeString(mSalaryEN);
        dest.writeString(mSalaryAR);
        dest.writeLong(mCreatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vacancy> CREATOR = new Creator<Vacancy>() {
        @Override
        public Vacancy createFromParcel(Parcel in) {
            return new Vacancy(in);
        }

        @Override
        public Vacancy[] newArray(int size) {
            return new Vacancy[size];
        }
    };

    public String getJobTitleEN() {
        return mJobTitleEN;
    }

    public void setJobTitleEN(String jobTitleEN) {
        mJobTitleEN = jobTitleEN;
    }

    public String getJobTitleAR() {
        return mJobTitleAR;
    }

    public void setJobTitleAR(String jobTitleAR) {
        mJobTitleAR = jobTitleAR;
    }

    public String getCompanyNameEN() {
        return mCompanyNameEN;
    }

    public void setCompanyNameEN(String companyNameEN) {
        mCompanyNameEN = companyNameEN;
    }

    public String getCompanyNameAR() {
        return mCompanyNameAR;
    }

    public void setCompanyNameAR(String companyNameAR) {
        mCompanyNameAR = companyNameAR;
    }

    public String getAddressEN() {
        return mAddressEN;
    }

    public void setAddressEN(String addressEN) {
        mAddressEN = addressEN;
    }

    public String getAddressAR() {
        return mAddressAR;
    }

    public void setAddressAR(String addressAR) {
        mAddressAR = addressAR;
    }

    public String getCompanyTel() {
        return mCompanyTel;
    }

    public void setCompanyTel(String companyTel) {
        mCompanyTel = companyTel;
    }

    public String getJobDescEN() {
        return mJobDescEN;
    }

    public void setJobDescEN(String jobDescEN) {
        mJobDescEN = jobDescEN;
    }

    public String getJobDescAR() {
        return mJobDescAR;
    }

    public void setJobDescAR(String jobDescAR) {
        mJobDescAR = jobDescAR;
    }

    public String getWorkingHoursEN() {
        return mWorkingHoursEN;
    }

    public void setWorkingHoursEN(String workingHoursEN) {
        mWorkingHoursEN = workingHoursEN;
    }

    public String getWorkingHoursAR() {
        return mWorkingHoursAR;
    }

    public void setWorkingHoursAR(String workingHoursAR) {
        mWorkingHoursAR = workingHoursAR;
    }

    public String getSalaryEN() {
        return mSalaryEN;
    }

    public void setSalaryEN(String salaryEN) {
        mSalaryEN = salaryEN;
    }

    public String getSalaryAR() {
        return mSalaryAR;
    }

    public void setSalaryAR(String salaryAR) {
        mSalaryAR = salaryAR;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public String getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(String vacancyId) {
        this.vacancyId = vacancyId;
    }

}
