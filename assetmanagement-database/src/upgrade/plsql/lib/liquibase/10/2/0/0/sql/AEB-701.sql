--liquibase formatted sql
-- Update the parm_value values for Aerobuy outbound messages wrapper.

--changeSet AEB-701:1 stripComments:false
UPDATE utl_config_parm
SET parm_value = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><soapenv:Body><aer:receiveRequest xmlns:aer="aeroxchange.com"><inXMLData><![CDATA[{0}]]></inXMLData></aer:receiveRequest></soapenv:Body></soapenv:Envelope>'
WHERE parm_name = 'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER_ENVELOP';