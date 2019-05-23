--liquibase formatted sql

--changeSet OPER-29663:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "MX_KEY_PKG" IS

  /**
   * Public version to create a UUID
   */
  FUNCTION new_uuid RETURN RAW
    PARALLEL_ENABLE IS
  BEGIN
    return sys_guid();
  END;

END MX_KEY_PKG;
/