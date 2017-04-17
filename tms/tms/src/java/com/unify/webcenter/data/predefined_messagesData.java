/*
 * predefined_messagesData.java
 *
 * Created on January 30, 2003, 11:47 AM
 */

package com.unify.webcenter.data;

/**
 * Class that represent the predefined_messages table
 * @author  Gerardo Arroyo Arce
 */
public class predefined_messagesData extends mainData {
    private int id;
    private String name;
   // private char[] text;
    private String text;
                
    /** Creates a new instance of predefined_messagesData */
    public predefined_messagesData() {
    }
    
    public void setid(int val) { id = val; }
    public void setname(String val) { name = val; }
   /* public void settext(char[] val) { text = val; }
    public void settext(String val) { text = val.toCharArray(); }
*/
     public void settext(String val) { text = val; }
    public int getid() { return id; }
    public String getname() { return name; }
    //public char[] gettext() { return text; }
    public String gettext() { return text; }

}
