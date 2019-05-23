--liquibase formatted sql


--changeSet OPER-1877:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchVendorCdByVendorCode',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchVendorTypeByVendorCode',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchOrderTypeByOrderTypeStatus',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchOrgCdByOrderTypeStatus',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchStatusByOrderTypeStatus',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchOrgCdByServiceTypeStatus',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchServiceTypeByServiceTypeStatus',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'sVendorSearchStatusByServiceTypeStatus',
 'SESSION',        
 'Vendor search by type parameters.',
 'USER',                    
 'STRING',              
 '',                   
 0,                        
 'Vendor Search By Type',       
 '8.1-SP2',                    
 0                         
);
 
END;
/

--changeSet OPER-1877:9 stripComments:false
-- Vendor Search By Type
INSERT INTO 
        utl_menu_item 
     	(
		MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
      	)
  SELECT 
     	120941,NULL,'web.menuitem.VENDOR_SEARCH_BY_TYPE', '/maintenix/web/vendor/VendorSearchByType.jsp',0,'Vendor Search By Type',0,0
  FROM
	dual
  WHERE NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE MENU_ID = 120941 );