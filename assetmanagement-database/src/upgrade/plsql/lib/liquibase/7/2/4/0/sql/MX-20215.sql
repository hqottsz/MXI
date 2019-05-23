--liquibase formatted sql


--changeSet MX-20215:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Insert into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_ADD_USER_ATTACHMENT', 
                    'Permission to add attachment for other user.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'HR - Users',
                    '8.0',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );
END;
/

--changeSet MX-20215:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_REMOVE_USER_ATTACHMENT', 
                    'Permission to remove attachment for other user.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'HR - Users',
                    '8.0',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );
END;
/

--changeSet MX-20215:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_OPEN_USER_ATTACHMENT', 
                    'Permission to open attachment for other user.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'HR - Users',
                    '8.0',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );
END;
/

--changeSet MX-20215:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_ADD_MY_USER_ATTACHMENT', 
                    'Permission to add my own attachment.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'HR - Users',
                    '8.0',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );
END;
/

--changeSet MX-20215:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_REMOVE_MY_USER_ATTACHMENT', 
                    'Permission to remove my own attachment.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'HR - Users',
                    '8.0',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );
END;
/

--changeSet MX-20215:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_OPEN_MY_USER_ATTACHMENT', 
                    'Permission to open my own attachment.',
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'HR - Users',
                    '8.0',
                    0,
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                    );
END;
/