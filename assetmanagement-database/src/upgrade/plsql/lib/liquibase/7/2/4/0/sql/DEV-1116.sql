--liquibase formatted sql


--changeSet DEV-1116:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script to remove EQP_ASSMBL_ORG table 
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EQP_ASSMBL_ORG', 'FK_MIMRSTAT_EQPASSMBLORG');
END;
/

--changeSet DEV-1116:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EQP_ASSMBL_ORG', 'FK_EQPASSMBL_EQPASSMBLORG');
END;
/

--changeSet DEV-1116:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EQP_ASSMBL_ORG', 'FK_ORGORG_EQPASSMBLORG');
END;
/

--changeSet DEV-1116:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_EQP_ASSMBL_ORG');
END;
/

--changeSet DEV-1116:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_EQP_ASSMBL_ORG');
END;
/

--changeSet DEV-1116:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('EQP_ASSMBL_ORG');
END;
/