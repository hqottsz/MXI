--liquibase formatted sql


--changeSet MX-26205:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('ISCURRENTUSERINLABOURORGTREE');
END;
/