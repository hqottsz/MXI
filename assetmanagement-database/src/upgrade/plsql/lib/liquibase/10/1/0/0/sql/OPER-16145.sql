--liquibase formatted sql

--changeset OPER-16145:1 stripComments:false
--comment Insert ppc_task_defn info
INSERT INTO ppc_task_defn 
               SELECT sys_guid(),
                      wp.work_package_id,
                      t.task_defn_db_id, 
                      t.task_defn_id,
                      'MPC',
                      0, SYSDATE, SYSDATE, 0, 'OPER-16145'
               FROM   ppc_plan pp
               JOIN   ppc_wp wp ON pp.plan_id = wp.plan_id
               JOIN   ppc_activity a ON a.work_package_id = wp.work_package_id
               JOIN   ppc_task t ON t.task_id = a.activity_id
               JOIN   task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                                  AND t.task_defn_id = td.task_defn_id
               JOIN   task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                                  AND tt.task_defn_id = td.task_defn_id
                                  AND td.last_revision_ord = tt.revision_ord
                                  AND tt.task_class_cd IN ('MPC','OPENPANEL','CLOSEPANEL')
               WHERE  pp.template_bool = 1
               AND NOT EXISTS (SELECT 1 FROM ppc_task_defn valid WHERE wp.work_package_id = valid.work_package_id
                      AND t.task_defn_db_id = valid.task_defn_db_id
                      AND t.task_defn_id = valid.task_defn_id);

--changeset OPER-16145:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Correct records missing ppc_task from OPER-11512
BEGIN

FOR lc_null_ppc_tasks IN (SELECT ppc_activity.activity_id, 
                                 ppc_wp.start_dt, 
                                 task_task.task_cd, 
                                 task_defn.task_defn_db_id, 
                                 task_defn.task_defn_id, 
                                 eqp_planning_type.planning_type_db_id, 
                                 eqp_planning_type.planning_type_id,
                                 mim_local_db.db_id
                            FROM ppc_activity 
                                 JOIN ppc_wp ON 
                                      ppc_wp.work_package_id=ppc_activity.work_package_id
                                 JOIN ppc_plan ON 
                                      ppc_plan.plan_id=ppc_wp.plan_id
                                 JOIN task_task ON 
                                      lower(task_task.task_cd)=lower(ppc_activity.activity_cd) AND
                                      ppc_wp.assmbl_cd=task_task.assmbl_cd
                                 JOIN task_defn ON 
                                      task_defn.task_defn_id=task_task.task_defn_id AND
                                      task_defn.task_defn_db_id=task_task.task_defn_db_id AND
                                      task_defn.last_revision_ord=task_task.revision_ord
                                 JOIN eqp_planning_type ON 
                                      eqp_planning_type.assmbl_cd=ppc_wp.assmbl_cd AND 
                                      planning_type_cd='STDR'
                                 JOIN mim_local_db ON 1=1
                           WHERE ppc_activity.ppc_activity_type_cd='TASK' AND ppc_plan.template_bool=1 AND
                                 NOT EXISTS (SELECT * 
                                               FROM ppc_task 
                                              WHERE ppc_task.task_id=ppc_activity.activity_id)
                                         ) 
LOOP
        INSERT INTO ppc_task
        (task_id,start_dt,end_Dt,task_defn_db_id,task_defn_id, planning_type_db_id,planning_type_id,nr_est_bool,watch_bool,late_bool,max_split_qt,split_bool, rstat_cd, task_status, pause_reason)
        VALUES (lc_null_ppc_tasks.activity_id, lc_null_ppc_tasks.start_dt, lc_null_ppc_tasks.start_dt,
                                      lc_null_ppc_tasks.task_defn_db_id,lc_null_ppc_tasks.task_defn_id,
                                      lc_null_ppc_tasks.planning_type_db_id,lc_null_ppc_tasks.planning_type_id,
                                      0,0,0,0,1,0,'ACTV',NULL);

END LOOP;
END;
/

--changeset OPER-16145:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Correct incorrect naming convention caused by bad OPER-11512 migration
DECLARE

BEGIN
-- For loop to go through all tasks which were migrated incorrectly
FOR fixTasks IN (SELECT task_task.task_defn_db_id AS task_defn_db_id,
                        task_task.task_defn_id AS task_defn_id,
                        task_task.task_cd AS task_cd,
                        task_task.task_name AS task_name,
                        mpc_task.activity_id AS activity_id,
                        mpc_task.operation_cd AS operation_cd
                   FROM task_task
                        INNER JOIN (SELECT CASE WHEN activity_cd LIKE '%-OPEN' THEN SUBSTR(ppc_activity.activity_cd, 0, LENGTH(ppc_activity.activity_cd) - 5)
                                                ELSE SUBSTR(ppc_activity.activity_cd, 0, LENGTH(ppc_activity.activity_cd) - 6)
                                            END AS task_cd,
                                            CASE WHEN activity_cd LIKE '%-OPEN' THEN SUBSTR(ppc_activity.activity_cd, LENGTH(ppc_activity.activity_cd) - 3, LENGTH(ppc_activity.activity_cd))
                                                ELSE SUBSTR(ppc_activity.activity_cd, LENGTH(ppc_activity.activity_cd) - 4, LENGTH(ppc_activity.activity_cd))
                                            END AS operation_cd,
                                            ppc_wp.assmbl_db_id AS assmbl_db_id,
                                            ppc_wp.assmbl_cd AS assmbl_cd,
                                            ppc_activity.activity_id AS activity_id
                                       FROM ppc_activity
                                            INNER JOIN ppc_wp ON
                                                  ppc_wp.work_package_id = ppc_activity.work_package_id
                                      WHERE activity_cd LIKE '%-OPEN' OR activity_cd LIKE '%-CLOSE') mpc_task ON
                               mpc_task.task_cd = task_task.task_cd AND
                               mpc_task.assmbl_db_id = task_task.assmbl_db_id AND
                               mpc_task.assmbl_cd = task_task.assmbl_cd
                    WHERE task_task.task_def_status_cd = 'ACTV')
LOOP
    
    -- Update ppc_task to include a link to the correct task definition information where missing
    UPDATE ppc_task
       SET task_defn_db_id = fixTasks.task_defn_db_id, 
           task_defn_id = fixTasks.task_defn_id
     WHERE task_id = fixTasks.activity_id;
     
    -- Correct the badly created activity codes and descriptions
    UPDATE ppc_activity
       SET activity_cd = fixTasks.task_cd,
           activity_sdesc = '[ ' || fixTasks.operation_cd || '-' || fixTasks.task_cd || ' (' || fixTasks.task_name || ') ]'
     WHERE activity_id = fixTasks.activity_id;
     
     
END LOOP;
END;
/

