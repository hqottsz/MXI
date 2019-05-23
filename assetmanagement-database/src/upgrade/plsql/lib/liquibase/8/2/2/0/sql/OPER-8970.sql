--liquibase formatted sql


--changeSet OPER-8970:1 stripComments:false
--change display name and display description 
UPDATE 
   ref_usage_type 
SET display_name = 'Usage Record',
    display_desc = 'Usage Record' 
WHERE usage_type_cd = 'MXACCRUAL';

--changeSet OPER-8970:2 stripComments:false
--change display name and display description 
UPDATE 
   ref_usage_type 
SET 
   display_name = 'Edit Inventory',
   display_desc = 'Edit Inventory' 
WHERE 
   usage_type_cd = 'MXCORRECTION';