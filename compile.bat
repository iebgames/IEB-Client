@echo off
setlocal
echo ========================================
echo IEB CLIENT - NUCLEAR BUILDER (DEBUG)
echo ========================================

:: Use the pre-configured JAVA_HOME in gradle.properties
set "JAVA_HOME=C:\niga\jre\jre-legacy\windows-x64\jre-legacy"
set "PATH=%JAVA_HOME%\bin;C:\Windows\System32;C:\Windows"

echo [INFO] Running build...
call C:\niga\temp\gradle4\gradle-4.10.3\bin\gradle.bat build --no-daemon -Djava.io.tmpdir="C:\niga\temp"

pause
endlocal
