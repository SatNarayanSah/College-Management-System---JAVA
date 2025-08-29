@echo off
echo Cleaning build directory...
rd /s /q build 2>nul
mkdir build\config

echo Copying resources...
copy src\main\resources\config\database.properties build\config\

echo Compiling Java files...
javac -d build -sourcepath src/main/java -cp "lib/*" src/main/java/com/cms/Main.java src/main/java/com/cms/gui/*.java src/main/java/com/cms/dao/*.java src/main/java/com/cms/model/*.java

if %errorlevel% equ 0 (
    echo Build successful!
    echo Running the application...
    java -cp "build;lib/*" com.cms.Main
) else (
    echo Build failed!
    pause
)
