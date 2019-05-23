/*************************************************
** INSERT SCRIPT FOR UTL_ACTION_ROLE_PARM
**************************************************/

-- Purchasing Manager
INSERT INTO 
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT 
   10019, 'API_PURCHASE_ORDER_REQUEST', 'true', 10
FROM 
   dual
WHERE 
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10019 AND parm_name = 'API_PURCHASE_ORDER_REQUEST');

   
INSERT INTO 
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT 
   10019, 'API_CREATE_ORDER_EXCEPTION_REQUEST', 'true', 10
FROM 
   dual
WHERE 
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10019 AND parm_name = 'API_CREATE_ORDER_EXCEPTION_REQUEST');
   

INSERT INTO 
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT 
   10019, 'API_RAISE_ALERT_REQUEST', 'true', 10
FROM 
   dual
WHERE 
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10019 AND parm_name = 'API_RAISE_ALERT_REQUEST');
   