--changeSet OPER-16145:4 stripComments:false
MERGE INTO ppc_activity
USING (
   
   WITH template AS (SELECT ppc_wp.work_package_id
                       FROM ppc_plan
                      INNER JOIN ppc_wp ON ppc_wp.plan_id = ppc_plan.plan_id
                      WHERE ppc_plan.template_bool = 1)
     (SELECT template.work_package_id,
            task_defn.task_defn_db_id AS mpc_task_defn_db_id,
            task_defn.task_defn_id AS mpc_task_defn_id,
            task_task.task_cd AS mpc_code,
			task_task.task_name AS mpc_name,
            0 AS task_type_db_id,
            'OPEN' AS operation_cd
       FROM template
      INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id =
                                  template.work_package_id
      INNER JOIN task_defn ON task_defn.task_defn_db_id =
                              ppc_task_defn.task_defn_db_id
                          AND task_defn.task_defn_id =
                              ppc_task_defn.task_defn_id
      INNER JOIN task_task ON task_task.task_defn_db_id = 
                              task_defn.task_defn_db_id
                          AND task_task.task_defn_id =
                              task_defn.task_defn_id
						  AND task_defn.last_revision_ord = task_task.revision_ord
      WHERE 
         ppc_task_defn.class_mode_cd = 'MPC'
		 AND
		 task_task.task_def_status_db_id = 0 AND
		 task_task.task_def_status_cd = 'ACTV'
     
     UNION
     
     SELECT template.work_package_id,
            task_defn.task_defn_db_id AS mpc_task_defn_db_id,
            task_defn.task_defn_id AS mpc_task_defn_id,
            task_task.task_cd AS mpc_code,
			task_task.task_name AS mpc_name,
            0 AS task_type_db_id,
            'CLOSE' AS operation_cd
       FROM template
      INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id =
                                  template.work_package_id
      INNER JOIN task_defn ON task_defn.task_defn_db_id =
                              ppc_task_defn.task_defn_db_id
                          AND task_defn.task_defn_id =
                              ppc_task_defn.task_defn_id
      INNER JOIN task_task ON task_task.task_defn_db_id = 
                              task_defn.task_defn_db_id
                          AND task_task.task_defn_id =
                              task_defn.task_defn_id
						  AND task_defn.last_revision_ord = task_task.revision_ord
      WHERE ppc_task_defn.class_mode_cd = 'MPC'
	  AND
	  task_task.task_def_status_db_id = 0 AND
	  task_task.task_def_status_cd = 'ACTV'
      )
      ) mpc_template_task ON (
         ppc_activity.work_package_id = mpc_template_task.work_package_id 
         AND 
         ppc_activity.ppc_activity_type_db_id = 0 AND
         ppc_activity.ppc_activity_type_cd    = 'MPC_TMPL_TASK'
         ) WHEN NOT MATCHED THEN
       INSERT
         (activity_id,
          work_package_id,
          activity_cd,
          activity_sdesc,
          start_after_hrs,
          end_before_hrs,
          ppc_activity_type_db_id,
          ppc_activity_type_cd)
       VALUES
         (mx_key_pkg.new_uuid,
          mpc_template_task.work_package_id,
          mpc_template_task.mpc_code,
          '[ ' || mpc_template_task.operation_cd || '-' || mpc_template_task.mpc_code || ' (' || mpc_template_task.mpc_name || ') ]',
          null,
          null,
          0,
          'MPC_TMPL_TASK'
         );


--changeSet OPER-16145:5 stripComments:false
MERGE INTO ppc_task
USING (
   WITH template AS (SELECT ppc_wp.work_package_id,
                            ppc_wp.start_dt,
                            ppc_wp.assmbl_db_id,
                            ppc_wp.assmbl_cd
                       FROM ppc_plan
                      INNER JOIN ppc_wp ON ppc_wp.plan_id = ppc_plan.plan_id
                      WHERE ppc_plan.template_bool = 1)
     (SELECT 
          template.start_dt,
          ppc_activity.activity_id,
          ppc_activity.activity_cd,
          task_task.task_cd,
		  task_defn.task_defn_db_id,
		  task_defn.task_defn_id,
          eqp_planning_type.planning_type_db_id,
          eqp_planning_type.planning_type_id
       FROM template
      INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id =
                                  template.work_package_id
      INNER JOIN task_defn ON task_defn.task_defn_db_id =
                              ppc_task_defn.task_defn_db_id
                          AND task_defn.task_defn_id =
                              ppc_task_defn.task_defn_id
      INNER JOIN task_task ON task_task.task_defn_db_id = 
                              task_defn.task_defn_db_id
                          AND task_task.task_defn_id =
                              task_defn.task_defn_id
						  AND task_defn.last_revision_ord = task_task.revision_ord
      INNER JOIN ppc_activity ON ppc_activity.work_package_id = template.work_package_id AND
                                 ppc_activity.ppc_activity_type_db_id = 0 AND
                                 ppc_activity.activity_cd = task_task.task_cd AND
								 ppc_activity.activity_sdesc = '[ ' || 'OPEN' || '-' || task_task.task_cd || ' (' || task_task.task_name || ') ]'
      LEFT OUTER JOIN eqp_planning_type ON eqp_planning_type.assmbl_db_id = template.assmbl_db_id AND
                                           eqp_planning_type.assmbl_cd = template.assmbl_cd 
                                           AND
                                           eqp_planning_type.planning_type_cd = 'STDR'
      WHERE 
         ppc_task_defn.class_mode_cd = 'MPC'
		 AND
		 task_task.task_def_status_db_id = 0 AND
		 task_task.task_def_status_cd = 'ACTV'

      UNION
      
           SELECT 
          template.start_dt,
          ppc_activity.activity_id,
          ppc_activity.activity_cd,
          task_task.task_cd,
		  task_defn.task_defn_db_id,
		  task_defn.task_defn_id,
          eqp_planning_type.planning_type_db_id,
          eqp_planning_type.planning_type_id
       FROM template
      INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id =
                                  template.work_package_id
      INNER JOIN task_defn ON task_defn.task_defn_db_id =
                              ppc_task_defn.task_defn_db_id
                          AND task_defn.task_defn_id =
                              ppc_task_defn.task_defn_id
      INNER JOIN task_task ON task_task.task_defn_db_id = 
                              task_defn.task_defn_db_id
                          AND task_task.task_defn_id =
                              task_defn.task_defn_id
						  AND task_defn.last_revision_ord = task_task.revision_ord
      INNER JOIN ppc_activity ON ppc_activity.work_package_id = template.work_package_id AND
                                 ppc_activity.ppc_activity_type_db_id = 0 AND
                                 ppc_activity.activity_cd = task_task.task_cd AND
								 ppc_activity.activity_sdesc = '[ ' || 'CLOSE' || '-' || task_task.task_cd || ' (' || task_task.task_name || ') ]'
      LEFT OUTER JOIN eqp_planning_type ON eqp_planning_type.assmbl_db_id = template.assmbl_db_id AND
                                           eqp_planning_type.assmbl_cd = template.assmbl_cd 
                                           AND
                                           eqp_planning_type.planning_type_cd = 'STDR'
      WHERE 
         ppc_task_defn.class_mode_cd = 'MPC'
		 AND
		 task_task.task_def_status_db_id = 0 AND
		 task_task.task_def_status_cd = 'ACTV'
      )
      ) mpc_template_task ON (
         ppc_task.task_id = mpc_template_task.activity_id 
         ) WHEN NOT MATCHED THEN
       INSERT
         (task_id, 
          start_dt, 
          end_dt, 
          sched_db_id, 
          sched_id, 
          task_defn_db_id, 
          task_defn_id, 
          phase_id, 
          work_area_id, 
          nr_phase_id, 
          nr_start_milestone_id, 
          nr_end_milestone_id, 
          seq_id, 
          task_priority_db_id, 
          task_priority_cd, 
          planning_type_db_id, 
          planning_type_id, 
          crew_id, 
          nr_est_bool, 
          watch_bool, 
          late_bool, 
          max_split_qt, 
          split_bool)
       VALUES
         (mpc_template_task.activity_id,       -- task_id
          mpc_template_task.start_dt,          -- start_dt
          mpc_template_task.start_dt,          -- end_dt
          null,                                -- sched_db_id
          null,                                -- sched_id
          mpc_template_task.task_defn_db_id,   -- task_defn_db_id
          mpc_template_task.task_defn_id,      -- task_defn_id
          null, -- phase_id
          null, -- work_area_id
          null, -- nr_phase_id 
          null, -- nr_start_milestone_id
          null, -- nr_end_milestone_id
          null, -- seq_id
          null, -- task_priority_db_id
          null, -- task_priority_cd
          mpc_template_task.planning_type_db_id, -- planning_type_db_id
          mpc_template_task.planning_type_id,    -- planning_type_id
          null, -- crew_id
          0, -- nr_est_bool
          0, -- watch_bool 
          0, -- late_bool 
          0, -- max_split_qt
          1  -- split_bool
         );

