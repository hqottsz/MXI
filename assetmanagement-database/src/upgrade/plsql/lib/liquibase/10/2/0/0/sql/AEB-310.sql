--liquibase formatted sql
-- Update the default values for Aerobuy outbound messages wrapper.

--changeSet AEB-310:1 stripComments:false
UPDATE
utl_config_parm
SET
parm_value = 'TRUE'
WHERE 
parm_name = 'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER';

--changeSet AEB-310:2 stripComments:false
UPDATE
utl_config_parm
SET
default_value = 'TRUE'
WHERE
parm_name = 'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER';

--changeSet AEB-310:3 stripComments:false
UPDATE
utl_config_parm
SET
parm_value = '<aer:receiveRequest xmlns:aer="aeroxchange.com"><inXMLData><![CDATA[{0}]]></inXMLData></aer:receiveRequest>'
WHERE 
parm_name = 'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER_ENVELOP';

--changeSet AEB-310:4 stripComments:false
UPDATE
utl_config_parm
SET
default_value = '<aer:receiveRequest xmlns:aer="aeroxchange.com"><inXMLData><![CDATA[{0}]]></inXMLData></aer:receiveRequest>'
WHERE
parm_name = 'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER_ENVELOP';