--liquibase formatted sql


--changeSet OPER-836:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     isTaskApplicableForMaintPrgm
*
* Arguments:    aTaskDbId, aTaskId  - pk for the task definition revision
*               aMaintPrgmDbId, aMaintPrgmId - pk for the maintenance program 
*
* Description:  This function determines if a task definition revision 
*               is applicable for the maintenance program
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
         task_task
         INNER JOIN maint_prgm ON 
            maint_prgm.maint_prgm_db_id = aMaintPrgmDbId AND
            maint_prgm.maint_prgm_id    = aMaintPrgmId
         INNER JOIN maint_prgm_carrier_map ON
            maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
            maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
         INNER JOIN maint_prgm_defn ON
            maint_prgm_defn.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
            maint_prgm_defn.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
         INNER JOIN inv_inv assm_inv_inv ON
            assm_inv_inv.carrier_db_id     = maint_prgm_carrier_map.carrier_db_id AND
            assm_inv_inv.carrier_id        = maint_prgm_carrier_map.carrier_id
            AND
            assm_inv_inv.orig_assmbl_db_id = maint_prgm_defn.assmbl_db_id AND
            assm_inv_inv.orig_assmbl_cd    = maint_prgm_defn.assmbl_cd
         INNER JOIN inv_inv ON
            inv_inv.assmbl_inv_no_db_id = assm_inv_inv.inv_no_db_id AND
            inv_inv.assmbl_inv_no_id    = assm_inv_inv.inv_no_id
         LEFT OUTER JOIN eqp_bom_part ON
            eqp_bom_part.bom_part_db_id = inv_inv.bom_part_db_id AND
            eqp_bom_part.bom_part_id    = inv_inv.bom_part_id    AND
            eqp_bom_part.assmbl_db_id   = inv_inv.assmbl_db_id   AND
            eqp_bom_part.assmbl_cd      = inv_inv.assmbl_cd      AND
            eqp_bom_part.assmbl_bom_id  = inv_inv.assmbl_bom_id  
      WHERE
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         (
            (
               task_task.assmbl_bom_id = 0
               AND
               inv_inv.orig_assmbl_db_id = task_task.assmbl_db_id AND
               inv_inv.orig_assmbl_cd    = task_task.assmbl_cd
            )
            OR
            (
               task_task.assmbl_bom_id <> 0
               AND
               inv_inv.assmbl_db_id  = task_task.assmbl_db_id AND
               inv_inv.assmbl_cd     = task_task.assmbl_cd    AND
               inv_inv.assmbl_bom_id = task_task.assmbl_bom_id
            )
         );
   
   lrec_Info       lcur_Info%ROWTYPE;
   ln_EarlyReturn  NUMBER;

BEGIN

   /* The values in task_task.task_appl_sql_ldesc, task_task.task_appl_eff_ldesc and eqp_bom_part.appl_eff_ldesc
      are primarily null. Working with this data characteristic, we are able to optimize the performance of this
      by eliminating the need to loop and evaluate the data returned in lrec_Info if all three values are null.
   */   
   
   ln_EarlyReturn := 0;
   BEGIN
      SELECT
         1 INTO ln_EarlyReturn
      FROM
         dual
      WHERE
         EXISTS
         (
            SELECT
               NULL
            FROM
               task_task
               INNER JOIN maint_prgm ON 
                  maint_prgm.maint_prgm_db_id = aMaintPrgmDbId AND
                  maint_prgm.maint_prgm_id    = aMaintPrgmId
               INNER JOIN maint_prgm_carrier_map ON
                  maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                  maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
               INNER JOIN maint_prgm_defn ON
                  maint_prgm_defn.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
                  maint_prgm_defn.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
               INNER JOIN inv_inv assm_inv_inv ON
                  assm_inv_inv.carrier_db_id     = maint_prgm_carrier_map.carrier_db_id AND
                  assm_inv_inv.carrier_id        = maint_prgm_carrier_map.carrier_id
                  AND
                  assm_inv_inv.orig_assmbl_db_id = maint_prgm_defn.assmbl_db_id AND
                  assm_inv_inv.orig_assmbl_cd    = maint_prgm_defn.assmbl_cd
               INNER JOIN inv_inv ON
                  inv_inv.assmbl_inv_no_db_id = assm_inv_inv.inv_no_db_id AND
                  inv_inv.assmbl_inv_no_id    = assm_inv_inv.inv_no_id
               LEFT OUTER JOIN eqp_bom_part ON
                  eqp_bom_part.bom_part_db_id = inv_inv.bom_part_db_id AND
                  eqp_bom_part.bom_part_id    = inv_inv.bom_part_id    AND
                  eqp_bom_part.assmbl_db_id   = inv_inv.assmbl_db_id   AND
                  eqp_bom_part.assmbl_cd      = inv_inv.assmbl_cd      AND
                  eqp_bom_part.assmbl_bom_id  = inv_inv.assmbl_bom_id  
            WHERE
               task_task.task_db_id = aTaskDbId AND
               task_task.task_id    = aTaskId
               AND   
               task_task.task_appl_eff_ldesc IS NULL
               AND
               task_task.task_appl_sql_ldesc IS NULL
               AND
               (
                  (
                     task_task.assmbl_bom_id = 0
                     AND
                     inv_inv.orig_assmbl_db_id = task_task.assmbl_db_id AND
                     inv_inv.orig_assmbl_cd    = task_task.assmbl_cd
                  )
                  OR
                  (
                     task_task.assmbl_bom_id <> 0
                     AND
                     inv_inv.assmbl_db_id  = task_task.assmbl_db_id AND
                     inv_inv.assmbl_cd     = task_task.assmbl_cd    AND
                     inv_inv.assmbl_bom_id = task_task.assmbl_bom_id
                  )
               )
               AND
               eqp_bom_part.appl_eff_ldesc IS NULL 
             );

      EXCEPTION
      WHEN OTHERS THEN
         NULL;
      END;

   IF (ln_EarlyReturn) <> 0  THEN
      RETURN 1;
   END IF;

   
   OPEN lcur_Info;
   LOOP
     FETCH lcur_Info into lrec_Info;
     EXIT WHEN lcur_Info%notfound;
     IF
        isApplicable(lrec_Info.appl_eff_ldesc, lrec_Info.appl_eff_cd) = 1 AND 
        isApplicable(lrec_Info.task_appl_eff_ldesc , lrec_Info.appl_eff_cd) = 1 AND
        getTaskApplicability(lrec_Info.inv_no_db_id, lrec_Info.inv_no_id, lrec_Info.task_appl_sql_ldesc) = 1
     THEN
           RETURN 1;
     END IF;
   END LOOP;
   CLOSE lcur_Info;
   RETURN 0;

