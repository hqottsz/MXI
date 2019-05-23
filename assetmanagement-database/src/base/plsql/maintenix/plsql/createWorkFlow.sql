--liquibase formatted sql


--changeSet createWorkFlow:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure createWorkFlow(
aWfDefnDbId in number,
aWfDefnId in number,
aRefPriorityDbId in number,
aRefPriorityCd in varchar2,
aDueDate in Date,
aWfDbId out number,
aWfId out number,
aError out number)is

--variable required
tTempWf_id     wf_defn.wf_defn_id%TYPE;
tTempStep_id   wf_step.wf_step_id%TYPE;
tStepId        wf_step.wf_step_id%TYPE;
tNextStepId    wf_step.wf_step_id%TYPE;
tLevelId       wf_level.wf_level_id%TYPE;
lNextStepCount NUMBER;
begin
    aError := 0;

    --WorkFlow Creation
    SELECT wf_id.nextval INTO tTempWf_id FROM dual;

    INSERT INTO wf_wf(wf_db_id,wf_id,wf_status_db_id,wf_status_cd,wf_priority_db_id,wf_priority_cd,wf_defn_db_id,wf_defn_id,wf_due_date)
    VALUES(aWfDefnDbid,tTempWf_id,0,'INPROGRESS',aRefPriorityDbId,aRefPriorityCd,aWfDefnDbId,aWfDefnId,aDueDate);

    aWfDbId := aWfDefnDbid;
    aWfId :=  tTempWf_id;
    --Create raw steps
    FOR createSteps IN (SELECT
                         wf_defn_step.wf_defn_step_db_id,
                         wf_defn_step.wf_defn_step_id,
                         wf_level_defn.wf_level_defn_db_id,
                         wf_level_defn.wf_level_defn_id
                        FROM
                         wf_defn_steps,
                         wf_defn_step,
                         (SELECT
                                wf_defn_step_levels.*
                          FROM
                               wf_defn_step_levels,
                               wf_level_defn
                         WHERE
                               wf_defn_step_levels.wf_level_defn_db_id = wf_level_defn.wf_level_defn_db_id AND
                               wf_defn_step_levels.wf_level_defn_id    = wf_level_defn.wf_level_defn_id
                         )wf_level_defn
                         WHERE
                         wf_defn_steps.wf_defn_step_db_id = wf_defn_step.wf_defn_step_db_id AND
                         wf_defn_steps.wf_defn_step_id    = wf_defn_step.wf_defn_step_id
                         AND
                         wf_level_defn.wf_defn_step_db_id (+)= wf_defn_step.wf_defn_step_db_id AND
                         wf_level_defn.wf_defn_step_id    (+)= wf_defn_step.wf_defn_step_id
                         AND
                         wf_defn_steps.wf_defn_db_id = aWfDefnDbId AND
                         wf_defn_steps.wf_defn_id    = aWfDefnId
                       )LOOP

        SELECT wf_step_id.nextval INTO tTempStep_id FROM dual;
        INSERT INTO
wf_step(wf_step_db_id,wf_step_id,wf_step_status_db_id,wf_step_status_cd,wf_defn_step_db_id,wf_defn_step_id,wf_step_reason_db_id,wf_step_reason_cd)
        VALUES(createSteps.wf_defn_step_db_id,tTempStep_id,0,'PENDING',createSteps.wf_defn_step_db_id,createSteps.wf_defn_step_id,0,'NONE');

        INSERT INTO wf_steps(wf_db_id,wf_id,wf_step_db_id,wf_step_id)
        VALUES(aWfDefnDbid,tTempWf_id,createSteps.wf_defn_step_db_id,tTempStep_id);

        -- insert in levels as well
        IF createSteps.wf_level_defn_id IS NOT NULL THEN
           SELECT wf_level_id.nextval INTO tLevelId FROM dual;
           INSERT INTO
wf_level(wf_level_db_id,wf_level_id,wf_level_defn_db_id,wf_level_defn_id)
           VALUES(aWfDefnDbid,tLevelId,createSteps.wf_level_defn_db_id,createSteps.wf_level_defn_id);

           INSERT INTO
