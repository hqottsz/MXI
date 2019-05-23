--liquibase formatted sql


--changeSet QC-6336:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Migration script for QC-6336
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Author: sdevi
--
-- Purpose:
-- 1) The MRO's and operators that are not directly below the ROOT organization are considered as invalid configurations.
-- this migration script will find these invalid MROs and Operators, and replace their parent organization with the ROOT organization.
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
DECLARE
  -- cursor to hold invalid configurations for MRO/OPER organizations
   CURSOR lcur_InvalidOrganizations IS
      SELECT
        org_org.org_db_id,
        org_org.org_id
      FROM
        org_org
        INNER JOIN org_org parent_org ON
           parent_org.org_db_id = org_org.nh_org_db_id AND
           parent_org.org_id    = org_org.nh_org_id
      WHERE
        --find all MRO and OPERATORs which are not under the ROOT organization
        org_org.org_type_db_id = 0 AND
        org_org.org_type_cd IN ('MRO', 'OPERATOR')
        AND
        parent_org.nh_org_db_id IS NOT NULL;
      
   lrec_InvalidOrg lcur_InvalidOrganizations%ROWTYPE;
      
   -- cursor to hold the root organization   
   CURSOR lcur_RootOrganization IS
	   SELECT
		 org_org.org_db_id AS root_org_db_id,
		 org_org.org_id AS root_org_id
	   FROM 
		 org_org
	   WHERE 
		 org_org.nh_org_db_id IS NULL AND
		 org_org.nh_org_id IS NULL;   
   
   lrec_RootOrg lcur_RootOrganization%ROWTYPE;  
      
BEGIN 
   OPEN lcur_RootOrganization;
   FETCH lcur_RootOrganization INTO lrec_RootOrg;
   CLOSE lcur_RootOrganization;
   
   -- replace the parent organization for the invalid MROs and OPERATORs with the ROOT Organization
   FOR lrec_InvalidOrg IN lcur_InvalidOrganizations LOOP
      UPDATE org_org
      SET org_org.nh_org_db_id = lrec_RootOrg.root_org_db_id,
          org_org.nh_org_id    = lrec_RootOrg.root_org_id
      WHERE
          org_org.org_db_id = lrec_InvalidOrg.org_db_id AND
          org_org.org_id = lrec_InvalidOrg.org_id;
   END LOOP;
END;
/