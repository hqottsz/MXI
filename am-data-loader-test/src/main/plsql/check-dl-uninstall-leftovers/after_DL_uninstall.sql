/*****************************************************************************
*
* Script:       after_DL_uninstall.sql
*
* Last Mod By:  Anton Shlyakhtov
* Last Mod On:  28-May-2018
*
* Description:  Create list of database objects after Data Loader uninstall,
*               compare it with list of objects captured before DL installation
*               and report on any left-over objects found
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
SET SERVEROUTPUT ON
SET LINESIZE 200
SET TRIMSPOOL ON

-- Define and initialize SQL*PLUS ERRORLEVEL variable 
VAR v_result_cd     NUMBER;
EXEC :v_result_cd := 3;

SPOOL check-dl-uninstall-leftovers.log

-- Create list of all database objects after DL uninstall
BEGIN

   EXECUTE IMMEDIATE 
   'CREATE TABLE tmp_obj_list_2 AS ' ||
   'SELECT ' ||
       'object_type, ' ||
       'object_name '  ||
   'FROM '   ||
       'user_objects ' ||
   'WHERE '  ||
       'object_name NOT IN (''TMP_OBJ_LIST_1'', ''TMP_OBJ_LIST_2'')';

   :v_result_cd := 1;

EXCEPTION WHEN OTHERS THEN

   DBMS_OUTPUT.PUT_LINE('Unable to create list of all database objects in table TMP_OBJ_LIST_2 ' || 
      ' after un-installation of Data Loader: ' || SQLERRM);
END;
/


-- Compare list of database objects before and after Data Loader install/uninstall cycle
DECLARE

   ln_cmp_12      NUMBER;
   ln_cmp_21      NUMBER;
   lv_select      VARCHAR2(100) := 'SELECT object_type, object_name FROM ';
   TYPE ltyp_cur  IS REF CURSOR;
   lcur_obj       ltyp_cur;
   lv_obj_type    user_objects.object_type%TYPE;
   lv_obj_name    user_objects.object_name%TYPE;
   lv_header      VARCHAR2(100) := RPAD('OBJECT_TYPE', 31, ' ') || 'OBJECT_NAME';
   lv_divider     VARCHAR2(100) := RPAD('-', 30, '-') || ' ' || RPAD('-', 30, '-');

BEGIN

   IF :v_result_cd = 1 THEN

      :v_result_cd := 3;

      EXECUTE IMMEDIATE
         'SELECT COUNT(*) FROM (' || lv_select || 'tmp_obj_list_1 MINUS ' || lv_select || 'tmp_obj_list_2)'
      INTO
         ln_cmp_12;

      EXECUTE IMMEDIATE
         'SELECT COUNT(*) FROM (' || lv_select || 'tmp_obj_list_2 MINUS ' || lv_select || 'tmp_obj_list_1)'
      INTO
         ln_cmp_21;


      IF ln_cmp_12 = 0 AND ln_cmp_21 = 0 THEN

         DBMS_OUTPUT.PUT_LINE('SUCCESS: The list of database objects before and after DL install/uninstall is IDENTICAL.');

      ELSIF ln_cmp_12 > 0 THEN

         DBMS_OUTPUT.PUT_LINE('ERROR: As result of DL install/uninstall the following ' || ln_cmp_12 || ' object(s) have been removed:' || CHR(10));
         DBMS_OUTPUT.PUT_LINE(lv_header);
         DBMS_OUTPUT.PUT_LINE(lv_divider);

         OPEN lcur_obj FOR lv_select || 'tmp_obj_list_1 MINUS ' || lv_select || 'tmp_obj_list_2 ORDER BY 1, 2';

         LOOP

            FETCH lcur_obj INTO lv_obj_type, lv_obj_name;
            EXIT WHEN lcur_obj%NOTFOUND;

            DBMS_OUTPUT.ENABLE(1000000);
            DBMS_OUTPUT.PUT_LINE(RPAD(lv_obj_type, 31, ' ') || lv_obj_name);

         END LOOP;

         CLOSE lcur_obj;

         DBMS_OUTPUT.PUT_LINE(lv_divider || CHR(10));

      END IF;

      IF ln_cmp_21 > 0 THEN

         DBMS_OUTPUT.PUT_LINE('ERROR: As result of DL install/uninstall the following ' || ln_cmp_21 || ' left-over object(s) found:' || CHR(10));
         DBMS_OUTPUT.PUT_LINE(lv_header);
         DBMS_OUTPUT.PUT_LINE(lv_divider);

         OPEN lcur_obj FOR lv_select || 'tmp_obj_list_2 MINUS ' || lv_select || 'tmp_obj_list_1 ORDER BY 1, 2';

         LOOP

            FETCH lcur_obj INTO lv_obj_type, lv_obj_name;
            EXIT WHEN lcur_obj%NOTFOUND;

            DBMS_OUTPUT.ENABLE(1000000);
            DBMS_OUTPUT.PUT_LINE(RPAD(lv_obj_type, 31, ' ') || lv_obj_name);

         END LOOP;

         CLOSE lcur_obj;

         DBMS_OUTPUT.PUT_LINE(lv_divider || CHR(10));

      END IF;

      :v_result_cd := 1;

   END IF;

EXCEPTION WHEN OTHERS THEN

   DBMS_OUTPUT.PUT_LINE('Unexpected error occurred when computing difference database objects during DL install/uninstall: ' || SQLERRM);

END;
/

SPOOL OFF
EXIT :v_result_cd
