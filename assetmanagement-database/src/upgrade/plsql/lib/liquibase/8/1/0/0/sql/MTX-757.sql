--liquibase formatted sql


--changeSet MTX-757:1 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- change current trigger NUM_GEN to RO_NUM_GEN  
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- In case user sets their own class (customized generator class) for NUM_GEN, we just need to change the trigger_cd
UPDATE UTL_TRIGGER
SET TRIGGER_CD = 'RO_NUM_GEN', TRIGGER_NAME = 'Repair Order Number Generator'
WHERE TRIGGER_CD = 'NUM_GEN';

--changeSet MTX-757:2 stripComments:false
-- Change the old system defined generator class name to the new one
UPDATE UTL_TRIGGER
SET CLASS_NAME = 'com.mxi.mx.core.plugin.ordernumber.RepairOrderNumberGenerator'
WHERE TRIGGER_CD = 'RO_NUM_GEN' AND CLASS_NAME = 'com.mxi.mx.core.plugin.ordernumber.DefaultOrderNumberGenerator';

--changeSet MTX-757:3 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --    
-- add new trigger WO_NUM_GEN   
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99951 , 'WO_NUM_GEN', 1, 'xxx', 'Work Order Number Generator', 'com.mxi.mx.core.plugin.ordernumber.WorkOrderNumberGenerator', 1, 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_TRIGGER WHERE TRIGGER_ID = 99951 );