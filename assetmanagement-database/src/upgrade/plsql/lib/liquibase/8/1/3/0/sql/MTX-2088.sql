--liquibase formatted sql


--changeSet MTX-2088:1 stripComments:false
-- Add entry into int_bp_lookup
-- MTX-2088
-- Add entry point for create user api in HR adapter
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/hr/create-user/2.0','create-user','JAVA', 'com.mxi.mx.core.adapter.hr.createuser.CreateUserEntryPoint', 'process', 'FULL', 0, to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/hr/create-user/2.0' AND root_name = 'create-user');