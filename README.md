# ChonIDE
_Developing an Embedded MAS is a task that requires the domain ofseveral areas of knowledge; therefore, the Embedded MAS architecture used inthis work is divided into four layers: reasoning, serial, firmware, and hardware.Different knowledge is required at each project layer: electronics, low-level pro-gramming, object-oriented and agent-oriented programming. This work seeksto present the first results of a specialized web Integrated Development Environ-ment (IDE) for Embedded MAS to simplify their development._

## Demonstration

<iframe width="560" height="315" src="https://www.youtube.com/embed/MfZQjUNYglQ" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

## How to install?
### Windows
Follow the two steps described below:
1. Download and install the [Oracle Virtual Box Virtualizer](https://www.virtualbox.org/wiki/Downloads)
2. Download and import the [ChonIDE Virtual Machine](https://sourceforge.net/projects/chonos/files/vms/chonIDEVM.ova)

### Debian 11 or Ubuntu 22.04
In a terminal, execute the commands below:
```sh
 echo "deb [trusted=yes] http://packages.chon.group/ chonos main" | sudo tee /etc/apt/sources.list.d/chonos.list
 sudo apt update; sudo apt install chonos-sysconfig chonide 
```

## Publications and how to cite
+ [Proposta de uma IDE para desenvolvimento de SMA Embarcados](https://www.researchgate.net/publication/362837129_Proposta_de_uma_IDE_para_desenvolvimento_de_SMA_Embarcados) [[Bibtex Citation](https://sourceforge.net/p/chonos/sysConfig/ci/master/tree/publications/proposta-de-IDE.bib?format=raw)]
    + SOUZA DE JESUS, Vinicius; LAZARIN, Nilson Mori; PANTOJA, Carlos; MANOEL, Fabian; VAZ ALVES, Gleifer; RAMOS, Gabriel; VITERBO, Jose. Proposta de uma IDE para desenvolvimento de SMA Embarcados. _In:_ 16ᵗʰ WORKSHOP-SCHOOL ON AGENTS, ENVIRONMENTS AND APPLICATIONS, 2022. __Anais do XVI Workshop-Escola de Sistemas de Agentes, seus Ambientes e Aplicações (WESAAC 2022).__ Blumenau: UFSC, 2022. p. 49–60. Disponível em: https://wesaac2022.ufsc.br/2022/proceedings/.