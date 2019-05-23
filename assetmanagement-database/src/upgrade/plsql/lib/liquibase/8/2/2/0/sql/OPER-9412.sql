--liquibase formatted sql

--changeSet OPER-9412:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table SD_FAULT_DEFERRAL_REQUEST add (
         "STAGE_REASON_DB_ID" Number(10,0) Check (STAGE_REASON_DB_ID BETWEEN 0 AND 4294967295 ) 
      )
   ');
END;
/

--changeSet OPER-9412:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table SD_FAULT_DEFERRAL_REQUEST add (
         "STAGE_REASON_CD" Varchar2(16) 
      )
   ');
END;
/

--changeSet OPER-9412:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table SD_FAULT_DEFERRAL_REQUEST add (
         "DEFERRAL_NOTES" Varchar2(4000) 
      )
   ');
END;
/

--changeSet OPER-9412:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT FK_REFSTAGEREA_SDFAULTDEFERREQ FOREIGN KEY ( STAGE_REASON_DB_ID, STAGE_REASON_CD ) REFERENCES REF_STAGE_REASON ( STAGE_REASON_DB_ID, STAGE_REASON_CD ) NOT DEFERRABLE 
   ');
END;
/
