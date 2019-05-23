--liquibase formatted sql


--changeSet mx_key_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "MX_KEY_PKG" IS

/********************************************************************************
*
* Function:       new_uuid
* Returns:        A new UUID value
*
* Description:    This function returns a new Type 1 UUID value using the
*                 java-uuid-generator library.  This is loaded into Oracle via
*                 the Type1UUIDGenerator.sql script.
*
*********************************************************************************/
FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE;
    
END MX_KEY_PKG;

/