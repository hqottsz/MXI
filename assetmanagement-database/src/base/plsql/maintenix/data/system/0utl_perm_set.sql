--liquibase formatted sql


--changeSet 0utl_perm_set:1 stripComments:false
INSERT INTO utl_perm_set(id, category, label, description, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_ROLE_EDIT', 'Administration', 'Edit Roles', 'This allows the role to be modified.', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set:2 stripComments:false
INSERT INTO utl_perm_set(id, category, label, description, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_ROLE_VIEW', 'Administration', 'View Roles', 'This allows the role pages to be accessible.', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set:3 stripComments:false
-- OPER-5618
-- Add permission set to wrap the permissions for the Enhanced Part Search feature.
INSERT INTO utl_perm_set(id, category, label, description, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'Maintenance', 'Enhanced Part Search', 'This allows the user to access the enhanced part search feature.', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set:4 stripComments:false
-- end OPER-5618
-- SWA-686
INSERT INTO utl_perm_set(id, category, label, description, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Inbound Integrations', 'Spec 2000', 'Receive Spec 2000 Message', 'Permissions required to receive and process inbound Spec 2000 messages for the AeroBuy Parts Purchasing feature', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set:5 stripComments:false
INSERT INTO utl_perm_set(id, category, label, description, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Outbound Integrations', 'Spec 2000', 'Send Spec 2000 Message', 'Permissions required to generate and send Spec 2000 messages for the AeroBuy Parts Purchasing feature', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');