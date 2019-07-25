package com.saratms.ismailiapharmserv.Models;

/**
 * Created by Sarah Al-Shamy on 06/07/2019.
 */

public class JobOffer {

    private int mCategoryId;
    private String mCompanyName;
    private String mPhone;
    private String mAddress;
    private String mVacancyDetails;
    private long mPostedAt;

    public JobOffer(int categoryId, String companyName, String phone, String address, String vacancyDetails, long postedAt) {

        mCategoryId = categoryId;
        mCompanyName = companyName;
        mPhone = phone;
        mAddress = address;
        mVacancyDetails = vacancyDetails;
        mPostedAt = postedAt;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setVacancyDetails(String vacancyDetails) {
        mVacancyDetails = vacancyDetails;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getVacancyDetails() {
        return mVacancyDetails;
    }

    public void setPostedAt(long postedAt) {
        mPostedAt = postedAt;
    }

    public long getPostedAt(){return mPostedAt;}
}
