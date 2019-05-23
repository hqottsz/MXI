--liquibase formatted sql


--changeSet MX-20445:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert (
      'sOrderSearchOrgCodeByReceiptOrg', 
      'SESSION',
      'Order search parameters.',
      'USER', 
      'String', 
      '', 
      0, 
      'Order Search', 
      '7.5', 
      0,
      utl_migr_data_pkg.DbTypeCdList( '' )
   );
END;
/