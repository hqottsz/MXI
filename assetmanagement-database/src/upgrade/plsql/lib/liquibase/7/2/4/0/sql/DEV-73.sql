--liquibase formatted sql


--changeSet DEV-73:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INVLOCSHIFT_LOC_FK');
END;
/

--changeSet DEV-73:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ORG_HR_SCHEDULE', 'FK_INVLOCSCHED_ORGHRSCHED');
END;
/

--changeSet DEV-73:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ORG_HR_SHIFT', 'FK_INVLOCSHIFT_ORGHRSHIFT');
END;
/

--changeSet DEV-73:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ORG_HR_SHIFT_PLAN', 'FK_INVLOCSHIFT_ORGHRSHIFTPLAN');
END;
/  

--changeSet DEV-73:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_INV_LOC_SCHEDULE');
END;
/

--changeSet DEV-73:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_INV_LOC_SCHEDULE');
END;
/

--changeSet DEV-73:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_INV_LOC_SCHEDULE_SHIFT');
END;
/

--changeSet DEV-73:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_INV_LOC_SCHEDULE_SHIFT');
END;
/

--changeSet DEV-73:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_INV_LOC_SHIFT');
END;
/

--changeSet DEV-73:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_INV_LOC_SHIFT');
END;
/

--changeSet DEV-73:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('INV_LOC_SCHEDULE_SHIFT'); 
END;
/

--changeSet DEV-73:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('INV_LOC_SCHEDULE');
END;
/

--changeSet DEV-73:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('INV_LOC_SHIFT');
END;
/ 

--changeSet DEV-73:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_HR_SCHEDULE', 'SCHEDULE_DB_ID');
END;
/

--changeSet DEV-73:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_HR_SCHEDULE', 'SCHEDULE_ID');
END;
/ 

--changeSet DEV-73:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SHIFT" add Constraint "FK_SHIFTTSHIFT_ORGHRSHIFT" foreign key ("SHIFT_DB_ID","SHIFT_ID") references "SHIFT_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-73:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SHIFT_PLAN" add Constraint "FK_SHIFTSHIFT_ORGHRSHIFTPLAN" foreign key ("SHIFT_DB_ID","SHIFT_ID") references "SHIFT_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")  DEFERRABLE
');
END;
/ 