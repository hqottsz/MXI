--liquibase formatted sql


--changeSet DEV-459:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_MATERIAL_REQ_STATUS" (
	"REQUEST_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REQUEST_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REQUEST_STATUS_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"USER_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"REQUEST_STATUS_SDESC" Varchar2 (80),
	"REQUEST_STATUS_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_MATERIAL_REQ_STATUS" primary key ("REQUEST_STATUS_DB_ID","REQUEST_STATUS_CD") 
) 

');
END;
/

--changeSet DEV-459:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ref_material_req_status" add Constraint "FK_MIMRSTAT_REFMATREQSTATUS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-459:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_MAT_REQ_STATUS" BEFORE INSERT
   ON "REF_MATERIAL_REQ_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-459:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_MAT_REQ_STATUS" BEFORE UPDATE
   ON "REF_MATERIAL_REQ_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-459:5 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 0, 'READY', 'READY', 'All requested parts for this task and subtasks are available.', 'Indicates that all the requested parts for this task and subtasks are available.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_material_req_status WHERE ref_material_req_status.request_status_db_id = 0 AND ref_material_req_status.request_status_cd = 'READY');

--changeSet DEV-459:6 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 0, 'NOT READY', 'NOT READY', 'One/more requested parts for the task/subtask are not available.', 'Indicates that one or more requested parts for this task and subtasks are not available.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_material_req_status WHERE ref_material_req_status.request_status_db_id = 0 AND ref_material_req_status.request_status_cd = 'NOT READY');

--changeSet DEV-459:7 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 0, 'AWAITING', 'AWAITING', 'One/more requested parts for the task/subtask has an expected time of arrival.', 'Indicates that one or more requested parts for the task/subtask has an expected time of arrival.  Used for internal logic and not for display purposes.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_material_req_status WHERE ref_material_req_status.request_status_db_id = 0 AND ref_material_req_status.request_status_cd = 'AWAITING');

--changeSet DEV-459:8 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 0, 'NOT KNOWN', 'N/A', 'The status of part request is not known.', 'Indicates that the status of part request is not known.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_material_req_status WHERE ref_material_req_status.request_status_db_id = 0 AND ref_material_req_status.request_status_cd = 'NOT KNOWN');

--changeSet DEV-459:9 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 0, 'N/A', 'N/A', 'Material request status not applicable for the task.', 'Indicates that material request status is not applicable for the task.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_material_req_status WHERE ref_material_req_status.request_status_db_id = 0 AND ref_material_req_status.request_status_cd = 'N/A');

--changeSet DEV-459:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet DEV-459:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
    lDbId         VARCHAR2(200);
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
    lId           VARCHAR2(200);
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
        lPartRequestETADate := sysdate - interval '10' year;
    ELSIF aRequestStatusCd = 'NOT READY' THEN
        lPartRequestETADate := sysdate - interval '20' year;
    ELSIF aRequestStatusCd = 'AWAITING' THEN
        IF aPartRequestETA is NOT NULL THEN
            lPartRequestETADate := aPartRequestETA;
        ELSE
            lPartRequestETADate := sysdate - interval '20' year;
        END IF;
    ELSIF aRequestStatusCd = 'NOT KNOWN' THEN
        lPartRequestETADate := sysdate + interval '100' year;
    ELSIF aRequestStatusCd = 'N/A' THEN
        lPartRequestETADate := sysdate + interval '100' year;
    END IF;
    
    RETURN lPartRequestETADate;

   

END getPartRequestETADate;


END PART_REQUEST_STATUS_PKG;
/

--changeSet DEV-459:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('MV_MATERIALS_REQUEST_STATUS');
END;
/

--changeSet DEV-459:13 stripComments:false
/********************************************************************************
*
* View:           mv_material_request_status
*
* Description:    This materialized view will return the details of the
*                 part requests
*
* Orig.Coder:    bparekh
* Recent Coder:
* Recent Date:   2010.06.23
*
*********************************************************************************/
CREATE MATERIALIZED VIEW MV_MATERIALS_REQUEST_STATUS
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT
   sched_stask.sched_db_id AS sched_db_id,
   sched_stask.sched_id AS sched_id,
   0 AS request_status_db_id,
   PART_REQUEST_STATUS_PKG.getPartRequestStatus(sched_stask.sched_db_id,sched_stask.sched_id) AS request_status_cd,
   PART_REQUEST_STATUS_PKG.getPartRequestETA(sched_stask.sched_db_id,sched_stask.sched_id) AS latest_estimated_time,
   PART_REQUEST_STATUS_PKG.getReqPartDbId(sched_stask.sched_db_id,sched_stask.sched_id) AS req_part_db_id,
   PART_REQUEST_STATUS_PKG.getReqPartId(sched_stask.sched_db_id,sched_stask.sched_id) AS req_part_id,
   PART_REQUEST_STATUS_PKG.getPartRequestWarning(sched_stask.sched_db_id,sched_stask.sched_id) AS warning_bool,
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
   ref_task_class.class_mode_cd NOT IN ('BLOCK') AND
   evt_event.hist_bool = 0 AND           
   ((req_task_class.task_class_cd IS NULL) OR (req_task_class.task_class_cd NOT IN ('BLOCK')));

--changeSet DEV-459:14 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
SELECT 'MX_CORE_MVIEW_TASK_MATERIAL_REQUEST_STATUS', 'Refresh the Task Material Request Status MView', null, 60, 60, 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_job WHERE utl_job.job_cd = 'MX_CORE_MVIEW_TASK_MATERIAL_REQUEST_STATUS');