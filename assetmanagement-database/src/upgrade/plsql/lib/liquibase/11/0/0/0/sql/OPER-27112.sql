--liquibase formatted sql


--changeSet OPER-27112:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  DELETE FROM UTL_CONTEXT WHERE CONTEXT_PACKAGE = 'context_package';
  -- no need to drop the Oracle context as it's already handled.
END;
/

