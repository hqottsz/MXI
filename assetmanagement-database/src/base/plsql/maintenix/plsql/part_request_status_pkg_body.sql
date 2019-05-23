--liquibase formatted sql

--changeSet PART_REQUEST_STATUS_PKG_BODY:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments
create or replace PACKAGE BODY PART_REQUEST_STATUS_PKG
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
   aSchedId   IN NUMBER
) RETURN VARCHAR2

IS
    lPartRequestStatus NUMBER(6);
    lStatus            VARCHAR2 (100);

    CURSOR lCur_PartRequestStatus
	(
		aSchedDbId evt_event.event_db_id%TYPE,
		aSchedId   evt_event.event_id%TYPE
	)
    IS
	SELECT
		*
	FROM
		(
		SELECT
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
            req_part.req_part_id    = req_part_event.event_id
        LEFT OUTER JOIN ref_material_req_status ON
            ref_material_req_status.event_status_db_id = req_part_event.event_status_db_id AND
            ref_material_req_status.event_status_cd    = req_part_event.event_status_cd

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
				   evt_event.nh_event_id    = PRIOR evt_event.event_id
			)
		)
		ORDER BY status_ord DESC
	;

   lReq_PartRequestStatus lCur_PartRequestStatus%ROWTYPE;

   ln_maxetaidx integer := 0;
   ln_maxdate   date    := to_date ('1900-JAN-01', 'yyyy-mon-dd');


BEGIN
    --
	IF ((gt_mxkey.db_id IS NOT NULL) AND (gt_mxkey.id IS NOT NULL))
	THEN
		IF ((gt_mxkey.db_id = aSchedDbId) AND (gt_mxkey.id = aSchedId))
		THEN
			IF (gn_count = 0)
			THEN
				RETURN ('N/A');
			END IF;

			RETURN  (gt_result_array_nulls_first.lPartRequestEta);
		END IF;
	END IF;

	IF (gCur_PartRequestStatus%isopen)
	THEN
		CLOSE gCur_PartRequestStatus;
	END IF;

	-- Cleanup record objects
	gt_result_array_nulls_last.delete ();

	gt_result_array_nulls_first.status_cd        :=  null;
	gt_result_array_nulls_first.status_ord       :=  null;
	gt_result_array_nulls_first.req_part_db_id   :=  null;
	gt_result_array_nulls_first.req_part_id      :=  null;
	gt_result_array_nulls_first.lPartRequestEta  :=  null;

	OPEN  gCur_PartRequestStatus (aSchedDbId, aSchedId);
		FETCH gCur_PartRequestStatus
		BULK COLLECT INTO gt_result_array_nulls_last;
	CLOSE gCur_PartRequestStatus;

	gn_count       := gt_result_array_nulls_last.count ();
	gt_mxkey.db_id := aSchedDbId;
	gt_mxkey.id    := aSchedId;

	IF (gn_count = 0)
	THEN
		RETURN ('N/A');
	END IF;

	IF (gn_count = 1)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (1);
		RETURN  (gt_result_array_nulls_last(1).status_cd);
	END IF;


	FOR i IN 1.. gn_count
	LOOP
		IF (gt_result_array_nulls_last(i).lPartRequestEta IS NULL)
		THEN
			gt_result_array_nulls_first  := gt_result_array_nulls_last (i);
			RETURN  (gt_result_array_nulls_last(1).status_cd);
		ELSE
			IF (gt_result_array_nulls_last (i).lPartRequestEta > ln_maxdate)
			THEN
				ln_maxetaidx := i;
				ln_maxdate   := gt_result_array_nulls_last (i).lPartRequestEta;
			END IF;
		END IF;
	END LOOP;

	IF (ln_maxetaidx > 0)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (ln_maxetaidx);
	END IF;

	RETURN  (gt_result_array_nulls_last(1).status_cd);

	EXCEPTION
		WHEN OTHERS THEN
		IF (gCur_PartRequestStatus%isopen)
		THEN
			CLOSE gCur_PartRequestStatus;
		END IF;
	RETURN (sqlerrm);

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
	aSchedId   IN NUMBER
) RETURN DATE
IS
    lPartRequestEta DATE;
    lEta            DATE;

    CURSOR lCur_PartRequestETA
	(
		aSchedDbId evt_event.event_db_id%TYPE,
		aSchedId   evt_event.event_id%TYPE
	)
    IS
		SELECT
			CASE
				-- Get the eta date for PRONORDER, PRRESERVE and PRREMOTE part request. If updated_eta is provided, use it; otherwise, use est_arrival_dt.
				WHEN (req_part_event.event_status_db_id, req_part_event.event_status_cd)
				IN
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
				req_part.req_part_id    = req_part_event.event_id

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
				   evt_event.nh_event_id    = PRIOR evt_event.event_id
			)
		ORDER BY
			lPartRequestEta DESC NULLS FIRST
	;

	lReq_PartRequestETA lCur_PartRequestETA%ROWTYPE;


	ln_maxetaidx integer := 0;
	ln_maxdate   date    := to_date ('1900-JAN-01', 'yyyy-mon-dd');

