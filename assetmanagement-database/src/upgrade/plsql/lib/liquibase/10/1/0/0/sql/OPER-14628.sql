--liquibase formatted sql

--changeSet OPER-14628:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      Alter table UTL_USER_ROLE ADD (TEMP_BOOL Number(1, 0) DEFAULT 0 NOT NULL)
   ');
END;
/  

--changeSet OPER-14628:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_USER_ROLE ADD CHECK ( TEMP_BOOL IN (0, 1))'
   );
END;
/  

--changeSet OPER-14628:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE UTL_TEMP_ASSIGN_ROLE_MAP
      (
         ROLE_ID            NUMBER (10) NOT NULL ,
         ASSIGNABLE_ROLE_ID NUMBER (10) NOT NULL ,
         UTL_ID             NUMBER (10) NOT NULL ,
         RSTAT_CD           NUMBER (3) NOT NULL ,
         CREATION_DT        DATE NOT NULL ,
         REVISION_DT        DATE NOT NULL ,
         REVISION_DB_ID     NUMBER (10) NOT NULL ,
         REVISION_USER      VARCHAR2 (30) NOT NULL
      )
   ');
END;
/

--changeSet OPER-14628:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CHECK ( ROLE_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CHECK ( ASSIGNABLE_ROLE_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CHECK ( UTL_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
END;
/  

--changeSet OPER-14628:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CONSTRAINT PK_UTLTEMPASSIGNROLEMAP PRIMARY KEY ( ROLE_ID, ASSIGNABLE_ROLE_ID )'
   );
END;
/  

--changeSet OPER-14628:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CONSTRAINT FK_MIMRSTAT_TMPASSIGNROLEMAP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE   
   ');
END;
/

--changeSet OPER-14628:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CONSTRAINT FK_UTLROLE_TMPASSIGNROLEMAP FOREIGN KEY ( ROLE_ID ) REFERENCES UTL_ROLE ( ROLE_ID ) NOT DEFERRABLE   
   ');
END;
/

--changeSet OPER-14628:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CONSTRAINT FK_UTLROLE_TMPASSIGNROLEMAP2 FOREIGN KEY ( ASSIGNABLE_ROLE_ID ) REFERENCES UTL_ROLE ( ROLE_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_TEMP_ASSIGN_ROLE_MAP ADD CONSTRAINT FK_UTLTEMPASSIGNROLEMAP_MIMDB FOREIGN KEY ( UTL_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE UTL_USER_TEMP_ROLE
      (
         USER_TEMP_ROLE_ID RAW (16) NOT NULL ,
         USER_ID        NUMBER (10) NOT NULL ,
         ROLE_ID        NUMBER (10) NOT NULL ,
         START_DT       DATE NOT NULL ,
         END_DT         DATE NOT NULL ,
         ASSIGNED_BY    NUMBER (10) NOT NULL ,
         ASSIGNED_DT    DATE NOT NULL ,
         UNASSIGNED_BY  NUMBER (10) ,
         UNASSIGNED_DT  DATE ,
         UTL_ID         NUMBER (10) NOT NULL ,
         RSTAT_CD       NUMBER (3) NOT NULL ,
         CREATION_DT    DATE NOT NULL ,
         REVISION_DT    DATE NOT NULL ,
         REVISION_DB_ID NUMBER (10) NOT NULL ,
         REVISION_USER  VARCHAR2 (30) NOT NULL
      )
   ');
END;
/

--changeSet OPER-14628:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_USER_TEMP_ROLE ADD CHECK ( USER_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_USER_TEMP_ROLE ADD CHECK ( ROLE_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_USER_TEMP_ROLE ADD CHECK ( ASSIGNED_BY BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_USER_TEMP_ROLE ADD CHECK ( UTL_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_USER_TEMP_ROLE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
END;
/  

--changeSet OPER-14628:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE UTL_USER_TEMP_ROLE ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/  

--changeSet OPER-14628:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT PK_UTLUSERTEMPROLE PRIMARY KEY ( USER_TEMP_ROLE_ID )   
   ');
END;
/

--changeSet OPER-14628:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT IX_UTLUSERTEMPROLE UNIQUE ( USER_ID , ROLE_ID , START_DT , END_DT )
   ');
END;
/

--changeSet OPER-14628:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT FK_MIMRSTAT_UTLUSERTEMPROLE FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT FK_UTLROLE_UTLUSERTEMPROLE FOREIGN KEY ( ROLE_ID ) REFERENCES UTL_ROLE ( ROLE_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT FK_UTLUSER_UTLUSERTEMPROLE FOREIGN KEY ( USER_ID ) REFERENCES UTL_USER ( USER_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT FK_UTLUSER_UTLUSERTEMPROLE2 FOREIGN KEY ( ASSIGNED_BY ) REFERENCES UTL_USER ( USER_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT FK_UTLUSER_UTLUSERTEMPROLE3 FOREIGN KEY ( UNASSIGNED_BY ) REFERENCES UTL_USER ( USER_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE UTL_USER_TEMP_ROLE ADD CONSTRAINT FK_UTLUSERTEMPROLE_MIMDB FOREIGN KEY ( UTL_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-14628:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_TEMP_ASSIGN_ROLE_MAP" BEFORE UPDATE
   ON "UTL_TEMP_ASSIGN_ROLE_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-14628:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_TEMP_ASSIGN_ROLE_MAP" BEFORE INSERT
   ON "UTL_TEMP_ASSIGN_ROLE_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-14628:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_USER_TEMP_ROLE" BEFORE UPDATE
   ON "UTL_USER_TEMP_ROLE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-14628:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_USER_TEMP_ROLE" BEFORE INSERT
   ON "UTL_USER_TEMP_ROLE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
