--liquibase formatted sql

--changeSet OPER-25592:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST MODIFY "HR_DB_ID" NUMBER (10) NOT NULL
');
END;
/

--changeSet OPER-25592:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST MODIFY "HR_ID" NUMBER (10) NOT NULL
');
END;
/

--changeSet OPER-25592:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD (APPROVER_HR_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-25592:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD (APPROVER_HR_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-25592:5 stripComments:false
COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.HR_DB_ID IS 'FK to ORG_HR. identifies the user that made the request.' ;

--changeSet OPER-25592:6 stripComments:false
COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.HR_ID IS 'FK to ORG_HR. identifies the user that made the request.' ;

--changeSet OPER-25592:7 stripComments:false
COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.APPROVER_HR_DB_ID IS 'FK to ORG_HR. identifies the user that approved the request.' ;

--changeSet OPER-25592:8 stripComments:false
COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.APPROVER_HR_ID IS 'FK to ORG_HR. identifies the user that approved the request.' ;

--changeSet OPER-25592:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CONSTRAINT FK_FAULTREQUEST_APP_ORGHR FOREIGN KEY ( APPROVER_HR_DB_ID, APPROVER_HR_ID ) REFERENCES ORG_HR ( HR_DB_ID, HR_ID )
	');
END;
/

--changeset OPER-25592:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_SDFAULTREFREQUEST_APPHRID ON SD_FAULT_REFERENCE_REQUEST
        (
          APPROVER_HR_ID ASC ,
          APPROVER_HR_DB_ID ASC
        )
   ');
END;
/