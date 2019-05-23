am-data-loader-test Module
===

This module contains the data loader (baseline and actuals loader) tests that require a database with baseline loader and actuals loader installed.

To build the test environment and run all dataloader tests
===
1. Pull latest version of am_git repository using GIT
2. Execute gradle commands as followings: 
- gradlew :am-data-loader-test:clean
- gradlew :am-data-loader-test:build
- gradlew :am-data-loader-test:envSetUp
- gradlew :am-data-loader-test:envTest
3. Test results are available at: am/am-data-loader-test/build/reports/tests


Setup local environment for development
===
1. Pull latest version of am_git repository using GIT
2. Execute following to setup eclipse workspace and setup integ junit properties
   2.1 Run setup-eclipse-workspace.bat
   2.2 Run setup-integ-junit-properties.bat
3. Execute gradle commands as followings:
	3.1 gradlew :am-data-loader-test:envSetup
	3.2 gradlew :am-data-loader-test:installCore
4. Run eclipse.bat in am_git directory
5. Setup database information in test.data.loader.config.proeperties.

To test from Eclipse:
===
1. Follow steps above "Setup local environment for development"
2. Run *.java as Junit Test in Eclipse

Gradle Commands references:
===
#gradle tasks through command line in am folder

#to clean gradle project folder
gradlew :am-data-loader-test:clean

#to build gradle project folder
gradlew :am-data-loader-test:build

#to set up a empty database run
gradlew :am-data-loader-test:envSetup

#to install data loader in empty database run
gradlew :am-data-loader-test:installCore

#to install dataloader and run all tests run (must be done after setting up database)
gradlew :am-data-loader-test:envTest

#to get all gradlew tasks
gradlew tasks