--liquibase formatted sql


--changeSet isApplicableToBoth:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isApplicableToBoth
* Arguments:     aMaintPrgmDbId_A
*                aMaintPrgmId_A
*                aTaskDbId_A
*                aTaskId_A
*                aMaintPrgmDbId_B
*                aMaintPrgmId_B
*                aTaskDbId_B
*                aTaskId_B
* Description:   This function determines if the two requirement revisions 
*                belonging to the given maintenance program revisions are
*                applicable to at least one of the operator's aircraft.
*
* Orig.Coder:    Y. Sotozaki
* Recent Coder:  
* Recent Date:   2008-02-13
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isApplicableToBoth
(
   aMaintPrgmDbId_A  NUMBER,
   aMaintPrgmId_A    NUMBER,
   aTaskDbId_A       NUMBER,
   aTaskId_A         NUMBER,
   aMaintPrgmDbId_B  NUMBER,
   aMaintPrgmId_B    NUMBER,
   aTaskDbId_B       NUMBER,
   aTaskId_B         NUMBER
)  RETURN NUMBER
IS
   ln_Applicable NUMBER;

BEGIN
   ln_Applicable := 0;

   SELECT
      COUNT(*)
   INTO
      ln_Applicable
   FROM
      DUAL
   WHERE
      EXISTS ( SELECT 1
               FROM
                  maint_prgm_carrier_map,
                  inv_inv,
                  inv_ac_reg,
                  eqp_bom_part,
                  task_task
               WHERE
                  maint_prgm_carrier_map.maint_prgm_db_id   = aMaintPrgmDbId_A   AND
                  maint_prgm_carrier_map.maint_prgm_id      = aMaintPrgmId_A
                  AND
                  inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id  AND
                  inv_ac_reg.inv_no_id    = inv_inv.inv_no_id
                  AND
                  inv_inv.carrier_db_id   = maint_prgm_carrier_map.carrier_db_id AND
                  inv_inv.carrier_id      = maint_prgm_carrier_map.carrier_id
                  AND
         	  inv_inv.rstat_cd	= 0
                  AND
                  eqp_bom_part.bom_part_db_id   (+)= inv_inv.bom_part_db_id   AND
                  eqp_bom_part.bom_part_id      (+)= inv_inv.bom_part_id
                  AND
                  task_task.task_db_id = aTaskDbId_A  AND
                  task_task.task_id    = aTaskId_A
                  AND
                  isApplicable(eqp_bom_part.appl_eff_ldesc, inv_inv.appl_eff_cd) = 1
                  AND
                  isApplicable(task_task.task_appl_eff_ldesc, inv_inv.appl_eff_cd) = 1
                  AND
                  getTaskApplicability(inv_inv.inv_no_db_id, inv_inv.inv_no_id, task_task.task_appl_sql_ldesc) = 1
      )
      AND
      EXISTS ( SELECT 1
               FROM
                  maint_prgm_carrier_map,
                  inv_inv,
                  inv_ac_reg,
                  eqp_bom_part,
                  task_task
               WHERE
                  maint_prgm_carrier_map.maint_prgm_db_id   = aMaintPrgmDbId_B   AND
                  maint_prgm_carrier_map.maint_prgm_id      = aMaintPrgmId_B
                  AND
                  inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id  AND
                  inv_ac_reg.inv_no_id    = inv_inv.inv_no_id
                  AND
                  inv_inv.carrier_db_id   = maint_prgm_carrier_map.carrier_db_id AND
                  inv_inv.carrier_id      = maint_prgm_carrier_map.carrier_id
                  AND
         	  inv_inv.rstat_cd	= 0
                  AND
                  eqp_bom_part.bom_part_db_id   (+)= inv_inv.bom_part_db_id   AND
                  eqp_bom_part.bom_part_id      (+)= inv_inv.bom_part_id
                  AND
                  task_task.task_db_id = aTaskDbId_B  AND
                  task_task.task_id    = aTaskId_B
                  AND
                  isApplicable(eqp_bom_part.appl_eff_ldesc, inv_inv.appl_eff_cd) = 1
                  AND
                  isApplicable(task_task.task_appl_eff_ldesc, inv_inv.appl_eff_cd) = 1
                  AND
                  getTaskApplicability(inv_inv.inv_no_db_id, inv_inv.inv_no_id, task_task.task_appl_sql_ldesc) = 1
      );

   -- return the applicability
   RETURN ln_Applicable;

END isApplicableToBoth;
/