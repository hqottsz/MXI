--liquibase formatted sql


--changeSet getPossibleKits:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getPossibleKits
(
   aReqPartDbId req_part.req_part_db_id%TYPE,
   aReqPartId req_part.req_part_id%TYPE
) RETURN VARCHAR
IS

   lPossibleKitsQty INTEGER;
BEGIN

   lPossibleKitsQty := 0;

   RETURN lPossibleKitsQty;
END getPossibleKits;
/