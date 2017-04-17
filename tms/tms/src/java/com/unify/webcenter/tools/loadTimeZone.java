/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author MARCELA QUINTERO
 */
public class loadTimeZone {

    ArrayList _timeZoneList;

    public loadTimeZone() {
        _timeZoneList = new ArrayList();
    }

    public ArrayList loadValues() {
        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; i++) {
            TimeZone timeZone = TimeZone.getTimeZone(ids[i]);
            if (!timeZone.getID().startsWith("Etc/") && !timeZone.getID().startsWith("System")
                  && timeZone.getID().indexOf("/")>1  ) {

                _timeZoneList.add(timeZone.getID());
            }
            Collections.sort(_timeZoneList);            
        }
        return _timeZoneList;
    }
}

