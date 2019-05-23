--liquibase formatted sql


--changeSet 0int_event_config:1 stripComments:false
/*************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE INT_EVENT_CONFIG
**************************************************/
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_AIRCRAFT_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:2 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_WORK_PACKAGE_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:3 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_TASK_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:4 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_INVENTORY_DEADLINE_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:5 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_FAULT_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:6 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_PART_REQUEST_MESSAGE_V2_2',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:7 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_INVENTORY_CONDITION_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:8 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_AIRCRAFT_CAPABILITIES_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:9 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_ENGINE_ATTACHED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:10 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_INVENTORY_USAGE_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0int_event_config:11 stripComments:false
INSERT INTO int_event_config (event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES ('MX_RFQ_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');
