@echo off
title Blood of the Rift - Game Launcher
color 0F

REM ========================================
REM BLOOD OF THE RIFT LAUNCHER
REM Downloads JDK 24.0.1 and fixes version issues
REM Ensures compilation and runtime use same Java
REM No admin privileges required
REM ========================================

REM Set window properties for better visibility
mode con cols=95 lines=32

REM Safe fullscreen method using Windows Script Host
echo Initializing Blood of the Rift Launcher...
echo Set ws = CreateObject("WScript.Shell") > "%TEMP%\fs.vbs"
echo ws.SendKeys "{F11}" >> "%TEMP%\fs.vbs"
start /wait /min cscript //nologo "%TEMP%\fs.vbs"
del "%TEMP%\fs.vbs" >nul 2>&1

REM Small delay for fullscreen effect
ping 127.0.0.1 -n 2 >nul

cls
echo.
echo  ====================================================
echo  ^|               BLOOD OF THE RIFT                  ^|
echo  ^|          Java Compatibility Launcher             ^|
echo  ^|         Fixes Version Mismatch Issues            ^|
echo  ====================================================
echo.

REM Search variables
set "GAME_LOCATION="
set "FOUND_GAME="
set "JAVA_CMD="
set "JAVAC_CMD="
set "JDK_DOWNLOADED="

echo [BLOOD OF THE RIFT] Locating game files...
echo.

REM Priority 1: Same directory as this batch file (most common for distribution)
set "BATCH_DIR=%~dp0"
echo [SCAN] Checking launcher directory...
if exist "%BATCH_DIR%GAME\src\Main.java" (
    set "GAME_LOCATION=%BATCH_DIR%GAME"
    set "FOUND_GAME=YES"
    echo [FOUND] Blood of the Rift located: %BATCH_DIR%GAME
    goto check_java_version
)

if exist "%BATCH_DIR%src\Main.java" (
    set "GAME_LOCATION=%BATCH_DIR%"
    set "FOUND_GAME=YES"
    echo [FOUND] Blood of the Rift launcher inside game folder: %BATCH_DIR%
    goto check_java_version
)

REM Priority 2: Current working directory
echo [SCAN] Checking current directory...
if exist ".\GAME\src\Main.java" (
    set "GAME_LOCATION=%CD%\GAME"
    set "FOUND_GAME=YES"
    echo [FOUND] Blood of the Rift in current directory: %CD%\GAME
    goto check_java_version
)

if exist ".\src\Main.java" (
    set "GAME_LOCATION=%CD%"
    set "FOUND_GAME=YES"
    echo [FOUND] Already in Blood of the Rift folder: %CD%
    goto check_java_version
)

REM Priority 3: Desktop (very common for distributed games)
echo [SCAN] Checking Desktop...
for %%D in ("%USERPROFILE%\Desktop" "%PUBLIC%\Desktop") do (
    if exist "%%D\GAME\src\Main.java" (
        set "GAME_LOCATION=%%D\GAME"
        set "FOUND_GAME=YES"
        echo [FOUND] Blood of the Rift on Desktop: %%D\GAME
        goto check_java_version
    )
)

REM Priority 4: Documents folder
echo [SCAN] Checking Documents...
if exist "%USERPROFILE%\Documents\GAME\src\Main.java" (
    set "GAME_LOCATION=%USERPROFILE%\Documents\GAME"
    set "FOUND_GAME=YES"
    echo [FOUND] Blood of the Rift in Documents: %USERPROFILE%\Documents\GAME
    goto check_java_version
)

REM Priority 5: Downloads folder (common extraction location)
echo [SCAN] Checking Downloads...
if exist "%USERPROFILE%\Downloads\GAME\src\Main.java" (
    set "GAME_LOCATION=%USERPROFILE%\Downloads\GAME"
    set "FOUND_GAME=YES"
    echo [FOUND] Blood of the Rift in Downloads: %USERPROFILE%\Downloads\GAME
    goto check_java_version
)

REM Priority 6: Common directories and drive roots
echo [SCAN] Checking common game locations...
for %%G in ("%USERPROFILE%\Games" "C:\Games" "D:\Games" "C:\GAME" "D:\GAME") do (
    if exist "%%G\src\Main.java" (
        set "GAME_LOCATION=%%G"
        set "FOUND_GAME=YES"
        echo [FOUND] Blood of the Rift found: %%G
        goto check_java_version
    )
    if exist "%%G\GAME\src\Main.java" (
        set "GAME_LOCATION=%%G\GAME"
        set "FOUND_GAME=YES"
        echo [FOUND] Blood of the Rift found: %%G\GAME
        goto check_java_version
    )
)

