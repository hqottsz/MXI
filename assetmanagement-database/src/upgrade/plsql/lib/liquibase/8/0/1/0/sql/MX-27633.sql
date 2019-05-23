--liquibase formatted sql


--changeSet MX-27633:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      ISPINNED
* Arguments:     
* Description:   This function returns whether or not a service check is 
*                considered pinned according to Line Planning Automation rules.
*                
*********************************************************************************
*
* Confidential, proprietary and/or trade secret information of Mxi Technologies,
*    Ltd. Copyright 2013 Mxi Technologies, Ltd.  All Rights Reserved.
* Except as expressly provided by written license signed by a duly appointed 
*   officer of Mxi Technologies, Ltd., any disclosure, distribution, reproduction, 
*   compilation, modification, creation of derivative works and/or other use of 
*   the Mxi source code is strictly prohibited. 
* Inclusion of a copyright notice shall not be taken to indicate that the source
*   code has been published.
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION ISPINNED
(
    aLPADisabledWP   sched_stask.prevent_lpa_bool%TYPE,
    aWpStatusDbId    evt_event.event_status_db_id%TYPE,
    aWpStatusCd      evt_event.event_status_cd%TYPE,
    aTaskStatusDbId  evt_event.event_status_db_id%TYPE,
    aTaskStatusCd    evt_event.event_status_cd%TYPE,
    aLPADisabledAcft inv_ac_reg.prevent_lpa_bool%TYPE,
    aHeavyBool       sched_stask.heavy_bool%TYPE,
    aLrpWpBool       sched_stask.lrp_bool%TYPE,
    aLrpTaskBool     sched_stask.lrp_bool%TYPE
)
  RETURN NUMBER IS
BEGIN

  IF
    -- LPA Disabled WPs
    (aLPADisabledWP = 1) OR
    -- LPA Disabled Aircraft
    (aLPADisabledAcft = 1) OR
    -- 'COMMIT' or 'IN WORK' WPs
    ((aWpStatusDbId = 0) AND (aWpStatusCd IN ('COMMIT', 'IN WORK'))) OR
    -- 'IN WORK' tasks
    ((aTaskStatusDbId = 0) AND (aTaskStatusCd = 'IN WORK')) OR
    -- heavy maintenance
    (aHeavyBool = 1) OR
    -- LRP Work Package
    (aLrpWpBool = 1) OR
    -- LRP Task
    (aLrpTaskBool = 1)
  THEN
    RETURN 1;
  END IF;
  
  RETURN 0;

END ISPINNED;
/