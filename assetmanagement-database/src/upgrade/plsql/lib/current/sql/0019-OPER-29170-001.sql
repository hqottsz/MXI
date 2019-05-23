--liquibase formatted sql

--changeSet OPER-29170:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Create index for inventory searching range. 
BEGIN
   upg_migr_schema_v1_pkg.index_create('
		CREATE INDEX IX_RECEIVEDDT_INVINV ON INV_INV
		(
		  RECEIVED_DT
		)
   ');
END;
/