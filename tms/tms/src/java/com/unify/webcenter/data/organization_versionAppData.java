/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.data;

/**
 *
 * @author esper
 */
public class organization_versionAppData extends mainData{
    private int id;
    private versionAppData parentVerApp;
    private int id_verApp; 
    private organizationsData parentOranization;
    private int id_organization;
    private int id_account;

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

    public int getId_verApp() {
        return id_verApp;
    }

    public void setId_verApp(int id_verApp) {
        this.id_verApp = id_verApp;
    }

    public organizationsData getParentOranization() {
        return parentOranization;
    }

    public void setParentOranization(organizationsData parentOranization) {
        this.parentOranization = parentOranization;
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

    

    
    
    
}