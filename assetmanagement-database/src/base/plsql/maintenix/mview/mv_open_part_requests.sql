--liquibase formatted sql


--changeSet mv_open_part_requests:1 stripComments:false
/********************************************************************************
*
* View:           mv_open_part_requests
*
* Description:    This materialized view will present the details of the open part
*                 requests list
*
* Orig.Coder:    jclarkin
* Recent Coder:
* Recent Date:   2008.06.13
*
*********************************************************************************/
CREATE MATERIALIZED VIEW mv_open_part_requests
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT
   -- part request information
   req_part.req_part_db_id,
   req_part.req_part_id,
   evt_event.event_sdesc                                                            AS req_part_sdesc,
   getAvailableLocalForReqPart( req_part.req_part_db_id, req_part.req_part_id )     AS avail_local,
   getAvailableRemoteForReqPart( req_part.req_part_db_id, req_part.req_part_id )    AS avail_remote,
   getAvailableUSForReqPart( req_part.req_part_db_id, req_part.req_part_id )        AS avail_us,
   getAvailableOnOrderForReqPart( req_part.req_part_db_id, req_part.req_part_id )   AS avail_on_order,
   getPossibleKits( req_part.req_part_db_id, req_part.req_part_id )                 AS possible_kits,
   getExchangeAvailability(
      req_part.req_part_db_id, req_part.req_part_id,
      req_part.req_loc_db_id, req_part.req_loc_id
   )                                                                                AS exchange
FROM
   req_part,
   evt_event
WHERE
   -- only get part requests without inventory that have a location and where autoreservation has run
   req_part.inv_no_db_id      IS NULL      AND
   req_part.req_loc_db_id     IS NOT NULL  AND
   req_part.last_auto_rsrv_dt IS NOT NULL
   AND
   -- only get open part requests
   evt_event.event_db_id = req_part.req_part_db_id AND
   evt_event.event_id    = req_part.req_part_id    AND
   evt_event.event_status_db_id = 0 AND
   evt_event.event_status_cd    = 'PROPEN'
   AND NOT EXISTS
   (
     SELECT
        1
     FROM
        evt_event task_evt_event
     WHERE
        task_evt_event.event_db_id = req_part.pr_sched_db_id AND
        task_evt_event.event_id    = req_part.pr_sched_id
        AND
        task_evt_event.hist_bool = 1
   );