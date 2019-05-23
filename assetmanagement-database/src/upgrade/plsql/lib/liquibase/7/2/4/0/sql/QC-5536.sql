--liquibase formatted sql


--changeSet QC-5536:1 stripComments:false
UPDATE org_org 
   SET org_type_cd = 'MRO' 
 WHERE org_type_cd = 'OPERMRO';

--changeSet QC-5536:2 stripComments:false
UPDATE ref_org_sub_type
   SET org_type_cd = 'MRO'
 WHERE org_type_cd = 'OPERMRO';

--changeSet QC-5536:3 stripComments:false
DELETE FROM ref_org_type 
 WHERE org_type_cd = 'OPERMRO';