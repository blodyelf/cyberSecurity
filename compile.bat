@echo off

REM javac Communication.java 
set classpath=.;libraries\*
set folders=security/*.java database/*.java data_classes/*.java exceptions/*.java

javac -cp %classpath% %folders% *.java

pause