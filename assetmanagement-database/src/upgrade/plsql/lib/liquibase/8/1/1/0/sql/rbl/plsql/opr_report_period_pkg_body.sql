--liquibase formatted sql


--changeSet opr_report_period_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_report_period_pkg IS

   ----------------------------------------------------------------------------
   -- Object Name
   --
   --      opr_report_utility
   ---
   -- Description
   --
   --     Contains utility routines for opr_report_utility
   ----------------------------------------------------------------------------

   ---------------------------------------------------------------------------------
   --      create_period
   --
   -- Description:
   --
   --   create a period type that falls betweeen a start date and end date
   --
   -- Notes:
   --
   --   start date - a period starts at midnight
   --   end_date   - a period ends at 1 second before midnight
   --
   ----------------------------------------------------------------------------
   FUNCTION create_calendar_month
   (
      p_start_date opr_calendar_month.start_date%TYPE,
      p_end_date   opr_calendar_month.end_date%TYPE
   ) return INTEGER
   AS

      l_start_date  opr_calendar_month.start_date%TYPE;
      l_end_date    opr_calendar_month.end_date%TYPE;

      l_row_count INTEGER;

   BEGIN

      --
      -- set the start of the period to the first day of the month
      l_start_date := TRUNC(nvl(p_start_date, SYSDATE),'MONTH');
      --
      --
      -- set the end of the period to the last day of the year
      l_end_date := TRUNC(last_day(nvl(p_end_date, SYSDATE)));
      --
      --
      MERGE INTO opr_calendar_month calendar_month
      USING
      (
         SELECT
           year_code                                AS year_code,
            month_code                              AS month_code,
            --
            -- a period starts at midnight
            trunc(start_date)                       AS start_date,
            --
            -- a period ends one (1) second before midnight
            trunc(end_date)+1-1/24/60/60            AS end_date
         FROM
            (
               SELECT
                  year_code                        AS year_code,
                  month_code                       AS month_code,
                  MIN(generated_date)              AS start_date,
                  MAX(generated_date)              AS end_date
               FROM
                  (
                     SELECT
                        to_char(generated_date, 'YYYY')  AS year_code,
                        to_char(generated_date, 'MM')    AS month_code,
                        generated_date
                     FROM
                        (
                           SELECT
                               l_start_date + LEVEL - 1 as generated_date
                           FROM
                              DUAL
                           CONNECT BY
                              LEVEL <= l_end_date - l_start_date + 1
                        )
                  )
               GROUP BY
                  year_code,
                  month_code
         )
      ) generated_period
      ON (
            generated_period.year_code  = calendar_month.year_code AND
            generated_period.month_code = calendar_month.month_code
         )
      WHEN MATCHED THEN
        UPDATE
        SET 
          calendar_month.start_date = generated_period.start_date,
          calendar_month.end_date   = generated_period.end_date         
      WHEN NOT MATCHED THEN
      INSERT
      (
         calendar_month.start_date,
         calendar_month.end_date,
         calendar_month.year_code,
         calendar_month.month_code
      )
      VALUES
      (
         generated_period.start_date,
         generated_period.end_date,
         generated_period.year_code,
         generated_period.month_code
      );
      l_row_count := SQL%ROWCOUNT;
      --
      RETURN (l_row_count);

   END create_calendar_month;

END opr_report_period_pkg;
/