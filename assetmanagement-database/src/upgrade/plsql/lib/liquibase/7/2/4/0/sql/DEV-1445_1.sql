--liquibase formatted sql


--changeSet DEV-1445_1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***********************************************************
*
*  New tables for Basic Line Planning Automation
*
************************************************************/
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_LPA_ISSUE_TYPE" (
	"LPA_ISSUE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LPA_ISSUE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LPA_ISSUE_TYPE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"DISPLAY_CODE" Varchar2 (16) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
	"DISPLAY_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
	"DISPLAY_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
	"DISPLAY_ORD" Number(5,0) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_LPA_ISSUE_TYPE" primary key ("LPA_ISSUE_TYPE_DB_ID","LPA_ISSUE_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-1445_1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_LPA_RUN_STATUS" (
	"LPA_RUN_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LPA_RUN_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LPA_RUN_STATUS_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"DISPLAY_CODE" Varchar2 (16) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
	"DISPLAY_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
	"DISPLAY_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
	"DISPLAY_ORD" Number(5,0) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_LPA_RUN_STATUS" primary key ("LPA_RUN_STATUS_DB_ID","LPA_RUN_STATUS_CD") 
) 
');
END;
/

--changeSet DEV-1445_1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LPA_RUN" (
	"RUN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RUN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LPA_RUN_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LPA_RUN_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LPA_RUN_STATUS_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"FLEET_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FLEET_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FLEET_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"REQUESTED_BY_HR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REQUESTED_BY_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REQUESTED_BY_HR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REQUESTED_BY_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PLANNING_START_DT" Date NOT NULL DEFERRABLE ,
	"PLANNING_RANGE_DAYS" Number(2,0) NOT NULL DEFERRABLE ,
	"PLANNING_END_DT" Date NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LPA_RUN" primary key ("RUN_ID") 
) 
');
END;
/

