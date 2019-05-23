--liquibase formatted sql

--changeSet OPER-18174:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
	Alter table REF_REQ_PRIORITY add (
	    DEFAULT_BOOL NUMBER(1)
	)
');
END;
/

--changeSet OPER-18174:2 stripComments:false
UPDATE 
    ref_req_priority
SET 
    default_bool = 0
WHERE
    default_bool IS NULL;

--changeSet OPER-18174:3 stripComments:false
UPDATE ref_req_priority
SET default_bool = 1
WHERE req_priority_db_id = 0 AND
      req_priority_cd = 'NORMAL';

--changeSet OPER-18174:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
upg_migr_schema_v1_pkg.table_column_modify('
   Alter table REF_REQ_PRIORITY modify (
       DEFAULT_BOOL NUMBER(1) DEFAULT 0 NOT NULL
   )
');
END;
/

--changeSet OPER-18174:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
upg_migr_schema_v1_pkg.table_constraint_add('
   Alter table REF_REQ_PRIORITY add check (
       DEFAULT_BOOL IN (0, 1)
   )
');
END;
/