--liquibase formatted sql

--changeSet OPER-23124:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN   
utl_migr_schema_pkg.index_create('
   CREATE INDEX IX_INVINV_REFINVCLASS ON INV_INV
   (
      INV_CLASS_CD ASC ,
      INV_CLASS_DB_ID ASC
   )
   ');
END;
/

--changeSet OPER-23124:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN   
utl_migr_schema_pkg.index_create('
   CREATE INDEX IX_SCHEDSTASK_HISTBOOL ON SCHED_STASK
   (
      HIST_BOOL_RO ASC
   )
   ');
END;
/
