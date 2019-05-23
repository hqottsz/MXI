--liquibase formatted sql


--changeSet QC-4338:1 stripComments:false
DELETE FROM utl_sequence 
   WHERE sequence_cd = 'INV_SHIFT_ID_SEQ';

--changeSet QC-4338:2 stripComments:false
DELETE FROM utl_sequence 
   WHERE sequence_cd = 'SCHEDULE_ID_SEQ';   

--changeSet QC-4338:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('INV_SHIFT_ID_SEQ');
END;
/

--changeSet QC-4338:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('SCHEDULE_ID_SEQ');
END;
/