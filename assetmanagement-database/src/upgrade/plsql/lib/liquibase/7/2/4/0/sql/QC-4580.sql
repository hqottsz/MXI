--liquibase formatted sql


--changeSet QC-4580:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:       GetInvsMissingDatesForTaskDefn
* Arguments:      an_TaskDbId - the task defn db id.
*                 an_TaskId   - the task defn id.
*                 an_HrDbId   - the hr db id.
*                 an_HrId     - the hr id.
*                 an_MaxRecords the number of records that should be returned.   
*
* Return:         ltot_tab_inv_tree - a table of mxkeytable type with total number of inv records.
*
* Description:   .This function checks inventories for presence of dates as per the task defns.
*                 If the task defn is scheduled from manufactured then inventory(including it's sub-inventories) 
*                 on which the it can be initialized should have manufactured date. Same is true for received date.
*                 Due to performance issues all the inventory records are not returned. Only 
*                 (an_MaxRecords + 101) are returned.
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION GetInvsMissingDatesForTaskDefn
(
   an_TaskDbId IN task_task.task_db_id%TYPE,
   an_TaskId   IN task_task.task_id%TYPE,
   an_HrDbId IN org_hr.hr_db_id%TYPE,
   an_HrId   IN org_hr.hr_id%TYPE,
   an_MaxRecords IN NUMBER
) RETURN mxkeytable

IS
   
   ln_AssmblBomId     NUMBER;
   ls_BomClassCd      eqp_assmbl_bom.bom_class_cd%TYPE;
   lb_ReceivedDtBool  NUMBER;
   
   lt_tab_inv_tree    mxkeytable;
   ltot_tab_inv_tree  mxkeytable;
   
   ln_RecCounter     NUMBER;
   ln_MaxRecCount    NUMBER;
   
   -- This cursor returns all the inventories on which task defn can 
   -- be initialized and they are missing the required date.
   CURSOR lcur_taskdefn_invs
   (
      cn_TaskDbId    IN task_task.task_db_id%TYPE,
      cn_TaskId      IN task_task.task_id%TYPE,
      cn_HrDbId      IN org_hr.hr_db_id%TYPE,
      cn_HrId        IN org_hr.hr_id%TYPE,
      cn_AssmblBomId IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      cs_BomClassCd  IN eqp_assmbl_bom.bom_class_cd%TYPE
   )
   IS
   
      SELECT
          task_inv_inv.inv_no_db_id,
          task_inv_inv.inv_no_id
      FROM
          inv_inv        task_inv_inv,
          inv_inv        task_ass_inv_inv,
          eqp_assmbl_pos task_eqp_assmbl_pos,
          eqp_bom_part   task_eqp_bom_part,
          inv_inv        h_inv_inv,
          task_task,
          org_hr
      
      WHERE
         cs_BomClassCd IS NOT NULL
         AND
         task_task.task_db_id = cn_TaskDbId  AND
         task_task.task_id    = cn_TaskId
         AND
         h_inv_inv.inv_no_db_id = task_inv_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id    = task_inv_inv.h_inv_no_id
         AND
         (
             -- If the task definition is assigned to a maintenance program, only show ACFT/ASSY
             -- that are assigned to the MP through their operators to the selected revision
             EXISTS
             ( SELECT 1
               FROM
                   maint_prgm_task,
                   maint_prgm_carrier_map
               WHERE
                   maint_prgm_task.task_db_id = task_task.task_db_id AND
                   maint_prgm_task.task_id    = task_task.task_id AND
                   maint_prgm_task.unassign_bool = 0
                   AND
                   maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
                   maint_prgm_carrier_map.maint_prgm_id    = maint_prgm_task.maint_prgm_id AND
                   maint_prgm_carrier_map.latest_revision_bool = 1
                   AND
                   h_inv_inv.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
                   h_inv_inv.carrier_id    = maint_prgm_carrier_map.carrier_id
             )
             OR
             -- Otherwise show all ACFT/ASSY
             NOT EXISTS
             ( SELECT 1
               FROM
                   maint_prgm_task,
                   maint_prgm_carrier_map
               WHERE
                   maint_prgm_task.task_db_id = task_task.task_db_id AND
                   maint_prgm_task.task_id    = task_task.task_id AND
                   maint_prgm_task.unassign_bool = 0
                   AND
                   maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
                   maint_prgm_carrier_map.maint_prgm_id    = maint_prgm_task.maint_prgm_id AND
                   maint_prgm_carrier_map.latest_revision_bool = 1
              )
         )
         AND
         task_task.task_def_status_cd IN ('BUILD','REVISION')
         AND
         -- make sure the user has authority over the aircraft
         org_hr.hr_db_id = cn_HrDbId AND
         org_hr.hr_id    = cn_HrId
         AND
         (
            org_hr.all_authority_bool = 1
            OR
            h_inv_inv.authority_db_id IS NULL
            OR
            EXISTS
            (
               SELECT
                  1
               FROM
                  org_hr_authority
               WHERE
                  org_hr_authority.hr_db_id = org_hr.hr_db_id AND
                  org_hr_authority.hr_id    = org_hr.hr_id
                  AND
                  org_hr_authority.authority_db_id = h_inv_inv.authority_db_id AND
                  org_hr_authority.authority_id    = h_inv_inv.authority_id
            )
         )
         -- task will be scheduled from BIRTH
         AND
         task_task.relative_bool = 0
         -- only BLOCK and REQ
         AND
         task_task.task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd IN ('BLOCK', 'REQ'))
         AND
         task_task.task_class_cd NOT IN ('REPL', 'CORR')
         -- should have scheduling rules
         AND EXISTS
         (
            SELECT
               1
            FROM
               task_sched_rule
            WHERE
               task_sched_rule.task_db_id = cn_TaskDbId   AND
               task_sched_rule.task_id    = cn_TaskId
         )
         -- conditions for Manufacture Date or Received Date
         AND
         (
          (
           task_task.sched_from_received_dt_bool = 1 AND
           task_inv_inv.received_dt IS NULL
          )
          OR
          (
           task_task.sched_from_received_dt_bool = 0 AND
           task_inv_inv.manufact_dt IS NULL
          )
         )
         -- inv should not be ARCHIVE or SCRAP
         AND
         task_inv_inv.inv_cond_cd NOT IN ('ARCHIVE', 'SCRAP')
         AND
         (
            cs_BomClassCd = 'TRK'
            AND
            task_eqp_bom_part.assmbl_db_id  = task_task.assmbl_db_id   AND
            task_eqp_bom_part.assmbl_cd     = task_task.assmbl_cd      AND
            task_eqp_bom_part.assmbl_bom_id = task_task.assmbl_bom_id
            AND
            task_inv_inv.bom_part_db_id = task_eqp_bom_part.bom_part_db_id  AND
            task_inv_inv.bom_part_id    = task_eqp_bom_part.bom_part_id
            AND
            task_ass_inv_inv.inv_no_db_id (+)= task_inv_inv.assmbl_inv_no_db_id  AND
            task_ass_inv_inv.inv_no_id    (+)= task_inv_inv.assmbl_inv_no_id
            AND
            task_eqp_assmbl_pos.assmbl_db_id  (+)= task_inv_inv.assmbl_db_id AND
            task_eqp_assmbl_pos.assmbl_cd     (+)= task_inv_inv.assmbl_cd    AND
            task_eqp_assmbl_pos.assmbl_bom_id (+)= task_inv_inv.assmbl_bom_id AND
            task_eqp_assmbl_pos.assmbl_pos_id (+)= task_inv_inv.assmbl_pos_id
      
         )
         AND
         task_inv_inv.rstat_cd = 0
         AND
         task_task.rstat_cd = 0
      
      UNION
      
      SELECT
          task_inv_inv.inv_no_db_id,
          task_inv_inv.inv_no_id
      FROM
          inv_inv        task_inv_inv,
          inv_inv        task_ass_inv_inv,
          eqp_assmbl_pos task_eqp_assmbl_pos,
          eqp_bom_part   task_eqp_bom_part,
          inv_inv        h_inv_inv,
          task_task,
          org_hr
      
      WHERE
          cs_BomClassCd IS NOT NULL
          AND
          task_task.task_db_id = cn_TaskDbId  AND
          task_task.task_id    = cn_TaskId
          AND
          h_inv_inv.inv_no_db_id = task_inv_inv.h_inv_no_db_id AND
          h_inv_inv.inv_no_id    = task_inv_inv.h_inv_no_id
          AND
         (
           -- If the task definition is assigned to a maintenance program, only show ACFT/ASSY
           -- that are assigned to the MP through their operators to the selected revision
           EXISTS
           ( SELECT 1
             FROM
                 maint_prgm_task,
                 maint_prgm_carrier_map
             WHERE
                 maint_prgm_task.task_db_id = task_task.task_db_id AND
                 maint_prgm_task.task_id    = task_task.task_id AND
                 maint_prgm_task.unassign_bool = 0
                 AND
                 maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
                 maint_prgm_carrier_map.maint_prgm_id    = maint_prgm_task.maint_prgm_id AND
                 maint_prgm_carrier_map.latest_revision_bool = 1
                 AND
                 h_inv_inv.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
                 h_inv_inv.carrier_id    = maint_prgm_carrier_map.carrier_id
           )
           OR
           -- Otherwise show all ACFT/ASSY
           NOT EXISTS
           ( SELECT 1
             FROM
                 maint_prgm_task,
                 maint_prgm_carrier_map
             WHERE
                 maint_prgm_task.task_db_id = task_task.task_db_id AND
                 maint_prgm_task.task_id    = task_task.task_id AND
                 maint_prgm_task.unassign_bool = 0
                 AND
                 maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
                 maint_prgm_carrier_map.maint_prgm_id    = maint_prgm_task.maint_prgm_id AND
                 maint_prgm_carrier_map.latest_revision_bool = 1
            )
         )
         AND
         task_task.task_def_status_cd IN ('BUILD','REVISION')
         AND
         -- make sure the user has authority over the aircraft
         org_hr.hr_db_id = cn_HrDbId AND
         org_hr.hr_id    = cn_HrId
         AND
         (
          org_hr.all_authority_bool = 1
          OR
          h_inv_inv.authority_db_id IS NULL
          OR
          EXISTS
          (
             SELECT
                1
             FROM
                org_hr_authority
             WHERE
                org_hr_authority.hr_db_id = org_hr.hr_db_id AND
                org_hr_authority.hr_id    = org_hr.hr_id
                AND
                org_hr_authority.authority_db_id = h_inv_inv.authority_db_id AND
                org_hr_authority.authority_id    = h_inv_inv.authority_id
          )
         )
         -- task will be scheduled from BIRTH
         AND
         task_task.relative_bool = 0
         -- only BLOCK and REQ
         AND
         task_task.task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd IN ('BLOCK', 'REQ'))
         AND
         task_task.task_class_cd NOT IN ('REPL', 'CORR')
         -- should have scheduling rules
         AND EXISTS
         (
          SELECT
             1
          FROM
             task_sched_rule
          WHERE
             task_sched_rule.task_db_id = cn_TaskDbId   AND
             task_sched_rule.task_id    = cn_TaskId
         )
         -- conditions for Manufacture Date or Received Date
         AND
         (
            (
               task_task.sched_from_received_dt_bool = 1 AND
               task_inv_inv.received_dt IS NULL
            )
            OR
            (
               task_task.sched_from_received_dt_bool = 0 AND
               task_inv_inv.manufact_dt IS NULL
            )
         )
         -- inv should not be ARCHIVE or SCRAP
         AND
         task_inv_inv.inv_cond_cd NOT IN ('ARCHIVE', 'SCRAP')
         AND
         (
                
            cs_BomClassCd != 'TRK'
            AND
            (
                (
                    task_inv_inv.orig_assmbl_db_id  = task_task.assmbl_db_id  AND
                    task_inv_inv.orig_assmbl_cd     = task_task.assmbl_cd     AND
                    task_inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
                    AND
                    cn_AssmblBomId = 0
                )
                OR
                (
                    task_inv_inv.assmbl_db_id  = task_task.assmbl_db_id      AND
                    task_inv_inv.assmbl_cd     = task_task.assmbl_cd         AND
                    task_inv_inv.assmbl_bom_id = task_task.assmbl_bom_id   
                    AND
                    cn_AssmblBomId != 0
                )
            )
      
            AND
      
      
            task_eqp_bom_part.assmbl_db_id    (+)= task_inv_inv.assmbl_db_id     AND
            task_eqp_bom_part.assmbl_cd       (+)= task_inv_inv.assmbl_cd        AND
            task_eqp_bom_part.assmbl_bom_id   (+)= task_inv_inv.assmbl_bom_id    AND
            task_eqp_bom_part.bom_part_db_id  (+)= task_inv_inv.bom_part_db_id   AND
            task_eqp_bom_part.bom_part_id     (+)= task_inv_inv.bom_part_id      AND
      
            task_eqp_assmbl_pos.assmbl_db_id  (+)= task_inv_inv.assmbl_db_id     AND
            task_eqp_assmbl_pos.assmbl_cd     (+)= task_inv_inv.assmbl_cd        AND
            task_eqp_assmbl_pos.assmbl_bom_id (+)= task_inv_inv.assmbl_bom_id    AND
            task_eqp_assmbl_pos.assmbl_pos_id (+)= task_inv_inv.assmbl_pos_id 
      
            AND               
      
            (
      
                (
                    task_ass_inv_inv.inv_no_db_id = task_inv_inv.assmbl_inv_no_db_id   AND
                    task_ass_inv_inv.inv_no_id    = task_inv_inv.assmbl_inv_no_id      
                    AND
                    cs_BomClassCd = 'SYS'
                )
                OR
                (
                    task_ass_inv_inv.inv_no_db_id = task_inv_inv.inv_no_db_id  AND
                    task_ass_inv_inv.inv_no_id    = task_inv_inv.inv_no_id     
                    AND
                    cs_BomClassCd != 'SYS'
                )
            )
         )
         AND
         task_inv_inv.rstat_cd = 0
         AND
         task_task.rstat_cd = 0
      
      
      UNION
      
      
      SELECT
         task_inv_inv.inv_no_db_id,
         task_inv_inv.inv_no_id
      FROM
         task_task
         INNER JOIN task_part_map ON 
            task_part_map.task_db_id = task_task.task_db_id AND
            task_part_map.task_id    = task_task.task_id
         INNER JOIN eqp_part_no   ON 
            eqp_part_no.part_no_db_id  = task_part_map.part_no_db_id AND
            eqp_part_no.part_no_id     = task_part_map.part_no_id
         INNER JOIN inv_inv task_inv_inv ON 
            task_inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
            task_inv_inv.part_no_id    = eqp_part_no.part_no_id
         INNER JOIN inv_inv h_inv_inv ON 
            h_inv_inv.inv_no_db_id = task_inv_inv.h_inv_no_db_id AND
            h_inv_inv.inv_no_id    = task_inv_inv.h_inv_no_id
         INNER JOIN inv_inv task_ass_inv_inv ON 
            task_ass_inv_inv.inv_no_db_id = task_inv_inv.inv_no_db_id AND
            task_ass_inv_inv.inv_no_id    = task_inv_inv.inv_no_id
         LEFT OUTER JOIN eqp_bom_part task_eqp_bom_part ON 
            task_eqp_bom_part.assmbl_db_id    = task_inv_inv.assmbl_db_id AND
            task_eqp_bom_part.assmbl_cd       = task_inv_inv.assmbl_cd    AND
            task_eqp_bom_part.assmbl_bom_id   = task_inv_inv.assmbl_bom_id AND
            task_eqp_bom_part.bom_part_db_id  = task_inv_inv.bom_part_db_id AND
            task_eqp_bom_part.bom_part_id     = task_inv_inv.bom_part_id
         LEFT OUTER JOIN eqp_assmbl_pos task_eqp_assmbl_pos ON  
            task_eqp_assmbl_pos.assmbl_db_id  = task_inv_inv.assmbl_db_id  AND
            task_eqp_assmbl_pos.assmbl_cd     = task_inv_inv.assmbl_cd AND
            task_eqp_assmbl_pos.assmbl_bom_id = task_inv_inv.assmbl_bom_id AND
            task_eqp_assmbl_pos.assmbl_pos_id = task_inv_inv.assmbl_pos_id
      
         WHERE
            cs_BomClassCd IS NULL
            AND
            task_task.task_db_id = cn_TaskDbId  AND
            task_task.task_id    = cn_TaskId
            AND
            task_task.task_def_status_cd IN ('BUILD','REVISION')
            AND
            (
               EXISTS
               (
                  SELECT
                     1
                  FROM
                     org_hr
                  WHERE
                     org_hr.hr_db_id = cn_HrDbId AND
                     org_hr.hr_id    = cn_HrId
                     AND
                     org_hr.all_authority_bool = 1
               )
               OR
               h_inv_inv.authority_db_id IS NULL
               OR
               EXISTS
               (
                  SELECT
                     1
                  FROM
                     org_hr_authority
                  WHERE
                     org_hr_authority.hr_db_id = cn_HrDbId AND
                     org_hr_authority.hr_id    = cn_HrId
                     AND
                     org_hr_authority.authority_db_id = h_inv_inv.authority_db_id AND
                     org_hr_authority.authority_id    = h_inv_inv.authority_id
               )
            )
            -- task will be scheduled from BIRTH
            AND
            task_task.relative_bool = 0
            -- only BLOCK and REQ
            AND
            task_task.task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd IN ('BLOCK', 'REQ'))
            AND
            task_task.task_class_cd NOT IN ('REPL', 'CORR')
            -- should have scheduling rules
            AND EXISTS
            (
               SELECT
                  1
               FROM
                  task_sched_rule
               WHERE
                  task_sched_rule.task_db_id = cn_TaskDbId   AND
                  task_sched_rule.task_id    = cn_TaskId
            )
            -- conditions for Manufacture Date or Received Date
            AND
            (
             (
              task_task.sched_from_received_dt_bool = 1 AND
              task_inv_inv.received_dt IS NULL
             )
             OR
             (
              task_task.sched_from_received_dt_bool = 0 AND
              task_inv_inv.manufact_dt IS NULL
             )
            )
            -- inv should not be ARCHIVE or SCRAP
            AND
            task_inv_inv.inv_cond_cd NOT IN ('ARCHIVE', 'SCRAP')
            AND
            task_inv_inv.rstat_cd = 0
            AND
            task_task.rstat_cd = 0;

   lrec_taskdefn_invs    lcur_taskdefn_invs%ROWTYPE;
   
   
