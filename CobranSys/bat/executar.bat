@echo off
setlocal

set DIR=%~dp0

set PATH=%DIR%runtime\bin;%PATH%

%DIR%runtime\bin\java ^
  -Dprism.verbose=true -Dprism.order=sw ^
  -jar %DIR%target\cobransys-app-1.0.jar

pause