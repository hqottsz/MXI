--liquibase formatted sql


--changeSet MTX-300:1 stripComments:false
-- Adding a new UUID surrogate key to PPC_WP
ALTER TRIGGER TUBR_PPC_WP DISABLE;

--changeSet MTX-300:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PPC_WP add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-300:3 stripComments:false
UPDATE 
   PPC_WP 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-300:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PPC_WP modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-300:5 stripComments:false
ALTER TRIGGER TUBR_PPC_WP ENABLE;

--changeSet MTX-300:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PPC_WP_ALT_ID" BEFORE INSERT
   ON "PPC_WP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-300:7 stripComments:false
-- Adding a new UUID surrogate key to PPC_ACTIVITY
ALTER TRIGGER TUBR_PPC_ACTIVITY DISABLE;

--changeSet MTX-300:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PPC_ACTIVITY add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-300:9 stripComments:false
UPDATE 
   PPC_ACTIVITY 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-300:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PPC_ACTIVITY modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-300:11 stripComments:false
ALTER TRIGGER TUBR_PPC_ACTIVITY ENABLE;

--changeSet MTX-300:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PPC_ACTIVITY_ALT_ID" BEFORE INSERT
   ON "PPC_ACTIVITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-300:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- CREATING A NEW TABLE PPC_WP_SNAPSHOT
BEGIN
utl_migr_schema_pkg.table_create('
Create table "PPC_WP_SNAPSHOT" (
   "WP_SNAPSHOT_ID" raw(16) Default SYS_GUID() NOT NULL DEFERRABLE ,
   "WP_ALT_ID" raw(16) NOT NULL DEFERRABLE ,
   "SNAPSHOT_DT" Date NOT NULL DEFERRABLE ,
   "DESC_SDESC" Varchar2 (80) NOT NULL DEFERRABLE ,
   "DESC_LDESC" Varchar2 (4000) ,
   "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
   "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
   "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "CREATION_DT" Date NOT NULL DEFERRABLE ,
   "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "REVISION_DT" Date NOT NULL DEFERRABLE ,
   "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_PPC_WP_SNAPSHOT" primary key ("WP_SNAPSHOT_ID") 
)
');
END;
/

--changeSet MTX-300:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PPC_WP_SNAPSHOT" BEFORE INSERT
   ON "PPC_WP_SNAPSHOT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-300:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PPC_WP_SNAPSHOT" BEFORE UPDATE
   ON "PPC_WP_SNAPSHOT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-300:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WP_SNAPSHOT" add Constraint "FK_MIMRSTAT_PPCWPSNAPSHOT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WP_SNAPSHOT" add Constraint "FK_MIMDB_CTRLPPCWPSNAPSHOT" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WP_SNAPSHOT" add Constraint "FK_MIMDB_CREATEPPCWPSNAPSHOT" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WP_SNAPSHOT" add Constraint "FK_MIMDB_REVISIONPPCWPSNAPSHOT" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- CREATING A NEW TABLE PPC_ACTVTY_SNAPSHOT
BEGIN
utl_migr_schema_pkg.table_create('
Create table "PPC_ACTVTY_SNAPSHOT" (
   "ACTVTY_SNAPSHOT_ID" raw(16) Default SYS_GUID() NOT NULL DEFERRABLE ,
   "WP_SNAPSHOT_ID" raw(16) NOT NULL DEFERRABLE ,
   "ACTIVITY_ALT_ID" raw(16) NOT NULL DEFERRABLE ,
   "START_DT" Date NOT NULL DEFERRABLE ,
   "END_DT" Date NOT NULL DEFERRABLE ,
   "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
   "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
   "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "CREATION_DT" Date NOT NULL DEFERRABLE ,
   "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "REVISION_DT" Date NOT NULL DEFERRABLE ,
   "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_PPC_ACTVTY_SNAPSHOT" primary key ("ACTVTY_SNAPSHOT_ID") 
)
');
END;
/

--changeSet MTX-300:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PPC_ACTVTY_SNAPSHOT" BEFORE INSERT
   ON "PPC_ACTVTY_SNAPSHOT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-300:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PPC_ACTVTY_SNAPSHOT" BEFORE UPDATE
   ON "PPC_ACTVTY_SNAPSHOT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-300:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Establishing a new constraints for PPC_ACTVTY_SNAPSHOT table. 
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_ACTVTY_SNAPSHOT" add Constraint "FK_MIMRSTAT_PPCACTVTSNAPSHOT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_ACTVTY_SNAPSHOT" add Constraint "FK_MIMDB_CTRLPPCACTVTSNAPSHOT" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_ACTVTY_SNAPSHOT" add Constraint "FK_MIMDB_CREATEPPCACTVTSNPSHOT" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_ACTVTY_SNAPSHOT" add Constraint "FK_MIMDB_REVPPCACTVTSNAPSHOT" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MTX-300:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_PPCWPSNAPSHOT_ACTVTSNAPSHOT" ON "PPC_ACTVTY_SNAPSHOT" ("WP_SNAPSHOT_ID")
   ');
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_ACTVTY_SNAPSHOT" add Constraint "FK_PPCWPSNAPSHT_PPCACTSNAPSHT" foreign key ("WP_SNAPSHOT_ID") references "PPC_WP_SNAPSHOT" ("WP_SNAPSHOT_ID")  DEFERRABLE
   ');
END;
/