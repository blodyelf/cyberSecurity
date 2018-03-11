@echo off

set classpath=.;libraries\*

java -cp %classpath% database/DatabaseConnection

pause