/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.form;

import com.unify.webcenter.data.applicationData;
import com.unify.webcenter.data.versionData;
import org.apache.struts.validator.ValidatorActionForm;

/**
 *
 * @author esper
 */
public class versionAppForm extends ValidatorActionForm{
    private int id;
    private versionData parentVersion;
    private applicationData parentApplication;
    private int id_version;
    private int id_application;
    private String operation;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public versionData getParentVersion() {
        return parentVersion;
    }

    public void setParentVersion(versionData parentVersion) {
        this.parentVersion = parentVersion;
    }

    public applicationData getParentApplication() {
        return parentApplication;
    }

    public void setParentApplication(applicationData parentApplication) {
        this.parentApplication = parentApplication;
    }

    public int getId_version() {
        return id_version;
    }

    public void setId_version(int id_version) {
        this.id_version = id_version;
    }

    public int getId_application() {
        return id_application;
    }

    public void setId_application(int id_application) {
        this.id_application = id_application;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
}
