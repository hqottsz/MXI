--liquibase formatted sql


--changeSet OPER-15633:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
* Recent Coder:  yvakulenko
* Recent Date:   Jun 17, 2016
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

    CURSOR lCur_PartRequestStatus
           (
    	        aSchedDbId evt_event.event_db_id%TYPE,
	            aSchedId   evt_event.event_id%TYPE
    	     )
    IS
       SELECT
          *
       FROM
          (SELECT
             DECODE
               (
                  ref_material_req_status.request_status_cd,
                  NULL, 'N/A',ref_material_req_status.request_status_cd
               ) AS status_cd,
             DECODE
               (
                  ref_material_req_status.priority_ord,
                  NULL, 0, ref_material_req_status.priority_ord
               ) AS status_ord
           FROM
              req_part
           INNER JOIN evt_event req_part_event ON
                      req_part.req_part_db_id = req_part_event.event_db_id AND
                      req_part.req_part_id = req_part_event.event_id
           LEFT OUTER JOIN ref_material_req_status ON
                           ref_material_req_status.event_status_db_id = req_part_event.event_status_db_id AND
                           ref_material_req_status.event_status_cd = req_part_event.event_status_cd
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
          )
   ORDER BY status_ord DESC;

   lReq_PartRequestStatus lCur_PartRequestStatus%ROWTYPE;


BEGIN
   OPEN lCur_PartRequestStatus (aSchedDbId, aSchedId);
   FETCH lCur_PartRequestStatus INTO lReq_PartRequestStatus;

   IF lCur_PartRequestStatus%NOTFOUND THEN
   	  lStatus := 'N/A';
   ELSE
      lStatus := lReq_PartRequestStatus.status_cd;
      CLOSE lCur_PartRequestStatus;
   END IF;

   RETURN lStatus;

END getPartRequestStatus;


