--liquibase formatted sql

--changeSet OPER-22906:1 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'EVT_STAGE' AND column_name = 'SYSTEM_STAGE_NOTE';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('EVT_STAGE');
  
  -- in a single change set because we don't want to drop the column if the data didn't migrate
  update /*+ PARALLEL */ evt_stage set user_stage_note = system_stage_note where user_stage_note is null and system_stage_note is not null;
  
   --log the number of rows that were updated by the previous SQL statement
   DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT ||
                        ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) ||
                        ' in the evt_stage table.');
  
  -- no need to validate constraints since were moving data between two unconstrained columns
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('EVT_STAGE', FALSE);
END;
/

--changeSet OPER-22906:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
    l_defer boolean;
BEGIN 
    l_defer := upg_migr_schema_v1_pkg.get_defer_column_drop_param;
	
	-- due to performance concerns, this column should always be disabled rather than dropped.
	upg_migr_schema_v1_pkg.set_defer_column_drop_param(TRUE);
	upg_migr_schema_v1_pkg.table_column_drop('EVT_STAGE','SYSTEM_STAGE_NOTE');
	
	upg_migr_schema_v1_pkg.set_defer_column_drop_param(l_defer);
END;
/

--changeSet OPER-22906:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_column_rename('EVT_STAGE','USER_STAGE_NOTE','STAGE_NOTE');
END;
/


