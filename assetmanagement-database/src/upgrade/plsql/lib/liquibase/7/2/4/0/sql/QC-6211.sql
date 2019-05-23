--liquibase formatted sql


--changeSet QC-6211:1 stripComments:false
UPDATE
   ref_event_status
SET
   desc_sdesc = 'The order has been issued.',
   desc_ldesc = 'The order has been issued.'
WHERE
   event_status_db_id = 0 AND
   event_status_cd = 'POISSUED'
;