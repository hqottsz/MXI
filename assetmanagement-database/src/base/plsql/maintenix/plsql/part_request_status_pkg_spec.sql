--liquibase formatted sql


--changeSet part_request_status_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments re-define the PART_REQUEST_STATUS_PKG header
create or replace PACKAGE PART_REQUEST_STATUS_PKG
IS

/********************************************************************************
*
* Package:     PART_REQUEST_STATUS_PKG
* Description: This package is used to perform various actions on the part request
*              1) Get the part request status for the given sched_db_id and sched_id,
*              2) Get the part request ETA
*              3) Get the req_part key
*              4) Get the Part Request ETA Date
*
* Orig.Coder:   bparekh
* Recent Coder: gtate
* Recent Date:  10 Dec 2018
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
FUNCTION getPartRequestETADate
(
      aRequestStatusCd IN VARCHAR,
      aPartRequestETA  IN DATE
) RETURN DATE;

	CURSOR  gCur_PartRequestStatus (iSchedDbId in number, iSchedId in number)
	IS
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
		) AS status_ord ,
		req_part.req_part_db_id,
		req_part.req_part_id,
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
		   evt_event.event_db_id = iSchedDbId AND
		   evt_event.event_id    = iSchedId
		CONNECT BY
		   evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
		   evt_event.nh_event_id    = PRIOR evt_event.event_id
		)
	ORDER BY status_ord DESC
	;


	gt_mxkey mxkey  := mxkey (null, null);
	gn_count number := 0;

	gt_result_array_nulls_first gCur_PartRequestStatus%rowtype;
	gt_cs_test                  gCur_PartRequestStatus%rowtype;

	TYPE cs_cprs_array_t IS TABLE OF gCur_PartRequestStatus%rowtype
		INDEX BY BINARY_INTEGER;
	gt_result_array_nulls_last   cs_cprs_array_t;

END PART_REQUEST_STATUS_PKG;
/