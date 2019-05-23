--liquibase formatted sql

--changeSet OPER-30754:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "MX_KEY_PKG" IS
/********************************************************************************
*
* Function:       new_uuid
* Returns:        A new UUID value
*
* Description:    This function returns a new UUID
*
*********************************************************************************/
FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE;
    
END MX_KEY_PKG;
/

--changeSet OPER-30754:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "MX_KEY_PKG" IS
  FUNCTION new_uuid RETURN RAW
    PARALLEL_ENABLE IS
  BEGIN
    return sys_guid();
  END;
END MX_KEY_PKG;
/