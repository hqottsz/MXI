--liquibase formatted sql

--changeSet OPER-21491:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
	'ENABLE_MARK_JOB_CARD_STEP_NOT_APPLICABLE',
	'MXWEB',
	'Allows users to mark job card steps as not applicable.',
	'GLOBAL',
	'TRUE/FALSE',
	'TRUE',
	1,
	'MXWEB',
	'8.3',
	0
   );
   
END;
/
