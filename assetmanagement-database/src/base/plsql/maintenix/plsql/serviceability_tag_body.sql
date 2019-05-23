--liquibase formatted sql


--changeSet serviceability_tag_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY serviceability_tag_rpt_pkg AS

/********************************************************************************
*
* Procedure:      GetPreviousInventoryCondition
* Arguments:      an_InvInvDbId (IN NUMBER): DB_ID of the inventory to search for
*                                            the previous inventory condition.
*                 an_InvInvId (IN NUMBER):   ID of the inventory to search for
*                                            the previous inventory condition.
*                 av_InvPreviousCondition:   The previous condition for the
*                 (OUT VARCHAR2)             inventory specified by the input
*                                            arguments. If a previous condition
*                                            cannot be found for the inventory
*                                            then the current condition cd is
*                                            returned.
*
* Description:   This procedure is invoked by the serviceability tag report.
*                The report must be run on an inventory that is currently in
*                in RFI condition. The objective is to find the previous
*                condition of the inventory, before being promoted to RFI.
*                This is achieved by search for condition change events for
*                the specified inventory, ordering them by completion date.
*                Since the records are sorted in descending order,
*                the first record should be the most recent event, and so on.
*                We reference the 2nd last condition change event to determine
*                the inventory condition prior to being set to RFI.
*                It would not be this hard if inventory condition change
*                events recorded the from-condition and to-condition, but that
*                is currently not the architecture.
*
*                StyleReports prevents the use of user-defined types in the
*                the stored procedure. For this reason, only standard types
*                are used for the stored procedure parameters.
*
* Orig.Coder:     Lisa Henderson
* Recent Coder:   Lisa Henderson
* Recent Date:    Aug 28, 2003
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetPreviousInventoryCondition(
          an_InvInvDbId                  IN  inv_inv.inv_no_db_id%TYPE,
          an_InvInvId                    IN  inv_inv.inv_no_id%TYPE,
          av_InvPreviousCondition        OUT evt_event.event_status_cd%TYPE)
IS

   lv_InvCurrentCondCd inv_inv.inv_cond_cd%TYPE;
   lv_EventStatusCd evt_event.event_status_cd%TYPE;

   /* cursor declarations */
   CURSOR lcur_ConditionChangeEvents (
         cl_InvInvDbId   inv_inv.inv_no_id%TYPE,
         cl_InvInvId     inv_inv.inv_no_id%TYPE
      ) IS
          SELECT
             evt_event.event_status_cd
          FROM
             evt_event,
             evt_inv
          WHERE
             evt_inv.inv_no_db_id = cl_InvInvDbId AND
             evt_inv.inv_no_id    = cl_InvInvId AND

             evt_event.event_db_id        = evt_inv.event_db_id AND
             evt_event.event_id           = evt_inv.event_id AND
             evt_event.hist_bool          = 1 AND
             evt_event.event_type_cd      ='AC' 	AND
             evt_event.rstat_cd		  = 0

          ORDER BY
             evt_event.event_dt DESC;


   BEGIN

      /*Get the inventory current condition code*/
      SELECT
         inv_inv.inv_cond_cd
      INTO
         lv_InvCurrentCondCd
      FROM
         inv_inv
      WHERE
         inv_inv.inv_no_db_id = an_InvInvDbId AND
         inv_inv.inv_no_id    = an_InvInvId	AND
         inv_inv.rstat_cd	= 0;

      /* Default the Previous condition code value (OUT parameter)
       * to be the current condition code value, in case
       * previous condition code values cannot be found.
       * The condition code field is smaller than the event status
       * code, so the assignment is safe.
       */
      av_InvPreviousCondition := lv_InvCurrentCondCd;

      /* Retrieve the previous condition change events for the specified
       * inventory.
       */
      OPEN lcur_ConditionChangeEvents (an_InvInvDbId, an_InvInvId);

      /* There should always be at least two condition change events
       * per inventory. However, there is nothing from preventing the
       * customer from bypassing certain steps in the life cycle.
       * If we can't find the desired number of previous inventory
       * condition values, then we will give them more recent values.
       */
      FOR i IN 1..2 LOOP

         FETCH lcur_ConditionChangeEvents INTO lv_EventStatusCd;

         /* The following ensures that the loop is exited safely if
          * there are not two rows returned or the fetch fails to
          * execute successfully.
          */
         EXIT WHEN lcur_ConditionChangeEvents%NOTFOUND OR
                   lcur_ConditionChangeEvents%NOTFOUND IS NULL;

         /* We know that a row was found. Assign the event status code
          * of the row returned to the parameter Previous condition code
          * value (OUT parameter).
          */
         av_InvPreviousCondition := lv_EventStatusCd;

      END LOOP;

      CLOSE lcur_ConditionChangeEvents;

   END GetPreviousInventoryCondition;
