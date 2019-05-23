--liquibase formatted sql

--changeSet OPER-23208:1 stripComments:false
INSERT INTO
   utl_config_parm
   (
      parm_value,
      parm_name,
      parm_type,
      parm_desc,
      config_type,
      allow_value_desc,
      default_value,
      mand_config_bool,
      category,
      modified_in,
      utl_id
   )
   SELECT
      null,
      'sOrderSearchPartGroupPurchaseTypeByContact',
      'SESSION',
      'Order search parameters.',
      'USER',
      'String',
      '',
      0,
      'Order Search',
      '8.3',
      0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sOrderSearchPartGroupPurchaseTypeByContact' );