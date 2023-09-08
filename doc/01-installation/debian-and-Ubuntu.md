# Installation of ChonIDE in the Debian 11 or Ubuntu 22.04

In a terminal, execute the steps described below:

### 1) Include the ChonOS repository address into APT Sources List 

```sh
 echo "deb [trusted=yes] http://packages.chon.group/ chonos main" | sudo tee /etc/apt/sources.list.d/chonos.list 
```

![Captura de tela de 2023-09-08 15-43-51](https://github.com/chon-group/chonIDE/assets/32855001/5377d472-ef84-4772-ba48-3c03f506baa8)

### 2) Execute the APT Update and Install the packages

```sh
 sudo apt update 
```
![Sem título](https://github.com/chon-group/chonIDE/assets/32855001/49210bde-a227-4e41-8759-46c6ca263526)


### 3) Install the Java 17 and the ChonIDE packages

```sh
 sudo apt install openjdk-17-jre chonide 
```
![Captura de tela de 2023-09-08 16-02-13](https://github.com/chon-group/chonIDE/assets/32855001/436cb21d-e50f-48e4-918f-c03c892c1663)

### 4) Reboot your system

```sh
 sudo reboot 
```
![Captura de tela de 2023-09-08 16-04-07](https://github.com/chon-group/chonIDE/assets/32855001/2ecbfe1b-2827-49a9-9081-b682b0a9b415)

### 5) Execute the [Hello Agent!](https://github.com/chon-group/chonIDE/blob/main/doc/02-helloWorld/Ubuntu.md)
The ChonIDE was installed in your system. 

Now, see the instructions of the [Hello Agent!](https://github.com/chon-group/chonIDE/blob/main/doc/02-helloWorld/Ubuntu.md) test page to try your ChonIDE.
 
