--liquibase formatted sql


--changeSet RND-3731:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_API_DEFINITION" (
	"API_ID" Number NOT NULL DEFERRABLE ,
	"API_CD" Varchar2 (20) NOT NULL DEFERRABLE ,
	"API_NAME" Varchar2 (200) NOT NULL DEFERRABLE ,
	"API_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_API_DEFINITION" primary key ("API_ID") 
) 
');
END;
/

--changeSet RND-3731:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_API_VERSION" (
	"API_VERSION_ID" Number NOT NULL DEFERRABLE ,
	"API_ID" Number NOT NULL DEFERRABLE ,
	"API_VERSION_NAME" Varchar2 (100) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_API_VERSION" primary key ("API_VERSION_ID") 
) 
');
END;
/

--changeSet RND-3731:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_API_VERSION_CONFIG" (
	"API_ID" Number NOT NULL DEFERRABLE ,
	"API_VERSION_ID" Number NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_API_VERSION_CONFIG" primary key ("API_ID") 
) 
');
END;
/

--changeSet RND-3731:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_NOTIF_EVENT_TYPE" (
	"NOTIF_EVENT_TYPE_ID" Number NOT NULL DEFERRABLE ,
	"NOTIF_EVENT_CD" Varchar2 (100) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
	"NOTIF_EVENT_NAME" Varchar2 (200) NOT NULL DEFERRABLE ,
	"NOTIF_EVENT_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_NOTIF_EVENT_TYPE" primary key ("NOTIF_EVENT_TYPE_ID") 
) 
');
END;
/

--changeSet RND-3731:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_NOTIF_DEFINITION" (
	"NOTIF_DEFINITION_ID" Number NOT NULL DEFERRABLE ,
	"NOTIF_TYPE_ID" Number NOT NULL DEFERRABLE ,
	"NOTIF_DEFINITION_NAME" Varchar2 (200) NOT NULL DEFERRABLE ,
	"NOTIF_DEFINITION_LDESC" Varchar2 (4000),
	"NOTIF_TRANSFORMER_CLASS" Varchar2 (200) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_NOTIF_DEFINITION" primary key ("NOTIF_DEFINITION_ID") 
) 
');
END;
/

--changeSet RND-3731:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_NOTIFICATION_CONFIG" (
	"NOTIF_TYPE_ID" Number NOT NULL DEFERRABLE ,
	"ACTIVE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ACTIVE_BOOL IN (0, 1) ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE INITIALLY DEFERRED ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_NOTIFICATION_CONFIG" primary key ("NOTIF_TYPE_ID") 
) 
');
END;
/

--changeSet RND-3731:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_NOTIF_TYPE" (
	"NOTIF_TYPE_ID" Number NOT NULL DEFERRABLE ,
	"NOTIF_TYPE_CD" Varchar2 (100) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
	"NOTIF_TYPE_NAME" Varchar2 (200) NOT NULL DEFERRABLE ,
	"NOTIF_TYPE_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_NOTIF_TYPE" primary key ("NOTIF_TYPE_ID") 
) 
');
END;
/

--changeSet RND-3731:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_API_NOTIF_ASSIGN" (
	"API_VERSION_ID" Number NOT NULL DEFERRABLE ,
	"NOTIF_DEFINITION_ID" Number NOT NULL DEFERRABLE ,
	"NOTIF_EVENT_TYPE_ID" Number NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_API_NOTIF_ASSIGN" primary key ("API_VERSION_ID","NOTIF_DEFINITION_ID") 
) 
');
END;
/

--changeSet RND-3731:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_API_DEFINITION BEFORE INSERT
   ON UTL_API_DEFINITION REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_API_DEFINITION BEFORE UPDATE
   ON UTL_API_DEFINITION REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_API_VERSION BEFORE INSERT
   ON UTL_API_VERSION REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_API_VERSION BEFORE UPDATE
   ON UTL_API_VERSION REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_NOTIF_EVENT_TYPE BEFORE INSERT
   ON UTL_NOTIF_EVENT_TYPE REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_NOTIF_EVENT_TYPE BEFORE UPDATE
   ON UTL_NOTIF_EVENT_TYPE REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_API_NOTIF_ASSIGN BEFORE INSERT
   ON UTL_API_NOTIF_ASSIGN REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_API_NOTIF_ASSIGN BEFORE UPDATE
   ON UTL_API_NOTIF_ASSIGN REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_NOTIF_DEFINITION BEFORE INSERT
   ON UTL_NOTIF_DEFINITION REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_NOTIF_DEFINITION BEFORE UPDATE
   ON UTL_NOTIF_DEFINITION REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_NOTIF_TYPE BEFORE INSERT
   ON UTL_NOTIF_TYPE REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_NOTIF_TYPE BEFORE UPDATE
   ON UTL_NOTIF_TYPE REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_API_VERSION_CONFIG BEFORE INSERT
   ON UTL_API_VERSION_CONFIG REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_API_VERSION_CONFIG BEFORE UPDATE
   ON UTL_API_VERSION_CONFIG REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UTL_NOTIFICATION_CONFIG BEFORE INSERT
   ON UTL_NOTIFICATION_CONFIG REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-3731:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_UTL_NOTIFICATION_CONFIG BEFORE UPDATE
   ON UTL_NOTIFICATION_CONFIG REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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