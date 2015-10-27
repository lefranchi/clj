#!/bin/sh
#############################################################################################
#
#
#
#############################################################################################

printLog() {
	echo "$1" >> /opt/ablebit/clj/logs/udev.log;
}

printLog "CONECTOU->$1 : ACTION->${ACTION}"

/opt/ablebit/clj/bin/pppManager $1 ${ACTION}
