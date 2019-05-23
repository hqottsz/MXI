--liquibase formatted sql


--changeSet MX-28108:1 stripComments:false
UPDATE
   utl_config_parm
SET
   utl_config_parm.parm_desc = 'Difference, in percentage, between the previous price and the new price of a modified purchase order, above which Maintenix assesses whether the purchase order must be reapproved. To specify a price difference of 10% as the value, type 0.1.'
WHERE
   utl_config_parm.parm_name = 'REAUTHORIZE_PO_PRICE_TOLERANCE' AND 
   EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'REAUTHORIZE_PO_PRICE_TOLERANCE');