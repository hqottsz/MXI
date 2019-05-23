--liquibase formatted sql


--changeSet OPER-4193_1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new configuration parameter for determining the overage of a shelf life percentage remaining
****************************************************************/
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'PERCENT_SHELF_LIFE_OVERAGE_WARNING', 
 'LOGIC',       
 'Allowable percentage of shelf life remaining when receiving new inventory items or picking the inventory item for kit. If the percentage of shelf life remaining is greater than this percentage it is displayed in red to warn the user.',
 'GLOBAL',                    
 'Number',              
 '',                   
 1,                         
 'SYSTEM',         
 '8.2-SP1',                    
 0                        
);
 
END;

/