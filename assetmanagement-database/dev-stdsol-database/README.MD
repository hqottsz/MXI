Setting up Jasper Server/Database:
---

Note: Jasper is not yet avaiable as part of am-smoke builds when running in the build farm. Once it is, we will be able to start running jasper tests as part of am-smoke.

1. Ensure existing running Jasper services or container are disabled or stopped on your machine.
2. Ensure docker is enabled on your machine.
3. Docker must be enabled for your development/test environment (useDocker=true).
4. Enable containerized Jasper for your environment (jasperDocker=true). (Ref: http://rdwiki/display/DevGuide/Gradle+Property+Override)

Option 1 - Adding to existing Maintenix database & server:

1. Set up the jasper database inside the stdsol database container by running the following command "gradlew assetmanagement-database:dev-stdsol-database:setupJasperDatabase".
2. Update your Maintenix server properties to point to the Jasper server using the command "gradlew assetmanagement-database:dev-stdsol-database:updateJasperProperties".
3. Start the jasper server using the command "gradlew assetmanagement-database:dev-stdsol-database:startJasper".
4. Stop the jasper server using the command "gradlew assetmanagement-database:dev-stdsol-database:stopJasper".

Option 2 - Building new Maintenix database & server:

1. After enabling containerized jasper run build-dev-stdsol-database-and-server.bat. This step sets up the jasper database inside the integ database container and sets your Maintenix server properties to point to the Jasper server.
2. Start the jasper server using the command "gradlew assetmanagement-database:dev-stdsol-database:startJasper".
3. Stop the jasper server using the command "gradlew assetmanagement-database:dev-stdsol-database:stopJasper".