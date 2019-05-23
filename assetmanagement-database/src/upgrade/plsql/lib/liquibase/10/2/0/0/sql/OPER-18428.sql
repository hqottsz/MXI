--liquibase formatted sql

--changeSet OPER-18428:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add SENSITIVITY_CD column
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL add
      (
         SENSITIVITY_CD  VARCHAR2 (8)
      )'
   );
END;
/

--changeSet OPER-18428:2 stripComments:false
-- comments on SENSITIVITY_CD column
COMMENT ON COLUMN REF_ACFT_CAP_LEVEL.SENSITIVITY_CD
IS
  'FK to ref_sensitivity table.';


--changeSet OPER-18428:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CONSTRAINT FK_REFSENS_REFACFTCAPLEV FOREIGN KEY ( SENSITIVITY_CD ) REFERENCES REF_SENSITIVITY ( SENSITIVITY_CD ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-18428:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('CREATE INDEX IX_REFSENS_SENSCD ON REF_ACFT_CAP_LEVEL ( SENSITIVITY_CD ASC ) LOGGING');
END;
/