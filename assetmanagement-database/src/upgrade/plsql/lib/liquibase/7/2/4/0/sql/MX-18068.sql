--liquibase formatted sql


--changeSet MX-18068:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getTaskActions
(
   aTaskDbId sched_action.sched_db_id%TYPE,
   aTaskId sched_action.sched_id%TYPE
) RETURN CLOB
IS
   lActionLdesc CLOB;

   /* cursor declarations */
   CURSOR lcur_TaskActions (
         cl_TaskDbId   sched_stask.sched_db_id%type,
         cl_TaskId     sched_stask.sched_id%type
      ) IS
      -- regular action data need HTML formatting
      SELECT
        DECODE( sched_action.cancel_bool, 1, '<s>', '') ||
        '<b><u>' || rank() OVER (ORDER BY sched_action.action_dt ASC) || '.  ' ||
        utl_user.first_name || ' ' ||
        utl_user.last_name  || ', ' ||
        to_char( sched_action.action_dt, 'DD-Mon-YYYY hh24:mi:ss') || ' ' || utl_timezone.user_cd || '</u></b><br>' ||
        to_clob( sched_action.action_ldesc ) ||
        DECODE( sched_action.cancel_bool, 1, '</s>', '') AS action_ldesc,
        sched_action.action_dt
      FROM
        sched_action,
        org_hr,
        utl_user,
        utl_timezone

      WHERE
        sched_action.sched_db_id = cl_TaskDbId AND
        sched_action.sched_id    = cl_TaskId
        AND
        DBMS_LOB.substr( sched_action.action_ldesc, 6, 1 ) <> '<b><u>'
        AND
        org_hr.hr_db_id = sched_action.hr_db_id AND
        org_hr.hr_id    = sched_action.hr_id	AND
        org_hr.rstat_cd	= 0
        AND
        utl_user.user_id = org_hr.user_id
        AND
        utl_timezone.default_bool = 1

      UNION ALL

      -- Migrated data is preformatted
      SELECT
        DECODE( sched_action.cancel_bool, 1, '<s>', '') ||
        to_clob(sched_action.action_ldesc) ||
        DECODE( sched_action.cancel_bool, 1, '</s>', '') AS action_ldesc,
        sched_action.action_dt
      FROM
        sched_action
      WHERE
        sched_action.sched_db_id = cl_TaskDbId AND
        sched_action.sched_id    = cl_TaskId
        AND
        DBMS_LOB.substr( sched_action.action_ldesc, 6, 1 ) = '<b><u>'

      ORDER BY
        action_dt;

   lrec_TaskActions lcur_TaskActions%ROWTYPE;


BEGIN

   lActionLdesc := NULL;

   /* loop for every deadline defined for this task */
   FOR lrec_TaskActions IN lcur_TaskActions(aTaskDbId, aTaskId) LOOP

      IF lActionLdesc IS NULL THEN
        /* *** If this is the first row, don't add break *** */
        lActionLdesc := lrec_TaskActions.action_ldesc;

      ELSE
        /* *** add an html break *** */
        lActionLdesc := lActionLdesc || '<br><br>' || lrec_TaskActions.action_ldesc;

      END IF;

   END LOOP;


   RETURN lActionLdesc;

END getTaskActions;
/