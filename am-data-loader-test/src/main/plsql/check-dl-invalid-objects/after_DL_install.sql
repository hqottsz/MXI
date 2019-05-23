/*****************************************************************************
*
* Script:       after_DL_uninstall.sql
*
* Last Mod By:  Anton Shlyakhtov
* Last Mod On:  19-Sep-2018
*
* Description:  Create list of invalid database objects after Data Loader install
*               and report on any invalid objects found
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

SPOOL check-dl-invalid-objects.log

-- Recompile invalid objects if any
BEGIN
   upg_utility_v1_pkg.compile_invalid_objects;
END;
/

-- Create list of all database objects after DL install
BEGIN

   EXECUTE IMMEDIATE 
   'INSERT INTO tmp_invalid_objects ' ||
   'SELECT ' ||
      'object_type, '    ||
      'object_name, '    ||
      '2 AS after_bool ' ||
   'FROM ' ||
      'user_objects ' ||
   'WHERE ' ||
      'status = ''INVALID''';

   COMMIT;

   :v_result_cd := 1;

EXCEPTION WHEN OTHERS THEN

   DBMS_OUTPUT.PUT_LINE('Unable to create list of all invalid database objects into table TMP_INVALID_OBJECTS ' || 
      'after installation of Data Loader: ' || SQLERRM);
END;
/


-- Report on invalid database objects before and after Data Loader install
DECLARE

   ln_count         NUMBER;
   TYPE ltyp_cur    IS REF CURSOR;
   lcur_obj         ltyp_cur;
   lv_obj_type      user_objects.object_type%TYPE;
   lv_obj_name      user_objects.object_name%TYPE;
   lv_when_invalid  VARCHAR2(30);
   lv_header        VARCHAR2(90) := RPAD('OBJECT_TYPE', 31, ' ') || RPAD('OBJECT_NAME', 31, ' ') || 'WHEN_BECOME_INVALID';
   lv_divider       VARCHAR2(90) := RPAD('-', 30, '-') || ' ' || RPAD('-', 30, '-') || ' ' || RPAD('-', 27, '-');

BEGIN

   IF :v_result_cd = 1 THEN

      :v_result_cd := 3;

      EXECUTE IMMEDIATE
         'SELECT COUNT(*) FROM tmp_invalid_objects'
      INTO
         ln_count;

      DBMS_OUTPUT.PUT_LINE('-');


      IF ln_count = 0 THEN

         DBMS_OUTPUT.PUT_LINE('SUCCESS: All database objects have been successfully compiled.');

      ELSE

         DBMS_OUTPUT.PUT_LINE('ERROR: The following INVALID database objects found:' || CHR(10));
         DBMS_OUTPUT.PUT_LINE(lv_header);
         DBMS_OUTPUT.PUT_LINE(lv_divider);

         OPEN lcur_obj FOR 'SELECT object_type, object_name, DECODE(after_bool, ' ||
            '1, ''Before'', 2, ''After'', ''Before and after'') || '' DL install'' FROM ' ||
            '(SELECT object_type, object_name, SUM(after_bool) AS after_bool ' ||
            'FROM tmp_invalid_objects GROUP BY object_type, object_name) ORDER BY 1, 2';

         LOOP

            FETCH lcur_obj INTO lv_obj_type, lv_obj_name, lv_when_invalid;
            EXIT WHEN lcur_obj%NOTFOUND;

            DBMS_OUTPUT.ENABLE(1000000);
            DBMS_OUTPUT.PUT_LINE(RPAD(lv_obj_type, 31, ' ') || RPAD(lv_obj_name, 31, ' ') || lv_when_invalid);

         END LOOP;

         CLOSE lcur_obj;

         DBMS_OUTPUT.PUT_LINE(lv_divider || CHR(10));

      END IF;

      :v_result_cd := 1;

   END IF;

EXCEPTION WHEN OTHERS THEN

   DBMS_OUTPUT.PUT_LINE('Unexpected error occurred when reporting on invalid database objects: ' || SQLERRM);

END;
/

SPOOL OFF
EXIT :v_result_cd
