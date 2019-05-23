--liquibase formatted sql


--changeSet MX-22804:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX-22804 It is possible to UncollectCollect job cards from a work package after it has been completed
-- the trunk
-- Add configuration parameters, ACTION_COLLECT_JIC_HISTORIC & ACTION_UNCOLLECT_JIC_HISTORIC
BEGIN
   utl_migr_data_pkg.config_parm_insert(
                    'ACTION_COLLECT_JIC_HISTORIC', 
                    'SECURED_RESOURCE',
                    'Permission to mark historic job card as collected',
                    'USER', 
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint - Tasks',
                    '8.0',
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );

END;
/

--changeSet MX-22804:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_copy(
                    'ACTION_COLLECT_JIC_HISTORIC', 
                    'SECURED_RESOURCE',
                    'USER',
                    'ACTION_COLLECT_JIC'
                    );
END;
/

--changeSet MX-22804:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
                    'ACTION_UNCOLLECT_JIC_HISTORIC', 
                    'SECURED_RESOURCE',
                    'Permission to mark historic job card as uncollected',
                    'USER', 
                    'TRUE/FALSE', 
                    'FALSE', 
                    1, 
                    'Maint - Tasks',
                    '8.0',
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );

END;
/

--changeSet MX-22804:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_copy(
                    'ACTION_UNCOLLECT_JIC_HISTORIC', 
                    'SECURED_RESOURCE',
                    'USER',
                    'ACTION_UNCOLLECT_JIC'
                    );
END;
/