package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelCompanies implements Serializable{

    private int companyImage;
    private String companyImageUrl;
    private String companyTitle, companyDetail, companyStatus;

//    int companyImage,

    public ModelCompanies() {
    }


    public ModelCompanies(String companyImageUrl, String companyTitle, String companyDetail, String companyStatus) {
        this.companyImageUrl = companyImageUrl;
        this.companyTitle = companyTitle;
        this.companyDetail = companyDetail;
        this.companyStatus = companyStatus;
    }

    public String getCompanyImageUrl() {
        return companyImageUrl;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public String getCompanyDetail() {
        return companyDetail;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }


    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public void setCompanyDetail(String companyDetail) {
        this.companyDetail = companyDetail;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }
}
