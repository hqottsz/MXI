--liquibase formatted sql
--Add the param API_PURCHASE_ORDER_PARM
--Copy the role permissions from READ_PURCHASE_ORDER and WRITE_PURCHASE_ORDER
--Add all permission sets of READ_PURCHASE_ORDER and WRITE_PURCHASE_ORDER into API_PURCHASE_ORDER_PARM
--Delete the READ_PURCHASE_ORDER and WRITE_PURCHASE_ORDER permission set
--Delete the READ_PURCHASE_ORDER and WRITE_PURCHASE_ORDER

--changeSet OPER-22662:1 stripComments:false
INSERT INTO 
  utl_action_config_parm 
  (
    parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
  SELECT
    'API_PURCHASE_ORDER_PARM', 'FALSE',  0, 'Permission to utilize the Purchase Order API data' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.3', 0 
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_PURCHASE_ORDER_PARM' );

--changeSet OPER-22662:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--copy all permission of READ_PURCHASE_ORDER into API_PURCHASE_ORDER_PARM
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'API_PURCHASE_ORDER_PARM',
      'READ_PURCHASE_ORDER'
   );
END;
/

--changeSet OPER-22662:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--copy all permission of WRITE_PURCHASE_ORDER into API_PURCHASE_ORDER_PARM
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'API_PURCHASE_ORDER_PARM',
      'WRITE_PURCHASE_ORDER'
   );
END;
/

--changeSet OPER-22662:4 stripComments:false
--replace READ_PURCHASE_ORDER entries with API_PURCHASE_ORDER_PARM but where we need to check for primary key violation because 
--in case any perm_set_id exists for API_PURCHASE_ORDER_PARM
UPDATE 
   utl_perm_set_action_parm 
SET 
   parm_name = 'API_PURCHASE_ORDER_PARM' 
WHERE 
   parm_name = 'READ_PURCHASE_ORDER' AND 
   perm_set_id NOT IN 
   (
      SELECT 
         old_perm_set.perm_set_id 
      FROM 
         utl_perm_set_action_parm old_perm_set
      WHERE 
         old_perm_set.parm_name = 'API_PURCHASE_ORDER_PARM'
   ); 


--changeSet OPER-22662:5 stripComments:false
--replace WRITE_PURCHASE_ORDER entries with API_PURCHASE_ORDER_PARM but where we need to check for primary key violation because 
--in case any perm_set_id exists for API_PURCHASE_ORDER_PARM
UPDATE 
   utl_perm_set_action_parm 
SET 
   parm_name = 'API_PURCHASE_ORDER_PARM' 
WHERE 
   parm_name = 'WRITE_PURCHASE_ORDER' AND 
   perm_set_id NOT IN 
   (
      SELECT 
         old_perm_set.perm_set_id 
      FROM 
         utl_perm_set_action_parm old_perm_set
      WHERE 
         old_perm_set.parm_name = 'API_PURCHASE_ORDER_PARM'
   ); 

--changeSet OPER-22662:6 stripComments:false
--delete any entry for READ_PURCHASE_ORDER but we will not lose any existing data since we already updated to API_PURCHASE_ORDER_PARM above
DELETE FROM utl_perm_set_action_parm
WHERE parm_name = 'READ_PURCHASE_ORDER';
    
--changeSet OPER-22662:7 stripComments:false
--delete any entry for WRITE_PURCHASE_ORDER but we will not lose any existing data since we already updated to API_PURCHASE_ORDER_PARM above 
DELETE FROM utl_perm_set_action_parm
WHERE parm_name = 'WRITE_PURCHASE_ORDER';

--changeSet OPER-22662:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
   utl_migr_data_pkg.action_parm_delete('READ_PURCHASE_ORDER');
   utl_migr_data_pkg.action_parm_delete('WRITE_PURCHASE_ORDER');
END;
/