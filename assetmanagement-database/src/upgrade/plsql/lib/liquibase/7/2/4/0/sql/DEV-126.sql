--liquibase formatted sql


--changeSet DEV-126:1 stripComments:false
-- Migration script for 1003 LRP concept: Section 2 (Enable Multiple Planners)
-- Author: Karan Mehta
-- Date  : December 30, 2009
-- Update the READONLY_SEV_DB_ID and READONLY_SEV_CD for all existing plans to use 0HIGHLITE
-- Changed to use 0-level db_id vide. QC-546364
UPDATE
  lrp_plan_config
SET
  readonly_sev_db_id = 0;  

--changeSet DEV-126:2 stripComments:false
UPDATE
  lrp_plan_config
SET
  readonly_sev_cd = 'HIGHLITE';  