@echo off
rem Robust run script: ensure correct working dir, check Java, compile all sources, run app

rem Change to the script directory (project root for nested NoteApp)
cd /d "%~dp0"

echo Working directory: %CD%

rem Check for java and javac
where java >nul 2>&1
if errorlevel 1 (
	echo ERROR: 'java' not found in PATH. Install a JDK or set JAVA_HOME and update PATH.
	goto end
)
where javac >nul 2>&1
if errorlevel 1 (
	echo ERROR: 'javac' not found in PATH. Install a JDK or set JAVA_HOME and update PATH.
	goto end
)

echo Compiling Note App...

rem Ensure build directories exist
if not exist build\classes (
	if not exist build mkdir build
	mkdir build\classes
)

rem Gather all Java sources into a list file (produce relative paths)
if exist build\sources.txt del /q build\sources.txt
setlocal ENABLEDELAYEDEXPANSION
set "CUR_DIR=%CD%"
for /r "src\main\java" %%f in (*.java) do (
	set "p=%%f"
	set "p=!p:%CUR_DIR%\=!"
	echo !p!>> build\sources.txt
)
endlocal

if not exist build\sources.txt (
	echo ERROR: No Java sources found under src\main\java
	goto end
)

rem Compile using the collected sources
javac -d build\classes @build\sources.txt
if errorlevel 1 (
	echo Compilation failed. See errors above.
	goto end
)

echo Running Note App...
java -cp build\classes com.noteapp.NoteAppMain

:end
rem Cleanup temporary sources list
if exist build\sources.txt del /q build\sources.txt >nul 2>&1
