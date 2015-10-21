#! /bin/sh

echo "Removendo Diretorio CLJ..."
rm -rf /opt/ablebit/clj

echo "Criando Diretorio CLJ..."
mkdir -p /opt/ablebit/clj

echo "Copiando Diretorio CLJ..."
cp -r ../target/clj/jsw/* /opt/ablebit/clj/

echo "====================================================================="
echo "Configurando Receptor"
echo "====================================================================="

echo "Criando diretório de Logs..."
mkdir -p /opt/ablebit/clj/receptor/logs/

echo "Copiando Arquivos de Configuracoes..."
cp ../src/main/resources/clj.properties /opt/ablebit/clj/receptor/conf/
cp ../src/main/resources/log4j.xml /opt/ablebit/clj/receptor/conf/

echo "Setando Permissoes..."
chmod +x /opt/ablebit/clj/receptor/bin/receptor
chmod +x /opt/ablebit/clj/receptor/bin/wrapper-linux-x86-64

echo "====================================================================="
echo "Configurando Transmissor"
echo "====================================================================="

echo "Criando diretório de Logs..."
mkdir -p /opt/ablebit/clj/transmissor/logs/

echo "Copiando Arquivos de Configuracoes..."
cp ../src/main/resources/clj.properties /opt/ablebit/clj/transmissor/conf/
cp ../src/main/resources/log4j.xml /opt/ablebit/clj/transmissor/conf/

echo "Setando Permissoes..."
chmod +x /opt/ablebit/clj/transmissor/bin/transmissor
chmod +x /opt/ablebit/clj/transmissor/bin/wrapper-linux-x86-64

echo "====================================================================="
