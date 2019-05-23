--liquibase formatted sql


--changeSet MX-20828:1 stripComments:false
UPDATE UTL_CONFIG_PARM
SET    PARM_DESC = 'The maximum amount of time (in seconds) that the Message Ordering Processor will wait before skipping the next expected message.'
WHERE  PARM_NAME = 'MESSAGE_ORDER_TIMEOUT'
AND    PARM_TYPE = 'INTEGRATION';