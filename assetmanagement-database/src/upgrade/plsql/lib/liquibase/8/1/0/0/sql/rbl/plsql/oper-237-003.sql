--liquibase formatted sql


--changeSet oper-237-003:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE opr_report_period_pkg IS
   ----------------------------------------------------------------------------
   -- Object Name
   --
   --      opr_report_utility
   ---
   -- Description
   --
   --     Contains utility routines for opr_report_utility
   ----------------------------------------------------------------------------


   FUNCTION create_calendar_month
   (
      p_start_date opr_calendar_month.start_date%TYPE,
      p_end_date   opr_calendar_month.end_date%TYPE
   ) RETURN INTEGER;

END opr_report_period_pkg;
/