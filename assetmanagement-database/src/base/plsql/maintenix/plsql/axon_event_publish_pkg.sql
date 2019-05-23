--liquibase formatted sql

--changeSet axon_event_publish_pkg:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment axon_event_publish_pkg spec definition
CREATE OR REPLACE PACKAGE axon_event_publish_pkg AS
/********************************************************************************
*
* Package:      axon_event_publish_pkg
*
* Description:  This package is used to publish axon events.
*
*********************************************************************************/
   icn_Success CONSTANT NUMBER := 1;
   icn_NoProc  CONSTANT NUMBER := 0;

   ics_TaskCreated_Evt          CONSTANT VARCHAR2(128) := 'com.mxi.mx.core.production.task.domain.TaskCreatedEvent';
   ics_TaskDrvDeadlineResch_Evt CONSTANT VARCHAR2(128) := 'com.mxi.mx.core.production.task.domain.TaskDrivingDeadlineRescheduledEvent';
   ics_TaskAssgToWp_Evt         CONSTANT VARCHAR2(128) := 'com.mxi.mx.core.production.task.domain.TaskAssignedToWorkPackageEvent';
   ics_TaskUnassnFrWp_Evt       CONSTANT VARCHAR2(128) := 'com.mxi.mx.core.production.task.domain.TaskUnassignedFromWorkPackageEvent';

   ics_Meta_Data CONSTANT VARCHAR2(8) := '{ }';
   
   is_EnableAxonFrameworkFailSafe VARCHAR2(5) DEFAULT NULL;

   FUNCTION dl_get_tsk_created_evt_payload (
      an_SchedDbId               sched_stask.sched_db_id%TYPE,
      an_SchedId                 sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId        task_task.task_db_id%TYPE,
      an_OrigTaskTaskId          task_task.task_id%TYPE,
      an_MaxRangeQt              task_task.forecast_range_qt%TYPE,
      an_InvNoDbId               inv_inv.inv_no_db_id%TYPE,
      an_InvNoId                 inv_inv.inv_no_id%TYPE,
      an_PreviousTaskDbId        evt_event.event_db_id%TYPE,
      an_PreviousTaskId          evt_event.event_id%TYPE,
      ad_PreviousCompletionDt    evt_event.sched_end_gdt%TYPE,
      as_UserNote                evt_stage.stage_note%TYPE,
      an_HrDbId                  org_hr.hr_db_id%TYPE,
      an_HrId                    org_hr.hr_id%TYPE,
      ab_Historic                NUMBER
   ) RETURN BLOB;

   FUNCTION get_axon_identifier RETURN VARCHAR2;

   FUNCTION dl_get_tsk_assgn_to_wk_evt_pld (
      an_WorkPackageTaskDbId   evt_event.event_db_id%TYPE,
      an_WorkPackageTaskId     evt_event.event_id%TYPE,
      an_TaskDbId              evt_event.event_db_id%TYPE,
      an_TaskId                evt_event.event_id%TYPE,
      an_HrDbId                org_hr.hr_db_id%TYPE,
      an_HrId                  org_hr.hr_id%TYPE,
      as_Note                  evt_stage.stage_note%TYPE,
      an_ReasonDbId            evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd              evt_stage.stage_reason_cd%TYPE
   ) RETURN BLOB;

   FUNCTION dl_get_tsk_deadl_reshd_evt_pld (
      an_TaskDbId            evt_event.event_db_id%TYPE,
      an_TaskId              evt_event.event_id%TYPE,
      an_DeadlineTypeDbId    evt_sched_dead.data_type_db_id%TYPE,
      an_DeadlineTypeId      evt_sched_dead.data_type_id%TYPE,
      ad_DeadlineDate        evt_sched_dead.sched_dead_dt%TYPE,
      an_DeadlineQt          evt_sched_dead.sched_dead_qt%TYPE,
      ad_EstimatedDate       evt_sched_dead.sched_dead_dt%TYPE
   ) RETURN BLOB;

   PROCEDURE publish_task_created_event (
      an_SchedDbId              IN sched_stask.sched_db_id%TYPE,
      an_SchedId                IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId       IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId         IN task_task.task_id%TYPE,
      an_MaxRangeQt             IN task_task.forecast_range_qt%TYPE,
      an_InvNoDbId              IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId                IN inv_inv.inv_no_id%TYPE,
      an_PreviousTaskDbId       IN evt_event.event_db_id%TYPE,
      an_PreviousTaskId         IN evt_event.event_id%TYPE,
      ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
      an_ReasonDbId             IN evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd               IN evt_stage.stage_reason_cd%TYPE,
      as_UserNote               IN evt_stage.stage_note%TYPE,
      an_HrDbId                 IN org_hr.hr_db_id%TYPE,
      an_HrId                   IN org_hr.hr_id%TYPE,
      ab_CalledExternally       IN BOOLEAN,
      ab_Historic               IN BOOLEAN,
      ab_CreateNATask           IN BOOLEAN,
      on_Return                 OUT NUMBER
   );

   PROCEDURE publish_task_deadl_resched_evt (
      an_TaskDbId           IN evt_event.event_db_id%TYPE,
      an_TaskId             IN evt_event.event_id%TYPE,
      an_DeadlineTypeDbId   IN evt_sched_dead.data_type_db_id%TYPE,
      an_DeadlineTypeId     IN evt_sched_dead.data_type_id%TYPE,
      ad_DeadlineDate       IN evt_sched_dead.sched_dead_dt%TYPE,
      an_DeadlineQt         IN evt_sched_dead.sched_dead_qt%TYPE,
      ad_EstimatedDate      IN evt_sched_dead.sched_dead_dt%TYPE,
      on_Return             OUT NUMBER
   );

   PROCEDURE publish_tsk_assign_to_wk_evt (
      an_WorkPackageTaskDbId   IN evt_event.event_db_id%TYPE,
      an_WorkPackageTaskId     IN evt_event.event_id%TYPE,
      an_TaskDbId              IN evt_event.event_db_id%TYPE,
      an_TaskId                IN evt_event.event_id%TYPE,
      an_HrDbId                IN org_hr.hr_db_id%TYPE,
      an_HrId                  IN org_hr.hr_id%TYPE,
      as_Note                  IN evt_stage.stage_note%TYPE,
      an_ReasonDbId            IN evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd              IN evt_stage.stage_reason_cd%TYPE,
      on_Return                OUT NUMBER
   );

   PROCEDURE publish_tsk_unassgn_frm_wk_evt (
      an_WorkPackageTaskDbId   IN evt_event.event_db_id%TYPE,
      an_WorkPackageTaskId     IN evt_event.event_id%TYPE,
      an_TaskDbId              IN evt_event.event_db_id%TYPE,
      an_TaskId                IN evt_event.event_id%TYPE,
      an_HrDbId                IN org_hr.hr_db_id%TYPE,
      an_HrId                  IN org_hr.hr_id%TYPE,
      on_Return                OUT NUMBER
   );

   PROCEDURE setConfigParmValue( as_enableAxonFrameworkFailSafe IN VARCHAR2);

