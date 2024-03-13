package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 28/11/17.
 */

public class CustomerInfoPojo {

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("emailId")
    private String emailId;

    @SerializedName("jobId")
    private String jobId;

    @SerializedName("phone1")
    private String phone1;

    @SerializedName("phone2")
    private String phone2;

    @SerializedName("phone3")
    private String phone3;

    @SerializedName("job_activity_name")
    private String jobActivityName;

    @SerializedName("account_name")
    private String accountName;

    @SerializedName("account_type_flag")
    private int accountTypeFlag = 2;

    public String getJobActivityName() {
        return jobActivityName;
    }

    public void setJobActivityName(String jobActivityName) {
        this.jobActivityName = jobActivityName;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String displayCustomerName() {
        if (accountTypeFlag == 1) {
            return accountName + "\n" + customerName;
        } else {
            return customerName;
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountTypeFlag() {
        return accountTypeFlag;
    }

    public void setAccountTypeFlag(int accountTypeFlag) {
        this.accountTypeFlag = accountTypeFlag;
    }
}
