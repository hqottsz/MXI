--liquibase formatted sql


--changeSet QC-6448:1 stripComments:false
-- Removes the ENCF event relationship ("flight encounters fault") and all events relationships of that type
-- 1) Remove the relationships
DELETE FROM 
   evt_event_rel 
WHERE 
   rel_type_db_id = 0 AND
   rel_type_cd    = 'ENCF';   

--changeSet QC-6448:2 stripComments:false
-- 2) Remove the relationship type
DELETE FROM
   ref_rel_type
WHERE
   rel_type_db_id = 0 AND
   rel_type_cd    = 'ENCF';   