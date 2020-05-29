# 串口通讯（短信猫-18258834762）

## 调试工具
### 串口虚拟工具：vspd.exe
```
设置两个虚拟串口，进行串口间通讯，即从串口1发送数据到串口2进行模拟串口通讯
custom pinout 设置 preset 为loopback mode 即可生效
```
### 串口通讯模拟工具：UartAssist.exe
```
打开软件，选择串口和频率，发送消息进行模拟
```
<hr>

## 项目依赖
### x64
```
RXTX包：rxtx-2.2pre2-bins.zip

1、RXTXcomm.jar加入项目依赖库里
2、将rxtxParallel.dll和rxtxSerial.dll放入jdk的bin目录下
```

### x86
```
win32com.dll

1、把smslib-3.3.0b2.jar、comm.jar与log4j-1.2.11.jar，放入到工程的lib中；
2、把javax.comm.properties放到%JAVA_HOME%/jre/lib下；
3、把win32com.dll放到%JAVA_HOME%/jre/bin下；
4、把comm.jar放到%JAVA_HOME%/jre/ext下
```