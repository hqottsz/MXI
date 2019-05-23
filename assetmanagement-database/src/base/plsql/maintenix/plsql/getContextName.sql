--liquibase formatted sql


--changeSet getContextName:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**
   This function creates the unique name for a context.  The method takes in the context name, 
   and appends the last 10 digits of the database user id.  If the context already exists, an error is thrown
*/
CREATE OR REPLACE FUNCTION getContextName
(
   aContextName IN VARCHAR2
)
RETURN VARCHAR2
IS
   lUserId NUMBER;
   lReturn VARCHAR2(31);
   context_length EXCEPTION;
BEGIN

   -- if the context name is too long, raise an exception
   IF LENGTH(aContextName) > 20 THEN
      RAISE context_length;
   END IF;

   -- get the user id
   lUserId := SYS_CONTEXT('USERENV', 'SESSION_USERID');

   -- only take the last 10 digits if the number is larger than 10 digits
   IF LENGTH(lUserId) > 10 THEN
      lReturn := aContextName || SUBSTR(lUserId, -10, 10);
   ELSE
      lReturn := aContextName || lUserId;
   END IF;

   RETURN lReturn;

EXCEPTION
   WHEN context_length THEN
      RAISE_APPLICATION_ERROR(-20913, 'Error Creating Context.  The context name is longer than 20 characters: ' || aContextName);
END;
/