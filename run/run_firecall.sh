#!/bin/bash

# --------------------------------------------------------
#
# how to run:
#
#	bash run_firecall.sh start
#	bash run_firecall.sh stop
#
# ---------------------------------------------------------

# --------------------------------------------------------
#
# step #01 getting command code
#
# ---------------------------------------------------------
echo running firecall script
run_code=${fireCall}

# if command is not coming from firecall parameter 
# then getting command from input
if [ "${run_code}" = "" ]; 
then
    echo getting command code from input
    run_code=$1
fi

if [ "${run_code}" = "run" ];
then
   echo processing request to start rad
   run_code="run"  
fi

if [ "${run_code}" = "start" ];
then
   echo processing request to start rad
   run_code="run"
fi

if [ "${run_code}" = "stop" ];
then
   echo processing request to stop rad
   run_code="stop";
fi


# --------------------------------------------------------
#
# step #02 set jar to run, property name and log file
#
# ---------------------------------------------------------
jar_name=${jarName}
if [ "${jar_name}" = "" ]; 
then
	jar_name='rad-0.0.1-SNAPSHOT.jar';
	echo $jar_name;
fi
prop_name='application.properties';
log='rad.log'

# --------------------------------------------------------
#
# step #03 execute
#
# ---------------------------------------------------------
if [ "${run_code}" = "run" ];
then
    #echo "START"

    # delete instance of app if it is running for whatever reason
    PID=$(ps -Af | grep java | grep rad | grep -v netbeans | grep -v grep | awk '{ print $2 }');
    echo $PID

    if  [ "$PID" -eq "$PID" ] 2>/dev/null; then
      kill -9 $PID;
    fi

    # delete previously loaded files
    rm -f $jar_name
    rm -f $prop_name
    rm -f $log

    # geting files
    cp ../target/$jar_name ./
    cp ../target/classes/$prop_name ./

    # run rad
    /usr/bin/java -jar ./$jar_name &
    disown

    # finally
    sleep 10
    echo rad is up and running
fi

if [ "${run_code}" = "stop" ];
then
   #echo "STOP"

   # delete instance of app if it is running for whatever reason
   PID=$(ps -Af | grep java | grep rad | grep -v netbeans | grep -v grep | awk '{ print $2 }');
   echo RAD process to kill $PID

   if  [ "$PID" -eq "$PID" ] 2>/dev/null; 
   then
     kill -9 $PID;
   fi

   # delete previously loaded files
   rm -f $jar_name
   rm -f $prop_name
   rm -f $log
fi