INSERT INTO
org_labour_skill_map
( 
 org_db_id, org_id,labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd 
)
SELECT 
 0, 2, application_object_pkg.getdbid,'AMT', 1, 1, 4, 0 
FROM
 dual
WHERE 
NOT EXISTS 
 (
   SELECT 1 
   FROM org_labour_skill_map 
   WHERE 
     org_labour_skill_map.org_db_id = 0 AND 
     org_labour_skill_map.org_id = 2 AND
     org_labour_skill_map.labour_skill_db_id = application_object_pkg.getdbid AND
     org_labour_skill_map.labour_skill_cd = 'AMT'    
 );
 
 INSERT INTO
 org_labour_skill_map
 ( 
  org_db_id, org_id,labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd 
 )
 SELECT 
  0, 1, application_object_pkg.getdbid,'AMT', 1, 1, 4, 0 
 FROM
  dual
 WHERE 
 NOT EXISTS 
  (
    SELECT 1 
    FROM org_labour_skill_map 
    WHERE 
      org_labour_skill_map.org_db_id = 0 AND 
      org_labour_skill_map.org_id = 1 AND
      org_labour_skill_map.labour_skill_db_id = application_object_pkg.getdbid AND
      org_labour_skill_map.labour_skill_cd = 'AMT'    
 );