/********************************************************************************
*
* Function:      getPartRequestETA
* Arguments:     aSchedDbId, aSchedId - pk for the sched_stask table.
* Description:   This function retrieves the latest eta
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
    lPartRequestEta DATE;
    lEta DATE;

    CURSOR lCur_PartRequestETA
           (
    	    aSchedDbId evt_event.event_db_id%TYPE,
	       aSchedId   evt_event.event_id%TYPE
           )
    IS
      SELECT
         CASE
            -- Get the eta date for PRONORDER, PRRESERVE and PRREMOTE part request. If updated_eta is provided, use it; otherwise, use est_arrival_dt.
            WHEN (req_part_event.event_status_db_id, req_part_event.event_status_cd) IN
               ((0, 'PRONORDER'), (0, 'PRRESERVE'), (0, 'PRREMOTE'))
            THEN
               NVL(req_part.updated_eta, req_part.est_arrival_dt)
         ELSE
            -- Get the eta date for non-(PRONORDER, PRRESERVE and PRREMOTE) part request. Only use updated_eta.
            req_part.updated_eta
         END as lPartRequestEta
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
	     lPartRequestEta DESC NULLS FIRST;

   lReq_PartRequestETA lCur_PartRequestETA%ROWTYPE;


BEGIN

   OPEN lCur_PartRequestETA(aSchedDbId, aSchedId);
   FETCH lCur_PartRequestETA INTO lReq_PartRequestETA;

   IF lCur_PartRequestETA%ROWCOUNT = 0 THEN
   	lEta := null;
   CLOSE lCur_PartRequestETA;

   ELSE

   	IF lReq_PartRequestETA.lPartRequestEta = null THEN
   		lEta := null;
   	ELSE
   		lEta := lReq_PartRequestETA.lPartRequestEta;
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
*                latest eta value for the given task key.
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
    lPartRequestEta DATE;
    lDbId 		  VARCHAR2(200);
    lReqPartDbId            VARCHAR2(200);

    CURSOR lCur_ReqPartDbId
    	   (
    	    aSchedDbId evt_event.event_db_id%TYPE,
	    aSchedId   evt_event.event_id%TYPE
    	   )
    IS
      SELECT
         CASE
            -- Get the eta date for PRONORDER, PRRESERVE and PRREMOTE part request. If updated_eta is provided, use it; otherwise, use est_arrival_dt.
            WHEN (req_part_event.event_status_db_id, req_part_event.event_status_cd) IN
               ((0, 'PRONORDER'), (0, 'PRRESERVE'), (0, 'PRREMOTE'))
            THEN
               NVL(req_part.updated_eta, req_part.est_arrival_dt)
         ELSE
            -- Get the eta date for non-(PRONORDER, PRRESERVE and PRREMOTE) part request. Only use updated_eta.
            req_part.updated_eta
         END as lPartRequestEta,
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
	     lPartRequestEta DESC NULLS FIRST;

   lReq_ReqPartDbId lCur_ReqPartDbId%ROWTYPE;

BEGIN

   OPEN lCur_ReqPartDbId(aSchedDbId, aSchedId);
   FETCH lCur_ReqPartDbId INTO lReq_ReqPartDbId;

   IF lCur_ReqPartDbId%ROWCOUNT = 0 THEN
   	lDbId := null;
   CLOSE lCur_ReqPartDbId;

   ELSE

   	IF lReq_ReqPartDbId.lPartRequestEta = null THEN
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
*                latest eta value for the given task key.
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
    lPartRequestEta DATE;
    lId 		  VARCHAR2(200);
    lReqPartId            VARCHAR2(200);

    CURSOR lCur_ReqPartId
    	   (
    	    aSchedDbId evt_event.event_db_id%TYPE,
	    aSchedId   evt_event.event_id%TYPE
    	   )
    IS
      SELECT
         CASE
            -- Get the eta date for PRONORDER, PRRESERVE and PRREMOTE part request. If updated_eta is provided, use it; otherwise, use est_arrival_dt.
            WHEN (req_part_event.event_status_db_id, req_part_event.event_status_cd) IN
               ((0, 'PRONORDER'), (0, 'PRRESERVE'), (0, 'PRREMOTE'))
            THEN
               NVL(req_part.updated_eta, req_part.est_arrival_dt)
         ELSE
            -- Get the eta date for non-(PRONORDER, PRRESERVE and PRREMOTE) part request. Only use updated_eta.
            req_part.updated_eta
         END as lPartRequestEta,
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
	     lPartRequestEta DESC NULLS FIRST;

   lReq_ReqPartId lCur_ReqPartId%ROWTYPE;

BEGIN

   OPEN lCur_ReqPartId(aSchedDbId, aSchedId);
   FETCH lCur_ReqPartId INTO lReq_ReqPartId;

   IF lCur_ReqPartId%ROWCOUNT = 0 THEN
   	lId := null;
   CLOSE lCur_ReqPartId;

   ELSE

   	IF lReq_ReqPartId.lPartRequestEta = null THEN
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
*                It could be 'PENDING','ACKNOWLEDGED','NOT AVAILABLE','ON ORDER',
*                 'IN REPAIR','IN TRANSIT','ON HAND','READY TO PICK UP',
*                 'N/A','NOT KNOWN'
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
   IF aRequestStatusCd = 'PENDING' THEN
      lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
   ELSIF aRequestStatusCd = 'ACKNOWLEDGED' THEN
      lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
   ELSIF aRequestStatusCd = 'NOT AVAILABLE' THEN
      lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
   ELSIF aRequestStatusCd = 'ON ORDER' THEN
         IF aPartRequestETA is NOT NULL THEN
    		   lPartRequestETADate := aPartRequestETA;
         ELSE
    		   lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
    	   END IF;
   ELSIF aRequestStatusCd = 'IN REPAIR' THEN
         IF aPartRequestETA is NOT NULL THEN
    		   lPartRequestETADate := aPartRequestETA;
         ELSE
    		   lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
    	   END IF;
   ELSIF aRequestStatusCd = 'IN TRANSIT' THEN
         IF aPartRequestETA is NOT NULL THEN
    		   lPartRequestETADate := aPartRequestETA;
         ELSE
    		   lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
    	   END IF;
   ELSIF aRequestStatusCd = 'ON HAND' THEN
      lPartRequestETADate := ADD_MONTHS ( SYSDATE, -120 );
   ELSIF aRequestStatusCd = 'READY TO PICK UP' THEN
      lPartRequestETADate := ADD_MONTHS ( SYSDATE, -120 );
   ELSIF aRequestStatusCd = 'N/A' THEN
      lPartRequestETADate := ADD_MONTHS ( SYSDATE, 1200 );
   ELSIF aRequestStatusCd = 'NOT KNOWN' THEN
      lPartRequestETADate := ADD_MONTHS ( SYSDATE, 1200 );
   END IF;

   RETURN lPartRequestETADate;
END getPartRequestETADate;


END PART_REQUEST_STATUS_PKG;
/