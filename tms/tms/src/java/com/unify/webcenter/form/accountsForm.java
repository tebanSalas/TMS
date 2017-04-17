/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorActionForm;

/**
 *
 * @author MARCELA QUINTERO
 */
public class accountsForm extends ValidatorActionForm {

    private String operation;
    private String sortColumn;
    private String sortOrder;
    private String fromPage;
    private int id;
    private String name;
    private String description;
    private String address;
    private String phone_1;
    private String phone_2;
    private String email;
    private String account_logo;
    private BigDecimal user_fare;
    private String account_email;
    private String shortname;
    private Timestamp final_trial_date;
    private String company;
    private String creator;
    private String website;
    private FormFile link;
    private String active;
    private String main_url;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_1() {
        return phone_1;
    }

    public void setPhone_1(String phone_1) {
        this.phone_1 = phone_1;
    }

    public String getPhone_2() {
        return phone_2;
    }

    public void setPhone_2(String phone_2) {
        this.phone_2 = phone_2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_logo() {
        return account_logo;
    }

    public void setAccount_logo(String account_logo) {
        this.account_logo = account_logo;
    }

    public BigDecimal getUser_fare() {
        return user_fare;
    }

    public void setUser_fare(BigDecimal user_fare) {
        this.user_fare = user_fare;
    }

    public String getAccount_email() {
        return account_email;
    }

    public void setAccount_email(String account_email) {
        this.account_email = account_email;
    }

    public String getFromPage() {
        return fromPage;
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public FormFile getLink() {
        return link;
    }

    public void setLink(FormFile link) {
        this.link = link;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Timestamp getFinal_trial_date() {
        return final_trial_date;
    }

    public void setFinal_trial_date(Timestamp final_trial_date) {
        this.final_trial_date = final_trial_date;
    }

    /**
     * @return the main_url
     */
    public String getMain_url() {
        return main_url;
    }

    /**
     * @param main_url the main_url to set
     */
    public void setMain_url(String main_url) {
        this.main_url = main_url;
    }
}
