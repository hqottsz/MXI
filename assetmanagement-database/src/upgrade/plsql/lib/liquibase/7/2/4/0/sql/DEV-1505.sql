--liquibase formatted sql


--changeSet DEV-1505:1 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/matadapter/part_definition_updated/1.0', 'part_definition_updated', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('02-04-2013 10:48:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('02-04-2013 10:48:32', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT  1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/matadapter/part_definition_updated/1.0' AND root_name = 'part_definition_updated');