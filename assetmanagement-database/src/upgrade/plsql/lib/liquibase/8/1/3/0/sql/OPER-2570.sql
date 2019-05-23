--liquibase formatted sql


--changeSet OPER-2570:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_ACTIVITY add (COMPLETION_PCT Number(5,2))
   ');
END;
/

--changeSet OPER-2570:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_WP add (COMPLETION_PCT Number(5,2))
   ');
END;
/

--changeSet OPER-2570:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_TASK add (TASK_STATUS Varchar2 (16))
   ');
END;
/

--changeSet OPER-2570:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_TASK add (PAUSE_REASON Varchar2 (16))
   ');
END;
/