BEGIN

   ln_RecCounter := 1;
   ln_MaxRecCount := an_MaxRecords + 101;
   ltot_tab_inv_tree := mxkeytable();
   ltot_tab_inv_tree.extend(ln_MaxRecCount);
   
   BEGIN
      
      SELECT 
         task_task.assmbl_bom_id, task_task.sched_from_received_dt_bool, eqp_assmbl_bom.bom_class_cd 
         INTO 
         ln_AssmblBomId, lb_ReceivedDtBool, ls_BomClassCd 
      FROM 
         task_task
         INNER JOIN eqp_assmbl_bom ON
            eqp_assmbl_bom.assmbl_db_id  = task_task.assmbl_db_id  AND
            eqp_assmbl_bom.assmbl_cd     = task_task.assmbl_cd   AND
            eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id
      WHERE 
         task_task.task_db_id = an_TaskDbId 
         AND 
         task_task.task_id    = an_TaskId;
      
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            
            ln_AssmblBomId := null;
            ls_BomClassCd  := null;
            
            SELECT 
               sched_from_received_dt_bool INTO lb_ReceivedDtBool
            FROM 
               task_task 
            WHERE 
               task_db_id = an_TaskDbId AND 
               task_id    = an_TaskId;
   END;
   
      
   FOR lrec_taskdefn_invs IN  lcur_taskdefn_invs(an_TaskDbId,an_TaskId, an_HrDbId, an_HrId, ln_AssmblBomId, ls_BomClassCd)
   LOOP
   
      EXIT WHEN lcur_taskdefn_invs%NOTFOUND;
      
      -- get the sub inv tree into lt_tab_inv_tree
      SELECT 
         mxkey(inv_no_db_id, inv_no_id) BULK COLLECT INTO lt_tab_inv_tree
      FROM
         (
            SELECT 
               inv_no_db_id, inv_no_id, inv_no_sdesc, barcode_sdesc, inv_cond_cd , received_dt, manufact_dt, rstat_cd
            FROM 
               inv_inv
               START WITH
               inv_inv.inv_no_db_id = lrec_taskdefn_invs.inv_no_db_id  AND
               inv_inv.inv_no_id    = lrec_taskdefn_invs.inv_no_id
               CONNECT BY
               inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id  and
               inv_inv.nh_inv_no_id    = PRIOR inv_inv.inv_no_id
         ) inv_tree
      WHERE
         -- conditions for Manufacture Date or Received Date
         (
          (
           lb_ReceivedDtBool = 1 AND
           inv_tree.received_dt IS NULL
          )
          OR
          (
           lb_ReceivedDtBool = 0 AND
           inv_tree.manufact_dt IS NULL
          )
         )
         -- inv should not be ARCHIVE or SCRAP
         AND
         inv_tree.inv_cond_cd NOT IN ('ARCHIVE', 'SCRAP') 
         AND
         inv_tree.rstat_cd = 0;
         
         
      -- copy the records from lt_tab_inv_tree to ltot_tab_inv_tree
      FOR i IN 1..lt_tab_inv_tree.COUNT
      LOOP
         
         ltot_tab_inv_tree(ln_RecCounter) := mxkey(lt_tab_inv_tree(i).db_id, lt_tab_inv_tree(i).id);
         ln_RecCounter := ln_RecCounter + 1;
         EXIT WHEN ln_RecCounter > ln_MaxRecCount;
         
      END LOOP;         
         
         
      EXIT WHEN ln_RecCounter > ln_MaxRecCount;
   
   END LOOP;
   
   
   -- if total records are less than (ln_MaxRecCount + 101), then remove the existing null objects from ltot_tab_inv_tree
   IF ln_RecCounter < ln_MaxRecCount THEN
      ltot_tab_inv_tree.trim(ln_MaxRecCount - (ln_RecCounter - 1));   
   END IF;
   
   
   
   RETURN ltot_tab_inv_tree;

END GetInvsMissingDatesForTaskDefn;
/