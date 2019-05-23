--liquibase formatted sql


--changeSet DEV-173:1 stripComments:false
INSERT INTO
   INT_BP_LOOKUP
   (
      NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 'http://xml.mxi.com/xsd/core/hr/user-licenses/1.0', 'user-licenses', 'JAVA', 'com.mxi.mx.core.adapter.hr.userlicense.UserLicenseEntryPoint', 'coordinate', 0, to_date('18-01-2010 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-01-2010 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/hr/user-licenses/1.0' and ROOT_NAME = 'user-licenses' );      