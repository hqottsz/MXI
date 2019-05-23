INSERT INTO
ref_labour_skill
( 
 labour_skill_db_id, labour_skill_cd, desc_sdesc, desc_ldesc, est_hourly_cost, esig_req_bool, rstat_cd 
)
SELECT 
 application_object_pkg.getdbid,'AMT', 'Aircraft Maintenance Technician', '', 1, 1, 0  
FROM
 dual
WHERE 
NOT EXISTS (SELECT 1 FROM ref_labour_skill WHERE ref_labour_skill.labour_skill_db_id = application_object_pkg.getdbid AND ref_labour_skill.labour_skill_cd = 'AMT');