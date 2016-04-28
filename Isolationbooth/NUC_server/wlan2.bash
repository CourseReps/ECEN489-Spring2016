#!/bin/bash

ifconfig wlan3 down
iwconfig wlan3 mode monitor
ifconfig wlan3 up

while true; do
	iwconfig wlan3 channel 1
	if [ $? -ne 0 ] 
		then
		exec bash wlan3.bash
	fi

	tcpdump -i wlan3 -e >> snif2.txt &
	sleep 2.0
	kill $!

	iwconfig wlan3 channel 6
	if [ $? -ne 0 ] 
		then
		exec bash wlan3.bash
	fi

	tcpdump -i wlan3 -e >> snif2.txt &
	sleep 2.0
	kill $!

	iwconfig wlan3 channel 11
	if [ $? -ne 0 ] 
		then
		exec bash wlan3.bash
	fi

	tcpdump -i wlan3 -e >> snif2.txt &
	sleep 2.0
	kill $!
	
	java -classpath ".:sqlite-jdbc-3.8.11.2.jar" parse2
done
