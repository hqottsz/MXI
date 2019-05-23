@ECHO OFF
ECHO.
ECHO *** Performing check for Invalid Objects in the Data Loader
ECHO.
ECHO *   Prepare zip-ditributions for core database and for Data Loader
ECHO.
ECHO *   Cleaning up the project "assetmanagement-database-installer"
ECHO.
CALL ..\gradlew.bat :assetmanagement-database-installer:clean
IF ERRORLEVEL 1 GOTO the_exit
ECHO.
ECHO *   Creating zip-distribution of core AM database from the local branch
ECHO.
CALL ..\gradlew.bat :assetmanagement-database-installer:distWindows
IF ERRORLEVEL 1 GOTO the_exit
ECHO.
ECHO *   Cleaning up the project "am-data-loader"
ECHO.
CALL ..\gradlew.bat :am-data-loader:clean
IF ERRORLEVEL 1 GOTO the_exit
ECHO.
ECHO *   Creating zip-distribution of Data Loader from the local branch
ECHO.
CALL ..\gradlew.bat :am-data-loader:build
IF ERRORLEVEL 1 GOTO the_exit
ECHO.
CALL ..\gradlew.bat :am-data-loader-test:testDlInvalidObjects
ECHO.
:the_exit
%_NO_PAUSE% PAUSE