END axon_event_publish_pkg;
/

--changeSet axon_event_publish_pkg:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment axon_event_publish_pkg body definition
CREATE OR REPLACE PACKAGE BODY axon_event_publish_pkg AS
---------------------------------------------------------------------------------
-- private funtions and procedures
---------------------------------------------------------------------------------

   FUNCTION concate_id ( an_dbId number, an_id number ) RETURN VARCHAR2 IS
   ln_str VARCHAR(255);
   BEGIN
     CASE
       WHEN an_dbId IS NULL OR an_id IS NULL THEN
       ln_str := NULL;
     ELSE
       ln_str := '"' || to_char(an_dbId) || ':' || to_char(an_id) || '"';
     END CASE;
     RETURN ln_str;
   END;


   FUNCTION construct_key_value( as_key VARCHAR2, as_value VARCHAR2 ) RETURN VARCHAR2 IS
   BEGIN
     RETURN '"' || as_key || '": ' || nvl(as_value, 'null');
   END;

   -- convert the date to milli format
   FUNCTION date_to_milli( ad_date DATE ) RETURN NUMBER IS
   BEGIN
     RETURN round((cast(sys_extract_utc(cast(ad_date as timestamp)) as date) - to_date('1970-01-01', 'YYYY-MM-DD')) * (24 * 60 * 60 * 1000));
   END;

   -- replace " or \" with \" in json string value
   FUNCTION escape_json_str_value ( as_value VARCHAR2 ) RETURN VARCHAR2 IS
   BEGIN
      RETURN regexp_replace(as_value,'(\\"|")','\"');
   END;

   PROCEDURE clob_to_blob ( ac_clob IN VARCHAR2, ab_blob IN OUT BLOB) IS
      ln_dest_offset  integer := 1;
      ln_src_offset   integer := 1;
      ln_lang_context integer := 0;
      ln_warning      integer := 0;
   BEGIN
      DBMS_LOB.ConvertToBlob(ab_blob, ac_clob, length(ac_clob), ln_dest_offset, ln_src_offset, DBMS_LOB.DEFAULT_CSID, ln_lang_context, ln_warning);
   END clob_to_blob;

   -- This function computes and returns the payload column of the AXON_DOMAIN_EVENT_ENTRY table for task created event.
   FUNCTION get_tsk_crt_evt_payload
   (
      an_SchedDbId              sched_stask.sched_db_id%TYPE,
      an_SchedId                sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId       task_task.task_db_id%TYPE,
      an_OrigTaskTaskId         task_task.task_id%TYPE,
      an_MaxRangeQt             task_task.forecast_range_qt%TYPE,
      an_InvNoDbId              inv_inv.inv_no_db_id%TYPE,
      an_InvNoId                inv_inv.inv_no_id%TYPE,
      an_PreviousTaskDbId       evt_event.event_db_id%TYPE,
      an_PreviousTaskId         evt_event.event_id%TYPE,
      ad_PreviousCompletionDt   evt_event.sched_end_gdt%TYPE,
      an_ReasonDbId             evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd               evt_stage.stage_reason_cd%TYPE,
      as_UserNote               evt_stage.stage_note%TYPE,
      an_HrDbId                 org_hr.hr_db_id%TYPE,
      an_HrId                   org_hr.hr_id%TYPE,
      ab_CalledExternally       BOOLEAN,
      ab_Historic               BOOLEAN,
      ab_CreateNATask           BOOLEAN,
      ab_HasDeadline            BOOLEAN DEFAULT NULL
   ) RETURN  VARCHAR2
   IS
     ln_HasDeadline NUMBER;
     ls_Payload     VARCHAR2(32767);
   BEGIN

      -- find if task has deadline
      ln_HasDeadline := sys.diutil.bool_to_int(ab_HasDeadline);

      IF NOT ab_Historic AND ab_HasDeadline IS NULL THEN
         SELECT
           CASE
              WHEN EXISTS
              (
                 SELECT 1 FROM
                    evt_sched_dead
                 WHERE
                    evt_sched_dead.event_db_id = an_SchedDbId AND
                    evt_sched_dead.event_id    = an_SchedId
              )
              THEN 1
              ELSE 0
            END INTO ln_HasDeadline
         FROM dual;
      END IF;

      -- build payload body in a json format as the following example:
      /* {
           "newlyCreatedTaskKey" : "1111:22222",
           "taskTaskKey" : "1111:33333",
           "previousTaskKey" : null,
           "inventoryKey" : "1111:44444",
           "reasonKey" : null,
           "userNote" : null,
           "humanResourceKey" : null,
           "isCalledExternally" : false,
           "isHistoric" : false,
           "isCreatedNATask" : false,
           "previousCompletionDate" : null,
           "forecastRangeQuantity"  : 5,
           "hasDeadline"  : true
         } */
      ls_payload := '';
      ls_payload := ls_payload || '{';
      ls_payload := ls_payload ||    '"newlyCreatedTaskKey":'    || CASE WHEN an_SchedId IS NULL THEN 'null' ELSE '"' || to_char(an_SchedDbId) || ':' || to_char(an_SchedId) || '"' END || ',';
      ls_payload := ls_payload ||    '"taskTaskKey":'            || CASE WHEN an_OrigTaskTaskId IS NULL THEN 'null' ELSE '"' || to_char(an_OrigTaskTaskDbId) || ':' || to_char(an_OrigTaskTaskId) || '"' END || ',';
      ls_payload := ls_payload ||    '"previousTaskKey":'        || CASE WHEN an_PreviousTaskId IS NULL THEN 'null' ELSE '"' || to_char(an_PreviousTaskDbId) || ':' || to_char(an_PreviousTaskId) || '"' END || ',';
      ls_payload := ls_payload ||    '"inventoryKey":'           || CASE WHEN an_InvNoId IS NULL THEN 'null' ELSE '"' || to_char(an_InvNoDbId) || ':' || to_char(an_InvNoId) || '"' END || ',';
      ls_payload := ls_payload ||    '"reasonKey":'              || CASE WHEN an_ReasonCd IS NULL THEN 'null' ELSE '"' || to_char(an_ReasonDbId) || ':' || escape_json_str_value(an_ReasonCd) || '"' END || ',';
      ls_payload := ls_payload ||    '"userNote":'               || CASE WHEN as_UserNote IS NULL THEN 'null' ELSE '"' || escape_json_str_value(as_UserNote) || '"' END || ',';
      ls_payload := ls_payload ||    '"humanResourceKey":'       || CASE WHEN an_HrId IS NULL THEN 'null' ELSE '"' || to_char(an_HrDbId) || ':' || to_char(an_HrId) || '"' END || ',';
      ls_payload := ls_payload ||    '"isCalledExternally":'     || CASE WHEN ab_CalledExternally THEN 'true' ELSE 'false' END || ',';
      ls_payload := ls_payload ||    '"isHistoric":'             || CASE WHEN ab_Historic THEN 'true' ELSE 'false' END || ',';
      ls_payload := ls_payload ||    '"isCreatedNATask":'        || CASE WHEN ab_CreateNATask THEN 'true' ELSE 'false' END || ',';
      ls_payload := ls_payload ||    '"previousCompletionDate":' || CASE WHEN ad_PreviousCompletionDt IS NULL THEN 'null' ELSE '"' || date_to_milli(ad_PreviousCompletionDt) || '"' END || ',';
      ls_payload := ls_payload ||    '"forecastRangeQuantity":'  || CASE WHEN an_MaxRangeQt IS NULL THEN 'null' ELSE '"' || an_MaxRangeQt || '"' END || ',';
      ls_payload := ls_payload ||    '"isDeadlineExisting":'     || CASE WHEN ln_hasDeadline > 0 THEN 'true' ELSE 'false' END;
      ls_payload := ls_payload || '}';

      RETURN ls_payload;

   END get_tsk_crt_evt_payload;

   -- This function computes and returns the payload column of the AXON_DOMAIN_EVENT_ENTRY table for task assigned to work package event.
   FUNCTION get_tsk_assgn_to_wk_payload
   (
      an_WorkPackageTaskDbId   evt_event.event_db_id%TYPE,
      an_WorkPackageTaskId     evt_event.event_id%TYPE,
      an_TaskDbId              evt_event.event_db_id%TYPE,
      an_TaskId                evt_event.event_id%TYPE,
      an_HrDbId                org_hr.hr_db_id%TYPE,
      an_HrId                  org_hr.hr_id%TYPE,
      as_Note                  evt_stage.stage_note%TYPE,
      an_ReasonDbId            evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd              evt_stage.stage_reason_cd%TYPE
   ) RETURN VARCHAR2
   IS
      ls_Payload     VARCHAR2(32767);
   BEGIN
      -- build event payload
      ls_payload := '';
      ls_payload := ls_payload || '{';
      ls_payload := ls_payload || construct_key_value('workPackageKey', concate_id(an_WorkPackageTaskDbId, an_WorkPackageTaskId)) || ', ';
      ls_payload := ls_payload || construct_key_value('taskKey', concate_id(an_TaskDbId, an_TaskId)) || ', ';
      ls_payload := ls_payload || construct_key_value('humanResourceKey', concate_id(an_HrDbId, an_HrId)) || ', ';
      ls_payload := ls_payload || construct_key_value('note', '"' || escape_json_str_value(as_Note) || '"') || ', ';
      ls_payload := ls_payload || construct_key_value('reasonKey', CASE WHEN an_ReasonCd IS NULL THEN NULL ELSE '"' || to_char(an_ReasonDbId) || ':' || escape_json_str_value(an_ReasonCd) || '"' END);
      ls_payload := ls_payload || '}';

   RETURN ls_payload;
   END get_tsk_assgn_to_wk_payload;

   -- This function computes and returns the payload column of the AXON_DOMAIN_EVENT_ENTRY table for task deadline rescheduled event.
   FUNCTION get_tsk_deadl_resched_payload
   (
      an_TaskDbId            evt_event.event_db_id%TYPE,
      an_TaskId              evt_event.event_id%TYPE,
      an_DeadlineTypeDbId    evt_sched_dead.data_type_db_id%TYPE,
      an_DeadlineTypeId      evt_sched_dead.data_type_id%TYPE,
      ad_DeadlineDate        evt_sched_dead.sched_dead_dt%TYPE,
      an_DeadlineQt          evt_sched_dead.sched_dead_qt%TYPE,
      ad_EstimatedDate       evt_sched_dead.sched_dead_dt%TYPE
   ) RETURN VARCHAR2
   IS
      ls_Payload     VARCHAR2(32767);
   BEGIN
      ls_payload := '';
      ls_payload := ls_payload || '{';
      ls_payload := ls_payload || construct_key_value('taskKey', concate_id(an_TaskDbId, an_TaskId)) || ', ';
      ls_payload := ls_payload || construct_key_value('deadlineTypeKey', concate_id(an_DeadlineTypeDbId, an_DeadlineTypeId)) || ', ';
      ls_payload := ls_payload || construct_key_value('deadlineDate', date_to_milli(ad_DeadlineDate)) || ', ';
      ls_payload := ls_payload || construct_key_value('deadlineQt', an_DeadlineQt) || ', ';
      ls_payload := ls_payload || construct_key_value('estimatedDate', date_to_milli(ad_EstimatedDate));
      ls_payload := ls_payload || '}';

   RETURN ls_payload;
   END get_tsk_deadl_resched_payload;

