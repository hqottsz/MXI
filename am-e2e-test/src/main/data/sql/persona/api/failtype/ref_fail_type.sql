/**************************************************
******** INSERT SCRIPT FOR TABLE REF_FAIL_TYPE *******
***************************************************/
-- Create a ref_fail_type record.
INSERT
   INTO
      ref_fail_type
         (
            fail_type_db_id,
            fail_type_cd,
            desc_sdesc,
            desc_ldesc,
            rstat_cd
        )
   SELECT
      4650,
      'C',
      'Broken, Burst, Ruptured, Torn, Open Circuit, Sheared',
      'Broken, Burst, Ruptured, Torn, Open Circuit, Sheared',
      0
   FROM
      dual;