--changeSet OPER-16145:6 stripComments:false
MERGE INTO ppc_mpc_template_task
USING (
   WITH template AS (SELECT ppc_wp.work_package_id
                       FROM ppc_plan
                      INNER JOIN ppc_wp ON ppc_wp.plan_id = ppc_plan.plan_id
                      WHERE ppc_plan.template_bool = 1)
     SELECT 
          ppc_activity.activity_id,
          task_defn.task_defn_db_id,
          task_defn.task_defn_id,
          'MPCOPEN' AS task_class_subclass_cd
       FROM template
      INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id =
                                  template.work_package_id
      INNER JOIN task_defn ON task_defn.task_defn_db_id =
                              ppc_task_defn.task_defn_db_id
                          AND task_defn.task_defn_id =
                              ppc_task_defn.task_defn_id
      INNER JOIN task_task ON task_task.task_defn_db_id = task_defn.task_defn_db_id AND
                              task_task.task_defn_id = task_defn.task_defn_id
						  AND task_defn.last_revision_ord = task_task.revision_ord
      INNER JOIN ppc_activity ON ppc_activity.work_package_id = template.work_package_id AND
                                 ppc_activity.ppc_activity_type_db_id = 0 AND
                                 ppc_activity.activity_cd = task_task.task_cd AND
								 ppc_activity.activity_sdesc = '[ ' || 'OPEN' || '-' || task_task.task_cd || ' (' || task_task.task_name || ') ]'
      WHERE 
         ppc_task_defn.class_mode_cd = 'MPC'
		 AND
		 task_task.task_def_status_db_id = 0 AND
		 task_task.task_def_status_cd = 'ACTV'
      
      UNION
      
           SELECT 
          ppc_activity.activity_id,
          task_defn.task_defn_db_id,
          task_defn.task_defn_id,
          'MPCCLOSE' AS task_class_subclass_cd
       FROM template
      INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id =
                                  template.work_package_id
      INNER JOIN task_defn ON task_defn.task_defn_db_id =
                              ppc_task_defn.task_defn_db_id
                          AND task_defn.task_defn_id =
                              ppc_task_defn.task_defn_id
      INNER JOIN task_task ON task_task.task_defn_db_id = task_defn.task_defn_db_id AND
                              task_task.task_defn_id = task_defn.task_defn_id
						  AND task_defn.last_revision_ord = task_task.revision_ord
      INNER JOIN ppc_activity ON ppc_activity.work_package_id = template.work_package_id AND
                                 ppc_activity.ppc_activity_type_db_id = 0 AND
                                 ppc_activity.activity_cd = task_task.task_cd AND
								 ppc_activity.activity_sdesc = '[ ' || 'CLOSE' || '-' || task_task.task_cd || ' (' || task_task.task_name || ') ]'
      WHERE 
         ppc_task_defn.class_mode_cd = 'MPC'
		 AND
		 task_task.task_def_status_db_id = 0 AND
		 task_task.task_def_status_cd = 'ACTV'
      ) mpc_template_task ON (
         ppc_mpc_template_task.task_id = mpc_template_task.activity_id 
         ) WHEN NOT MATCHED THEN
       INSERT
         (task_id, 
          parent_task_defn_db_id, 
          parent_task_defn_id, 
          task_class_subclass_cd)
       VALUES
         (mpc_template_task.activity_id,
          mpc_template_task.task_defn_db_id,
          mpc_template_task.task_defn_id,
          mpc_template_task.task_class_subclass_cd
         );


