--liquibase formatted sql


--changeSet DEV-82:1 stripComments:false
-- Migration script for 7.1 Quantas Shops.
-- Author: Rangarajan Sundararajan
-- Date  : January 26, 2010
-- Set enforce work scope bool for all existing requirements to false.
UPDATE task_task SET task_task.enforce_workscope_bool = 0;

--changeSet DEV-82:2 stripComments:false
-- Set enfoce work scope bool for all existing work packages to false.
UPDATE sched_wp SET sched_wp.enforce_workscope_bool =0;

--changeSet DEV-82:3 stripComments:false
-- Set workscope order for all tasks in a work package to null.
UPDATE sched_wo_line SET sched_wo_line.workscope_order = NULL;