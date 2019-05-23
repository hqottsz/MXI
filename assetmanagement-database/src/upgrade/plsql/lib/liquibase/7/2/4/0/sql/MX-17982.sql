--liquibase formatted sql


--changeSet MX-17982:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet MX-17982:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     isTaskApplicableForMaintPrgm
*
* Arguments:    aTaskDbId, aTaskId 	- pk for the task definition revision
*		            aMaintPrgmDbId, aMaintPrgmId - pk for the maintenance program	
*
* Description:  This function determines if a task definition revision 
*		            is applicable for the maintenance program
*
* Orig.Coder:    jbajer
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isTaskApplicableForMaintPrgm
(
    aTaskDbId      task_task.task_db_id%TYPE,
    aTaskId        task_task.task_id%TYPE,
    aMaintPrgmDbId maint_prgm.maint_prgm_db_id%TYPE,
    aMaintPrgmId   maint_prgm.maint_prgm_id%TYPE
    
)  RETURN NUMBER
IS

  CURSOR lcur_Info IS
      SELECT
         CASE WHEN inv_inv.inv_class_cd IN ('ACFT', 'ASSY') 
              THEN inv_inv.appl_eff_cd          
              ELSE assm_inv_inv.appl_eff_cd
         END appl_eff_cd,
         task_task.task_appl_sql_ldesc,
         task_task.task_appl_eff_ldesc,
         eqp_bom_part.appl_eff_ldesc,
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id         
      FROM
         inv_inv,
         inv_inv assm_inv_inv,
         maint_prgm,
         maint_prgm_defn,
         maint_prgm_carrier_map,
         task_task,
         eqp_bom_part
      WHERE
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         maint_prgm.maint_prgm_db_id = aMaintPrgmDbId AND
         maint_prgm.maint_prgm_id    = aMaintPrgmId
         AND
         maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
         maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
         AND
         assm_inv_inv.carrier_db_id  = maint_prgm_carrier_map.carrier_db_id AND
         assm_inv_inv.carrier_id     = maint_prgm_carrier_map.carrier_id
         AND
         maint_prgm_defn.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
         maint_prgm_defn.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
         AND
         assm_inv_inv.orig_assmbl_db_id   = maint_prgm_defn.assmbl_db_id AND
         assm_inv_inv.orig_assmbl_cd      = maint_prgm_defn.assmbl_cd
         AND
         inv_inv.assmbl_inv_no_db_id = assm_inv_inv.inv_no_db_id AND
         inv_inv.assmbl_inv_no_id    = assm_inv_inv.inv_no_id
         AND
         (
          ( task_task.assmbl_bom_id = 0 
            AND
            inv_inv.orig_assmbl_db_id = task_task.assmbl_db_id AND
            inv_inv.orig_assmbl_cd    = task_task.assmbl_cd
            AND
            inv_inv.assmbl_inv_no_db_id = assm_inv_inv.inv_no_db_id AND
            inv_inv.assmbl_inv_no_id    = assm_inv_inv.inv_no_id            
           )
           OR
          ( task_task.assmbl_bom_id <> 0 
            AND
            inv_inv.assmbl_db_id  = task_task.assmbl_db_id AND
            inv_inv.assmbl_cd     = task_task.assmbl_cd    AND
            inv_inv.assmbl_bom_id = task_task.assmbl_bom_id  
            AND
            inv_inv.assmbl_inv_no_db_id = assm_inv_inv.inv_no_db_id AND
            inv_inv.assmbl_inv_no_id    = assm_inv_inv.inv_no_id            
           )
         )
         AND
         eqp_bom_part.assmbl_db_id   (+)= inv_inv.assmbl_db_id   AND
         eqp_bom_part.assmbl_cd      (+)= inv_inv.assmbl_cd      AND
         eqp_bom_part.assmbl_bom_id  (+)= inv_inv.assmbl_bom_id  AND
         eqp_bom_part.bom_part_db_id (+)= inv_inv.bom_part_db_id AND
         eqp_bom_part.bom_part_id    (+)= inv_inv.bom_part_id;
   lrec_Info lcur_Info%ROWTYPE;
   
BEGIN   
   FOR lrec_Info IN lcur_Info LOOP
     IF isApplicable(lrec_Info.appl_eff_ldesc, lrec_Info.appl_eff_cd) = 1 AND
        isApplicable(lrec_Info.task_appl_eff_ldesc , lrec_Info.appl_eff_cd) = 1 AND
        getTaskApplicability(lrec_Info.inv_no_db_id, lrec_Info.inv_no_id, lrec_Info.task_appl_sql_ldesc) = 1  THEN
        RETURN 1;
     END IF; 
   END LOOP;

   RETURN 0;

EXCEPTION
   WHEN OTHERS THEN
   RETURN 0;

END isTaskApplicableForMaintPrgm;
/