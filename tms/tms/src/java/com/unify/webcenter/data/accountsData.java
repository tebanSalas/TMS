/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.data;

import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.tools.StringManipulator;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author MARCELA QUINTERO
 */
public class accountsData extends mainData implements java.lang.Comparable {

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
        private String key_encriptation;
    private String company;
    private String creator;
    private String website;
    private String active;
    private Timestamp final_trial_date;
    private String main_url;

    //Metodo de comparacion para efectos de hacer un ordenamiento por nombre default
    public int compareTo(Object obj) {
        return this.getName().compareTo(((membersData) obj).getname());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAccount_logo() {
        return account_logo;
    }

    public void setAccount_logo(String account_logo) {
        this.account_logo = account_logo;
    }

    public String getPath(String account) {
        String relpath = "";
        String separator = java.io.File.separator;

        // Get the URL to the upload dir        
        String subdir = TMSConfigurator.getDownloadPath();
        // Si el string no termina con el separador de files, se agrgega
        if (!subdir.endsWith(separator)) {
            subdir = subdir + separator;
        }

        String[] splitSub = subdir.split("ACC");
        if (splitSub.length > 1) {
            subdir = splitSub[0] + "ACC" + account + separator;
        }

      
        // delete each file entry in DB and your file asociated.
        String fileName = "";

        fileName = getAccount_logo();
        // Removemos cualquier caracter invalido
        fileName = StringManipulator.generateValidFilename(fileName);
        // make path
        relpath = subdir + fileName;

        return relpath;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getKey_encriptation() {
        return key_encriptation;
    }

    public void setKey_encriptation(String key_encriptation) {
        this.key_encriptation = key_encriptation;
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