--changeSet OPER-16145:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Updating properties of the newly created MPC_TMPL_TASKs
DECLARE

   CURSOR lcur_GetAllOpenClosePanelJic IS
   SELECT
      mpc_task.task_defn_db_id,
      mpc_task.task_defn_id,
      DECODE(jic_task.task_class_cd, 'OPENPANEL', 'MPCOPEN', 'MPCCLOSE') AS task_class_subclass_cd,
      ppc_task.phase_id,
      ppc_task.work_area_id,
      ppc_task.nr_phase_id,
      ppc_task.nr_start_milestone_id,
      ppc_task.nr_end_milestone_id,
      ppc_task.task_priority_db_id,
      ppc_task.task_priority_cd,
      ppc_task.planning_type_db_id,
      ppc_task.planning_type_id,
      ppc_task.watch_bool
   FROM
      ppc_task
      INNER JOIN ppc_activity ON
         ppc_activity.activity_id = ppc_task.task_id
	  INNER JOIN task_defn ON
	     ppc_task.task_Defn_db_id = task_Defn.task_defn_db_id AND
		 ppc_task.task_defn_id = task_defn.task_defn_id
      INNER JOIN task_task jic_task ON
         jic_task.task_defn_db_id = ppc_task.task_defn_db_id AND
         jic_task.task_defn_id = ppc_task.task_defn_id
    AND task_defn.last_revision_ord = jic_task.revision_ord
      INNER JOIN task_panel jic_panel ON
         jic_panel.task_db_id = jic_task.task_db_id AND
         jic_panel.task_id = jic_task.task_id
      INNER JOIN task_panel mpc_panel ON
         mpc_panel.panel_db_id = jic_panel.panel_db_id AND
         mpc_panel.panel_id = jic_panel.panel_id
      INNER JOIN task_task mpc_task ON
         mpc_task.task_db_id = mpc_panel.task_db_id AND
         mpc_task.task_id = mpc_panel.task_id
      INNER JOIN ppc_task_defn ON
         ppc_task_defn.task_defn_db_id = mpc_task.task_defn_db_id AND
         ppc_task_defn.task_defn_id = mpc_task.task_defn_id
   WHERE
      jic_task.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
      AND
      ppc_task_defn.work_package_id = ppc_activity.work_package_id;

    lrec_AllOpenCloseJic lcur_GetAllOpenClosePanelJic%ROWTYPE;       

BEGIN
   FOR lrec_AllOpenCloseJic IN lcur_GetAllOpenClosePanelJic LOOP
      
      MERGE INTO ppc_task USING (
         SELECT
            ppc_mpc_template_task.task_id
         FROM
            ppc_mpc_template_task
         WHERE
            ppc_mpc_template_task.parent_task_defn_db_id = lrec_AllOpenCloseJic.task_defn_db_id AND
            ppc_mpc_template_task.parent_task_defn_id = lrec_AllOpenCloseJic.task_defn_id
            AND
            ppc_mpc_template_task.task_class_subclass_cd = lrec_AllOpenCloseJic.task_class_subclass_cd
      ) mpc_template_task ON (
         mpc_template_task.task_id = ppc_task.task_id
      ) WHEN MATCHED THEN 
         UPDATE SET
            ppc_task.phase_id = lrec_AllOpenCloseJic.phase_id,
            ppc_task.work_area_id = lrec_AllOpenCloseJic.work_area_id,
            ppc_task.nr_phase_id = lrec_AllOpenCloseJic.nr_phase_id,
            ppc_task.nr_start_milestone_id = lrec_AllOpenCloseJic.nr_start_milestone_id,
            ppc_task.nr_end_milestone_id = lrec_AllOpenCloseJic.nr_end_milestone_id,
            ppc_task.task_priority_db_id = lrec_AllOpenCloseJic.task_priority_db_id,
            ppc_task.task_priority_cd = lrec_AllOpenCloseJic.task_priority_cd,
            ppc_task.planning_type_db_id = lrec_AllOpenCloseJic.planning_type_db_id,
            ppc_task.planning_type_id = lrec_AllOpenCloseJic.planning_type_id,
            ppc_task.watch_bool = lrec_AllOpenCloseJic.watch_bool;
      
   END LOOP;
END;
/

--changeSet OPER-16145:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Inserting Labour Rows for newly created tasks
DECLARE
  CURSOR lcur_GetLabourForOpenCloseJic IS
  SELECT 
     mpc_task.task_defn_db_id,
     mpc_task.task_defn_id,
     DECODE(jic_task.task_class_cd, 'OPENPANEL', 'MPCOPEN', 'MPCCLOSE') AS task_class_subclass_cd,
     ppc_activity.activity_id,
     task_labour_list.labour_skill_db_id, 
     task_labour_list.labour_skill_cd, 
     task_labour_list.man_pwr_ct, 
     task_labour_list.work_perf_bool, 
     task_labour_list.work_perf_hr, 
     task_labour_list.cert_bool, 
     task_labour_list.cert_hr, 
     task_labour_list.insp_bool, 
     task_labour_list.insp_hr
  FROM ppc_task 
  INNER JOIN ppc_activity ON 
     ppc_activity.activity_id = ppc_task.task_id
	  INNER JOIN task_defn ON
	     ppc_task.task_Defn_db_id = task_Defn.task_defn_db_id AND
		 ppc_task.task_defn_id = task_defn.task_defn_id
  INNER JOIN task_task jic_task ON
     jic_task.task_defn_db_id = ppc_task.task_defn_db_id AND
     jic_task.task_defn_id = ppc_task.task_defn_id AND
	 jic_task.revision_ord = task_defn.last_revision_ord
  INNER JOIN task_panel jic_panel ON
     jic_panel.task_db_id = jic_task.task_db_id AND
     jic_panel.task_id = jic_task.task_id
  INNER JOIN task_panel mpc_panel ON
     mpc_panel.panel_db_id = jic_panel.panel_db_id AND
     mpc_panel.panel_id = jic_panel.panel_id
  INNER JOIN task_task mpc_task ON
     mpc_task.task_db_id = mpc_panel.task_db_id AND
     mpc_task.task_id = mpc_panel.task_id
  INNER JOIN ppc_task_defn ON
     ppc_task_defn.task_defn_db_id = mpc_task.task_defn_db_id AND
     ppc_task_defn.task_defn_id = mpc_task.task_defn_id
  INNER JOIN task_labour_list ON
     task_labour_list.task_db_id = jic_task.task_db_id AND
     task_labour_list.task_id = jic_task.task_id
  WHERE
     jic_task.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
     AND
     ppc_task_defn.work_package_id = ppc_activity.work_package_id
     AND
     jic_task.task_def_status_db_id = 0 AND
     jic_task.task_def_status_cd = 'ACTV'
     AND
     mpc_task.task_def_status_db_id = 0 AND
     mpc_task.task_def_status_cd = 'ACTV';

     lrec_LabourForOpenCloseJic lcur_GetLabourForOpenCloseJic%ROWTYPE;       

BEGIN

   FOR lrec_LabourForOpenCloseJic IN lcur_GetLabourForOpenCloseJic LOOP
        
      MERGE INTO ppc_labour USING (
         SELECT
            ppc_mpc_template_task.task_id
         FROM
            ppc_mpc_template_task
         WHERE
            ppc_mpc_template_task.parent_task_defn_db_id = lrec_LabourForOpenCloseJic.task_defn_db_id AND
            ppc_mpc_template_task.parent_task_defn_id = lrec_LabourForOpenCloseJic.task_defn_id
            AND
            ppc_mpc_template_task.task_class_subclass_cd = lrec_LabourForOpenCloseJic.task_class_subclass_cd
      ) mpc_template_task ON (
         mpc_template_task.task_id = ppc_labour.task_id
      ) WHEN NOT MATCHED THEN 
         INSERT
          (
             ppc_labour.labour_id, 
             ppc_labour.task_id, 
             ppc_labour.labour_skill_db_id, 
             ppc_labour.labour_skill_cd, 
             ppc_labour.labour_stage_db_id, 
             ppc_labour.labour_stage_cd
          ) VALUES (
             mx_key_pkg.new_uuid,
             mpc_template_task.task_id,
             lrec_LabourForOpenCloseJic.labour_skill_db_id,
             lrec_LabourForOpenCloseJic.labour_skill_cd,
             0,
             'ACTV'
          );
   END LOOP;
