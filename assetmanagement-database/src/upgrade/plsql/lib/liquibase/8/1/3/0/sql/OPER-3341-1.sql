--liquibase formatted sql


--changeSet OPER-3341-1:1 stripComments:false
-- Data model change required for OPER-3341. 
-- A new mandatory column REQ_RES_CT will be added to the SCHED_LABOUR and PPC_HR_SLOT table.
-- Default value is 1.
ALTER TRIGGER TUBR_SCHED_LABOUR DISABLE;

--changeSet OPER-3341-1:2 stripComments:false
ALTER TRIGGER TUBR_PPC_HR_SLOT DISABLE;

--changeSet OPER-3341-1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE sched_labour ADD (
        req_res_ct Float
      )
   ');

   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE ppc_hr_slot ADD (
        req_res_ct Float
      )
   ');

END;
/

--changeSet OPER-3341-1:4 stripComments:false
UPDATE sched_labour SET req_res_ct = 1.0 WHERE req_res_ct IS NULL;

--changeSet OPER-3341-1:5 stripComments:false
UPDATE ppc_hr_slot SET req_res_ct = 1.0 WHERE req_res_ct IS NULL;

--changeSet OPER-3341-1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE sched_labour MODIFY (
        req_res_ct Float Default 1.0 NOT NULL DEFERRABLE
      )
   ');

   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_slot MODIFY (
        req_res_ct Float Default 1.0 NOT NULL DEFERRABLE
      )
   ');
END;
/

--changeSet OPER-3341-1:7 stripComments:false
ALTER TRIGGER TUBR_SCHED_LABOUR ENABLE;

--changeSet OPER-3341-1:8 stripComments:false
ALTER TRIGGER TUBR_PPC_HR_SLOT ENABLE;