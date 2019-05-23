--liquibase formatted sql

--changeset OPER-30194:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column UTL_FILE_IMPORT.HR_DB_ID
BEGIN

   upg_migr_schema_v1_pkg.table_column_add('
      Alter table UTL_FILE_IMPORT add (
         HR_DB_ID               NUMBER (10) NOT NULL
      )
   ');

END;
/

--changeset OPER-30194:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column UTL_FILE_IMPORT.HR_ID
BEGIN

   upg_migr_schema_v1_pkg.table_column_add('
      Alter table UTL_FILE_IMPORT add (
         HR_ID                  NUMBER (10) NOT NULL
      )
   ');

END;
/

--changeset OPER-30194:3 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.HR_DB_ID
IS
  'The hr db id reference of the user who uploaded the file' ;

--changeset OPER-30194:4 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.HR_ID
IS
  'The hr id reference of the user who uploaded the file' ;


--changeset OPER-30194:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_ORGHR_UTLFILEIMPORT to table UTL_FILE_IMPORT
BEGIN

   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE UTL_FILE_IMPORT ADD CONSTRAINT FK_ORGHR_UTLFILEIMPORT FOREIGN KEY ( HR_DB_ID, HR_ID ) REFERENCES ORG_HR ( HR_DB_ID, HR_ID ) NOT DEFERRABLE
   ');

END;
/