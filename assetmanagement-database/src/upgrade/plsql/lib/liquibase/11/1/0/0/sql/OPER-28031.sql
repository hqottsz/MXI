--liquibase formatted sql

--changeSet OPER-28031:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO ref_printer_type ( printer_type_db_id, printer_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
   VALUES ( 0, 'EXTERNAL', 'Custom printer implementation', 'Core print logic will not execute, a custom implementation must be defined for printing to occur', 1, TO_DATE('2018-12-18', 'YYYY-MM-DD'), TO_DATE('2018-12-18', 'YYYY-MM-DD'), 0, 'MXI' );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