wf_step_levels(wf_step_db_id,wf_step_id,wf_level_db_id,wf_level_id)
           VALUES(aWfDefnDbid,tTempStep_id,aWfDefnDbid,tLevelId);
        END IF;
    END LOOP;

  --Create flow
   FOR createFlow IN (SELECT
                            wf_defn_flow.wf_defn_step_db_id,
                            wf_defn_flow.wf_defn_step_id,
                            wf_defn_flow.next_wf_defn_step_db_id,
                            wf_defn_flow.next_wf_defn_step_id
                      FROM
                          wf_defn_flow,
                          wf_defn_steps
                      WHERE
                         wf_defn_steps.wf_defn_step_db_id = wf_defn_flow.wf_defn_step_db_id AND
                         wf_defn_steps.wf_defn_step_id    = wf_defn_flow.wf_defn_step_id
                         AND
                         wf_defn_steps.wf_defn_db_id = aWfDefnDbId AND
                         wf_defn_steps.wf_defn_id    = aWfDefnId
                      )LOOP
    SELECT
    wf_step.wf_step_id INTO tStepId
    FROM
    wf_step, wf_steps
    WHERE
    wf_step.wf_step_db_id = wf_steps.wf_step_db_id AND
    wf_step.wf_step_id    = wf_steps.wf_step_id
    AND
    wf_steps.wf_db_id = aWfDefnDbid AND
    wf_steps.wf_id    = tTempWf_id
    AND
    wf_step.wf_defn_step_db_id = createFlow.wf_defn_step_db_id AND
    wf_step.wf_defn_step_id    = createFlow.wf_defn_step_id ;

    SELECT
    wf_step.wf_step_id INTO tNextStepId
    FROM
    wf_step, wf_steps
    WHERE
    wf_step.wf_step_db_id = wf_steps.wf_step_db_id AND
    wf_step.wf_step_id    = wf_steps.wf_step_id
    AND
    wf_steps.wf_db_id = aWfDefnDbid AND
    wf_steps.wf_id    = tTempWf_id
    AND
    wf_step.wf_defn_step_db_id = createFlow.next_wf_defn_step_db_id AND
    wf_step.wf_defn_step_id    = createFlow.next_wf_defn_step_id ;

    INSERT INTO
wf_step_flow(wf_step_db_id,wf_step_id,next_wf_step_db_id,next_wf_step_id)
    values(aWfDefnDbId, tStepId, aWfDefnDbId, tNextStepId);
 END LOOP;

 --Create Parent-child relation

 FOR createRelation IN (SELECT
                         wf_defn_step.wf_defn_step_db_id,
                         wf_defn_step.wf_defn_step_id,
                         wf_defn_step_group.child_wf_defn_step_db_id,
                         wf_defn_step_group.child_wf_defn_step_id,
                         wf_level_defn_child.wf_level_defn_db_id,
                         wf_level_defn_child.wf_level_defn_id
                       FROM
                         wf_defn_steps,
                         wf_defn_step,
                         wf_defn_step_group,
                         (SELECT
                                 wf_defn_step_levels.*
                          FROM
                               wf_defn_step_levels,
                               wf_level_defn
                         WHERE
                               wf_defn_step_levels.wf_level_defn_db_id = wf_level_defn.wf_level_defn_db_id AND
                               wf_defn_step_levels.wf_level_defn_id    = wf_level_defn.wf_level_defn_id
                         )wf_level_defn_child
                         WHERE
                         wf_defn_steps.wf_defn_step_db_id = wf_defn_step.wf_defn_step_db_id AND
                         wf_defn_steps.wf_defn_step_id    = wf_defn_step.wf_defn_step_id
                         AND
                         wf_defn_step_group.wf_defn_step_db_id (+)= wf_defn_step.wf_defn_step_db_id AND
                         wf_defn_step_group.wf_defn_step_id    (+)= wf_defn_step.wf_defn_step_id
                         AND
                         wf_level_defn_child.wf_defn_step_db_id (+)= wf_defn_step_group.child_wf_defn_step_db_id AND
                         wf_level_defn_child.wf_defn_step_id    (+)= wf_defn_step_group.child_wf_defn_step_id
                         AND
                         wf_defn_step_group.child_wf_defn_step_id IS NOT NULL
                         AND
                         wf_defn_steps.wf_defn_db_id = aWfDefnDbId AND
                         wf_defn_steps.wf_defn_id    = aWfDefnId
                      )LOOP
    SELECT
    wf_step.wf_step_id INTO tStepId
    FROM
    wf_step, wf_steps
    WHERE
    wf_step.wf_step_db_id = wf_steps.wf_step_db_id AND
    wf_step.wf_step_id    = wf_steps.wf_step_id
    AND
    wf_steps.wf_db_id = aWfDefnDbid AND
    wf_steps.wf_id    = tTempWf_id
    AND
    wf_step.wf_defn_step_db_id = createRelation.wf_defn_step_db_id AND
    wf_step.wf_defn_step_id    = createRelation.wf_defn_step_id ;

    --missing steps
    SELECT wf_step_id.nextval INTO tTempStep_id FROM dual;
    INSERT INTO
wf_step(wf_step_db_id,wf_step_id,wf_step_status_db_id,wf_step_status_cd,wf_defn_step_db_id,wf_defn_step_id,wf_step_reason_db_id,wf_step_reason_cd)
    VALUES(createRelation.wf_defn_step_db_id,tTempStep_id,0,'PENDING',createRelation.child_wf_defn_step_db_id,createRelation.child_wf_defn_step_id,0,'NONE');

    INSERT INTO
