语言：java
版本：OpenJDK-19
maven: 3.9.0
os:macOs

打包命令并执行junit命令： mvn clean package 
仅执行junit命令：mvn test

使用方法：执行打包命令，生成jar包，在其他项目中引入，如果是正式环境使用，需要把包名
<dependency>
    <groupId>com.su</groupId>
    <artifactId>jerri-problem</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
