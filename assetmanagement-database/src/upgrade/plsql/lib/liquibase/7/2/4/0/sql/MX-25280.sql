--liquibase formatted sql


--changeSet MX-25280:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PART_REQUEST_STATUS_PKG
IS

/* *** Function Bodies *** */

/********************************************************************************
*
* Function:      getPartRequestStatus
* Arguments:     aSchedDbId, aSchedId - pk for the sched_stask table.
* Description:   This function sets the part request status based
*                on the event status codes and returns the status.
*
* Orig.Coder:    bparekh
* Recent Coder:  bparekh
* Recent Date:   Jul 21, 2010
*
*********************************************************************************/
FUNCTION getPartRequestStatus
(
   aSchedDbId IN NUMBER,
   aSchedId IN NUMBER
) RETURN VARCHAR2

IS
    lPartRequestStatus NUMBER(6);
    lStatus VARCHAR2 (100);
    lTemp VARCHAR2 (100);

    CURSOR lCur_PartRequestStatus
           (
    	    aSchedDbId evt_event.event_db_id%TYPE,
	    aSchedId   evt_event.event_id%TYPE
    	   )
    IS
      SELECT
         DECODE(req_part_event.event_status_cd,
         'PROPEN',1,
         'PRPOREQ',1,
         'PRQUAR',1,
         'PRONORDER',2,
         'PRRESERVE',2,
         'PRREMOTE',2,
         'PRAVAIL',3,
         'PRINSPREQ',3,
         'PRISSUED',4,
         'PRCOMPLETE',4,
         'PRCANCEL',4,5)
          as lPartRequestStatus
      FROM
         req_part
      INNER JOIN evt_event req_part_event ON
      req_part.req_part_db_id = req_part_event.event_db_id AND
      req_part.req_part_id = req_part_event.event_id
      WHERE
         (req_part.sched_db_id, req_part.sched_id)
         IN (
            SELECT
               evt_event.event_db_id ,
               evt_event.event_id
            FROM
               evt_event
            WHERE
               evt_event.hist_bool = 0
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id = PRIOR evt_event.event_id)
      ORDER BY
	case req_part_event.event_status_cd
	WHEN 'PROPEN' THEN 1
	WHEN 'PRPOREQ' THEN 1
	WHEN 'PRQUAR' THEN 1
	WHEN 'PRONORDER' THEN 2
	WHEN 'PRRESERVE' THEN 2
	WHEN 'PRREMOTE' THEN 2
	WHEN 'PRAVAIL' THEN 3
	WHEN 'PRINSPREQ' THEN 3
	WHEN 'PRISSUED' THEN 4
	WHEN 'PRCOMPLETE' THEN 4
	WHEN 'PRCANCEL' THEN 4
	ELSE 5
	END;

   lReq_PartRequestStatus lCur_PartRequestStatus%ROWTYPE;


BEGIN

   OPEN lCur_PartRequestStatus (aSchedDbId, aSchedId);
   FETCH lCur_PartRequestStatus INTO lReq_PartRequestStatus;

   IF lCur_PartRequestStatus%ROWCOUNT = 0 THEN
   	lStatus := 'N/A';

   ELSIF lReq_PartRequestStatus.lPartRequestStatus = 1 THEN
   	lStatus := 'NOT READY';
   CLOSE lCur_PartRequestStatus;

   ELSIF lReq_PartRequestStatus.lPartRequestStatus = 2 THEN
   	lStatus := 'AWAITING';
   CLOSE lCur_PartRequestStatus;

   ELSIF lReq_PartRequestStatus.lPartRequestStatus = 3 THEN
   	lStatus := 'READY';
   CLOSE lCur_PartRequestStatus;

   ELSIF lReq_PartRequestStatus.lPartRequestStatus = 4 THEN
   	lStatus := 'N/A';
   CLOSE lCur_PartRequestStatus;

   ELSIF lReq_PartRequestStatus.lPartRequestStatus = 5 THEN
   	lStatus := 'NOT KNOWN';
   CLOSE lCur_PartRequestStatus;

   END IF;

   RETURN lStatus;

END getPartRequestStatus;


