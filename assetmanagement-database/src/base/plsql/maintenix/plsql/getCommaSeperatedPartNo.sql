--liquibase formatted sql


--changeSet getCommaSeperatedPartNo:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getCommaSeperatedPartNo
* Arguments:     aTaskDbId, aTaskId - pk for the task
*
* Description:   This function will return a list of Part Numbers assigned to the
*                given task definition
*
* Orig.Coder:    
* Recent Coder:  edo
* Recent Date:   2012.06.21
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getCommaSeperatedPartNo
(
  aTaskDbId task_task.task_db_id%TYPE,
  aTaskId   task_task.task_id%TYPE
) RETURN VARCHAR2
IS
  lPartNumbers VARCHAR2(10000);
  lNumOfParts NUMBER;

BEGIN

  lPartNumbers := '';
  lNumOfParts := 1;

  FOR rec IN
  (
    SELECT
        eqp_part_no.part_no_oem AS part_number
    FROM
        eqp_part_no
        INNER JOIN task_part_map ON
              task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
              task_part_map.part_no_id    = eqp_part_no.part_no_id
    WHERE
         task_part_map.task_db_id = aTaskDbId AND
         task_part_map.task_id    = aTaskId
         AND
         eqp_part_no.rstat_cd = 0
    ORDER BY
         part_number
  )

  LOOP
     lPartNumbers := lPartNumbers || rec.part_number || ', ';

     lNumOfParts := lNumOfParts + 1;

     -- only show 20 part numbers
     EXIT WHEN lNumOfParts > 20;
  END LOOP;


  IF lNumOfParts > 20 THEN
     -- if there are more than 20 part numbers, then show an ellipsis
     lPartNumbers := lPartNumbers || '...';
  ELSE
     -- if there are less than 20 part numbers, then remove the comma and space
     lPartNumbers :=  substr( lPartNumbers, 0, length( lPartNumbers ) - 2 );
  END IF;

  RETURN lPartNumbers;

END getCommaSeperatedPartNo;
/