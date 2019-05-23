--liquibase formatted sql


--changeSet mx_key_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "MX_KEY_PKG"
IS

/********************************************************************************
*
* Function:       new_uuid
* Returns:        A new UUID value
*
* Description:    This function returns a new UUID value using Oracle’s built-in sys_guid() function.
*
*********************************************************************************
*
* Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
	FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE IS 
	BEGIN
		RETURN SYS_GUID();
	END new_uuid;

END MX_KEY_PKG;
/