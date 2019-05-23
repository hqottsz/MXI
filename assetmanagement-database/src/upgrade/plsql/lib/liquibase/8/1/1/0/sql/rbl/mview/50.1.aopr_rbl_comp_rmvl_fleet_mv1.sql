--liquibase formatted sql


--changeSet 50.1.aopr_rbl_comp_rmvl_fleet_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_COMP_RMVL_FLEET_MV1');
END;
/

--changeSet 50.1.aopr_rbl_comp_rmvl_fleet_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_comp_rmvl_fleet_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   rmvl_curr.operator_id,
   rmvl_curr.operator_code,
   rmvl_curr.fleet_type,
   rmvl_curr.config_slot,   
   rmvl_curr.part_number,
   rmvl_curr.part_name,         
   SUBSTR(rmvl_curr.year_month,1,4) year_code,
   SUBSTR(rmvl_curr.year_month,-2) month_code,
   -- current month
   removal_curr,
   unsched_removal_curr,
   justified_failure_curr,
   total_cycle_curr,
   total_hour_curr,   
   --
   mtbr_curr,
   mcbr_curr,
   mtbur_curr,
   mcbur_curr,
   mtbf_curr,
   mcbf_curr,
   f_rate_curr,
   urr_hour_curr,
   urr_cycle_curr,   
   -- Q1 - between 1st and 3rd month
   removal_q1,
   unsched_removal_q1,
   justified_failure_q1,
   total_cycle_q1,
   total_hour_q1,   
   --   
   mtbr_q1,
   mcbr_q1,
   mtbur_q1,
   mcbur_q1,
   mtbf_q1,
   mcbf_q1,
   f_rate_q1,   
   urr_hour_q1,
   urr_cycle_q1,
   -- Q2 - between 4th and 6th month
   removal_q2,
   unsched_removal_q2,  
   justified_failure_q2,
   total_cycle_q2,
   total_hour_q2,   
   --       
   mtbr_q2,
   mcbr_q2,
   mtbur_q2,
   mcbur_q2,
   mtbf_q2,
   mcbf_q2,   
   f_rate_q2,      
   urr_hour_q2,
   urr_cycle_q2,   
   -- last 6 months
   removal_6mon,
   unsched_removal_6mon,
   justified_failure_6mon,
   total_cycle_6mon,
   total_hour_6mon,   
   --   
   mtbr_6mon,
   mcbr_6mon,
   mtbur_6mon,
   mcbur_6mon,
   mtbf_6mon,
   mcbf_6mon,   
   f_rate_6mon,      
   urr_hour_6mon,
   urr_cycle_6mon, 
   -- Q3 - between 7th and 9th month
   removal_q3,
   unsched_removal_q3,
   justified_failure_q3,
   total_cycle_q3,
   total_hour_q3,   
   --      
   mtbr_q3,
   mcbr_q3,
   mtbur_q3,
   mcbur_q3,
   mtbf_q3,
   mcbf_q3,   
   f_rate_q3,         
   urr_hour_q3,
   urr_cycle_q3,      
   -- Q4 - between 10th and 12th month
   removal_q4,
   unsched_removal_q4,   
   justified_failure_q4,
   total_cycle_q4,
   total_hour_q4,   
   --      
   mtbr_q4,
   mcbr_q4,
   mtbur_q4,
   mcbur_q4,
   mtbf_q4,
   mcbf_q4,      
   f_rate_q4,         
   urr_hour_q4,
   urr_cycle_q4,      
   -- last 12 months  
   removal_12mon,
   unsched_removal_12mon,   
   justified_failure_12mon,
   total_cycle_12mon,
   total_hour_12mon,   
   --      
   mtbr_12mon,
   mcbr_12mon,
   mtbur_12mon,
   mcbur_12mon,
   mtbf_12mon,
   mcbf_12mon,     
   f_rate_12mon,      
   urr_hour_12mon,
   urr_cycle_12mon,
   -- last 12 months UCL
   ucl_hour_12mon,
   ucl_cycle_12mon,
   -- last 24 months  
   removal_24mon,
   unsched_removal_24mon,   
   justified_failure_24mon,
   total_cycle_24mon,
   total_hour_24mon,   
   --      
   mtbr_24mon,
   mcbr_24mon,
   mtbur_24mon,
   mcbur_24mon,
   mtbf_24mon,
   mcbf_24mon,     
   f_rate_24mon,      
   urr_hour_24mon,
   urr_cycle_24mon,
   -- aircraft count
   rmvl_curr.inventory_class,
   quantity_per_acft,
   number_of_acft,
   NVL((rmvl_curr.quantity_per_acft * rmvl_curr.number_of_acft),0) AS number_of_units,
   --
   lower_fh.lower_interval_fh
