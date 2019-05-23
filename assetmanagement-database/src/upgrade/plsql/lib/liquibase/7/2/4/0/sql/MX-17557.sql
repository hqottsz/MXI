--liquibase formatted sql


--changeSet MX-17557:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'AUTO_RSRV_ROW_PROCESS_COUNT', '20', 'LOGIC', 0, 'The maximum number of rows in auto_rsrv_queue for which processing of autoreservation will be attempted. Allowable Values: Number', 'GLOBAL', 'Number', 20, 0, 'Core Logic', '6.8.9', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'AUTO_RSRV_ROW_PROCESS_COUNT' );                  

--changeSet MX-17557:2 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99980, 'MX_IN_AUTO_RESERVATION', 1, 'COMPONENT', 'Auto Reservation Request Handler', 'com.mxi.mx.core.services.inventory.reservation.AutoReservationRequestHandler', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99980);         