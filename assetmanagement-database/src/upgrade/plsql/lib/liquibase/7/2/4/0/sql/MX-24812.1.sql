--liquibase formatted sql


--changeSet MX-24812.1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "UTL_WORK_ITEM" ADD (
	"RETRY_CT" Number(10,0) Check (RETRY_CT BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24812.1:2 stripComments:false
UPDATE
  UTL_WORK_ITEM
SET
  retry_ct = 0
WHERE
  retry_ct IS NULL
;

--changeSet MX-24812.1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_WORK_ITEM modify (
	"RETRY_CT" Number(10,0) Default 0 NOT NULL DEFERRABLE  Check (RETRY_CT BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24812.1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "UTL_WORK_ITEM_TYPE" ADD (
	"MAX_RETRY_CT" Number(10,0) Check (MAX_RETRY_CT BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24812.1:5 stripComments:false
UPDATE
  utl_work_item_type
SET
  MAX_RETRY_CT = 0
WHERE
  MAX_RETRY_CT IS NULL
;

--changeSet MX-24812.1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_WORK_ITEM_TYPE modify (
	"MAX_RETRY_CT" Number(10,0) Default 0 NOT NULL DEFERRABLE  Check (MAX_RETRY_CT BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24812.1:7 stripComments:false
UPDATE
  utl_work_item_type
SET
  max_retry_ct = 10
WHERE
  max_retry_ct != 10
  AND
  name IN ('Real_Time_Aircraft_Deadline_Update', 'Real_Time_Inventory_Deadline_Update')
;