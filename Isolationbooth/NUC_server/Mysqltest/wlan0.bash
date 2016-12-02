#!/bin/bash

ifconfig wlan0 down
iwconfig wlan0 mode monitor
ifconfig wlan0 up

while true; do
	iwconfig wlan0 channel 1
	if [ $? -ne 0 ] 
		then
		exec bash wlan0.bash
	fi

	tcpdump -i wlan0 -e >> snif0.txt &
	sleep 2.0
	kill $!

	iwconfig wlan0 channel 6
	if [ $? -ne 0 ] 
		then
		exec bash wlan0.bash
	fi

	tcpdump -i wlan0 -e >> snif0.txt &
	sleep 2.0
	kill $!

	iwconfig wlan0 channel 11
	if [ $? -ne 0 ] 
		then
		exec bash wlan0.bash
	fi

	tcpdump -i wlan0 -e >> snif0.txt &
	sleep 2.0
	kill $!
	
	java -classpath ".:mysql-connector-java-5.1.39-bin.jar" parse0
done
