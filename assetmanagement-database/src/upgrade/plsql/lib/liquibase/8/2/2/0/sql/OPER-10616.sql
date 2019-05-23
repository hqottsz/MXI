--liquibase formatted sql

--changeSet OPER-10616:1 stripComments:false
DELETE FROM 
   ref_material_req_status
WHERE 
   request_status_db_id = 0 AND
   request_status_cd    IN ('READY', 'NOT READY', 'AWAITING');

--changeSet OPER-10616:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REF_MATERIAL_REQ_STATUS add (
   EVENT_STATUS_DB_ID NUMBER(10,0)  Check (EVENT_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) 
)
');
END;
/

--changeSet OPER-10616:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REF_MATERIAL_REQ_STATUS add (
   EVENT_STATUS_CD Varchar(16)
)
');
END;
/

--changeSet OPER-10616:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('  
   Alter table REF_MATERIAL_REQ_STATUS add Constraint FK_REFEVTSTAT_REFMATREQSTAT foreign key (EVENT_STATUS_DB_ID,EVENT_STATUS_CD) 
   references REF_EVENT_STATUS (EVENT_STATUS_DB_ID,EVENT_STATUS_CD)  
');
END;
/

--changeSet OPER-10616:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
      Alter table REF_MATERIAL_REQ_STATUS add (PRIORITY_ORD Number(4,0))
');
END;
/

--changeSet OPER-10616:6 stripComments:false
MERGE INTO 
   ref_material_req_status
USING
( 
   SELECT
      0          AS request_status_db_id, 
      'N/A'      AS request_status_cd, 
      'N/A'      AS user_cd, 
      'Material request status not applicable for the task.' AS request_status_sdesc, 
      'Indicates that material request status is not applicable for the task, as there are no part requests, or the part requests have been completed.' AS request_status_ldesc,
      1          AS priority_ord,
      0          AS event_status_db_id,
      'PRISSUED' AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0         AS request_status_db_id, 
      'PENDING' AS request_status_cd, 
      'PENDING' AS user_cd, 
      'Material request pending.' AS request_status_sdesc, 
      'Indicates that at least one request is awaiting processing.' AS request_status_ldesc,
      10         AS priority_ord,
      null      AS event_status_db_id,
      null      AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0                AS request_status_db_id, 
      'ACKNOWLEDGED'   AS request_status_cd, 
      'ACKNOWLEDGED'   AS user_cd, 
      'Material request receiving acknoweleged.' AS request_status_sdesc, 
      'Indicates that at least one request has been received and acknowledged by materials, though the request has not been processed.' AS request_status_ldesc,
      9                AS priority_ord,
      0                AS event_status_db_id,
      'PRACKNOWLEDGED' AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0               AS request_status_db_id, 
      'NOT AVAILABLE' AS request_status_cd, 
      'NOT AVAILABLE' AS user_cd, 
      'Material request is not available.' AS request_status_sdesc, 
      'Indicates that at least one request is not available.' AS request_status_ldesc,
      8               AS priority_ord,
      0               AS event_status_db_id,
      'PRNOTAVAILABLE'    AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0             AS request_status_db_id, 
      'ON ORDER'    AS request_status_cd, 
      'ON ORDER'    AS user_cd, 
      'Material request is on order.' AS request_status_sdesc, 
      'Indicates that at least one request is on order.' AS request_status_ldesc,
      7             AS priority_ord,
      0             AS event_status_db_id,
      'PRONORDER'   AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0              AS request_status_db_id, 
      'IN REPAIR'    AS request_status_cd, 
      'IN REPAIR'    AS user_cd, 
      'Material request is in repair.' AS request_status_sdesc, 
      'Indicates that at least one request is in repair.' AS request_status_ldesc,
      6              AS priority_ord,
      0              AS event_status_db_id,
      'PRINREPAIR'      AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0              AS request_status_db_id, 
      'IN TRANSIT'   AS request_status_cd, 
      'IN TRANSIT'   AS user_cd, 
      'Material request is in transit.' AS request_status_sdesc, 
      'Indicates that at least one request is in transit.' AS request_status_ldesc,
      5             AS priority_ord,
      0             AS event_status_db_id,
      'PRINTRANSIT' AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0          AS request_status_db_id, 
      'ON HAND'  AS request_status_cd, 
      'ON HAND'  AS user_cd, 
      'All requested parts are available, but have not been individually reserved.' AS request_status_sdesc, 
      'Indicates that all components are available, but have not been individually reserved as the needed by date is not for some time.' AS request_status_ldesc,
      4          AS priority_ord,
      0          AS event_status_db_id,
      'PRONHAND' AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0                  AS request_status_db_id, 
      'READY TO PICK UP' AS request_status_cd, 
      'READY TO PICK UP' AS user_cd, 
      'All requested parts for this task and subtasks are available to pick up.' AS request_status_sdesc, 
      'Indicates that all the requested parts for this task and subtasks are available to pick up.' AS request_status_ldesc,
      3                  AS priority_ord,
      0                  AS event_status_db_id,
      'PRREADYFORPICKUP'    AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0                  AS request_status_db_id, 
      'NOT KNOWN' AS request_status_cd, 
      'N/A' AS user_cd, 
      'The status of part request is not known.' AS request_status_sdesc, 
      'Indicates that the status of part request is not known.' AS request_status_ldesc,
      2                  AS priority_ord,
      null                  AS event_status_db_id,
      null    AS event_status_cd
   FROM
      DUAL
   UNION ALL
   SELECT
      0                  AS request_status_db_id, 
      'OPEN' AS request_status_cd, 
      'OPEN' AS user_cd, 
      'Material request is open.' AS request_status_sdesc, 
      'Indicates that at least one request is open.' AS request_status_ldesc,
      0                  AS priority_ord,
      0                  AS event_status_db_id,
      'PROPEN'    AS event_status_cd
   FROM
      DUAL
) new_value
   ON
(
   new_value.request_status_db_id = ref_material_req_status.request_status_db_id AND
   new_value.request_status_cd    = ref_material_req_status.request_status_cd 
)
WHEN MATCHED THEN
UPDATE
SET
   user_cd              = new_value.user_cd,
   request_status_sdesc = new_value.request_status_sdesc, 
   request_status_ldesc = new_value.request_status_ldesc,
   priority_ord         = new_value.priority_ord,
   event_status_db_id   = new_value.event_status_db_id,
   event_status_cd      = new_value.event_status_cd
WHEN NOT MATCHED THEN
INSERT
(
   request_status_db_id,
   request_status_cd,
   user_cd,
   request_status_sdesc,
   request_status_ldesc,
   priority_ord,
   event_status_db_id,
   event_status_cd
)
VALUES
(
   new_value.request_status_db_id,
   new_value.request_status_cd,
   new_value.user_cd,
   new_value.request_status_sdesc,
   new_value.request_status_ldesc,
   new_value.priority_ord,
   new_value.event_status_db_id,
   new_value.event_status_cd
);

--changeSet OPER-10616:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table REF_MATERIAL_REQ_STATUS modify (PRIORITY_ORD Number(4,0) NOT NULL)
   ');
END;
/

--changeSet OPER-10616:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE PART_REQUEST_STATUS_PKG
IS

/********************************************************************************
*
* Package:     PART_REQUEST_STATUS_PKG
* Description: This package is used to perform various actions on the part request
*              1) Get the part request statuc for the given sched_db_id and sched_id,
*              2) Get the part request ETA
*              3) Get the req_part key,
*              4) Get the Part Request ETA Date
*
* Orig.Coder:   bparekh
* Recent Coder:
* Recent Date:  August 5, 2010
*
*********************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

/*---------------------------------- Functions -----------------------------*/

/* This function will return the part request status */
FUNCTION getPartRequestStatus (
      aSchedDbId IN NUMBER,
      aSchedId   IN NUMBER
   ) RETURN VARCHAR2;

/* This function will return the Estimated Time of Arrival (ETA) of the part request */
FUNCTION getPartRequestETA (
      aSchedDbId IN NUMBER,
      aSchedId   IN NUMBER
   ) RETURN DATE;

/* This function will return a warning bool by comparing the ETA and the scheduled date */
FUNCTION getPartRequestWarning (
      aSchedDbId IN NUMBER,
      aSchedId   IN NUMBER
   ) RETURN NUMBER;

/* This function will return the req_part_db_id */
FUNCTION getReqPartDbId (
      aSchedDbId IN NUMBER,
      aSchedId   IN NUMBER
   ) RETURN VARCHAR2;

/* This function will return the req_part_id */
FUNCTION getReqPartId (
      aSchedDbId IN NUMBER,
      aSchedId   IN NUMBER
   ) RETURN VARCHAR2;


/* This function will return the req_part_date value */
FUNCTION getReqPartDate (
      aSchedDbId IN NUMBER,
      aSchedId   IN NUMBER
   ) RETURN DATE;

/* This function will return the part request ETA Date */
FUNCTION getPartRequestETADate (
      aRequestStatusCd IN VARCHAR,
      aPartRequestETA IN DATE
   ) RETURN DATE;

END PART_REQUEST_STATUS_PKG;
/

--changeSet OPER-10616:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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