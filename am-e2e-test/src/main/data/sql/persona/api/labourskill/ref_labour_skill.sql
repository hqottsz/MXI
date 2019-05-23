/********************************************************
******** INSERT SCRIPT FOR TABLE REF_LABOUR_SKILL *******
********************************************************/
-- Create a ref_labour_skill record.
INSERT
   INTO
      ref_labour_skill
         (
            labour_skill_db_id,
            labour_skill_cd,
            desc_sdesc,
            desc_ldesc,
            est_hourly_cost,
            esig_req_bool,
            rstat_cd
        )
   SELECT
      4650,
      'PILOT',
      'Pilot',
      'Pilot',
      100.00000,
      1,
      0
   FROM
      dual;