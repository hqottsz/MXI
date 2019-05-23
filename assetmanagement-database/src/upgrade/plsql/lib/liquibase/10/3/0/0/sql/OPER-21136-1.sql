--liquibase formatted sql

--changeSet OPER-21136-1:1 stripComments:false
--comment Maintenix Job to generate index for enhanced part search
UPDATE utl_job SET active_bool = 1
WHERE job_cd = 'MX_CORE_GENERATE_PART_SEARCH_INDEX';

--changeSet OPER-21136-1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment index creation on MT_ENH_PART_SEARCH
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
		CREATE INDEX IX_ENPARTSEARCH_PARTIDS
		ON MT_ENH_PART_SEARCH
		(
			EQP_PART_NO_PART_NO_DB_ID,
			EQP_PART_NO_PART_NO_ID
		)
	');
END;
/

--changeSet OPER-21136-1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment index creation on MT_ENH_PART_SEARCH
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
		CREATE INDEX IX_ENPARTSEARCH_BOMIDS
		ON MT_ENH_PART_SEARCH
		(
			EQP_BOM_PART_BOM_PART_DB_ID,
			EQP_BOM_PART_BOM_PART_ID
		)
	');
END;
/