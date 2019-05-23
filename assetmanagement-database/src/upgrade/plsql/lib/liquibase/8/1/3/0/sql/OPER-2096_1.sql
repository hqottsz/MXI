--liquibase formatted sql


--changeSet OPER-2096_1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- 
-- Add Edit Skill Order config parm
-- --  
BEGIN
 
utl_migr_data_pkg.action_parm_insert(
 'ACTION_EDIT_SKILL_ORDER', 
 'Permission to edit skill order.', 
 'TRUE/FALSE',            
 'FALSE',                  
  1,                        
 'HR - Users',         
 '8.1-SP3',                    
 0,
 0,
 UTL_MIGR_DATA_PKG.DbTypeCdList('OPER') 
);
 
END;
/

--changeSet OPER-2096_1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- 
-- Add new SKILL_ORDER column. Not Null before migration
-- -- 
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table org_labour_skill_map add (
   "SKILL_ORDER" Number(4,0) 
)
');
END;
/ 

--changeSet OPER-2096_1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- 
-- Migrate skill_order column
-- -- 
DECLARE

	ln_Count number;
	
	ln_OrgDbId org_labour_skill_map.org_db_id%TYPE;
  ln_OrgId   org_labour_skill_map.org_id%TYPE;
  
  ln_LabourSkillDbId org_labour_skill_map.labour_skill_db_id%TYPE;
  ln_LabourSkillCd   org_labour_skill_map.labour_skill_cd%TYPE;


  -- Retrieve all organizations that has skills assigned
	CURSOR lcur_OrganizationWithSkills IS

	SELECT
		org_labour_skill_map.org_db_id, 
    org_labour_skill_map.org_id, 
    count(*) as skills_count
	FROM
		org_labour_skill_map
  GROUP BY 
        org_labour_skill_map.org_db_id, org_labour_skill_map.org_id;

  lrec_OrganizationWithSkills lcur_OrganizationWithSkills%ROWTYPE;
  
  -- Retrieve all labour skills given organization order by skill code in descending order
  CURSOR lcur_SkillsByOrg(
    an_OrgDbId org_labour_skill_map.org_db_id%TYPE,
    an_OrgId   org_labour_skill_map.org_id%TYPE
  ) IS
  SELECT
        org_labour_skill_map.labour_skill_db_id,
        org_labour_skill_map.labour_skill_cd
  FROM
      org_labour_skill_map
  WHERE
       org_labour_skill_map.org_db_id = an_OrgDbId AND
       org_labour_skill_map.org_id    = an_OrgId
  ORDER BY org_labour_skill_map.skill_order DESC NULLS FIRST, UPPER(org_labour_skill_map.labour_skill_cd) DESC;
       
  lrec_SkillsByOrg lcur_SkillsByOrg%ROWTYPE;

BEGIN
     
    -- loop through all organizations with skills
    FOR lrec_OrganizationWithSkills IN lcur_OrganizationWithSkills LOOP
        
        ln_OrgDbId := lrec_OrganizationWithSkills.org_db_id;
        ln_OrgId   := lrec_OrganizationWithSkills.org_id;
        ln_Count   := lrec_OrganizationWithSkills.skills_count;
         
        -- through through all skills given organization
        FOR lrec_SkillsByOrg IN lcur_SkillsByOrg( ln_OrgDbId,ln_OrgId ) LOOP
        
            ln_LabourSkillDbId := lrec_SkillsByOrg.labour_skill_db_id;
            ln_LabourSkillCd   := lrec_SkillsByOrg.labour_skill_cd;
            
            -- update the skill order
            UPDATE org_labour_skill_map
            SET skill_order = ln_Count 
            WHERE
                 org_db_id = ln_OrgDbId AND
                 org_id    = ln_OrgId
                 AND
                 labour_skill_db_id = ln_LabourSkillDbId AND
                 labour_skill_cd    = ln_LabourSkillCd;
                 
            ln_Count := ln_Count-1;
        
        END LOOP;   
    END LOOP;
END;
/