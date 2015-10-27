#! /bin/sh

mvn clean

echo "Removendo Diretorio CLJ..."
rm -rf /opt/ablebit/clj

echo "Removendo UDEV Rules"
rm -f /etc/udev/rules.d/10-clj.rules

echo "UDEV Rules Reload..."
udevadm control --reload-rules
