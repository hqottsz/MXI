--liquibase formatted sql


--changeSet MX-25361:1 stripComments:false
UPDATE 
   UTL_TRIGGER 
SET 
   TRIGGER_NAME = 'update part request by part requirement after cancel part requirement' 
WHERE 
   TRIGGER_ID = 99965 AND 
   TRIGGER_CD = 'MX_PR_CANCEL';

--changeSet MX-25361:2 stripComments:false
UPDATE 
   UTL_TRIGGER 
SET 
   TRIGGER_NAME = 'update part request by part requirement after cancel task' 
WHERE 
   TRIGGER_ID = 99966 AND 
   TRIGGER_CD = 'MX_TS_CANCEL';