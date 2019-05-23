--liquibase formatted sql

--changeset OPER-10268-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment adding CLOSED_BOOL field to the PPC_PHASE table
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_PHASE add (
         "CLOSED_BOOL" Number(1) Default 0 NOT NULL
      )
   ');
END;
/

--changeset OPER-10268-2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment adding constraint for CLOSED_BOOL field in the PPC_PHASE table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE PPC_PHASE ADD CHECK ( CLOSED_BOOL IN (0, 1))
   ');
END;
/