/********************************************************************************
*
* Function:      getPartRequestETA
* Arguments:     aSchedDbId, aSchedId - pk for the sched_stask table.
* Description:   This function retrieves the latest estimated arrival date
*                from the req_part table for the given task key.
*
* Orig.Coder:    bparekh
* Recent Coder:  bparekh
* Recent Date:   Jul 21, 2010
*
*********************************************************************************/
FUNCTION getPartRequestETA
(
   aSchedDbId IN NUMBER,
   aSchedId IN NUMBER
) RETURN DATE

IS
    lEstimatedArrivalDate DATE;
    lEta DATE;

    CURSOR lCur_PartRequestETA
           (
    	    aSchedDbId evt_event.event_db_id%TYPE,
	    aSchedId   evt_event.event_id%TYPE
           )
    IS
      SELECT
         req_part.est_arrival_dt as lEstimatedArrivalDate
      FROM
         req_part
      INNER JOIN evt_event req_part_event ON
      req_part.req_part_db_id = req_part_event.event_db_id AND
      req_part.req_part_id = req_part_event.event_id
      WHERE
         (req_part.sched_db_id, req_part.sched_id)
         IN (
            SELECT
               evt_event.event_db_id ,
               evt_event.event_id
            FROM
               evt_event
            WHERE
               evt_event.hist_bool = 0
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id = PRIOR evt_event.event_id)
         AND
         req_part_event.event_status_cd
         IN
         ('PRONORDER','PRRESERVE','PRREMOTE')
      ORDER BY
	req_part.est_arrival_dt DESC NULLS FIRST;

   lReq_PartRequestETA lCur_PartRequestETA%ROWTYPE;


BEGIN

   OPEN lCur_PartRequestETA(aSchedDbId, aSchedId);
   FETCH lCur_PartRequestETA INTO lReq_PartRequestETA;

   IF lCur_PartRequestETA%ROWCOUNT = 0 THEN
   	lEta := null;
   CLOSE lCur_PartRequestETA;

   ELSE

   	IF lReq_PartRequestETA.lEstimatedArrivalDate = null THEN
   		lEta := null;
   	ELSE
   		lEta := lReq_PartRequestETA.lEstimatedArrivalDate;
   	END IF;
   CLOSE lCur_PartRequestETA;


   END IF;

   RETURN lEta;

END getPartRequestETA;

/********************************************************************************
*
* Function:      getPartRequestWarning
* Arguments:     aSchedDbId, aSchedId - pk for the sched_stask table.
* Description:   This function sets a warning bool value by comparing the ETA
*                and 'Needed By' date value for the part request (if available)
*                of by comparing the ETA with the sched_dead_dt value of the
*                evt_sched_date table.
*
* Orig.Coder:    bparekh
* Recent Coder:  bparekh
* Recent Date:   Jul 21, 2010
*
*********************************************************************************/
FUNCTION getPartRequestWarning
(
   aSchedDbId IN NUMBER,
   aSchedId IN NUMBER
) RETURN NUMBER

IS
    lEstimatedArrivalDate VARCHAR2(200);
    lEta VARCHAR2(200);
    lWarningBool NUMBER(1);
    lSchedDate DATE;
    lReqPartDate DATE;


    CURSOR lCur_PartRequestWarning
           (
            aSchedDbId evt_sched_dead.event_db_id%TYPE,
        aSchedId   evt_sched_dead.event_id%TYPE
           )
    IS
      SELECT
         evt_sched_dead.sched_dead_dt as lSchedDate
      FROM
         evt_sched_dead
      WHERE
         evt_sched_dead.event_db_id = aSchedDbId AND
         evt_sched_dead.event_id = aSchedId AND
         evt_sched_dead.sched_driver_bool = 1;

   lReq_PartRequestWarning lCur_PartRequestWarning%ROWTYPE;


