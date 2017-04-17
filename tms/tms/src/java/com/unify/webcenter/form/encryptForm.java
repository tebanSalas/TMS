/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unify.webcenter.form;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;
/**
 *
 * @author KARLA CARDENAS
 */
public class encryptForm extends ValidatorActionForm {
 private String operation;
 private int account;
 private String accountName;
              private String fromPage;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFromPage() {
        return fromPage;
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
   
}
