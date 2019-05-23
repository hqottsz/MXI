--liquibase formatted sql


--changeSet MX-17659:1 stripComments:false
-- update auto_complete_bool for all MPC task definitions and tasks
UPDATE task_task SET auto_complete_bool = 0 WHERE task_class_cd = 'MPC';

--changeSet MX-17659:2 stripComments:false
UPDATE sched_stask SET auto_complete_bool = 0 WHERE task_class_cd = 'MPC';