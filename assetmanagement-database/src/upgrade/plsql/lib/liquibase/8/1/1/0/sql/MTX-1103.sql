--liquibase formatted sql


--changeSet MTX-1103:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'INSTALLED_INVENTORY_NOT_APPLICABLE', 
 'LOGIC',       
 'Specifies the severity of the validation for any non-applicable installed inventory. If the specified applicability code does not apply to the installed inventory, the code will either be processed without a warning, processed with a warning, or not processed with an error. Allowable values are OK, WARNING, ERROR (case sensitive)',
 'USER',                    
 'WARNING/ERROR/OK',              
 'ERROR',                   
 1,                         
 'Core Logic',         
 '8.1-sp2',                    
 0                        
);
 
END;
/