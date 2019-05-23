--liquibase formatted sql


--changeSet MX-12706:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UPDATE
      utl_sequence
   SET 
      table_name = 'IETM_IETM',
      column_name = 'IETM_ID'
   WHERE
      sequence_cd = 'IETM_ID_SEQ'
      AND
      (
         table_name != 'IETM_IETM'
         OR
         column_name != 'IETM_ID'
      );

END;
/