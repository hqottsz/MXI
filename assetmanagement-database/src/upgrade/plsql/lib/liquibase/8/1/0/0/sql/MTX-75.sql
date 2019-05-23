--liquibase formatted sql


--changeSet MTX-75:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table PPC_PLAN add CONTROL_USER_ID Number(10,0)
');
END;
/

--changeSet MTX-75:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table PPC_PLAN add CONTROL_DT Date
');
END;
/

--changeSet MTX-75:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PPC_PLAN" add Constraint "FK_PPCPLAN_CONTROLUSER" foreign key ("CONTROL_USER_ID") references "UTL_USER" ("USER_ID")  DEFERRABLE
');
END;
/