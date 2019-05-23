@ECHO OFF
CALL ..\gradlew.bat :am-data-loader-test:testDlUninstallLeftovers
%_NO_PAUSE% PAUSE