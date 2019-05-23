--liquibase formatted sql


--changeSet OPER-2096_3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- 
-- Migrate org_hr_shift_plan.labour_skill_db_idcd columns
-- -- 
DECLARE

  ln_HrDbId org_hr.hr_db_id%TYPE;
  ln_HrId   org_hr.hr_id%TYPE;
  
  ln_LabourSkillDbId org_labour_skill_map.labour_skill_db_id%TYPE;
  ln_LabourSkillCd   org_labour_skill_map.labour_skill_cd%TYPE;
  
 
  -- Retrieve all hr plan shifts that does not have labour specified
  CURSOR lcur_GetHrWithNoSkills IS

	SELECT DISTINCT
		org_hr_shift_plan.hr_db_id,
    org_hr_shift_plan.hr_id
	FROM
		org_hr_shift_plan
    
  WHERE
       org_hr_shift_plan.labour_skill_db_id IS NULL;
 
  lrec_GetHrWithNoSkills lcur_GetHrWithNoSkills%ROWTYPE;
  
  -- Retrieve hr plan shift info given hr
  CURSOR lcur_GetPlanShiftInfo(
    an_HrDbId org_hr_shift_plan.hr_db_id%TYPE,
    an_HrId org_hr_shift_plan.hr_id%TYPE
  ) IS

	SELECT
		org_hr_shift_plan.hr_db_id,
    org_hr_shift_plan.hr_id,
    org_hr_shift_plan.hr_shift_plan_id,
    org_hr_shift_plan.shift_db_id,
    org_hr_shift_plan.shift_id
	FROM
		org_hr_shift_plan
    
  WHERE
       org_hr_shift_plan.hr_db_id = an_HrDbId AND
       org_hr_shift_plan.hr_id    = an_HrId;
 
  lrec_GetPlanShiftInfo lcur_GetPlanShiftInfo%ROWTYPE;
  
  -- Retrieve skill ranked highest given hr
  CURSOR lcur_HighestRankedSkill(
    an_HrDbId org_hr_shift_plan.hr_db_id%TYPE,
    an_HrId org_hr_shift_plan.hr_id%TYPE
  ) IS
  SELECT *
  FROM
      (
        SELECT 
              org_labour_skill_map.labour_skill_db_id,
              org_labour_skill_map.labour_skill_cd
        FROM
            org_hr
            
            INNER JOIN org_hr_qual ON
                  org_hr.hr_db_id = org_hr_qual.hr_db_id AND
                  org_hr.hr_id    = org_hr_qual.hr_id
                  
            INNER JOIN org_org_hr ON
                   org_org_hr.hr_db_id = org_hr_qual.hr_db_id AND
                   org_org_hr.hr_id    = org_hr_qual.hr_id
      
            INNER JOIN org_labour_skill_map ON
                   org_labour_skill_map.org_db_id = org_org_hr.org_db_id AND
                   org_labour_skill_map.org_id    = org_org_hr.org_id AND
                   org_labour_skill_map.labour_skill_db_id = org_hr_qual.labour_skill_db_id AND
                   org_labour_skill_map.labour_skill_cd    = org_hr_qual.labour_skill_cd  
        WHERE
             org_hr.hr_db_id = an_HrDbId AND
             org_hr.hr_id    = an_HrId
             AND
             org_org_hr.default_org_bool = 1
             
        ORDER BY org_labour_skill_map.skill_order
      )
  WHERE
     ROWNUM = 1;
       
  lrec_HighestRankedSkill lcur_HighestRankedSkill%ROWTYPE;

BEGIN
     
    -- loop through all hr plan shifts with no labour
    FOR lrec_GetHrWithNoSkills IN lcur_GetHrWithNoSkills LOOP
        
        ln_HrDbId := lrec_GetHrWithNoSkills.hr_db_id;
        ln_HrId   := lrec_GetHrWithNoSkills.hr_id;
        
        -- get highest rank skill
        OPEN lcur_HighestRankedSkill(ln_HrDbId, ln_HrId);
        FETCH lcur_HighestRankedSkill INTO lrec_HighestRankedSkill;
        
        -- if user has no skills print out row info, then delete row
        IF lcur_HighestRankedSkill%NOTFOUND THEN
        
           dbms_output.put_line( 'Rows in org_hr_shift_plan removed due to no primary skills defined for user.'); 
           
           FOR lrec_GetPlanShiftInfo IN lcur_GetPlanShiftInfo(ln_HrDbId, ln_HrId) LOOP
          
             -- print out row information
             dbms_output.put_line('Hr_db_id,hr_id,Hr_shift_plan_id,shift_db_id,shift_id');
             dbms_output.put_line( lrec_GetPlanShiftInfo.hr_db_id || ',' || lrec_GetPlanShiftInfo.hr_id || ',' || lrec_GetPlanShiftInfo.hr_shift_plan_id || ','|| 
             lrec_GetPlanShiftInfo.shift_db_id || ',' || lrec_GetPlanShiftInfo.shift_id );
             
           END LOOP;
          
           -- remove rows
           DELETE FROM org_hr_shift_plan
           WHERE 
                 org_hr_shift_plan.hr_db_id = ln_HrDbId AND
                 org_hr_shift_plan.hr_id    = ln_HrId;
                     
        ELSE
           ln_LabourSkillDbId := lrec_HighestRankedSkill.labour_skill_db_id;
           ln_LabourSkillCd   := lrec_HighestRankedSkill.labour_skill_cd;
        END IF;
        
        CLOSE lcur_HighestRankedSkill;
    
        -- update the skill order
        UPDATE org_hr_shift_plan
        SET labour_skill_db_id = ln_LabourSkillDbId,
            labour_skill_cd    = ln_LabourSkillCd
        WHERE
             org_hr_shift_plan.hr_db_id = ln_HrDbId AND
             org_hr_shift_plan.hr_id    = ln_HrId;
    
    END LOOP;
END;
/ 

--changeSet OPER-2096_3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- 
-- Change column to mandatory
-- -- 
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table org_hr_shift_plan modify (
   "LABOUR_SKILL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet OPER-2096_3:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table org_hr_shift_plan modify (
   "LABOUR_SKILL_CD" Varchar2 (8) NOT NULL DEFERRABLE
)
');
END;
/