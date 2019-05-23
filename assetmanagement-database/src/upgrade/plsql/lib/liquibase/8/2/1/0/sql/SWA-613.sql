--liquibase formatted sql


--changeSet SWA-613:1 stripComments:false
/**************************************************************************************
*
* Insert trigger for the Cancel Spares Order in utl_trigger
*
***************************************************************************************/
INSERT INTO
  UTL_TRIGGER
  (
    TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID
  )
  SELECT
    99917, 'MX_PO_CANCEL',  1, 'COMPONENT' , 'Spec2000 Cancel PO trigger', 'com.mxi.mx.integration.procurement.atasparesorder.triggers.CancelSparesOrderMessageTriggerV1_0', 0, 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_TRIGGER WHERE TRIGGER_ID = 99917 );