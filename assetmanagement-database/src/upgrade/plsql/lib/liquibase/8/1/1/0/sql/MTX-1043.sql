--liquibase formatted sql


--changeSet MTX-1043:1 stripComments:false
UPDATE
   utl_report_type
SET
   report_path = '/organizations/Maintenix/Reports/Core/inventory/DetailFinancialLog'
WHERE 
   report_name = 'inventory.DetailInvFncLog'
;