---------------------------------------------------------------------------------
-- public funtions and procedures
---------------------------------------------------------------------------------

   -- generate a new uuid as aggregate identifier and event identifier and convert it to the format of 'XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX'
   FUNCTION get_axon_identifier RETURN VARCHAR2
   IS
      ls_Identifier  VARCHAR2(255);
   BEGIN
      SELECT
         regexp_replace
         (
            rawtohex(mx_key_pkg.new_uuid()),
            '([A-F0-9]{8})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{12})' ,
            '\1-\2-\3-\4-\5'
         ) INTO ls_Identifier FROM dual;

      RETURN ls_Identifier;
   END get_axon_identifier;

   /*
    * Function: dl_get_taskcreatedevt_pl
    *
    * Description: This function computes and returns the payload for task created event for the data loader
    */
   FUNCTION dl_get_tsk_created_evt_payload (
      an_SchedDbId               sched_stask.sched_db_id%TYPE,
      an_SchedId                 sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId        task_task.task_db_id%TYPE,
      an_OrigTaskTaskId          task_task.task_id%TYPE,
      an_MaxRangeQt              task_task.forecast_range_qt%TYPE,
      an_InvNoDbId               inv_inv.inv_no_db_id%TYPE,
      an_InvNoId                 inv_inv.inv_no_id%TYPE,
      an_PreviousTaskDbId        evt_event.event_db_id%TYPE,
      an_PreviousTaskId          evt_event.event_id%TYPE,
      ad_PreviousCompletionDt    evt_event.sched_end_gdt%TYPE,
      as_UserNote                evt_stage.stage_note%TYPE,
      an_HrDbId                  org_hr.hr_db_id%TYPE,
      an_HrId                    org_hr.hr_id%TYPE,
      ab_Historic                NUMBER
   ) RETURN BLOB IS

   ls_payload VARCHAR2(32767);

   BEGIN
      ls_payload := get_tsk_crt_evt_payload
         (
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            an_MaxRangeQt,
            an_InvNoDbId,
            an_InvNoId,
            an_PreviousTaskDbId,
            an_PreviousTaskId,
            ad_PreviousCompletionDt,
            NULL,
            NULL,
            as_UserNote,
            an_HrDbId,
            an_HrId,
            FALSE,
            sys.diutil.int_to_bool(ab_Historic),
            FALSE
         );

      RETURN utl_raw.cast_to_raw(ls_payload);

   END dl_get_tsk_created_evt_payload;

   /*
    * Function: dl_get_tsk_assgn_to_wk_evt_pld
    *
    * Description: This function computes and returns the payload for task assigned to work package event for the data loader
    */
   FUNCTION dl_get_tsk_assgn_to_wk_evt_pld (
      an_WorkPackageTaskDbId   evt_event.event_db_id%TYPE,
      an_WorkPackageTaskId     evt_event.event_id%TYPE,
      an_TaskDbId              evt_event.event_db_id%TYPE,
      an_TaskId                evt_event.event_id%TYPE,
      an_HrDbId                org_hr.hr_db_id%TYPE,
      an_HrId                  org_hr.hr_id%TYPE,
      as_Note                  evt_stage.stage_note%TYPE,
      an_ReasonDbId            evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd              evt_stage.stage_reason_cd%TYPE
   ) RETURN BLOB
   IS
      ls_payload VARCHAR2(32767);
   BEGIN
      ls_payload := get_tsk_assgn_to_wk_payload
         (
            an_WorkPackageTaskDbId,
            an_WorkPackageTaskId,
            an_TaskDbId,
            an_TaskId,
            an_HrDbId,
            an_HrId,
            as_Note,
            an_ReasonDbId,
            an_ReasonCd
         );

      RETURN utl_raw.cast_to_raw(ls_payload);
   END dl_get_tsk_assgn_to_wk_evt_pld;

   /*
    * Function: dl_get_tsk_deadl_reshd_evt_pld
    *
    * Description: This function computes and returns the payload for task deadline rescheduled event for the data loader
    */
   FUNCTION dl_get_tsk_deadl_reshd_evt_pld (
      an_TaskDbId            evt_event.event_db_id%TYPE,
      an_TaskId              evt_event.event_id%TYPE,
      an_DeadlineTypeDbId    evt_sched_dead.data_type_db_id%TYPE,
      an_DeadlineTypeId      evt_sched_dead.data_type_id%TYPE,
      ad_DeadlineDate        evt_sched_dead.sched_dead_dt%TYPE,
      an_DeadlineQt          evt_sched_dead.sched_dead_qt%TYPE,
      ad_EstimatedDate       evt_sched_dead.sched_dead_dt%TYPE
   ) RETURN BLOB
   IS
      ls_payload VARCHAR2(32767);
   BEGIN
      ls_payload := get_tsk_deadl_resched_payload
         (
            an_TaskDbId,
            an_TaskId,
            an_DeadlineTypeDbId,
            an_DeadlineTypeId,
            ad_DeadlineDate,
            an_DeadlineQt,
            ad_EstimatedDate
         );

      RETURN utl_raw.cast_to_raw(ls_payload);
   END dl_get_tsk_deadl_reshd_evt_pld;

   /*
    * Procedure:    publish_event
    *
    * Description:  This procedure publish axon event by inserting event message into AXON_DOMAIN_EVENT_ENTRY table.
    *               The event handler will take the event from this table and process it.
    */
   PROCEDURE publish_event (
      an_SequenceNumber  IN NUMBER,
      as_Type            IN VARCHAR2,
      as_MetaData        IN CLOB,
      as_Payload         IN CLOB,
      as_PayloadRevision IN VARCHAR2,
      as_EventType       IN VARCHAR2,
      on_Return          OUT NUMBER
   ) IS
      ls_Identifier  VARCHAR2(255);
      ls_MetaData_blob BLOB;
      ls_Payload_blob BLOB;

   BEGIN
      -- Initialize the return value
      on_Return := icn_NoProc;

      -- generate a new uuid as aggregate identifier and event identifier and convert it to the format of 'XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX'
      ls_Identifier := get_axon_identifier;

      DBMS_LOB.createTemporary (ls_MetaData_blob, TRUE);
      DBMS_LOB.createTemporary (ls_Payload_blob, TRUE);

      clob_to_blob(as_MetaData, ls_MetaData_blob);
      clob_to_blob(as_Payload, ls_Payload_blob);

      IF (is_EnableAxonFrameworkFailSafe IS NULL OR is_EnableAxonFrameworkFailSafe <> 'TRUE') THEN
         INSERT INTO axon_domain_event_entry
         (
            "AGGREGATEIDENTIFIER"   ,
            "SEQUENCENUMBER"        ,
            "TYPE"                  ,
            "EVENTIDENTIFIER"       ,
            "METADATA"              ,
            "PAYLOAD"               ,
            "PAYLOADREVISION"       ,
            "PAYLOADTYPE"           ,
            "TIMESTAMP"
         )
         VALUES
         (
            ls_Identifier,
            an_SequenceNumber,
            as_Type,
            ls_Identifier,
            ls_MetaData_blob,
            ls_Payload_blob,
            as_PayloadRevision,
            as_EventType,
            to_char(SYSTIMESTAMP AT TIME ZONE 'UTC', 'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"')
         );
      END IF;

      dbms_lob.freetemporary(ls_MetaData_blob);
      dbms_lob.freetemporary(ls_Payload_blob);

      -- Return success
      on_Return := icn_Success;

      EXCEPTION
         WHEN OTHERS THEN
            -- Unexpected error
            on_Return := SQLCODE;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','axon_event_publish_pkg@@@publish_event@@@: '||SQLERRM);
            RETURN;

   END publish_event;

   /*
    * Procedure:    publish_task_created_event
    *
    * Description:  This procedure publishes TaskCreatedEvent.
    */
   PROCEDURE publish_task_created_event (
      an_SchedDbId              IN sched_stask.sched_db_id%TYPE,
      an_SchedId                IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId       IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId         IN task_task.task_id%TYPE,
      an_MaxRangeQt             IN task_task.forecast_range_qt%TYPE,
      an_InvNoDbId              IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId                IN inv_inv.inv_no_id%TYPE,
      an_PreviousTaskDbId       IN evt_event.event_db_id%TYPE,
      an_PreviousTaskId         IN evt_event.event_id%TYPE,
      ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
      an_ReasonDbId             IN evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd               IN evt_stage.stage_reason_cd%TYPE,
      as_UserNote               IN evt_stage.stage_note%TYPE,
      an_HrDbId                 IN org_hr.hr_db_id%TYPE,
      an_HrId                   IN org_hr.hr_id%TYPE,
      ab_CalledExternally       IN BOOLEAN,
      ab_Historic               IN BOOLEAN,
      ab_CreateNATask           IN BOOLEAN,
      on_Return                 OUT NUMBER
   ) IS
      -- ln_HasDeadline NUMBER;
      ls_Payload     VARCHAR2(32767);
   BEGIN
      -- Initialize the return value
      on_Return := icn_NoProc;

      ls_payload := get_tsk_crt_evt_payload(an_SchedDbId, an_SchedId, an_OrigTaskTaskDbId, an_OrigTaskTaskId,  an_MaxRangeQt, an_InvNoDbId, an_InvNoId, an_PreviousTaskDbId, an_PreviousTaskId, ad_PreviousCompletionDt, an_ReasonDbId, an_ReasonCd, as_UserNote, an_HrDbId, an_HrId, ab_CalledExternally, ab_Historic, ab_CreateNATask);

      -- publish task created event
      publish_event( 0, NULL, ics_Meta_Data, ls_Payload, NULL, ics_TaskCreated_Evt, on_Return);
      IF on_Return < 1 THEN
         RETURN;
      END IF;

      -- Return success
      on_Return := icn_Success;

      EXCEPTION
         WHEN OTHERS THEN
            -- Unexpected error
            on_Return := SQLCODE;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','axon_event_publish_pkg@@@publish_task_created_event@@@: '||SQLERRM);
            RETURN;

   END publish_task_created_event;


   /*
    * Procedure:    publish_task_deadl_resched_evt
    *
    * Description:  This procedure publishes TaskDrivingDeadlineRescheduledEvent.
    */
   PROCEDURE publish_task_deadl_resched_evt (
      an_TaskDbId         IN evt_event.event_db_id%TYPE,
      an_TaskId           IN evt_event.event_id%TYPE,
      an_DeadlineTypeDbId IN evt_sched_dead.data_type_db_id%TYPE,
      an_DeadlineTypeId   IN evt_sched_dead.data_type_id%TYPE,
      ad_DeadlineDate     IN evt_sched_dead.sched_dead_dt%TYPE,
      an_DeadlineQt       IN evt_sched_dead.sched_dead_qt%TYPE,
      ad_EstimatedDate    IN evt_sched_dead.sched_dead_dt%TYPE,
      on_Return OUT NUMBER
   ) IS
      ls_Payload     VARCHAR2(32767);
   BEGIN
      -- Initialize the return value
      on_Return := icn_NoProc;

      -- build payload body in a json format as the following example:
      /* {
           "taskKey" : "1111:33333",
           "deadlineTypeKey" : null,
           "deadlineDate" : null,
           "deadlineQt" : null,
           "estimatedDate" : null
         } */

      ls_payload := get_tsk_deadl_resched_payload(
                       an_TaskDbId,
                       an_TaskId,
                       an_DeadlineTypeDbId,
                       an_DeadlineTypeId,
                       ad_DeadlineDate,
                       an_DeadlineQt,
                       ad_EstimatedDate
                    );


      -- publish task deadline rescheduled event
      publish_event( 0, NULL, ics_Meta_Data, ls_Payload, NULL, ics_TaskDrvDeadlineResch_Evt, on_Return);
      IF on_Return < 1 THEN
         RETURN;
      END IF;

      -- Return success
      on_Return := icn_Success;

      EXCEPTION
         WHEN OTHERS THEN
            -- Unexpected error
            on_Return := SQLCODE;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','axon_event_publish_pkg@@@publish_task_deadl_resched_evt@@@: '||SQLERRM);
            RETURN;

   END publish_task_deadl_resched_evt;

   /*
    * Procedure: publish_tsk_assign_to_wk_evt
    *
    * Description: This procedure publish task assign to work package event
    */
   PROCEDURE publish_tsk_assign_to_wk_evt (
      an_WorkPackageTaskDbId   IN evt_event.event_db_id%TYPE,
      an_WorkPackageTaskId     IN evt_event.event_id%TYPE,
      an_TaskDbId              IN evt_event.event_db_id%TYPE,
      an_TaskId                IN evt_event.event_id%TYPE,
      an_HrDbId                IN org_hr.hr_db_id%TYPE,
      an_HrId                  IN org_hr.hr_id%TYPE,
      as_Note                  IN evt_stage.stage_note%TYPE,
      an_ReasonDbId            IN evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd              IN evt_stage.stage_reason_cd%TYPE,
      on_Return                OUT NUMBER
   ) IS
      ls_payload   VARCHAR2(32767);
   BEGIN
      -- initialize the return value
      on_Return := icn_NoProc;

      -- build event payload
      ls_payload := get_tsk_assgn_to_wk_payload
      (
         an_WorkPackageTaskDbId,
         an_WorkPackageTaskId,
         an_TaskDbId,
         an_TaskId,
         an_HrDbId,
         an_HrId,
         as_Note,
         an_ReasonDbId,
         an_ReasonCd
       );

      -- publish event
      publish_event( 0, NULL, ics_Meta_Data, ls_Payload, NULL, ics_TaskAssgToWp_Evt, on_Return);

      IF on_Return < 1 THEN
         RETURN;
      END IF;

      -- return success
      on_Return := icn_Success;

      EXCEPTION
         WHEN OTHERS THEN
            -- exceptions
            on_Return := SQLCODE;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','axon_event_publish_pkg@@@publish_tsk_assign_to_wk_evt@@@: '||SQLERRM);
            RETURN;
   END publish_tsk_assign_to_wk_evt;

   /*
    * Procedure: publish_tsk_unassgn_frm_wk_evt
    *
    * Description: This procedure publish task unassign from work package event
    */
   PROCEDURE publish_tsk_unassgn_frm_wk_evt (
      an_WorkPackageTaskDbId   IN evt_event.event_db_id%TYPE,
      an_WorkPackageTaskId     IN evt_event.event_id%TYPE,
      an_TaskDbId              IN evt_event.event_db_id%TYPE,
      an_TaskId                IN evt_event.event_id%TYPE,
      an_HrDbId                IN org_hr.hr_db_id%TYPE,
      an_HrId                  IN org_hr.hr_id%TYPE,
      on_Return                OUT NUMBER
   ) IS
      ls_payload   VARCHAR2(32767);
   BEGIN
      -- initialize the return value
      on_Return := icn_NoProc;

      -- build event payload
      ls_payload := '';
      ls_payload := ls_payload || '{';
      ls_payload := ls_payload || construct_key_value('workPackageKey', concate_id(an_WorkPackageTaskDbId, an_WorkPackageTaskId)) || ', ';
      ls_payload := ls_payload || construct_key_value('taskKey', concate_id(an_TaskDbId, an_TaskId)) || ', ';
      ls_payload := ls_payload || construct_key_value('humanResourceKey', concate_id(an_HrDbId, an_HrId));
      ls_payload := ls_payload || '}';

      -- publish event
      publish_event( 0, NULL, ics_Meta_Data, ls_Payload, NULL, ics_TaskUnassnFrWp_Evt, on_Return);

      IF on_Return < 1 THEN
         RETURN;
      END IF;

      -- return success
      on_Return := icn_Success;

      EXCEPTION
         WHEN OTHERS THEN
            -- exceptions
            on_Return := SQLCODE;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','axon_event_publish_pkg@@@publish_tsk_assign_to_wk_evt@@@: '||SQLERRM);
            RETURN;

   END publish_tsk_unassgn_frm_wk_evt;

   /*
    * Procedure:    setConfigParmValue
    *
    * Description:  This procedure sets the global variable to a user defined value only for unit test.
    *
    *               The proceedure does not apply the setting globally or persist the value; as such it does not affect any other sessions.
    */
   PROCEDURE setConfigParmValue( as_enableAxonFrameworkFailSafe IN VARCHAR2)IS
   BEGIN
      is_EnableAxonFrameworkFailSafe := as_enableAxonFrameworkFailSafe;
   END setConfigParmValue;

   /*
    * Procedure:    initializeGlobalVariable
    *
    * Description:  This procedure initializes global variables.
    */
   PROCEDURE initializeGlobalVariable IS
   BEGIN
      SELECT parm_value INTO is_EnableAxonFrameworkFailSafe
      FROM utl_config_parm
      WHERE parm_name = 'ENABLE_AXON_FRAMEWORK_FAILSAFE';

      EXCEPTION
         WHEN OTHERS THEN
            -- Unexpected error
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','axon_event_publish_pkg@@@initializeGlobalVariable@@@: '||SQLERRM);
            RETURN;
   END initializeGlobalVariable;

BEGIN
   initializeGlobalVariable;

END axon_event_publish_pkg;
/