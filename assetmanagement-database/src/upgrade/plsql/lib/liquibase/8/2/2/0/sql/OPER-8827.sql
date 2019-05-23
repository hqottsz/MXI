--liquibase formatted sql

--changeSet OPER-8827:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Remove action parameters ACTION_DEFER_FAULT,ACTION_LOG_FAULT_AND_DEFER
BEGIN  
   utl_migr_data_pkg.action_parm_delete(
      p_parm_name        => 'ACTION_DEFER_FAULT'
   );
END;
/