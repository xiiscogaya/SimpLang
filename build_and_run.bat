@echo off

:: Variables
set JAVA=java
set JAVAC=javac
set SRC_DIR=src
set BUILD_DIR=build
set LIB_DIR=lib
set TESTS_DIR=tests

:: 1. Generar el parser con JavaCUP
echo Generando el parser...
%JAVA% -jar %LIB_DIR%\JavaCUP\java-cup-11b.jar -destdir %SRC_DIR%\compiler\sintactic %SRC_DIR%\compiler\sintactic\sintactic.cup
if %ERRORLEVEL% neq 0 (
    echo Error al generar el parser.
    exit /b %ERRORLEVEL%
)

:: 2. Generar el scanner con JFlex
echo Generando el scanner...
%JAVA% -jar %LIB_DIR%\JFlex\jflex-full-1.8.2.jar %SRC_DIR%\compiler\lexic\lexic.flex
if %ERRORLEVEL% neq 0 (
    echo Error al generar el scanner.
    exit /b %ERRORLEVEL%
)

:: 3. Crear el directorio de compilación
if not exist %BUILD_DIR% (
    mkdir %BUILD_DIR%
)

:: 4. Compilar todos los archivos .java en src
echo Compilando el proyecto...
%JAVAC% -cp %LIB_DIR%\JavaCUP\java-cup-11b.jar;%SRC_DIR% -d %BUILD_DIR% %SRC_DIR%\analitzador.java
if %ERRORLEVEL% neq 0 (
    echo Error al compilar el proyecto.
    exit /b %ERRORLEVEL%
)

:: 5. Ejecutar el programa con el test especificado
if "%1"=="" (
    echo No se especificó un número de test. Ejecuta: build_and_run.bat [numero_test]
    exit /b 1
)

set TEST_FILE=%TESTS_DIR%\test%1.txt
if not exist "%TEST_FILE%" (
    echo El archivo %TEST_FILE% no existe.
    exit /b 1
)

echo Ejecutando el programa con el test %1...
%JAVA% -cp %LIB_DIR%\JavaCUP\java-cup-11b.jar;%BUILD_DIR% analitzador %TEST_FILE%
if %ERRORLEVEL% neq 0 (
    echo Error al ejecutar el programa.
    exit /b %ERRORLEVEL%
)

:: Finalización exitosa
echo Proceso completado.
pause
