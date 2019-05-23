--liquibase formatted sql


--changeSet MTX-973_2:1 stripComments:false
-- Update the org_carrier.carrier_cd with the org_org.org_cd corresponding value
-- when they are different and the organization type is OPERATOR
-- This script has to be executed right after MTX-973-1.sql
UPDATE 
   org_carrier
SET 
   org_carrier.carrier_cd = 
   (SELECT
       org_org.org_cd
    FROM 
       org_org
    WHERE
       org_org.org_db_id = org_carrier.org_db_id
       AND
       org_org.org_id = org_carrier.org_id
       AND
       org_org.org_cd <> org_carrier.carrier_cd
       AND
       org_org.org_type_db_id = 0
       AND
       org_org.org_type_cd = 'OPERATOR'
       AND
       org_carrier.rstat_cd = 0
    )
WHERE EXISTS
   (SELECT
       org_org.org_cd
    FROM 
       org_org
    WHERE
       org_org.org_db_id = org_carrier.org_db_id
       AND
       org_org.org_id = org_carrier.org_id
       AND
       org_org.org_cd <> org_carrier.carrier_cd
       AND
       org_org.org_type_db_id = 0
       AND
       org_org.org_type_cd = 'OPERATOR'
       AND
       org_carrier.rstat_cd = 0
    )
;