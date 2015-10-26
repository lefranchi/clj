#!/bin/sh

echo "Instalando dependecias..."

echo "Instalando RXTX for Java..."

mvn install:install-file -DgroupId=org.rxtx \
	-DartifactId=rxtx \
	-Dversion=2.2pre2 \
	-Dfile=files/lib/RXTXcomm-2.2pre2.jar \
	-Dpackaging=jar \
	-DgeneratePom=true
	
echo "RXTX instalada com sucesso."
	
echo "Dependencias instaladas com sucesso."
