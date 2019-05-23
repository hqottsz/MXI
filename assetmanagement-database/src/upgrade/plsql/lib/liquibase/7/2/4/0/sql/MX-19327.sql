--liquibase formatted sql


--changeSet MX-19327:1 stripComments:false
  UPDATE sched_stask
  SET    sched_stask.orphan_frct_bool = 1
  WHERE  sched_stask.orphan_frct_bool = 0
         AND
         ( sched_stask.sched_db_id, sched_stask.sched_id )
         IN
         ( WITH forecast_heads AS ( SELECT evt_event.event_db_id,
                                           evt_event.event_id
                                    FROM   evt_event
                                    WHERE  evt_event.event_status_cd = 'FORECAST'
                                           AND
                                           NOT EXISTS ( SELECT 1 
                                                        FROM   evt_event_rel 
                                                        WHERE  evt_event_rel.rel_event_db_id = evt_event.event_db_id AND 
                                                               evt_event_rel.rel_event_id    = evt_event.event_id    AND 
                                                               evt_event_rel.rel_type_cd     = 'DEPT'
                                                       )
                                   )
           SELECT rel_event_db_id AS sched_db_id,
                  rel_event_id    AS sched_id
           FROM  evt_event_rel
           START WITH  
                EXISTS ( SELECT 1 
                         FROM  forecast_heads
                         WHERE forecast_heads.event_db_id = evt_event_rel.event_db_id AND
                               forecast_heads.event_id    = evt_event_rel.event_id
                       )
                AND evt_event_rel.rel_type_cd  = 'DEPT'
           CONNECT BY 
                evt_event_rel.event_db_id  = PRIOR evt_event_rel.rel_event_db_id AND
                evt_event_rel.event_id     = PRIOR evt_event_rel.rel_event_id    AND
                evt_event_rel.rel_type_cd  = 'DEPT'         
           UNION ALL
           SELECT forecast_heads.event_db_id AS sched_db_id,
                  forecast_heads.event_id    AS sched_id
           FROM forecast_heads
         );         