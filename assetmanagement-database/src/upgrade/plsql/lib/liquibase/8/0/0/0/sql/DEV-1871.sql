--liquibase formatted sql


--changeSet DEV-1871:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop foreign key constrains with REF_STEP_STATUS
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_NN_DROP('LRP_EVENT', 'DURATION_DAYS'); 
END;
/

--changeSet DEV-1871:2 stripComments:false
update lrp_event set duration_days = null;