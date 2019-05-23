--liquibase formatted sql


--changeSet doesPartHaveInProgressRFQ:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      doesPartHaveInProgressRFQ
* Arguments:     aPartNoDbId, aPartNoId - pk for the part number
* Description:   This function will check if there is an in progress RFQ for a part number
*
* Orig.Coder:    jcimino
* Recent Coder:   
* Recent Date:   Dec 20, 2006
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION doesPartHaveInProgressRFQ
(
   aPartNoDbId eqp_part_no.part_no_db_id%TYPE,
   aPartNoId eqp_part_no.part_no_id%TYPE
) RETURN VARCHAR
IS
   lRFQCount NUMBER;
BEGIN

   SELECT
      COUNT(*)
   INTO
      lRFQCount
   FROM
		rfq_line,
   	evt_event
	WHERE
		rfq_line.part_no_db_id = aPartNoDbId AND
		rfq_line.part_no_id    = aPartNoId
		AND
		rfq_line.po_db_id IS NULL
		AND
		evt_event.event_db_id = rfq_line.rfq_db_id AND
		evt_event.event_id    = rfq_line.rfq_id
		AND
		evt_event.event_status_cd IN ( 'RFQOPEN', 'RFQSENT' );

	IF lRFQCount = 0 THEN
		RETURN 0;
	END IF;

	RETURN 1;

END doesPartHaveInProgressRFQ;
/