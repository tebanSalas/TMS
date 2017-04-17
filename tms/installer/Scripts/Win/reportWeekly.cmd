:: INFORME LABORES TMS
:: Reporte Anual TMS
:: Modificado por: Alberto Campos Sanchez 20110329
:: Uso: Debe ejecutarse con el usuario donde se instal el Applicacion Server
::      Usar el java del Applicacion Server

::Variables a definir

set JAVA_HOME=C:\archiv~1\Java\jdk1.6.0_24
set APP_HOME=C:\glassfish
set INSTANCE_NAME=domains\domain1\applications\j2ee-modules
set APP_NAME=tms
set PATH_LOGS=C:\glassfish\scripts_tms_sfs\logs\indexDocuments_tms.log
::Fin Variables a definir

set RUTA=%APP_HOME%\%INSTANCE_NAME%\%APP_NAME%\WEB-INF\lib

set JAR="";
set _JAR=%JAR%

set JAR=C:\glassfish\domains\domain1\applications\j2ee-modules\tms\WEB-INF\lib

::Se enlistan todos los .jar de la ruta
for %%i in ( %RUTA%\*.jar ) do call append.cmd %%~fsi
	if "%_JAR%" == "" goto 
END

set JAR=%_JAR%;%JAR% 

set CLASSV=com.unify.webcenter.crons.sendReportWeekly
set LASTV=%JAR% %CLASSV% 

%JAVA_HOME%\bin\java -cp %APP_HOME%\%INSTANCE_NAME%\%APP_NAME%\WEB-INF\classes;%LASTV% 

::Se inicializa el Glassfish
%GLASSFISH% start-domain domain1
