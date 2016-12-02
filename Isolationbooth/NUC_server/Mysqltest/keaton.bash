#!/bin/bash

java -classpath ".:mysql-connector-java-5.1.39-bin.jar" SQLiteJDBC
A=$?
echo "Exit code: $A"
exit 0
