--liquibase formatted sql

--changeSet OPER-27394:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-27394:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
