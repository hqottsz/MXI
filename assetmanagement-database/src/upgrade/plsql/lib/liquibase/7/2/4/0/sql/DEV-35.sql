--liquibase formatted sql


--changeSet DEV-35:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT add (
    "LOC_DB_ID" Number(10,0) Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT add (
    "LOC_ID" Number(10,0) Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SCHEDULE modify (
    "SCHEDULE_DB_ID" Number(10,0) Check (SCHEDULE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /

--changeSet DEV-35:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SCHEDULE modify (
    "SCHEDULE_ID" Number(10,0) Check (SCHEDULE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
  )
  ');
 END;
 /  

--changeSet DEV-35:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SCHEDULE add (
    "USER_SHIFT_PATTERN_DB_ID" Number(10,0) Check (USER_SHIFT_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SCHEDULE add (
    "USER_SHIFT_PATTERN_ID" Number(10,0) Check (USER_SHIFT_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SCHEDULE add (
    "LOC_DB_ID" Number(10,0) Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SCHEDULE add (
    "LOC_ID" Number(10,0) Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "SHIFT_SHIFT" (
    "SHIFT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SHIFT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SHIFT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SHIFT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SHIFT_CD" Varchar2 (80) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
    "SHIFT_NAME" Varchar2 (240) NOT NULL DEFERRABLE ,
    "START_HOUR" Number(6,2) NOT NULL DEFERRABLE ,
    "DURATION_QT" Float NOT NULL DEFERRABLE ,
    "WORK_HOURS_QT" Float NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_SHIFT_SHIFT" primary key ("SHIFT_DB_ID","SHIFT_ID") 
) 
');
END;
/

--changeSet DEV-35:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "USER_SHIFT_PATTERN" (
    "USER_SHIFT_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (USER_SHIFT_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USER_SHIFT_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (USER_SHIFT_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USER_SHIFT_PATTERN_CD" Varchar2 (80) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
    "USER_SHIFT_PATTERN_NAME" Varchar2 (240) NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_USER_SHIFT_PATTERN" primary key ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID") 
) 
');
END;
/

--changeSet DEV-35:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "USER_SHIFT_PATTERN_DAY" (
    "USER_SHIFT_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (USER_SHIFT_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USER_SHIFT_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (USER_SHIFT_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USER_SHIFT_PATTERN_DAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_USER_SHIFT_PATTERN_DAY" primary key ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID","USER_SHIFT_PATTERN_DAY_ORD") 
) 
');
END;
/

--changeSet DEV-35:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "USER_SHIFT_PATTERN_DAY_SHIFT" (
    "USER_SHIFT_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (USER_SHIFT_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USER_SHIFT_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (USER_SHIFT_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USER_SHIFT_PATTERN_DAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "SHIFT_DB_ID" Number(10,0) Check (SHIFT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SHIFT_ID" Number(10,0) Check (SHIFT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_USER_SHIFT_PATTERN_DAY_SHIF" primary key ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID","USER_SHIFT_PATTERN_DAY_ORD") 
) 
');
END;
/

--changeSet DEV-35:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "CAPACITY_PATTERN" (
    "CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_CD" Varchar2 (80) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
    "CAPACITY_PATTERN_NAME" Varchar2 (240) NOT NULL DEFERRABLE ,
    "CAPACITY_PATTERN_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "CAPACITY_PATTERN_LDESC" Varchar2 (4000),
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_CAPACITY_PATTERN" primary key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID") 
) 
');
END;
/

--changeSet DEV-35:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_CAPACITY_PATTERN_TYPE" (
    "CAPACITY_PATTERN_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "DESC_SDESC" Varchar2 (80) NOT NULL DEFERRABLE ,
    "DESC_LDESC" Varchar2 (4000),
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_CAPACITY_PATTERN_TYPE" primary key ("CAPACITY_PATTERN_TYPE_DB_ID","CAPACITY_PATTERN_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-35:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "CAPACITY_PATTERN_DAY_SHIFT" (
    "CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_DAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "SHIFT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SHIFT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SHIFT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SHIFT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_CAPACITY_PATTERN_DAY_SHIFT" primary key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD","SHIFT_DB_ID","SHIFT_ID") 
) 
');
END;
/

--changeSet DEV-35:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "CAPACITY_PATTERN_DAY" (
    "CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_DAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_CAPACITY_PATTERN_DAY" primary key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD") 
) 
');
END;
/

--changeSet DEV-35:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "CAPACITY_PATTERN_SKILL" (
    "CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LABOUR_SKILL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LABOUR_SKILL_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_CAPACITY_PATTERN_SKILL" primary key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") 
) 
');
END;
/

--changeSet DEV-35:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "CAPACITY_PATTERN_DAY_SKILL" (
    "CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CAPACITY_PATTERN_DAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "SHIFT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SHIFT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SHIFT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SHIFT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LABOUR_SKILL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LABOUR_SKILL_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "LIC_RESOURCE_QT" Float Default 0 NOT NULL DEFERRABLE ,
    "UNLIC_RESOURCE_QT" Float Default 0 NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_CAPACITY_PATTERN_DAY_SKILL" primary key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD","SHIFT_DB_ID","SHIFT_ID","LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") 
) 
');
END;
/

--changeSet DEV-35:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SCHEDULE" add Constraint "FK_INVLOC_ORGHRSCHEDULE" foreign key ("LOC_DB_ID","LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SHIFT" add Constraint "FK_INVLOC_ORGHRSHIFT" foreign key ("LOC_DB_ID","LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_SKILL" add Constraint "FK_REFLABOURSKILL_CPSKILL" foreign key ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") references "REF_LABOUR_SKILL" ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY_SKILL" add Constraint "FK_REFLABOURSKILL_CPDAYSKILL" foreign key ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") references "REF_LABOUR_SKILL" ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SHIFT_SHIFT" add Constraint "FK_MIMRSTAT_SHIFTSHIFT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USER_SHIFT_PATTERN" add Constraint "FK_MIMRSTAT_USERSHIFTPATTERN" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USER_SHIFT_PATTERN_DAY" add Constraint "FK_MIMRSTAT_USPDAY" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USER_SHIFT_PATTERN_DAY_SHIFT" add Constraint "FK_MIMRSTAT_USPDAYSHIFT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_CAPACITY_PATTERN_TYPE" add Constraint "FK_MIMRSTAT_REFCPTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN" add Constraint "FK_MIMRSTAT_CAPACITYPATTERN" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY" add Constraint "FK_MIMRSTAT_CPDAY" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY_SHIFT" add Constraint "FK_MIMRSTAT_CPDAYSHIFT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_SKILL" add Constraint "FK_MIMRSTAT_CPSKILL" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY_SKILL" add Constraint "FK_MIMRSTAT_CPDAYSKILL" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USER_SHIFT_PATTERN_DAY_SHIFT" add Constraint "FK_SHIFTSHIFT_USPDAYSHIFT" foreign key ("SHIFT_DB_ID","SHIFT_ID") references "SHIFT_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY_SHIFT" add Constraint "FK_SHIFTSHIFT_CPDAYSHIFT" foreign key ("SHIFT_DB_ID","SHIFT_ID") references "SHIFT_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USER_SHIFT_PATTERN_DAY" add Constraint "FK_USPATTERN_USPATTERNDAY" foreign key ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID") references "USER_SHIFT_PATTERN" ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SCHEDULE" add Constraint "FK_USP_ORGHRSCHEDULE" foreign key ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID") references "USER_SHIFT_PATTERN" ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USER_SHIFT_PATTERN_DAY_SHIFT" add Constraint "FK_USPDAY_USPDAYSHIFT" foreign key ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID","USER_SHIFT_PATTERN_DAY_ORD") references "USER_SHIFT_PATTERN_DAY" ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID","USER_SHIFT_PATTERN_DAY_ORD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY" add Constraint "FK_CP_CPDAY" foreign key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID") references "CAPACITY_PATTERN" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_SKILL" add Constraint "FK_CP_CPSKILL" foreign key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID") references "CAPACITY_PATTERN" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN" add Constraint "FK_REFCPTYPE_CP" foreign key ("CAPACITY_PATTERN_TYPE_DB_ID","CAPACITY_PATTERN_TYPE_CD") references "REF_CAPACITY_PATTERN_TYPE" ("CAPACITY_PATTERN_TYPE_DB_ID","CAPACITY_PATTERN_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY_SKILL" add Constraint "FK_CPDAYSHIFT_CPDAYSKILL" foreign key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD","SHIFT_DB_ID","SHIFT_ID") references "CAPACITY_PATTERN_DAY_SHIFT" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD","SHIFT_DB_ID","SHIFT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CAPACITY_PATTERN_DAY_SHIFT" add Constraint "FK_CPDAY_CPDAYSHIFT" foreign key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD") references "CAPACITY_PATTERN_DAY" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD")  DEFERRABLE
');
END;
/

