--liquibase formatted sql


--changeSet mv_defer_fault_part_req:1 stripComments:false
/********************************************************************************
*
* View:           mv_defer_fault_part_req
*
* Description:    This materialized view will present the details of the deferred
*                 fault part requests list. A part request must be on a non-historical
*                 task that is under a corrective task for a fault that is deferred.
*
*
* Orig.Coder:    jclarkin
* Recent Coder:  dmacdonald
* Recent Date:   2017.11.27
*
*********************************************************************************/
CREATE MATERIALIZED VIEW mv_defer_fault_part_req
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT
   -- part request information
   request_event.event_db_id                                   AS req_part_db_id,
   request_event.event_id                                      AS req_part_id,
   getAvailableLocalForReqPart( req_part.req_part_db_id, req_part.req_part_id )     AS avail_local,
   getAvailableRemoteForReqPart( req_part.req_part_db_id, req_part.req_part_id )    AS avail_remote,
   getAvailableUSForReqPart( req_part.req_part_db_id, req_part.req_part_id )        AS avail_us,
   getAvailableOnOrderForReqPart( req_part.req_part_db_id, req_part.req_part_id )   AS avail_on_order,
   getExchangeAvailability(request_event.event_db_id, request_event.event_id, request_loc.loc_db_id, request_loc.loc_id) AS exchange,
   task_event.*
FROM
   ref_event_type,
   evt_event request_event,
   req_part,
   (  SELECT
         evt_event.event_db_id,
         evt_event.event_id,
         evt_event.event_sdesc,
         evt_event.h_event_db_id,
         evt_event.h_event_id
      FROM
         evt_event
      CONNECT BY
         PRIOR evt_event.event_db_id = evt_event.nh_event_db_id AND
         PRIOR evt_event.event_id    = evt_event.nh_event_id
      START WITH
         ( evt_event.event_db_id, evt_event.event_id )
         IN
         (
            SELECT
               evt_event_rel.rel_event_db_id,
               evt_event_rel.rel_event_id
            FROM
               ref_rel_type,
               ref_event_status,
               evt_event fault_event,
               evt_event_rel
            WHERE
               ref_rel_type.rel_type_db_id = 0 AND
               ref_rel_type.rel_type_cd    = 'CORRECT'
               AND
               evt_event_rel.rel_type_db_id = ref_rel_type.rel_type_db_id AND
               evt_event_rel.rel_type_cd    = ref_rel_type.rel_type_cd
               AND
               fault_event.event_db_id = evt_event_rel.event_db_id AND
               fault_event.event_id    = evt_event_rel.event_id
               AND
               fault_event.hist_bool = 0
               AND
               ref_event_status.event_status_db_id = 0 AND
               ref_event_status.event_status_cd    = 'CFDEFER'
               AND
               fault_event.event_status_db_id = ref_event_status.event_status_db_id AND
               fault_event.event_status_cd    = ref_event_status.event_status_cd
         )
   ) task_event,
   inv_loc request_loc
WHERE
   ref_event_type.event_type_db_id = 0 AND
   ref_event_type.event_type_cd = 'PR'
   AND
   request_event.event_type_db_id = ref_event_type.event_type_db_id AND
   request_event.event_type_cd    = ref_event_type.event_type_cd AND
   request_event.hist_bool = 0
   AND
   req_part.req_part_db_id = request_event.event_db_id AND
   req_part.req_part_id    = request_event.event_id
   AND
   request_loc.loc_db_id (+)= req_part.req_loc_db_id AND
   request_loc.loc_id    (+)= req_part.req_loc_id
   AND
   -- get the task
   task_event.event_db_id = req_part.pr_sched_db_id AND
   task_event.event_id    = req_part.pr_sched_id;
