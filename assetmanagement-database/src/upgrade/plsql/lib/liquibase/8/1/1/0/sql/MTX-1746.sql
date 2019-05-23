--liquibase formatted sql


--changeSet MTX-1746:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************
* Add ATTACH_INVENTORY_FROM_NON_ACFT_LOCATION config parm
********************************************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ATTACH_INVENTORY_FROM_DIFFERENT_LOCATION', 
      'LOGIC',
      'If set to true it allows inventory to be attached and its parent inventory to be in different locations.',
      'USER',
      'TRUE/FALSE', 	  
      'TRUE',  
      1, 
      'Core Logic', 
      '8.1-SP2',  
      0     
   );
END;
/