--changeSet DEV-35:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SHIFT_SHIFT" BEFORE INSERT
   ON "SHIFT_SHIFT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SHIFT_SHIFT" BEFORE UPDATE
   ON "SHIFT_SHIFT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_USER_SHIFT_PATTERN" BEFORE INSERT
   ON "USER_SHIFT_PATTERN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_USER_SHIFT_PATTERN" BEFORE UPDATE
   ON "USER_SHIFT_PATTERN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_USER_SHIFT_PATTERN_DAY" BEFORE INSERT
   ON "USER_SHIFT_PATTERN_DAY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_USER_SHIFT_PATTERN_DAY" BEFORE UPDATE
   ON "USER_SHIFT_PATTERN_DAY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_USP_DAY_SHIFT" BEFORE INSERT
   ON "USER_SHIFT_PATTERN_DAY_SHIFT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_USP_DAY_SHIFT" BEFORE UPDATE
   ON "USER_SHIFT_PATTERN_DAY_SHIFT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_CAPACITY_PATTERN" BEFORE INSERT
   ON "CAPACITY_PATTERN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_CAPACITY_PATTERN" BEFORE UPDATE
   ON "CAPACITY_PATTERN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_CP_DAY" BEFORE INSERT
   ON "CAPACITY_PATTERN_DAY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_CP_DAY" BEFORE UPDATE
   ON "CAPACITY_PATTERN_DAY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_CP_DAY_SHIFT" BEFORE INSERT
   ON "CAPACITY_PATTERN_DAY_SHIFT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_CP_DAY_SHIFT" BEFORE UPDATE
   ON "CAPACITY_PATTERN_DAY_SHIFT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_CP_SKILL" BEFORE INSERT
   ON "CAPACITY_PATTERN_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_CP_SKILL" BEFORE UPDATE
   ON "CAPACITY_PATTERN_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_CP_DAY_SKILL" BEFORE INSERT
   ON "CAPACITY_PATTERN_DAY_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_CP_DAY_SKILL" BEFORE UPDATE
   ON "CAPACITY_PATTERN_DAY_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_CPTYPE" BEFORE INSERT
   ON "REF_CAPACITY_PATTERN_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_CPTYPE" BEFORE UPDATE
   ON "REF_CAPACITY_PATTERN_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-35:63 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'SHIFT_ID_SEQ', 1, 'SHIFT_SHIFT', 'SHIFT_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'SHIFT_ID_SEQ');

