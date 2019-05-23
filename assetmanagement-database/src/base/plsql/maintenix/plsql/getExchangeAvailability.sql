--liquibase formatted sql


--changeSet getExchangeAvailability:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:	      getExchangeAvailability
* Arguments:	   aReqPartNoDbId:   the db id of the part request
*		            aReqPartNoId:     the id of the part request
*		            aWhereNeededDbId: the db id of the where needed location
*                 aWhereNeededId    the id of the where needed location
* Description:	Returns the exchange availablity of a part request.
*********************************************************************************/
CREATE OR REPLACE FUNCTION getExchangeAvailability
(
   aReqPartNoDbId          req_part.req_part_db_id%TYPE,
   aReqPartNoId            req_part.req_part_id%TYPE,
   aWhereNeededDbId        inv_loc.loc_db_id%TYPE,
   aWhereNeededId          inv_loc.loc_id%TYPE
) RETURN VARCHAR2
IS
   -- cursors for all airports boolean
   CURSOR all_airport_cursor(
      lReqPartNoDbId          req_part.req_part_db_id%TYPE,
      lReqPartNoId            req_part.req_part_id%TYPE
   )
   IS
      SELECT
         1
      FROM
         req_part,
         eqp_part_vendor_exchg
      WHERE
         req_part.req_part_db_id = lReqPartNoDbId AND
         req_part.req_part_id = lReqPartNoId	  AND
         req_part.rstat_cd    = 0
         AND
         eqp_part_vendor_exchg.part_no_db_id = req_part.po_part_no_db_id AND
         eqp_part_vendor_exchg.part_no_id = req_part.po_part_no_id AND
         eqp_part_vendor_exchg.all_airport_bool = 1;

   -- cursor for determining if a part request has any exchange locations
   CURSOR has_exchange_locations(
      lReqPartNoDbId          req_part.req_part_db_id%TYPE,
      lReqPartNoId            req_part.req_part_id%TYPE
   )
   IS
      SELECT
         1
      FROM
         req_part
      WHERE
         req_part.req_part_db_id = lReqPartNoDbId AND
         req_part.req_part_id = lReqPartNoId	  AND
         req_part.rstat_cd    = 0
         AND
         (
            EXISTS
            (
               SELECT 1
               FROM
                  eqp_part_vendor_exchg
               WHERE
                  eqp_part_vendor_exchg.part_no_db_id    = req_part.po_part_no_db_id AND
                  eqp_part_vendor_exchg.part_no_id       = req_part.po_part_no_id AND
                  eqp_part_vendor_exchg.all_airport_bool = 1
            )
            OR
            EXISTS
            (
               SELECT 1
               FROM
                  eqp_part_vendor_exchg_loc,
		  		  inv_loc
               WHERE
		  		  eqp_part_vendor_exchg_loc.part_no_db_id = req_part.po_part_no_db_id AND
		  		  eqp_part_vendor_exchg_loc.part_no_id    = req_part.po_part_no_id
		  		  AND
		  		  inv_loc.loc_db_id = eqp_part_vendor_exchg_loc.loc_db_id AND
				  inv_loc.loc_id    = eqp_part_vendor_exchg_loc.loc_id
				  AND
				  inv_loc.supply_loc_db_id IS NOT NULL
            )
         );

   -- cursor for determining if a location is an exchange location
   CURSOR is_an_exchange_location(
      lReqPartNoDbId          req_part.req_part_db_id%TYPE,
      lReqPartNoId            req_part.req_part_id%TYPE,
      lLocDbId                inv_loc.loc_db_id%TYPE,
      lLocId                  inv_loc.loc_id%TYPE
   )
   IS
      SELECT
         1
      FROM
         req_part,
         eqp_part_vendor_exchg_loc,
         inv_loc
      WHERE
         req_part.req_part_db_id = lReqPartNoDbId AND
         req_part.req_part_id = lReqPartNoId	  AND
         req_part.rstat_cd    = 0
         AND
         eqp_part_vendor_exchg_loc.part_no_db_id = req_part.po_part_no_db_id AND
         eqp_part_vendor_exchg_loc.part_no_id = req_part.po_part_no_id
         AND
         eqp_part_vendor_exchg_loc.loc_db_id = inv_loc.loc_db_id AND
         eqp_part_vendor_exchg_loc.loc_id = inv_loc.loc_id
         AND
         inv_loc.supply_loc_db_id = lLocDbId AND
         inv_loc.supply_loc_id = lLocId	     AND
         inv_loc.rstat_cd      = 0;

   -- instance variables
   lQueryResult      int;
   lSupplyLocDbId    inv_loc.loc_db_id%TYPE;
   lSupplyLocId      inv_loc.loc_id%TYPE;

BEGIN
   -- if no exchange relationships exist, then return blank
   OPEN has_exchange_locations(aReqPartNoDbId, aReqPartNoId);
   FETCH has_exchange_locations INTO lQueryResult;
   IF has_exchange_locations%NOTFOUND THEN
      CLOSE has_exchange_locations;
      RETURN NULL;

   -- exchange relationships exist
   ELSE
      CLOSE has_exchange_locations;

      -- if the all airport boolean is found, then return LOCAL
      OPEN all_airport_cursor(aReqPartNoDbId, aReqPartNoId);
      FETCH all_airport_cursor INTO lQueryResult;
      IF all_airport_cursor%FOUND THEN
         CLOSE all_airport_cursor;
         RETURN 'LOCAL';
      END IF;
      CLOSE all_airport_cursor;

      -- if the where needed is not provided and all airport boolean is false, return YES
      IF (aWhereNeededDbId IS NULL OR aWhereNeededId IS NULL) THEN
         RETURN 'YES';
      END IF;

      -- get the supply location for the where needed
      SELECT
         inv_loc.supply_loc_db_id,
         inv_loc.supply_loc_id
      INTO
         lSupplyLocDbId,
         lSupplyLocId
      FROM
         inv_loc
      WHERE
         inv_loc.loc_db_id = aWhereNeededDbId AND
         inv_loc.loc_id = aWhereNeededId      AND
         inv_loc.rstat_cd	= 0;

      -- if the where needed location does not have a supply location, then return blank
      IF (lSupplyLocDbId IS NULL OR lSupplyLocId IS NULL) THEN
         RETURN NULL;
      END IF;

      OPEN is_an_exchange_location(aReqPartNoDbId, aReqPartNoId, lSupplyLocDbId, lSupplyLocId);
      FETCH is_an_exchange_location INTO lQueryResult;

      -- if there are no exchange locations setup for this part request
      IF is_an_exchange_location%NOTFOUND THEN
         CLOSE is_an_exchange_location;
         RETURN 'REMOTE';

      -- if there is an exchange location setup for this part request
      ELSE
         CLOSE is_an_exchange_location;
         RETURN 'LOCAL';
      END IF;

      CLOSE is_an_exchange_location;
   END IF;
END;
/