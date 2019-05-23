--liquibase formatted sql


--changeSet DEV-135:1 stripComments:false
-- Migration script for 1003 LRP concept: Section 2 (Enable Multiple Planners)
-- Author: Karan Mehta
-- Date  : January 7, 2010
-- Migrate data in the LRP_EVENT table to set the value of the new column 'TYPE'
-- Logic:
-- 1) If there is no associated aircraft, then it is an ad hoc location work package - LOCATION.
-- 2) If there is no workscope (i.e. no tasks assigned), then it is an ad hoc aircraft work package - AIRCRAFT.
-- 3) If others (where there is an aircraft, and at least one assigned task) - WORK_EVENT
-- 1)
UPDATE
  lrp_event
SET
  lrp_event.type = 'LOCATION'
WHERE
  lrp_event.lrp_inv_inv_id IS NULL;  

--changeSet DEV-135:2 stripComments:false
-- 2)
UPDATE
  lrp_event
SET
  lrp_event.type = 'AIRCRAFT'
WHERE
  NOT EXISTS
    (
      SELECT
        *
      FROM
        lrp_event_workscope
      WHERE
        lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
        lrp_event.lrp_event_id = lrp_event_workscope.lrp_event_id
    );

--changeSet DEV-135:3 stripComments:false
-- 3)    
UPDATE
  lrp_event
SET
  lrp_event.type = 'WORK_EVENT'
WHERE
  lrp_event.lrp_inv_inv_id IS NOT NULL
  AND
  EXISTS
    (
      SELECT
        *
      FROM
        lrp_event_workscope
      WHERE
        lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
        lrp_event.lrp_event_id = lrp_event_workscope.lrp_event_id    
    );       