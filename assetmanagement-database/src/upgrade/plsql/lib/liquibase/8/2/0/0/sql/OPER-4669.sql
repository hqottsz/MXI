--liquibase formatted sql


--changeSet OPER-4669:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'FORCE_ENTER_ZERO_QUANTITY_ON_RECEIVE_SHIPMENT',
 'LOGIC',        
 'If TRUE, the user must enter a valid quantity, including zero, for all shipment lines on the Receive Shipment page. If FALSE, Maintenix will auto-populate any empty shipment lines with zero after displaying a warning message.',
 'GLOBAL',                    
 'TRUE/FALSE',              
 'TRUE',                   
 1,                        
 'Supply - Shipments',       
 '8.2-SP1',                    
 0                         
);
 
END;
/