--liquibase formatted sql


--changeSet oper-237-001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE opr_fleet_summary_pkg IS

   ----------------------------------------------------------------------------
   -- Object Name
   --
   --      opr_fleet_summary
   ---
   -- Description
   --
   --     ETL for Fleet Statistics
   --
   ----------------------------------------------------------------------------

  ---------------------------------------------------------------------------------
   -- function
   --
   --      extract usage
   --
   -- Description:
   --
   --   extract detailed usage from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_usage
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract fault
   --
   -- Description:
   --
   --   extract fault information from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------

   FUNCTION extract_faults
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract fault incidents
   --
   -- Description:
   --
   --   extract fault incident(s) from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_fault_incidents
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract flights
   --
   -- Description:
   --
   --   extract flight information from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_flights
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract flight disruptions
   --
   -- Description:
   --
   --   extract flight disruption information from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_flight_disruptions
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract maitenance
   --
   -- Description:
   --
   --   extract maintenance information from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_maintenance
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      summarize usage
   --
   -- Description:
   --
   --   summarize detailed usage by
   --
   --      - operator registration code
   --      - period
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION summarize_usage
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      summarize departures
   --
   -- Description:
   --
   --   summarize departures
   --
   --      - operator registration code
   --      - serial number
   --      - operator code
   --      - year
   --      - month
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION summarize_departures
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate completed departures
   --
   -- Description:
   --
   --   calculate completed departures  by
   --
   --      - operator registration code
   --      - serial number
   --      - operator code
   --      - disruption type
   --      - month
   --      - year
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_completed_departures
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate cancelled departures
   --
   -- Description:
   --
   --   calculate cancelled departures  by
   --
   --      - operator registration code
   --      - serial number
   --      - operator code
   --      - disruption type
   --      - month
   --      - year
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_cancelled_departures
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate mel departures
   --
   -- Description:
   --
   --   calculate mel departures  by
   --
   --      - operator registration code
   --      - serial number
   --      - operator code
   --      - disruption type
   --      - month
   --      - year
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_mel_departures
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate departure breakdown
   --
   -- Description:
   --
   --   calculate departure breakdown  by
   --
   --      - delay category code
   --      - operator registration code
   --      - serial number
   --      - operator code
   --      - disruption type
   --      - month
   --      - year
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_departure_breakdown
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate incident breakdown
   --
   -- Description:
   --
   --   calculate incident breakdown  by
   --
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_incident_breakdown
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      summarize incidents
   --
   -- Description:
   --
   --   calculate completed departures  by
   --
   --      - operator registration code
   --      - serial number
   --      - operator code
   --      - disruption type
   --      - month
   --      - year
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION summarize_incidents
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
  ---------------------------------------------------------------------------------
   -- function
   --
   --      summarize tair incidents
   --
   -- Description:
   --
   --   summarize incidents  by
   --
   --      - year
   --      - month
   --      - operator registration code
   --      - serial number
   --      - operator code
   --      - incident type code
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION summarize_tair_incidents
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

END opr_fleet_summary_pkg;
/