/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.form;

import com.unify.webcenter.data.accountsData;
import com.unify.webcenter.data.organizationsData;
import com.unify.webcenter.data.versionAppData;
import org.apache.struts.validator.ValidatorActionForm;

/**
 *
 * @author esper
 */
public class organization_versionAppForm extends ValidatorActionForm{
    private int id;
    private versionAppData parentVerApp;
    private int id_verapp; 
    private organizationsData parentOrganization;
    private int id_organization;
    private int id_account;
    private String operation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public versionAppData getParentVerApp() {
        return parentVerApp;
    }

    public void setParentVerApp(versionAppData parentVerApp) {
        this.parentVerApp = parentVerApp;
    }

    public int getId_verapp() {
        return id_verapp;
    }

    public void setId_verapp(int id_verapp) {
        this.id_verapp = id_verapp;
    }

    public organizationsData getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(organizationsData parentOrganization) {
        this.parentOrganization = parentOrganization;
    }

    public int getId_organization() {
        return id_organization;
    }

    public void setId_organization(int id_organization) {
        this.id_organization = id_organization;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    
}
