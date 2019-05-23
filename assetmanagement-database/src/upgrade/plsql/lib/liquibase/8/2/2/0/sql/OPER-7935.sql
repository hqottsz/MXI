--liquibase formatted sql


--changeSet OPER-7935:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Add FK columns, FK and Index
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table eqp_part_no add (REPAIR_ORDER_ACCOUNT_DB_ID NUMBER (10))
   ');

   utl_migr_schema_pkg.table_column_add('
      Alter table eqp_part_no add (REPAIR_ORDER_ACCOUNT_ID NUMBER (10))
   ');

   utl_migr_schema_pkg.table_constraint_add('
      Alter table "EQP_PART_NO" add constraint "FK_RO_FNCACCOUNT_EQPPARTNO" foreign key ("REPAIR_ORDER_ACCOUNT_DB_ID","REPAIR_ORDER_ACCOUNT_ID") references "FNC_ACCOUNT" ("ACCOUNT_DB_ID","ACCOUNT_ID") NOT DEFERRABLE
   ');

   utl_migr_schema_pkg.index_create('
      Create Index "IX_RO_FNCACCNT_EQPPRTNO" ON "EQP_PART_NO" ("REPAIR_ORDER_ACCOUNT_DB_ID","REPAIR_ORDER_ACCOUNT_ID")
   ');

END;
/

--changeSet OPER-7935:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Add config parameter
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ALLOW_DEFAULT_PART_NUMBER_REPAIR_ORDER_ACCOUNT',
      'MXWEB',
      'Indicates whether to allow the input of default part number repair account.',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'MXWEB',
      '8.2-SP3',
      0
   );
END;
/