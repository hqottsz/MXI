--liquibase formatted sql

--changeset OPER-11512:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create new PPC_TASK_PANEL table
BEGIN
utl_migr_schema_pkg.table_create('
   CREATE TABLE PPC_TASK_PANEL
     (
        TASK_ID RAW (16) NOT NULL ,
        PANEL_DB_ID    NUMBER (10) NOT NULL ,
        PANEL_ID       NUMBER (10) NOT NULL ,
        RSTAT_CD       NUMBER (3) NOT NULL ,
        CREATION_DT    DATE NOT NULL ,
        REVISION_DT    DATE NOT NULL ,
        REVISION_DB_ID NUMBER (10) NOT NULL ,
        REVISION_USER  VARCHAR2 (30) NOT NULL
     )
');
END;
/

--changeset OPER-11512:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints PPC_TASK_PANEL table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE PPC_TASK_PANEL ADD CONSTRAINT PK_PPC_TASK_PANEL PRIMARY KEY ( TASK_ID, PANEL_DB_ID, PANEL_ID )
');

utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE PPC_TASK_PANEL ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');

utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE PPC_TASK_PANEL ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
');
END;
/

--changeSet OPER-11512:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment add FKs to PPC_TASK_PANEL table
BEGIN
utl_migr_schema_pkg.table_constraint_add('  
   ALTER TABLE PPC_TASK_PANEL ADD CONSTRAINT FK_MIM_RSTAT_PPC_TASK_PANEL FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('  
   ALTER TABLE PPC_TASK_PANEL ADD CONSTRAINT FK_PPC_TASK_PPC_TASK_PANEL FOREIGN KEY ( TASK_ID ) REFERENCES PPC_TASK ( TASK_ID ) NOT DEFERRABLE
');
END;
/

--changeset OPER-11512:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create new PPC_MPC_TEMPLATE_TASK table
BEGIN
utl_migr_schema_pkg.table_create('
   CREATE TABLE PPC_MPC_TEMPLATE_TASK (
      TASK_ID RAW (16) NOT NULL ,
      PARENT_TASK_DEFN_DB_ID NUMBER (10) NOT NULL ,
      PARENT_TASK_DEFN_ID    NUMBER (10) NOT NULL ,
      TASK_CLASS_SUBCLASS_CD VARCHAR2 (8) NOT NULL ,
      RSTAT_CD               NUMBER (3) NOT NULL ,
      CREATION_DT            DATE NOT NULL ,
      REVISION_DT            DATE NOT NULL ,
      REVISION_DB_ID         NUMBER (10) NOT NULL ,
      REVISION_USER          VARCHAR2 (30) NOT NULL
  )
');
END;
/

--changeset OPER-11512:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints to PPC_MPC_TEMPLATE_TASK table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE PPC_MPC_TEMPLATE_TASK ADD CONSTRAINT PK_PPC_MPC_TEMPLATE_TASK PRIMARY KEY ( TASK_ID )
');

utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE PPC_MPC_TEMPLATE_TASK ADD CHECK ( TASK_CLASS_SUBCLASS_CD IN (''MPCOPEN'',''MPCCLOSE''))
');

utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE PPC_MPC_TEMPLATE_TASK ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');

utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE PPC_MPC_TEMPLATE_TASK ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
');
END;
/

--changeSet OPER-11512:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment add FKs to PPC_MPC_TEMPLATE_TASK table
BEGIN
utl_migr_schema_pkg.table_constraint_add('  
   ALTER TABLE PPC_MPC_TEMPLATE_TASK ADD CONSTRAINT FK_MIMRSTAT_PPCMPCTEMPLTSK FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
');

utl_migr_schema_pkg.table_constraint_add('  
   ALTER TABLE PPC_MPC_TEMPLATE_TASK ADD CONSTRAINT FK_PPCTASK_PPCMPCTMPLTSK FOREIGN KEY ( TASK_ID ) REFERENCES PPC_TASK ( TASK_ID ) NOT DEFERRABLE
');

END;
/

--changeSet OPER-11512:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PPC_MPC_TEMPLATE_TASK" BEFORE UPDATE
   ON "PPC_MPC_TEMPLATE_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-11512:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PPC_MPC_TEMPLATE_TASK" BEFORE INSERT
   ON "PPC_MPC_TEMPLATE_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-11512:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PPC_TASK_PANEL" BEFORE UPDATE
   ON "PPC_TASK_PANEL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-11512:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PPC_TASK_PANEL" BEFORE INSERT
   ON "PPC_TASK_PANEL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-11512:11 stripComments:false
-- Add 0 level data to REF_PPC_ACTIVITY_TYPE
INSERT INTO REF_PPC_ACTIVITY_TYPE (PPC_ACTIVITY_TYPE_DB_ID, PPC_ACTIVITY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
SELECT 0, 'MPC_TMPL_TASK', 'MPC_TMPL_TASK', 'MPC Template task activity type.', 0, TO_DATE('2017-03-30','YYYY-MM-DD'), TO_DATE('2017-03-30','YYYY-MM-DD'), 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM REF_PPC_ACTIVITY_TYPE WHERE PPC_ACTIVITY_TYPE_DB_ID = 0 AND PPC_ACTIVITY_TYPE_CD = 'MPC_TMPL_TASK');
