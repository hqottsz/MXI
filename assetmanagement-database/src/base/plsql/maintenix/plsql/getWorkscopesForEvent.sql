--liquibase formatted sql


--changeSet getWorkscopesForEvent:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getWorkscopesForEvent
(
  aEventDbId lrp_event.lrp_event_db_id%TYPE,
  aEventId   lrp_event.lrp_event_id%TYPE
) RETURN  VARCHAR2
IS
  ls_work_scopes VARCHAR2(4000);

   CURSOR lcur_WorkScopes (
         cn_EventDbId    IN lrp_event.lrp_event_db_id%TYPE,
         cn_EventId      IN lrp_event.lrp_event_id%TYPE
      ) IS
      SELECT
         task_task.task_cd
      FROM
         lrp_event_workscope JOIN task_task ON
         task_task.task_defn_db_id = lrp_event_workscope.task_defn_db_id AND
         task_task.task_defn_id    = lrp_event_workscope.task_defn_id
      WHERE
         lrp_event_workscope.lrp_event_db_id = cn_EventDbId AND
         lrp_event_workscope.lrp_event_id    = cn_EventId
         AND
         task_task.task_def_status_cd       = 'ACTV'
      ORDER BY
         task_task.task_cd;
   lrec_WorkScopes lcur_WorkScopes%ROWTYPE;
BEGIN

   FOR lrec_WorkScopes IN lcur_WorkScopes (aEventDbId,aEventId ) LOOP
       ls_work_scopes := ls_work_scopes || ', ' || lrec_WorkScopes.task_cd;
   END LOOP;

   IF ls_work_scopes IS NULL THEN
      RETURN NULL;
   END IF;

  RETURN substr(ls_work_scopes, 3);
END getWorkscopesForEvent;
/