--liquibase formatted sql


--changeSet DEV-1290:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************/
/*Create Tables            */
/***************************/
BEGIN
utl_migr_schema_pkg.table_create('
Create table "BLT_REF_WF_LOG_TYPE" (
	"REF_WF_LOG_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"REF_WF_LOG_SUB_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"REF_WF_LOG_TYPE_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
	"REF_WF_LOG_TYPE_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_BLT_REF_WF_LOG_TYPE" primary key ("REF_WF_LOG_TYPE_CD","REF_WF_LOG_SUB_TYPE_CD") 
) 
');

utl_migr_schema_pkg.table_create('
	Create table "BLT_REF_ERROR" (
		"REF_ERROR_CD" Varchar2 (30) NOT NULL DEFERRABLE ,
		"REF_ERROR_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
		"REF_ERROR_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
		"REF_ERROR_TYPE_CD" Varchar2 (30) NOT NULL DEFERRABLE ,
		"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
		"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
		"CREATION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
	 Constraint "PK_BLT_REF_ERROR" primary key ("REF_ERROR_CD") 
	) 
');

utl_migr_schema_pkg.table_create('
	Create table "BLT_REF_ERROR_TYPE" (
		"REF_ERROR_TYPE_CD" Varchar2 (30) NOT NULL DEFERRABLE ,
		"REF_ERROR_TYPE_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
		"REF_ERROR_SEVERITY" Number(10,0) NOT NULL DEFERRABLE  Check (REF_ERROR_SEVERITY IN (1,2,3,4) ) DEFERRABLE ,
		"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
		"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
		"CREATION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
	 Constraint "PK_BLT_REF_ERROR_TYPE" primary key ("REF_ERROR_TYPE_CD") 
	) 
');

utl_migr_schema_pkg.table_create('
Create table "BLT_WF_ERROR_LOG" (
	"WF_ERROR_LOG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"WF_REC_LOG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"WF_CYCLE_LOG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"REF_ERROR_CD" Varchar2 (30) NOT NULL DEFERRABLE ,
	"WF_ERROR_REC_IDENTIFIER" Varchar2 (4000) NOT NULL DEFERRABLE ,
 Constraint "PK_BLT_WF_ERROR_LOG" primary key ("WF_ERROR_LOG_ID") 
) 
');

utl_migr_schema_pkg.table_create('
Create table "BLT_WF_REC_LOG" (
	"WF_REC_LOG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"WF_CYCLE_LOG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"REF_WF_LOG_STATUS_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"REF_WF_LOG_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"REF_WF_LOG_SUB_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"WF_LOG_REC_IDENTIFIER" Varchar2 (4000),
	"WF_LOG_START_DT" Date,
	"WF_LOG_END_DT" Date,
	"WF_LOG_ERR_STATUS_CD" Varchar2 (8),
	"WF_LOG_ERR_MSG" Clob,
 Constraint "PK_BLT_WF_REC_LOG" primary key ("WF_REC_LOG_ID","WF_CYCLE_LOG_ID") 
) 
');

utl_migr_schema_pkg.table_create('
Create table "BLT_WF_CYCLE_LOG" (
	"WF_CYCLE_LOG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"WF_CYCLE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"REF_WF_CYCLE_STATUS_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"WF_CYCLE_START_DT" Date,
	"WF_CYCLE_END_DT" Date,
	"WF_CYCLE_ERR_STATUS_CD" Varchar2 (8),
	"WF_CYCLE_ERR_MSG" Clob,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_BLT_WF_CYCLE_LOG" primary key ("WF_CYCLE_LOG_ID") 
) 
');

utl_migr_schema_pkg.table_create('
	Create table "BLT_REF_WF_LOG_STATUS" (
		"REF_WF_LOG_STATUS_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
		"REF_WF_LOG_STATUS_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
		"REF_WF_LOG_STATUS_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
		"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
		"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
		"CREATION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
	 Constraint "PK_BLT_REF_WF_LOG_STATUS" primary key ("REF_WF_LOG_STATUS_CD") 
	) 
');

utl_migr_schema_pkg.table_create('
	Create table "BLT_REF_WF_CYCLE_STATUS" (
		"REF_WF_CYCLE_STATUS_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
		"REF_WF_CYCLE_STATUS_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
		"REF_WF_CYCLE_STATUS_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
		"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
		"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
		"CREATION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
	 Constraint "PK_BLT_REF_WF_CYCLE_STATUS" primary key ("REF_WF_CYCLE_STATUS_CD") 
) 
');

/***************************/
/*Indexes                  */
/***************************/

utl_migr_schema_pkg.index_create('
   Create UNIQUE Index "BLT_WF_ERROR_LOG_NK" ON "BLT_WF_ERROR_LOG" ("WF_REC_LOG_ID","REF_ERROR_CD") 
');

utl_migr_schema_pkg.index_create('
	Create  Index "IX_BLTWFERRORLOG_WFERRORRECIDE" ON "BLT_WF_ERROR_LOG" ("WF_ERROR_REC_IDENTIFIER") 
');

utl_migr_schema_pkg.index_create('
    Create UNIQUE Index "WFLOGRECIDENTIFIER_INDX" ON "BLT_WF_REC_LOG" ("WF_LOG_REC_IDENTIFIER") 
');

utl_migr_schema_pkg.index_create('
Create UNIQUE Index "BLT_WF_CYCLE_LOG_NK" ON "BLT_WF_CYCLE_LOG" ("WF_CYCLE_CD") 
');

/***************************/
/*Constraints    */
/***************************/
utl_migr_schema_pkg.table_constraint_add('
	Alter table "BLT_REF_WF_LOG_TYPE" add Constraint "FK_MIMDB_BLTREFWFLOGTYPE" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
	Alter table "BLT_REF_WF_LOG_TYPE" add Constraint "FK_MIMDB_CREATIONBLTREFWFLOGTY" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
  Alter table "BLT_REF_WF_LOG_TYPE" add Constraint "FK_MIMDB_REVIDBLTREFWFLOGTYPE" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR_TYPE" add Constraint "FK_MIMDB_CREATIONBLTREFERRORTY" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR_TYPE" add Constraint "FK_MIMDB_REVBLTREFERRORTYPE" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR_TYPE" add Constraint "FK_MIMDB_CTRLBLTREFERRORTYPE" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR" add Constraint "FK_MIMDB_CTRLBLTREFERROR" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR" add Constraint "FK_MIMDB_CREATIONBLTREFERROR" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR" add Constraint "FK_MIMDB_REVBLTREFERROR" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_LOG_STATUS" add Constraint "FK_MIMDB_CTRLBLTREFWFLOGSTATUS" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_LOG_STATUS" add Constraint "FK_MIMDB_CREATIONBLTREFWFLOGST" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_LOG_STATUS" add Constraint "FK_MIMDB_REVBLTREFWFLOGSTATUS" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_WF_CYCLE_LOG" add Constraint "FK_MIMDB_CTRLBLTWFCYCLELOG" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_WF_CYCLE_LOG" add Constraint "FK_MIMDB_CREATIONBLTWFCYCLELOG" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_WF_CYCLE_LOG" add Constraint "FK_MIMDB_REVBLTWFCYCLELOG" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_CYCLE_STATUS" add Constraint "REF_MIMDB_CTRLBLTREFWFCYCLESTA" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_CYCLE_STATUS" add Constraint "FK_MIMDB_CREATIONBLTREFWFCYCLE" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_CYCLE_STATUS" add Constraint "FK_MIMDB_REVBLTREFWFCYCLESTATU" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_LOG_TYPE" add Constraint "FK_MIMRSTAT_BLTREFWFLOGTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR_TYPE" add Constraint "FK_MIMRSTAT_BLTREFERRORTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR" add Constraint "FK_MIMRSTAT_BLTREFERROR" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_LOG_STATUS" add Constraint "FK_MIMRSTAT_BLTREFWFLOGSTATUS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_WF_CYCLE_LOG" add Constraint "FK_MIMRSTAT_BLTWFCYCLELOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_WF_CYCLE_STATUS" add Constraint "FK_MIMSTAT_BLTREFWFCYCLESTATUS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_WF_REC_LOG" add Constraint "FK_BLTREFWFLOGTYPE_BLTWFRECLOG" foreign key ("REF_WF_LOG_TYPE_CD","REF_WF_LOG_SUB_TYPE_CD") references "BLT_REF_WF_LOG_TYPE" ("REF_WF_LOG_TYPE_CD","REF_WF_LOG_SUB_TYPE_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_WF_ERROR_LOG" add Constraint "FK_BLTREFERROR_BLTWFERRORLOG" foreign key ("REF_ERROR_CD") references "BLT_REF_ERROR" ("REF_ERROR_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_REF_ERROR" add Constraint "FK_BLTREFERRORTYPE_BLTREFERROR" foreign key ("REF_ERROR_TYPE_CD") references "BLT_REF_ERROR_TYPE" ("REF_ERROR_TYPE_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
Alter table "BLT_WF_ERROR_LOG" add Constraint "FK_BLTWFRECLOG_BLTWFERRORLOG" foreign key ("WF_REC_LOG_ID","WF_CYCLE_LOG_ID") references "BLT_WF_REC_LOG" ("WF_REC_LOG_ID","WF_CYCLE_LOG_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
Alter table "BLT_WF_REC_LOG" add Constraint "PFK_BLTWFCYCLELOG_BLTWFRECLOG" foreign key ("WF_CYCLE_LOG_ID") references "BLT_WF_CYCLE_LOG" ("WF_CYCLE_LOG_ID")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
Alter table "BLT_WF_REC_LOG" add Constraint "FK_BLTREFWFLOGSTATUS_BLTWFRECL" foreign key ("REF_WF_LOG_STATUS_CD") references "BLT_REF_WF_LOG_STATUS" ("REF_WF_LOG_STATUS_CD")  DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('
   Alter table "BLT_WF_CYCLE_LOG" add Constraint "FK_BLTREFWFCYCLESTATUS_LOG" foreign key ("REF_WF_CYCLE_STATUS_CD") references "BLT_REF_WF_CYCLE_STATUS" ("REF_WF_CYCLE_STATUS_CD")  DEFERRABLE
');

END;
/

--changeSet DEV-1290:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************/
/*Audit triggers           */
/***************************/
CREATE OR REPLACE TRIGGER "TIBR_BLT_REF_WF_LOG_TYPE" BEFORE INSERT
   ON "BLT_REF_WF_LOG_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-1290:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_BLT_REF_WF_LOG_TYPE" BEFORE UPDATE
   ON "BLT_REF_WF_LOG_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-1290:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_BLT_REF_ERROR" BEFORE INSERT
   ON "BLT_REF_ERROR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-1290:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_BLT_REF_ERROR" BEFORE UPDATE
   ON "BLT_REF_ERROR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-1290:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_BLT_REF_ERROR_TYPE" BEFORE INSERT
   ON "BLT_REF_ERROR_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-1290:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_BLT_REF_ERROR_TYPE" BEFORE UPDATE
   ON "BLT_REF_ERROR_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-1290:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_BLT_REF_WF_CYCLE_STATUS" BEFORE INSERT
   ON "BLT_REF_WF_CYCLE_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-1290:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_BLT_REF_WF_CYCLE_STATUS" BEFORE UPDATE
   ON "BLT_REF_WF_CYCLE_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-1290:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_BLT_WF_CYCLE_LOG" BEFORE INSERT
   ON "BLT_WF_CYCLE_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-1290:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_BLT_WF_CYCLE_LOG" BEFORE UPDATE
   ON "BLT_WF_CYCLE_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-1290:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_BLT_REF_WF_LOG_STATUS" BEFORE INSERT
   ON "BLT_REF_WF_LOG_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-1290:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_BLT_REF_WF_LOG_STATUS" BEFORE UPDATE
   ON "BLT_REF_WF_LOG_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-1290:14 stripComments:false
-- 0 level data for blt_ref_error_type 
/***************************/
/*Insert 0 level data      */
/***************************/
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'CRITICAL','Critical error that must be fixed.',1,0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_error_type WHERE ref_error_type_cd = 'CRITICAL');

--changeSet DEV-1290:15 stripComments:false
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'MAJOR','Major error that must be fixed but may still allow induction into Maintenix.',2,0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_error_type WHERE ref_error_type_cd = 'MAJOR');

--changeSet DEV-1290:16 stripComments:false
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'MINOR','Minor error that should be fixed but may still allow induction into Maintenix.',3,0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM blt_ref_error_type WHERE ref_error_type_cd = 'MINOR');

--changeSet DEV-1290:17 stripComments:false
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'WARNING','Warning that won''t prevent induction into Maintenix but does present an issue.',4,0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_error_type WHERE ref_error_type_cd = 'WARNING');

--changeSet DEV-1290:18 stripComments:false
-- 0 level data for blt_ref_wf_cycle_status
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'PENDING','Pending','The cycle has been initiated and is awaiting the process to start.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_cycle_status WHERE ref_wf_cycle_status_cd = 'PENDING');

--changeSet DEV-1290:19 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'INPROG','In Progress','The cycle is currently processing.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_cycle_status WHERE ref_wf_cycle_status_cd = 'INPROG');

--changeSet DEV-1290:20 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'COMPLTD','Completed','The cycle is complete and all workflows completed successfully.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_cycle_status WHERE ref_wf_cycle_status_cd = 'COMPLTD');

--changeSet DEV-1290:21 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'ERROR','Error','The cycle failed as one or more workflows had errors.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_cycle_status WHERE ref_wf_cycle_status_cd = 'ERROR');

--changeSet DEV-1290:22 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'COMPLTD','Completed','The cycle is complete and all workflows completed successfully.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_cycle_status WHERE ref_wf_cycle_status_cd = 'COMPLTD');

--changeSet DEV-1290:23 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'STAGED','Staged','The cycle has been staged.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_cycle_status WHERE ref_wf_cycle_status_cd = 'STAGED');

--changeSet DEV-1290:24 stripComments:false
-- 0 level data for blt_ref_wf_log_status
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'PENDING','Pending','The record is currently pending and is awaiting the start of its parent workflow.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'PENDING');

--changeSet DEV-1290:25 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'STGQD','Stage Queued','The record is under the control of the parent workflow and is queued for staging.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'STGQD');

--changeSet DEV-1290:26 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'STGING','Staging','The record is under the control of the parent workflow and is in the process of transformation and validation.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'STGING');

--changeSet DEV-1290:27 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'STGED','Staged','The record is under the control of the parent workflow and has successfully completed the transformation and validation process.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'STGED');

--changeSet DEV-1290:28 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'INDCTQD','Induct Queued','The record is under the control of the parent workflow and is queued for induction.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual 
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'INDCTQD');

--changeSet DEV-1290:29 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'INDCTING','Inducting','The record is under the control of the parent workflow and is in the process of being inducted into Maintenix.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'INDCTING');

--changeSet DEV-1290:30 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'INDCTED','Inducted','The record has successfully been inducted into Maintenix.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'INDCTED');

--changeSet DEV-1290:31 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
SELECT 'ERROR','Error','The record has encountered an error while processing.',0,1,0,0,TO_DATE('2011-11-18', 'YYYY-MM-DD'),TO_DATE('2011-11-18', 'YYYY-MM-DD'),0,'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM blt_ref_wf_log_status WHERE ref_wf_log_status_cd = 'ERROR');