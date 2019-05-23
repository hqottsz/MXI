am-e2e-test 
===

This repository contains the end-to-end tests for Asset Management

<table>
  <thead>
    <tr>
      <th>Folder</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>src/main/data/actuals</td>
      <td>Contains source data to be loaded by Actuals Loader when running build-e2e-database.bat in am project. Data is separated into sub-folders by persona and feature to assist in mapping data to specific tests.</td>
    </tr>
    <tr>
      <td>src/main/data/baseline</td>
      <td>Contains source data to be loaded by Configurator when running build-e2e-database.bat in am project. Data is separated into sub-folders by persona and feature to assist in mapping data to specific tests.</td>
    </tr>
    <tr>
      <td>src/main/data/sql</td>
      <td>Contains sql scripts that will be executed when running build-e2e-database.bat in am project. These scripts are required to assist with e2e data setup.</td>
    </tr>
    <tr>
      <td>src/main/java</td>
      <td>Contains all Cucumber implementation files and Cucumber Step Definitions. Step Definitions are separated into sub-folders by persona.</td>
    </tr>
    <tr>
      <td>src/main/resources</td>
      <td>Contains all Cucumber Feature files. Features are separated into sub-folders by persona.</td>
    </tr>
    <tr>
      <td>src/main/sampleData</td>
      <td>Contains sample templates for actuals data.</td>
    </tr>
  </tbody>
</table>


Setting up e2e environment:
---

1. Pull latest version of AM repository using GIT
2. Run build-e2e-server.bat
3. Run build-e2e-database.bat
4. Run setup-database-properties.bat (required for any tests using database drivers)
5. Start local e2e server with am-e2e-test\build\environment\e2e\domain\startWebLogic.cmd (recommended method over starting server from eclipse)
6. Connect to Maintenix environment at http://localhost/maintenix


Running e2e tests locally thru gradle:
---

1. Perform steps from "Setting up e2e environment".
2. Open a gradle command window at root of AM project and type: "gradlew am-e2e-test:envTest"

|Test Runner                  | Command                                        | Description                                                      |
|---------------------------- | ---------------------------------------------- | -----------------------------------------------------------------|
|  smokeTest                  | gradlew am-e2e-test:smokeTest                  | Runs e2e-smoke (from am-smoke) and generates overview report     |
|  envTest                    | gradlew am-e2e-test:envTest                    | Runs envTest build - Same scope of tests used by overnight build |
|  annoTest                   | gradlew am-e2e-test:annoTest                   | Runs all @RunThis tests in sequence                              |
|  ppcTest                    | gradlew am-e2e-test:ppcTest                    | Runs all PPC tests in sequence                                   |
|  checkAllE2ETestAnnotations | gradlew am-e2e-test:checkAllE2ETestAnnotations | Checks feature files to ensure they have expected annotations    |


Selecting a different browser (gradle only):
---

The following browser names can be used:
- chrome
- chrome_headless
- chrome_docker**
- firefox
- firefox_docker**
- ie32
- ie64
- edge
- phantomjs
- safari

** Note: Docker setup and configuration must be done in order to support docker contained browsers.

Via gradle.properties:
To permanently run tests with a specific browser via gradle modify your gradle.properties file at C:\Users\<your username>\.gradle and add the following:
testBrowser=firefox_docker

Via Gradle command line:
To run tests with a specific browser via gradle add the following to the end of command:   "-Dwebdriver.browser=chrome"
For example: "gradlew am-e2e-test:envTest -Dwebdriver.browser=chrome"



Running tests or test suites locally thru eclipse:
---

1. Perform steps from "Setting up e2e environment".
2. Run setup-eclipse-workspace.bat
3. Open eclipse
4. Import Existing Projects into Workspace
5. Run e2e tests via jUnit using Run"TestType"CucumberTests.java from folder am-e2e-test\src\main\java\com\mxi\am


All test results and reports can be accessed in the following location:
---
**am-e2e-test\build\reports**


Setting up Jasper Server/Database:
---

Note: Jasper is not yet avaiable as part of am-smoke builds when running in the build farm. Once it is, we will be able to start running jasper tests as part of am-smoke.

1. Ensure existing running Jasper services or containers are disabled or stopped on your machine.
2. Ensure docker is enabled on your machine.
3. Docker must be enabled for your development/test environment (useDocker=true).
4. Enable containerized Jasper for your environment (jasperDocker=true). (Ref: http://rdwiki/display/DevGuide/Gradle+Property+Override)

Option 1 - Adding to existing Maintenix database & server:

1. Set up the jasper database inside the e2e database container by running the following command "gradlew am-e2e-test:setupJasperDatabase".
2. Update your Maintenix server properties to point to the Jasper server using the command "gradlew am-e2e-test:updateJasperProperties".
3. Start the jasper server using the command "gradlew am-e2e-test:startJasper".
4. Stop the jasper server using the command "gradlew am-e2e-test:stopJasper".

Option 2 - Building new Maintenix database & server:

1. After enabling containerized jasper run build-e2e-database-and-server.bat. This step sets up the jasper database inside the integ database container and sets your Maintenix server properties to point to the Jasper server.
2. Start the jasper server using the command "gradlew am-e2e-test:startJasper".
3. Stop the jasper server using the command "gradlew am-e2e-test:stopJasper".