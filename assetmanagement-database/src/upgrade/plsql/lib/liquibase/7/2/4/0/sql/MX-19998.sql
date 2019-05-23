--liquibase formatted sql


--changeSet MX-19998:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REF_LABOUR_SKILL add (
                "ESIG_REQ_BOOL" Number(1,0) Check (ESIG_REQ_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet MX-19998:2 stripComments:false
UPDATE REF_LABOUR_SKILL SET ESIG_REQ_BOOL = 1 WHERE ESIG_REQ_BOOL IS NULL;

--changeSet MX-19998:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_LABOUR_SKILL modify (
                "ESIG_REQ_BOOL" Number(1,0) DEFAULT 1 NOT NULL DEFERRABLE Check (ESIG_REQ_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/