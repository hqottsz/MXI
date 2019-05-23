--liquibase formatted sql


--changeSet MX-26208:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.PROCEDURE_DROP('CREATEINVADVISORY');
END;
/