@echo off
title Gerando .JAR do CobranSys
echo ===============================
echo [1] Compilando e empacotando com Maven
echo ===============================

call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Falha na compilação. Verifique o código.
    goto FIM
)

if not exist target\cobransys-app-1.0.jar (
    echo ❌ Arquivo .jar não foi gerado!
    goto FIM
)

echo.
echo ✔️ JAR gerado com sucesso: target\cobransys-app-1.0.jar
:: === CONFIGURAÇÃO ===
set NOME_APP=CobranSys
set MAIN_JAR=cobransys-app-1.0.jar
set MAIN_CLASS=App
set ICON=src\Estilo\favicon.ico
set DESTINO=target\installer

:: ===  Configurado na variavel de ambientes do windows
:: === JDK_MODS  -> jmod do jdk
:: === JAVAFX_MODS -> jmod do javafx
:: === FXDLL -> dlls do javafx
:: === [1/2] GERANDO IMAGEM RUNTIME COM JLINK ===

echo.
echo ===== GERANDO imagem runtime com suporte JavaFX =====
if exist runtime rmdir /s /q runtime

jlink ^
  --module-path %JDK_MODS%;%JAVAFX_MODS% ^
  --add-modules java.base,java.desktop,javafx.controls,javafx.fxml,javafx.graphics,jdk.unsupported ^
  --output runtime

xcopy /Y /E /I "%FXDLL%\*.dll" runtime\bin\

if errorlevel 1 (
  echo ERRO ao gerar a imagem runtime.
  pause
  exit /b
)

:: === [2/2] EMPACOTANDO COM JPACKAGE ===
echo.
echo ===== EMPACOTANDO com jpackage =====
jpackage ^
  --type exe ^
  --name %NOME_APP% ^
  --input target ^
  --main-jar %MAIN_JAR% ^
  --main-class %MAIN_CLASS% ^
  --icon %ICON% ^
  --dest %DESTINO% ^
  --win-menu ^
  --win-shortcut ^
  --runtime-image runtime ^
  --java-options "-Dprism.order=sw"

if errorlevel 1 (
  echo ERRO ao empacotar com jpackage.
  pause
  exit /b
)

echo.
echo ===== EMPACOTAMENTO CONCLUÍDO COM SUCESSO =====
echo Instalador criado em: %DESTINO%\%NOME_APP%.exe
pause
