--liquibase formatted sql


--changeSet MX-28966:1 stripComments:false
-- Update HTTP_COMPRESSION_EXCLUDE_URLS for Jasper SSO login
UPDATE utl_config_parm
SET    parm_value = parm_value || ',.*/ValidateTicket',
       default_value = default_value || ',.*/ValidateTicket'
WHERE  parm_name = 'HTTP_COMPRESSION_EXCLUDE_URLS'
       AND
       parm_value NOT LIKE '%,.*/ValidateTicket%';