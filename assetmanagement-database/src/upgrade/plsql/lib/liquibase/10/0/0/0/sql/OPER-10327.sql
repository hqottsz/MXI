--liquibase formatted sql

--changeset OPER-10327:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment drop table REF_TECH_MILESTONE
BEGIN
DELETE FROM dpo_rep_table
  WHERE table_name = 'REF_TECH_MILESTONE';
END;
/
--changeset OPER-10327:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PK_REF_TECH_MILESTONE');
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TIBR_REF_TECH_MILESTONE');
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TUBR_REF_TECH_MILESTONE');
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('REF_TECH_MILESTONE');
END;
/

