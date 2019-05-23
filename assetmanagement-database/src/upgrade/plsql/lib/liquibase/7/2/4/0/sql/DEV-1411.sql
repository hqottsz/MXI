--liquibase formatted sql


--changeSet DEV-1411:1 stripComments:false
UPDATE 
  UTL_TRIGGER 
SET 
  CLASS_NAME = 'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById21' 
WHERE 
  TRIGGER_ID = 99999 AND
  TRIGGER_CD = 'MX_FNC_SEND_ORDER_INFORMATION' AND
  CLASS_NAME = 'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById20';