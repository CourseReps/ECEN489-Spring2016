#!/bin/bash

./wlan0.bash > /dev/null &
./wlan1.bash > /dev/null &
./wlan2.bash > /dev/null

exit 1