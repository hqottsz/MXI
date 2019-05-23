--liquibase formatted sql

--changeSet OPER-10062:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

--comment Create the ref_deferral_request_status table
BEGIN
   utl_migr_schema_pkg.table_create('
      CREATE TABLE FAIL_DEFER_REF_TASK_DEFN
      (
        FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
        TASK_DEFN_ID RAW (16) NOT NULL ,
        CTRL_DB_ID     NUMBER (10) NOT NULL ,
        REVISION_NO    NUMBER (10) NOT NULL ,
        CREATION_DB_ID NUMBER (10) NOT NULL ,
        RSTAT_CD       NUMBER (3) NOT NULL ,
        CREATION_DT    DATE NOT NULL ,
        REVISION_DT    DATE NOT NULL ,
        REVISION_DB_ID NUMBER (10) NOT NULL ,
        REVISION_USER  VARCHAR2 (30) NOT NULL
      )
   ');
END;
/

--changeSet OPER-10062:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-10062:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/


--changeSet OPER-10062:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-10062:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-10062:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CONSTRAINT PK_FAIL_DEFER_REF_TASK_DEFN PRIMARY KEY ( FAIL_DEFER_REF_ID, TASK_DEFN_ID ) 
   ');
END;
/  


--changeSet OPER-10062:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CONSTRAINT FK_TASKDEFN_FAILDEFERREF FOREIGN KEY ( FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID ) NOT DEFERRABLE 
   ');
END;
/

--changeSet OPER-10062:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CONSTRAINT FK_TASKDEFN_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE 
   ');
END;
/

--changeSet OPER-10062:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE FAIL_DEFER_REF_TASK_DEFN ADD CONSTRAINT FK_TASKDEFN_TASKDEFN FOREIGN KEY ( TASK_DEFN_ID ) REFERENCES TASK_DEFN ( ALT_ID ) NOT DEFERRABLE 
   ');
END;
/


--comment Add the auditing triggers
--changeSet OPER-10062:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_REF_TASK_DEFN" BEFORE INSERT
   ON "FAIL_DEFER_REF_TASK_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

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
END;
/

--changeSet OPER-10062:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FAIL_DEFER_REF_TASK_DEFN" BEFORE UPDATE
   ON "FAIL_DEFER_REF_TASK_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
END;
/
