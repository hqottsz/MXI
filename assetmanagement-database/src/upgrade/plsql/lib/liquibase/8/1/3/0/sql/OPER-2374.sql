--liquibase formatted sql


--changeSet OPER-2374:1 stripComments:false
ALTER TRIGGER tubr_ppc_actvty_snapshot DISABLE;

--changeSet OPER-2374:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_ACTVTY_SNAPSHOT add (TOTAL_SCHED_HR Number(8,2))
   ');
END;
/

--changeSet OPER-2374:3 stripComments:false
UPDATE ppc_actvty_snapshot SET total_sched_hr=(end_dt-start_dt)*24;

--changeSet OPER-2374:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_ACTVTY_SNAPSHOT modify (TOTAL_SCHED_HR Number(8,2) NOT NULL DEFERRABLE)
   ');
END;
/

--changeSet OPER-2374:5 stripComments:false
ALTER TRIGGER tubr_ppc_actvty_snapshot ENABLE;