--liquibase formatted sql

--changeSet OPER-22657-1:1 stripComments:false
UPDATE utl_config_parm
SET
   parm_desc = 'Used in cases where IFS Maintenix has two inventory records (individual barcodes) that both refer to the same physical inventory item. This permission allows the user to create an association between the records, so that a more complete inventory record can be presented.'
WHERE
   parm_name = 'ACTION_CREATE_INVENTORY_ASSOCIATION';