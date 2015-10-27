# Chico Leandro Java #

Este projeto tem como objetivo transmitir media via rede. A transmissão é feita por todos meios de rede disponíveis efetuando balanceamento de carga e tolerância a falhas entre eles.

### Artefatos ###

* Receptor: Recebe dados;
* Transmissor: Envia dados;
* JSInfo: Aplicativo de áudio para testes;
* DataLineTester: Verifica DataLines disponíveis no equipamento;
* RmsVolumeTester: Testa volume de entrada;
* PPPManager: Gerenciador de conexão/desconexão de modem via ppp.

### Scripts ###

* udev/10-clj.rules: udev para acionar o programa gerenciador de conexão/desconexão;
* udev/clj-ppp.sh: shell que executa o programa acionado pelo udev rule;
* depInstall.sh: instala dependências maven não contidas no reportório central;
* install.sh: compila e instala todos artefatos do sistema;
* kill.sh: finaliza execução de todos programas do sistema;
* uninstall.sh: remove todo o sistema.