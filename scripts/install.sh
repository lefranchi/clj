#! /bin/sh

echo "INICIO ====================================================================="

./scripts/depInstall.sh

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

echo "Copiando UDEV Rules..."
cp ./scripts/udev/10-clj.rules /etc/udev/rules.d/

echo "UDEV Rules Reload..."
udevadm control --reload-rules

echo "Copiando Script UDEV"
cp ./scripts/udev/clj-ppp.sh /opt/ablebit/clj/bin/

echo "Setando Permissoes..."
chmod +x /opt/ablebit/clj/bin/receptor
chmod +x /opt/ablebit/clj/bin/transmissor
chmod +x /opt/ablebit/clj/bin/jsinfo
chmod +x /opt/ablebit/clj/bin/dataLineTester
chmod +x /opt/ablebit/clj/bin/rmsVolumeTester
chmod +x /opt/ablebit/clj/bin/pppManager
chmod +x /opt/ablebit/clj/bin/clj-ppp.sh

echo "FIM ====================================================================="
