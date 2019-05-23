--liquibase formatted sql
--Add the param API_INVOICE_PARM
--Copy the role permissions from READ_INVOICE and WRITE_INVOICE
--Add all permission sets of READ_INVOICE and WRITE_INVOICE into API_INVOICE_PARM
--Delete the READ_INVOICE and WRITE_INVOICE permission set
--Delete the READ_INVOICE and WRITE_INVOICE

--changeSet OPER-28259:1 stripComments:false
INSERT INTO
  utl_action_config_parm
  (
    parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
  SELECT
    'API_INVOICE_PARM', 'FALSE',  0, 'Permission to access the Invoice API.' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.3-SP1', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_INVOICE_PARM' );

--changeSet OPER-28259:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--copy all permission of READ_INVOICE into API_INVOICE_PARM
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'API_INVOICE_PARM',
      'READ_INVOICE'
   );
END;
/

--changeSet OPER-28259:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--copy all permission of WRITE_INVOICE into API_INVOICE_PARM
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'API_INVOICE_PARM',
      'WRITE_INVOICE'
   );
END;
/

--changeSet OPER-28259:4 stripComments:false
--replace READ_INVOICE entries with API_INVOICE_PARM but where we need to check for primary key violation because
--in case any perm_set_id exists for API_INVOICE_PARM
UPDATE
   utl_perm_set_action_parm
SET
   parm_name = 'API_INVOICE_PARM'
WHERE
   parm_name = 'READ_INVOICE' AND
   perm_set_id NOT IN
   (
      SELECT
         old_perm_set.perm_set_id
      FROM
         utl_perm_set_action_parm old_perm_set
      WHERE
         old_perm_set.parm_name = 'API_INVOICE_PARM'
   );


--changeSet OPER-28259:5 stripComments:false
--replace WRITE_INVOICE entries with API_INVOICE_PARM but where we need to check for primary key violation because
--in case any perm_set_id exists for API_INVOICE_PARM
UPDATE
   utl_perm_set_action_parm
SET
   parm_name = 'API_INVOICE_PARM'
WHERE
   parm_name = 'WRITE_INVOICE' AND
   perm_set_id NOT IN
   (
      SELECT
         old_perm_set.perm_set_id
      FROM
         utl_perm_set_action_parm old_perm_set
      WHERE
         old_perm_set.parm_name = 'API_INVOICE_PARM'
   );

--changeSet OPER-28259:6 stripComments:false
--delete any entry for READ_INVOICE but we will not lose any existing data since we already updated to API_INVOICE_PARM above
DELETE FROM utl_perm_set_action_parm
WHERE parm_name = 'READ_INVOICE';

--changeSet OPER-28259:7 stripComments:false
--delete any entry for WRITE_INVOICE but we will not lose any existing data since we already updated to API_INVOICE_PARM above
DELETE FROM utl_perm_set_action_parm
WHERE parm_name = 'WRITE_INVOICE';

--changeSet OPER-28259:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_delete('READ_INVOICE');
   utl_migr_data_pkg.action_parm_delete('WRITE_INVOICE');
END;
/