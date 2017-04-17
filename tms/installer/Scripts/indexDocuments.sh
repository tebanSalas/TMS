#! /bin/sh

# Index Documents
# Indexación de Tareas
# Modificado por: Giovanni Amir Leiton B 20081226
# Uso: Debe ejecutarse con el usuario donde se instala el Applicacion Server
#      Usar el java del Applicacion Server

#Variables a definir
JAVA_HOME=/usr/lib/jvm/jdk1.5.0_14
APP_HOME=/u01/glassfish
INSTANCE_NAME=domains/d01/applications/j2ee-modules
APP_NAME=$1
PATH_LOGS=/home/glassfish/scripts_tms_sfs/logs/indexDocuments_$1.log
#Fin Variables a definir

RUTA=$APP_HOME/$INSTANCE_NAME/$APP_NAME/WEB-INF/lib

EXECPATH=`echo $RUTA/*.jar | tr ' ' ':'`:$CLASSPATH
$JAVA_HOME/bin/java -cp $APP_HOME/$INSTANCE_NAME/$APP_NAME/WEB-INF/classes:$EXECPATH com.unify.webcenter.tools.indexDocuments > $PATH_LOGS

