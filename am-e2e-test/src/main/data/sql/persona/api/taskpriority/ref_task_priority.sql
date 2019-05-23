/**************************************************
******** INSERT SCRIPT FOR TABLE REF_TASK_PRIORITY *******
***************************************************/
-- Create a ref_task_priority record.
INSERT
   INTO
      ref_task_priority
         (
            task_priority_db_id,
            task_priority_cd,
            task_priority_ord,
            bitmap_db_id,
            bitmap_tag,
            desc_sdesc,
            desc_ldesc,
            rstat_cd
        )
   SELECT
      11,
      'LOW',
      2,
      0,
      88,
      'Low priority',
      'Low priority',
      0
   FROM
      dual;