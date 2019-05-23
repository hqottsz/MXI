--liquibase formatted sql


--changeSet MX-16828:1 stripComments:false
UPDATE 
  utl_config_parm
SET
  parm_desc= 'If true, all purchase/repair/exchange order lines for repairable parts must have an associated warranty (or exemption) before the order can be issued.',
  CONFIG_TYPE= 'GLOBAL',
  CATEGORY = 'Purchasing - Purchase Orders',
  MODIFIED_IN =  '7.5'
WHERE 
  parm_name = 'ENFORCE_WARRANTY_REP_ORDER_LINES';

--changeSet MX-16828:2 stripComments:false
DELETE FROM 
   utl_user_parm 
WHERE 
   parm_type = 'SECURED_RESOURCE' 
   AND 
   parm_name = 'ENFORCE_WARRANTY_REP_ORDER_LINES';

--changeSet MX-16828:3 stripComments:false
DELETE FROM 
   utl_role_parm 
WHERE 
   parm_type = 'SECURED_RESOURCE' 
   AND   
   parm_name = 'ENFORCE_WARRANTY_REP_ORDER_LINES';

--changeSet MX-16828:4 stripComments:false
UPDATE 
   utl_config_parm 
SET 
   config_type = 'GLOBAL' 
WHERE 
   parm_type = 'SECURED_RESOURCE' 
   AND 
   parm_name = 'ENFORCE_WARRANTY_REP_ORDER_LINES';