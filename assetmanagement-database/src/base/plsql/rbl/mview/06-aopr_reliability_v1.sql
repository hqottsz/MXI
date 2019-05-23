--liquibase formatted sql


--changeSet 06-aopr_reliability_v1:1 stripComments:false
CREATE MATERIALIZED VIEW AOPR_RELIABILITY_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
     monthly_dispatch.year_code,
     monthly_dispatch.month_code,
     monthly_dispatch.year_month,
     monthly_dispatch.start_date,
     monthly_dispatch.end_date,
     monthly_dispatch.fleet_type,
     monthly_dispatch.operator_registration_code,
     monthly_dispatch.serial_number,
     monthly_dispatch.operator_code,
     monthly_dispatch.cycles,
     monthly_dispatch.flight_hours,
     monthly_dispatch.cancelled_departures,
     monthly_dispatch.diverted_departures,
     monthly_dispatch.delay_time,
     monthly_dispatch.delayed_departures_gt_15,
     monthly_dispatch.delayed_departures,
     monthly_dispatch.completed_departures,
     monthly_dispatch.aog_delayed_departures,
     monthly_dispatch.mel_delayed_departures,
     monthly_dispatch.air_turnbacks,
     monthly_dispatch.aborted_departures,
     monthly_dispatch.ground_turn_backs,
     monthly_dispatch.aborted_approaches,
     monthly_dispatch.emergency_descents,
     monthly_dispatch.inflight_shutdowns,
     --
     --
     -- Dispatch Reliability Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         (1 - round((
                (
                 delayed_departures
                 + cancelled_departures
                ) /
                (completed_departures + cancelled_departures)
              ),4)) * 100
     end as TDRL,
     --
     --
     --
     --
     -- Dispatch Reliability Rate - when delay is greater than 15 minutes
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         (1 - round((
               (
                 delayed_departures_GT_15
                 + cancelled_departures
               ) /
               (completed_departures + cancelled_departures)
              ),4)) * 100
     end as TDRL_GT_15,
     --
     --
     -- Dispatch Interuption Rate
     case when completed_departures + cancelled_departures= 0
       then 0
       else
          round((
               (
                 delayed_departures
                 + cancelled_departures
               ) /
               (completed_departures + cancelled_departures)
         ),4) * 100
     end AS TDIR,
     --
     --
     -- Dispatch Interuption Rate - when delay is greater than 15 minutes
     case when completed_departures + cancelled_departures= 0
       then 0
       else
         round((
               (
                 delayed_departures_gt_15
                 + cancelled_departures
               ) /
               (completed_departures + cancelled_departures)
         ),4)* 100
     end AS TDIR_GT_15,
     --
     --
     -- Schedule Reliability
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         (1 - round((
               (
                  delayed_departures
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /
               (completed_departures + cancelled_departures)
             ),4)) * 100
     end AS SREL,
     --
     --
     -- Schedule Reliability - when the delay is greater than 15 minutes
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         (1 - round((
               (
                  delayed_departures_gt_15
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /
               (completed_departures + cancelled_departures)
             ),4)) * 100
     end AS SREL_GT_15,
     --
     --
     -- Schedule Interuption Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
             round((
               (
                  delayed_departures
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /
               (completed_departures + cancelled_departures)
             ),4) * 100
     end AS SIRT,
     --
     --
     -- Schedule Interuption Rate - when the delay is greater than 15 minutes
     case when completed_departures + cancelled_departures = 0
       then 0
       else
             round((
               (
                  delayed_departures_gt_15
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /
               (completed_departures + cancelled_departures)
             ),4) * 100
     end AS SIRT_GT_15,
     --
     --
     -- Technical Cancellation Performance
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         (1 - round((
               (
                 + cancelled_departures
               ) /
               (completed_departures + cancelled_departures)
             ),4)) * 100
     end AS TCPF,
     --
     --
     -- Technical Cancellation Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
             round((
               (
                 + cancelled_departures
               ) /
               (completed_departures + cancelled_departures)
             ),4) * 100
     end AS TCNR,
     --
     --
     -- Technical Completion Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         (1 - round((
               (
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /
               (completed_departures + cancelled_departures)
             ),4)) * 100
     end AS TCRT,
     --
     --
     -- Technical Non Completion Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         (round((
               (
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /
               (completed_departures + cancelled_departures)
             ),4)) * 100
     end AS TNCR,
     --
     --
     -- Diversion Rate
     case when cycles = 0
       then 0
       else
          round((diverted_departures ) / cycles,4) * 1000
     end AS DVRT,
     --
     --
     -- Air Turnback Rate
     case when cycles = 0
       then 0
       else
          round( (air_turnbacks) / cycles,4) * 1000
     end AS ATBR,
     --
     --
     -- Aborted Takeoff Rate
     case when cycles = 0
       then 0
       else
          round ((aborted_departures) / cycles,4) * 1000
     end AS ABTR,
     --
     --
     -- Return To Gate Rate
     case when cycles = 0
       then 0
       else
          round ((ground_turn_backs) / cycles,4) * 1000
     end AS RTGR,
     --
     --
     -- Aborted Approach Rate
     case when cycles = 0
       then 0
       else
          round((aborted_approaches) / cycles,4) * 1000
     end AS ABAR,
     --
     --
     -- Emergency Descent Rate
     case when cycles = 0
       then 0
       else
          round((emergency_descents) / cycles,4) * 1000
     end AS EMDR,
     --
     --
     -- Inflight Shutdown Rate
     case when cycles = 0
       then 0
       else
          round((inflight_shutdowns) / cycles,4) * 1000
     end AS IFSDR,
     --
     --
     -- Total Air Incident Rate
     case when cycles = 0
       then 0
       else
          round( (general_air_interruptions) / cycles,4) * 1000
     end AS TAIR,
     --
     --
     -- Total Incident Rate
     case when cycles = 0
       then 0
       else
          round( (general_air_interruptions) / cycles,4) * 1000
     end AS TINR
FROM
   opr_rbl_monthly_dispatch_mv monthly_dispatch;