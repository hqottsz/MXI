--liquibase formatted sql


--changeSet MX-22247:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isDuplicateTaskPartReqInfo
*
* Description:   Returns 1 if two tasks have the same part requirement information.
*                Returns 0 if the part requirement information is different.
*
* Arguments:     an_FirstTaskSchedDbId, an_FirstTaskSchedId - pk for the first task
*                an_SecondTaskSchedDbId, an_SecondTaskSchedId - pk for the second task
*
* Orig.Coder:    ntaitt
* Recent Coder:  ntaitt
* Recent Date:   April 20, 2011
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isDuplicateTaskPartReqInfo
(
   an_FirstTaskSchedDbId  IN sched_stask.sched_db_id%TYPE,
   an_FirstTaskSchedId    IN sched_stask.sched_db_id%TYPE,
   an_SecondTaskSchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SecondTaskSchedId   IN sched_stask.sched_db_id%TYPE
)  RETURN NUMBER
IS
   ln_IsDuplicate NUMBER;
   ls_FirstTaskRequirements VARCHAR2(32767);
   ls_SecondTaskRequirements VARCHAR2(32767);
   ls_FirstTaskResults VARCHAR2(32767);
   ls_SecondTaskResults VARCHAR2(32767);
   ls_Delimiter VARCHAR2(1) := ',';
   
   CURSOR lcur_FirstTaskRequirements
   IS
   SELECT
      sched_part.nh_assmbl_db_id  || ':' ||
      sched_part.nh_assmbl_cd     || ':' ||
      sched_part.nh_assmbl_bom_id || ':' ||
      sched_part.nh_assmbl_pos_id || ':' ||
      sched_part.assmbl_db_id     || ':' ||
      sched_part.assmbl_cd        || ':' ||
      sched_part.assmbl_bom_id    || ':' ||
      sched_part.assmbl_pos_id AS part_req_info
   FROM
      sched_part
   WHERE
      sched_part.sched_db_id = an_FirstTaskSchedDbId AND
      sched_part.sched_id    = an_FirstTaskSchedId
      AND
      sched_part.rstat_cd = 0
   ORDER BY
      sched_part.nh_assmbl_db_id,
      sched_part.nh_assmbl_cd,
      sched_part.nh_assmbl_bom_id,
      sched_part.nh_assmbl_pos_id,
      sched_part.assmbl_db_id,
      sched_part.assmbl_cd,
      sched_part.assmbl_bom_id,
      sched_part.assmbl_pos_id;


   CURSOR lcur_SecondTaskRequirements
   IS
   SELECT
      sched_part.nh_assmbl_db_id  || ':' ||
      sched_part.nh_assmbl_cd     || ':' ||
      sched_part.nh_assmbl_bom_id || ':' ||
      sched_part.nh_assmbl_pos_id || ':' ||
      sched_part.assmbl_db_id     || ':' ||
      sched_part.assmbl_cd        || ':' ||
      sched_part.assmbl_bom_id    || ':' ||
      sched_part.assmbl_pos_id AS part_req_info
   FROM
      sched_part
   WHERE
      sched_part.sched_db_id = an_SecondTaskSchedDbId AND
      sched_part.sched_id    = an_SecondTaskSchedId
      AND
      sched_part.rstat_cd = 0
   ORDER BY
      sched_part.nh_assmbl_db_id,
      sched_part.nh_assmbl_cd,
      sched_part.nh_assmbl_bom_id,
      sched_part.nh_assmbl_pos_id,
      sched_part.assmbl_db_id,
      sched_part.assmbl_cd,
      sched_part.assmbl_bom_id,
      sched_part.assmbl_pos_id; 
   
BEGIN
   IF an_FirstTaskSchedDbId IS NULL OR an_FirstTaskSchedId IS NULL
        OR an_SecondTaskSchedDbId IS NULL OR an_SecondTaskSchedId IS NULL
   THEN
     RETURN 0;
   END IF;

   /* Concatenate the part info for the first task */   
   OPEN lcur_FirstTaskRequirements;
   
   LOOP
      FETCH lcur_FirstTaskRequirements INTO ls_FirstTaskRequirements;
      EXIT WHEN lcur_FirstTaskRequirements%NOTFOUND;
      /* if this is the first value, do not add a delimiter */
      IF ls_FirstTaskResults IS NOT NULL THEN
         ls_FirstTaskResults := ls_FirstTaskResults || ls_Delimiter;
      END IF;    
      
      /* Add the value to the result string */
      ls_FirstTaskResults := ls_FirstTaskResults || ls_FirstTaskRequirements;      
   END LOOP;
   
   CLOSE lcur_FirstTaskRequirements;
   
   /* Concatenate the part info for the second task */
   OPEN lcur_SecondTaskRequirements;
   
   LOOP
      FETCH lcur_SecondTaskRequirements INTO ls_SecondTaskRequirements;
      EXIT WHEN lcur_SecondTaskRequirements%NOTFOUND;
      /* if this is the first value, do not add a delimiter */
      IF ls_SecondTaskResults IS NOT NULL THEN
         ls_SecondTaskResults := ls_SecondTaskResults || ls_Delimiter;
      END IF;
      
      /* Add the value to the result string */
      ls_SecondTaskResults := ls_SecondTaskResults || ls_SecondTaskRequirements;      
   END LOOP;
   
   CLOSE lcur_SecondTaskRequirements;
      
   IF (ls_FirstTaskResults IS NULL AND ls_SecondTaskResults IS NULL) OR
      (ls_FirstTaskResults = ls_SecondTaskResults) 
   THEN
      ln_IsDuplicate := 1;
   ELSE
      ln_IsDuplicate := 0;
   END IF;
      
   
   RETURN ln_IsDuplicate;

END isDuplicateTaskPartReqInfo;
/