REM If still not found - show error
if not defined FOUND_GAME (
    echo.
    echo =====================================================
    echo       BLOOD OF THE RIFT GAME FILES NOT FOUND
    echo =====================================================
    echo.
    echo Blood of the Rift launcher searched for game files in:
    echo - Same directory as this launcher
    echo - Current directory
    echo - Desktop, Documents, Downloads
    echo - Common game directories
    echo.
    echo SETUP INSTRUCTIONS:
    echo 1. Place this launcher next to the GAME folder, OR
    echo 2. Extract Blood of the Rift to Desktop/Documents, OR
    echo 3. Ensure GAME folder contains src\Main.java
    echo.
    pause
    exit /b 1
)

:check_java_version
echo.
echo [BLOOD OF THE RIFT] Checking Java environment...
echo.

REM Check if we have the correct JDK 24 already downloaded
set "JDK_DIR=%TEMP%\BloodOfTheRift_JDK24"
if exist "%JDK_DIR%\jdk-24.0.1\bin\java.exe" (
    echo [FOUND] Blood of the Rift JDK 24 already available
    set "JAVA_CMD=%JDK_DIR%\jdk-24.0.1\bin\java.exe"
    set "JAVAC_CMD=%JDK_DIR%\jdk-24.0.1\bin\javac.exe"
    set "JDK_DOWNLOADED=YES"
    goto start_compilation
)

REM Check system Java version
echo [CHECK] Testing system Java version...
java -version >nul 2>&1
if errorlevel 1 (
    echo [INFO] No system Java found - will download JDK 24
    goto download_jdk24
)

REM Get Java version details
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set "JAVA_VER=%%g"
    goto got_version
)

:got_version
echo [DETECTED] System Java version: %JAVA_VER%

REM Check if it's Java 24
echo %JAVA_VER% | findstr /i "24\.0" >nul
if not errorlevel 1 (
    echo [OK] System Java 24 detected - checking compiler...
    javac -version >nul 2>&1
    if not errorlevel 1 (
        echo [OK] Java 24 compiler available
        set "JAVA_CMD=java"
        set "JAVAC_CMD=javac"
        goto start_compilation
    )
)

echo [INFO] System Java is not version 24 - downloading JDK 24.0.1
echo [INFO] This ensures compatibility with Blood of the Rift

:download_jdk24
echo.
echo =====================================================
echo     DOWNLOADING JDK 24.0.1 FOR BLOOD OF THE RIFT
echo =====================================================
echo.

echo [DOWNLOAD] Setting up JDK 24.0.1 for Blood of the Rift...
echo This ensures perfect compatibility and fixes version errors.
echo.

REM Create download directory
if not exist "%JDK_DIR%" mkdir "%JDK_DIR%"

echo [INFO] Downloading Java 24.0.1 (Latest version)...
echo This may take 3-5 minutes depending on your connection.
echo.

REM Download JDK 24.0.1 from Oracle
set "JDK_URL=https://download.oracle.com/java/24/archive/jdk-24.0.1_windows-x64_bin.zip"
set "JDK_FILE=%JDK_DIR%\jdk24.zip"

echo Downloading JDK 24.0.1 from Oracle...
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; try { $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri '%JDK_URL%' -OutFile '%JDK_FILE%' -UseBasicParsing; Write-Host 'Download completed successfully' } catch { Write-Host 'Oracle download failed: ' + $_.Exception.Message; exit 1 }}" 2>nul

if not exist "%JDK_FILE%" (
    echo [WARNING] Oracle download failed, trying OpenJDK mirror...
    echo.
    
    REM Try alternative OpenJDK 24 source
    set "JDK_URL=https://github.com/adoptium/temurin24-binaries/releases/download/jdk-24+36/OpenJDK24U-jdk_x64_windows_hotspot_24_36.zip"
    
    echo Trying alternative download source...
    powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; try { $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri '%JDK_URL%' -OutFile '%JDK_FILE%' -UseBasicParsing; Write-Host 'Alternative download completed' } catch { Write-Host 'Alternative download failed: ' + $_.Exception.Message; exit 1 }}" 2>nul
    
    if not exist "%JDK_FILE%" (
        echo [ERROR] Automatic JDK download failed.
        echo.
        echo MANUAL INSTALLATION OPTIONS:
        echo 1. Download JDK 24 from: https://www.oracle.com/java/technologies/downloads/
        echo 2. Or download from: https://adoptium.net (Eclipse Temurin)
        echo 3. Install JDK 24 and restart this launcher
        echo.
        echo IMPORTANT: Make sure to install JDK 24, not an older version!
        echo.
        pause
        exit /b 1
    )
)

echo [SUCCESS] JDK 24.0.1 downloaded successfully!
echo [EXTRACT] Setting up Java 24 environment...

REM Extract JDK using PowerShell
powershell -Command "& {Add-Type -AssemblyName System.IO.Compression.FileSystem; try { [System.IO.Compression.ZipFile]::ExtractToDirectory('%JDK_FILE%', '%JDK_DIR%'); Write-Host 'Extraction completed' } catch { Write-Host 'Extraction failed: ' + $_.Exception.Message; exit 1 }}" >nul 2>&1

