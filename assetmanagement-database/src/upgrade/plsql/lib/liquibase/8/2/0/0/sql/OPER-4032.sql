--liquibase formatted sql


--changeSet OPER-4032:1 stripComments:false
-- add new entry for UTC time zone
UPDATE utl_timezone SET utl_id = 0 WHERE timezone_cd = 'UTC';

--changeSet OPER-4032:2 stripComments:false
INSERT INTO utl_timezone (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
SELECT
   'UTC',
   'Universal Coordinated Time',
   'Universal Coordinated Time',
   'UTC',
   0,
   0,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_timezone WHERE timezone_cd = 'UTC'
   )
;

--changeSet OPER-4032:3 stripComments:false
UPDATE utl_timezone
SET visible_bool = 0
WHERE
   timezone_cd IN ('Etc/UTC', 'Etc/UCT');