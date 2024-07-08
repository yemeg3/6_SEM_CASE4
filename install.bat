@echo off
rem Installing Java project
javac Main.java

rem Checking errs:
if errorlevel 1 (
    echo Error. Check Main.java.
    pause
    exit /b 1
) else (
    echo Install successfully.
    echo.
    pause
)
