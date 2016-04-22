#!/bin/bash

ifconfig wlan2 down
iwconfig wlan2 mode monitor
ifconfig wlan2 up

while true; do
	iwconfig wlan2 channel 1
	if [ $? -ne 0 ] 
		then
		exec bash wlan2.bash
	fi

	tcpdump -i wlan2 -e >> snif2.txt &
	sleep 2.0
	kill $!

	iwconfig wlan2 channel 6
	if [ $? -ne 0 ] 
		then
		exec bash wlan2.bash
	fi

	tcpdump -i wlan2 -e >> snif2.txt &
	sleep 2.0
	kill $!

	iwconfig wlan2 channel 11
	if [ $? -ne 0 ] 
		then
		exec bash wlan2.bash
	fi

	tcpdump -i wlan2 -e >> snif2.txt &
	sleep 2.0
	kill $!
	
	java -classpath ".:sqlite-jdbc-3.8.11.2.jar" parse2
done
