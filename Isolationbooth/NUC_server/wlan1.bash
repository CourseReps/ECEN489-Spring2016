#!/bin/bash

ifconfig wlan1 down
iwconfig wlan1 mode monitor
ifconfig wlan1 up

while true; do
	iwconfig wlan1 channel 1
	if [ $? -ne 0 ] 
		then
		exec bash wlan1.bash
	fi

	tcpdump -i wlan1 -e >> snif1.txt &
	sleep 2.0
	kill $!

	iwconfig wlan1 channel 6
	if [ $? -ne 0 ] 
		then
		exec bash wlan1.bash
	fi

	tcpdump -i wlan1 -e >> snif1.txt &
	sleep 2.0
	kill $!

	iwconfig wlan1 channel 11
	if [ $? -ne 0 ] 
		then
		exec bash wlan1.bash
	fi

	tcpdump -i wlan1 -e >> snif1.txt &
	sleep 2.0
	kill $!
	
	java -classpath ".:sqlite-jdbc-3.8.11.2.jar" parse1
done
