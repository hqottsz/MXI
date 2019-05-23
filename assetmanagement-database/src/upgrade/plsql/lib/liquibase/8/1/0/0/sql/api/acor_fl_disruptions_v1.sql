--liquibase formatted sql


--changeSet acor_fl_disruptions_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fl_disruptions_v1
AS 
SELECT
   fl_leg.leg_id,
   fl_leg_disrupt.leg_disrupt_id,
   sched_stask.alt_id                    AS sched_id,
   fl_leg_disrupt_type.disrupt_type_cd   AS disruption_type_code,
   fl_leg_disrupt.ext_ref                AS external_reference,
   fl_leg_disrupt.delay_code_cd          AS delay_code,
   fl_leg_disrupt.flight_stage_cd        AS flight_stage_code,
   FLOOR((fl_leg.actual_departure_dt-fl_leg.sched_departure_dt)*24) || ':' ||
     MOD((fl_leg.actual_departure_dt-fl_leg.sched_departure_dt)*24,1)*60 AS total_delay_time,
   CASE
     WHEN INSTR(fl_leg_disrupt.maint_delay_time_qt,':') > 0 THEN
      TO_CHAR(fl_leg_disrupt.maint_delay_time_qt)
     ELSE
       '0:' || TO_CHAR(fl_leg_disrupt.maint_delay_time_qt)
   END                                   AS maintenance_delay_time,
   maint_delay_time_qt,
   fl_leg_disrupt.tech_delay_bool        AS technical_delay_flag
FROM
   fl_leg
   INNER JOIN fl_leg_disrupt ON
      fl_leg.leg_id = fl_leg_disrupt.leg_id
   LEFT JOIN sched_stask ON
      fl_leg_disrupt.sched_db_id = sched_stask.sched_db_id AND
      fl_leg_disrupt.sched_id    = sched_stask.sched_id
   INNER JOIN fl_leg_disrupt_type ON
      fl_leg_disrupt.leg_disrupt_id = fl_leg_disrupt_type.leg_disrupt_id
;