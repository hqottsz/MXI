/*****************************************************************************
*
* Script:       before_DL_install.sql
*
* Last Mod By:  Anton Shlyakhtov
* Last Mod On:  19-Sep-2018
*
* Description:  Create list of invalid objects before Data Loader installation
* 
*******************************************************************************
*
* Confidential, proprietary and/or trade secret information of IFS.
* Copyright 2018 IFS. All Rights Reserved.
*
******************************************************************************/

-- Catch SQLERROR and OSERROR
WHENEVER SQLERROR EXIT 3
WHENEVER OSERROR EXIT 3

SET FEEDBACK OFF
SET ECHO OFF

-- Define and initialize SQL*PLUS ERRORLEVEL variable 
VAR v_result_cd     NUMBER;
EXEC :v_result_cd := 3;

SPOOL check-dl-invalid-objects.log

-- Drop temp table if exist
BEGIN

   EXECUTE IMMEDIATE 'DROP TABLE tmp_invalid_objects';
   
EXCEPTION WHEN OTHERS THEN
   NULL;
END;
/

-- Recompile invalid objects if any
BEGIN
   upg_utility_v1_pkg.compile_invalid_objects;
END;
/

-- Create list of invalid database objects  
BEGIN

   EXECUTE IMMEDIATE 
   'CREATE TABLE tmp_invalid_objects ' ||
   'AS '     ||
   'SELECT ' ||
      'object_type, '    ||
      'object_name, '    ||
      '1 AS after_bool ' ||
   'FROM ' ||
      'user_objects ' ||
   'WHERE ' ||
      'status = ''INVALID''';

   :v_result_cd := 1;

EXCEPTION WHEN OTHERS THEN

   DBMS_OUTPUT.PUT_LINE('Unable to create list of all invalid core database objects into table TMP_INVALID_OBJECTS ' || 
      ' before installation of Data Loader: ' || SQLERRM);
END;
/

SPOOL OFF
EXIT :v_result_cd
