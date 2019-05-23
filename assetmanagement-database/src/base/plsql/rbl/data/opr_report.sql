--liquibase formatted sql


--changeSet opr_report:1 stripComments:false
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
  execution_order,
  cutoff_date
)
SELECT
   'USAGE-XTRACT',
   'Extraction: Usage',
   'opr_fleet_summary_pkg.extract_usage(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   1,
   NULL
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'USAGE-XTRACT'
              );

--changeSet opr_report:2 stripComments:false
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
  execution_order,
  cutoff_date
)
SELECT
   'WORK-PACKAGE-XTRACT',
   'Extraction:Work Package',
   'opr_fleet_summary_pkg.extract_work_package(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   1,
   1,
   2,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'WORK-PACKAGE-XTRACT'
              );

--changeSet opr_report:3 stripComments:false
-- fault extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'FAULT-XTRACT',
   'Extraction: Fault',
   'opr_fleet_summary_pkg.extract_fault(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   1,
   1,
   3,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'FAULT-XTRACT'
              );

--changeSet opr_report:4 stripComments:false
-- fault incident extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'FAULT-INCIDENT-XTRACT',
   'Extraction: Fault Incident',
   'opr_fleet_summary_pkg.extract_fault_incident(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   1,
   1,
   4,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'FAULT-INCIDENT-XTRACT'
              );

--changeSet opr_report:5 stripComments:false
-- flight extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'FLIGHT-XTRACT',
   'Extraction: Flight',
   'opr_fleet_summary_pkg.extract_flight(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   1,
   1,
   5,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'FLIGHT-XTRACT'
              );

--changeSet opr_report:6 stripComments:false
-- flight disruption extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'FLIGHT-DISRUPTION-XTRACT',
   'Extraction: Flight Disruption',
   'opr_fleet_summary_pkg.extract_flight_disruption(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   1,
   1,
   6,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'FLIGHT-DISRUPTION-XTRACT'
              );

--changeSet opr_report:7 stripComments:false
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
  execution_order,
  cutoff_date
)
SELECT
   'PIREPMAREP-XTRACT',
   'Extraction: Pilot, Mechanic and Cabin faults report',
   'opr_rbl_pirepmarep_pkg.opr_rbl_pirepmarep_xtract(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   7,
   NULL
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'PIREPMAREP-XTRACT'
              );              

--changeSet opr_report:8 stripComments:false
-- component removal extraction
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'COMPREMOVAL-XTRACT',
   'Extraction: Component Reliability report',
   'opr_rbl_comp_rmvl_pkg.opr_rbl_comp_rmvl_xtract(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   1,
   1,
   8,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'COMPREMOVAL-XTRACT'
              );              

--changeSet opr_report:9 stripComments:false
-- engine operation summary
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'ENGINE-XTRACT',
   'Extraction: Engine Operation Summary',
   'opr_rbl_eng_pkg.opr_rbl_eng_xtract(:aidt_start_date, :aidt_end_date)',
   'XTRACT',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   1,
   1,
   9,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'ENGINE-XTRACT'
              );                                                                           

--changeSet opr_report:10 stripComments:false
-- create calendar month
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'CREATE-CALENDAR-XFORM',
   'Transformation: Create Calendar Month',
   'opr_report_period_pkg.create_calendar_month(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   1,
   NULL
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

--changeSet opr_report:11 stripComments:false
-- summarize usage
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'USAGE-XFORM',
   'Transformation: Summarize Usage',
   'opr_fleet_summary_pkg.summarize_usage(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   5,
   NULL
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

--changeSet opr_report:12 stripComments:false
-- calculate dos part 1
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-DOS-P1-XFORM',
   'Transformation: Calculate DOS Part 1',
   'opr_fleet_summary_pkg.calculate_dos_pt1(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-30','YYYY-MM-DD'),
   1,
   1,
   6,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'CALCULATE-DOS-P1-XFORM'
              );

