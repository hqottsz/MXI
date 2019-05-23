--liquibase formatted sql


--changeSet 0utl_role_perm_set:1 stripComments:false
/****************************************** 
** 0-Level INSERT SCRIPT FOR UTL_ROLE_PERM_SET
*******************************************/
INSERT INTO utl_role_perm_set(role_id, perm_set_id, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES(17000, 'Spec 2000 PO Inbound Integrations', 0, 1, 0, sysdate, 0, sysdate, 0,'MXI');

--changeSet 0utl_role_perm_set:2 stripComments:false
INSERT INTO utl_role_perm_set(role_id, perm_set_id, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES(17000, 'Spec 2000 PO Outbound Integrations', 0, 1, 0, sysdate, 0, sysdate, 0,'MXI');