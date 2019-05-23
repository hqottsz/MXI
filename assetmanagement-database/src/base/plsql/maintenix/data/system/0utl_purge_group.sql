--liquibase formatted sql


--changeSet 0utl_purge_group:1 stripComments:false
/*************** Alerts **************/
INSERT INTO utl_purge_group (purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ALERTS', 'Alerts', 'A set of purging policies related to alerts', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_group:2 stripComments:false
/*************** Integration **************/
INSERT INTO utl_purge_group (purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG', 'Integration Log Purging', 'A set of purging policies related to Integration Log Tables', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_group:3 stripComments:false
/*************** ASB Logging **************/
INSERT INTO UTL_PURGE_GROUP(purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB', 'ASB Integration Purging', 'A set of purging policies related to removing the data from the ASB logging framework', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_group:4 stripComments:false
/*************** ARC **************/
INSERT INTO UTL_PURGE_GROUP(purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ARC', 'ARC Message Records Purging', 'A set of purging policies related to removing the data from the ARC tables', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_group:5 stripComments:false
/*************** Work Items **************/
INSERT INTO utl_purge_group (purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('WORK_ITEM', 'Work Item Purging', 'A set of purging policies related to Work Items', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_group:6 stripComments:false
/*************** Auto Reservation **************/
INSERT INTO utl_purge_group (purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('AUTO_RESERVATION', 'Part Request Auto Reservation Purging', 'A set of purging policies related to Auto Reservation', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_group:7 stripComments:false
/*************** Zipping **************/
INSERT INTO utl_purge_group (purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ZIP_QUEUE', 'Baseline sync zip item Purging', 'A set of purging policies related to Baseline sync zipping', 0, 0, sysdate, sysdate, 0, 'MXI');