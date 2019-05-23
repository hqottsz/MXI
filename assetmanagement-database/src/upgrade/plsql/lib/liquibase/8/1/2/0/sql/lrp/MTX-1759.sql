--liquibase formatted sql
   

--changeSet MTX-1759:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Schema Changes --            
--
-- Add new column to LRP_LOC
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table LRP_LOC add ( 
   "DURATION_MODE" VARCHAR2(32) 
)
');
END;
/

--changeSet MTX-1759:2 stripComments:false
UPDATE
   LRP_LOC
SET
   DURATION_MODE = 'MX_LOCATION'
WHERE
   DURATION_MODE IS NULL         AND
   DEFAULT_CAPACITY IS NOT NULL
;

--changeSet MTX-1759:3 stripComments:false
UPDATE 
   LRP_LOC
SET 
   DURATION_MODE = 'MX_DEFINITION'
WHERE
   DURATION_MODE IS NULL
; 

--changeSet MTX-1759:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_LOC modify (
   "DURATION_MODE" VARCHAR2(32) Default ''MX_DEFINITION'' NOT NULL DEFERRABLE
)
');
END;
/

--changeSet MTX-1759:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add the new Ref Table --
BEGIN
utl_migr_schema_pkg.table_create('
   Create table REF_LRP_DURATION_MODE (
      LRP_DURATION_MODE_CD Varchar2 (32) NOT NULL DEFERRABLE ,
      DISPLAY_CODE Varchar2 (20) NOT NULL DEFERRABLE ,
      DISPLAY_NAME Varchar2 (80),
      DISPLAY_DESC Varchar2 (4000),
      DISPLAY_ORD Number(4,0) NOT NULL DEFERRABLE ,
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) ,
      REVISION_NO Number(10,0) NOT NULL DEFERRABLE ,
      CTRL_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) ,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      CREATION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE ,
    Constraint PK_REF_LRP_DURATION_MODE primary key (LRP_DURATION_MODE_CD) 
   )
');
END;
/

--changeSet MTX-1759:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add Indexes --
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_MIM_DB_CREATIONDBID" ON "REF_LRP_DURATION_MODE" ("CREATION_DB_ID")
');
END;
/

--changeSet MTX-1759:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   Alter table "REF_LRP_DURATION_MODE" add Constraint "FK_MIM_DB_CREATIONDBID" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")
');
END;
/

--changeSet MTX-1759:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_MIMDB_CTRLDBID" ON "REF_LRP_DURATION_MODE" ("CTRL_DB_ID")
');
END;
/

--changeSet MTX-1759:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   Alter table "REF_LRP_DURATION_MODE" add Constraint "FK_MIMDB_CTRLDBID" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")
');
END;
/

--changeSet MTX-1759:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_MIMDB_REVISIONDBID" ON "REF_LRP_DURATION_MODE" ("REVISION_DB_ID")
');
END;
/

--changeSet MTX-1759:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   Alter table "REF_LRP_DURATION_MODE" add Constraint "FK_MIMDB_REVISIONDBID" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")
');
END;
/

--changeSet MTX-1759:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_MIMRSTAT_REFLRPDURMODE" ON "REF_LRP_DURATION_MODE" ("RSTAT_CD")
');
END;
/

--changeSet MTX-1759:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   Alter table "REF_LRP_DURATION_MODE" add Constraint "FK_MIMRSTAT_REFLRPDURMODE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")
');
END;
/

--changeSet MTX-1759:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_LRP_DURATION_MODE" BEFORE INSERT
   ON "REF_LRP_DURATION_MODE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-1759:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_LRP_DURATION_MODE" BEFORE UPDATE
   ON "REF_LRP_DURATION_MODE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-1759:16 stripComments:false
-- Update the ref table with data --
INSERT INTO 
   ref_lrp_duration_mode ( LRP_DURATION_MODE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
   SELECT 
      'MX_DEFINITION', 'DEFINITION', 'DEFINITION', 'Work package duration is calculated using the default durations configured on the block or requirement definition.', 1, 0, 1, 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 100, 'MXI'
   FROM 
      DUAL
   WHERE 
      NOT EXISTS (
         SELECT 1 FROM ref_lrp_duration_mode WHERE LRP_DURATION_MODE_CD = 'MX_DEFINITION'
      );

--changeSet MTX-1759:17 stripComments:false
INSERT INTO 
   ref_lrp_duration_mode ( LRP_DURATION_MODE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
   SELECT  
      'MX_LOCATION', 'LOCATION', 'Location', 'Work package duration is calculated using the location capacity configured on the Capacity tab.', 2 , 0, 1, 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 100, 'MXI'
   FROM 
      DUAL
   WHERE 
      NOT EXISTS (
         SELECT 1 FROM ref_lrp_duration_mode WHERE LRP_DURATION_MODE_CD = 'MX_LOCATION'
      ); 

--changeSet MTX-1759:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add Indexes --
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index IX_FK_LRPLOC_REFLRPDURMODE ON LRP_LOC (DURATION_MODE)
');
END;
/

--changeSet MTX-1759:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   Alter table LRP_LOC add Constraint FK_LRPLOC_REFLRPDURMODE foreign key (DURATION_MODE) references REF_LRP_DURATION_MODE (LRP_DURATION_MODE_CD) DEFERRABLE
');
END;
/