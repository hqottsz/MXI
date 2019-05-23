--liquibase formatted sql


--changeSet OPER-2417:1 stripComments:false
INSERT INTO 
   utl_sequence( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
SELECT 
   'ORG_HR_SHIFT_PLAN_ID_SEQ', 
   ( SELECT NVL(MAX(HR_SHIFT_PLAN_ID), 99999) + 1 FROM ORG_HR_SHIFT_PLAN ), 
   'ORG_HR_SHIFT_PLAN', 'HR_SHIFT_PLAN_ID' , 1, 0
FROM
   DUAL
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'ORG_HR_SHIFT_PLAN_ID_SEQ' );	 

--changeSet OPER-2417:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   lStartNum ORG_HR_SHIFT_PLAN.HR_SHIFT_PLAN_ID%TYPE;
BEGIN   
   SELECT NVL(MAX(HR_SHIFT_PLAN_ID), 99999) + 1
   INTO lStartNum
   FROM ORG_HR_SHIFT_PLAN;
   utl_migr_schema_pkg.sequence_create('ORG_HR_SHIFT_PLAN_ID_SEQ', lStartNum );	 
END; 
/	 