BEGIN

	IF ((gt_mxkey.db_id IS NOT NULL) AND (gt_mxkey.id IS NOT NULL))
	THEN
		IF ((gt_mxkey.db_id = aSchedDbId) AND (gt_mxkey.id = aSchedId))
		THEN
			IF (gn_count = 0)
			THEN
				RETURN (null);
			END IF;

			RETURN  (gt_result_array_nulls_first.lPartRequestEta);
		END IF;
	END IF;

	IF (gCur_PartRequestStatus%isopen)
	THEN
		CLOSE gCur_PartRequestStatus;
	END IF;

	gt_result_array_nulls_last.delete ();
	gt_result_array_nulls_first.status_cd        :=  null;
	gt_result_array_nulls_first.status_ord       :=  null;
	gt_result_array_nulls_first.req_part_db_id   :=  null;
	gt_result_array_nulls_first.req_part_id      :=  null;
	gt_result_array_nulls_first.lPartRequestEta  :=  null;

	OPEN  gCur_PartRequestStatus (aSchedDbId, aSchedId);
		FETCH gCur_PartRequestStatus
		BULK COLLECT INTO gt_result_array_nulls_last;
	CLOSE gCur_PartRequestStatus;

	gn_count       := gt_result_array_nulls_last.count ();
	gt_mxkey.db_id := aSchedDbId;
	gt_mxkey.id    := aSchedId;


	IF (gn_count = 0)
	THEN
		RETURN (null);
	END IF;

	IF (gn_count = 1)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (1);
        RETURN  (gt_result_array_nulls_first.lPartRequestEta);
	END IF;


	FOR i IN 1.. gn_count
	LOOP
		IF (gt_result_array_nulls_last(i).lPartRequestEta IS NULL)
		THEN
			gt_result_array_nulls_first  := gt_result_array_nulls_last (i);
			RETURN  (gt_result_array_nulls_first.lPartRequestEta);
		ELSE
			IF (gt_result_array_nulls_last (i).lPartRequestEta > ln_maxdate)
			THEN
				ln_maxetaidx := i;
				ln_maxdate   := gt_result_array_nulls_last (i).lPartRequestEta;
			END IF;
		END IF;
	END LOOP;

	IF (ln_maxetaidx > 0)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (ln_maxetaidx);
	END IF;

	RETURN  (gt_result_array_nulls_first.lPartRequestEta);

	EXCEPTION
	WHEN OTHERS
	THEN
		IF (gCur_PartRequestStatus%isopen)
		THEN
			CLOSE gCur_PartRequestStatus;
		END IF;
    RETURN (null);

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
	aSchedId   IN NUMBER
) RETURN NUMBER
IS
	lEstimatedArrivalDate VARCHAR2(200);
	lEta                  VARCHAR2(200);
	lWarningBool          NUMBER(1);
	lSchedDate            DATE;
	lReqPartDate          DATE;


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
		evt_sched_dead.event_db_id       = aSchedDbId AND
		evt_sched_dead.event_id          = aSchedId   AND
		evt_sched_dead.sched_driver_bool = 1;

	lReq_PartRequestWarning lCur_PartRequestWarning%ROWTYPE;


BEGIN

   lReqPartDate := getReqPartDate(aSchedDbId,aSchedId);
   lEta         := getPartRequestETA(aSchedDbId,aSchedId);

   OPEN lCur_PartRequestWarning(aSchedDbId, aSchedId);
   FETCH lCur_PartRequestWarning INTO lReq_PartRequestWarning;

	IF lReqPartDate IS NULL AND lCur_PartRequestWarning%ROWCOUNT = 0
	THEN
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
	aSchedId   IN NUMBER
) RETURN VARCHAR2
IS
    lPartRequestEta DATE;
    lDbId 		    VARCHAR2(200);
    lReqPartDbId    VARCHAR2(200);

    CURSOR lCur_ReqPartDbId
	(
		aSchedDbId evt_event.event_db_id%TYPE,
		aSchedId   evt_event.event_id%TYPE
	)
    IS
		SELECT
			CASE
			-- Get the eta date for PRONORDER, PRRESERVE and PRREMOTE part request. If updated_eta is provided, use it; otherwise, use est_arrival_dt.
			WHEN (req_part_event.event_status_db_id, req_part_event.event_status_cd)
				IN
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
				   evt_event.nh_event_id    = PRIOR evt_event.event_id
			)
    ORDER BY
		lPartRequestEta DESC NULLS FIRST
	;

	lReq_ReqPartDbId lCur_ReqPartDbId%ROWTYPE;

    ln_maxetaidx integer := 0;
	ln_maxdate   date    := to_date ('1900-JAN-01', 'yyyy-mon-dd');