BEGIN

   lReqPartDate := getReqPartDate(aSchedDbId,aSchedId);
   lEta := getPartRequestETA(aSchedDbId,aSchedId);
   OPEN lCur_PartRequestWarning(aSchedDbId, aSchedId);
   FETCH lCur_PartRequestWarning INTO lReq_PartRequestWarning;

   IF lReqPartDate IS NULL AND lCur_PartRequestWarning%ROWCOUNT = 0 THEN
    lWarningBool := 0;
   CLOSE lCur_PartRequestWarning;

   ELSE

    IF lReqPartDate IS NOT NULL THEN
	    IF lEta > lReqPartDate THEN
		lWarningBool := 1;
	    ELSE
		lWarningBool := 0;
	    END IF;
    ELSE
	    IF lEta > lReq_PartRequestWarning.lSchedDate THEN
		lWarningBool := 1;
	    ELSE
		lWarningBool := 0;
	    END IF;
    END IF;
   CLOSE lCur_PartRequestWarning;


   END IF;

   RETURN lWarningBool;

END getPartRequestWarning;

/********************************************************************************
*
* Function:      getReqPartDbId
* Arguments:     aSchedDbId, aSchedId - pk for the sched_stask table.
* Description:   This function retrieves the req part db id which has the
*                latest estimated arrival date value for the given task key.
*
* Orig.Coder:    bparekh
* Recent Coder:  bparekh
* Recent Date:   Jul 21, 2010
*
*********************************************************************************/
FUNCTION getReqPartDbId
(
   aSchedDbId IN NUMBER,
   aSchedId IN NUMBER
) RETURN VARCHAR2
IS
    lEstimatedArrivalDate DATE;
    lDbId 		  VARCHAR2(200);
    lReqPartDbId            VARCHAR2(200);

    CURSOR lCur_ReqPartDbId
    	   (
    	    aSchedDbId evt_event.event_db_id%TYPE,
	    aSchedId   evt_event.event_id%TYPE
    	   )
    IS
      SELECT
         req_part.est_arrival_dt as lEstimatedArrivalDate,
         req_part.req_part_db_id as lReqPartDbId
      FROM
         req_part
      INNER JOIN evt_event req_part_event ON
      req_part.req_part_db_id = req_part_event.event_db_id AND
      req_part.req_part_id = req_part_event.event_id
      WHERE
         (req_part.sched_db_id, req_part.sched_id)
         IN (
            SELECT
               evt_event.event_db_id,
               evt_event.event_id
            FROM
               evt_event
            WHERE
               evt_event.hist_bool = 0
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id = PRIOR evt_event.event_id)
         AND
         req_part_event.event_status_cd
         IN
         ('PRONORDER','PRRESERVE','PRREMOTE')
      ORDER BY
	req_part.est_arrival_dt DESC;

   lReq_ReqPartDbId lCur_ReqPartDbId%ROWTYPE;

BEGIN

   OPEN lCur_ReqPartDbId(aSchedDbId, aSchedId);
   FETCH lCur_ReqPartDbId INTO lReq_ReqPartDbId;

   IF lCur_ReqPartDbId%ROWCOUNT = 0 THEN
   	lDbId := null;
   CLOSE lCur_ReqPartDbId;

   ELSE

   	IF lReq_ReqPartDbId.lEstimatedArrivalDate = null THEN
   		lDbId := null;
   	ELSE
   		lDbId := lReq_ReqPartDbId.lReqPartDbId;
   	END IF;
   CLOSE lCur_ReqPartDbId;

   END IF;

   RETURN lDbId;

END getReqPartDbId;

