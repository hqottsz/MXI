--liquibase formatted sql

--changeSet OPER-10589:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Remove action parameters ACTION_TASK_ADD_NOTE
BEGIN  
   utl_migr_data_pkg.action_parm_delete(
      p_parm_name        => 'ACTION_TASK_ADD_NOTE'
   );
END;
/