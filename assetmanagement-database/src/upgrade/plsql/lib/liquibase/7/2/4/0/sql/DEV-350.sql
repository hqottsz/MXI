--liquibase formatted sql


--changeSet DEV-350:1 stripComments:false
UPDATE utl_sequence
 SET next_value = 200000
 WHERE sequence_cd = 'TRIGGER_ID_SEQ';

--changeSet DEV-350:2 stripComments:false
UPDATE utl_sequence
 SET next_value = 200000
 WHERE sequence_cd = 'UTL_MENU_ITEM_ID';