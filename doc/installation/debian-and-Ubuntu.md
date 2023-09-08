# Installation of ChonIDE in the Debian 11 or Ubuntu 22.04

In a terminal, execute the steps described below:

### i) Include the ChonOS repository address into APT Sources List 

```sh
 echo "deb [trusted=yes] http://packages.chon.group/ chonos main" | sudo tee /etc/apt/sources.list.d/chonos.list 
```

![Captura de tela de 2023-09-08 15-43-51](https://github.com/chon-group/chonIDE/assets/32855001/5377d472-ef84-4772-ba48-3c03f506baa8)

### ii) Execute the APT Update and Install the packages

```sh
 sudo apt update 
```
![Sem título](https://github.com/chon-group/chonIDE/assets/32855001/49210bde-a227-4e41-8759-46c6ca263526)


### iii) Install the Java 17 and the ChonIDE packages

```sh
 sudo apt install openjdk-17-jre chonide 
```
![Captura de tela de 2023-09-08 16-02-13](https://github.com/chon-group/chonIDE/assets/32855001/436cb21d-e50f-48e4-918f-c03c892c1663)

### iv) Reboot your system
```sh
 sudo reboot 
```
![Captura de tela de 2023-09-08 16-04-07](https://github.com/chon-group/chonIDE/assets/32855001/2ecbfe1b-2827-49a9-9081-b682b0a9b415)

### v) Executing the ChonIDE
A ChonIDE conta com alguns projetos de demonstração que facilitam o teste de funcionamento. Para tal, execute a ChonIDE. No Ubuntu, clique em mostrar aplicativos (1), na barra de pesquisa (2), digite chonide, por fim clique no ícone (3) para executar. Caso esteja utilizando a ChonIDE no virtual box, o ícone encontra-se na área de trabalho.

![image3](https://github.com/chon-group/chonIDE/assets/32855001/6cf8a1c0-7cab-4068-94ad-7c140e69bc13)

### vi) Logging into ChonIDE 
Uma vez que a ChonIDE é executada como uma página web, para utilizá-la, é necessário realizar a autenticação na página inicial. Informe um nome de usuário válido no computador (1) e a sua respectiva senha (2). Após isso, clique em entrar (3). 
![login](https://github.com/chon-group/chonIDE/assets/32855001/7a699246-ed8a-46bd-b5ca-c76e46467465)

### vii) Hello Agent
Na tela principal e abra o projeto modelo helloAgent.
![hello](https://github.com/chon-group/chonIDE/assets/32855001/7eb81da9-3219-4435-af0c-8ca217150490)


### viii) Executing
Uma vez aberto o projeto helloAgent, clique em Run MAS (1) para executar o exemplo. Após iniciado, clique em Logs (2), para vizualizar a execução.
![inicial](https://github.com/chon-group/chonIDE/assets/32855001/fdaddc1e-5e2d-40c2-967c-fc7fbb08a432)


### ix) Test
Caso a mensagem “Never Send A Human To Do A Machine's Job!” seja exibida, a instalação está correta. Parabéns!
![ok](https://github.com/chon-group/chonIDE/assets/32855001/9476760b-ae2d-42d2-8bf1-e5ed48a05121)





## Copyright
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />ChonIDE is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
You are free to: Share — copy and redistribute the material in any medium or format; Adapt — remix, transform, and build upon the material for any purpose, even commercially. 
The licensor cannot revoke these freedoms as long as you follow the license terms:

__Attribution__ — You must give __appropriate credit__ like below:

Souza de Jesus, V., Mori Lazarin, N., Pantoja, C.E., Vaz Alves, G., Ramos Alves de Lima, G., Viterbo, J. (2023). An IDE to Support the Development of Embedded Multi-Agent Systems. In: Mathieu, P., Dignum, F., Novais, P., De la Prieta, F. (eds) Advances in Practical Applications of Agents, Multi-Agent Systems, and Cognitive Mimetics. The PAAMS Collection. PAAMS 2023. Lecture Notes in Computer Science(), vol 13955. Springer, Cham. https://doi.org/10.1007/978-3-031-37616-0_29