END;
/

--changeSet OPER-16145:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Inserting Labour Roles for newly created labour rows
DECLARE 

  CURSOR lcur_GetLabourForOpenCloseJic IS
  SELECT 
     mpc_task.task_defn_db_id,
     mpc_task.task_defn_id,
     DECODE(jic_task.task_class_cd, 'OPENPANEL', 'MPCOPEN', 'MPCCLOSE') AS task_class_subclass_cd,
     ppc_activity.activity_id,
     task_labour_list.labour_skill_db_id, 
     task_labour_list.labour_skill_cd, 
     task_labour_list.man_pwr_ct, 
     task_labour_list.work_perf_bool, 
     task_labour_list.work_perf_hr, 
     task_labour_list.cert_bool, 
     task_labour_list.cert_hr, 
     task_labour_list.insp_bool, 
     task_labour_list.insp_hr
  FROM ppc_task 
  INNER JOIN ppc_activity ON 
     ppc_activity.activity_id = ppc_task.task_id
	 INNER JOIN task_defn on task_defn.task_defn_db_id = ppc_task.task_defn_db_id
						and task_defn.task_defn_id = ppc_task.task_defn_id
  INNER JOIN task_task jic_task ON
     jic_task.task_defn_db_id = ppc_task.task_defn_db_id AND
     jic_task.task_defn_id = ppc_task.task_defn_id and
	 jic_task.revision_ord = task_defn.last_revision_ord
  INNER JOIN task_panel jic_panel ON
     jic_panel.task_db_id = jic_task.task_db_id AND
     jic_panel.task_id = jic_task.task_id
  INNER JOIN task_panel mpc_panel ON
     mpc_panel.panel_db_id = jic_panel.panel_db_id AND
     mpc_panel.panel_id = jic_panel.panel_id
  INNER JOIN task_task mpc_task ON
     mpc_task.task_db_id = mpc_panel.task_db_id AND
     mpc_task.task_id = mpc_panel.task_id
  INNER JOIN ppc_task_defn ON
     ppc_task_defn.task_defn_db_id = mpc_task.task_defn_db_id AND
     ppc_task_defn.task_defn_id = mpc_task.task_defn_id
  INNER JOIN task_labour_list ON
     task_labour_list.task_db_id = jic_task.task_db_id AND
     task_labour_list.task_id = jic_task.task_id
  WHERE
     jic_task.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
     AND
     ppc_task_defn.work_package_id = ppc_activity.work_package_id;

     lrec_LabourForOpenCloseJic lcur_GetLabourForOpenCloseJic%ROWTYPE;

BEGIN
   FOR lrec_LabourForOpenCloseJic IN lcur_GetLabourForOpenCloseJic LOOP
      
      IF lrec_LabourForOpenCloseJic.work_perf_bool=1 THEN
        MERGE INTO ppc_labour_role USING (
           SELECT
              ppc_labour.labour_id,
              ppc_task.start_dt
           FROM
              ppc_mpc_template_task
              INNER JOIN ppc_task ON
                 ppc_task.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour ON
                 ppc_labour.task_id = ppc_mpc_template_task.task_id
           WHERE
              ppc_mpc_template_task.parent_task_defn_db_id = lrec_LabourForOpenCloseJic.task_defn_db_id AND
              ppc_mpc_template_task.parent_task_defn_id = lrec_LabourForOpenCloseJic.task_defn_id
              AND
              ppc_mpc_template_task.task_class_subclass_cd = lrec_LabourForOpenCloseJic.task_class_subclass_cd
              AND
              ppc_labour.labour_skill_db_id = lrec_LabourForOpenCloseJic.labour_skill_db_id AND
              ppc_labour.labour_skill_cd = lrec_LabourForOpenCloseJic.labour_skill_cd
        ) mpc_template_task_labour ON (
           mpc_template_task_labour.labour_id = ppc_labour_role.labour_id
           AND
           ppc_labour_role.labour_role_type_db_id = 0 AND
           ppc_labour_role.labour_role_type_cd = 'TECH'
        ) WHEN NOT MATCHED THEN 
           INSERT
            (
               ppc_labour_role.labour_role_id, 
               ppc_labour_role.labour_id, 
               ppc_labour_role.labour_role_type_db_id, 
               ppc_labour_role.labour_role_type_cd, 
               ppc_labour_role.start_dt, 
               ppc_labour_role.end_dt, 
               ppc_labour_role.sched_hr, 
               ppc_labour_role.actual_hr, 
               ppc_labour_role.labour_role_status_db_id, 
               ppc_labour_role.labour_role_status_cd
            ) VALUES (
               mx_key_pkg.new_uuid,
               mpc_template_task_labour.labour_id,
               0,
               'TECH',
               mpc_template_task_labour.start_dt,
               mpc_template_task_labour.start_dt + lrec_LabourForOpenCloseJic.work_perf_hr/24,
               lrec_LabourForOpenCloseJic.work_perf_hr,
               0,
               0,
               'ACTV'
            );
      END IF;
      
      IF lrec_LabourForOpenCloseJic.cert_bool = 1 THEN
        MERGE INTO ppc_labour_role USING (
           SELECT
              ppc_labour.labour_id,
              ppc_task.start_dt
           FROM
              ppc_mpc_template_task
              INNER JOIN ppc_task ON
                 ppc_task.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour ON
                 ppc_labour.task_id = ppc_mpc_template_task.task_id
           WHERE
              ppc_mpc_template_task.parent_task_defn_db_id = lrec_LabourForOpenCloseJic.task_defn_db_id AND
              ppc_mpc_template_task.parent_task_defn_id = lrec_LabourForOpenCloseJic.task_defn_id
              AND
              ppc_mpc_template_task.task_class_subclass_cd = lrec_LabourForOpenCloseJic.task_class_subclass_cd
              AND
              ppc_labour.labour_skill_db_id = lrec_LabourForOpenCloseJic.labour_skill_db_id AND
              ppc_labour.labour_skill_cd = lrec_LabourForOpenCloseJic.labour_skill_cd
        ) mpc_template_task_labour ON (
           mpc_template_task_labour.labour_id = ppc_labour_role.labour_id
           AND
           ppc_labour_role.labour_role_type_db_id = 0 AND
           ppc_labour_role.labour_role_type_cd = 'CERT'
        ) WHEN NOT MATCHED THEN 
           INSERT
            (
               ppc_labour_role.labour_role_id, 
               ppc_labour_role.labour_id, 
               ppc_labour_role.labour_role_type_db_id, 
               ppc_labour_role.labour_role_type_cd, 
               ppc_labour_role.start_dt, 
               ppc_labour_role.end_dt, 
               ppc_labour_role.sched_hr, 
               ppc_labour_role.actual_hr, 
               ppc_labour_role.labour_role_status_db_id, 
               ppc_labour_role.labour_role_status_cd
            ) VALUES (
               mx_key_pkg.new_uuid,
               mpc_template_task_labour.labour_id,
               0,
               'CERT',
               mpc_template_task_labour.start_dt,
               mpc_template_task_labour.start_dt + lrec_LabourForOpenCloseJic.cert_hr/24,
               lrec_LabourForOpenCloseJic.cert_hr,
               0,
               0,
               'ACTV'
            );
      END IF;

      IF lrec_LabourForOpenCloseJic.insp_bool = 1 THEN
        MERGE INTO ppc_labour_role USING (
           SELECT
              ppc_labour.labour_id,
              ppc_task.start_dt
           FROM
              ppc_mpc_template_task
              INNER JOIN ppc_task ON
                 ppc_task.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour ON
                 ppc_labour.task_id = ppc_mpc_template_task.task_id
           WHERE
              ppc_mpc_template_task.parent_task_defn_db_id = lrec_LabourForOpenCloseJic.task_defn_db_id AND
              ppc_mpc_template_task.parent_task_defn_id = lrec_LabourForOpenCloseJic.task_defn_id
              AND
              ppc_mpc_template_task.task_class_subclass_cd = lrec_LabourForOpenCloseJic.task_class_subclass_cd
              AND
              ppc_labour.labour_skill_db_id = lrec_LabourForOpenCloseJic.labour_skill_db_id AND
              ppc_labour.labour_skill_cd = lrec_LabourForOpenCloseJic.labour_skill_cd
        ) mpc_template_task_labour ON (
           mpc_template_task_labour.labour_id = ppc_labour_role.labour_id
           AND
           ppc_labour_role.labour_role_type_db_id = 0 AND
           ppc_labour_role.labour_role_type_cd = 'INSP'
        ) WHEN NOT MATCHED THEN 
           INSERT
            (
               ppc_labour_role.labour_role_id, 
               ppc_labour_role.labour_id, 
               ppc_labour_role.labour_role_type_db_id, 
               ppc_labour_role.labour_role_type_cd, 
               ppc_labour_role.start_dt, 
               ppc_labour_role.end_dt, 
               ppc_labour_role.sched_hr, 
               ppc_labour_role.actual_hr, 
               ppc_labour_role.labour_role_status_db_id, 
               ppc_labour_role.labour_role_status_cd
            ) VALUES (
               mx_key_pkg.new_uuid,
               mpc_template_task_labour.labour_id,
               0,
               'INSP',
               mpc_template_task_labour.start_dt,
               mpc_template_task_labour.start_dt + lrec_LabourForOpenCloseJic.insp_hr/24,
               lrec_LabourForOpenCloseJic.insp_hr,
               0,
               0,
               'ACTV'
            );
      END IF;

   END LOOP;
