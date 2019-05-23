--liquibase formatted sql

--changeSet OPER-29931:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'ACTION_PACKAGE_AND_COMPLETE_TASK_ENTER_USAGE',
         'Permission to package and complete tasks by clicking the Package And Complete Task And Enter Usage button on the Open Tasks tab of the Inventory Details page. When you click the button, the Complete Task page opens and you can enter location, date, and usage information for the parent and all sub-assemblies.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'Maint - Historic Editing',
         '8.3-SP2',
         0,
         0
      );
END;
/