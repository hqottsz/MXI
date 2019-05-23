--liquibase formatted sql


--changeSet OPER-20378:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

  FOR c_cur_job_step IN ( WITH
                                labour_role_status AS
                                (
                                  SELECT
                                     sched_labour_role.labour_db_id,
                                     sched_labour_role.labour_id,
                                     sched_labour_role.labour_role_type_cd,
                                     sched_labour_role_status.labour_role_db_id,
                                     sched_labour_role_status.labour_role_id,
                                     sched_labour_role_status.hr_db_id,
                                     sched_labour_role_status.hr_id
                                  FROM
                                     sched_labour_role
                                    LEFT OUTER JOIN sched_labour_role_status
                                        ON sched_labour_role_status.labour_role_db_id = sched_labour_role.labour_role_db_id AND
                                           sched_labour_role_status.labour_role_id    = sched_labour_role.labour_role_id
                                  WHERE
                                     -- Filter to only the latest Role for each Labour Requirement
                                     sched_labour_role_status.status_ord IS NULL
                                     OR
                                     sched_labour_role_status.status_ord =
                                       ( SELECT MAX( temp_status.status_ord )
                                          FROM sched_labour_role_status temp_status
                                          WHERE
                                             temp_status.labour_role_db_id = sched_labour_role.labour_role_db_id AND
                                             temp_status.labour_role_id    = sched_labour_role.labour_role_id
                                             AND
                                             temp_status.labour_role_status_cd = 'COMPLETE'
                                       )
                                )
                            
                            SELECT
                                 sched_db_id,
                                 sched_id,
                                 step_id,
                                 step_ord,
                                 labour_step_ord,
                                 sched_step_revision_no,
                                 step_status_cd,
                                 labour_step_status_cd,
                                 barcode_sdesc,
                                 labour_step_creation_dt
                            FROM(
                                  SELECT DISTINCT
                                     RANK() OVER (PARTITION BY sched_step.sched_db_id, sched_step.sched_id, sched_step.step_id ORDER BY sched_labour_history_union.ord_id DESC NULLS LAST) labour_ord_rank,
                                     sched_step.sched_db_id,
                                     sched_step.sched_id,
                                     sched_step.step_id,
                                     sched_step.step_ord,
                                     sched_step.step_status_cd,
                                     sched_labour_history_union.step_status_cd labour_step_status_cd,
                                     sched_labour_history_union.ord_id AS labour_step_ord,
                                     sched_labour_history_union.creation_dt AS labour_step_creation_dt,
                                     sched_step.revision_no AS sched_step_revision_no,
                                     sched_stask.barcode_sdesc
                                  FROM
                                     sched_step
                        
                                     INNER JOIN sched_stask
                                        ON sched_stask.sched_db_id = sched_step.sched_db_id AND
                                           sched_stask.sched_id    = sched_step.sched_id AND
                                           sched_stask.hist_bool_ro = 0 AND
                                           ((sched_stask.task_class_cd = 'ADHOC' AND
                                            sched_stask.task_class_db_id = 0)
                                            OR
                                            (sched_stask.fault_db_id IS NOT NULL)) 
                                        
                                     LEFT JOIN task_step
                                        ON task_step.task_db_id = sched_stask.task_db_id AND
                                           task_step.task_id    = sched_stask.task_id AND
                                           task_step.step_ord     = sched_step.step_ord
                        
                                     LEFT JOIN (
                                           SELECT
                                              sched_db_id,
                                              sched_id,
                                              step_id,
                                              ord_id,
                                              notes_ldesc,
                                              step_status_cd,
                                              labour_db_id,
                                              labour_id,
                                              null as hr_db_id,
                                              null as hr_id,
                                              creation_dt,
                                              revision_no
                                           FROM
                                              sched_labour_step
                                           UNION
                                           SELECT
                                              sched_db_id,
                                              sched_id,
                                              step_id,
                                              null,
                                              notes_ldesc,
                                              step_status_cd,
                                              null,
                                              null,
                                              hr_db_id,
                                              hr_id,
                                              creation_dt,
                                              revision_no
                                           FROM
                                              sched_step_appl_log
                                        ) sched_labour_history_union ON
                                        sched_labour_history_union.sched_db_id = sched_step.sched_db_id AND
                                        sched_labour_history_union.sched_id    = sched_step.sched_id AND
                                        sched_labour_history_union.step_id     = sched_step.step_id
                        
                                     LEFT JOIN ref_step_status labour_step_status
                                        ON labour_step_status.step_status_cd    = sched_labour_history_union.step_status_cd
                                    
                                     LEFT JOIN labour_role_status
                                        ON labour_role_status.labour_db_id = sched_labour_history_union.labour_db_id AND
                                           labour_role_status.labour_id    = sched_labour_history_union.labour_id
                                           AND
                                           labour_role_status.labour_role_type_cd = 'TECH'
                        
                                     LEFT JOIN sched_labour
                                        ON sched_labour.labour_db_id = labour_role_status.labour_db_id AND
                                           sched_labour.labour_id    = labour_role_status.labour_id
                                     LEFT JOIN org_hr
                                        ON org_hr.hr_db_id = labour_role_status.hr_db_id AND
                                           org_hr.hr_id    = labour_role_status.hr_id
                        
                                     LEFT JOIN utl_user
                                        ON utl_user.user_id = org_hr.user_id
                        
                                     LEFT JOIN org_hr appl_log_org_hr ON
                                        appl_log_org_hr.hr_db_id = sched_labour_history_union.hr_db_id AND
                                        appl_log_org_hr.hr_id = sched_labour_history_union.hr_id
                        
                                     LEFT JOIN utl_user appl_log_user ON
                                        appl_log_user.user_id = appl_log_org_hr.user_id
                                     
                                ) WHERE labour_ord_rank = 1 AND
                                  -- Do not sync pending job step labour
                                  labour_step_status_cd <> 'MXPENDING' AND
                                  -- Do not sync already completed steps
                                  step_status_cd <> 'MXCOMPLETE' AND
                                  step_status_cd <> labour_step_status_cd
                                 
                                ORDER BY
                                   step_ord asc,
                                   labour_step_creation_dt ASC)
  LOOP
        -- Update Job Step without revision incrementing
        UPDATE sched_step
        SET sched_step.step_status_cd = c_cur_job_step.labour_step_status_cd,
          sched_step.revision_no = -1
        WHERE sched_step.sched_db_id = c_cur_job_step.sched_db_id AND
            sched_step.sched_id = c_cur_job_step.sched_id AND
            sched_step.step_id = c_cur_job_step.step_id;
  END LOOP;
END;
/