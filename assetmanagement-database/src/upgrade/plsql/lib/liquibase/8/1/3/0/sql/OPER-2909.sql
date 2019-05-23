--liquibase formatted sql


--changeSet OPER-2909:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_rename('po_auth','reverted_bool','hist_bool');
END;
/ 

--changeSet OPER-2909:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PO_AUTH', 'HIST_BOOL');
END;
/

--changeSet OPER-2909:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table po_auth modify (hist_bool Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (HIST_BOOL IN (0, 1) ) DEFERRABLE)
   ');
END;
/

--changeSet OPER-2909:4 stripComments:false
UPDATE 
   DPO_RLM_RULE 
SET 
   RULE_LDESC  = 'HIST_BOOL NOT IN (0,1)', 
   CONDITION   = '<condition> ' || chr(10) || ' <object name="EO_TA_S_PO_AUTH"> ' || chr(10) || '  HIST_BOOL NOT IN (0,1) ' || chr(10) || ' </object> ' || chr(10) || '</condition>',
   REVISION_DT = to_date('23-03-2015 23:00:00', 'dd-mm-yyyy hh24:mi:ss')
WHERE
   RULE_CLASS_CD = 'IC_S_PO_AUTH' AND
   RULE_ID       = 'RULE_5';
