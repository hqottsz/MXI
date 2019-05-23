--liquibase formatted sql


--changeSet OPER-1542:1 stripComments:false
-- Change only display order for MXDCRSE code 
-- which is the same as MXCNCL code
UPDATE
   ref_change_reason 
SET
   display_ord = 6
WHERE
  change_reason_cd = 'MXDCRSE'
;