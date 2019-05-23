--liquibase formatted sql


--changeSet OPER-2201:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************
Insert new config parm ACTION_ADJUST_TOTAL_SPARES

*************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ADJUST_TOTAL_SPARES', 
      'Permission to edit the total spares for a rotable part.',
      'TRUE/FALSE', 	  
      'FALSE',  
      1, 
      'Org - Financials', 
      '8.2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-2201:2 stripComments:false
/************************************************
Insert new transaction type SPARESQTYADJ

*************************************************/
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'SPARESQTYADJ', 'SPARESQTYADJ', 'A transaction occurred when the on-hand quantity for a rotable part was adjusted.', 0, TO_DATE('2014-11-28', 'YYYY-MM-DD'), TO_DATE('2014-11-28', 'YYYY-MM-DD'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS(
SELECT 1 FROM ref_xaction_type WHERE xaction_type_cd='SPARESQTYADJ' )
;

--changeSet OPER-2201:3 stripComments:false
/************************************************
Insert new part log type PAATS

*************************************************/
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0,'PAATS','Total Spares Adjustment', 'The total spares amount for this part has been adjusted.','PAATS', 0 , TO_DATE('2014-11-28', 'YYYY-MM-DD'), TO_DATE('2014-11-28', 'YYYY-MM-DD'), 0 , 'MXI' 
FROM DUAL WHERE NOT EXISTS(
SELECT 1 FROM REF_LOG_ACTION WHERE LOG_ACTION_CD='PAATS'
)
;