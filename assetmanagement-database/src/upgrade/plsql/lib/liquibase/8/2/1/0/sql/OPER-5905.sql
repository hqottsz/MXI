--liquibase formatted sql


--changeSet OPER-5905:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Adding a config parm for controlling the editing of the receive date on a shipment 
BEGIN
 utl_migr_data_pkg.config_parm_insert(
 'ALLOW_EDIT_RECEIVE_DATE_ON_RECEIVE_SHIPMENT',
 'LOGIC',        
 'Controls if the received date on the receive shipment page can be modified by the user or not. If the parameter is TRUE, the user can select a different received date. If it is FALSE, the user cannot modify the received date and the system default date and time applies.',
 'GLOBAL',                    
 'TRUE/FALSE',              
 'TRUE',                   
 1,                        
 'Supply - Shipments',       
 '8.2-SP2',                    
 0                         
);
 
END;
/