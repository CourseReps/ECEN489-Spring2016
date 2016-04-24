#!/bin/bash

java -classpath ".:sqlite-jdbc-3.8.11.2.jar" SQLiteJDBC
A=$?
echo "Exit code: $A"
exit 0