EXCEPTION
   WHEN OTHERS THEN
   RETURN 0;

END isTaskApplicableForMaintPrgm;
/

--changeSet OPER-836:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isApplicable
* Arguments:     as_range - applicability range
*                as_code  - applicability code
* Description:   This function determines if the applicability code is present
*                within the applicability range
*
* Orig.Coder:    Sylvain Charette
* Recent Coder:  edo
* Recent Date:   July 2, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isApplicable
(
    as_range STRING,
    as_code  STRING
)  RETURN NUMBER 
IS
   ln_Applicable NUMBER;
   ln_Comma      NUMBER;
   ln_Hyphen     NUMBER;
   ls_Token      VARCHAR2(4000);
   ls_String     VARCHAR2(4000);
   ls_Min        VARCHAR2(4000);
   ls_Max        VARCHAR2(4000);
BEGIN
   -- initialize variables
   ln_Applicable := 0;
   ln_Comma  := 1;
   ls_String := as_range;
   
   
   -- if the range is N/A then consider it is not applicable
   IF ( as_range = 'N/A') THEN
      RETURN 0;
   END IF;
   
   -- if the range or code is null, then consider it applicable
   IF ( as_range IS NULL ) OR ( as_range = '' ) OR ( as_code IS NULL ) OR ( as_code = '' ) THEN
      RETURN 1;
   END IF;
      
   WHILE ln_Comma > 0 LOOP
      -- find the index of the first comma
      ln_Comma := instr( ls_String, ',' ); 

      IF ln_Comma > 0 THEN
         -- separate the first token from the remaining tokens
         ls_Token  := substr( ls_String, 1, ln_Comma - 1 );
         ls_String := substr( ls_String, ln_Comma + 1 );
      ELSE
         -- entire string is a single token
         ls_Token := ls_String;
      END IF;
      
      -- find the index of the hyphen in the first token
      ln_Hyphen := instr( ls_Token, '-' ); 
      
      IF ln_Hyphen > 0 THEN
         -- separate the minimum value from the maximum value
         ls_Min := substr( ls_Token, 1, ln_Hyphen - 1 );
         ls_Max := substr( ls_Token, ln_Hyphen + 1 );
         
         -- if the code is between these two values, the code is applicable
         IF ( length( as_code ) = length( ls_Min ) ) AND ( as_code >= ls_Min ) AND ( as_code <= ls_Max ) THEN
            ln_Applicable := 1;
         END IF;
      ELSE
         -- if the code matches the entire token, the code is applicable
         IF ( as_code = ls_Token ) THEN
            ln_Applicable := 1;
         END IF;
      END IF;
      
      -- if the code is found to be applicable, exit the loop
      EXIT WHEN ln_Applicable = 1;
   END LOOP;
   
   -- return the applicability
   RETURN ln_Applicable;
END isApplicable;
/