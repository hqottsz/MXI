--liquibase formatted sql


--changeSet MTX-663:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- LABOUR_SKILL_DB_ID column
/***************************************************************
* Drop the not null constraint for 
* LABOUR_SKILL_DB_ID/LABOUR_SKILL_CD columns
****************************************************************/
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_NN_DROP('ORG_HR_SHIFT_PLAN', 'LABOUR_SKILL_DB_ID');
END;
/

--changeSet MTX-663:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- LABOUR_SKILL_CD column
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_NN_DROP('ORG_HR_SHIFT_PLAN', 'LABOUR_SKILL_CD');
END;
/