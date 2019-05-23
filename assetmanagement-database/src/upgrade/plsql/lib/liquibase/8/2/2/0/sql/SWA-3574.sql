--liquibase formatted sql

--changeSet SWA-3574:1 stripComments:false
INSERT INTO 
  org_hr
  (
    HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
  )
  SELECT
    0, 10, 17, 0, 'SALARY',0, 0,'SYSTEM', NULL,'15-JUL-08', '15-JUL-08',10, 'MXI'
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM org_hr WHERE HR_ID = 10 );

--changeSet SWA-3574:2 stripComments:false
INSERT INTO 
  org_org_hr
  (
    hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
  )
  SELECT
   0, 10, 0, 1, 1, 0, '28-AUG-09', '28-AUG-09', 0, 'MXI'
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM org_org_hr WHERE hr_id = 10 );
