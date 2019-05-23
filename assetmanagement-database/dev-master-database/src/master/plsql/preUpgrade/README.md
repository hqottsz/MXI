## Summary
How to run pre-upgrade SQL statements in the development database scripts to cleanup data problems


## Problem
If we add an upgrade script that adds a new constraint to the database and the data cannot be fixed in the upgrade script (because customer input is required) then we include a pre-check script to raise these issues to allow the customer to fix the data problem prior to running the upgrade. However, some database components import the SAS.DMPX, MASTER.DMPX or MASTER_STDSOL.DMPX from the previous release prior to running the upgrade so how do we fix data problems in this data between the step that imports the data and the step that runs the database upgrade?

## Solution
The following script has been added to allow us to add SQL statement that should be ran between the import and database upgrade:
* http://rdstash/projects/AM/repos/am/browse/assetmanagement-database/dev-master-database/src/master/plsql/preUpgrade/preUpgradeCleanUp.sql

The following gradle files that import the SAS.DMPX, MASTER.DMPX or MASTER_STDSOL.DMPX files have been modified to call the above script to process the cleanup prior to running the database upgrade:
* http://rdstash/projects/AM/repos/am/browse/assetmanagement-database/dev-master-database/dev-master-database.gradle
* http://rdstash/projects/AM/repos/am/browse/assetmanagement-database/dev-stdsol-database/dev-stdsol-database.gradle
* http://rdstash/projects/AM/repos/am/browse/assetmanagement-database/sas-database/sas-database.gradle
* http://rdstash/projects/AM/repos/am/browse/mxwebtest/mxwebtest.gradle
* http://rdstash/projects/AM/repos/am/browse/testingendejb/testingendejb.gradle

Please note that the above components call the same file so that we don't have to maintain multiple versions of the preUpgradeCleanUp.sql script. This file will be cleared out during the build release steps each time we modify a branch to start using the exports from the latest release (since the cleanup will have already happened in the new exports).

WARNING: Only include SQL statements that should be ran during the generation of the next release of the Standard Airline Solution database (SAS.DMPX), Development Master Integ database (MASTER.DMPX) or Development Master Standard Solution database (MASTER_STDSOL.DMPX) exports.