BEGIN


	IF ((gt_mxkey.db_id IS NOT NULL) AND (gt_mxkey.id IS NOT NULL))
	THEN
		IF ((gt_mxkey.db_id = aSchedDbId) AND (gt_mxkey.id = aSchedId))
		THEN
			IF (gn_count = 0)
			THEN
				RETURN (null);
			END IF;
		IF (gt_result_array_nulls_first.lPartRequestEta IS NULL)
		THEN
			RETURN (null);
		END IF;
			RETURN  (gt_result_array_nulls_first.req_part_db_id);
		END IF;
	END IF;

	IF (gCur_PartRequestStatus%isopen)
	THEN
		CLOSE gCur_PartRequestStatus;
	END IF;

   gt_result_array_nulls_last.delete ();
   gt_result_array_nulls_first.status_cd        :=  null;
   gt_result_array_nulls_first.status_ord       :=  null;
   gt_result_array_nulls_first.req_part_db_id   :=  null;
   gt_result_array_nulls_first.req_part_id      :=  null;
   gt_result_array_nulls_first.lPartRequestEta  :=  null;

	OPEN  gCur_PartRequestStatus (aSchedDbId, aSchedId);
		FETCH gCur_PartRequestStatus
		BULK COLLECT INTO gt_result_array_nulls_last;
	CLOSE gCur_PartRequestStatus;


	gn_count       := gt_result_array_nulls_last.count ();
	gt_mxkey.db_id := aSchedDbId;
	gt_mxkey.id    := aSchedId;


	IF (gn_count = 0)
	THEN
		RETURN (null);
	END IF;

	IF (gn_count = 1)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (1);
		RETURN  (gt_result_array_nulls_first.req_part_db_id);
	END IF;


	FOR i IN 1.. gn_count
	LOOP
		IF (gt_result_array_nulls_last(i).lPartRequestEta IS NULL)
		THEN
			gt_result_array_nulls_first  := gt_result_array_nulls_last (i);
			RETURN  (gt_result_array_nulls_first.req_part_db_id);
		ELSE
			IF (gt_result_array_nulls_last (i).lPartRequestEta > ln_maxdate)
			THEN
				ln_maxetaidx := i;
				ln_maxdate   := gt_result_array_nulls_last (i).lPartRequestEta;
			END IF;
		END IF;
	END LOOP;

	IF (ln_maxetaidx > 0)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (ln_maxetaidx);
	END IF;

	RETURN  (gt_result_array_nulls_first.req_part_db_id);

	EXCEPTION
		WHEN OTHERS THEN
		IF (gCur_PartRequestStatus%isopen)
		THEN
			CLOSE gCur_PartRequestStatus;
		END IF;
    RETURN (null);

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
	aSchedId   IN NUMBER
) RETURN VARCHAR2
IS
    lPartRequestEta DATE;
    lId 		    VARCHAR2(200);
    lReqPartId      VARCHAR2(200);

    CURSOR lCur_ReqPartId
	(
		aSchedDbId evt_event.event_db_id%TYPE,
		aSchedId   evt_event.event_id%TYPE
	)
    IS
		SELECT
			CASE
			-- Get the eta date for PRONORDER, PRRESERVE and PRREMOTE part request. If updated_eta is provided, use it; otherwise, use est_arrival_dt.
			WHEN (req_part_event.event_status_db_id, req_part_event.event_status_cd)
				IN
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
            req_part.req_part_id    = req_part_event.event_id

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
				   evt_event.nh_event_id    = PRIOR evt_event.event_id
			)
      ORDER BY
	     lPartRequestEta DESC NULLS FIRST
	;

	lReq_ReqPartId lCur_ReqPartId%ROWTYPE;

    ln_maxetaidx integer := 0;
    ln_maxdate   date    := to_date ('1900-JAN-01', 'yyyy-mon-dd');

