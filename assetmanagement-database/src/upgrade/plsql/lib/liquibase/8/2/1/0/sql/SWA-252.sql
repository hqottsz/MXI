--liquibase formatted sql


--changeSet SWA-252:1 stripComments:false
INSERT INTO
	UTL_TRIGGER
	(
		TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID
	)
  SELECT
  	99919, 'MX_PO_ISSUE',  1, 'COMPONENT' , 'Spec2000 Issue PO trigger', 'com.mxi.mx.integration.procurement.atasparesorder.triggers.IssueSparesOrderMessageTriggerV1_0', 0, 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM UTL_TRIGGER WHERE TRIGGER_ID = 99919 );