FROM 
   aopr_rbl_comp_rmvl_curr_mv1 rmvl_curr
   INNER JOIN aopr_rbl_comp_rmvl_q1_mv1 rmvl_q1 ON 
      rmvl_curr.operator_id      = rmvl_q1.operator_id AND 
      rmvl_curr.fleet_type         = rmvl_q1.fleet_type AND
      rmvl_curr.config_slot        = rmvl_q1.config_slot AND
      rmvl_curr.part_number        = rmvl_q1.part_number AND
      rmvl_curr.year_month         = rmvl_q1.year_month       
   INNER JOIN aopr_rbl_comp_rmvl_q2_mv1 rmvl_q2 ON 
      rmvl_curr.operator_id      = rmvl_q2.operator_id AND 
      rmvl_curr.fleet_type         = rmvl_q2.fleet_type AND
      rmvl_curr.config_slot        = rmvl_q2.config_slot AND
      rmvl_curr.part_number        = rmvl_q2.part_number AND
      rmvl_curr.year_month         = rmvl_q2.year_month      
   INNER JOIN aopr_rbl_comp_rmvl_6mon_mv1 rmvl_6mon ON 
      rmvl_curr.operator_id      = rmvl_6mon.operator_id AND 
      rmvl_curr.fleet_type         = rmvl_6mon.fleet_type AND
      rmvl_curr.config_slot        = rmvl_6mon.config_slot AND
      rmvl_curr.part_number        = rmvl_6mon.part_number AND
      rmvl_curr.year_month         = rmvl_6mon.year_month
   INNER JOIN aopr_rbl_comp_rmvl_q3_mv1 rmvl_q3 ON 
      rmvl_curr.operator_id      = rmvl_q3.operator_id AND 
      rmvl_curr.fleet_type         = rmvl_q3.fleet_type AND
      rmvl_curr.config_slot        = rmvl_q3.config_slot AND
      rmvl_curr.part_number        = rmvl_q3.part_number AND
      rmvl_curr.year_month         = rmvl_q3.year_month       
   INNER JOIN aopr_rbl_comp_rmvl_q4_mv1 rmvl_q4 ON 
      rmvl_curr.operator_id      = rmvl_q4.operator_id AND 
      rmvl_curr.fleet_type         = rmvl_q4.fleet_type AND
      rmvl_curr.config_slot        = rmvl_q4.config_slot AND
      rmvl_curr.part_number        = rmvl_q4.part_number AND
      rmvl_curr.year_month         = rmvl_q4.year_month          
   INNER JOIN aopr_rbl_comp_rmvl_12mon_mv1 rmvl_12mon ON 
      rmvl_curr.operator_id      = rmvl_12mon.operator_id AND 
      rmvl_curr.fleet_type         = rmvl_12mon.fleet_type AND
      rmvl_curr.config_slot        = rmvl_12mon.config_slot AND
      rmvl_curr.part_number        = rmvl_12mon.part_number AND
      rmvl_curr.year_month         = rmvl_12mon.year_month 
   LEFT JOIN aopr_rbl_comp_rmvl_flt_ucl_mv1 last_12mon_ucl ON    
      rmvl_curr.operator_id      = last_12mon_ucl.operator_id AND 
      rmvl_curr.fleet_type         = last_12mon_ucl.fleet_type AND
      rmvl_curr.config_slot        = last_12mon_ucl.config_slot AND
      rmvl_curr.part_number        = last_12mon_ucl.part_number AND
      TO_NUMBER(SUBSTR(rmvl_curr.year_month,1,4)-1) = last_12mon_ucl.year_code
   INNER JOIN aopr_rbl_comp_rmvl_24mon_mv1 rmvl_24mon ON 
      rmvl_curr.operator_id      = rmvl_24mon.operator_id AND 
      rmvl_curr.fleet_type         = rmvl_24mon.fleet_type AND
      rmvl_curr.config_slot        = rmvl_24mon.config_slot AND
      rmvl_curr.part_number        = rmvl_24mon.part_number AND
      rmvl_curr.year_month         = rmvl_24mon.year_month
   -- lower FH interval
   LEFT JOIN acor_rbl_comp_ata_lower_fh_mv1 lower_fh ON 
      rmvl_curr.fleet_type  = lower_fh.fleet_type AND
      rmvl_curr.config_slot = lower_fh.config_slot;