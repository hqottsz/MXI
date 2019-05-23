--liquibase formatted sql

--changeSet OPER-27394:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments create the intermediate results storage table for MView refresh.
BEGIN
	utl_migr_schema_pkg.table_create('
	CREATE TABLE "MT_MAT_REQ_STAT_LOG"
	(
		"SCHED_DB_ID" NUMBER(10,0),
		"SCHED_ID" NUMBER(10,0),
		"REQUEST_STATUS_DB_ID" NUMBER,
		"REQUEST_STATUS_CD" VARCHAR2(4000 BYTE),
		"LATEST_ESTIMATED_TIME" DATE,
		"REQ_PART_DB_ID" VARCHAR2(4000 BYTE),
		"REQ_PART_ID" VARCHAR2(4000 BYTE),
		"WARNING_BOOL" NUMBER,
		"REQ_PART_SDESC" VARCHAR2(16 BYTE)
	)
	PCTFREE 0 PCTUSED 99
	');
END;
/

--changeSet OPER-27394:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments alter column definitions to match 
BEGIN
    utl_migr_schema_pkg.table_column_modify('
        ALTER TABLE MT_MAT_REQ_STAT_LOG modify (
            REQUEST_STATUS_CD VARCHAR2(16),
            REQ_PART_DB_ID VARCHAR2(200),
            REQ_PART_ID VARCHAR2(200),
            REQ_PART_SDESC VARCHAR2(20)
        )    
    ');
END;
/

--changeSet OPER-27394:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments remove Materialized View MT_MATERIALS_REQUEST_STATUS
BEGIN
    UTL_MIGR_SCHEMA_PKG.materialized_view_drop('MT_MATERIALS_REQUEST_STATUS');
END;
/

--changeSet OPER-27394:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments create a materialized view storing final results of refresh job MATERIALS_REQUEST_STATUS.
BEGIN
	utl_migr_schema_pkg.table_create('
/********************************************************************************
*
* Materialized View: MT_MATERIALS_REQUEST_STATUS
*
* Description:    This materialized view will return the details of the
*                 part requests
*
*********************************************************************************/
CREATE MATERIALIZED VIEW MT_MATERIALS_REQUEST_STATUS
PCTFREE 0
PCTUSED 99
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT  *
FROM  MT_MAT_REQ_STAT_LOG
	');
END;
/

--changeSet OPER-27394:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments remove Materialized View MV_MRS_DRIVING
BEGIN
    UTL_MIGR_SCHEMA_PKG.materialized_view_drop('MV_MRS_DRIVING');
END;
/

--changeSet OPER-27394:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments create a materialized view storing requests to be processed for refresh job.
BEGIN
	utl_migr_schema_pkg.table_create('
/********************************************************************************
*
* Materialized View: MV_MRS_DRIVING
*
* Description:    This materialized view is used as part of the materials request
*                 status job. It produces the initial list of tasks to work on.
*
*
*********************************************************************************/
CREATE MATERIALIZED VIEW MV_MRS_DRIVING
PCTFREE 0
PCTUSED 99
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT
   sched_stask.sched_db_id      AS sched_db_id,
   sched_stask.sched_id         AS sched_id,
   0                            AS request_status_db_id,
   req_task_class.task_class_cd AS req_part_sdesc
FROM
   sched_stask
   INNER JOIN evt_event ON
   evt_event.event_db_id = sched_stask.sched_db_id AND
   evt_event.event_id = sched_stask.sched_id
   INNER JOIN ref_task_class ON
   ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
   ref_task_class.task_class_cd = sched_stask.task_class_cd
   LEFT OUTER JOIN sched_stask req_stask ON
   evt_event.nh_event_db_id = req_stask.sched_db_id AND
   evt_event.nh_event_id = req_stask.sched_id
   LEFT OUTER JOIN ref_task_class req_task_class ON
   req_task_class.task_class_db_id = req_stask.task_class_db_id AND
   req_task_class.task_class_cd = req_stask.task_class_cd
WHERE
   ref_task_class.class_mode_cd NOT IN (''BLOCK'') AND
   evt_event.hist_bool = 0 AND
   ((req_task_class.task_class_cd IS NULL) OR (req_task_class.task_class_cd NOT IN (''BLOCK'')))
	');
END;
/


--changeSet OPER-27394:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments remove replaced Materialized View MV_MATERIALS_REQUEST_STATUS
BEGIN
    UTL_MIGR_SCHEMA_PKG.materialized_view_drop('MV_MATERIALS_REQUEST_STATUS');
END;
/


--changeSet OPER-27394:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments add index to Materialized View MT_MATERIALS_REQUEST_STATUS
BEGIN
      UTL_MIGR_SCHEMA_PKG.index_create( 'CREATE INDEX ix_mv_mc ON MT_MATERIALS_REQUEST_STATUS(SCHED_DB_ID, SCHED_ID)');
END;
/


--changeSet OPER-27394:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments create view as a public object to access Materials Request status data. This keeps current name for backwards compatibility to existing queries.
BEGIN
	utl_migr_schema_pkg.table_create('
/********************************************************************************
*
* View:           mv_material_request_status
*
* Description:    This materialized view will return the details of the
*                 part requests
*
* Orig.Coder:    bparekh
* Recent Coder:  gtate
* Recent Date:   12 DEC 2018
*
*********************************************************************************/
CREATE OR REPLACE FORCE VIEW "MV_MATERIALS_REQUEST_STATUS"
AS
SELECT
   *
FROM
   MT_MATERIALS_REQUEST_STATUS');
END;
/



--changeSet OPER-27394:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments create new package header to handle Materialized View Refresh
create or replace PACKAGE MVIEW_REFRESH_PKG AS
/********************************************************************************
*
* Description: This package should be used as entry point for
*              materialized view refresh from the Maintenix job framework.
*              A single public refresh procedure is used. Internally, private
*              procedures are defined and added for each materialized view.
*
********************************************************************************/
	PROCEDURE REFRESH (aiMVName in varchar2);

END MVIEW_REFRESH_PKG;
/

--changeSet OPER-27394:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments create new package body to handle Materialized View Refresh
create or replace PACKAGE BODY MVIEW_REFRESH_PKG AS


/********************************************************************************
*
* Procedure:    MAT_REQ_STATUS_REFRESH
*
* Arguments:	aiDegree - The parallelism to be used. Testing indicated default of 4
*               is reasonable in both large and small systems. Higher numbers do not
*               decrease time in linear fashion for thia particular job.
*
* Return: N/A
*
* Description:  Logic to handle refresh of Materials Request Status information.
*               Essentially works in 3 phases -
*               [collect keys of work] -> [work in parallel] -> [Refresh MView]
*
********************************************************************************/
PROCEDURE MAT_REQ_STATUS_REFRESH (aiDegree IN NUMBER DEFAULT 4)
IS
    l_sql_stmt		VARCHAR2(4000);
    l_try           NUMBER;
    l_status        NUMBER;
    l_task_name     VARCHAR2 (64);

	BEGIN


	-- Collect PKs of REQs of interest
	BEGIN
		DBMS_MVIEW.REFRESH ('MV_MRS_DRIVING', 'C');
	EXCEPTION
		WHEN OTHERS THEN NULL;
	END;

	-- Ensure not possible to execute same task.
	l_task_name := DBMS_PARALLEL_EXECUTE.GENERATE_TASK_NAME ();
	BEGIN
		   DBMS_PARALLEL_EXECUTE.DROP_TASK(l_task_name);
	EXCEPTION
		   WHEN OTHERS THEN NULL;
	END;

	-- Clean prep table
	DELETE FROM MT_MAT_REQ_STAT_LOG;
	COMMIT;

	-- Create the TASK
    DBMS_PARALLEL_EXECUTE.CREATE_TASK (l_task_name);

	-- Chunk the table by ROWID 10,000 rows at a time
    DBMS_PARALLEL_EXECUTE.CREATE_CHUNKS_BY_ROWID(l_task_name, user, 'MV_MRS_DRIVING', true, 10000);

	-- Statement executed for each chunk
	l_sql_stmt :=
			'INSERT INTO MT_MAT_REQ_STAT_LOG
			 SELECT
				sched_db_id,
				sched_id,
				request_status_db_id,
				PART_REQUEST_STATUS_PKG.getPartRequestStatus( sched_db_id, sched_id) AS request_status_cd,
				PART_REQUEST_STATUS_PKG.getPartRequestETA( sched_db_id, sched_id) AS latest_estimated_time,
				PART_REQUEST_STATUS_PKG.getReqPartDbId(sched_db_id,sched_id) AS req_part_db_id,
				PART_REQUEST_STATUS_PKG.getReqPartId(sched_db_id,sched_id) AS req_part_id,
				PART_REQUEST_STATUS_PKG.getPartRequestWarning(sched_db_id,sched_id) AS warning_bool,
				req_part_sdesc
			 FROM
				MV_MRS_DRIVING
			 WHERE
			    rowid BETWEEN :start_id AND :end_id';


	-- Finally, execute tasks
	DBMS_PARALLEL_EXECUTE.RUN_TASK(
				l_task_name,
				l_sql_stmt,
				DBMS_SQL.NATIVE,
				parallel_level => aiDegree
				);

	-- If there is an error, RESUME it for at most 2 times.
	l_try    := 0;
	l_status := DBMS_PARALLEL_EXECUTE.TASK_STATUS(l_task_name);

	WHILE(l_try < 2 and l_status != DBMS_PARALLEL_EXECUTE.FINISHED)
	LOOP
		l_try := l_try + 1;
		DBMS_PARALLEL_EXECUTE.RESUME_TASK(l_task_name);
		l_status := DBMS_PARALLEL_EXECUTE.TASK_STATUS(l_task_name);
	END LOOP;

	-- Done with processing; drop the task
	DBMS_PARALLEL_EXECUTE.DROP_TASK(l_task_name);

	-- Execute final refresh of consumer, ie Materials REQ status MView
	BEGIN
		DBMS_MVIEW.REFRESH ('MT_MATERIALS_REQUEST_STATUS', 'C');
	EXCEPTION
		WHEN OTHERS THEN NULL;
	END;


    RETURN;

	-- Final exception handler
	EXCEPTION
    WHEN OTHERS THEN
		BEGIN
			DBMS_PARALLEL_EXECUTE.DROP_TASK(l_task_name);
		EXCEPTION
			WHEN OTHERS THEN RAISE;
		END;
    RAISE;

	END MAT_REQ_STATUS_REFRESH;



/********************************************************************************
*
* Procedure:    REFRESH
*
* Arguments:	aiMVName - Name of Materialized View to be refreshed
*
* Return: N/A
*
* Description:  Public endpoint to be called by Maintenix Job Framework. Further
*               logic is processed for Materialized Views individually in private
*               procedures. If no specialized logic is necessary, this procedure
*               handles default complete refresh operation.
*
********************************************************************************/
	PROCEDURE REFRESH (aiMVName in varchar2) AS
	BEGIN
		--simple sanitation checks
		IF (aiMVName IS NULL)
		THEN
		   RETURN;
		END IF;

		IF (LENGTH (aiMVName) <= 0)
		THEN
		   RETURN;
		END IF;

		IF (aiMVName = 'MT_MATERIALS_REQUEST_STATUS')
		THEN
		   MAT_REQ_STATUS_REFRESH;
		   RETURN;
		END IF;

		-- DEFAULT case, no special logic, complete refresh.
		DBMS_MVIEW.refresh( aiMVName, 'C' );

	END REFRESH;


END MVIEW_REFRESH_PKG;
/


--changeSet OPER-27394:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments remove replaced global procedure
BEGIN
    UTL_MIGR_SCHEMA_PKG.procedure_drop('refreshMView');
END;
/


--changeSet OPER-27394:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-27394:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-27394:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comments Adding table comments to MT_MAT_REQ_STAT_LOG
BEGIN

EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_MAT_REQ_STAT_LOG
IS
  'This table is used as intermediate storage for results for Materials Request Status Job. It is not to be used directly. Instead, please execute JOIN or logic operations on MV_MATERIALS_REQUEST_STATUS object. ']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.SCHED_DB_ID
IS
  'FK to sched_stask']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.SCHED_ID
IS
  'FK to sched_stask']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.REQUEST_STATUS_DB_ID
IS
  'Ref to ref_material_req_status']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.REQUEST_STATUS_CD
IS
  'Ref to ref_material_req_status']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.LATEST_ESTIMATED_TIME
IS
  'Ref to req_part updated_eta or est_arrival_dt']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.REQ_PART_DB_ID
IS
  'FK to req_part']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.REQ_PART_ID
IS
  'FK to req_part']' ;
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_MAT_REQ_STAT_LOG.WARNING_BOOL
IS
  'Description of part']';

END;
/
