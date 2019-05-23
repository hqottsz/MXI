--liquibase formatted sql


--changeSet DEV-585:1 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-request/1.0', 'optimize-production-plan-request', 'JAVA', 
'com.mxi.mx.core.adapter.ppc.optimize.optimizeproductionplan.OptmizeProductionPlanEntryPoint', 'coordinate', 
0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-request/1.0' AND ROOT_NAME = 'optimize-production-plan-request');

--changeSet DEV-585:2 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-result/1.0', 'optimize-production-plan-result', 'JAVA', 
'com.mxi.mx.core.adapter.ppc.optimize.productionplanresult.ProductionPlanResultEntryPoint', 'coordinate', 
0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-result/1.0' AND ROOT_NAME = 'optimize-production-plan-result');

--changeSet DEV-585:3 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-status/1.0', 'optimize-production-plan-status', 'JAVA', 
'com.mxi.mx.core.adapter.ppc.optimize.productionplanstatus.ProductionPlanStatusEntryPoint', 'coordinate', 
0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-status/1.0' AND ROOT_NAME = 'optimize-production-plan-status');