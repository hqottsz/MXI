--liquibase formatted sql


--changeSet MX-18467:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table SCHED_LABOUR modify (
    "STAGE_REASON_DB_ID" Number(10,0) Check (STAGE_REASON_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 / 

--changeSet MX-18467:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_KIT_MAP', 'FK_INVINV_SCHEDKITMAP');
END;
/

--changeSet MX-18467:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_INVINV_SCHEDKITMAP" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/ 

--changeSet MX-18467:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_LABOUR', 'FK_REFSTAGEREASON_SCHDLBR');
END;
/

--changeSet MX-18467:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_LABOUR" add Constraint "FK_REFSTAGEREASON_SCHDLBR" foreign key ("STAGE_REASON_DB_ID","STAGE_REASON_CD") references "REF_STAGE_REASON" ("STAGE_REASON_DB_ID","STAGE_REASON_CD")  DEFERRABLE
');
END;
/ 

--changeSet MX-18467:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_KIT_MAP', 'FK_SCHEDSTASK_SCHEDKITMAP');
END;
/

--changeSet MX-18467:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_SCHEDSTASK_SCHEDKITMAP" foreign key ("SCHED_DB_ID","SCHED_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID")  DEFERRABLE
');
END;
/ 

--changeSet MX-18467:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_KIT_MAP', 'FK_INVKIT_SCHEDKITMAP');
END;
/

--changeSet MX-18467:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_INVKIT_SCHEDKITMAP" foreign key ("KIT_INV_NO_DB_ID","KIT_INV_NO_ID") references "INV_KIT" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/