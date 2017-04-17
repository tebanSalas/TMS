/*
 * CustomRenderer.java
 *
 * Created on 28 de febrero de 2005, 09:30 PM
 */

package com.unify.webcenter.charts;

import java.awt.Paint;
import org.jfree.chart.renderer.category.BarRenderer3D;


public class CustomRenderer extends org.jfree.chart.renderer.category.BarRenderer3D
{
   private Paint colors[];
    
    public Paint getItemPaint(int i, int j)
    {
    return colors[j % colors.length];
    }



    public CustomRenderer(Paint apaint[])
    {
    colors = apaint;
    } 
}