--changeSet opr_report:13 stripComments:false
-- calculate dos part 2
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-DOS-P2-XFORM',
   'Transformation: Calculate DOS Part 2',
   'opr_fleet_summary_pkg.calculate_dos_pt2(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-30','YYYY-MM-DD'),
   1,
   1,
   7,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'CALCULATE-DOS-P2-XFORM'
              );

--changeSet opr_report:14 stripComments:false
-- calculate dos part 3
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-DOS-P3-XFORM',
   'Transformation: Calculate DOS Part 3',
   'opr_fleet_summary_pkg.calculate_dos_pt3(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-30','YYYY-MM-DD'),
   1,
   1,
   8,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'CALCULATE-DOS-P3-XFORM'
              );

--changeSet opr_report:15 stripComments:false
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
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-COMPLETED-DEPARTURES-XFORM',
   'Transformation: Calculate Completed Departures',
   'opr_fleet_summary_pkg.calculate_completed_departures(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-30','YYYY-MM-DD'),
   1,
   1,
   9,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'CALCULATE-COMPLETED-DEPARTURES-XFORM'
              );

--changeSet opr_report:16 stripComments:false
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
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-CANCELLED-DEPARTURES-XFORM',
   'Transformation: Calculate Cancelled Departures',
   'opr_fleet_summary_pkg.calculate_cancelled_departures(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-30','YYYY-MM-DD'),
   1,
   1,
   10,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'CALCULATE-CANCELLED-DEPARTURES-XFORM'
              );

--changeSet opr_report:17 stripComments:false
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
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-MEL-DEPARTURES-XFORM',
   'Transfomation: Calculate MEL Departures',
   'opr_fleet_summary_pkg.calculate_mel_departures(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   11,
   NULL
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

--changeSet opr_report:18 stripComments:false
-- calculate departure breakdown
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-DEPARTURE-BREAKDOWN-XFORM',
   'Transformation: Calculate Departure Breadkown',
   'opr_fleet_summary_pkg.calculate_departure_breakdown(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   12,
   NULL -- set to NULL to address the data variance against SP1
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

--changeSet opr_report:19 stripComments:false
-- calculate incident breakdown
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'CALCULATE-INCIDENT-BREAKDOWN-XFORM',
   'Transformation: Calculate Incident Breadkown',
   'opr_fleet_summary_pkg.calculate_incident_breakdown(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   13,
   NULL
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

--changeSet opr_report:20 stripComments:false
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
  execution_order,
  cutoff_date
)
SELECT
   'PIREPMAREP-XFORM',
   'Transformation : Pilot, Mechanic and Cabin faults report',
   'opr_rbl_pirepmarep_pkg.opr_rbl_pirepmarep_xform(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2000-01-01','YYYY-MM-DD'),
   TO_DATE('2014-03-09','YYYY-MM-DD'),
   1,
   1,
   14,
   NULL
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

--changeSet opr_report:21 stripComments:false
-- component transformation               
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'COMPREMOVAL-XFORM',
   'Transformation : Component Reliability report',
   'opr_rbl_comp_rmvl_pkg.opr_rbl_comp_rmvl_xform(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-30','YYYY-MM-DD'),
   1,
   1,
   15,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'COMPREMOVAL-XFORM'
              );              

--changeSet opr_report:22 stripComments:false
-- engine operation transformation
INSERT INTO opr_report (
  report_code,
  report_name,
  program_name,
  program_type_code,
  start_date,
  end_date,
  exec_parallel_flag,
  active_flag,
  execution_order,
  cutoff_date
)
SELECT
   'ENGINE-XFORM',
   'Transformation : Engine Operation Summary',
   'opr_rbl_eng_pkg.opr_rbl_eng_xform(:aidt_start_date, :aidt_end_date)',
   'XFORM',
   TO_DATE('2014-06-01','YYYY-MM-DD'),
   TO_DATE('2014-06-30','YYYY-MM-DD'),
   1,
   1,
   16,
   TO_DATE('2014-06-01','YYYY-MM-DD')
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report
                WHERE
                   report_code = 'ENGINE-XFORM'
              );                            