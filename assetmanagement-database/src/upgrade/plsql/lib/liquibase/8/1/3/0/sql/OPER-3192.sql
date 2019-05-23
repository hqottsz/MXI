--liquibase formatted sql


--changeSet OPER-3192:1 stripComments:false
UPDATE utl_config_parm SET
    parm_desc = 'Represents the number of hours before the part request due date to lock the part request and print the issue ticket. The part will no longer be able to be stolen after it is locked.'
 WHERE
    parm_name = 'PRINT_TICKET_INTERVAL';