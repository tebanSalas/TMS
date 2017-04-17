<%@page language="java" import ="com.unify.webcenter.data.*" %>
<%@page language="java" import ="com.unify.webcenter.broker.*" %>
<%@page language="java" import ="com.unify.webcenter.tools.*" %>
<%
            int start = Integer.parseInt(request.getParameter("id"));
            int end = start + 1;

            schedulesBroker brokerSchedule = new schedulesBroker();
            String labelStart = scheduleFormat.hour(1, start);
            String labelEnd = scheduleFormat.hour(1, end);

           // String[] difference = brokerSchedule.getRegisteredTime(labelStart, labelEnd);            
            try {
                String buffer = "<select name='hour_end'>";
                hourData hoursData = new hourData();
                for (int i = start + 1; i < 287; i++) {
                    hoursData = new hourData();
                    buffer = buffer + "<option value='" + i + "'>" + scheduleFormat.hour(1, i) + "</option>";
                }
                buffer = buffer + "</select>";
             /*   buffer= buffer+" <tr class='odd'><td valign='top' class='leftvalue'> $msg.get('common.registeredTime') :</td><td>"
                + difference[0] + "horas - "+difference[1]+" minutos" +
              "</td></tr >";              */
                out.print(buffer);
                
            } catch (Exception e) {
                out.print(e);
            }
%>