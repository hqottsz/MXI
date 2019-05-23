--liquibase formatted sql


--changeSet 50.1.aopr_rbl_eng_indicator_mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_ENG_INDICATOR_MON_MV1');
END;
/

--changeSet 50.1.aopr_rbl_eng_indicator_mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_eng_indicator_mon_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS   
SELECT
   operator_id,
   operator_code,
   fleet_type,
   engine_type,
   oper_indicator,
   year_code,
   month_code,
   quantity          
FROM 
   opr_rbl_eng_indicator_mon   
-- maintenix indicator 
UNION ALL 
-- to historical daa
SELECT
   operator_id, 
   operator_code, 
   fleet_type, 
   engine_type, 
   oper_indicator,
   year_code, 
   month_code,
   quantity
FROM 
  (
     SELECT
        operator.operator_id, 
        operator.operator_code, 
        fleet_type, 
        engine_type, 
        year_code, 
        month_code, 
        ifsd, 
        power_loss, 
        rejected_t_o, 
        full_power_to, 
        flight_cancelation
     FROM 
        opr_rbl_hist_eng_indicator_mon
        INNER JOIN acor_operator_v1 operator ON 
           opr_rbl_hist_eng_indicator_mon.operator_code = operator.operator_code         
  ) -- unpivot - column to row
  UNPIVOT ( (quantity) 
             FOR oper_indicator IN ( 
                                     ifsd               AS 'IFSD',
                                     power_loss         AS 'Power Loss',
                                     rejected_t_o       AS 'Rejected T/O',
                                     full_power_to      AS 'Full Power T/O',
                                     flight_cancelation AS 'Flight Cancellation'
                                   )
           );