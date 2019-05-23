--liquibase formatted sql


--changeSet opr_fleet_summary_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

   FUNCTION extract_fault
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
   FUNCTION extract_fault_incident
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract flight
   --
   -- Description:
   --
   --   extract flight information from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_flight
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract flight disruption
   --
   -- Description:
   --
   --   extract flight disruption information from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_flight_disruption
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract work package
   --
   -- Description:
   --
   --   extract work package information from maintenix
   --
   -- Notes:
   --
   ----------------------------------------------------------------------------
   FUNCTION extract_work_package
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
   --      calcualte_dos
   --
   -- Description:
   --
   --   calculate days oot of service
   --
   --      - operator registration code
   --      - period
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_dos_pt1
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      calcualte_dos_pt2
   --
   -- Description:
   --
   --   calculate days oot of service
   --
   --      - operator registration code
   --      - period
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_dos_pt2
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate_dos_pt3
   --
   -- Description:
   --
   --   calculate days out of service
   --
   --
   -- Notes:
   --
   --
   ----------------------------------------------------------------------------
   FUNCTION calculate_dos_pt3
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
END opr_fleet_summary_pkg;
/