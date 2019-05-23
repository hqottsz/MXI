--liquibase formatted sql

--changeSet OPER-28541-002:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment axon_event_publish_pkg body definition
CREATE OR REPLACE PACKAGE BODY axon_event_publish_pkg AS


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


   FUNCTION date_to_milli( ad_date DATE ) RETURN NUMBER IS
   BEGIN
     RETURN round((cast(sys_extract_utc(cast(ad_date as timestamp)) as date) - to_date('1970-01-01', 'YYYY-MM-DD')) * (24 * 60 * 60 * 1000));
   END;


/********************************************************************************
*
* Procedure:    publish_event
*
* Arguments:    an_SequenceNumber   (number) - The sequence number of the event
*               as_type             (string) - The type
*               as_MetaData         (string) - The metadata
*               as_Payload          (string) - The payload of the event in the format of JSON
*               as_PayloadRevision  (string) - The payload revision
*               as_EventType        (string) - The event type
*
* Description:  This procedure publish axon event by inserting event message into AXON_DOMAIN_EVENT_ENTRY table.
*               The event handler will take the event from this table and process it.
*
********************************************************************************/
   PROCEDURE publish_event (
      an_SequenceNumber  IN NUMBER,
      as_Type            IN VARCHAR2,
      as_MetaData        IN VARCHAR2,
      as_Payload         IN VARCHAR2,
      as_PayloadRevision IN VARCHAR2,
      as_EventType       IN VARCHAR2,
      on_Return          OUT NUMBER
   ) IS
      ls_Identifier  VARCHAR2(255);
   BEGIN
      -- Initialize the return value
      on_Return := icn_NoProc;

      -- generate a new uuid as aggregate identifier and event identifier and convert it to the format of 'XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX'
      SELECT
         regexp_replace
         (
            rawtohex(mx_key_pkg.new_uuid()),
            '([A-F0-9]{8})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{12})' ,
            '\1-\2-\3-\4-\5'
         )
      INTO ls_Identifier FROM dual;

      INSERT INTO axon_domain_event_entry
         (
            "GLOBALINDEX"           ,
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
            axon_domain_event_entry_seq.nextval,
            ls_Identifier,
            an_SequenceNumber,
            as_Type,
            ls_Identifier,
            utl_raw.cast_to_raw(as_MetaData),
            utl_raw.cast_to_raw(as_Payload),
            as_PayloadRevision,
            as_EventType,
            to_char(SYSTIMESTAMP AT TIME ZONE 'UTC', 'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"')
         );

      -- Return success
      on_Return := icn_Success;

      EXCEPTION
         WHEN OTHERS THEN
            -- Unexpected error
            on_Return := SQLCODE;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','axon_event_publish_pkg@@@publish_event@@@: '||SQLERRM);
            RETURN;

   END publish_event;

-- replace " or \" with \" in json string value
   FUNCTION escape_json_str_value ( as_value VARCHAR2 ) RETURN VARCHAR2 IS
   BEGIN
      RETURN regexp_replace(as_value,'(\\"|")','\"');
   END;

/********************************************************************************
*
* Procedure:    publish_task_created_event
*
* Arguments:    an_SchedDbId              (long) - Task primary key
*               an_SchedId                (long) - ""
*               an_OrigTaskTaskDbId       (long) - The task definition that the task is created on
*               an_OrigTaskTaskId         (long) - ""
*               an_MaxRangeQt             (number) - The task's forecast_range_qt
*               an_InvNoDbId              (long) - The inventory that the task is created on
*               an_InvNoId                (long) - ""
*               an_PreviousTaskDbId       (long) - The previous task (set to -1 if no previous task exists)
*               an_PreviousTaskId         (long) - ""
*               ad_CompletionDate         (date) - completion date only used when task is historic
*               ad_PreviousCompletionDt   (date) - The completion date of the installation task, that triggered
*                                                  the create_on_install logic. NULL otherwise
*               an_ReasonDbId             (long) - reason for creation db id
*               an_ReasonCd               (String)- reason for creation code
*               as_UserNote               (String)- user note
*               an_HrDbId                 (long) - human resource authorizing creation
*               an_HrId                   (long) - ""
*               ab_CalledExternally       (bool) - Flag indicating whether this procedure was called externally
*                                                  (1) or internally (0)
*               ab_Historic               (bool) - Whether newly created task is historic
*               ab_CreateNATask           (bool) - Whether newly created task is N/A
*
* Description:  This procedure publish task created event.
*
********************************************************************************/
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
      ln_HasDeadline NUMBER;
      ls_Payload     VARCHAR2(32767);
   BEGIN
      -- Initialize the return value
      on_Return := icn_NoProc;

      -- find if task has deadline
      SELECT COUNT(*) INTO ln_HasDeadline
      FROM
      (
         SELECT 1 FROM
            evt_sched_dead
         INNER JOIN mim_data_type ON
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
         WHERE
            evt_sched_dead.event_db_id = an_SchedDbId AND
            evt_sched_dead.event_id    = an_SchedId
         UNION
         SELECT 1 FROM
            sched_stask
         INNER JOIN task_sched_rule ON
            task_sched_rule.task_db_id = sched_stask.task_db_id AND
            task_sched_rule.task_id    = sched_stask.task_id
         INNER JOIN mim_data_type ON
            mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
            mim_data_type.data_type_id    = task_sched_rule.data_type_id
         WHERE
            sched_stask.sched_db_id = an_SchedDbId AND
            sched_stask.sched_id    = an_SchedId   AND
            sched_stask.rstat_cd    = 0
      );

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
      ls_payload := ls_payload ||    '"previousCompletionDate":' || CASE WHEN ad_PreviousCompletionDt IS NULL THEN 'null' ELSE '"' || ad_PreviousCompletionDt || '"' END || ',';
      ls_payload := ls_payload ||    '"forecastRangeQuantity":'  || CASE WHEN an_MaxRangeQt IS NULL THEN 'null' ELSE '"' || an_MaxRangeQt || '"' END || ',';
      ls_payload := ls_payload ||    '"isDeadlineExisting":'     || CASE WHEN ln_hasDeadline > 0 THEN 'true' ELSE 'false' END;
      ls_payload := ls_payload || '}';

      -- publish task created event
      publish_event( 0, NULL, '{ }', ls_Payload, NULL, 'com.mxi.mx.core.production.task.domain.TaskCreatedEvent', on_Return);
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

      ls_payload := '';
      ls_payload := ls_payload || '{';
      ls_payload := ls_payload || construct_key_value('taskKey', concate_id(an_TaskDbId, an_TaskId)) || ', ';
      ls_payload := ls_payload || construct_key_value('deadlineTypeKey', concate_id(an_DeadlineTypeDbId, an_DeadlineTypeId)) || ', ';
      ls_payload := ls_payload || construct_key_value('deadlineDate', date_to_milli(ad_DeadlineDate)) || ', ';
      ls_payload := ls_payload || construct_key_value('deadlineQt', an_DeadlineQt) || ', ';
      ls_payload := ls_payload || construct_key_value('estimatedDate', date_to_milli(ad_EstimatedDate));
      ls_payload := ls_payload || '}';

      -- publish task deadline rescheduled event
      publish_event( 0, NULL, '{ }', ls_Payload, NULL, 'com.mxi.mx.core.production.task.domain.TaskDrivingDeadlineRescheduledEvent', on_Return);
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

END axon_event_publish_pkg;
/