wf_step_group(wf_step_db_id,wf_step_id,child_wf_step_db_id,child_wf_step_id)
    values(aWfDefnDbId, tStepId, aWfDefnDbId, tTempStep_id);

    --Assuming only 1 level depth
    SELECT wf_level_id.nextval INTO tLevelId FROM dual;
    INSERT INTO
wf_level(wf_level_db_id,wf_level_id,wf_level_defn_db_id,wf_level_defn_id)
    VALUES(aWfDefnDbid,tLevelId,createRelation.wf_level_defn_db_id,createRelation.wf_level_defn_id);

    INSERT INTO
wf_step_levels(wf_step_db_id,wf_step_id,wf_level_db_id,wf_level_id)
    VALUES(aWfDefnDbid,tTempStep_id,aWfDefnDbid,tLevelId);
 END LOOP;

   -- If there are no "next step", thi is a single step workflow
   SELECT
      COUNT(wf_step_flow.wf_step_id)
   INTO
      lNextStepCount
   FROM
      wf_wf,
      wf_steps,
      wf_step_flow,
      wf_step_flow next_step
   WHERE
      wf_wf.wf_db_id = aWfDbId AND
      wf_wf.wf_id    = aWfId
      AND
      wf_wf.wf_db_id = wf_steps.wf_db_id AND
      wf_wf.wf_id    = wf_steps.wf_id
      AND
      wf_step_flow.wf_step_db_id = wf_steps.wf_step_db_id AND
      wf_step_flow.wf_step_id    = wf_steps.wf_step_id
      AND
      wf_step_flow.wf_step_db_id = next_step.next_wf_step_db_id(+) AND
      wf_step_flow.wf_step_id    = next_step.next_wf_step_id(+)
      AND
      next_step.wf_step_id IS NULL;

   IF lNextStepCount = 0 THEN
      -- If there is only one step, we don't want to use wf_step_flow table
      SELECT
         wf_steps.wf_step_id
      INTO
         tTempStep_id
      FROM
         wf_wf,
         wf_steps
      WHERE
         wf_wf.wf_db_id = aWfDbId AND
         wf_wf.wf_id    = aWfId
         AND
         wf_wf.wf_db_id = wf_steps.wf_db_id AND
         wf_wf.wf_id    = wf_steps.wf_id;

       UPDATE wf_step set wf_step.wf_step_status_cd = 'INPROGRESS' WHERE wf_step.wf_step_id = tTempStep_id;


      --if the step is root then set the same for their childs as well
      FOR lookForChilds IN (SELECT
				wf_step_group.child_wf_step_id
			    FROM
				wf_step_group
			    WHERE
				wf_step_group.wf_step_id = tTempStep_id
			    )LOOP

        UPDATE wf_step SET wf_step.wf_step_status_cd = 'INPROGRESS' WHERE wf_step.wf_step_id = lookForChilds.child_wf_step_id;
       END LOOP;

   ELSE
      -- If there are more than one step, use the wf_step_flow table
      SELECT
         wf_step_flow.wf_step_id
      INTO
         tTempStep_id
      FROM
         wf_wf,
         wf_steps,
         wf_step_flow,
         wf_step_flow next_step
      WHERE
         wf_wf.wf_db_id = aWfDbId AND
         wf_wf.wf_id    = aWfId
         AND
         wf_wf.wf_db_id = wf_steps.wf_db_id AND
         wf_wf.wf_id    = wf_steps.wf_id
         AND
         wf_step_flow.wf_step_db_id = wf_steps.wf_step_db_id AND
         wf_step_flow.wf_step_id    = wf_steps.wf_step_id
         AND
         wf_step_flow.wf_step_db_id = next_step.next_wf_step_db_id(+) AND
         wf_step_flow.wf_step_id    = next_step.next_wf_step_id(+)
         AND
         next_step.wf_step_id IS NULL;
       UPDATE wf_step set wf_step.wf_step_status_cd = 'INPROGRESS' WHERE wf_step.wf_step_id = tTempStep_id;

       --if the step is root then set the same for their childs as well
       FOR lookForChilds IN (SELECT
                                 wf_step_group.child_wf_step_id
                             FROM
                                 wf_step_group
                             WHERE
                                 wf_step_group.wf_step_id = tTempStep_id
                             )LOOP

                             UPDATE wf_step SET wf_step.wf_step_status_cd = 'INPROGRESS' WHERE wf_step.wf_step_id = lookForChilds.child_wf_step_id;
       END LOOP;
    END IF;

EXCEPTION
WHEN NO_DATA_FOUND THEN
ROLLBACK;
aError:=1;

end
createWorkFlow;
/