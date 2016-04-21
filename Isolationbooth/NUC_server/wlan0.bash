#!/bin/bash

ifconfig wlan0 down
iwconfig wlan0 mode monitor
ifconfig wlan0 up

while true; do
	iwconfig wlan0 channel 1

	tcpdump -i wlan0 -e >> snif0.txt &
	sleep 2.0
	kill $!

	iwconfig wlan0 channel 6

	tcpdump -i wlan0 -e >> snif0.txt &
	sleep 2.0
	kill $!

	iwconfig wlan0 channel 11

	tcpdump -i wlan0 -e >> snif0.txt &
	sleep 2.0
	kill $!
	
	time java parse0
done
