--liquibase formatted sql


--changeSet getWorkFlowRootStep:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE getWorkFlowRootStep(
   aWfStepDbId in number,
   aWfStepId in number,
   aWfRootStepDbId out number,
   aWfRootStepId out number
)
IS
lParentStep   wf_step_group%ROWTYPE;
lChildStep   wf_step_group%ROWTYPE;
lRootStep   number(1);


BEGIN


   -- start by checking to see if this step is a root
   SELECT count(*) INTO lRootStep
   FROM
      wf_steps
   WHERE
      wf_steps.wf_step_db_id = aWfStepDbId AND
      wf_steps.wf_step_id    = aWfStepId;

   IF(lRootStep > 0) THEN
      aWfRootStepDbId := aWfStepDbId;
      aWfRootStepId   := aWfStepId;
      return;
   END IF;

   -- ok so it's not a root node, lets go and find one!
   -- get the parent row for what's pointing to child.
   SELECT * INTO lParentStep
   FROM
      wf_step_group
   WHERE
      wf_step_group.child_wf_step_db_id = aWfStepDbId AND
      wf_step_group.child_wf_step_id = aWfStepId;

   WHILE NVL(lParentStep.Wf_Step_Id, 0) > 0 LOOP
      -- need to get next parent!
      lChildStep := lParentStep;

      -- check to see if this is a root step
      SELECT count(*) INTO lRootStep
      FROM
         wf_steps
      WHERE
         wf_steps.wf_step_db_id = lChildStep.Wf_Step_Db_Id AND
         wf_steps.wf_step_id    = lChildStep.Wf_Step_Id;

      IF(lRootStep > 0) THEN
         aWfRootStepDbId := lChildStep.Wf_Step_Db_Id;
         aWfRootStepId   := lChildStep.Wf_Step_Id;
         return;
      END IF;

      -- check to see if there is another parent!
      SELECT * INTO lParentStep
      FROM
         wf_step_group
      WHERE
         wf_step_group.child_wf_step_db_id = lChildStep.Wf_Step_Db_Id AND
         wf_step_group.child_wf_step_id = lChildStep.Wf_Step_Id;

   END LOOP;

   -- we should have the parent step now as the lChildStep;


END getWorkFlowRootStep;
/