--liquibase formatted sql


--changeSet MX-22307:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX-22307 Work items are not balanced across multiple application servers
-- Version 7200
-- Add configuration parameters, MAX_WORK_ITEMS_SCHEDULED_AT_ONCE
BEGIN
   utl_migr_data_pkg.config_parm_insert(
                    'MAX_WORK_ITEMS_SCHEDULED_AT_ONCE', 
                    'WORK_MANAGER',
                    'This parameter limits the number of work items of a type that can be scheduled at any given iteration of the WorkItemDispatcher.  It is used to allow for better load-balancing of work items across multiple application servers.',
                    'GLOBAL', 
                    'Number', 
                    '50', 
                    1, 
                    'Work Manager Parameter',
                    '7.2 alpha',
                    0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );

END;
/