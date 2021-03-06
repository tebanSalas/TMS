//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.broker;
import com.unify.webcenter.data.*;
import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
/**
* Class that represent the broker for the table calendar
* @author Administrator
*/
public class calendarBroker  extends MainBroker {
	public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
          // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account",new Integer(accountId));
                
		// Query of all the calendar
		Query query = new QueryByCriteria(calendarData.class, criteria);
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);
		// now iterate over the result to print each Service
		return allLines.iterator();
	}
        
           public java.util.Iterator getList(String sortColumnName, String sortOrder) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Query of all the assignments
        Query query = new QueryByCriteria(calendarData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

        //Obtener todo el calendario de un miembro en una fecha especifica 
        public java.util.Iterator getList(int member,java.sql.Timestamp dateTemp,int id,boolean edit, int idAccount) throws PersistenceBrokerException {
		Criteria criteria = new Criteria();
                
                // Se define solo para ese miembro.
                criteria.addEqualTo("member", Integer.valueOf("" + member));
                 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
                //Se descarta que si se trata del mismo eventeo, para el caso de modificar el evento
                if(edit)
                  criteria.addNotEqualTo("id", new Integer(id));
                
                // Se toma la fecha inicial
                Calendar date = Calendar.getInstance();  
                date.setTimeInMillis(dateTemp.getTime());                          
                date.set(Calendar.HOUR, 1);
                date.set(Calendar.MINUTE,0);
                date.set(Calendar.SECOND,0);
                date.set(Calendar.MILLISECOND,01);
                date.set(Calendar.AM_PM, Calendar.AM);  
                criteria.addGreaterOrEqualThan("day", new java.sql.Timestamp(date.getTimeInMillis()));
           
                // Se toma la fecha final
                Calendar endDate = Calendar.getInstance();  
                endDate.setTimeInMillis(dateTemp.getTime());                 
                endDate.set(Calendar.HOUR, 1);
                endDate.set(Calendar.MINUTE,0);
                endDate.set(Calendar.SECOND,1);
                endDate.set(Calendar.MILLISECOND,01);
                endDate.set(Calendar.AM_PM, Calendar.AM); 
                
                // Y menor estricto al siguiente dia
                endDate.add(Calendar.DAY_OF_YEAR,1);   
                criteria.addLessThan("day", new java.sql.Timestamp(endDate.getTimeInMillis()));
                // Query of all the calendar
		Query query = new QueryByCriteria(calendarData.class, criteria);
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);
		// now iterate over the result to print each Service
		return allLines.iterator();
	}
        
      public java.util.Iterator getList(String sortColumnName, String sortOrder, int accountId) throws PersistenceBrokerException {
		// New criteria for sortering
		Criteria criteria = new Criteria();
		// We order the result set
		if (sortOrder.equalsIgnoreCase("ASC"))
			criteria.addOrderByAscending(sortColumnName);
		else
			criteria.addOrderByDescending(sortColumnName);
                 criteria.addEqualTo("id_account",new Integer(accountId));
		// Query of all the calendar
		Query query = new QueryByCriteria(calendarData.class, criteria);
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);
		// now iterate over the result 
		return allLines.iterator();
	}     
        
        
	
	public java.util.ArrayList getListByDate(String sortColumnName, String sortOrder,
            int member, Calendar theDay, int idAccount) throws PersistenceBrokerException {
		// New criteria for sortering
		Criteria criteria = new Criteria();
		// We order the result set
		if (sortOrder.equalsIgnoreCase("ASC"))
			criteria.addOrderByAscending(sortColumnName);
		else
			criteria.addOrderByDescending(sortColumnName);
                // Se define solo para ese miembro.
                criteria.addEqualTo("member", Integer.valueOf("" + member));
                  // Se define solo para ese miembro.
                criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
                      
                // Y para esta fecha.
                Calendar date = (Calendar)theDay.clone();  
                date.set(Calendar.HOUR, 1);
                date.set(Calendar.MINUTE,0);
                date.set(Calendar.SECOND,0);
                date.set(Calendar.MILLISECOND,01);
                date.set(Calendar.AM_PM, Calendar.AM);  
                criteria.addGreaterOrEqualThan("day", new java.sql.Timestamp(date.getTimeInMillis()));
           
                // Se toma la fecha final
                Calendar endDate = (Calendar) date.clone();
                endDate.set(Calendar.HOUR, 1);
                endDate.set(Calendar.MINUTE,0);
                endDate.set(Calendar.SECOND,1);
                endDate.set(Calendar.MILLISECOND,01);
                endDate.set(Calendar.AM_PM, Calendar.AM); 
                
                // Y menor estricto al siguiente dia
                endDate.add(Calendar.DAY_OF_YEAR,1);   
                criteria.addLessThan("day", new java.sql.Timestamp(endDate.getTimeInMillis()));
        
		// Query of all the calendar
		Query query = new QueryByCriteria(calendarData.class, criteria);
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);
                Iterator e = allLines.iterator();         
                ArrayList salida = new ArrayList();     
                while(e.hasNext())
                    salida.add( e.next());      
                
		// now iterate over the result 
		return salida;
	}
	// Return the object associated with the key.
	public com.unify.webcenter.data.mainData getData(int id, int idAccount) {
		calendarData data = new calendarData();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("id", new Integer(id));
                criteria.addEqualTo("id_account", new Integer(idAccount));
		// Query of the exact organization
		Query query = new QueryByCriteria(calendarData.class, criteria);
                
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);
		Iterator e = allLines.iterator();
		// If exists the record -MUST EXISTS ALWAYS
		if (e.hasNext())
			data = (calendarData) e.next();
		// We return the object
		return data;
	}
        
        // Return the object associated with the key.
	public com.unify.webcenter.data.mainData getData(int id) {
		calendarData data = new calendarData();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("id", new Integer(id));
          
		// Query of the exact organization
		Query query = new QueryByCriteria(calendarData.class, criteria);
                
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);
		Iterator e = allLines.iterator();
		// If exists the record -MUST EXISTS ALWAYS
		if (e.hasNext())
			data = (calendarData) e.next();
		// We return the object
		return data;
	}
    /*
     * Metodo que elimina todas las referencias de un member en todos los calendarss.
     */
   public void deleteAllReferences(int memberId, int idAccount) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();
        
        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("member", Integer.valueOf("" + memberId));
                criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(calendarData.class, criteria);
        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);          
        Iterator e = allLines.iterator();
        // Se borra cada entrada
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }        
    /* Devuelve la cantidad de horas de esta tarea con base en el usuario dado */
    public java.math.BigDecimal getTotalTaskEstimatedTime(int taskid, int idMember,
        Timestamp start, Timestamp end, int idAccount) throws PersistenceBrokerException {
            System.out.println("getTotalTaskEstimatedTime");
                      
        // Query of all the schedules
        Criteria criteria = new Criteria();
        criteria.addEqualTo("task", new Integer(taskid));   
        criteria.addEqualTo("member", new Integer(idMember));   
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Restringido en el espacio de fechas dadas.
        criteria.addGreaterOrEqualThan("day", start);
        criteria.addLessOrEqualThan("day", end);   
        ReportQueryByCriteria q = QueryFactory.newReportQuery(calendarData.class, criteria);
        // define the 'columns' of the report
        q.setColumns(new String[] { " sum(hour_end - hour_start), sum(min_end - min_start) "});
        Iterator iter = broker.getReportQueryIteratorByQuery(q);
        Integer totalHoras = new Integer(0);
        Integer totalMin = new Integer(0);
        if (iter.hasNext()) {
        Object[] arr = (Object[]) iter.next();    
            if (arr[0] != null) {
                //totalHoras = new Integer(((java.math.BigDecimal) arr[0]).intValue());
                totalHoras = new Integer(arr[0].toString());
                try {
                    //totalMin = new Integer(((java.math.BigDecimal) arr[1]).intValue());
                    totalMin = new Integer(arr[1].toString());
                } catch (Exception e) {
                    totalMin = new Integer(0);
                }
            }
           /* else if (arr[0] != null) {
                totalHoras = (Integer)new Integer(arr[0].toString());
                totalMin = (Integer) new Integer(arr[1].toString());
            }*/ 
        
            // En caso de que sea nulo el resultado, se regresa 0
            if (totalHoras == null)
                totalHoras = new Integer(0); 
            if (totalMin == null)
                totalMin = new Integer(0);  
        }                 
        // Se toman los minutos y se dividen entre 60, para sumarlos a las horas
        int subTotal = totalHoras.intValue();
        subTotal += totalMin.intValue() / 60;  
        // Se pasa a un string.
        String val = "" + subTotal;    
        
        // Se modifica la variable privada de total
        return new java.math.BigDecimal(val);         
    } 
    /**
     * Metodo que se encarga de regresar la carga de trabajo de un usuario con base en la
     * informacion que el mismo ha registrado en su agenda de trabajo.
     */
    public java.util.ArrayList getLoadByUser(int idMember,
        Timestamp start, Timestamp end, int idAccount) throws PersistenceBrokerException {
      // New criteria for search
        Criteria criteria = new Criteria();
        
        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("member", Integer.valueOf("" + idMember));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Restringido en el espacio de fechas dadas.
        criteria.addGreaterOrEqualThan("day", start);
        criteria.addLessOrEqualThan("day", end);           
        criteria.addOrderByAscending("task");
        
       
        // Query of all the task in the calendar.
        Query query = new QueryByCriteria(calendarData.class, criteria);
        
        // Ejecutamos la consulta para con este usuario.
        Collection allLines = broker.getCollectionByQuery(query);	
        Iterator e = allLines.iterator();      
        
        int oldValue = 0;
        int tiempoAgendado = 0;
        calendarData data = null;
        tasksData dataTask;
        performanceDetail performanceData;
        tasksBroker taskBroker = new tasksBroker();
        Hashtable proyectosHash = new Hashtable();
        String projName= "";
        // Se procesa cada entrada del calendario ordenada por id de tarea
        while(e.hasNext()) {
            data = (calendarData) e.next();
            if ((data.gettask() != oldValue && oldValue != 0)){
                // Es una tarea con un id diferente, se debe guardar en la lista de 
                // tareas procesadas.
                // Se toma la referencia de la tarea.
                dataTask =(tasksData) taskBroker.getData(oldValue,idAccount);                
                dataTask.setCalendarTime(tiempoAgendado);     
                
                if (dataTask.getid() > 0) {
                
                    // Se ve si esta atrasada o no.
                    if (dataTask.getdue_date() == null || dataTask.getdue_date().before(end) &&
                        dataTask.getstatus() == 3) {
                        dataTask.setLate(true);
                    } else {
                        dataTask.setLate(false);                   
                    }
                    // Verificamos si el proyecto al cual pertenece esta en el hashtable.
                    if (proyectosHash.containsKey("" + dataTask.getproject())) {
                        // Se extra el valor del hash para hacer una actualizacion
                        performanceData = (performanceDetail) proyectosHash.get("" + dataTask.getproject());
                        performanceData.addToTaskList(dataTask);
                        proyectosHash.put("" + dataTask.getproject(), performanceData);
                    } else {
                        // No esta en el hash, lo incorporamos.
                        ArrayList lista = new ArrayList();
                        lista.add(dataTask);
                        if (dataTask.getparentProject() != null)
                            projName =dataTask.getparentProject().getname();
                        if (projName == null) projName = "N/A";
                        performanceData = new performanceDetail(lista, dataTask.getestimated_time(), 
                            new BigDecimal(dataTask.getCalendarTime()), new BigDecimal(0), new BigDecimal(0), 
                            projName);
                        proyectosHash.put("" + dataTask.getproject(), performanceData);
                    }
                    // Se actualiza la referencia                
                    tiempoAgendado = 0;
                    
                }
            } 
            
            oldValue = data.gettask();
            
            // Se hace la suma del tiempo agendado TODO: consider minutos.
            tiempoAgendado += Integer.valueOf(data.getHourEnd()).intValue() - 
                    Integer.valueOf(data.getHourStart()).intValue();                

        }
        
      
       
        if (allLines.size() > 0 ) {
            // Se debe consider el ultimo registro que ha sido procesado.
            // ya que no fue incluido en el ciclo anterior
            if (data.gettask() == oldValue ) {
                // El ultimo registro pertenece a una misma tarea que estuvo procesandose
//               tiempoAgendado += Integer.valueOf(data.getHourEnd()).intValue() - 
//                        Integer.valueOf(data.getHourStart()).intValue();                
                // Es una tarea con un id diferente, se debe guardar en la lista de 
                // tareas procesadas.
                // Se toma la referencia de la tarea.
                dataTask =(tasksData) taskBroker.getData(oldValue,idAccount);                
                dataTask.setCalendarTime(tiempoAgendado);     
            }  else {
                dataTask =(tasksData) taskBroker.getData(data.gettask(),idAccount);                
                dataTask.setCalendarTime(Integer.valueOf(data.getHourEnd()).intValue() - 
                        Integer.valueOf(data.getHourStart()).intValue());                 
            }           
                // Se ve si esta atrasada o no.
                if (dataTask.getdue_date().before(end) &&
                    dataTask.getstatus() == 3) {
                    dataTask.setLate(true);
                } else {
                    dataTask.setLate(false);                      
                }
            
            // Verificamos si el proyecto al cual pertenece esta en el hashtable.
            if (proyectosHash.containsKey("" + dataTask.getproject())) {
                // Se extra el valor del hash para hacer una actualizacion
                performanceData = (performanceDetail) proyectosHash.get("" + dataTask.getproject());
                performanceData.addToTaskList(dataTask);
                proyectosHash.put("" + dataTask.getproject(), performanceData);
            } else {
                // No esta en el hash, lo incorporamos.
                ArrayList lista = new ArrayList();
                lista.add(dataTask);
                if (dataTask.getparentProject() != null)
                    projName =dataTask.getparentProject().getname();
                if (projName == null) projName = "N/A";
                performanceData = new performanceDetail(lista, dataTask.getestimated_time(), 
                    new BigDecimal(dataTask.getCalendarTime()), new BigDecimal(0), new BigDecimal(0), 
                    projName);
                proyectosHash.put("" + dataTask.getproject(), performanceData);
            }        
        }
        
        // Se cierran los brokers que se han usado.
        taskBroker.close();
        
        ArrayList salida = new ArrayList();        
        e = proyectosHash.values().iterator();
        
        while (e.hasNext()) {         
            salida.add(e.next());
        }
        return salida;
    }
    
      /*
     * Metodo que elimina todas las referencias de un member en todos los calendarss.
     */
   public void deleteAllReferencesbyAccount(int accountId) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();
        
        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId));
        // Query of all the teams
        Query query = new QueryByCriteria(calendarData.class, criteria);
        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);          
        Iterator e = allLines.iterator();
        // Se borra cada entrada
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }        
  
   
    public void deleteAllReferencesbyTask(int taskId,int accountId) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();
        
        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId));
        criteria.addEqualTo("task", Integer.valueOf("" + taskId));
        // Query of all the teams
        Query query = new QueryByCriteria(calendarData.class, criteria);
        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);          
        Iterator e = allLines.iterator();
        // Se borra cada entrada
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }        
 }