--liquibase formatted sql


--changeSet SWA-2002:1 stripComments:false
/**************************************************************************************
* 
* SWA-2002 Update REF_EVENT_STATUS
*
***************************************************************************************/
UPDATE 
   REF_EVENT_STATUS 
SET 
   desc_sdesc = 'Cancelled' 
WHERE 
   event_status_cd = 'POCANCEL';

