--liquibase formatted sql


--changeSet oper-237-003:1 stripComments:false
delete from opr_report;

--changeSet oper-237-003:2 stripComments:false
-- usage extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'USAGE-XTRACK',
   'Extraction: Usage',
   'opr_fleet_summary_pkg.extract_usage(:aidt_start_date, :aidt_end_date)',
   'XTRACK',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   1
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'USAGE-XTRACK'
              );

--changeSet oper-237-003:3 stripComments:false
-- maintenance extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'MAINTENANCE-XTRACK',
   'Extraction: Maintenance',
   'opr_fleet_summary_pkg.extract_maintenance(:aidt_start_date, :aidt_end_date)',
   'XTRACK',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   2
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'MAINTENANCE-XTRACK'
              );

--changeSet oper-237-003:4 stripComments:false
-- faults extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'FAULTS-XTRACK',
   'Extraction: Faults',
   'opr_fleet_summary_pkg.extract_faults(:aidt_start_date, :aidt_end_date)',
   'XTRACK',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   3
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'FAULTS-XTRACK'
              );

--changeSet oper-237-003:5 stripComments:false
-- fault incidents extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'FAULT-INCIDENTS-XTRACK',
   'Extraction: Fault Incidents',
   'opr_fleet_summary_pkg.extract_fault_incidents(:aidt_start_date, :aidt_end_date)',
   'XTRACK',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   4
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'FAULT-INCIDENTS-XTRACK'
              );

--changeSet oper-237-003:6 stripComments:false
-- flights extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'FLIGHTS-XTRACK',
   'Extraction: Flights',
   'opr_fleet_summary_pkg.extract_flights(:aidt_start_date, :aidt_end_date)',
   'XTRACK',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   5
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'FLIGHTS-XTRACK'
              );

--changeSet oper-237-003:7 stripComments:false
-- flights extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'FLIGHT-DISRUPTIONS-XTRACK',
   'Extraction: Flight Disruptions',
   'opr_fleet_summary_pkg.extract_flight_disruptions(:aidt_start_date, :aidt_end_date)',
   'XTRACK',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   6
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'FLIGHT-DISRUPTIONS'
              );

--changeSet oper-237-003:8 stripComments:false
-- pirepmarep extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'PIREPMAREP-XTRACK',
   'Extraction: Pilot, Mechanic and Cabin faults report',
   'opr_rbl_fault_pkg.opr_rbl_fault_xtract_proc(:aidt_start_date, :aidt_end_date)',
   'XTRACK',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   7
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'PIREPMAREP-XTRACK'
              );

--changeSet oper-237-003:9 stripComments:false
-- summarize:usage
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'CREATE-CALENDAR-XFORM',
   'Create Calendar Month',
   'opr_report_period_pkg.create_calendar_month(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   1
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'CREATE-CALENDAR-XFORM'
              );

--changeSet oper-237-003:10 stripComments:false
-- summarize:usage
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'USAGE-XFORM',
   'Summarize: Usage',
   'opr_fleet_summary_pkg.summarize_usage(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   2
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'USAGE-XFORM'
              );

--changeSet oper-237-003:11 stripComments:false
-- calculate cancelled departures
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'CANCELLED-DEPARTURES-XFORM',
   'Calculate Cancelled Departures',
   'opr_fleet_summary_pkg.calculate_cancelled_departures(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   3
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'CANCELLED-DEPARTURES-XFORM'
              );

--changeSet oper-237-003:12 stripComments:false
-- calculate completed departures
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'COMPLETED-DEPARTURES-XFORM',
   'Calculate Completed Departures',
   'opr_fleet_summary_pkg.calculate_completed_departures(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   4
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'COMPLETED-DEPARTURES-XFORM'
              );

--changeSet oper-237-003:13 stripComments:false
-- calculate MEL departures
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'CALCULATE-MEL-DEPARTURES-XFORM',
   'Calculate Mel Departures',
   'opr_fleet_summary_pkg.calculate_mel_departures(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   5
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'CALCULATE-MEL-DEPARTURES-XFORM'
              );

--changeSet oper-237-003:14 stripComments:false
-- calculate completed departures
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'CALCULATE-DEPARTURE-BREAKDOWN-XFORM',
   'Calculate Departure Breadkown',
   'opr_fleet_summary_pkg.calculate_departure_breakdown(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   6
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'CALCULATE-DEPARTURE-BREAKDOWN-XFORM'
              );

--changeSet oper-237-003:15 stripComments:false
-- calculate incident departures
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'CALCULATE-INCIDENT-BREAKDOWN-XFORM',
   'Calculate Incident Breadkown',
   'opr_fleet_summary_pkg.calculate_incident_breakdown(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   7
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'CALCULATE-INCIDENT-BREAKDOWN-XFORM'
              );

--changeSet oper-237-003:16 stripComments:false
-- summarize incidents
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'SUMMARIZE-INCIDENTS-XFORM',
   'Summarize Incidents',
   'opr_fleet_summary_pkg.summarize_incidents(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   8
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'SUMMARIZE-INCIDENTS-XFORM'
              );

--changeSet oper-237-003:17 stripComments:false
-- pirepmarep transformation               
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'PIREPMAREP-XFORM',
   'Transformation : Pilot, Mechanic and Cabin faults report',
   'opr_rbl_fault_pkg.opr_rbl_fault_xform_proc(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   10
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'PIREPMAREP-XFORM'
              );

--changeSet oper-237-003:18 stripComments:false
-- summarize incidents
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order
)
SELECT
   'SUMMARIZE-TAIR-INCIDENTS-XFORM',
   'Summarize TAIR Incidents',
   'opr_fleet_summary_pkg.summarize_tair_incidents(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TRUNC(SYSDATE),
   1,
   1,
   9
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   opr_report
                WHERE
                   report_code = 'SUMMARIZE-TAIR-INCIDENTS-XFORM'
              );