--changeSet DEV-1445_1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LPA_FLEET" (
	"FLEET_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FLEET_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FLEET_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"SERVICE_CHECK_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SERVICE_CHECK_BOOL IN (0, 1) ) DEFERRABLE ,
	"SERVICE_BLOCK_DEFN_DB_ID" Number(10,0) Check (SERVICE_BLOCK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_BLOCK_DEFN_ID" Number(10,0) Check (SERVICE_BLOCK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_WP_NAME_TEMPLATE" Varchar2 (4000),
	"SERVICE_WP_TASK_SUBCLASS_DB_ID" Number(10,0) Check (SERVICE_WP_TASK_SUBCLASS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_WP_TASK_SUBCLASS_CD" Varchar2 (8),
	"TURN_CHECK_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (TURN_CHECK_BOOL IN (0, 1) ) DEFERRABLE ,
	"TURN_BLOCK_DEFN_DB_ID" Number(10,0) Check (TURN_BLOCK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TURN_BLOCK_DEFN_ID" Number(10,0) Check (TURN_BLOCK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TURN_WP_NAME_TEMPLATE" Varchar2 (4000),
	"TURN_WP_TASK_SUBCLASS_DB_ID" Number(10,0) Check (TURN_WP_TASK_SUBCLASS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TURN_WP_TASK_SUBCLASS_CD" Varchar2 (8),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LPA_FLEET" primary key ("FLEET_DB_ID","FLEET_CD") 
) 
');
END;
/

--changeSet DEV-1445_1:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LPA_SERVICE_WORK_TYPE" (
	"FLEET_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FLEET_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FLEET_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"WORK_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (WORK_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"WORK_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LPA_SERVICE_WORK_TYPE" primary key ("FLEET_DB_ID","FLEET_CD","WORK_TYPE_DB_ID","WORK_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-1445_1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LPA_TURN_WORK_TYPE" (
	"FLEET_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FLEET_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FLEET_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"WORK_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (WORK_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"WORK_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LPA_TURN_WORK_TYPE" primary key ("FLEET_DB_ID","FLEET_CD","WORK_TYPE_DB_ID","WORK_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-1445_1:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LPA_RUN_INV" (
	"RUN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RUN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LPA_RUN_INV" primary key ("RUN_ID","INV_NO_DB_ID","INV_NO_ID") 
) 
');
END;
/

--changeSet DEV-1445_1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LPA_RUN_ISSUE" (
	"RUN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RUN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RUN_ISSUE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RUN_ISSUE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LPA_ISSUE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LPA_ISSUE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LPA_ISSUE_TYPE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"LPA_ISSUE_NOTE" Varchar2 (4000),
	"INV_NO_DB_ID" Number(10,0) Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_ID" Number(10,0) Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_LDESC" Varchar2 (400),
	"BLOCK_DB_ID" Number(10,0) Check (BLOCK_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BLOCK_ID" Number(10,0) Check (BLOCK_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BLOCK_LDESC" Varchar2 (4000),
	"WP_DB_ID" Number(10,0) Check (WP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"WP_ID" Number(10,0) Check (WP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"WP_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LPA_RUN_ISSUE" primary key ("RUN_ID","RUN_ISSUE_ID") 
) 
');
END;
/

--changeSet DEV-1445_1:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***********************************************************
*
*  Indexes
*
************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
	Create Index "IX_INVACREG_LPARUNINV" ON "LPA_RUN_INV" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet DEV-1445_1:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_LPARUN" ON "LPA_RUN" ("REQUESTED_BY_HR_DB_ID","REQUESTED_BY_HR_ID")
');
END;
/

--changeSet DEV-1445_1:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_LPARUINISSUE" ON "LPA_RUN_ISSUE" ("BLOCK_DB_ID","BLOCK_ID")
');
END;
/

--changeSet DEV-1445_1:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WPSCHEDSTASK_LPARUNISSUE" ON "LPA_RUN_ISSUE" ("WP_DB_ID","WP_ID")
');
END;
/

--changeSet DEV-1445_1:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_LPAFLEETTURN" ON "LPA_FLEET" ("TURN_BLOCK_DEFN_DB_ID","TURN_BLOCK_DEFN_ID")
');
END;
/

--changeSet DEV-1445_1:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_LPAFLEETSRVC" ON "LPA_FLEET" ("SERVICE_BLOCK_DEFN_DB_ID","SERVICE_BLOCK_DEFN_ID")
');
END;
/

--changeSet DEV-1445_1:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REFTASKSUBCL_LPAFLEETSRVC" ON "LPA_FLEET" ("SERVICE_WP_TASK_SUBCLASS_DB_ID","SERVICE_WP_TASK_SUBCLASS_CD")
');
END;
/

--changeSet DEV-1445_1:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REFTASKSUBCL_LPAFLEETTURN" ON "LPA_FLEET" ("TURN_WP_TASK_SUBCLASS_DB_ID","TURN_WP_TASK_SUBCLASS_CD")
');
END;
/

--changeSet DEV-1445_1:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPAFLEET_LPASRVCWTYPE" ON "LPA_SERVICE_WORK_TYPE" ("FLEET_DB_ID","FLEET_CD")
');
END;
/

--changeSet DEV-1445_1:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPARUN_LPARUNISSUE" ON "LPA_RUN_ISSUE" ("RUN_ID")
');
END;
/

--changeSet DEV-1445_1:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPARUN_LPARUNINV" ON "LPA_RUN_INV" ("RUN_ID")
');
END;
/

--changeSet DEV-1445_1:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPAFLEET_LPARUN" ON "LPA_RUN" ("FLEET_DB_ID","FLEET_CD")
');
END;
/

--changeSet DEV-1445_1:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPAFLEET_LPATURNWTYPE" ON "LPA_TURN_WORK_TYPE" ("FLEET_DB_ID","FLEET_CD")
');
END;
/

--changeSet DEV-1445_1:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPARUNINV_LPARUNISSUE" ON "LPA_RUN_ISSUE" ("RUN_ID","INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet DEV-1445_1:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*********************************************************************************
*
*  Drop constraints: 
*  FK_MIMDB_LPARUNREV was created in 7.x. 
*  In order to be renamed to FK_MIMDB_LPARUN it has to be dropped first and then created under new name
*
**********************************************************************************/
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('LPA_RUN', 'FK_MIMDB_LPARUNREV');
END;
/

--changeSet DEV-1445_1:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***********************************************************
*
*  Constraints
*
************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_FLEET" add Constraint "FK_EQPASSMBL_LPAFLEET" foreign key ("FLEET_DB_ID","FLEET_CD") references "EQP_ASSMBL" ("ASSMBL_DB_ID","ASSMBL_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_INV" add Constraint "FK_INVACREG_LPARUNINV" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_AC_REG" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "REF_LPA_ISSUE_TYPE" add Constraint "FK_MIMDB_REFLPAISSUETYPE" foreign key ("LPA_ISSUE_TYPE_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "REF_LPA_ISSUE_TYPE" add Constraint "FK_MIMDB_REFLPAISSUETYPEREV" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "REF_LPA_RUN_STATUS" add Constraint "FK_MIMDB_REFLPARUNSTATUS" foreign key ("LPA_RUN_STATUS_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "REF_LPA_RUN_STATUS" add Constraint "FK_MIMDB_REFLPARUNSTATUSREV" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_FLEET" add Constraint "FK_MIMDB_LPAFLEET" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_SERVICE_WORK_TYPE" add Constraint "FK_MIMDB_LPASERVICEWTYPE" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_TURN_WORK_TYPE" add Constraint "FK_MIMDB_LPATURNWTYPE" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN" add Constraint "FK_MIMDB_LPARUN" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_INV" add Constraint "FK_MIMDB_LPARUNINV" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_ISSUE" add Constraint "FK_MIMDB_LPARUNISSUE" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN" add Constraint "FK_ORGHR_LPARUN" foreign key ("REQUESTED_BY_HR_DB_ID","REQUESTED_BY_HR_ID") references "ORG_HR" ("HR_DB_ID","HR_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_FLEET" add Constraint "FK_REFTASKSUBCL_LPAFLEETSRVC" foreign key ("SERVICE_WP_TASK_SUBCLASS_DB_ID","SERVICE_WP_TASK_SUBCLASS_CD") references "REF_TASK_SUBCLASS" ("TASK_SUBCLASS_DB_ID","TASK_SUBCLASS_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_FLEET" add Constraint "FK_REFTASKSUBCL_LPAFLEETTURN" foreign key ("TURN_WP_TASK_SUBCLASS_DB_ID","TURN_WP_TASK_SUBCLASS_CD") references "REF_TASK_SUBCLASS" ("TASK_SUBCLASS_DB_ID","TASK_SUBCLASS_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_SERVICE_WORK_TYPE" add Constraint "FK_REFWTYPE_LPASERVWTYPE" foreign key ("WORK_TYPE_DB_ID","WORK_TYPE_CD") references "REF_WORK_TYPE" ("WORK_TYPE_DB_ID","WORK_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_TURN_WORK_TYPE" add Constraint "FK_REFWTYPE_LPATURNWTYPE" foreign key ("WORK_TYPE_DB_ID","WORK_TYPE_CD") references "REF_WORK_TYPE" ("WORK_TYPE_DB_ID","WORK_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_ISSUE" add Constraint "FK_SCHEDSTASK_LPARUINISSUE" foreign key ("BLOCK_DB_ID","BLOCK_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_ISSUE" add Constraint "FK_WPSCHEDSTASK_LPARUNISSUE" foreign key ("WP_DB_ID","WP_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_FLEET" add Constraint "FK_TASKDEFN_LPAFLEETSRVC" foreign key ("SERVICE_BLOCK_DEFN_DB_ID","SERVICE_BLOCK_DEFN_ID") references "TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_FLEET" add Constraint "FK_TASKDEFN_LPAFLEETTURN" foreign key ("TURN_BLOCK_DEFN_DB_ID","TURN_BLOCK_DEFN_ID") references "TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "REF_LPA_ISSUE_TYPE" add Constraint "FK_MIMRSTAT_REFLPAISSUETYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "REF_LPA_RUN_STATUS" add Constraint "FK_MIMRSTAT_REFLPARUNSTATUS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_FLEET" add Constraint "FK_MIMRSTAT_LPAFLEET" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_SERVICE_WORK_TYPE" add Constraint "FK_MIMRSTAT_LPASERVWTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_TURN_WORK_TYPE" add Constraint "FK_MIMRSTAT_LPATURNWTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN" add Constraint "FK_MIMRSTAT_LPARUN" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_INV" add Constraint "FK_MIMRSTAT_LPARUNINV" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_ISSUE" add Constraint "FK_MIMRSTAT_LPARUNISSUE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_ISSUE" add Constraint "FK_REFLPAISSUETYPE_LPARUNISSUE" foreign key ("LPA_ISSUE_TYPE_DB_ID","LPA_ISSUE_TYPE_CD") references "REF_LPA_ISSUE_TYPE" ("LPA_ISSUE_TYPE_DB_ID","LPA_ISSUE_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN" add Constraint "FK_REFLPARUNSTATUS_LPARUN" foreign key ("LPA_RUN_STATUS_DB_ID","LPA_RUN_STATUS_CD") references "REF_LPA_RUN_STATUS" ("LPA_RUN_STATUS_DB_ID","LPA_RUN_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_INV" add Constraint "FK_LPARUN_LPARUNINV" foreign key ("RUN_ID") references "LPA_RUN" ("RUN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_ISSUE" add Constraint "FK_LPARUN_LPARUNISSUE" foreign key ("RUN_ID") references "LPA_RUN" ("RUN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_SERVICE_WORK_TYPE" add Constraint "FK_LPAFLEET_LPASRVCWTYPE" foreign key ("FLEET_DB_ID","FLEET_CD") references "LPA_FLEET" ("FLEET_DB_ID","FLEET_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_TURN_WORK_TYPE" add Constraint "FK_LPAFLEET_LPATURNWTYPE" foreign key ("FLEET_DB_ID","FLEET_CD") references "LPA_FLEET" ("FLEET_DB_ID","FLEET_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN" add Constraint "FK_LPAFLEET_LPARUN" foreign key ("FLEET_DB_ID","FLEET_CD") references "LPA_FLEET" ("FLEET_DB_ID","FLEET_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	Alter table "LPA_RUN_ISSUE" add Constraint "FK_LPARUNINV_LPARUNISSUE" foreign key ("RUN_ID","INV_NO_DB_ID","INV_NO_ID") references "LPA_RUN_INV" ("RUN_ID","INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1445_1:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************
 *
 * Audit triggers
 *
/****************************************************/
CREATE OR REPLACE TRIGGER "TIBR_LPA_FLEET" BEFORE INSERT
   ON "LPA_FLEET" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1445_1:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LPA_FLEET" BEFORE UPDATE
   ON "LPA_FLEET" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1445_1:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LPA_SERVICE_WORK_TYPE" BEFORE INSERT
   ON "LPA_SERVICE_WORK_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1445_1:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LPA_SERVICE_WORK_TYPE" BEFORE UPDATE
   ON "LPA_SERVICE_WORK_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1445_1:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LPA_TURN_WORK_TYPE" BEFORE INSERT
   ON "LPA_TURN_WORK_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1445_1:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LPA_TURN_WORK_TYPE" BEFORE UPDATE
   ON "LPA_TURN_WORK_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1445_1:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LPA_RUN" BEFORE INSERT
   ON "LPA_RUN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LPA_RUN" BEFORE UPDATE
   ON "LPA_RUN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LPA_RUN_ISSUE" BEFORE INSERT
   ON "LPA_RUN_ISSUE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LPA_RUN_ISSUE" BEFORE UPDATE
   ON "LPA_RUN_ISSUE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_LPA_RUN_STATUS" BEFORE INSERT
   ON "REF_LPA_RUN_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_LPA_RUN_STATUS" BEFORE UPDATE
   ON "REF_LPA_RUN_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LPA_RUN_INV" BEFORE INSERT
   ON "LPA_RUN_INV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LPA_RUN_INV" BEFORE UPDATE
   ON "LPA_RUN_INV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_LPA_ISSUE_TYPE" BEFORE INSERT
   ON "REF_LPA_ISSUE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_LPA_ISSUE_TYPE" BEFORE UPDATE
   ON "REF_LPA_ISSUE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-1445_1:77 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************************
 *
 * Insert sequence LPA_RUN_ID_SEQ
 *
/*******************************************************************************/
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('LPA_RUN_ID_SEQ', 100000);
END;
/

--changeSet DEV-1445_1:78 stripComments:false
INSERT INTO 
       utl_sequence 
       ( 
	  sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id 
       )
       SELECT 'LPA_RUN_ID_SEQ', 100000, 'LPA_RUN', 'RUN_ID' , 1, 0
       FROM
	 dual
       WHERE
	 NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'LPA_RUN_ID_SEQ' );	 	 	 

--changeSet DEV-1445_1:79 stripComments:false
/************************************************
** 0-Level INSERT SCRIPT FOR REF_LPA_ISSUE_TYPE
*************************************************/
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'ERROR_UNEXPECTED', 'ERROR_UNEXPECTED', 'Unexpected Error', 'An unexpected error occurred', 10, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'ERROR_UNEXPECTED' );

--changeSet DEV-1445_1:80 stripComments:false
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'MODE_INVALID', 'MODE_INVALID', 'Invalid Mode', 'The line planning automation mode is not BASIC', 20, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI'
   FROM dual
   WHERE 
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'MODE_INVALID' ); 

--changeSet DEV-1445_1:81 stripComments:false
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'INV_LOCK', 'INV_LOCK', 'Locked Inventory', 'The aircraft is locked, which prevents automation', 30, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'INV_LOCK' );

--changeSet DEV-1445_1:82 stripComments:false
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'INV_PREVENT_AUTO', 'INV_PREVENT_AUTO', 'Automation Prevented', 'Automation is prevented against the inventory', 40, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'INV_PREVENT_AUTO' );

--changeSet DEV-1445_1:83 stripComments:false
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'CHECK_CONFLICT', 'CHECK_CONFLICT', 'Check Conflict', 'The check overlaps with another check', 50, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'CHECK_CONFLICT' );

--changeSet DEV-1445_1:84 stripComments:false
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'FLIGHT_CONFLICT', 'FLIGHT_CONFLICT', 'Flight Conflict', 'The check occurs during a flight', 60, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'FLIGHT_CONFLICT' );

--changeSet DEV-1445_1:85 stripComments:false
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'STOP_CONFLICT', 'STOP_CONFLICT', 'Stop Conflict', 'The check spans multiple stops', 70, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'STOP_CONFLICT' );

--changeSet DEV-1445_1:86 stripComments:false
INSERT INTO 
   REF_LPA_ISSUE_TYPE
   ( 
      LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'STOP_NO_SUITABLE', 'STOP_NO_SUITABLE', 'No Suitable Stop', 'No suitable stop exists in which to schedule the check', 80, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_ISSUE_TYPE WHERE LPA_ISSUE_TYPE_DB_ID = 0 AND LPA_ISSUE_TYPE_CD = 'STOP_NO_SUITABLE' );

--changeSet DEV-1445_1:87 stripComments:false
/************************************************
** 0-Level INSERT SCRIPT FOR REF_LPA_RUN_STATUS
*************************************************/
INSERT INTO 
   REF_LPA_RUN_STATUS
   ( 
      LPA_RUN_STATUS_DB_ID, LPA_RUN_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'QUEUED', 'QUEUED', 'Queued', 'The automation run is queued', 10, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_RUN_STATUS WHERE LPA_RUN_STATUS_DB_ID = 0 AND LPA_RUN_STATUS_CD = 'QUEUED' );

--changeSet DEV-1445_1:88 stripComments:false
INSERT INTO 
   REF_LPA_RUN_STATUS
   ( 
      LPA_RUN_STATUS_DB_ID, LPA_RUN_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'RUNNING', 'RUNNING', 'Running', 'The automation run is in progress', 20, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_RUN_STATUS WHERE LPA_RUN_STATUS_DB_ID = 0 AND LPA_RUN_STATUS_CD = 'RUNNING' );

--changeSet DEV-1445_1:89 stripComments:false
INSERT INTO 
   REF_LPA_RUN_STATUS
   ( 
      LPA_RUN_STATUS_DB_ID, LPA_RUN_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
   SELECT 0, 'COMPLETED', 'COMPLETED', 'Completed', 'The automation has completed', 30, 0, TO_DATE('2012-01-16', 'YYYY-MM-DD'), TO_DATE('2012-01-16', 'YYYY-MM-DD'), 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LPA_RUN_STATUS WHERE LPA_RUN_STATUS_DB_ID = 0 AND LPA_RUN_STATUS_CD = 'COMPLETED' );

--changeSet DEV-1445_1:90 stripComments:false
/******************************************************************************
 *
 * For each aircraft assembly in EQP_ASSMBL table, insert a row into LPA_FLEET 
 *
/*******************************************************************************/
INSERT INTO 
       lpa_fleet( fleet_db_id, fleet_cd ) 
       SELECT 
	  eqp_assmbl.assmbl_db_id, 
	  eqp_assmbl.assmbl_cd 
       FROM 
	  eqp_assmbl 
       WHERE 
          eqp_assmbl.assmbl_class_db_id = 0  AND
          eqp_assmbl.assmbl_class_cd    = 'ACFT'
          AND
          NOT EXISTS ( SELECT 
                          1 
                       FROM 
                          lpa_fleet 
                       WHERE 
                          fleet_db_id = eqp_assmbl.assmbl_db_id AND
                          fleet_cd    = eqp_assmbl.assmbl_cd);                                                              

--changeSet DEV-1445_1:91 stripComments:false
 /*************************************************************************
 *
 * For each aircraft assembly in LPA_FLEET, insert a row into 
 * LPA_TURN_WORK_TYPE and LPA_SERVICE_WORK_TYPE with work types of 'TURN'
 * and 'SERVICE' respectively
 * 
 **************************************************************************/
       INSERT INTO 
           lpa_turn_work_type
           (fleet_db_id, fleet_cd, work_type_db_id, work_type_cd)
       SELECT
           lpa_fleet.fleet_db_id, 
           lpa_fleet.fleet_cd,
           0,
           'TURN'
       FROM
           lpa_fleet
       WHERE 
          NOT EXISTS ( SELECT 
                          1 
                       FROM 
                          lpa_turn_work_type 
                       WHERE 
                          fleet_db_id     = lpa_fleet.fleet_db_id AND 
                          fleet_cd        = lpa_fleet.fleet_cd AND 
                          work_type_db_id = 0 AND 
                          work_type_cd    = 'TURN');                                               

--changeSet DEV-1445_1:92 stripComments:false
      INSERT INTO 
           lpa_service_work_type
           (fleet_db_id, fleet_cd, work_type_db_id, work_type_cd)
      SELECT
           lpa_fleet.fleet_db_id, 
           lpa_fleet.fleet_cd,
           0,
           'SERVICE'
      FROM
           lpa_fleet
      WHERE 
         NOT EXISTS ( SELECT 
                         1 
                      FROM 
                         lpa_service_work_type 
                      WHERE 
                         fleet_db_id     = lpa_fleet.fleet_db_id AND 
                         fleet_cd        = lpa_fleet.fleet_cd AND 
                         work_type_db_id = 0 AND 
                         work_type_cd    = 'SERVICE');     