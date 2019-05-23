--liquibase formatted sql


--changeSet serviceability_tag_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE serviceability_tag_rpt_pkg AS

   /* Declare externally visible functions */
   PROCEDURE GetPreviousInventoryCondition (
      an_InvInvDbId                 IN  inv_inv.inv_no_db_id%TYPE,
      an_InvInvId                   IN  inv_inv.inv_no_id%TYPE,
      av_InvPreviousCondition       OUT evt_event.event_status_cd%TYPE);

   PROCEDURE GetMostRecentRO (
      an_InvInvDbId                 IN  inv_inv.inv_no_db_id%TYPE,
      an_InvInvId                   IN  inv_inv.inv_no_id%TYPE,
      av_IdRO                       OUT  sched_stask.wo_ref_sdesc%TYPE,
      an_FoundRO                    OUT NUMBER);


END serviceability_tag_rpt_pkg;
/