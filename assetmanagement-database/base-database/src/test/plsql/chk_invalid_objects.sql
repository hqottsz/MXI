/********************************************************************************
*
* Script:        chk_invalid_objects.sql
*
* Description:   This script validates that no invalid objects exist after all
*                objects are recompiled. If any invalid objects exist then the
*                script will raise an error to make sure that invalid objects
*                are fixed.
*
*********************************************************************************/

SET SERVEROUTPUT ON SIZE 1000000
SET DEFINE ON
SET LINESIZE 1000
SET WRAP ON
SET TIMING ON

-- Declare the SQL Plus variable for the invalid object list to
-- allow an error to be raised in a separate PLSQL block.
VAR gv_invalid_objects VARCHAR2(2000)

DECLARE

   CURSOR lcur_invalid_objects IS
   -- Build the list of invalid objects
   SELECT
      object_name,
      object_type
   FROM
      user_objects
   WHERE
      object_type IN ( 'PACKAGE', 'PACKAGE BODY', 'FUNCTION', 'PROCEDURE', 'TRIGGER', 'VIEW' ) AND
      status = 'INVALID';

    ln_invalid_count           NUMBER(10);
    lv_previous_object_name    VARCHAR2(30);

BEGIN

   -- Initialize variables
   :gv_invalid_objects := NULL;
   lv_previous_object_name := 'EMPTY';
   ln_invalid_count := 0;

   -- Output a header for the invalid objects
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('CHECKING FOR INVALID OBJECTS');
   DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------------------');

   -- Compile any invalid objects before we check for objects that won't compile
   upg_utility_v1_pkg.compile_invalid_objects;

   -- Loop through the invalid objects
   FOR lrec_invalid_objects IN lcur_invalid_objects
   LOOP

      -- Increment the error count
      ln_invalid_count := ln_invalid_count + 1;

      -- Output the invalid object type and object name
      DBMS_OUTPUT.PUT_LINE('- Invalid ' || lrec_invalid_objects.object_type || ': ' || lrec_invalid_objects.object_name || '.');

      -- Add the object name to the list of invalid objects if it hasn't already been added to the list.
      IF lrec_invalid_objects.object_name <> lv_previous_object_name
      THEN

         -- Add a comma to the invalid object list if this is not the first object
         IF :gv_invalid_objects IS NOT NULL THEN
            :gv_invalid_objects := SUBSTR(:gv_invalid_objects || ', ', 1, 2000);
         END IF;

         -- Add the current object to the invalid object list
         :gv_invalid_objects := SUBSTR(:gv_invalid_objects || LOWER(lrec_invalid_objects.object_name), 1, 2000);

      END IF;

      -- Set the lv_previous_object_name variable to the value of the current object
      lv_previous_object_name := lrec_invalid_objects.object_name;

   END LOOP;

   -- Output the total count of invalid objects
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('INVALID OBJECTS DETECTED: ' || ln_invalid_count);

   IF ln_invalid_count > 0 THEN

      -- If any errors occurred then notify the user that they must fix the invalid objects
      DBMS_OUTPUT.PUT_LINE(CHR(9));
      DBMS_OUTPUT.PUT_LINE('SOLUTION: FIX THE INVALID OBJECTS IDENTIFIED ABOVE.');

   END IF;

END;
/

-- If any errors were detected then raise an Oracle error to fail the build.
-- Include the list of invalid objects in the error message. The detailed list
-- of invalid object types and object names will appear in the log above this error.
--
-- NOTE:  We are raising the error in a separate PLSQL block to make the log
-- easier to read.  Oracle may spool the PLSQL block when the error is raised
-- so if an error is raised in a large block then users may not see that the
-- error details are available above the spooled block.  However, by raising
-- the error in the a separate small PLSQL block then the details output by
-- the previous block will be logged immediatly above the error message.
BEGIN

   -- Check if gv_invalid_objects is not empty
   IF :gv_invalid_objects IS NOT NULL THEN

      -- The gv_invalid_objects is not empty, therefore, raise an error to fail the build
      raise_application_error (-20001,'INVALID OBJECT(S) EXIST IN THE DATABASE AFTER COMPLIATION: ' || :gv_invalid_objects);

   END IF;

END;
/