END;
/

--changeSet OPER-16145:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Inserting HR Slots for newly created labour roles
DECLARE
  CURSOR lcur_GetLbrRoleForOpenCloseJic IS
  SELECT 
     mpc_task.task_defn_db_id,
     mpc_task.task_defn_id,
     DECODE(jic_task.task_class_cd, 'OPENPANEL', 'MPCOPEN', 'MPCCLOSE') AS task_class_subclass_cd,
     ppc_activity.activity_id,
     task_labour_list.labour_skill_db_id, 
     task_labour_list.labour_skill_cd, 
     task_labour_list.man_pwr_ct, 
     task_labour_list.work_perf_bool, 
     task_labour_list.work_perf_hr, 
     task_labour_list.cert_bool, 
     task_labour_list.cert_hr, 
     task_labour_list.insp_bool, 
     task_labour_list.insp_hr
  FROM ppc_task 
  INNER JOIN ppc_activity ON 
     ppc_activity.activity_id = ppc_task.task_id
	 INNER JOIN task_defn on task_defn.task_defn_db_id = ppc_task.task_defn_db_id
						and task_defn.task_defn_id = ppc_task.task_defn_id
  INNER JOIN task_task jic_task ON
     jic_task.task_defn_db_id = ppc_task.task_defn_db_id AND
     jic_task.task_defn_id = ppc_task.task_defn_id AND
	 jic_task.revision_ord = task_defn.last_revision_ord
  INNER JOIN task_panel jic_panel ON
     jic_panel.task_db_id = jic_task.task_db_id AND
     jic_panel.task_id = jic_task.task_id
  INNER JOIN task_panel mpc_panel ON
     mpc_panel.panel_db_id = jic_panel.panel_db_id AND
     mpc_panel.panel_id = jic_panel.panel_id
  INNER JOIN task_task mpc_task ON
     mpc_task.task_db_id = mpc_panel.task_db_id AND
     mpc_task.task_id = mpc_panel.task_id
  INNER JOIN ppc_task_defn ON
     ppc_task_defn.task_defn_db_id = mpc_task.task_defn_db_id AND
     ppc_task_defn.task_defn_id = mpc_task.task_defn_id
  INNER JOIN task_labour_list ON
     task_labour_list.task_db_id = jic_task.task_db_id AND
     task_labour_list.task_id = jic_task.task_id
  WHERE
     jic_task.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
     AND
     ppc_task_defn.work_package_id = ppc_activity.work_package_id
     AND
     jic_task.task_def_status_db_id = 0 AND
     jic_task.task_def_status_cd = 'ACTV'
     AND
     mpc_task.task_def_status_db_id = 0 AND
         mpc_task.task_def_status_cd IN ('ACTV','OBSOLETE');

     lrec_LabourRoleForOpenCloseJic lcur_GetLbrRoleForOpenCloseJic%ROWTYPE;       


