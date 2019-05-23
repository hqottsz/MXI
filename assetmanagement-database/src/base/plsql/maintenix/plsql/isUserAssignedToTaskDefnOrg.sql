--liquibase formatted sql


--changeSet isUserAssignedToTaskDefnOrg:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION isUserAssignToTaskDefnOrg
(
   aHrDbId      org_hr.hr_db_id%TYPE,
   aHrId        org_hr.hr_id%TYPE,
   aTaskDbId    task_task.task_db_id%TYPE,
   aTaskId      task_task.task_id%TYPE
)  RETURN NUMBER
IS

  lUserAssignedToTaskDefnOrg NUMBER;
BEGIN

   WITH
   user_org_tree AS
   (
      SELECT
         org_org.org_db_id,
         org_org.org_id
      FROM
         org_org
      WHERE
         -- Ignore the default organizations.
         org_org.org_type_cd != 'DEFAULT'
      START WITH
         -- Start with all organizations to which the user belongs.
         (org_org.org_db_id, org_org.org_id) IN
         (
            SELECT
               org_db_id,
               org_id
            FROM
               org_org_hr
            WHERE
               org_org_hr.hr_db_id = aHrDbId AND
               org_org_hr.hr_id    = aHrId
         )
      -- Get all sub-organizations of those organizations.
      CONNECT BY
         PRIOR org_org.org_db_id = org_org.nh_org_db_id AND
         PRIOR org_org.org_id    = org_org.nh_org_id
   )
   --
   SELECT
      1
   INTO
      lUserAssignedToTaskDefnOrg
   FROM
      DUAL
   WHERE
      EXISTS
      (
         SELECT
            1
         FROM
            task_task
            INNER JOIN user_org_tree ON
               user_org_tree.org_db_id = task_task.org_db_id AND
               user_org_tree.org_id    = task_task.org_id
         WHERE
            task_task.task_db_id = aTaskDbId AND
            task_task.task_id    = aTaskId
      )
   ;

  RETURN lUserAssignedToTaskDefnOrg;

END isUserAssignToTaskDefnOrg;
/