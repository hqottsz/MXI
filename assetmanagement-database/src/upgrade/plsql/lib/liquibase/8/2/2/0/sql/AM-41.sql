--liquibase formatted sql


--changeSet AM-41:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTASSIGNEDMANHRSREL');
END;
/

--changeSet AM-41:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTASSIGNEDMANHRSRELTABLE');
END;
/

--changeSet AM-41:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.PACKAGE_DROP('ASSIGNED_MANHOURS_PKG');
END;
/