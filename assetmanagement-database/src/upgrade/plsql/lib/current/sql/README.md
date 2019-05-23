# Upgrade Script Filenames
The build will dynamically generate the control file for this folder based on the upgrade script filenames; therefore, the upgrade scripts in this folder must be named using the following format to set the order in which they should be ran.

ATTENTION: Upgrade scripts must generally be named to run after pre-existing upgrade scripts by using the next available OrderIndex.

## Format:
- `<OrderIndex>-<JiraItem>-<SubIndex>.<Ext>`
- Example: `0027-OPER-12345-001.sql`

#### \<OrderIndex\> :
- Use highest OrderIndex in the folder plus one (to run the new upgrade scripts after pre-existing upgrade scripts).
- Must be zero padded to four digits followed by a hyphen (validated during the build).
- The "0027-" value in the above example indicates that this upgrade script should be ran after the oldest pre-existing upgrade script in the directory (using 0026).

#### \<JiraItem\> :
- Include the JIRA item code.
- The "OPER-12345" value in the above example indicates that this upgrade script was generated for OPER-12345.

#### \<SubIndex\> :
- This field allows us to specify the run order when we add multiple upgrade scripts at the same time for the same OrderIndex and JiraItem.
- Should begin with a hyphen and be zero padded to three digits to prevent sorting problems if the subindex exceeds nine.
- The "-001" value in the above example indicates that it is the first upgrade script for this OrderIndex and JiraItem combination.

#### \<Ext\> :
- The file extension indicates the contents of the upgrade script.
- The ".sql" value in the above example indicates that this is a Liquibase Formatted SQL file.

