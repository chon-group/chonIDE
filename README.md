# Cognitive Hardware on Network - IDE (ChonIDE)
_Embedded MAS development requires knowledge in different areas, such as agent-oriented programming, object-oriented programming, low-level programming, and basic electronics concepts. The literature has a consolidated Embedded MAS development architecture divided into four layers: Reasoning, Serial, Firmware, and Hardware. However, one of the main difficulties that MAS designers face is the need to use and configure different Integrated Development Environments (IDE) and make several integrations to embed the MAS. Even using all these technologies, embedding and monitoring the Embedded MAS is done using physical wired connections, making them limited and impracticable depending on the application. Therefore, this work aims to present an IDE to develop Embedded MAS that centralizes the entire development in a single IDE with all required integrations and configurations done. Moreover, the embedding and monitoring MAS of the IDE are done remotely without physical wired connections._

## How to install?

### MS Windows
Follow the two steps described below:
1. Install the Oracle Virtual Box Virtualizer [Available at](https://www.virtualbox.org/)
2. Download and Import the ChonIDE Virtual Machine [Available at](https://chonide.sf.net)

### Debian 11 or Ubuntu 22.04

In a terminal, execute the two steps described below:

i) Include the ChonOS repository address into APT Sources List 

```sh
 echo "deb [trusted=yes] http://packages.chon.group/ chonos main" | sudo tee /etc/apt/sources.list.d/chonos.list 
```

ii) Execute the APT Update and Install the packages

```sh
 sudo apt update; sudo apt install openjdk-17-jre chonide 
```
## Publications
+ PAAMS 2023 (Full paper) - [An IDE to Support the Development of Embedded Multi-Agent Systems](https://www.researchgate.net/publication/372282731_An_IDE_to_Support_the_Development_of_Embedded_Multi-Agent_Systems)
  
+ WESAAC 2022 (Full paper) - [Proposta de uma IDE para desenvolvimento de SMA Embarcados](https://www.researchgate.net/publication/362837129_Proposta_de_uma_IDE_para_desenvolvimento_de_SMA_Embarcados)

+ WESAAC 2022 (Best Demo) - [Implementando uma IDE para SMA Embarcados](https://www.researchgate.net/publication/363108136_Implementando_uma_IDE_para_SMA_Embarcados)

## Copyright
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />ChonIDE is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
You are free to: Share — copy and redistribute the material in any medium or format; Adapt — remix, transform, and build upon the material for any purpose, even commercially. 
The licensor cannot revoke these freedoms as long as you follow the license terms:

__Attribution__ — You must give __appropriate credit__ like below:

Souza de Jesus, V., Mori Lazarin, N., Pantoja, C.E., Vaz Alves, G., Ramos Alves de Lima, G., Viterbo, J. (2023). An IDE to Support the Development of Embedded Multi-Agent Systems. In: Mathieu, P., Dignum, F., Novais, P., De la Prieta, F. (eds) Advances in Practical Applications of Agents, Multi-Agent Systems, and Cognitive Mimetics. The PAAMS Collection. PAAMS 2023. Lecture Notes in Computer Science(), vol 13955. Springer, Cham. https://doi.org/10.1007/978-3-031-37616-0_29


