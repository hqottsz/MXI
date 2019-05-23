--liquibase formatted sql


--changeSet APP_OBJ_PKG_GETMXIERROR:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure APP_OBJ_PKG_GETMXIERROR (as_error OUT varchar2) is
begin
   as_error := APPLICATION_OBJECT_PKG.GETMXIERROR;
end;
/