--liquibase formatted sql


--changeSet opr_report:1 stripComments:false
-- delete the OLD SP1 OPR report that is no longer in used in SP2
DELETE
FROM 
   opr_report
WHERE   
   report_code IN ('FLIGHTS-XTRACT',
                   'FLIGHT-DISRUPTIONS-XTRACT',
                   'FAULTS-XTRACT',
                   'FAULT-INCIDENTS-XTRACT',
                   'CANCELLED-DEPARTURES-XFORM',
                   'MAINTENANCE-XTRACT',
                   'COMPLETED-DEPARTURES-XFORM',
                   'SUMMARIZE-INCIDENTS-XFORM',
                   'SUMMARIZE-TAIR-INCIDENTS-XFORM'
                  )
   AND
   cutoff_date IS NULL;   

--changeSet opr_report:2 stripComments:false
--  
-- usage extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'USAGE-XTRACT'        report_code,
            'Extraction: Usage'   report_name,
            'opr_fleet_summary_pkg.extract_usage(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            1                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );     

--changeSet opr_report:3 stripComments:false
-- maintenance extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'WORK-PACKAGE-XTRACT'      report_code,
            'Extraction:Work Package'  report_name,
            'opr_fleet_summary_pkg.extract_work_package(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            2                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                                

--changeSet opr_report:4 stripComments:false
-- fault extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'FAULT-XTRACT'      report_code,
            'Extraction: Fault'  report_name,
            'opr_fleet_summary_pkg.extract_fault(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            3                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                                

--changeSet opr_report:5 stripComments:false
-- fault incident extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'FAULT-INCIDENT-XTRACT'      report_code,
            'Extraction: Fault Incident'  report_name,
            'opr_fleet_summary_pkg.extract_fault_incident(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            4                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                                   

--changeSet opr_report:6 stripComments:false
-- flight extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'FLIGHT-XTRACT'      report_code,
            'Extraction: Flight'  report_name,
            'opr_fleet_summary_pkg.extract_flight(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            5                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                              

--changeSet opr_report:7 stripComments:false
-- flight disruption extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'FLIGHT-DISRUPTION-XTRACT'      report_code,
            'Extraction: Flight Disruption'  report_name,
            'opr_fleet_summary_pkg.extract_flight_disruption(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            6                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                        

--changeSet opr_report:8 stripComments:false
-- pirepmarep extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'PIREPMAREP-XTRACT'      report_code,
            'Extraction: Pilot, Mechanic and Cabin faults report'  report_name,
            'opr_rbl_pirepmarep_pkg.opr_rbl_pirepmarep_xtract(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            7                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                        

