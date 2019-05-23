--liquibase formatted sql

--changeset OPER-10268:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table REF_PPC_MILESTONE_TYPE
BEGIN
utl_migr_schema_pkg.table_create('
   Create table "REF_PPC_MILESTONE_TYPE" (
      "PPC_MILESTONE_TYPE_DB_ID" Number(10) NOT NULL ,
      "PPC_MILESTONE_TYPE_CD" Varchar2 (16) NOT NULL ,
      "DESC_SDESC" Varchar2 (80),
      "DESC_LDESC" Varchar2 (4000),
      "RSTAT_CD" Number(3,0) NOT NULL ,
      "CREATION_DT" Date NOT NULL ,
      "REVISION_DT" Date NOT NULL ,
      "REVISION_DB_ID" Number(10) NOT NULL ,
      "REVISION_USER" Varchar2 (30) NOT NULL
   )
   LOGGING
');
END;
/

--changeset OPER-10268:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add PK constraint to table REF_PPC_MILESTONE_TYPE
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE REF_PPC_MILESTONE_TYPE ADD CONSTRAINT PK_REF_PPC_MILESTONE_TYPE PRIMARY KEY ( PPC_MILESTONE_TYPE_DB_ID, PPC_MILESTONE_TYPE_CD )
');
END;
/

--changeset OPER-10268:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add check constraints to table REF_PPC_MILESTONE_TYPE
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE REF_PPC_MILESTONE_TYPE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
');

utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE REF_PPC_MILESTONE_TYPE ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeset OPER-10268:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add FK constraint to table REF_PPC_MILESTONE_TYPE
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE REF_PPC_MILESTONE_TYPE ADD CONSTRAINT FK_MIMDB_REFPPCMILESTONETYPE FOREIGN KEY ( PPC_MILESTONE_TYPE_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeset OPER-10268:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add FK constraint to table REF_PPC_MILESTONE_TYPE
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE REF_PPC_MILESTONE_TYPE ADD CONSTRAINT FK_MIMRSTAT_REFPPCMILESTTYPE FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
');
END;
/

--changeset OPER-10268:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TIBR_REF_PPC_MILESTONE_TYPE
CREATE OR REPLACE TRIGGER "TIBR_REF_PPC_MILESTONE_TYPE" BEFORE INSERT
   ON "REF_PPC_MILESTONE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset OPER-10268:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TUBR_REF_PPC_MILESTONE_TYPE
CREATE OR REPLACE TRIGGER "TUBR_REF_PPC_MILESTONE_TYPE" BEFORE UPDATE
   ON "REF_PPC_MILESTONE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-10268:8 stripComments:false
INSERT INTO ref_ppc_milestone_type (
   ppc_milestone_type_db_id, 
   ppc_milestone_type_cd,
   desc_sdesc,
   desc_ldesc,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
) SELECT
   0, 
   'PROJECT', 
   'Project Milestone', 
   'Project Milestone type', 
   0, 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE NOT EXISTS
   (SELECT * FROM ref_ppc_milestone_type WHERE ppc_milestone_type_db_id = 0 and ppc_milestone_type_cd = 'PROJECT');

--changeSet OPER-10268:9 stripComments:false
INSERT INTO ref_ppc_milestone_type (
   ppc_milestone_type_db_id, 
   ppc_milestone_type_cd,
   desc_sdesc,
   desc_ldesc,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
) SELECT
   0, 
   'TECHNICAL', 
   'Technical Milestone', 
   'Technical Milestone type', 
   0, 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE NOT EXISTS
   (SELECT * FROM ref_ppc_milestone_type WHERE ppc_milestone_type_db_id = 0 and ppc_milestone_type_cd = 'TECHNICAL');

--changeSet OPER-10268:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Add FK columns, FK and Index
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN
utl_migr_schema_pkg.table_column_add('
   Alter table PPC_MILESTONE add (
      PPC_MILESTONE_TYPE_DB_ID NUMBER(10) 
   )
');

utl_migr_schema_pkg.table_column_add('
   Alter table PPC_MILESTONE add (
      PPC_MILESTONE_TYPE_CD Varchar2 (16)
   )
');

utl_migr_schema_pkg.table_constraint_add('  
   ALTER TABLE PPC_MILESTONE ADD CONSTRAINT FK_REFPPCMILSTTYPE_PPCMILEST FOREIGN KEY ( PPC_MILESTONE_TYPE_DB_ID, PPC_MILESTONE_TYPE_CD ) 
      REFERENCES REF_PPC_MILESTONE_TYPE ( PPC_MILESTONE_TYPE_DB_ID, PPC_MILESTONE_TYPE_CD ) NOT DEFERRABLE
');

utl_migr_schema_pkg.index_create('
   Create Index IX_PPCMILESTONE_MILESTONETYPE ON PPC_MILESTONE ( PPC_MILESTONE_TYPE_DB_ID ASC, PPC_MILESTONE_TYPE_CD ASC )
');
END;
/

--changeSet OPER-10268:11 stripComments:false
-- Update all empty columns to their defaults
UPDATE
   ppc_milestone
SET
   ppc_milestone_type_db_id = 0,
   ppc_milestone_type_cd = 
   DECODE
   (
      (
         SELECT
            COUNT(*)
         FROM
            ppc_milestone_cond
         WHERE
            ppc_milestone_cond.milestone_id = ppc_milestone.milestone_id
      ),
      0, 'PROJECT', 'TECHNICAL'
   );

--changeSet OPER-10268:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Change the the PPC_MILESTONE_TYPE_DB_ID:PPC_MILESTONE_TYPE_CD not null
BEGIN
utl_migr_schema_pkg.table_column_modify('
   Alter table PPC_MILESTONE modify (
      PPC_MILESTONE_TYPE_DB_ID Number(10,0) NOT NULL
   )
');

utl_migr_schema_pkg.table_column_modify('
   Alter table PPC_MILESTONE modify (
      PPC_MILESTONE_TYPE_CD Varchar2 (16) NOT NULL
   )
');

END;
/
