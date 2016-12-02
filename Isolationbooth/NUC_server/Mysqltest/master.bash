#!/bin/bash

./wlan0.bash > /dev/null &
./wlan1.bash > /dev/null &
./wlan2.bash > /dev/null &

#java -classpath ".:sqlite-jdbc-3.8.11.2.jar" SQLiteJDBC

exit 1
