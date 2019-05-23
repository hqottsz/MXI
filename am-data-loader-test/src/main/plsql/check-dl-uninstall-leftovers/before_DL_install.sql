/*****************************************************************************
*
* Script:       before_DL_install.sql
*
* Last Mod By:  Anton Shlyakhtov
* Last Mod On:  28-May-2018
*
* Description:  Create list of database objects before Data Loader installation
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

SPOOL check-dl-uninstall-leftovers.log

-- Drop temp tables if exist
DECLARE
   lv_table_name VARCHAR2(30);
BEGIN

   FOR x IN 
   (
      SELECT
         table_name
      FROM
         user_tables
      WHERE
         REGEXP_LIKE(table_name, ' ^TMP_OBJ_LIST_[12]$')
   )
   LOOP

      lv_table_name := x.table_name;
      EXECUTE IMMEDIATE 'DROP TABLE ' || lv_table_name || ' PURGE';

   END LOOP;

   :v_result_cd := 1;
   
EXCEPTION WHEN OTHERS THEN

   DBMS_OUTPUT.PUT_LINE('Unable to drop temp table ' || lv_table_name ||
      ' before installation of Data Loader: ' || SQLERRM);
END;
/


-- Create list of all database objects  
BEGIN

   IF :v_result_cd = 1 THEN

      :v_result_cd := 3;

      EXECUTE IMMEDIATE 
      'CREATE TABLE tmp_obj_list_1 AS ' ||
      'SELECT ' ||
          'object_type, ' ||
          'object_name '  ||
      'FROM '   ||
          'user_objects ' ||
      'WHERE '  ||
          'object_name <> ''TMP_OBJ_LIST_1''';

      :v_result_cd := 1;

   END IF;

EXCEPTION WHEN OTHERS THEN

   DBMS_OUTPUT.PUT_LINE('Unable to create list of all database objects in table TMP_OBJ_LIST_1 ' || 
      ' before installation of Data Loader: ' || SQLERRM);
END;
/

SPOOL OFF
EXIT :v_result_cd