--changeSet DEV-35:64 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'USER_SHIFT_PATTERN_ID_SEQ', 1, 'USER_SHIFT_PATTERN', 'USER_SHIFT_PATTERN_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'USER_SHIFT_PATTERN_ID_SEQ');

--changeSet DEV-35:65 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'CAPACITY_PATTERN_ID_SEQ', 1, 'CAPACITY_PATTERN', 'CAPACITY_PATTERN_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'CAPACITY_PATTERN_ID_SEQ');

--changeSet DEV-35:66 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('SHIFT_ID_SEQ', 1);
END;
/

--changeSet DEV-35:67 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('USER_SHIFT_PATTERN_ID_SEQ', 1);
END;
/

--changeSet DEV-35:68 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('CAPACITY_PATTERN_ID_SEQ', 1);
END;
/   

--changeSet DEV-35:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SHIFT modify (
    "LOC_DB_ID" Number(10,0) Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /

--changeSet DEV-35:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SHIFT modify (
    "LOC_ID" Number(10,0) Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /

--changeSet DEV-35:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SCHEDULE modify (
    "USER_SHIFT_PATTERN_DB_ID" Number(10,0) Check (USER_SHIFT_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /

--changeSet DEV-35:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SCHEDULE modify (
    "USER_SHIFT_PATTERN_ID" Number(10,0) Check (USER_SHIFT_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
  )
  ');
 END;
 /

--changeSet DEV-35:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SCHEDULE modify (
    "LOC_DB_ID" Number(10,0) Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
  )
  ');
 END;
 /

--changeSet DEV-35:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ORG_HR_SCHEDULE modify (
    "LOC_ID" Number(10,0) Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
  )
  ');
 END;
 /

--changeSet DEV-35:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_LOC add (
    "OVERNIGHT_SHIFT_DB_ID" Number(10,0) Check (OVERNIGHT_SHIFT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_LOC add (
    "OVERNIGHT_SHIFT_ID" Number(10,0) Check (OVERNIGHT_SHIFT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-35:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "INV_LOC" add Constraint "FK_SHIFTSHIFT_INVLOC" foreign key ("OVERNIGHT_SHIFT_DB_ID","OVERNIGHT_SHIFT_ID") references "SHIFT_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-35:78 stripComments:false
INSERT INTO
   ref_capacity_pattern_type
   (
      capacity_pattern_type_db_id, capacity_pattern_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'WEEKLY', '7 Day Pattern', 'This is a weekly pattern of shifts', 0, TO_DATE('2010-01-02', 'YYYY-MM-DD'), TO_DATE('2010-01-02', 'YYYY-MM-DD'), 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_capacity_pattern_type WHERE capacity_pattern_type_db_id = 0 and capacity_pattern_type_cd = 'WEEKLY' );      

--changeSet DEV-35:79 stripComments:false
INSERT INTO
   ref_capacity_pattern_type
   (
      capacity_pattern_type_db_id, capacity_pattern_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'DAILY', 'Daily Pattern', 'This is a one day pattern of shifts', 0, TO_DATE('2010-01-02', 'YYYY-MM-DD'), TO_DATE('2010-01-02', 'YYYY-MM-DD'), 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_capacity_pattern_type WHERE capacity_pattern_type_db_id = 0 and capacity_pattern_type_cd = 'DAILY' );      

--changeSet DEV-35:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Migration script for 1003 Capacity Patterns.
-- Author: Rangarajan Sundararajan
-- Date  : January 14, 2010
-- Cursor to iterate through existing location shifts.
DECLARE
  CURSOR lcur_invlocshift IS
    SELECT
    inv_loc_shift.shift_db_id,
    inv_loc_shift.shift_id,
    inv_loc_shift.loc_db_id,
    inv_loc_shift.loc_id,
    inv_loc_shift.shift_cd,
    inv_loc_shift.desc_sdesc,
    inv_loc_shift.start_hour,
    inv_loc_shift.duration_qt,
    inv_loc_shift.work_hours_qt,
    inv_loc_shift.line_capacity_bool
    FROM
    inv_loc_shift;   
  -- Variable to hold a single row of lcur_invlocshift
  lrec_invlocshift  lcur_invlocshift%ROWTYPE;
  -- Counter variable to check for records with duplicate codes.
  ln_count NUMBER;
  -- Variable to hold location code.
  ln_locationcode inv_loc.loc_cd%TYPE;
  -- Variable to hold the mim local db id.  Used for capacity pattern creation.
  ln_mimlocaldbid mim_local_db.db_id%TYPE;
  --  Variable to hold capacity pattern id generated from sequence.
  ln_capacitypatternid capacity_pattern.capacity_pattern_id%TYPE;
  
  -- Cursor to iterate through existing location schedule definitions.    
  CURSOR lcur_invlocschedule IS
    SELECT
    inv_loc_schedule.schedule_db_id,
    inv_loc_schedule.schedule_id,
    inv_loc_schedule.loc_db_id,
    inv_loc_schedule.loc_id,
    inv_loc_schedule.schedule_cd,
    inv_loc_schedule.desc_sdesc
    FROM
    inv_loc_schedule;
  
  -- Varaible to hold a single row of lcur_invlocschedule.
  lrec_invlocschedule lcur_invlocschedule%ROWTYPE;
  
  i INTEGER;
  j INTEGER;
  
  -- Cursor to iterate through shifts for a schedule definition.
  CURSOR lcur_invlocscheduleshift (
  an_SchedDbId inv_loc_schedule.schedule_db_id%TYPE,
  an_SchedId inv_loc_schedule.schedule_id%TYPE
  ) IS
  
    SELECT
    inv_loc_schedule_shift.schedule_db_id,
    inv_loc_schedule_shift.schedule_id,
    inv_loc_schedule_shift.day_ord,
    inv_loc_schedule_shift.shift_db_id,
    inv_loc_schedule_shift.shift_id
    FROM inv_loc_schedule_shift
    WHERE
    inv_loc_schedule_shift.schedule_db_id = an_SchedDbId AND
    inv_loc_schedule_shift.schedule_id = an_SchedId;  
  
  -- Cursor to iterate through locations that has one or more shifts.
  CURSOR lcur_invlocshiftlocation IS
    SELECT DISTINCT
    inv_loc_shift.loc_db_id,
    inv_loc_shift.loc_id 
    FROM
    inv_loc_shift; 
 
  -- Variable to hold a single row of lcur_invlocscheduleshift.
  lrec_invlocscheduleshift lcur_invlocscheduleshift%ROWTYPE;  
  -- Variable to hold a single row of lcur_invlocshiftlocation.
  lrec_invlocshiftlocation  lcur_invlocshiftlocation%ROWTYPE;
  
  -- CURSOR to hold the shifts for a given location.
  CURSOR lcur_shiftsforlocation 
  (an_locdbid inv_loc.loc_db_id%TYPE,
  an_locid inv_loc.loc_id%TYPE) IS
  
    SELECT 
    inv_loc_shift.shift_db_id,
    inv_loc_shift.shift_id
    FROM
    inv_loc_shift
    WHERE
    inv_loc_shift.loc_db_id = an_locdbid AND
    inv_loc_shift.loc_id = an_locid;
  
   -- Variable to hold a single row of lcur_shiftsforlocation
  lrec_shiftsforlocation lcur_shiftsforlocation%ROWTYPE;  
  -- Variable to hold number of records inserted in SHIFT_SHIFT table.
  ln_shiftcount INTEGER;
  -- Variable to hold number of records inserted in USER_SHIFT_PATTERN table.
  ln_usershiftpatterncount INTEGER;
  -- Variable to hold location db id.
  ln_locdbid inv_loc.loc_db_id%TYPE;
  -- Variable to hold location id.
  ln_locid inv_loc.loc_id%TYPE;
  
BEGIN
    
    -- Migrate existing location shifts.
    FOR lrec_invlocshift IN lcur_invlocshift
    LOOP
      
      -- Check if there is a duplicate shift code.
      SELECT COUNT(*) INTO ln_count FROM inv_loc_shift WHERE inv_loc_shift.shift_cd = lrec_invlocshift.shift_cd;
        
      IF ln_count > 1 THEN
        
        SELECT inv_loc.loc_cd INTO ln_locationcode FROM inv_loc WHERE inv_loc.loc_db_id = lrec_invlocshift.loc_db_id AND
        inv_loc.loc_id = lrec_invlocshift.loc_id;
        
        -- If the SHIFT_CD is not unique, append the associated location code to the start of the shift code. 
        -- For example, the shift code is DAY and the location is EWR/LINE, then the new shift code will be EWR/LINE - DAY.
        INSERT INTO 
        shift_shift 
        (shift_shift.shift_db_id, shift_shift.shift_id, shift_shift.shift_cd, 
        shift_shift.shift_name,shift_shift.start_hour, shift_shift.duration_qt, shift_shift.work_hours_qt)
        VALUES 
        (lrec_invlocshift.shift_db_id, lrec_invlocshift.shift_id, ln_locationcode || '-' || lrec_invlocshift.shift_cd, 
        lrec_invlocshift.desc_sdesc, lrec_invlocshift.start_hour, lrec_invlocshift.duration_qt, lrec_invlocshift.work_hours_qt);      
        
      ELSE 
        
        INSERT INTO 
        shift_shift 
        (shift_db_id, shift_id, shift_cd, shift_name,start_hour, duration_qt, work_hours_qt)
        VALUES 
        (lrec_invlocshift.shift_db_id, lrec_invlocshift.shift_id, lrec_invlocshift.shift_cd, lrec_invlocshift.desc_sdesc
        , lrec_invlocshift.start_hour, lrec_invlocshift.duration_qt, lrec_invlocshift.work_hours_qt);      
        
      END IF;
      
      -- If the shift is marked as the overnight capacity shift (LINE_CAPACITY_BOOL = 1), set 
      -- the shift as the overnight capacity shift for the location.  
      IF lrec_invlocshift.line_capacity_bool = 1 THEN
        
        UPDATE 
        inv_loc 
        SET 
        inv_loc.overnight_shift_db_id = lrec_invlocshift.shift_db_id, 
        inv_loc.overnight_shift_id = lrec_invlocshift.shift_id 
        WHERE
        inv_loc.loc_db_id = lrec_invlocshift.loc_db_id AND
        inv_loc.loc_id = lrec_invlocshift.loc_id;
        
      END IF;     
      
    END LOOP;
    
    -- Migrate existing location schedule definitions.  
    FOR lrec_invlocschedule IN lcur_invlocschedule
      
    LOOP
      
      -- Check if there is a duplicate schedule code.
      SELECT COUNT(*) INTO ln_count FROM inv_loc_schedule WHERE inv_loc_schedule.schedule_cd = lrec_invlocschedule.schedule_cd;
        
      IF ln_count > 1 THEN
        
        SELECT inv_loc.loc_cd INTO ln_locationcode FROM inv_loc WHERE inv_loc.loc_db_id = lrec_invlocschedule.loc_db_id AND
        inv_loc.loc_id = lrec_invlocschedule.loc_id;
        
        -- If the SCHEDULE_CD is not unique, append the associated location code to the start of the shift code. 
        -- For example, the schedule code is 5D and the location is EWR/LINE, then the new shift code will be EWR/LINE - 5D.  
        INSERT INTO 
        user_shift_pattern 
        (user_shift_pattern.user_shift_pattern_db_id, user_shift_pattern.user_shift_pattern_id,
        user_shift_pattern.user_shift_pattern_cd, user_shift_pattern.user_shift_pattern_name) 
        VALUES(
        lrec_invlocschedule.schedule_db_id, lrec_invlocschedule.schedule_id, 
        ln_locationcode || '-' || lrec_invlocschedule.schedule_cd, lrec_invlocschedule.desc_sdesc);      
          
      ELSE
        
        INSERT INTO 
        user_shift_pattern 
        (user_shift_pattern.user_shift_pattern_db_id, user_shift_pattern.user_shift_pattern_id,
        user_shift_pattern.user_shift_pattern_cd, user_shift_pattern.user_shift_pattern_name) 
        VALUES(
        lrec_invlocschedule.schedule_db_id, lrec_invlocschedule.schedule_id, 
        lrec_invlocschedule.schedule_cd, lrec_invlocschedule.desc_sdesc);      
        
      END IF;          
      
      -- Create a new USER_SHIFT_PATTERN_DAY for every INV_LOC_SCHEDULE_SHIFT entry related to the INV_LOC_SCHEDULE entry.  
      FOR lrec_invlocscheduleshift IN lcur_invlocscheduleshift( lrec_invlocschedule.schedule_db_id, lrec_invlocschedule.schedule_id )
        
      LOOP
        
        -- Insertion into USER_SHIFT_PATTERN_DAY table.
        INSERT INTO 
        user_shift_pattern_day 
        (user_shift_pattern_db_id,user_shift_pattern_id,user_shift_pattern_day_ord)
        VALUES 
        (lrec_invlocschedule.schedule_db_id,lrec_invlocschedule.schedule_id, lrec_invlocscheduleshift.day_ord);
          
        -- Insertion into USER_SHIFT_PATTERN_DAY_SHIFT table.

        INSERT INTO 
        user_shift_pattern_day_shift
        (user_shift_pattern_db_id,user_shift_pattern_id,
        user_shift_pattern_day_ord,shift_db_id,shift_id)
        VALUES
        (lrec_invlocschedule.schedule_db_id,lrec_invlocschedule.schedule_id, 
        lrec_invlocscheduleshift.day_ord, lrec_invlocscheduleshift.shift_db_id, lrec_invlocscheduleshift.shift_id);
        
      END LOOP;               
      
    END LOOP;
    
    -- For every location that has at least one shift defined, create a capacity pattern. 
    -- The capacity pattern will not have any labor skills assigned to it.  
    
    -- Get the mim local db id to insert into CAPACITY_PATTERN table.
    SELECT mim_local_db.db_id INTO ln_mimlocaldbid FROM mim_local_db;

    FOR lrec_invlocshiftlocation IN lcur_invlocshiftlocation
    LOOP
      
      SELECT capacity_pattern_id_seq.NEXTVAL INTO ln_capacitypatternid FROM DUAL;
          
      SELECT inv_loc.loc_cd INTO ln_locationcode FROM inv_loc WHERE
      inv_loc.loc_db_id = lrec_invlocshiftlocation.loc_db_id AND
      inv_loc.loc_id = lrec_invlocshiftlocation.loc_id;
      
      -- Insertion into CAPACITY_PATTERN table.    
      INSERT INTO 
      capacity_pattern 
      (capacity_pattern_db_id,capacity_pattern_id,capacity_pattern_cd
      ,capacity_pattern_name,capacity_pattern_type_db_id,capacity_pattern_type_cd,capacity_pattern_ldesc)
      VALUES 
      (ln_mimlocaldbid,ln_capacitypatternid,ln_locationcode, 'Daily Pattern', 0,'DAILY','');
          
      -- Insert values into Capacity Pattern Day table.  As the pattern is daily pattern, there
      -- will be only one day.
      INSERT INTO 
      capacity_pattern_day 
      (capacity_pattern_db_id,capacity_pattern_id,capacity_pattern_day_ord)
      VALUES 
      (ln_mimlocaldbid,ln_capacitypatternid,1);
      
      -- For all INV_LOC_SHIFT entries for the location, insert  the following into CAPACITY_PATTERN_DAY_SHIFT.    
      FOR lrec_shiftsforlocation IN lcur_shiftsforlocation(lrec_invlocshiftlocation.loc_db_id,lrec_invlocshiftlocation.loc_id)
      LOOP
          
        INSERT INTO 
        capacity_pattern_day_shift 
        (capacity_pattern_db_id,capacity_pattern_id,capacity_pattern_day_ord
        ,shift_db_id,shift_id) 
        VALUES (ln_mimlocaldbid,ln_capacitypatternid,1,lrec_shiftsforlocation.shift_db_id,
        lrec_shiftsforlocation.shift_id);
        
      END LOOP;                               
      
    END LOOP;
    
    -- Get the number of records inserted in SHIFT_SHIFT table to increment the index.  
    SELECT COUNT(*) INTO ln_shiftcount FROM shift_shift;
        -- Get the number of records inserted in USER_SHIFT_PATTERN table to increment the index.  
    SELECT COUNT(*) INTO ln_usershiftpatterncount FROM user_shift_pattern;
      
    IF ln_shiftcount > 0 THEN
      
      -- Increment SHIFT_ID_SEQ
      EXECUTE IMMEDIATE 'SELECT shift_id_seq.NEXTVAL FROM DUAL CONNECT BY ROWNUM < ' ||  (ln_shiftcount); 
    
    END IF;
      
    IF ln_usershiftpatterncount > 0 THEN
         
      -- Increment USER_SHIFT_PATTERN_ID_SEQ
      EXECUTE IMMEDIATE 'SELECT user_shift_pattern_id_seq.NEXTVAL FROM DUAL CONNECT BY ROWNUM < ' || (ln_usershiftpatterncount);
      
    END IF;
    
    -- For every ORG_HR_SCHEDULE entry, update the new foreign keys USER_SHIFT_PATTERN_DB_ID and USER_SHIFT_PATTERN_ID.
    UPDATE 
    org_hr_schedule 
    SET 
    org_hr_schedule.user_shift_pattern_db_id = org_hr_schedule.schedule_db_id,
    org_hr_schedule.user_shift_pattern_id = org_hr_schedule.schedule_id;
    
    -- For every ORG_HR_SCHEDULE entry, update the new foreign keys LOC_DB_ID and LOC_ID.
    UPDATE 
    org_hr_schedule 
    SET
    (org_hr_schedule.loc_db_id, org_hr_schedule.loc_id) =
    (SELECT inv_loc_schedule.loc_db_id, inv_loc_schedule.loc_id
    FROM inv_loc_schedule WHERE
    inv_loc_schedule.schedule_db_id = org_hr_schedule.schedule_db_id AND
    inv_loc_schedule.schedule_id = org_hr_schedule.schedule_id);
    
    -- For every ORG_HR_SHIFT entry, update the new foreign key  LOC_DB_ID and LOC_ID
    UPDATE 
    org_hr_shift
    SET
    (org_hr_shift.loc_db_id,org_hr_shift.loc_id) = 
    (SELECT inv_loc_shift.loc_db_id, inv_loc_shift.loc_id
    FROM inv_loc_shift WHERE
    inv_loc_shift.shift_db_id = org_hr_shift.shift_db_id
    AND inv_loc_shift.shift_id = org_hr_shift.shift_id);
    
    -- For every ORG_HR_SHIFT_PLAN entry, update the new foreign key  LOC_DB_ID and LOC_ID
    UPDATE 
    org_hr_shift_plan 
    SET    
    (org_hr_shift_plan.loc_db_id, org_hr_shift_plan.loc_id) 
              = (SELECT inv_loc_shift.loc_db_id, inv_loc_shift.loc_id  
                 FROM   inv_loc_shift
                 WHERE  inv_loc_shift.shift_db_id = org_hr_shift_plan.shift_db_id 
                 AND    inv_loc_shift.shift_id = org_hr_shift_plan.shift_id)     
    WHERE  org_hr_shift_plan.shift_db_id IS NOT NULL;                                                                       
      
    END;
/                              