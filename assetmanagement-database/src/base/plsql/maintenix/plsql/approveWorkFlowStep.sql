--liquibase formatted sql


--changeSet approveWorkFlowStep:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE approveWorkFlowStep(
   aWFStepDbId number,
   aWFStepId number
)
AS
BEGIN

   -- set the wofkslow step to approved,
   -- and update the workflow level to have an approved date of now.
   UPDATE Wf_Step SET WF_STEP_STATUS_CD='APPROVED', WF_STEP_STATUS_DB_ID=0
   WHERE
      Wf_Step.WF_STEP_DB_ID = aWFStepDbId AND
      Wf_Step.WF_STEP_ID    = aWFStepId;

END approveWorkFlowStep;
/