BEGIN
   FOR lrec_LabourRoleForOpenCloseJic IN lcur_GetLbrRoleForOpenCloseJic LOOP
      
        MERGE INTO ppc_hr_slot USING (
           SELECT
              ppc_labour_role.labour_role_id,
              ppc_task.start_dt
           FROM
              ppc_mpc_template_task
              INNER JOIN ppc_task ON
                 ppc_task.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour ON
                 ppc_labour.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour_role ON
                 ppc_labour_role.labour_id = ppc_labour.labour_id
           WHERE
              ppc_mpc_template_task.parent_task_defn_db_id = lrec_LabourRoleForOpenCloseJic.task_defn_db_id AND
              ppc_mpc_template_task.parent_task_defn_id = lrec_LabourRoleForOpenCloseJic.task_defn_id
              AND
              ppc_mpc_template_task.task_class_subclass_cd = lrec_LabourRoleForOpenCloseJic.task_class_subclass_cd
              AND
              ppc_labour.labour_skill_db_id = lrec_LabourRoleForOpenCloseJic.labour_skill_db_id AND
              ppc_labour.labour_skill_cd = lrec_LabourRoleForOpenCloseJic.labour_skill_cd
              AND
              ppc_labour_role.labour_role_type_db_id = 0 AND
              ppc_labour_role.labour_role_type_cd = 'TECH'
        ) mpc_template_task_labour_role ON (
           mpc_template_task_labour_role.labour_role_id = ppc_hr_slot.labour_role_id
        ) WHEN NOT MATCHED THEN 
           INSERT
            (
               ppc_hr_slot.human_resource_slot_id, 
               ppc_hr_slot.hr_slot_ord, 
               ppc_hr_slot.labour_role_id, 
               ppc_hr_slot.locked_bool, 
               ppc_hr_slot.start_dt, 
               ppc_hr_slot.end_dt, 
               ppc_hr_slot.sched_hr
            ) VALUES (
               mx_key_pkg.new_uuid,
               1,
               mpc_template_task_labour_role.labour_role_id,
               0,
               mpc_template_task_labour_role.start_dt,
               mpc_template_task_labour_role.start_dt + lrec_LabourRoleForOpenCloseJic.work_perf_hr/24,
               lrec_LabourRoleForOpenCloseJic.work_perf_hr
            );
      
        MERGE INTO ppc_hr_slot USING (
           SELECT
              ppc_labour_role.labour_role_id,
              ppc_task.start_dt
           FROM
              ppc_mpc_template_task
              INNER JOIN ppc_task ON
                 ppc_task.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour ON
                 ppc_labour.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour_role ON
                 ppc_labour_role.labour_id = ppc_labour.labour_id
           WHERE
              ppc_mpc_template_task.parent_task_defn_db_id = lrec_LabourRoleForOpenCloseJic.task_defn_db_id AND
              ppc_mpc_template_task.parent_task_defn_id = lrec_LabourRoleForOpenCloseJic.task_defn_id
              AND
              ppc_mpc_template_task.task_class_subclass_cd = lrec_LabourRoleForOpenCloseJic.task_class_subclass_cd
              AND
              ppc_labour.labour_skill_db_id = lrec_LabourRoleForOpenCloseJic.labour_skill_db_id AND
              ppc_labour.labour_skill_cd = lrec_LabourRoleForOpenCloseJic.labour_skill_cd
              AND
              ppc_labour_role.labour_role_type_db_id = 0 AND
              ppc_labour_role.labour_role_type_cd = 'CERT'
        ) mpc_template_task_labour_role ON (
           mpc_template_task_labour_role.labour_role_id = ppc_hr_slot.labour_role_id
        ) WHEN NOT MATCHED THEN 
           INSERT
            (
               ppc_hr_slot.human_resource_slot_id, 
               ppc_hr_slot.hr_slot_ord, 
               ppc_hr_slot.labour_role_id, 
               ppc_hr_slot.locked_bool, 
               ppc_hr_slot.start_dt, 
               ppc_hr_slot.end_dt, 
               ppc_hr_slot.sched_hr
            ) VALUES (
               mx_key_pkg.new_uuid,
               1,
               mpc_template_task_labour_role.labour_role_id,
               0,
               mpc_template_task_labour_role.start_dt,
               mpc_template_task_labour_role.start_dt + lrec_LabourRoleForOpenCloseJic.cert_hr/24,
               lrec_LabourRoleForOpenCloseJic.cert_hr
            );
      
        MERGE INTO ppc_hr_slot USING (
           SELECT
              ppc_labour_role.labour_role_id,
              ppc_task.start_dt
           FROM
              ppc_mpc_template_task
              INNER JOIN ppc_task ON
                 ppc_task.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour ON
                 ppc_labour.task_id = ppc_mpc_template_task.task_id
              INNER JOIN ppc_labour_role ON
                 ppc_labour_role.labour_id = ppc_labour.labour_id
           WHERE
              ppc_mpc_template_task.parent_task_defn_db_id = lrec_LabourRoleForOpenCloseJic.task_defn_db_id AND
              ppc_mpc_template_task.parent_task_defn_id = lrec_LabourRoleForOpenCloseJic.task_defn_id
              AND
              ppc_mpc_template_task.task_class_subclass_cd = lrec_LabourRoleForOpenCloseJic.task_class_subclass_cd
              AND
              ppc_labour.labour_skill_db_id = lrec_LabourRoleForOpenCloseJic.labour_skill_db_id AND
              ppc_labour.labour_skill_cd = lrec_LabourRoleForOpenCloseJic.labour_skill_cd
              AND
              ppc_labour_role.labour_role_type_db_id = 0 AND
              ppc_labour_role.labour_role_type_cd = 'INSP'
        ) mpc_template_task_labour_role ON (
           mpc_template_task_labour_role.labour_role_id = ppc_hr_slot.labour_role_id
        ) WHEN NOT MATCHED THEN 
           INSERT
            (
               ppc_hr_slot.human_resource_slot_id, 
               ppc_hr_slot.hr_slot_ord, 
               ppc_hr_slot.labour_role_id, 
               ppc_hr_slot.locked_bool, 
               ppc_hr_slot.start_dt, 
               ppc_hr_slot.end_dt, 
               ppc_hr_slot.sched_hr
            ) VALUES (
               mx_key_pkg.new_uuid,
               1,
               mpc_template_task_labour_role.labour_role_id,
               0,
               mpc_template_task_labour_role.start_dt,
               mpc_template_task_labour_role.start_dt + lrec_LabourRoleForOpenCloseJic.insp_hr/24,
               lrec_LabourRoleForOpenCloseJic.insp_hr
            );

   END LOOP;
END;
/

--changeSet OPER-16145:11 stripComments:false
--comment Remove all PPC_HR_SLOT items, related to OPENPANEL and CLOSEPANEL PPC Tasks
DELETE FROM 
   ppc_hr_slot
WHERE (ppc_hr_slot.human_resource_slot_id) IN (
      SELECT phs.human_resource_slot_id AS human_resource_slot_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN ppc_labour pl ON pl.task_id = t.task_id
             JOIN ppc_labour_role plr ON plr.labour_id = pl.labour_id
             JOIN ppc_hr_slot phs ON phs.labour_role_id = plr.labour_role_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK')
);

--changeSet OPER-16145:12 stripComments:false
--comment Remove all PPC_LABOUR_ROLE items, related to OPENPANEL and CLOSEPANEL PPC Tasks
DELETE FROM 
   ppc_labour_role
