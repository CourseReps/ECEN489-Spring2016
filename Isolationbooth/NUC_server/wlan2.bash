#!/bin/bash

ifconfig wlan2 down
iwconfig wlan2 mode monitor
ifconfig wlan2 up

while true; do
	iwconfig wlan2 channel 1

	tcpdump -i wlan2 -e >> snif2.txt &
	sleep 2.0
	kill $!

	iwconfig wlan2 channel 6

	tcpdump -i wlan2 -e >> snif2.txt &
	sleep 2.0
	kill $!

	iwconfig wlan2 channel 11

	tcpdump -i wlan2 -e >> snif2.txt &
	sleep 2.0
	kill $!
	
	java parse2
done