--changeSet opr_report:9 stripComments:false
-- component removal extraction
MERGE INTO opr_report trgt
USING (
         SELECT
            'COMPREMOVAL-XTRACT'      report_code,
            'Extraction: Component Reliability report'  report_name,
            'opr_rbl_comp_rmvl_pkg.opr_rbl_comp_rmvl_xtract(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            8                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                                     

--changeSet opr_report:10 stripComments:false
-- engine operation summary     
MERGE INTO opr_report trgt
USING (
         SELECT
            'ENGINE-XTRACT'      report_code,
            'Extraction: Engine Operation Summary'  report_name,
            'opr_rbl_eng_pkg.opr_rbl_eng_xtract(:aidt_start_date, :aidt_end_date)' program_name,
            'XTRACT' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-01','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            9                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                                 

--changeSet opr_report:11 stripComments:false
-- create calendar month    
MERGE INTO opr_report trgt
USING (
         SELECT
            'CREATE-CALENDAR-XFORM'      report_code,
            'Transformation: Create Calendar Month'  report_name,
            'opr_report_period_pkg.create_calendar_month(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            1                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );       

--changeSet opr_report:12 stripComments:false
-- summarize usage    
MERGE INTO opr_report trgt
USING (
         SELECT
            'USAGE-XFORM'      report_code,
            'Transformation: Summarize Usage'  report_name,
            'opr_fleet_summary_pkg.summarize_usage(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            5                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                

--changeSet opr_report:13 stripComments:false
-- calculate dos part 1             
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-DOS-P1-XFORM'      report_code,
            'Transformation: Calculate DOS Part 1'  report_name,
            'opr_fleet_summary_pkg.calculate_dos_pt1(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            6                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                     

--changeSet opr_report:14 stripComments:false
-- calculate dos part 2            
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-DOS-P2-XFORM'      report_code,
            'Transformation: Calculate DOS Part 2'  report_name,
            'opr_fleet_summary_pkg.calculate_dos_pt2(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            7                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                     

--changeSet opr_report:15 stripComments:false
-- calculate dos part 3        
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-DOS-P3-XFORM'      report_code,
            'Transformation: Calculate DOS Part 3'  report_name,
            'opr_fleet_summary_pkg.calculate_dos_pt3(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            8                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );       

--changeSet opr_report:16 stripComments:false
-- calculate completed departures             
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-COMPLETED-DEPARTURES-XFORM'      report_code,
            'Transformation: Calculate Completed Departures'  report_name,
            'opr_fleet_summary_pkg.calculate_completed_departures(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            9                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                     

--changeSet opr_report:17 stripComments:false
-- calculate completed departures             
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-CANCELLED-DEPARTURES-XFORM'      report_code,
            'Transformation: Calculate Cancelled Departures'  report_name,
            'opr_fleet_summary_pkg.calculate_cancelled_departures(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            10                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );      

--changeSet opr_report:18 stripComments:false
-- calculate MEL departures
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-MEL-DEPARTURES-XFORM'      report_code,
            'Transfomation: Calculate MEL Departures'  report_name,
            'opr_fleet_summary_pkg.calculate_mel_departures(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            11                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     ); 

--changeSet opr_report:19 stripComments:false
-- calculate departure breakdown
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-DEPARTURE-BREAKDOWN-XFORM'      report_code,
            'Transformation: Calculate Departure Breadkown'  report_name,
            'opr_fleet_summary_pkg.calculate_departure_breakdown(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            12                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );               

--changeSet opr_report:20 stripComments:false
-- calculate incident breakdown
MERGE INTO opr_report trgt
USING (
         SELECT
            'CALCULATE-INCIDENT-BREAKDOWN-XFORM'      report_code,
            'Transformation: Calculate Incident Breadkown'  report_name,
            'opr_fleet_summary_pkg.calculate_incident_breakdown(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            13                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );     

--changeSet opr_report:21 stripComments:false
-- pirepmarep transformation
MERGE INTO opr_report trgt
USING (
         SELECT
            'PIREPMAREP-XFORM'      report_code,
            'Transformation : Pilot, Mechanic and Cabin faults report'  report_name,
            'opr_rbl_pirepmarep_pkg.opr_rbl_pirepmarep_xform(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            14                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                   

--changeSet opr_report:22 stripComments:false
-- component transformation 
MERGE INTO opr_report trgt
USING (
         SELECT
            'COMPREMOVAL-XFORM'      report_code,
            'Transformation : Component Reliability report'  report_name,
            'opr_rbl_comp_rmvl_pkg.opr_rbl_comp_rmvl_xform(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            15                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );                    

--changeSet opr_report:23 stripComments:false
-- engine operation transformation\
MERGE INTO opr_report trgt
USING (
         SELECT
            'ENGINE-XFORM'      report_code,
            'Transformation : Engine Operation Summary'  report_name,
            'opr_rbl_eng_pkg.opr_rbl_eng_xform(:aidt_start_date, :aidt_end_date)' program_name,
            'XFORM' program_type_code,
            TO_DATE('2014-06-01','YYYY-MM-DD') start_date,
            TO_DATE('2014-06-30','YYYY-MM-DD') end_date,
            1                                  exec_parallel_flag,
            1                                  active_flag,
            16                                  execution_order,
            TO_DATE('2014-06-01','YYYY-MM-DD') cutoff_date
         FROM 
            dual
      ) src
ON (
     src.report_code = trgt.report_code
   )      
WHEN MATCHED THEN
   UPDATE
   SET
      trgt.report_name        = src.report_name,
      trgt.program_name       = src.program_name,
      trgt.program_type_code  = src.program_type_code,
      trgt.exec_parallel_flag = src.exec_parallel_flag,
      trgt.execution_order    = src.execution_order
WHEN NOT MATCHED THEN
  INSERT
     (
        trgt.report_code,
        trgt.report_name,
        trgt.program_name,
        trgt.program_type_code,
        trgt.start_date,
        trgt.end_date,
        trgt.exec_parallel_flag,
        trgt.active_flag,
        trgt.execution_order,
        trgt.cutoff_date
      )    
  VALUES 
     (
        src.report_code,
        src.report_name,
        src.program_name,
        src.program_type_code,
        src.start_date,
        src.end_date,
        src.exec_parallel_flag,
        src.active_flag,
        src.execution_order,
        src.cutoff_date
     );               