if errorlevel 1 (
    echo [ERROR] JDK extraction failed.
    echo The downloaded file may be corrupted.
    echo Please try downloading JDK 24 manually.
    echo.
    pause
    exit /b 1
)

REM Find extracted JDK directory
for /d %%D in ("%JDK_DIR%\jdk*") do (
    set "EXTRACTED_JDK=%%D"
    goto found_jdk24
)

:found_jdk24
if not defined EXTRACTED_JDK (
    echo [ERROR] Could not locate extracted JDK 24 directory.
    echo.
    dir "%JDK_DIR%" /b
    echo.
    pause
    exit /b 1
)

echo [SUCCESS] JDK 24.0.1 ready at: %EXTRACTED_JDK%

REM Test the JDK installation
echo [TEST] Verifying JDK 24.0.1 installation...
"%EXTRACTED_JDK%\bin\java" -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] JDK 24 verification failed.
    pause
    exit /b 1
)

"%EXTRACTED_JDK%\bin\javac" -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] JDK 24 compiler verification failed.
    pause
    exit /b 1
)

echo [SUCCESS] JDK 24.0.1 verified and ready!
echo.

REM Set commands to use downloaded JDK
set "JAVA_CMD=%EXTRACTED_JDK%\bin\java"
set "JAVAC_CMD=%EXTRACTED_JDK%\bin\javac"
set "JDK_DOWNLOADED=YES"

:start_compilation
echo [BLOOD OF THE RIFT] Preparing game compilation...
echo.
echo [ACCESS] Entering Blood of the Rift directory...
cd /d "%GAME_LOCATION%\src"

REM Final verification that Main.java exists
if not exist "Main.java" (
    echo.
    echo [ERROR] Blood of the Rift main class not found.
    echo Expected: %GAME_LOCATION%\src\Main.java
    echo Current: %CD%
    echo.
    pause
    exit /b 1
)

echo [SUCCESS] Blood of the Rift source located: %CD%\Main.java
echo [JAVA] Using Java: %JAVA_CMD%
echo [JAVAC] Using Compiler: %JAVAC_CMD%
echo.

REM ALWAYS recompile to ensure version compatibility
echo [INFO] Cleaning old compiled files to prevent version conflicts...
if exist "*.class" (
    del "*.class" >nul 2>&1
    echo [CLEAN] Removed old .class files
)

REM Compile all Java files with JDK 24
echo.
echo =====================================================
echo      COMPILING BLOOD OF THE RIFT WITH JDK 24.0.1
echo =====================================================
echo.
echo Command: %JAVAC_CMD% *.java
echo Directory: %CD%
echo.

"%JAVAC_CMD%" *.java

REM Check compilation success
if errorlevel 1 (
    echo.
    echo [ERROR] Blood of the Rift compilation failed!
    echo.
    echo This might be due to:
    echo - Syntax errors in Java source files
    echo - Missing dependencies
    echo - Incompatible code constructs
    echo.
    echo Please check the source code for errors.
    echo.
    pause
    exit /b 1
)

echo [SUCCESS] Blood of the Rift compiled successfully with JDK 24!
echo [OUTPUT] All .class files created with correct version
echo.

REM Run the game with the same Java version
echo =====================================================
echo             LAUNCHING BLOOD OF THE RIFT
echo =====================================================
echo.
echo Command: %JAVA_CMD% Main
echo Java Version: JDK 24.0.1
echo Directory: %CD%
echo Game Location: %GAME_LOCATION%
echo.
echo Starting Blood of the Rift in 3 seconds...
timeout /t 2 /nobreak >nul

REM Clear screen for clean game start
cls
echo.
echo *** BLOOD OF THE RIFT STARTING ***
echo *** Using JDK 24.0.1 - No Version Conflicts ***
echo.

REM Run the Main class with JDK 24
"%JAVA_CMD%" Main

REM Game execution completed
echo.
echo.
echo =====================================================
echo         BLOOD OF THE RIFT SESSION COMPLETED
echo =====================================================
echo.
echo Thank you for playing Blood of the Rift!
echo.
echo [INFO] Game files: %GAME_LOCATION%
echo [INFO] Compiled with: JDK 24.0.1
echo [INFO] All .class files saved for future runs
echo.

REM Cleanup temporary files but keep JDK
if "%JDK_DOWNLOADED%"=="YES" (
    echo [CLEANUP] Cleaning up temporary download files...
    if exist "%JDK_DIR%\jdk24.zip" del "%JDK_DIR%\jdk24.zip" >nul 2>&1
    echo [INFO] JDK 24 remains available for future game sessions
    echo [INFO] No more downloads needed for future runs!
    echo.
)

echo Thanks for playing Blood of the Rift!
echo Press any key to close...
pause >nul

exit /b 0