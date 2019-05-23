--liquibase formatted sql


--changeSet MX-16463:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_ADD_MPC_ZONE', 
                    'Permission to add a zone to a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_ADD_MPC_ZONE', 
                    'ACTION_ADD_REQ_ZONE'
                    );
END;
/

--changeSet MX-16463:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_REMOVE_MPC_ZONE', 
                    'Permission to remove a zone from a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_REMOVE_MPC_ZONE', 
                    'ACTION_REMOVE_REQ_ZONE'
                    );
END;
/

--changeSet MX-16463:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_ADD_MPC_PANEL', 
                    'Permission to add a panel to a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_ADD_MPC_PANEL', 
                    'ACTION_ADD_REQ_PANEL'
                    );
END;
/

--changeSet MX-16463:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_REMOVE_MPC_PANEL', 
                    'Permission to remove a panel from a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_REMOVE_MPC_PANEL', 
                    'ACTION_REMOVE_REQ_PANEL'
                    );
END;
/

--changeSet MX-16463:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_ADD_MPC_ATTACHMENT', 
                    'Permission to add an attachment to a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_ADD_MPC_ATTACHMENT', 
                    'ACTION_ADD_REQ_ATTACHMENT'
                    );
END;
/

--changeSet MX-16463:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_REMOVE_MPC_ATTACHMENT', 
                    'Permission to remove an attachment from a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_REMOVE_MPC_ATTACHMENT', 
                    'ACTION_REMOVE_REQ_ATTACHMENT'
                    );
END;
/

--changeSet MX-16463:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_CHANGE_MPC_ATTACHMENT_ORDER', 
                    'Permission to change the order of the attachments on a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_CHANGE_MPC_ATTACHMENT_ORDER', 
                    'ACTION_CHANGE_REQ_ATTACHMENT_ORDER'
                    );
END;
/

--changeSet MX-16463:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_ADD_MPC_TECHNICAL_REFERENCE', 
                    'Permission to add a technical reference to a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_ADD_MPC_TECHNICAL_REFERENCE', 
                    'ACTION_ADD_REQ_TECHNICAL_REFERENCE'
                    );
END;
/

--changeSet MX-16463:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_REMOVE_MPC_TECHNICAL_REFERENCE', 
                    'Permission to remove a technical reference from a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_REMOVE_MPC_TECHNICAL_REFERENCE', 
                    'ACTION_REMOVE_REQ_TECHNICAL_REFERENCE'
                    );
END;
/

--changeSet MX-16463:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_CHANGE_MPC_TECHNICAL_REFERENCE_ORDER', 
                    'Permission to change the order of the technical references on a MPC definition.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint Program - Master Panel Cards',
                    '8.0 alpha',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );

END;
/

--changeSet MX-16463:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Copy userrole parm values from REQ
BEGIN
   utl_migr_data_pkg.action_parm_copy(
                    'ACTION_CHANGE_MPC_TECHNICAL_REFERENCE_ORDER', 
                    'ACTION_CHANGE_REQ_TECHNICAL_REFERENCE_ORDER'
                    );
END;
/