--liquibase formatted sql


--changeSet MTX-643:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Global, logic parameter used by the InvalidBomPartException.
****************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT', 
      'LOGIC',
      'Allow part requirements to be scheduled to a fault/task when the config slot of the part is unrelated to the assembly of the fault.',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Tasks - Part Requirements',
      '8.0-SP2',
      0
   );
END;
/