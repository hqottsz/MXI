--liquibase formatted sql


--changeSet approveWorkFlowStepLevel:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE approveWorkFlowStepLevel(
   aWFStepDbId number,
   aWFStepId number,
   aLevelDbId number,
   aLevelId number,
   aHRDbId number,
   aHRId NUMBER
)
AS
BEGIN

   -- set the wofkslow step to approved,
   -- and update the workflow level to have an approved date of now.
   UPDATE wf_step SET wf_step_status_cd='APPROVED', wf_step_status_db_id=0
   WHERE
      wf_step.wf_step_db_id = aWFStepDbId AND
      wf_step.wf_step_id    = aWFStepId;

   UPDATE wf_level SET level_dt = SYSDATE
   WHERE
      wf_level.wf_level_db_id = aLevelDbId  AND
      wf_level.wf_level_id    = aLevelId;

   UPDATE wf_level SET wf_level.level_hr_db_id = aHrDbId, wf_level.level_hr_id = aHrId
   WHERE
      wf_level.wf_level_db_id = aLevelDbId  AND
      wf_level.wf_level_id    = aLevelId;

END approveWorkFlowStepLevel;
/