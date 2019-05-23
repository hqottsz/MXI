--liquibase formatted sql


--changeSet QC-6343:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Data migration script for QC-6343
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Author: sdevi
--
-- Purpose: Fix the organization code for old Operators 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
DECLARE
/* all records in org_carrier table are in this cursor */
  CURSOR lcur_org_carrier
   IS
     SELECT
        org_carrier.carrier_db_id,
        org_carrier.carrier_id,
        org_carrier.org_db_id,
        org_carrier.org_id,
        org_carrier.carrier_cd    
     FROM
        org_carrier
      ;
      
   lrec_org_carrier lcur_org_carrier%rowtype;     
    
BEGIN 
  FOR lrec_org_carrier IN lcur_org_carrier
  
  LOOP
    /*update the organization code to carrier code for old operators */
    UPDATE
       org_org
    SET
       org_org.org_cd = lrec_org_carrier.carrier_cd
    WHERE
	   /*only old operators that were migrated to org_org from org_carrier by DEV-714.sql 
	   would have the same carrier_db_id, carrier_id and org_db_id, org_id */
       org_org.org_db_id = lrec_org_carrier.org_db_id AND
       org_org.org_id    = lrec_org_carrier.org_id
       AND
       org_org.org_db_id = lrec_org_carrier.carrier_db_id AND
       org_org.org_id    = lrec_org_carrier.carrier_id
    ;    
	
   END LOOP;
END;
/