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
public class empleadosData extends mainData {
    private int id;
    private String name;
    private String firstName;
    private int id_account;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the account
     */
    public int getAccount() {
        return id_account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(int account) {
        this.id_account = account;
    }
    
    
}
