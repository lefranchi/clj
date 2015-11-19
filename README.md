# Chico Leandro Java #

Este projeto tem como objetivo transmitir mĂ­dia via rede. A transmissĂŁo ĂŠ feita por todos meios de rede disponĂ­veis efetuando balanceamento de carga e tolerĂ˘ncia a falhas entre eles.

### Artefatos ###

* Receptor: Recebe dados;
* Transmissor: Envia dados;
* JSInfo: Aplicativo de ĂĄudio para testes;
* DataLineTester: Verifica DataLines disponĂ­veis no equipamento;
* RmsVolumeTester: Testa volume de entrada;
* PPPManager: Gerenciador de conexĂŁo/desconexĂŁo de modem via ppp;
* PppInitializer: Configura ambiente geral para conexőes ppp;
* PppDbUpdate: Autalizador automatico de informaçőes para conexăo ppp.

### Scripts ###

Sempre executar do diretorio rais do clj: ./scripts/install.sh

* udev/10-clj.rules: udev para acionar o programa gerenciador de conexĂŁo/desconexĂŁo;
* udev/clj-ppp.sh: shell que executa o programa acionado pelo udev rule;
* depInstall.sh: instala dependĂŞncias maven nĂŁo contidas no reportĂłrio central;
* install.sh: compila e instala todos artefatos do sistema;
* kill.sh: finaliza execuĂ§ĂŁo de todos programas do sistema;
* uninstall.sh: remove todo o sistema;
* java-run-as-root.sh: executa o java em modo root;
* java-install.sh: instala o java utilizando link simbolico e update-alternatives.

### Raspberry Pi - Setup ###
* Work Directory: mkdir -p /home/pi/work/workspaces/ablebit
* Wifi: /etc/network/interfaces
```
#!shell
auto lo
iface lo inet loopback

auto eth0
allow-hotplug eth0
iface eth0 inet manual

allow-hotplug wlan0
auto wlan0

iface wlan0 inet dhcp
	wpa-ssid "xxxxxx"
	wpa-psk "xxxxxxxxx"

```
* sudo cp ./files/linux/keyboard /etc/default/;
* sudo service networking reload;
* sudo apt-get update;
* sudo apt-get upgrade;
* sudo cp /usr/share/zoneinfo/Brazil/East /etc/localtime;
* sudo apt-get install ntpdate;
* sudo apt-get install iptraf;
* sudo apt-get install maven;
* git clone XXXXX;
* sudo ./scripts/install.sh;
