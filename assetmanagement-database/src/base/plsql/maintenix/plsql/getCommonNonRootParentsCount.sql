--liquibase formatted sql


--changeSet getCommonNonRootParentsCount:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getCommonNonRootParentsCount
(
   aTaskDbId         task_task.task_db_id%TYPE,
   aTaskId           task_task.task_id%TYPE,
   aOtherTaskDbId    task_task.task_db_id%TYPE,
   aOtherTaskId      task_task.task_id%TYPE

) RETURN VARCHAR
IS
   lCommonNonRootParentsCount NUMBER;

BEGIN

   SELECT
      count(*) INTO lCommonNonRootParentsCount
   FROM
     task_task
     INNER JOIN task_task task_task_link ON task_task_link.org_db_id = task_task.org_db_id AND
					    task_task_link.org_id    = task_task.org_id
   WHERE
      task_task.task_db_id = aTaskDbId AND
      task_task.task_id    = aTaskId
      AND
      task_task_link.task_db_id = aOtherTaskDbId AND
      task_task_link.task_id    = aOtherTaskId;


   IF lCommonNonRootParentsCount = 0 THEN

   SELECT
      count(*) INTO lCommonNonRootParentsCount
   FROM
      task_task

      INNER JOIN org_suborg_cache ON  org_suborg_cache.sub_org_db_id = task_task.org_db_id AND
				      org_suborg_cache.sub_org_id    = task_task.org_id

      INNER JOIN org_org ON org_org.org_db_id =    org_suborg_cache.org_db_id AND
			    org_org.org_id    =    org_suborg_cache.org_id

      INNER JOIN org_suborg_cache org_suborg_cache_child ON org_suborg_cache_child.org_db_id  = org_suborg_cache.org_db_id AND
							    org_suborg_cache_child.org_id     = org_suborg_cache.org_id

      INNER JOIN task_task task_task_link ON task_task_link.org_db_id = org_suborg_cache_child.sub_org_db_id AND
					     task_task_link.org_id    = org_suborg_cache_child.sub_org_id

   WHERE
      task_task.task_db_id = aTaskDbId AND
      task_task.task_id    = aTaskId
      AND
      task_task_link.task_db_id = aOtherTaskDbId AND
      task_task_link.task_id    = aOtherTaskId
      AND
      org_org.nh_org_db_id  IS NOT NULL AND
      org_org.nh_org_id     IS NOT NULL AND
      org_org.org_type_cd  !=  'DEFAULT';

   END IF;


   IF lCommonNonRootParentsCount = 0 THEN

   SELECT
      count(*) INTO lCommonNonRootParentsCount
   FROM
      task_task

      INNER JOIN org_org ON org_org.org_db_id =    task_task.org_db_id AND
			    org_org.org_id    =    task_task.org_id

      INNER JOIN org_suborg_cache org_suborg_cache_child ON org_suborg_cache_child.org_db_id  = task_task.org_db_id AND
							    org_suborg_cache_child.org_id     = task_task.org_id

      INNER JOIN task_task task_task_link ON task_task_link.org_db_id = org_suborg_cache_child.sub_org_db_id AND
					     task_task_link.org_id    = org_suborg_cache_child.sub_org_id

   WHERE
      task_task.task_db_id = aTaskDbId AND
      task_task.task_id    = aTaskId
      AND
      task_task_link.task_db_id = aOtherTaskDbId AND
      task_task_link.task_id    = aOtherTaskId
      AND
      org_org.nh_org_db_id  IS NOT NULL AND
      org_org.nh_org_id     IS NOT NULL AND
      org_org.org_type_cd  !=  'DEFAULT';

   END IF;

   IF lCommonNonRootParentsCount = 0 THEN

   SELECT
      count(*) INTO lCommonNonRootParentsCount
   FROM
      task_task

      INNER JOIN org_suborg_cache ON  org_suborg_cache.sub_org_db_id = task_task.org_db_id AND
				      org_suborg_cache.sub_org_id    = task_task.org_id

      INNER JOIN org_org ON org_org.org_db_id =    org_suborg_cache.org_db_id AND
			    org_org.org_id    =    org_suborg_cache.org_id

      INNER JOIN task_task task_task_link ON task_task_link.org_db_id = org_suborg_cache.org_db_id AND
					     task_task_link.org_id    = org_suborg_cache.org_id

   WHERE
      task_task.task_db_id = aTaskDbId AND
      task_task.task_id    = aTaskId
      AND
      task_task_link.task_db_id = aOtherTaskDbId AND
      task_task_link.task_id    = aOtherTaskId
      AND
      org_org.nh_org_db_id  IS NOT NULL AND
      org_org.nh_org_id     IS NOT NULL AND
      org_org.org_type_cd  !=  'DEFAULT';

   END IF;

   RETURN lCommonNonRootParentsCount;

END getCommonNonRootParentsCount;
/