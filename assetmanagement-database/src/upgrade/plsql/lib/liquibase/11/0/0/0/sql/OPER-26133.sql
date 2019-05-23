--liquibase formatted sql


--changeSet OPER-26133:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER tubr_inv_loc DISABLE';
END;
/

--changeSet OPER-26133:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add(
     'ALTER TABLE inv_loc ADD AUTO_ISSUE_BOOL NUMBER (1) DEFAULT 0 NOT NULL DEFERRABLE'
   );
END;
/

--changeSet OPER-26133:3 stripComments:false
 COMMENT ON COLUMN INV_LOC.AUTO_ISSUE_BOOL
IS
  'Identifies whether a location is enabled for auto issue of parts. This feature can only be enabled  for Vendor Line (ref_loc_type.loc_type_cd = VENLINE)and Vendor Track (ref_loc_type.loc_type_cd = VENTRK) locations.' ;

--changeSet OPER-26133:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER tubr_inv_loc ENABLE';
END;
/
