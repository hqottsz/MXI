--liquibase formatted sql


--changeSet MX-19626:1 stripComments:false
/** remove the approve/unapprove/warning feature from operational installation  */
DELETE FROM 
   db_type_config_parm 
WHERE 
   parm_name IN 
   ( 'ACTION_APPROVE_VENDOR_ORDER_TYPE', 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE', 'ACTION_APPROVE_VENDOR_SERVICE_TYPE', 'ACTION_WARNING_VENDOR_SERVICE_TYPE', 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE', 'ACTION_WARNING_VENDOR_ORDER_TYPE')
   AND
   db_type_cd = 'OPER';

--changeSet MX-19626:2 stripComments:false
/** remove the UNASSIGN vendor feature from deployed ops installation  */
DELETE FROM 
   db_type_config_parm 
WHERE 
   parm_name =  'ACTION_UNASSIGN_VENDOR_FROM_ORGANIZATION';