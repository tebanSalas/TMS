/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unify.webcenter.form;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;
/**
 *
 * @author MARCELA QUINTERO
 */
public class adminForm extends ValidatorActionForm {
private int selected;

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
