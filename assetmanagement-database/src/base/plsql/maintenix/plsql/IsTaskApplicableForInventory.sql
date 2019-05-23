--liquibase formatted sql


--changeSet IsTaskApplicableForInventory:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     isTaskApplicableForInventory
* Arguments:    aInvNoDbId, aInvNoId 	- pk for the inventory item
*               aTaskDbId, aTaskId 	- pk for the task definition revision
* Description:  This function determines if a task definition revision 
		is applicable to an inventory
*
* Orig.Coder:    Cdaley
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isTaskApplicableForInventory
(
    aInvNoDbId inv_inv.inv_no_db_id%TYPE,
    aInvNoId inv_inv.inv_no_db_id%TYPE,
    aTaskDbId task_task.task_db_id%TYPE,
    aTaskId task_task.task_id%TYPE
)  RETURN NUMBER
IS
   TYPE query_cursor IS REF CURSOR;
   lTaskApplicable number;
   lBomPartApplEffLDesc eqp_bom_part.appl_eff_ldesc%TYPE;
   lInvApplEffCd inv_inv.appl_eff_cd%TYPE;
   lTaskApplEffLDesc task_task.task_appl_eff_ldesc%TYPE;
   lTaskApplSqlLDesc task_task.task_appl_sql_ldesc%TYPE;
   lCursor query_cursor;
   lCursorSql varchar2(4000);
BEGIN
   lTaskApplicable := 0;

   --open a cursor to determine the applicability
   -- if changes are made to this query, then they might need to also be made to isTaskApplicableForMaintPrgm
   OPEN lCursor FOR
   SELECT
       CASE WHEN isApplicable(eqp_bom_part.appl_eff_ldesc, task_ass_inv.appl_eff_cd) = 1 AND
                 isApplicable(task_task.task_appl_eff_ldesc , task_ass_inv.appl_eff_cd) = 1 AND
                 getTaskApplicability(aInvNoDbId, aInvNoId, task_task.task_appl_sql_ldesc) = 1
            THEN 1
            ELSE 0
            END as applicable
   FROM
       task_task,
       inv_inv task_inv_inv,
       inv_inv task_ass_inv,
       eqp_bom_part
   WHERE
       task_task.task_db_id = aTaskDbId AND
       task_task.task_id = aTaskId
       AND
       task_inv_inv.inv_no_db_id = aInvNoDbId AND
       task_inv_inv.inv_no_id = aInvNoId
       AND
       task_inv_inv.rstat_cd	= 0
       AND
       eqp_bom_part.assmbl_db_id (+)= task_inv_inv.assmbl_db_id AND
       eqp_bom_part.assmbl_cd (+)= task_inv_inv.assmbl_cd AND
       eqp_bom_part.assmbl_bom_id (+)= task_inv_inv.assmbl_bom_id AND
       eqp_bom_part.bom_part_db_id (+)= task_inv_inv.bom_part_db_id AND
       eqp_bom_part.bom_part_id (+)= task_inv_inv.bom_part_id
       AND
       --if not acft or assy, then ensure that the assembly inventory is used
       task_ass_inv.inv_no_db_id = CASE
            WHEN task_inv_inv.inv_class_cd  IN ('ACFT', 'ASSY') THEN
      		       task_inv_inv.inv_no_db_id
      	    ELSE
      		       nvl( task_inv_inv.assmbl_inv_no_db_id, task_inv_inv.h_inv_no_db_id )
      	    END
       AND
       task_ass_inv.inv_no_id = CASE
            WHEN task_inv_inv.inv_class_cd  IN ('ACFT', 'ASSY') THEN
      		       task_inv_inv.inv_no_id
      	    ELSE
      		       nvl( task_inv_inv.assmbl_inv_no_id, task_inv_inv.h_inv_no_id )
      	    END;

   FETCH lCursor INTO lTaskApplicable;
   CLOSE lCursor;
   RETURN lTaskApplicable;

EXCEPTION
   WHEN OTHERS THEN
      lTaskApplicable := 0;
      IF lCursor%ISOPEN THEN CLOSE lCursor; END IF;
   RETURN lTaskApplicable;

END isTaskApplicableForInventory;
/