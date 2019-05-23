--liquibase formatted sql


--changeSet oper-237-005:1 stripComments:false
--
--
-- calendar month
--
--
CREATE OR REPLACE FORCE VIEW aopr_calendar_month_v1
AS 
SELECT 
   opr_calendar_month.year_code,
   opr_calendar_month.month_code,
   opr_calendar_month.start_date,
   opr_calendar_month.end_date,
   trunc(opr_calendar_month.end_date - opr_calendar_month.start_date) + 1 AS number_of_days,
   TO_CHAR(opr_calendar_month.start_date, 'Month')                        AS month_name
FROM 
   opr_calendar_month;