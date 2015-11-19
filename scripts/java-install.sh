#!/bin/bash

echo "INICIO ====================================================================="

if [ $# -eq 0 ]
  then
    echo "Indique o diretório. Ex: /home/pi/work"
fi

JDK_PATH="$1/work/jdk"

echo "Usando diretório: $JDK_PATH"

echo "Removendo Diretorio JDK..."
rm -rf $JDK_PATH

echo "Criando Diretorio JDK..."
mkdir -p $JDK_PATH

echo "Fazendo download do JDK..."
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u65-b17/jdk-8u65-linux-x64.tar.gz

echo "Descompactando arquivo do JDK..."
tar -zxvf jdk-8u65-linux-x64.tar.gz -C $JDK_PATH/
rm -f jdk-8u65-linux-x64.tar.gz

echo "Criando link para o JDK..."
ln -s $JDK_PATH/jdk1.8.0_65/ $JDK_PATH/jdk-default

echo "Update alternatives..."
sudo update-alternatives --install /usr/bin/java java $JDK_PATH/jdk-default/bin/java 100
sudo update-alternatives --install /usr/bin/javac javac $JDK_PATH/jdk-default/bin/javac 100

echo "Cheking Java Version..."
java -version

echo "Atualizando java para executar como root..."
sudo apt-get install gksu
mv $JDK_PATH/jdk-default/bin/java $JDK_PATH/jdk-default/bin/java.ori
cp ./scripts/java-run-as-root.sh $JDK_PATH/jdk-default/bin/java
chmod +x $JDK_PATH/jdk-default/bin/java
sudo sed -i.bak s,java_ori,$JDK_PATH/jdk-default/bin/java.ori,g $JDK_PATH/jdk-default/bin/java

echo "Testando execução de Java como root..."
java --run-as-root -version

echo "FIM ====================================================================="
