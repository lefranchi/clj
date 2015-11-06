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

* udev/10-clj.rules: udev para acionar o programa gerenciador de conexĂŁo/desconexĂŁo;
* udev/clj-ppp.sh: shell que executa o programa acionado pelo udev rule;
* depInstall.sh: instala dependĂŞncias maven nĂŁo contidas no reportĂłrio central;
* install.sh: compila e instala todos artefatos do sistema;
* kill.sh: finaliza execuĂ§ĂŁo de todos programas do sistema;
* uninstall.sh: remove todo o sistema;
* java-run-as-root.sh: executa o java em modo root.

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
* apt-get install maven;
* git clone;
* sudo ./scripts/install.sh;