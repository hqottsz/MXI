--liquibase formatted sql


--changeSet 50.1.aopr_rbl_eng_delay_mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_ENG_DELAY_MON_MV1');
END;
/

--changeSet 50.1.aopr_rbl_eng_delay_mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_eng_delay_mon_mv1 
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS   
SELECT
   operator_id,
   operator_code,
   fleet_type,
   engine_type,
   delays,
   year_code,
   month_code,
   quantity
FROM 
   (
     SELECT
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        quantity,
        CASE 
           WHEN cycles > 0 THEN
              (quantity/cycles)*100
           ELSE 0
        END AS rate_per_100_cycles     
     FROM 
       opr_rbl_eng_delay_mon
   )
     UNPIVOT ( (quantity) 
                FOR delays IN ( 
                                quantity               AS 'Number',
                                rate_per_100_cycles    AS 'Rate per 100 Engine Cycles'
                              )
             );   