--liquibase formatted sql


--changeSet MTX-852-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
*********************************************************************************
*
* Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE;

END MX_KEY_PKG;
/

--changeSet MTX-852-2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "MX_KEY_PKG"
IS

/********************************************************************************
*
* Function:       new_uuid
* Returns:        A new UUID value
*
* Description:    This function returns a new Type 1 UUID value using the
*                 java-uuid-generator library.  This is loaded into Oracle via
*                 the Type1UUIDGenerator.sql script.
*
*********************************************************************************
*
* Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
	FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE AS
      LANGUAGE JAVA NAME 'com.mxi.uuid.Type1UUIDGenerator.generate() return byte[]';

END MX_KEY_PKG;
/

--changeSet MTX-852-2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE

   JAVA_SESSION_CLEARED EXCEPTION;
   PRAGMA EXCEPTION_INIT(JAVA_SESSION_CLEARED, -29549);
   
   ln_keep_tries  NUMBER := 3;
   lv_tmp        VARCHAR2(32000); 
   lraw_uuid     RAW(16);
  
BEGIN
     
   <<BEFORE_JAVA_CALL>>
   BEGIN
     lraw_uuid := mx_key_pkg.new_uuid;
   
   EXCEPTION 
     WHEN JAVA_SESSION_CLEARED THEN
       
       lv_tmp := DBMS_JAVA.ENDSESSION_AND_RELATED_STATE;
       ln_keep_tries := ln_keep_tries - 1;
       
       IF ln_keep_tries >= 0 THEN 
         GOTO BEFORE_JAVA_CALL;
       ELSE
          RAISE;
       END IF; 
       
    WHEN OTHERS THEN
      RAISE;      
   END;
   
END;  
/