/********************************************************************************
*
* Procedure:      GetMostRecentRO
* Arguments:      an_InvInvDbId (IN NUMBER): DB_ID of the inventory to search for
*                                            the most recent RO.
*
*                 an_InvInvId (IN NUMBER):   ID of the inventory to search for
*                                            the most recent RO.
*
*                 an_EventDbIdRO:            DB_ID of the most recent RO, or 0
*                 (OUT NUMBER)               if no RO was found for the inventory
*
*                 an_EventIdRO:              ID of the most recent RO, or 0
*                 (OUT NUMBER)               if no RO was found for the inventory
*
*                 an_FoundRO (OUT NUMBER):   1 if an RO was found, 0 otherwise.
*
* Description:   This procedure determines gets a list of RO events for the
*                inventory specified by the input arguments, sorted by completion
*                date. Since the records are sorted in descending order,
*                the first record should be the most recent RO.
*                The primary key of the most recent RO is returned in the
*                OUT parameters (an_EventDbIdRO, an_EventIdRo).
*
*                StyleReports prevents the use of user-defined types in the
*                the stored procedure. For this reason, only standard types
*                are used for the stored procedure parameters.
*
* Orig.Coder:     Lisa Henderson
* Recent Coder:   Lisa Henderson
* Recent Date:    Aug 28, 2003
*
**********************************************************************************
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
   PROCEDURE GetMostRecentRO(
          an_InvInvDbId                  IN  inv_inv.inv_no_db_id%TYPE,
          an_InvInvId                    IN  inv_inv.inv_no_id%TYPE,
          av_IdRO                       OUT  sched_stask.wo_ref_sdesc%TYPE,
          an_FoundRO                    OUT  NUMBER)
   IS

   /* cursor declarations */
   CURSOR lcur_EventsRO (
         cl_InvInvDbId   inv_inv.inv_no_id%TYPE,
         cl_InvInvId     inv_inv.inv_no_id%TYPE
      ) IS
          SELECT
             sched_stask.sched_db_id,
             sched_stask.sched_id,
             sched_stask.wo_ref_sdesc
          FROM
             sched_stask,
             evt_event,
             evt_inv
          WHERE
             evt_inv.inv_no_db_id      = cl_InvInvDbId AND
             evt_inv.inv_no_id         = cl_InvInvId AND

             evt_event.event_db_id     = evt_inv.event_db_id AND
             evt_event.event_id        = evt_inv.event_id AND
             evt_event.hist_bool       = 1 AND
             evt_event.rstat_cd	       = 0
             AND

             sched_stask.sched_db_id   = evt_event.event_db_id AND
             sched_stask.sched_id      = evt_event.event_id AND
             sched_stask.task_class_cd = 'RO'

          ORDER BY
             evt_event.event_dt DESC;

          lrec_EventsRO lcur_EventsRO%ROWTYPE;

   BEGIN

      /* Open the cursor and execute the cursor, to determine if any RO events
       * were found for the specified inventory.
       */
      OPEN lcur_EventsRO (an_InvInvDbId, an_InvInvId);

      FETCH lcur_EventsRO INTO lrec_EventsRO;

      /* If one or more ROs are found,the first RO in the list should be
       * the most recent RO (cursor sorts the ROs in descending
       * completion date order).Assign the retrieved RO number
       * to the OUT parameter. Return success for the RO search.
       */
      IF lcur_EventsRO%FOUND
      THEN
         an_FoundRO := 1;
         av_IdRO    := lrec_EventsRO.wo_ref_sdesc;

      /* If no ROs are found, then set the OUT argument an_FoundRO
       * to false (zero, i.e. failure) and assign a NULL value
       * to the OUT parameters for the RO number.
       */
      ELSE
         an_FoundRO := 0;
         av_IdRO    := NULL;

      END IF;

      CLOSE lcur_EventsRO;

   END GetMostRecentRO;

END serviceability_tag_rpt_pkg;
/