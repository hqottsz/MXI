--liquibase formatted sql


--changeSet isOrderAuthorized:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isOrderAuthorized
* Arguments:     an_EventDbId    - event db id
*                an_EventId      - event id
* Description:   This function determines if the Order is authorized or not.
*		 Return 1 if it is authorized.  Otherwise, return 0
*
* Orig.Coder:    sdevi
* Recent Coder:  sdevi
* Recent Date:   July 28, 2011
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isOrderAuthorized
(
   an_EventDbId evt_event.event_db_id%TYPE,
   an_EventId   evt_event.event_id%TYPE
)  RETURN NUMBER
IS
   ln_EvtStageRows NUMBER;
   ls_EventTypeCd evt_event.event_type_cd%TYPE;
   ls_EventStatus evt_event.event_status_cd%TYPE;
   xc_EventTypeNotPO EXCEPTION;

BEGIN

   SELECT
   	evt_event.event_type_cd,
   	evt_event.event_status_cd
   INTO
   	ls_EventTypeCd,
   	ls_EventStatus
   FROM
   	evt_event
   WHERE
	evt_event.event_db_id = an_EventDbId AND
	evt_event.event_id    = an_EventId;

   -- if event type is not PO then throw the exception
   IF ls_EventTypeCd <> 'PO' THEN
      RAISE xc_EventTypeNotPO;
   END IF;

   -- if event_status is OPEN, return 0
   IF ls_EventStatus = 'POOPEN' THEN
      RETURN 0;
   END IF;

   -- if event_status is not CANCEL, return 1
   IF ls_EventStatus <> 'POCANCEL' THEN
      RETURN 1;
   END IF;

   -- if event_status is CANCEL and if evt_stage rows with status other than CANCEL/OPEN exist then return 1
   IF ls_EventStatus = 'POCANCEL' THEN

       SELECT
           COUNT(*) INTO ln_EvtStageRows
       FROM
           evt_stage
       WHERE
          evt_stage.event_db_id = an_EventDbId AND
          evt_stage.event_id    = an_EventId   AND
          evt_stage.event_status_cd NOT IN ('POOPEN', 'POCANCEL');

       IF ln_EvtStageRows > 0 THEN
          RETURN 1;
       END IF;

   END IF;

   -- if you got this far, return 0 which means the order is not authorized
   RETURN 0;

END isOrderAuthorized;
/