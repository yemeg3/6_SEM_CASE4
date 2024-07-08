@echo off
rem Running Java project
java Main

rem Checking status Java project
if errorlevel 1 (
    echo Error. Please install project
    pause
    exit /b 1
) else (
    echo Closed app.
    pause
)
