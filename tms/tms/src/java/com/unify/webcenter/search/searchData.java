/*
 * searchData.java
 *
 * Created on March 11, 2003, 9:30 PM
 */

package com.unify.webcenter.search;


/**
 * Clase que representa el resultado de una busqueda
 * @author  Gerardo Arroyo Arce
 */
public class searchData implements java.lang.Comparable {
    private float score;
    private long id;
    private int  userId;
    private String header;
    private String summary;
    private String category;
    
    /** Creates a new instance of searchData */
    public searchData(long id, int userId, float score, String category,
        String header, String summary) {
        this.id = id;
        this.userId = userId;
        this.score = score;
        this.header = header;
        this.summary = summary;
        this.category = category;
    }
    
    public float getScore() { return score; }
    public long  getId() { return id; }
    public String getHeader() { return header; }
    public String getSummary() { return summary; }
    public String getCategory() { return category; }
    public int    getUserId() { return userId; }
    
    
    
    /** Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.<p>
     *
     * In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i>
     * is negative, zero or positive.
     *
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)<p>
     *
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.<p>
     *
     * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.<p>
     *
     * It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * @param   o the Object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     * 		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this Object.
     *
     */
    public int compareTo(Object o) {
        // se asume que son iguales.
        int val = 0;
        
        searchData data = (searchData) o;
        if (this.score < data.getScore())
            val = 2;
        else if (this.score > data.getScore())
            val = -1;
        
        
        return val;
    }
    
}
