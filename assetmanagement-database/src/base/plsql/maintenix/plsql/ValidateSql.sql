--liquibase formatted sql


--changeSet ValidateSql:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure VALIDATESQL(
               as_Sql IN utl_rule.rule_sql%TYPE,
               an_Return OUT NUMBER)
IS
   ln_CursorHandle   NUMBER;  /* handle to the dynamic SQL cursor */
   li_DbmsReturn     INTEGER; /* needed to run the DBMS functions */
BEGIN

   /* if there is an applicability clause, then run the query to determine
      if the rule is valid applicable */
   IF as_Sql IS NOT NULL THEN

      /* Attempt to execute the SQL statement. If the SQL runs without
         error, then the applicability rule is valid. If there is an error,
         then the applicability rule is not valid.  */

      /* open dynamic cursor */
      ln_CursorHandle := DBMS_SQL.OPEN_CURSOR;

      /* parse and define variables for the SQL statement */
      DBMS_SQL.PARSE(ln_CursorHandle, as_Sql, DBMS_SQL.V7);

      /* excute and fetch the dynamic SQL */
      li_DbmsReturn := DBMS_SQL.EXECUTE(ln_CursorHandle);
      li_DbmsReturn := DBMS_SQL.FETCH_ROWS(ln_CursorHandle);

      /* close dynamic cursor */
      DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle);

   ELSE
      an_Return := -1;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'VALIDATESQL');
   END IF;

   /* set return success */
   an_Return := 1;

EXCEPTION
   WHEN OTHERS THEN
     an_Return := -1;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'VALIDATESQL');
     IF DBMS_SQL.IS_OPEN(ln_CursorHandle) THEN DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle); END IF;
END VALIDATESQL;
/