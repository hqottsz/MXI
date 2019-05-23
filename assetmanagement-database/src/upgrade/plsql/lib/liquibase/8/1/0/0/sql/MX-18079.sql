--liquibase formatted sql


--changeSet MX-18079:1 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- change current trigger REL_NUM_GEN to RO_REL_NUM_GEN  
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- In case user set their own class (customized generator class) for REL_NUM_GEN, we just need to change the trigger_cd
UPDATE UTL_TRIGGER
SET TRIGGER_CD = 'RO_REL_NUM_GEN', TRIGGER_NAME = 'Repair Order Release Number Generator'
WHERE TRIGGER_CD = 'REL_NUM_GEN';

--changeSet MX-18079:2 stripComments:false
-- Change the old system defined generator class name to the new one
UPDATE UTL_TRIGGER
SET CLASS_NAME = 'com.mxi.mx.core.plugin.releasenumber.RepairOrderReleaseNumberGenerator'
WHERE TRIGGER_CD = 'RO_REL_NUM_GEN' AND CLASS_NAME = 'com.mxi.mx.core.plugin.releasenumber.DefaultReleaseNumberGenerator';

--changeSet MX-18079:3 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --    
-- add new trigger WO_REL_NUM_GEN   
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99952 , 'WO_REL_NUM_GEN', 1, 'xxx', 'Work Order Release Number Generator', 'com.mxi.mx.core.plugin.releasenumber.WorkOrderReleaseNumberGenerator', 1, 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_TRIGGER WHERE TRIGGER_ID = 99952 );