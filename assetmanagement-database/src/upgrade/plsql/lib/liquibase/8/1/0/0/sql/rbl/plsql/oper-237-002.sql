--liquibase formatted sql


--changeSet oper-237-002:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE body opr_fleet_summary_pkg IS

   ----------------------------------------------------------------------------
   -- Object Name
   --
   --      opr_fleet_summary
   ---
   -- Description
   --
   --     ETL for reliability reporting.
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_usage usage
      USING
      (

         SELECT
            serial_number,
            usage_date,
            data_type_code,
            usage_type_code,
            sum(tsn_delta_qty)     as usage_value
         FROM
            acor_aircraft_usage_v1 aircraft_usage
         WHERE
            aircraft_usage.usage_date BETWEEN aid_start_date AND aid_end_date AND
            aircraft_usage.usage_type_code in ('MXFLIGHT', 'MXACCRUAL')
         GROUP BY
            serial_number,
            usage_date,
            data_type_code,
            usage_type_code
      ) mx_usage
      ON (
            mx_usage.serial_number   = usage.serial_number  AND
            mx_usage.usage_date      = usage.usage_date     AND
            mx_usage.data_type_code  = usage.data_type_code AND
            mx_usage.usage_type_code = usage.usage_type_code
          )
      WHEN MATCHED
      THEN
         UPDATE SET
             usage.usage_value       = mx_usage.usage_value
      WHEN NOT MATCHED
      THEN
      --
         INSERT
         (
            usage.serial_number,
            usage.usage_date,
            usage.data_type_code,
            usage.usage_type_code,
            usage.usage_value
         )
         VALUES
         (
            mx_usage.serial_number,
            mx_usage.usage_date,
            mx_usage.data_type_code,
            mx_usage.usage_type_code,
            mx_usage.usage_value
         );
      --
      l_row_count := SQL%ROWCOUNT;
      --
      RETURN (l_row_count);

   END extract_usage;

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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_fault fault
      USING
      (
         SELECT
            fault.serial_number            AS serial_number,
            fault.chapter_code             AS chapter_code,
            fault.found_on_date            AS found_on_date,
            fault.fault_id                 AS fault_id,
            fault.fault_description        AS fault_description,
            fault.fault_severity           AS fault_severity,
            fault.deferral_class           AS deferral_class
         FROM
            acor_aircraft_fault_v1 fault
         WHERE
            fault.found_on_date BETWEEN aid_start_date AND aid_end_date
      ) mx_fault
      ON (
             mx_fault.serial_number = fault.serial_number AND
             mx_fault.chapter_code  = fault.chapter_code  AND
             mx_fault.found_on_date = fault.found_on_date AND
             mx_fault.fault_id      = fault.fault_id
         )
      WHEN MATCHED THEN
         UPDATE SET
            fault.fault_description         = mx_fault.fault_description,
            fault.fault_severity            = mx_fault.fault_severity,
            fault.deferral_class            = mx_fault.deferral_class

      WHEN NOT MATCHED THEN
         INSERT
         (
             fault.serial_number,
             fault.chapter_code,
             fault.found_on_date,
             fault.fault_id,
             fault.fault_description,
             fault.fault_severity,
             fault.deferral_class
         )
         VALUES
         (
             mx_fault.serial_number,
             mx_fault.chapter_code,
             mx_fault.found_on_date,
             mx_fault.fault_id,
             mx_fault.fault_description,
             mx_fault.fault_severity,
             mx_fault.deferral_class
         );

      l_row_count := SQL%ROWCOUNT;

      RETURN (l_row_count);

   END extract_faults;

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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_fault_incident fault_incident
      USING
      (
            SELECT
               fault.serial_number,
               fault.chapter_code,
               fault.found_on_date,
               fault.fault_id,
               fault_incident.result_event_code as incident_code
            FROM
               acor_fault_result_v1 fault_incident
               INNER JOIN opr_fault fault ON
                  fault.fault_id = fault_incident.fault_id
            WHERE
               fault.found_on_date BETWEEN aid_start_date AND aid_end_date
      ) mx_fault_incident
      ON (
            mx_fault_incident.serial_number = fault_incident.serial_number  AND
            mx_fault_incident.chapter_code  = fault_incident.chapter_code   AND
            mx_fault_incident.found_on_date = fault_incident.found_on_date  AND
            mx_fault_incident.fault_id      = fault_incident.fault_id       AND
            mx_fault_incident.incident_code = fault_incident.incident_code
         )
      WHEN NOT MATCHED THEN
         INSERT
         (
            fault_incident.serial_number,
            fault_incident.chapter_code,
            fault_incident.found_on_date,
            fault_incident.incident_code,
            fault_incident.fault_id
         )
         VALUES
         (
            mx_fault_incident.serial_number,
            mx_fault_incident.chapter_code,
            mx_fault_incident.found_on_date,
            mx_fault_incident.incident_code,
            mx_fault_incident.fault_id
         );

      --
      l_row_count := SQL%ROWCOUNT;

      RETURN (l_row_count);

   END extract_fault_incidents;
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_flight flight
      USING
      (
          SELECT
            flight_number           AS flight_number,
            aircraft_serial_number  AS serial_number,
            depart_airport          AS departure_airport_code,
            schedule_departure_date AS scheduled_departure_date,
            actual_departure_date   AS actual_departure_date,
            flight_status_code      AS flight_status_code,
            schedule_arrival_date   AS scheduled_arrival_date,
            actual_arrival_date     AS actual_arrival_date,
            aircraft_id             AS aircraft_id,
            leg_id                  AS flight_id
         FROM
            acor_flights_v1 flight
         WHERE
            flight.schedule_departure_date BETWEEN aid_start_date AND aid_end_date
      ) mx_flight
      ON (
             flight.flight_number            =  mx_flight.flight_number           AND
             flight.scheduled_departure_date = mx_flight.scheduled_departure_date AND
             flight.departure_airport_code   = mx_flight.departure_airport_code   AND
             flight.flight_id                = mx_flight.flight_id
         )
      WHEN NOT MATCHED THEN
          INSERT
          (
              flight.flight_number,
              flight.departure_airport_code,
              flight.scheduled_departure_date,
              flight.actual_departure_date,
              flight.serial_number,
              flight.flight_status_code,
              flight.aircraft_id,
              flight.flight_id
          )
          VALUES
          (
              mx_flight.flight_number,
              mx_flight.departure_airport_code,
              mx_flight.scheduled_departure_date,
              mx_flight.actual_departure_date,
              mx_flight.serial_number,
              mx_flight.flight_status_code,
              mx_flight.aircraft_id,
              mx_flight.flight_id
          )
        WHEN MATCHED THEN
           UPDATE
              SET
                 flight.serial_number          = mx_flight.serial_number,
                 flight.flight_status_code     = mx_flight.flight_status_code,
                 flight.aircraft_id            = mx_flight.aircraft_id,
                 flight.scheduled_arrival_date = mx_flight.scheduled_arrival_date,
                 flight.actual_arrival_date    = mx_flight.actual_arrival_date;

      l_row_count := SQL%ROWCOUNT;

      RETURN (l_row_count);

   END extract_flights;


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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_flight_disruption flight_disruption
      USING
      (
          SELECT
             flight_number,
             departure_airport_code,
             scheduled_departure_date,
             flight_id as flight_id,
             disruption_type_code,
             flight_disruption.delay_code,
             maint_delay_time_qt as maintenance_delay_time,
             flight_disruption.sched_id,
             fault.chapter_code,
             fault.fault_id,
             fault.found_on_date,
             fault.serial_number
          FROM
             acor_fl_disruptions_v1 flight_disruption
             INNER JOIN opr_flight flight ON
                flight.flight_id = flight_disruption.leg_id
             INNER JOIN acor_delay_v1 delay ON
                delay.delay_code = flight_disruption.delay_code
             INNER JOIN acor_corrective_task_v1 corrective_task ON
                corrective_task.corrective_task_id = flight_disruption.sched_id
             INNER JOIN acor_aircraft_fault_v1 fault ON
                fault.fault_id = corrective_task.fault_id
          WHERE
             delay.tech_delay_bool = 1 AND -- delayed because of a technical issue
             flight.scheduled_departure_date BETWEEN aid_start_date AND aid_end_date
      ) mx_flight_disruption
      ON
      (
         mx_flight_disruption.flight_number            = flight_disruption.flight_number            AND
         mx_flight_disruption.departure_airport_code   = flight_disruption.departure_airport_code   AND
         mx_flight_disruption.scheduled_departure_date = flight_disruption.scheduled_departure_date AND
         mx_flight_disruption.flight_id                = flight_disruption.flight_id                AND
         mx_flight_disruption.disruption_type_code     = flight_disruption.disruption_type_code
      )
      WHEN MATCHED THEN
         UPDATE SET
            flight_disruption.delay_code             = mx_flight_disruption.delay_code,
            flight_disruption.maintenance_delay_time = mx_flight_disruption.maintenance_delay_time,
            flight_disruption.chapter_code           = mx_flight_disruption.chapter_code,
            flight_disruption.serial_number          = mx_flight_disruption.serial_number,
            flight_disruption.found_on_date          = mx_flight_disruption.found_on_date
            
      WHEN NOT MATCHED THEN
         INSERT
         (
            flight_disruption.flight_number,
            flight_disruption.departure_airport_code,
            flight_disruption.scheduled_departure_date,
            flight_disruption.flight_id,
            flight_disruption.disruption_type_code,
            flight_disruption.delay_code,
            flight_disruption.maintenance_delay_time,
            flight_disruption.serial_number,
            flight_disruption.chapter_code,
            flight_disruption.found_on_date
         )
         VALUES
         (
            mx_flight_disruption.flight_number,
            mx_flight_disruption.departure_airport_code,
            mx_flight_disruption.scheduled_departure_date,
            mx_flight_disruption.flight_id,
            mx_flight_disruption.disruption_type_code,
            mx_flight_disruption.delay_code,
            mx_flight_disruption.maintenance_delay_time,
            mx_flight_disruption.serial_number,
            mx_flight_disruption.chapter_code,
            mx_flight_disruption.found_on_date
         );

      l_row_count := SQL%ROWCOUNT;

      RETURN (l_row_count);

   END extract_flight_disruptions;

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

   ) RETURN INTEGER

   IS
      l_row_count INTEGER;
      --
   BEGIN


       MERGE INTO opr_maintenance maintenance
       USING
       (
          SELECT
            serial_number,
            schedule_start_date,
            schedule_end_date,
            actual_start_date,
            actual_end_date,
            work_package_name,
            barcode,
            (work_package.actual_end_date - work_package.actual_start_date) as duration
          FROM
             acor_workpackages_v1 work_package
          WHERE
             work_package.schedule_start_date BETWEEN aid_start_date AND aid_end_date 
       ) mx_maintenance
       ON (
              mx_maintenance.serial_number        = maintenance.serial_number        AND
              mx_maintenance.schedule_start_date  = maintenance.scheduled_start_date AND
              mx_maintenance.barcode              = maintenance.work_package_barcode
          )
       WHEN NOT MATCHED
       THEN
       --
          INSERT
          (
              maintenance.serial_number,
              maintenance.scheduled_start_date,
              maintenance.scheduled_end_date,
              maintenance.actual_start_date,
              maintenance.actual_end_date,
              maintenance.work_package_barcode,
              maintenance.duration
          )
          VALUES
          (
              mx_maintenance.serial_number,
              mx_maintenance.schedule_start_date,
              mx_maintenance.schedule_end_date,
              mx_maintenance.actual_start_date,
              mx_maintenance.actual_end_date,
              mx_maintenance.barcode,
              mx_maintenance.duration
          );


       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

    END extract_maintenance;
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
       MERGE INTO opr_monthly_reliability monthly_reliability
       USING
       (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number,
             fleet_movement.operator_code,
             fleet_movement.source_system     AS source_system,
             sum(usage.cycles)                AS cycles,
             sum(usage.flight_hours)          AS flight_hours,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id,
             fleet_movement.fleet_type,
             0 as days_out_of_service
          FROM
             (
                SELECT
                   serial_number,
                   usage_date,
                   flight_hours,
                   cycles
                FROM
                   opr_usage usage
                PIVOT (
                         SUM(usage_value) FOR data_type_code IN
                                              (
                                                'CYCLES' AS cycles,
                                                'HOURS'  AS flight_hours
                                              )
                      )
             ) usage
             INNER JOIN opr_calendar_month report_period ON
               usage.usage_date BETWEEN report_period.start_date AND report_period.end_date
             INNER JOIN opr_fleet_movement fleet_movement ON
                    usage.serial_number = fleet_movement.serial_number AND
                    (
                       usage.usage_date >= fleet_movement.phase_in_date   AND
                       usage.usage_date <= coalesce(fleet_movement.phase_out_date, usage.usage_date)
                    )
          WHERE
             usage.usage_date BETWEEN aid_start_date and aid_end_date
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number,
             fleet_movement.fleet_type,
             fleet_movement.source_system,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id
       ) mx_usage
       ON (
              mx_usage.year_code                  = monthly_reliability.year_code                  AND
              mx_usage.month_code                 = monthly_reliability.month_code                 AND
              mx_usage.operator_registration_code = monthly_reliability.operator_registration_code AND
              mx_usage.serial_number              = monthly_reliability.serial_number              AND
              mx_usage.operator_code              = monthly_reliability.operator_code
          )
       WHEN MATCHED
       THEN
       --
          UPDATE SET
             monthly_reliability.cycles        = mx_usage.cycles,
             monthly_reliability.flight_hours  = mx_usage.flight_hours,
             monthly_reliability.source_system = mx_usage.source_system,
             monthly_reliability.aircraft_id   = mx_usage.aircraft_id,
             monthly_reliability.operator_id   = mx_usage.operator_id,
             monthly_reliability.fleet_type    = mx_usage.fleet_type
          WHERE
             monthly_reliability.source_system = 'MAINTENIX'
       --
       WHEN NOT MATCHED
       THEN
       --
          INSERT
          (
             monthly_reliability.year_code,
             monthly_reliability.month_code,
             monthly_reliability.operator_registration_code,
             monthly_reliability.serial_number,
             monthly_reliability.operator_code,
             monthly_reliability.cycles,
             monthly_reliability.flight_hours,
             monthly_reliability.source_system,
             monthly_reliability.aircraft_id,
             monthly_reliability.operator_id,
             monthly_reliability.fleet_type
          )
          VALUES
          (
             mx_usage.year_code,
             mx_usage.month_code,
             mx_usage.operator_registration_code,
             mx_usage.serial_number,
             mx_usage.operator_code,
             mx_usage.cycles,
             mx_usage.flight_hours,
             mx_usage.source_system,
             mx_usage.aircraft_id,
             mx_usage.operator_id,
             mx_usage.fleet_type
          );

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END summarize_usage;
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_reliability monthly_reliability
      USING
      (
         SELECT
            opr_monthly_delay.year_code,
            opr_monthly_delay.month_code,
            operator_registration_code,
            serial_number,
            operator_code,
            SUM(delayed_departures)  AS delayed_departures,
            SUM(case when
                        NVL(opr_delay_category.high_range,16) > 15 then delayed_departures
                        ELSE 0
                END
               )  AS delayed_departures_gt_15,
            SUM(delay_time) as delay_time
         FROM
           opr_monthly_delay
           INNER JOIN opr_delay_category ON
              opr_delay_category.delay_category_code = opr_monthly_delay.delay_category_code
           INNER JOIN opr_calendar_month calendar_month ON
             calendar_month.year_code  = opr_monthly_delay.year_code   AND
             calendar_month.month_code = opr_monthly_delay.month_code
         WHERE
            calendar_month.start_date between aid_start_date AND aid_end_date AND
            calendar_month.end_date   between aid_start_date AND aid_end_date
         GROUP BY
            opr_monthly_delay.year_code,
            opr_monthly_delay.month_code,
            operator_registration_code,
            operator_code,
            serial_number
      ) mx_delay
      ON (
             mx_delay.year_code                  = monthly_reliability.year_code                  AND
             mx_delay.month_code                 = monthly_reliability.month_code                 AND
             mx_delay.operator_registration_code = monthly_reliability.operator_registration_code AND
             mx_delay.serial_number              = monthly_reliability.serial_number              AND
             mx_delay.operator_code              = monthly_reliability.operator_code
         )
      WHEN MATCHED THEN
         UPDATE SET
            monthly_reliability.delayed_departures       = mx_delay.delayed_departures,
            monthly_reliability.delayed_departures_gt_15 = mx_delay.delayed_departures_gt_15,
            monthly_reliability.delay_time               = mx_delay.delay_time
          WHERE
             monthly_reliability.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END summarize_departures;

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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_reliability monthly_reliability
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number as serial_number,
             fleet_movement.operator_code,
             sum(cancelled_departures) as cancelled_departures
          FROM
             (
                SELECT
                   flight_number,
                   departure_airport_code,
                   scheduled_departure_date,
                   cancelled_departures
                FROM
                   opr_flight_disruption flight_disruption
                PIVOT (
                         COUNT(disruption_type_code) FOR disruption_type_code IN
                                              (
                                                'CNX'  AS cancelled_departures
                                              )
                      )
             ) cancels
             INNER JOIN opr_calendar_month report_period ON
                cancels.scheduled_departure_date BETWEEN report_period.start_date AND report_period.end_date
             INNER JOIN opr_flight flight ON
                flight.flight_number            = cancels.flight_number AND
                flight.departure_airport_code   = cancels.departure_airport_code AND
                flight.scheduled_departure_date = cancels.scheduled_departure_date
             INNER JOIN opr_fleet_movement fleet_movement ON
                flight.serial_number = fleet_movement.serial_number AND
                (
                    cancels.scheduled_departure_date >= fleet_movement.phase_in_date   AND
                    cancels.scheduled_departure_date <= coalesce(fleet_movement.phase_out_date, cancels.scheduled_departure_date)
                )
          WHERE
            flight.scheduled_departure_date BETWEEN aid_start_date AND aid_end_date AND
            flight.flight_status_code in (
                                             'MXCMPLT',
                                             'MXCANCEL'
                                         )
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number
       ) mx_cancels
       ON (
              mx_cancels.year_code = monthly_reliability.year_code                                   AND
              mx_cancels.month_code = monthly_reliability.month_code                                 AND
              mx_cancels.operator_registration_code = monthly_reliability.operator_registration_code AND
              mx_cancels.serial_number = monthly_reliability.serial_number                           AND
              mx_cancels.operator_code = monthly_reliability.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_reliability.cancelled_departures = mx_cancels.cancelled_departures
          WHERE
             monthly_reliability.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_cancelled_departures;
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_reliability monthly_reliability
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number as serial_number,
             fleet_movement.operator_code,
             count(*)                     as mel_delayed_departures
          FROM
             opr_fault fault
             INNER JOIN opr_calendar_month report_period ON
               fault.found_on_date BETWEEN report_period.start_date AND report_period.end_date
             INNER JOIN opr_fleet_movement fleet_movement ON
               fault.serial_number = fleet_movement.serial_number AND
               (
                    fault.found_on_date >= fleet_movement.phase_in_date   AND
                    fault.found_on_date <= coalesce(fleet_movement.phase_out_date, fault.found_on_date)
               )
          WHERE
              fault.found_on_date BETWEEN aid_start_date and aid_end_date AND
              fault.fault_severity = 'MEL'

          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number
       ) mx_fault
       ON (
              mx_fault.year_code = monthly_reliability.year_code   AND
              mx_fault.month_code = monthly_reliability.month_code AND
              mx_fault.operator_registration_code = monthly_reliability.operator_registration_code AND
              mx_fault.serial_number = monthly_reliability.serial_number AND
              mx_fault.operator_code = monthly_reliability.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_reliability.mel_delayed_departures = mx_fault.mel_delayed_departures
          WHERE
             monthly_reliability.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_mel_departures;
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_delay monthly_delay
      USING
      (
         SELECT
            report_period.year_code,
            report_period.month_code,
            fleet_movement.serial_number,
            fleet_movement.operator_registration_code,
            fleet_movement.operator_code,
            delay_category.delay_category_code,
            fleet_movement.fleet_type,
            flight_disruption.departure_airport_code,
            fault.chapter_code,
            fleet_movement.aircraft_id,
            fleet_movement.operator_id,
            COUNT(*) AS delayed_departures,
            sum(flight_disruption.maintenance_delay_time) as delay_time,
            'MAINTNEIX' as source_system
         FROM
            opr_flight_disruption flight_disruption
            INNER JOIN opr_fault fault ON
               fault.fault_id      = flight_disruption.fault_id      AND
               fault.serial_number = flight_disruption.serial_number AND
               fault.chapter_code  = flight_disruption.chapter_code  AND
               fault.found_on_date = flight_disruption.found_on_date
            INNER JOIN opr_delay_category delay_category ON
               flight_disruption.maintenance_delay_time between delay_category.low_range and nvl(delay_category.high_range, flight_disruption.maintenance_delay_time)
            INNER JOIN opr_calendar_month report_period ON
               flight_disruption.scheduled_departure_date BETWEEN report_period.start_date AND report_period.end_date
            INNER JOIN opr_fleet_movement fleet_movement ON
               fault.serial_number = fleet_movement.serial_number AND
               (
                  flight_disruption.scheduled_departure_date >= fleet_movement.phase_in_date   AND
                  flight_disruption.scheduled_departure_date <= coalesce(fleet_movement.phase_out_date, flight_disruption.scheduled_departure_date)
               )
         WHERE
            flight_disruption.scheduled_departure_date BETWEEN aid_start_date and aid_end_date AND
            disruption_type_code = 'DLY'

         GROUP BY
            report_period.year_code,
            report_period.month_code,
            fleet_movement.serial_number,
            fleet_movement.operator_registration_code,
            fleet_movement.operator_code,
            delay_category.delay_category_code,
            fleet_movement.fleet_type,
            flight_disruption.departure_airport_code,
            fault.chapter_code,
            fleet_movement.aircraft_id,
            fleet_movement.operator_id
      ) mx_delay
      ON (
              mx_delay.year_code                  = monthly_delay.year_code                  AND
              mx_delay.month_code                 = monthly_delay.month_code                 AND
              mx_delay.operator_registration_code = monthly_delay.operator_registration_code AND
              mx_delay.serial_number              = monthly_delay.serial_number              AND
              mx_delay.operator_code              = monthly_delay.operator_code              AND
              mx_delay.delay_category_code        = monthly_delay.delay_category_code
         )

      WHEN NOT MATCHED THEN
         INSERT
         (
            monthly_delay.year_code,
            monthly_delay.month_code,
            monthly_delay.operator_code,
            monthly_delay.serial_number,
            monthly_delay.operator_registration_code,
            monthly_delay.fleet_type,
            monthly_delay.departure_airport_code,
            monthly_delay.chapter_code,
            monthly_delay.delay_category_code,
            monthly_delay.delayed_departures,
            monthly_delay.delay_time,
            monthly_delay.source_system,
            monthly_delay.aircraft_id,
            monthly_delay.operator_id
         )
         VALUES
         (
            mx_delay.year_code,
            mx_delay.month_code,
            mx_delay.operator_code,
            mx_delay.serial_number,
            mx_delay.operator_registration_code,
            mx_delay.fleet_type,
            mx_delay.departure_airport_code,
            mx_delay.chapter_code,
            mx_delay.delay_category_code,
            mx_delay.delayed_departures,
            mx_delay.delay_time,
            mx_delay.source_system,
            mx_delay.aircraft_id,
            mx_delay.operator_id
         );
      l_row_count := SQL%ROWCOUNT;

      RETURN (l_row_count);

   END calculate_departure_breakdown;

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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_incident monthly_incident
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number,
             fleet_movement.operator_code,
             fleet_movement.source_system     AS source_system,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id,
             fleet_movement.fleet_type,
             fault_incident.incident_code,
             flight_disruption.departure_airport_code,
             count(*) as number_of_incidents
          FROM
             opr_fault_incident fault_incident 
             INNER JOIN
               opr_flight_disruption flight_disruption ON
                   flight_disruption.serial_number = fault_incident.serial_number AND
                   flight_disruption.chapter_code  = fault_incident.chapter_code  AND
                   flight_disruption.found_on_date = fault_incident.found_on_date AND
                   flight_disruption.fault_id      = fault_incident.fault_id
             INNER JOIN
                opr_calendar_month report_period ON
                   fault_incident.found_on_date BETWEEN
                      report_period.start_date AND report_period.end_date
             INNER JOIN
                opr_fleet_movement fleet_movement ON
                   fault_incident.serial_number = fleet_movement.serial_number AND
                   (
                       fault_incident.found_on_date >= fleet_movement.phase_in_date   AND
                       fault_incident.found_on_date <= coalesce(fleet_movement.phase_out_date, fault_incident.found_on_date)
                   )
          WHERE
             fault_incident.found_on_date BETWEEN aid_start_date and aid_end_date
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number,
             fault_incident.incident_code,
             flight_disruption.departure_airport_code,
             fleet_movement.fleet_type,
             fleet_movement.source_system,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id
        ) mx_incident
        ON (
              mx_incident.year_code                  = monthly_incident.year_code                  AND
              mx_incident.month_code                 = monthly_incident.month_code                 AND
              mx_incident.operator_registration_code = monthly_incident.operator_registration_code AND
              mx_incident.serial_number              = monthly_incident.serial_number              AND
              mx_incident.operator_code              = monthly_incident.operator_code              AND
              mx_incident.incident_code              = monthly_incident.incident_code
           )
        WHEN MATCHED THEN
           UPDATE SET
              monthly_incident.number_of_incidents   = mx_incident.number_of_incidents,
              monthly_incident.fleet_type            = mx_incident.fleet_type,
              monthly_incident.aircraft_id           = mx_incident.aircraft_id,
              monthly_incident.operator_id           = mx_incident.operator_id,
              monthly_incident.airport_code          = mx_incident.departure_airport_code

        WHEN NOT MATCHED THEN
           INSERT
           (
              monthly_incident.year_code,
              monthly_incident.month_code,
              monthly_incident.operator_code,
              monthly_incident.serial_number,
              monthly_incident.operator_registration_code,
              monthly_incident.fleet_type,
              monthly_incident.incident_code,
              monthly_incident.number_of_incidents,
              monthly_incident.aircraft_id,
              monthly_incident.operator_id
           )
           VALUES
           (
              mx_incident.year_code,
              mx_incident.month_code,
              mx_incident.operator_code,
              mx_incident.serial_number,
              mx_incident.operator_registration_code,
              mx_incident.fleet_type,
              mx_incident.incident_code,
              mx_incident.number_of_incidents,
              mx_incident.aircraft_id,
              mx_incident.operator_id
           );
       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_incident_breakdown;

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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_reliability monthly_reliability
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number,
             fleet_movement.operator_code,
             count(*) as completed_departures
          FROM
             opr_flight flight
             INNER JOIN
                opr_calendar_month report_period ON
                   flight.scheduled_departure_date BETWEEN
                      report_period.start_date AND report_period.end_date
             INNER JOIN
                opr_fleet_movement fleet_movement ON
                   flight.serial_number = fleet_movement.serial_number AND
                   (
                       flight.scheduled_departure_date >= fleet_movement.phase_in_date   AND
                       flight.scheduled_departure_date <= coalesce(fleet_movement.phase_out_date, flight.scheduled_departure_date)
                   )

          WHERE
             flight.scheduled_departure_date BETWEEN aid_start_date and aid_end_date AND
             flight.flight_status_code in ('MXCMPLT')
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number
       ) mx_complete
       ON (
              mx_complete.year_code                  = monthly_reliability.year_code                  AND
              mx_complete.month_code                 = monthly_reliability.month_code                 AND
              mx_complete.operator_registration_code = monthly_reliability.operator_registration_code AND
              mx_complete.serial_number              = monthly_reliability.serial_number              AND
              mx_complete.operator_code              = monthly_reliability.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_reliability.completed_departures = mx_complete.completed_departures
          WHERE
             monthly_reliability.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_completed_departures;

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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_reliability monthly_reliability
      USING
      (
         SELECT
            monthly_incident.year_code,
            monthly_incident.month_code,
            monthly_incident.operator_registration_code,
            monthly_incident.serial_number,
            monthly_incident.operator_code,
            monthly_incident.tair_incidents,
            monthly_incident.tinr_incidents
         FROM
            (
                SELECT incident.*, technical_incident.incident_type
                FROM
                   opr_monthly_incident incident
                   INNER JOIN
                      opr_calendar_month calendar_month ON
                         calendar_month.year_code  = incident.year_code AND
                         calendar_month.month_code = incident.month_code
                   INNER JOIN
                      opr_technical_incident technical_incident ON
                         technical_incident.incident_code = incident.incident_code
                WHERE
                   calendar_month.start_date between aid_start_date AND aid_end_date OR
                   calendar_month.end_date   BETWEEN aid_start_date AND aid_end_date
            )
            PIVOT (
                     SUM(number_of_incidents) FOR incident_type IN
                        (
                            'TAIR'   AS tair_incidents,
                            'TAIR'   AS tinr_incidents
                        )
                   ) monthly_incident
      ) mx_incident
      ON (
              mx_incident.year_code                  = monthly_reliability.year_code                  AND
              mx_incident.month_code                 = monthly_reliability.month_code                 AND
              mx_incident.operator_registration_code = monthly_reliability.operator_registration_code AND
              mx_incident.serial_number              = monthly_reliability.serial_number              AND
              mx_incident.operator_code              = monthly_reliability.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_reliability.tair_incidents    = mx_incident.tair_incidents,
             monthly_reliability.tinr_incidents    = mx_incident.tinr_incidents
          WHERE
             monthly_reliability.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END summarize_tair_incidents;
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_monthly_reliability monthly_reliability
      USING
      (
         SELECT
            monthly_incident.year_code,
            monthly_incident.month_code,
            monthly_incident.operator_registration_code,
            monthly_incident.serial_number,
            monthly_incident.operator_code,
            nvl(monthly_incident.diverted_departures,0)    AS diverted_departures,
            nvl(monthly_incident.AOG_delayed_departures,0) AS AOG_delayed_departures,
            nvl(monthly_incident.air_turnbacks,0)          AS air_turnbacks,
            nvl(monthly_incident.aborted_departures,0)     AS aborted_departures,
            nvl(monthly_incident.aborted_approaches,0)     AS aborted_approaches,
            nvl(monthly_incident.return_to_gate,0)         AS return_to_gate,
            nvl(monthly_incident.emergency_descents,0)     AS emergency_descents,
            nvl(monthly_incident.inflight_shutdowns,0)     AS inflight_shutdowns
         FROM
            (
                SELECT incident.*
                FROM
                   opr_monthly_incident incident
                   INNER JOIN
                      opr_calendar_month calendar_month ON
                         calendar_month.year_code  = incident.year_code AND
                         calendar_month.month_code = incident.month_code
                WHERE
                   calendar_month.start_date between aid_start_date AND aid_end_date OR
                   calendar_month.end_date   BETWEEN aid_start_date AND aid_end_date
            )
            PIVOT (
                     SUM(number_of_incidents) FOR incident_code IN
                        (
                            'ABT'    AS aborted_departures,
                            'ABA'    AS aborted_approaches,
                            'ATB'    AS air_turnbacks,
                            'DIV'    AS diverted_departures,
                            'IFD'    AS inflight_shutdowns,
                            'EMD'    AS emergency_descents,
                            'RTG'    AS return_to_gate,
                            'AOG'    AS AOG_delayed_departures
                        )
                   ) monthly_incident
      ) mx_incident
      ON (
              mx_incident.year_code                  = monthly_reliability.year_code                  AND
              mx_incident.month_code                 = monthly_reliability.month_code                 AND
              mx_incident.operator_registration_code = monthly_reliability.operator_registration_code AND
              mx_incident.serial_number              = monthly_reliability.serial_number              AND
              mx_incident.operator_code              = monthly_reliability.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_reliability.diverted_departures    = mx_incident.diverted_departures,
             monthly_reliability.AOG_delayed_departures = mx_incident.AOG_delayed_departures,
             monthly_reliability.air_turnbacks          = mx_incident.air_turnbacks,
             monthly_reliability.aborted_departures     = mx_incident.aborted_departures,
             monthly_reliability.aborted_approaches     = mx_incident.aborted_approaches,
             monthly_reliability.return_to_gate         = mx_incident.return_to_gate,
             monthly_reliability.emergency_descent      = mx_incident.emergency_descents,
             monthly_reliability.inflight_shutdowns     = mx_incident.inflight_shutdowns
          WHERE
             monthly_reliability.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END summarize_incidents;

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
   FUNCTION calculate_dos
   (
        aid_start_date  IN opr_calendar_month.start_date%TYPE,
        aid_end_date    IN opr_calendar_month.end_date%TYPE

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
       --
       MERGE INTO opr_monthly_reliability monthly_reliability
       USING
       (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number,
             SUM(maintenance.duration) as days_out_of_service
          FROM
             opr_maintenance maintenance
             INNER JOIN
                opr_calendar_month report_period ON
                   maintenance.actual_start_date BETWEEN
                       report_period.start_date AND report_period.end_date
             INNER JOIN
                opr_fleet_movement fleet_movement ON
                   maintenance.serial_number = fleet_movement.serial_number AND
                   (
                       maintenance.actual_start_date >= fleet_movement.phase_in_date   AND
                       maintenance.actual_start_date <= coalesce(fleet_movement.phase_out_date, maintenance.actual_start_date)
                   )
          WHERE
              maintenance.actual_start_date BETWEEN aid_start_date AND aid_end_date
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number
       ) mx_usage
       ON
          (
             mx_usage.operator_registration_code = monthly_reliability.operator_registration_code AND
             mx_usage.serial_number              = monthly_reliability.serial_number              AND
             mx_usage.operator_code              = monthly_reliability.operator_code              AND
             mx_usage.year_code                  = monthly_reliability.year_code                  AND
             mx_usage.month_code                 = monthly_reliability.month_code
          )
       WHEN MATCHED
       THEN
          UPDATE SET
              monthly_reliability.days_out_of_service = mx_usage.days_out_of_service
          WHERE
             monthly_reliability.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_dos;

END opr_fleet_summary_pkg;
/