WHERE (ppc_labour_role.labour_role_id) IN (
   SELECT plr.labour_role_id AS labour_role_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN ppc_labour pl ON pl.task_id = t.task_id
             JOIN ppc_labour_role plr ON plr.labour_id = pl.labour_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK')

);

--changeSet OPER-16145:13 stripComments:false
--comment Remove all PPC_LABOUR items, related to OPENPANEL and CLOSEPANEL PPC Tasks
DELETE FROM 
   ppc_labour
WHERE (ppc_labour.labour_id) IN (
   SELECT pl.labour_id AS labour_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN ppc_labour pl ON pl.task_id = t.task_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK')
);


--changeSet OPER-16145:14 stripComments:false
--comment Remove all PPC_PUBLISH_FAILURE items, related to OPENPANEL and CLOSEPANEL PPC Tasks
DELETE FROM 
   ppc_publish_failure
WHERE (ppc_publish_failure.failure_id) IN (
   SELECT ppf.failure_id AS failure_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
             INNER JOIN ppc_publish_failure ppf ON ppf.task_id = t.task_id
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK')
);


--changeSet OPER-16145:15 stripComments:false
--comment Remove all PPC_TASK_DEFN_MAP items, related to OPENPANEL and CLOSEPANEL PPC Tasks
DELETE FROM 
   ppc_task_defn_map
WHERE (ppc_task_defn_map.task_definition_id, ppc_task_defn_map.task_id) IN (
   SELECT ptdm.task_definition_id AS task_definition_id,
          t.task_id AS task_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
             INNER JOIN ppc_task_defn_map ptdm ON ptdm.task_id = t.task_id
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK')
);

--changeSet OPER-16145:16 stripComments:false
--comment Remove all PPC_TASK_PANEL items, related to OPENPANEL and CLOSEPANEL PPC Tasks
DELETE FROM 
   ppc_task_panel
WHERE (ppc_task_panel.task_id) IN (
   SELECT ptp.task_id AS task_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN ppc_task_panel ptp ON ptp.task_id = t.task_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK')
);

--changeSet OPER-16145:17 stripComments:false
--comment Remove all PPC_DEPENDENCIES items, related to OPENPANEL and CLOSEPANEL PPC Tasks
DELETE FROM 
   ppc_dependency
WHERE (ppc_dependency.from_activity_id, ppc_dependency.to_activity_id) IN (
   SELECT ppc_dependency.from_activity_id AS from_activity_id,
          ppc_dependency.to_activity_id AS to_activity_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
             JOIN ppc_dependency ON
                     (
                      ppc_dependency.from_activity_id = a.activity_id
                      OR
                      ppc_dependency.to_activity_id = a.activity_id
                     )
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK')
);

--changeSet OPER-16145:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Remove all PPC_TASK and PPC ACTIVITY items, related to deprecated MPC PPC Tasks
BEGIN

FOR TaskActivityList IN (
    SELECT a.activity_id AS activity_id
        FROM ppc_plan pp
             JOIN ppc_wp wp ON pp.plan_id = wp.plan_id
             JOIN ppc_activity a ON a.work_package_id = wp.work_package_id
             JOIN ppc_task t ON t.task_id = a.activity_id
             JOIN task_defn td ON t.task_defn_db_id = td.task_defn_db_id
                              AND t.task_defn_id = td.task_defn_id
             JOIN task_task tt ON tt.task_defn_db_id = td.task_defn_db_id
                              AND tt.task_defn_id = td.task_defn_id
                              AND td.last_revision_ord = tt.revision_ord
       WHERE tt.task_class_cd IN ('OPENPANEL','CLOSEPANEL')
          OR (
              pp.template_bool = 1
              AND tt.task_class_cd = 'MPC'
              AND a.ppc_activity_type_cd <> 'MPC_TMPL_TASK'))
LOOP
    DELETE FROM
        ppc_mpc_template_task
     WHERE ppc_mpc_template_task.task_id = TaskActivityList.Activity_Id;
    DELETE FROM 
        ppc_task
     WHERE ppc_task.task_id = TaskActivityList.activity_id; 
    DELETE FROM 
       ppc_activity
     WHERE ppc_activity.activity_id = TaskActivityList.activity_id;

END LOOP;
END;
/

--changeSet OPER-16145:19 stripComments:false
--comment Add all panels to PPC_TASKS used in template mode
MERGE INTO ppc_task_panel
USING (
   SELECT DISTINCT
      ppc_task.task_id,
      task_panel.panel_db_id,
      task_panel.panel_id
   FROM
      ppc_plan
      INNER JOIN ppc_wp ON
         ppc_wp.plan_id = ppc_plan.plan_id
      INNER JOIN ppc_activity ON
         ppc_activity.work_package_id = ppc_wp.work_package_id
      INNER JOIN ppc_task ON
         ppc_task.task_id = ppc_activity.activity_id
      INNER JOIN task_task ON
         task_task.task_defn_db_id = ppc_task.task_defn_db_id AND
         task_task.task_defn_id = ppc_task.task_defn_id
      INNER JOIN task_panel ON
         task_panel.task_db_id = task_task.task_db_id AND
         task_panel.task_id = task_task.task_id
   WHERE
      ppc_plan.template_bool=1
      AND
      task_task.task_def_status_db_id = 0 AND
      task_task.task_def_status_cd = 'ACTV'
   ) jic_panels ON (
      jic_panels.task_id = ppc_task_panel.task_id AND
      jic_panels.panel_db_id = ppc_task_panel.panel_db_id AND
      jic_panels.panel_id = ppc_task_panel.panel_id
   ) WHEN NOT MATCHED THEN INSERT (
      task_id,
      panel_db_id,
      panel_id
   ) VALUES (
      jic_panels.task_id,
      jic_panels.panel_db_id,
      jic_panels.panel_id
   );

--changeset OPER-16145:20 stripComments:false
--comment Delete ppc_activity records without an adjoining ppc_task record
DELETE
FROM ppc_activity 
WHERE ppc_activity.ppc_activity_type_cd='TASK' 
AND NOT EXISTS (SELECT * FROM ppc_task WHERE ppc_task.task_id=ppc_activity.activity_id);

--changeset OPER-16145:21 stripComments:false
--comment Clear corrupted original estimate data
UPDATE ppc_wp 
SET nr_orig_est = NULL 
WHERE (work_package_id, plan_id) IN 
(SELECT ppc_wp.work_package_id, ppc_wp.plan_id 
FROM ppc_wp 
INNER JOIN ppc_plan ON 
ppc_plan.plan_id = ppc_wp.plan_id 
WHERE nr_orig_est IS NOT NULL 
AND NOT dbms_lob.instr(nr_orig_est, utl_raw.CAST_TO_RAW('EmptyMap'), 1, 1) > 0); 
