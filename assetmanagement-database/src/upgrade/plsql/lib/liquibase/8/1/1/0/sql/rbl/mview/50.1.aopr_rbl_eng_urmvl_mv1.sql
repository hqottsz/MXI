--liquibase formatted sql


--changeSet 50.1.aopr_rbl_eng_urmvl_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_ENG_URMVL_MV1');
END;
/

--changeSet 50.1.aopr_rbl_eng_urmvl_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_eng_urmvl_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS   
WITH
rvw_removals
AS
   (
     SELECT
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        removals,
        year_code,
        month_code,
        quantity,
         TO_NUMBER(year_code || month_code) year_month,
         TRUNC(TO_DATE(year_code || month_code,'YYYYMM'),'MONTH') start_date, 
         LAST_DAY(TO_DATE(year_code || month_code,'YYYYMM')) end_date,            
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-1),'YYYYMM')) ym_m2,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-2),'YYYYMM')) ym_m3,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-3),'YYYYMM')) ym_m4,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-4),'YYYYMM')) ym_m5,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-5),'YYYYMM')) ym_m6,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-6),'YYYYMM')) ym_m7,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-7),'YYYYMM')) ym_m8,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-8),'YYYYMM')) ym_m9,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-9),'YYYYMM')) ym_m10,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-10),'YYYYMM')) ym_m11,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-11),'YYYYMM')) ym_m12,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-12),'YYYYMM')) ym_m13,                  
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-13),'YYYYMM')) ym_m14,                  
         TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(year_code || month_code,'YYYYMM'),-14),'YYYYMM')) ym_m15                       
     FROM 
        aopr_rbl_eng_urmvl_mon_mv1   
   )
SELECT
   m1.operator_id, 
   m1.operator_code, 
   m1.fleet_type, 
   m1.engine_type, 
   m1.removals,
   m1.year_code, 
   m1.month_code,
   m1.start_date,
   m1.end_date,
   NVL(m1.quantity,0) m1,
   NVL(m2.quantity,0) m2,
   NVL(m3.quantity,0) m3,
   NVL(m4.quantity,0) m4,
   NVL(m5.quantity,0) m5,
   NVL(m6.quantity,0) m6,
   NVL(m7.quantity,0) m7,
   NVL(m8.quantity,0) m8,
   NVL(m9.quantity,0) m9,
   NVL(m10.quantity,0) m10,
   NVL(m11.quantity,0) m11,
   NVL(m12.quantity,0) m12,
   NVL(m13.quantity,0) m13,
   NVL(m14.quantity,0) m14,
   NVL(m15.quantity,0) m15 
FROM  
   rvw_removals m1
   LEFT JOIN rvw_removals m2 ON
      m1.operator_id = m2.operator_id AND
      m1.fleet_type  = m2.fleet_type AND
      m1.engine_type = m2.engine_type AND
      m1.removals    = m2.removals
      AND
      m1.ym_m2 = m2.year_month
   LEFT JOIN rvw_removals m3 ON
      m1.operator_id = m3.operator_id AND
      m1.fleet_type  = m3.fleet_type AND
      m1.engine_type = m3.engine_type AND
      m1.removals    = m3.removals
      AND
      m1.ym_m3 = m3.year_month
   LEFT JOIN rvw_removals m4 ON
      m1.operator_id = m4.operator_id AND
      m1.fleet_type  = m4.fleet_type AND
      m1.engine_type = m4.engine_type AND
      m1.removals    = m4.removals      
      AND
      m1.ym_m4 = m4.year_month
   LEFT JOIN rvw_removals m5 ON
      m1.operator_id = m5.operator_id AND
      m1.fleet_type  = m5.fleet_type AND
      m1.engine_type = m5.engine_type and
      m1.removals    = m5.removals          
      AND
      m1.ym_m5 = m5.year_month
   LEFT JOIN rvw_removals m6 ON
      m1.operator_id = m6.operator_id AND
      m1.fleet_type  = m6.fleet_type AND
      m1.engine_type = m6.engine_type AND
      m1.removals    = m6.removals      
      AND
      m1.ym_m6 = m6.year_month
   LEFT JOIN rvw_removals m7 ON
      m1.operator_id = m7.operator_id AND
      m1.fleet_type  = m7.fleet_type AND
      m1.engine_type = m7.engine_type AND
      m1.removals    = m7.removals      
      AND
      m1.ym_m7 = m7.year_month
   LEFT JOIN rvw_removals m8 ON
      m1.operator_id = m8.operator_id AND
      m1.fleet_type  = m8.fleet_type AND
      m1.engine_type = m8.engine_type AND
      m1.removals    = m8.removals            
      AND
      m1.ym_m8 = m8.year_month
   LEFT JOIN rvw_removals m9 ON
      m1.operator_id = m9.operator_id AND
      m1.fleet_type  = m9.fleet_type AND
      m1.engine_type = m9.engine_type AND
      m1.removals    = m9.removals      
      AND
      m1.ym_m9 = m9.year_month
   LEFT JOIN rvw_removals m10 ON
      m1.operator_id = m10.operator_id AND
      m1.fleet_type  = m10.fleet_type AND
      m1.engine_type = m10.engine_type AND
      m1.removals    = m10.removals      
      AND
      m1.ym_m10 = m10.year_month
   LEFT JOIN rvw_removals m11 ON
      m1.operator_id = m11.operator_id AND
      m1.fleet_type  = m11.fleet_type AND
      m1.engine_type = m11.engine_type AND
      m1.removals    = m11.removals      
      AND
      m1.ym_m11 = m11.year_month
   LEFT JOIN rvw_removals m12 ON
      m1.operator_id = m12.operator_id AND
      m1.fleet_type  = m12.fleet_type AND
      m1.engine_type = m12.engine_type AND
      m1.removals    = m12.removals       
      AND
      m1.ym_m12 = m12.year_month
   LEFT JOIN rvw_removals m13 ON
      m1.operator_id = m13.operator_id AND
      m1.fleet_type  = m13.fleet_type AND
      m1.engine_type = m13.engine_type AND
      m1.removals    = m13.removals       
      AND
      m1.ym_m13 = m13.year_month
   LEFT JOIN rvw_removals m14 ON
      m1.operator_id = m14.operator_id AND
      m1.fleet_type  = m14.fleet_type AND
      m1.engine_type = m14.engine_type AND
      m1.removals    = m14.removals       
      AND
      m1.ym_m14 = m14.year_month
   LEFT JOIN rvw_removals m15 ON
      m1.operator_id = m15.operator_id AND
      m1.fleet_type  = m15.fleet_type AND
      m1.engine_type = m15.engine_type AND
      m1.removals    = m15.removals       
      AND
      m1.ym_m15 = m15.year_month;         