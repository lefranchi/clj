#! /bin/sh

echo "INICIO ====================================================================="

mvn clean package appassembler:assemble -Denvironment=production

echo "DEPLOY ====================================================================="

echo "Removendo Diretorio CLJ..."
rm -rf /opt/ablebit/clj

echo "Criando Diretorio ABLEBIT..."
mkdir -p /opt/ablebit/

echo "Copiando Diretorio CLJ..."
cp -r target/clj /opt/ablebit/

echo "Setando Permissoes..."
chmod +x /opt/ablebit/clj/bin/receptor
chmod +x /opt/ablebit/clj/bin/transmissor

echo "FIM ====================================================================="