/********************************************************************************
*
* Function:      getReqPartId
* Arguments:     aSchedDbId, aSchedId - pk for the sched_stask table.
* Description:   This function retrieves the req part id which has the
*                latest estimated arrival date value for the given task key.
*
* Orig.Coder:    bparekh
* Recent Coder:  bparekh
* Recent Date:   Jul 21, 2010
*
*********************************************************************************/
FUNCTION getReqPartId
(
   aSchedDbId IN NUMBER,
   aSchedId IN NUMBER
) RETURN VARCHAR2
IS
    lEstimatedArrivalDate DATE;
    lId 		  VARCHAR2(200);
    lReqPartId            VARCHAR2(200);

    CURSOR lCur_ReqPartId
    	   (
    	    aSchedDbId evt_event.event_db_id%TYPE,
	    aSchedId   evt_event.event_id%TYPE
    	   )
    IS
      SELECT
         req_part.est_arrival_dt as lEstimatedArrivalDate,
         req_part.req_part_id as lReqPartId
      FROM
         req_part
      INNER JOIN evt_event req_part_event ON
      req_part.req_part_db_id = req_part_event.event_db_id AND
      req_part.req_part_id = req_part_event.event_id
      WHERE
         (req_part.sched_db_id, req_part.sched_id)
         IN (
            SELECT
               evt_event.event_db_id,
               evt_event.event_id
            FROM
               evt_event
            WHERE
               evt_event.hist_bool = 0
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id = PRIOR evt_event.event_id)
         AND
         req_part_event.event_status_cd
         IN
         ('PRONORDER','PRRESERVE','PRREMOTE')
      ORDER BY
	req_part.est_arrival_dt DESC;

   lReq_ReqPartId lCur_ReqPartId%ROWTYPE;

BEGIN

   OPEN lCur_ReqPartId(aSchedDbId, aSchedId);
   FETCH lCur_ReqPartId INTO lReq_ReqPartId;

   IF lCur_ReqPartId%ROWCOUNT = 0 THEN
   	lId := null;
   CLOSE lCur_ReqPartId;

   ELSE

   	IF lReq_ReqPartId.lEstimatedArrivalDate = null THEN
   		lId := null;
   	ELSE
   		lId := lReq_ReqPartId.lReqPartId;
   	END IF;
   CLOSE lCur_ReqPartId;

   END IF;

   RETURN lId;

END getReqPartId;

/********************************************************************************
*
* Function:      getReqPartDate
* Arguments:     aSchedDbId, aSchedId - pk for the sched_stask table.
* Description:   This function retrieves the req part date for the given
*                task key.
*
* Orig.Coder:    bparekh
* Recent Coder:  bparekh
* Recent Date:   Jul 21, 2010
*
*********************************************************************************/
FUNCTION getReqPartDate
(
   aSchedDbId IN NUMBER,
   aSchedId IN NUMBER
) RETURN DATE
IS
   lReqDate DATE;

BEGIN
    SELECT
       req_part.req_by_dt
    INTO
       lReqDate
    FROM
       req_part
    WHERE
       req_part.req_part_db_id=getReqPartDbId(aSchedDbId, aSchedId) AND
       req_part.req_part_id=getReqPartId(aSchedDbId, aSchedId);
    RETURN lReqDate;

    EXCEPTION
    WHEN NO_DATA_FOUND THEN
    lReqDate := null;
    RETURN lReqDate;



END getReqPartDate;

/********************************************************************************
*
* Function:      getPartRequestETADate
* Arguments:     aRequestStatusCd - The request status code.
*                It could be READY, NOT READY, AWAITING, NOT KNOWN or N/A.
*
* Orig.Coder:    bparekh
* Recent Coder:  bparekh
* Recent Date:   Aug 5, 2010
*
*********************************************************************************/
FUNCTION getPartRequestETADate
(
   aRequestStatusCd IN VARCHAR,
   aPartRequestETA IN DATE
) RETURN DATE
IS
   lPartRequestETADate DATE;

BEGIN
    IF aRequestStatusCd = 'READY' THEN
    	lPartRequestETADate := ADD_MONTHS ( SYSDATE, -120 );
    ELSIF aRequestStatusCd = 'NOT READY' THEN
    	lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
    ELSIF aRequestStatusCd = 'AWAITING' THEN
    	IF aPartRequestETA is NOT NULL THEN
    		lPartRequestETADate := aPartRequestETA;
    	ELSE
    		lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
    	END IF;
    ELSIF aRequestStatusCd = 'NOT KNOWN' THEN
    	lPartRequestETADate := ADD_MONTHS ( SYSDATE, 1200 );
    ELSIF aRequestStatusCd = 'N/A' THEN
    	lPartRequestETADate := ADD_MONTHS ( SYSDATE, 1200 );
    END IF;

    RETURN lPartRequestETADate;



END getPartRequestETADate;


END PART_REQUEST_STATUS_PKG;
/