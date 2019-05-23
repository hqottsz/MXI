--liquibase formatted sql


--changeSet deleteWorkFlowsForTaskDefn:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure deleteWorkFlowsForTaskDefn(
aTaskDefndbId in number,
aTaskDefnId in number,
aError out number
) is
begin
     --set error to 0
     aError := 0;
     --get all work flows
     FOR work_flow_for_task IN(SELECT task_wf.wf_db_id, task_wf.wf_id
                             FROM task_wf
                             WHERE
                             task_wf.task_db_id = aTaskDefndbId AND
                             task_wf.task_id    = aTaskDefnId
                             )LOOP

     --delete all wf_level
     FOR level_list IN(
                      SELECT wf_step_levels.wf_level_db_id, wf_step_levels.wf_level_id
                      FROM wf_steps, wf_step_levels
                      WHERE
                      wf_steps.wf_step_db_id = wf_step_levels.wf_step_db_id AND
                      wf_steps.wf_step_id    = wf_step_levels.wf_step_id
                      AND
                      wf_steps.wf_db_id = work_flow_for_task.wf_db_id AND
                      wf_steps.wf_id    = work_flow_for_task.wf_id
                      UNION
                      SELECT wf_step_levels.wf_level_db_id, wf_step_levels.wf_level_id
                      FROM wf_steps, wf_step_group, wf_step_levels
                      WHERE
                      wf_steps.wf_step_db_id = wf_step_group.wf_step_db_id AND
                      wf_steps.wf_step_id    = wf_step_group.wf_step_id
                      AND
                      wf_step_group.child_wf_step_db_id = wf_step_levels.wf_step_db_id AND
                      wf_step_group.child_wf_step_id    = wf_step_levels.wf_step_id
                      AND
                      wf_steps.wf_db_id = work_flow_for_task.wf_db_id AND
                      wf_steps.wf_id    = work_flow_for_task.wf_id)LOOP
       DELETE FROM wf_step_levels WHERE
                                  wf_step_levels.wf_level_db_id = level_list.wf_level_db_id AND
                                  wf_step_levels.wf_level_id    = level_list.wf_level_id;

       DELETE FROM wf_level WHERE
                            wf_level.wf_level_db_id = level_list.wf_level_db_id AND
                            wf_level.wf_level_id    = level_list.wf_level_id;
     END LOOP;
     --delete all wf_step_flow
      DELETE FROM wf_step_flow WHERE EXISTS(
            SELECT 1
            FROM wf_steps
            WHERE
            wf_steps.wf_step_db_id = wf_step_flow.wf_step_db_id AND
            wf_steps.wf_step_id    = wf_step_flow.wf_step_id
            AND
            wf_steps.wf_db_id = work_flow_for_task.wf_db_id AND
            wf_steps.wf_id    = work_flow_for_task.wf_id);

     --delete all wf_step_group and the child steps
     FOR child_list IN(SELECT wf_step_group.child_wf_step_db_id, wf_step_group.child_wf_step_id
                       FROM wf_step_group, wf_steps
                       WHERE
                       wf_steps.wf_step_db_id = wf_step_group.wf_step_db_id AND
                       wf_steps.wf_step_id    = wf_step_group.wf_step_id
                       AND
                       wf_steps.wf_db_id = work_flow_for_task.wf_db_id AND
                       wf_steps.wf_id    = work_flow_for_task.wf_id
                       )LOOP
            --delete from group then from step
            DELETE FROM wf_step_group
            WHERE
            wf_step_group.child_wf_step_db_id = child_list.child_wf_step_db_id AND
            wf_step_group.child_wf_step_id    = child_list.child_wf_step_id;

            DELETE FROM wf_step
            WHERE
            wf_step.wf_step_db_id = child_list.child_wf_step_db_id AND
            wf_step.wf_step_id    = child_list.child_wf_step_id;
     END LOOP;
     --delete all wf_steps and then the steps
     FOR step_list IN(SELECT wf_steps.wf_step_db_id, wf_steps.wf_step_id
                      FROM wf_steps
                      WHERE
                      wf_steps.wf_db_id = work_flow_for_task.wf_db_id AND
                      wf_steps.wf_id    = work_flow_for_task.wf_id)LOOP

           DELETE FROM wf_steps WHERE
                                wf_steps.wf_db_id = work_flow_for_task.wf_db_id AND
                                wf_steps.wf_id    = work_flow_for_task.wf_id;
           DELETE FROM wf_step WHERE
                               wf_step.wf_step_db_id = step_list.wf_step_db_id AND
                               wf_step.wf_step_id    = step_list.wf_step_id;
     END LOOP;

     --delete all task_wf
     DELETE FROM task_wf WHERE
                       task_wf.wf_db_id = work_flow_for_task.wf_db_id AND
                       task_wf.wf_id    = work_flow_for_task.wf_id;
     --delete all wf_wf
     DELETE FROM wf_wf WHERE
                       wf_wf.wf_db_id = work_flow_for_task.wf_db_id AND
                       wf_wf.wf_id    = work_flow_for_task.wf_id;

     END LOOP;

     EXCEPTION
     WHEN NO_DATA_FOUND THEN
          ROLLBACK;
     aError:=1;

end deleteWorkFlowsForTaskDefn;
/