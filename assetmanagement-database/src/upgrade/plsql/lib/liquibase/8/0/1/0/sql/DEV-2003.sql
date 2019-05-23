--liquibase formatted sql


--changeSet DEV-2003:1 stripComments:false
ALTER TRIGGER TUBR_SCHED_LABOUR_ROLE DISABLE;

--changeSet DEV-2003:2 stripComments:false
-- adding surrogate key to SCHED_LABOUR_ROLE
ALTER TRIGGER TUBR_SCHED_LABOUR_ROLE DISABLE;

--changeSet DEV-2003:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_LABOUR_ROLE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2003:4 stripComments:false
UPDATE 
   SCHED_LABOUR_ROLE
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2003:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_LABOUR_ROLE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2003:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_LABOUR_ROLE_ALT_ID" BEFORE INSERT
   ON "SCHED_LABOUR_ROLE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2003:7 stripComments:false
ALTER TRIGGER TUBR_SCHED_LABOUR_ROLE ENABLE;

--changeSet DEV-2003:8 stripComments:false
-- Timesheet Adapter
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, ROOT_NAME, REF_TYPE,
   REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD,
   RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
)
SELECT
   'http://xml.mxi.com/xsd/core/labor/get-completed-work-hours-request/1.0','get-completed-work-hours-request','JAVA',
   'com.mxi.mx.core.adapter.labor.getcompletedhours.GetCompletedHoursEntryPoint10', 'process', 'FULL',
   0, to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT
         1
      FROM
         INT_BP_LOOKUP
      WHERE
         NAMESPACE = 'http://xml.mxi.com/xsd/core/labor/get-completed-work-hours-request/1.0'
   );

--changeSet DEV-2003:9 stripComments:false
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, ROOT_NAME, REF_TYPE,
   REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD,
   RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
)
SELECT
   'http://xml.mxi.com/xsd/core/labor/update-completed-work-hours-request/1.0','update-completed-work-hours-request','JAVA',
   'com.mxi.mx.core.adapter.labor.updatecompletedhours.UpdateCompletedHoursEntryPoint10', 'process', 'FULL',
   0, to_date('21-03-2013 11:01:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('21-03-2013 11:01:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT
         1
      FROM
         INT_BP_LOOKUP
      WHERE
         NAMESPACE = 'http://xml.mxi.com/xsd/core/labor/update-completed-work-hours-request/1.0'
   );