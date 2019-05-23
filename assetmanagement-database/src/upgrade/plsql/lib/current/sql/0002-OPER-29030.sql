--liquibase formatted sql

--changeSet OPER-29030:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('BULK_RECORD_INDUCTION_MODE');
END;
/

--changeSet OPER-29030:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.action_parm_delete('APP_RECORDS_INDUCTION');
END;
/