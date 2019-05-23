--liquibase formatted sql

--changeSet OPER-30102:1 stripComments:false
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_AXON_FRAMEWORK_FAILSAFE',
      'LOGIC',
      'This parameter disables event publication for axon. This is to be used in extreme circumstances only.',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Axon Framework',
      '8.3-SP2',
      '0'
   );

END;
/