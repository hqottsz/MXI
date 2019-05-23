--liquibase formatted sql


--changeSet oper-237-001:1 stripComments:false
CREATE OR REPLACE VIEW AOPR_RELIABILITY_V1 AS
SELECT
     monthly_reliability.year_code,
     monthly_reliability.month_code,
     monthly_reliability.fleet_type,
     monthly_reliability.operator_registration_code,
     monthly_reliability.serial_number,
     monthly_reliability.operator_code,
     monthly_reliability.cycles,
     monthly_reliability.flight_hours,
     monthly_reliability.cancelled_departures,
     monthly_reliability.diverted_departures,
     monthly_reliability.delay_time,
     monthly_reliability.delayed_departures_gt_15,
     monthly_reliability.delayed_departures,
     monthly_reliability.completed_departures,
     monthly_reliability.aog_delayed_departures,
     monthly_reliability.mel_delayed_departures,
     --
     --
     -- Dispatch Reliability Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         1 - (
               ( 
                 delayed_departures
                 + cancelled_departures
               ) /  
               (completed_departures + cancelled_departures)
             )
     end as TDRL,
     --
     --
     --
     --
     -- Dispatch Reliability Rate - when delay is greater than 15 minutes
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         1 - (
               ( 
                 delayed_departures_GT_15
                 + cancelled_departures
               ) /  
               (completed_departures + cancelled_departures)
             )
     end as TDRL_GT_15,
     --
     --
     -- Dispatch Interuption Rate
     case when completed_departures + cancelled_departures= 0
       then 0
       else
         (
               ( 
                 delayed_departures
                 + cancelled_departures
               ) /  
               (completed_departures/100 + cancelled_departures)
         )
     end AS TDIR,
     --
     --
     -- Dispatch Interuption Rate - when delay is greater than 15 minutes
     case when completed_departures + cancelled_departures= 0
       then 0
       else
         (
               ( 
                 delayed_departures_gt_15
                 + cancelled_departures
               ) /  
               (completed_departures/100 + cancelled_departures)
         )
     end AS TDIR_GT_15,
     --
     --
     -- Schedule Reliability
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         1 - (
               ( 
                  delayed_departures
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /  
               (completed_departures + cancelled_departures)
             )
     end AS SREL,
     --
     --
     -- Schedule Reliability - when the delay is greater than 15 minutes
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         1 - (
               ( 
                  delayed_departures_gt_15
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /  
               (completed_departures + cancelled_departures)
             )
     end AS SREL_GT_15,
     --
     --
     -- Schedule Interuption Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
             (
               ( 
                  delayed_departures
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /  
               (completed_departures/100 + cancelled_departures)
             )
     end AS SIRT,
     --
     --
     -- Schedule Interuption Rate - when the delay is greater than 15 minutes
     case when completed_departures + cancelled_departures = 0
       then 0
       else
             (
               ( 
                  delayed_departures_gt_15
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /  
               (completed_departures/100 + cancelled_departures)
             )
     end AS SIRT_GT_15,
     --
     --
     -- Technical Cancellation Performance
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         1 - (
               ( 
                 + cancelled_departures
               ) /  
               (completed_departures + cancelled_departures)
             )
     end AS TCPF,
     --
     --
     -- Technical Cancellation Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
             (
               ( 
                 + cancelled_departures
               ) /  
               (completed_departures/100 + cancelled_departures)
             )
     end AS TCNR,
     --
     --
     -- Technical Completion Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         1 - (
               ( 
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /  
               (completed_departures + cancelled_departures)
             )
     end AS TCRT,
     --
     --
     -- Technical Non Completion Rate
     case when completed_departures + cancelled_departures = 0
       then 0
       else
         1 - (
               ( 
                 + cancelled_departures
                 + diverted_departures
                 + air_turnbacks
               ) /  
               (completed_departures/100 + cancelled_departures)
             )
     end AS TNCR,
     --
     --
     -- Diversion Rate
     case when cycles = 0
       then 0
       else
          1000 * (diverted_departures ) / cycles
     end AS DVRT,
     --
     --
     -- Air Turnback Rate
     case when cycles = 0
       then 0
       else
          1000 * (air_turnbacks) / cycles
     end AS ATBR,
     --
     --
     -- Aborted Takeoff Rate
     case when cycles = 0
       then 0
       else
          1000 * (aborted_departures) / cycles
     end AS ABTR,
     --
     --
     -- Return To Gate Rate
     case when cycles = 0
       then 0
       else
          1000 * (return_to_gate) / cycles
     end AS RTGR,
     --
     --
     -- Aborted Approach Rate
     case when cycles = 0
       then 0
       else
          1000 * (aborted_approaches) / cycles
     end AS ABAR,
     --
     --
     -- Emergency Descent Rate
     case when cycles = 0
       then 0
       else
          1000 * (emergency_descent) / cycles
     end AS EMDR,
     --
     --
     -- Inflight Shutdown Rate
     case when cycles = 0
       then 0
       else
          1000 * (inflight_shutdowns) / cycles
     end AS IFSDR,
     --
     --
     -- Total Air Incident Rate
     case when cycles = 0
       then 0
       else
          1000 * (tair_incidents) / cycles
     end AS TAIR,
     --
     --
     -- Total Incident Rate
     case when cycles = 0
       then 0
       else
          1000 * (tinr_incidents) / cycles
     end AS TINR
FROM
   opr_monthly_reliability monthly_reliability;