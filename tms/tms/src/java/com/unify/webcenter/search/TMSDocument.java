/*
 * TMSDocument.java
 *
 * Created on March 11, 2003, 10:18 AM
 */

package com.unify.webcenter.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


import com.unify.webcenter.data.*;

/**
 * Clase que representa una entidad de tipo documento del Task Management System
 * @author  Gerardo Arroyo Arce
 */
public class TMSDocument {
    
    /** Creates a new instance of TMSDocument */
    private TMSDocument() {
    }
    
    
      public static Document Document(long id, int userId, String category, String header, String summary, 
        String data) {

        // make a new, empty document
        Document doc = new Document();

        // Add the id as a field named "id".  Use an UnIndexed field, so
        // that the url is just stored with the document, but is not searchable.        
        doc.add(new Field("id", ""+id, Field.Store.YES, Field.Index.NO));        
        
        // Se agrega tambien el header, summary y userId
        doc.add(new Field("header", header, Field.Store.YES, Field.Index.NO));         
        doc.add(new Field("summary", summary, Field.Store.YES, Field.Index.NO));        
        doc.add(new Field("userId", ""+userId, Field.Store.YES, Field.Index.NO));        
        
        // Y la categoria a la cual pertenece
        doc.add(new Field("category", category, Field.Store.YES, Field.Index.NO));         
        // Add the contents of the task to a field named "contents".  Use a Text
        // field, specifying a Reader, so that the text of the file is tokenized.
        doc.add(new Field("contents", data,Field.Store.YES, Field.Index.TOKENIZED));

        // return the document
        return doc;
      }
       
      public static Document Document(long id, String category, String header, String summary, 
        String data) {
            
            // Se ejecuta el metodo anterior.
            return Document(id, 0, category, header, summary, data);
      }      
      
}
