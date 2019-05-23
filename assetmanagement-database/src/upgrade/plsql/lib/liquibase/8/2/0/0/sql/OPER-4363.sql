--liquibase formatted sql


--changeSet OPER-4363:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'ALLOW_CREATE_REMOTE_TURN_IN_REPREQ_BATCH',
 'LOGIC',        
 'WARNING/ERROR for allowing to create remote REPREQ batch.',
 'USER',                    
 'WARNING/ERROR',              
 'ERROR',                   
 1,                        
 'Supply - Turn Ins',       
 '8.1-SP3',                    
 0                         
);
 
END;
/