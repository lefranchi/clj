#!/bin/sh
#############################################################################################
#
#
#
#############################################################################################

printLog() {
	echo "$1" >> /opt/ablebit/clj/logs/udev.log;
}

printLog "ENTROU->$1 : ACAO->${ACTION}"