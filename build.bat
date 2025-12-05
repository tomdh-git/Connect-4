@echo off
setlocal

echo Cleaning build directory...
if exist build rmdir /s /q build
mkdir build\classes

echo Compiling source code...
powershell -Command "Get-ChildItem -Path src\main\java -Recurse -Filter *.java | ForEach-Object { '\"' + $_.FullName.Replace('\', '/') + '\"' } | Out-File -Encoding ASCII sources.txt"
javac -d build\classes @sources.txt
if %errorlevel% neq 0 (
    echo Compilation failed!
    del sources.txt
    pause
    exit /b %errorlevel%
)
del sources.txt

echo Creating JAR file...
set JAR_CMD=jar
where jar >nul 2>nul
if %errorlevel% neq 0 (
    echo 'jar' command not found in PATH. Trying to locate it...
    if exist "C:\Program Files\Java\jdk-17\bin\jar.exe" (
        set JAR_CMD="C:\Program Files\Java\jdk-17\bin\jar.exe"
    ) else (
        echo Could not find jar.exe. Please ensure JDK is installed and in PATH.
        pause
        exit /b 1
    )
)

%JAR_CMD% cfe Connect-4.jar com.connect4.Connect4Game -C build/classes .

echo.
echo Creating Single Executable (EXE)...

REM Find CSC (C# Compiler)
set CSC_CMD=csc
where csc >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe" (
        set CSC_CMD="C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe"
    ) else (
        echo Could not find csc.exe. Cannot create EXE wrapper.
        echo Connect-4.jar has been created successfully, though.
        pause
        exit /b 1
    )
)

%CSC_CMD% /target:exe /out:Connect-4.exe /resource:Connect-4.jar Launcher.cs
if %errorlevel% neq 0 (
    echo Failed to create EXE.
    pause
    exit /b %errorlevel%
)

echo.
echo Build complete! Connect-4.exe created.
echo You can now distribute Connect-4.exe as a single file.
pause
