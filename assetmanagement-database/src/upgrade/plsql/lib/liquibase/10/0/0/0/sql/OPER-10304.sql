--liquibase formatted sql

--changeset OPER-10304:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment drop table REF_PART_REQ_STATUS
BEGIN
DELETE FROM dpo_rep_table
  WHERE table_name = 'REF_PART_REQ_STATUS';
END;
/
--changeset OPER-10304:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PK_REF_PART_REQ_STATUS');
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TIBR_REF_PART_REQ_STATUS');
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TUBR_REF_PART_REQ_STATUS');
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('REF_PART_REQ_STATUS');
END;
/

