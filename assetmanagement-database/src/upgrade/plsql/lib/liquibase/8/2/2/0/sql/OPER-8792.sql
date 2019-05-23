--liquibase formatted sql


--changeSet OPER-8792:1 stripComments:false
-- Remove 'LTD' from the organization short description
UPDATE
   org_org
SET
   org_sdesc = 'Mxi Technologies'
WHERE
   org_db_id = 0 AND
   org_id = 1 AND
   org_sdesc = 'Mxi Technologies Ltd.';

--changeSet OPER-8792:2 stripComments:false
UPDATE
   mim_site
SET
   company_sdesc = 'MxI Technologies'
WHERE
   site_cd = '0' AND
   rstat_cd = 0 AND
   company_sdesc = 'MxI Technologies Ltd.';