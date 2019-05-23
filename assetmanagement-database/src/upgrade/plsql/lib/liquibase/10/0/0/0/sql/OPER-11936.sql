--liquibase formatted sql
--changeSet OPER-11936:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.sequence_create('DEF_AUTH_CD_SEQ', 10000); 
END;
/

--changeSet OPER-11936:2 stripComments:false
INSERT INTO 
   utl_sequence ( 
      sequence_cd, 
      next_value,       
      oracle_seq, 
      utl_id 
   )
SELECT 
   'DEF_AUTH_CD_SEQ', 
   10000, 
   1, 
   0
FROM
   DUAL
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'DEF_AUTH_CD_SEQ' );