BEGIN


	IF ((gt_mxkey.db_id IS NOT NULL) AND (gt_mxkey.id IS NOT NULL))
	THEN
		IF ((gt_mxkey.db_id = aSchedDbId) AND (gt_mxkey.id = aSchedId))
		THEN
			IF (gn_count = 0)
			THEN
				RETURN (null);
			END IF;

			IF (gt_result_array_nulls_first.lPartRequestEta IS NULL)
			THEN
				RETURN (null);
			END IF;

			RETURN  (gt_result_array_nulls_first.req_part_id);
		END IF;
	END IF;



	IF (gCur_PartRequestStatus%isopen)
	THEN
		CLOSE gCur_PartRequestStatus;
	END IF;

	gt_result_array_nulls_last.delete ();
	gt_result_array_nulls_first.status_cd        :=  null;
	gt_result_array_nulls_first.status_ord       :=  null;
	gt_result_array_nulls_first.req_part_db_id   :=  null;
	gt_result_array_nulls_first.req_part_id      :=  null;
	gt_result_array_nulls_first.lPartRequestEta  :=  null;

	OPEN  gCur_PartRequestStatus (aSchedDbId, aSchedId);
		FETCH gCur_PartRequestStatus
		BULK COLLECT INTO gt_result_array_nulls_last;
	CLOSE gCur_PartRequestStatus;


   gn_count       := gt_result_array_nulls_last.count ();
   gt_mxkey.db_id := aSchedDbId;
   gt_mxkey.id    := aSchedId;


	IF (gn_count = 0)
	THEN
		RETURN (null);
	END IF;

	IF (gn_count = 1)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (1);
		IF (gt_result_array_nulls_first.lPartRequestEta IS NULL)
		THEN
			 RETURN (null);
		END IF;
		RETURN  (gt_result_array_nulls_first.req_part_id);
	END IF;

	RETURN (-1);


	FOR i IN 1.. gn_count
	LOOP
		IF (gt_result_array_nulls_last(i).lPartRequestEta IS NULL)
		THEN
			gt_result_array_nulls_first  := gt_result_array_nulls_last (i);
			RETURN (null);
		ELSE
			IF (gt_result_array_nulls_last (i).lPartRequestEta > ln_maxdate)
			THEN
				ln_maxetaidx := i;
				ln_maxdate   := gt_result_array_nulls_last (i).lPartRequestEta;
			END IF;
		END IF;
	END LOOP;


	IF (ln_maxetaidx > 0)
	THEN
		gt_result_array_nulls_first  := gt_result_array_nulls_last (ln_maxetaidx);
	END IF;

	IF (gt_result_array_nulls_first.lPartRequestEta IS NULL)
	THEN
		RETURN (null);
	END IF;

	RETURN  (gt_result_array_nulls_first.req_part_id);

	EXCEPTION
		WHEN OTHERS THEN
            IF (gCur_PartRequestStatus%isopen)
            THEN
                CLOSE gCur_PartRequestStatus;
            END IF;

        RETURN (null);

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
	aSchedId   IN NUMBER
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
       req_part.req_part_db_id = getReqPartDbId(aSchedDbId, aSchedId) AND
       req_part.req_part_id    = getReqPartId(aSchedDbId, aSchedId);
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
	aPartRequestETA  IN DATE
) RETURN DATE
IS
	lPartRequestETADate DATE;

BEGIN
	IF aRequestStatusCd      = 'PENDING' THEN
		lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
	ELSIF aRequestStatusCd   = 'ACKNOWLEDGED' THEN
		lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
	ELSIF aRequestStatusCd   = 'NOT AVAILABLE' THEN
		lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
	ELSIF aRequestStatusCd   = 'ON ORDER'
	THEN
		IF aPartRequestETA IS NOT NULL THEN
			lPartRequestETADate := aPartRequestETA;
		ELSE
			lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
		END IF;
	ELSIF aRequestStatusCd       = 'IN REPAIR'
	THEN
		IF aPartRequestETA IS NOT NULL THEN
			lPartRequestETADate := aPartRequestETA;
		ELSE
			lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
		END IF;
	ELSIF aRequestStatusCd       = 'IN TRANSIT'
	THEN
		IF aPartRequestETA IS NOT NULL THEN
			lPartRequestETADate := aPartRequestETA;
		ELSE
			lPartRequestETADate := ADD_MONTHS ( SYSDATE, -240 );
		END IF;
	ELSIF aRequestStatusCd   = 'ON HAND' THEN
		lPartRequestETADate := ADD_MONTHS ( SYSDATE, -120 );
	ELSIF aRequestStatusCd   = 'READY TO PICK UP' THEN
		lPartRequestETADate := ADD_MONTHS ( SYSDATE, -120 );
	ELSIF aRequestStatusCd   = 'N/A' THEN
		lPartRequestETADate := ADD_MONTHS ( SYSDATE, 1200 );
	ELSIF aRequestStatusCd   = 'NOT KNOWN' THEN
		lPartRequestETADate := ADD_MONTHS ( SYSDATE, 1200 );
	END IF;

   RETURN lPartRequestETADate;
END getPartRequestETADate;


BEGIN

	gt_result_array_nulls_last(1).status_cd        :=  null;
	gt_result_array_nulls_last(1).status_ord       :=  null;
	gt_result_array_nulls_last(1).req_part_db_id   :=  null;
	gt_result_array_nulls_last(1).req_part_id      :=  null;
	gt_result_array_nulls_last(1).lPartRequestEta  :=  null;
	gt_result_array_nulls_last.delete();


END PART_REQUEST_STATUS_PKG;
/