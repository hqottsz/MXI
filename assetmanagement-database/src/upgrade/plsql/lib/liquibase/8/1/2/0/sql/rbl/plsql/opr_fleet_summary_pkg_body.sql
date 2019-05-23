--liquibase formatted sql


--changeSet opr_fleet_summary_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
      MERGE INTO opr_rbl_usage usage
      USING
      (

         SELECT
            serial_number,
            usage_date,
            data_type_code,
            usage_type_code,
            tsn_delta_qty     as usage_value,
            usage_record_id   as usage_id
         FROM
            acor_aircraft_usage_v1 aircraft_usage
         WHERE
               TRUNC(aircraft_usage.usage_date) BETWEEN aid_start_date AND aid_end_date
      ) mx_usage
      ON (
            mx_usage.serial_number   = usage.serial_number  AND
            mx_usage.usage_date      = usage.usage_date     AND
            mx_usage.data_type_code  = usage.data_type      AND
            mx_usage.usage_type_code = usage.usage_type     AND
            mx_usage.usage_id        = usage.usage_record_id
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
            usage.data_type,
            usage.usage_type,
            usage.usage_value,
            usage.usage_record_id
         )
         VALUES
         (
            mx_usage.serial_number,
            mx_usage.usage_date,
            mx_usage.data_type_code,
            mx_usage.usage_type_code,
            mx_usage.usage_value,
            mx_usage.usage_id
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

   FUNCTION extract_fault
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
      MERGE INTO opr_rbl_fault fault
      USING
      (
         SELECT
            fault.serial_number            AS serial_number,
            substr(fault.chapter_code,1,2) AS chapter_code,
            fault.found_on_date            AS found_on_date,
            fault.fault_id                 AS fault_id,
            fault.fault_description        AS fault_description,
            fault.fault_severity           AS fault_severity,
            fault.deferral_class           AS deferral_class
         FROM
            acor_aircraft_fault_v1 fault
         WHERE
            TRUNC(fault.found_on_date) BETWEEN aid_start_date AND aid_end_date
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

   END extract_fault;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract fault incident
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_rbl_fault_incident fault_incident
      USING
      (
            SELECT
               fault.serial_number,
               substr(fault.chapter_code,1,2) as chapter_code,
               fault.found_on_date,
               fault.fault_id,
               fault_incident.result_event_code as incident_code
            FROM
               acor_fault_result_v1 fault_incident
               INNER JOIN opr_rbl_fault fault ON
                  fault.fault_id = fault_incident.fault_id
            WHERE
               TRUNC(fault.found_on_date) BETWEEN aid_start_date AND aid_end_date
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

   END extract_fault_incident;
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_rbl_flight flight
      USING
      (
          SELECT
            flight_number                AS flight_number,
            aircraft_serial_number       AS serial_number,
            depart_airport               AS departure_airport_code,
            schedule_departure_date      AS scheduled_departure_date,
            actual_departure_date        AS actual_departure_date,
            flight_status_code           AS flight_status_code,
            nvl(flight_type_code,'RGEN') AS flight_type_code,
            schedule_arrival_date        AS scheduled_arrival_date,
            actual_arrival_date          AS actual_arrival_date,
            flight_type_code             AS flight_type,
            nvl(delta_cycle,0)           AS cycles,
            nvl(delta_hourr,0)           AS flight_hours,
            aircraft_id                  AS aircraft_id,
            leg_id                       AS flight_id,
            usage_record_id              AS usage_id
         FROM
            acor_flights_v1 flight
         WHERE
            TRUNC(flight.schedule_departure_date) BETWEEN aid_start_date AND aid_end_date
      ) mx_flight
      ON (
             flight.flight_number            = mx_flight.flight_number            AND
             flight.scheduled_departure_date = mx_flight.scheduled_departure_date AND
             flight.departure_airport        = mx_flight.departure_airport_code   AND
             flight.flight_id                = mx_flight.flight_id
         )
      WHEN NOT MATCHED THEN
          INSERT
          (
              flight.flight_number,
              flight.departure_airport,
              flight.scheduled_departure_date,
              flight.actual_departure_date,
              flight.serial_number,
              flight.flight_status,
              flight.flight_type,
              flight.aircraft_id,
              flight.flight_id,
              flight.usage_record_id
          )
          VALUES
          (
              mx_flight.flight_number,
              mx_flight.departure_airport_code,
              mx_flight.scheduled_departure_date,
              mx_flight.actual_departure_date,
              mx_flight.serial_number,
              mx_flight.flight_status_code,
              mx_flight.flight_type_code,
              mx_flight.aircraft_id,
              mx_flight.flight_id,
              mx_flight.usage_id
          )
        WHEN MATCHED THEN
           UPDATE
              SET
                 flight.serial_number          = mx_flight.serial_number,
                 flight.flight_status          = mx_flight.flight_status_code,
                 flight.aircraft_id            = mx_flight.aircraft_id,
                 flight.scheduled_arrival_date = mx_flight.scheduled_arrival_date,
                 flight.actual_arrival_date    = mx_flight.actual_arrival_date,
                 flight.actual_departure_date  = mx_flight.actual_departure_date,
                 flight.usage_record_id        = mx_flight.usage_id;

      l_row_count := SQL%ROWCOUNT;

      RETURN (l_row_count);

   END extract_flight;


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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_rbl_flight_disruption flight_disruption
      USING
      (
          SELECT
             flight_number,
             departure_airport,
             scheduled_departure_date,
             flight_id as flight_id,
             disruption_type_code,
             flight_disruption.delay_code,
             maint_delay_time_qt as maintenance_delay_time,
             flight_disruption.sched_id,
             fault.chapter_code,
             fault.fault_id,
             fault.found_on_date,
             flight.serial_number,
             flight_disruption.leg_disrupt_id
          FROM
             acor_fl_disruptions_v1 flight_disruption
             INNER JOIN opr_rbl_flight flight ON
                flight.flight_id = flight_disruption.leg_id
             INNER JOIN acor_delay_v1 delay ON
                delay.delay_code = flight_disruption.delay_code
             LEFT JOIN acor_corrective_task_v1 corrective_task ON
                corrective_task.corrective_task_id = flight_disruption.sched_id
             LEFT JOIN opr_rbl_fault fault ON
                fault.fault_id = corrective_task.fault_id
          WHERE
             delay.tech_delay_bool = 1 AND -- delayed because of a technical issue
             TRUNC(flight.scheduled_departure_date) BETWEEN aid_start_date AND aid_end_date
      ) mx_flight_disruption
      ON
      (
         mx_flight_disruption.flight_number            = flight_disruption.flight_number            AND
         mx_flight_disruption.departure_airport        = flight_disruption.departure_airport        AND
         mx_flight_disruption.scheduled_departure_date = flight_disruption.scheduled_departure_date    AND
         mx_flight_disruption.flight_id                = flight_disruption.flight_id                AND
         mx_flight_disruption.disruption_type_code     = flight_disruption.disruption_type_code     AND
         mx_flight_disruption.leg_disrupt_id           = flight_disruption.disruption_id
      )
      WHEN MATCHED THEN
         UPDATE SET
            flight_disruption.delay_code             = mx_flight_disruption.delay_code,
            flight_disruption.maintenance_delay_time = mx_flight_disruption.maintenance_delay_time,
            flight_disruption.chapter_code           = mx_flight_disruption.chapter_code,
            flight_disruption.serial_number          = mx_flight_disruption.serial_number,
            flight_disruption.found_on_date          = mx_flight_disruption.found_on_date,
            flight_disruption.fault_id               = mx_flight_disruption.fault_id

      WHEN NOT MATCHED THEN
         INSERT
         (
            flight_disruption.flight_number,
            flight_disruption.departure_airport,
            flight_disruption.scheduled_departure_date,
            flight_disruption.flight_id,
            flight_disruption.disruption_type_code,
            flight_disruption.delay_code,
            flight_disruption.maintenance_delay_time,
            flight_disruption.serial_number,
            flight_disruption.chapter_code,
            flight_disruption.found_on_date,
            flight_disruption.disruption_id,
            flight_disruption.fault_id
         )
         VALUES
         (
            mx_flight_disruption.flight_number,
            mx_flight_disruption.departure_airport,
            mx_flight_disruption.scheduled_departure_date,
            mx_flight_disruption.flight_id,
            mx_flight_disruption.disruption_type_code,
            mx_flight_disruption.delay_code,
            mx_flight_disruption.maintenance_delay_time,
            mx_flight_disruption.serial_number,
            mx_flight_disruption.chapter_code,
            mx_flight_disruption.found_on_date,
            mx_flight_disruption.leg_disrupt_id,
            mx_flight_disruption.fault_id
         );

      l_row_count := SQL%ROWCOUNT;

      RETURN (l_row_count);

   END extract_flight_disruption;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      extract work_package
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

   ) RETURN INTEGER

   IS
      l_row_count INTEGER;
      --
   BEGIN


       MERGE INTO opr_rbl_work_package work_package
       USING
       (
          SELECT
            aircraft_serial_number,
            schedule_start_date,
            schedule_end_date,
            actual_start_date,
            actual_end_date,
            work_package_name,
            barcode                             as work_package_barcode,
            sched_id                            as work_package_id,
            actual_end_date - actual_start_date as duration
          FROM
             acor_acft_wp_v1
          WHERE
             TRUNC(actual_start_date) BETWEEN aid_start_date AND aid_end_date AND
             actual_start_date IS NOT NULL                             AND
             status in ('COMPLETE', 'IN WORK')                         AND
             NVL(actual_end_date,LAST_DAY(actual_start_date)) - actual_start_date >= 1
       ) mx_work_package
       ON (
              mx_work_package.aircraft_serial_number        = work_package.serial_number        AND
              mx_work_package.actual_start_date    = work_package.actual_start_date    AND
              mx_work_package.work_package_barcode = work_package.work_package_barcode
          )
       WHEN NOT MATCHED
       THEN
       --
          INSERT
          (
              work_package.serial_number,
              work_package.scheduled_start_date,
              work_package.scheduled_end_date,
              work_package.actual_start_date,
              work_package.actual_end_date,
              work_package.work_package_barcode,
              work_package.work_package_name
          )
          VALUES
          (
              mx_work_package.aircraft_serial_number,
              mx_work_package.schedule_start_date,
              mx_work_package.schedule_end_date,
              mx_work_package.actual_start_date,
              mx_work_package.actual_end_date,
              mx_work_package.work_package_barcode,
              mx_work_package.work_package_name
          );


       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

    END extract_work_package;
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
       MERGE INTO opr_rbl_monthly_usage monthly_usage
       USING
       (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number,
             fleet_movement.operator_code,
             sum(usage.cycles)                AS cycles,
             sum(usage.flight_hours)          AS flight_hours,
             sum(usage.revenue_cycles)        AS revenue_cycles,
             sum(usage.revenue_flight_hours)  AS revenue_flight_hours,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id
          FROM
             (
                 SELECT
                    rbl_usage.serial_number,
                    rbl_usage.usage_date,
                    rbl_usage.flight_hours,
                    rbl_usage.cycles,
                    rbl_usage.usage_record_id,
                    CASE flight.flight_type
                       WHEN 'RGEN' THEN rbl_usage.cycles
                       ELSE             0
                    END AS revenue_cycles,
                    CASE flight.flight_type
                       WHEN 'RGEN' THEN rbl_usage.flight_hours
                       ELSE             0
                    END AS revenue_flight_hours
                 FROM
                     (
                        SELECT
                           serial_number,
                           usage_date,
                           flight_hours,
                           cycles,
                           usage_record_id
                        FROM
                           opr_rbl_usage
                        PIVOT (
                                 SUM(usage_value) FOR data_type IN
                                                      (
                                                        'CYCLES' AS cycles,
                                                        'HOURS'  AS flight_hours
                                                      )
                              )
                     ) rbl_usage
                     LEFT JOIN opr_rbl_flight flight ON
                        flight.usage_record_id = rbl_usage.usage_record_id
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
             TRUNC(usage.usage_date) BETWEEN aid_start_date and aid_end_date
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id
       ) mx_usage
       ON (
              mx_usage.year_code                  = monthly_usage.year_code                  AND
              mx_usage.month_code                 = monthly_usage.month_code                 AND
              mx_usage.fleet_type                 = monthly_usage.fleet_type                 AND
              mx_usage.operator_registration_code = monthly_usage.operator_registration_code AND
              mx_usage.serial_number              = monthly_usage.serial_number              AND
              mx_usage.operator_code              = monthly_usage.operator_code
          )
       WHEN MATCHED
       THEN
       --
          UPDATE SET
             monthly_usage.revenue_cycles       = mx_usage.revenue_cycles,
             monthly_usage.revenue_flight_hours = mx_usage.revenue_flight_hours,
             monthly_usage.cycles               = mx_usage.cycles,
             monthly_usage.flight_hours         = mx_usage.flight_hours,
             monthly_usage.aircraft_id          = mx_usage.aircraft_id,
             monthly_usage.operator_id          = mx_usage.operator_id
       --
       WHEN NOT MATCHED
       THEN
       --
          INSERT
          (
             monthly_usage.year_code,
             monthly_usage.month_code,
             monthly_usage.fleet_type,
             monthly_usage.operator_registration_code,
             monthly_usage.serial_number,
             monthly_usage.operator_code,
             monthly_usage.cycles,
             monthly_usage.flight_hours,
             monthly_usage.revenue_cycles,
             monthly_usage.revenue_flight_hours,
             monthly_usage.aircraft_id,
             monthly_usage.operator_id
          )
          VALUES
          (
             mx_usage.year_code,
             mx_usage.month_code,
             mx_usage.fleet_type,
             mx_usage.operator_registration_code,
             mx_usage.serial_number,
             mx_usage.operator_code,
             mx_usage.cycles,
             mx_usage.flight_hours,
             mx_usage.revenue_cycles,
             mx_usage.revenue_flight_hours,
             mx_usage.aircraft_id,
             mx_usage.operator_id
          );

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END summarize_usage;

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
      MERGE INTO opr_rbl_monthly_usage monthly_usage
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number as serial_number,
             fleet_movement.operator_code,
             sum(cancelled_departures) as cancelled_departures
          FROM
             (
                SELECT
                   flight_number,
                   departure_airport,
                   scheduled_departure_date,
                   cancelled_departures,
                   flight_id
                FROM
                   opr_rbl_flight_disruption flight_disruption
                PIVOT (
                         COUNT(disruption_type_code) FOR disruption_type_code IN
                                              (
                                                'CNX'  AS cancelled_departures
                                              )
                      )
             ) cancels
             INNER JOIN opr_calendar_month report_period ON
                cancels.scheduled_departure_date BETWEEN report_period.start_date AND report_period.end_date
             INNER JOIN opr_rbl_flight flight ON
                flight.flight_number            = cancels.flight_number            AND
                flight.departure_airport        = cancels.departure_airport        AND
                flight.scheduled_departure_date = cancels.scheduled_departure_date AND
                flight.flight_id                = cancels.flight_id
             INNER JOIN opr_fleet_movement fleet_movement ON
             flight.serial_number = fleet_movement.serial_number AND
                (
                    cancels.scheduled_departure_date >= fleet_movement.phase_in_date   AND
                    cancels.scheduled_departure_date <= coalesce(fleet_movement.phase_out_date, cancels.scheduled_departure_date)
                )
          WHERE
            TRUNC(flight.scheduled_departure_date) BETWEEN aid_start_date AND aid_end_date
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number
       ) mx_cancels
       ON (
              mx_cancels.year_code                  = monthly_usage.year_code                  AND
              mx_cancels.month_code                 = monthly_usage.month_code                 AND
              mx_cancels.fleet_type                 = monthly_usage.fleet_type                 AND
              mx_cancels.operator_registration_code = monthly_usage.operator_registration_code AND
              mx_cancels.serial_number              = monthly_usage.serial_number              AND
              mx_cancels.operator_code              = monthly_usage.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_usage.cancelled_departures = mx_cancels.cancelled_departures;

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
      MERGE INTO opr_rbl_monthly_usage monthly_usage
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number as serial_number,
             fleet_movement.operator_code,
             count(*)                     as mel_delayed_departures
          FROM
             opr_rbl_fault fault
             INNER JOIN opr_calendar_month report_period ON
               fault.found_on_date BETWEEN report_period.start_date AND report_period.end_date
             INNER JOIN opr_fleet_movement fleet_movement ON
               fault.serial_number = fleet_movement.serial_number AND
               (
                    fault.found_on_date >= fleet_movement.phase_in_date   AND
                    fault.found_on_date <= coalesce(fleet_movement.phase_out_date, fault.found_on_date)
               )
          WHERE
              TRUNC(fault.found_on_date) BETWEEN aid_start_date and aid_end_date AND
              fault.fault_severity = 'MEL'

          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number
       ) mx_fault
       ON (
              mx_fault.year_code                  = monthly_usage.year_code                  AND
              mx_fault.month_code                 = monthly_usage.month_code                 AND
              mx_fault.fleet_type                 = monthly_usage.fleet_type                  AND
              mx_fault.operator_registration_code = monthly_usage.operator_registration_code AND
              mx_fault.serial_number              = monthly_usage.serial_number              AND
              mx_fault.operator_code              = monthly_usage.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_usage.mel_delayed_departures = mx_fault.mel_delayed_departures;

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
      MERGE INTO opr_rbl_monthly_delay monthly_delay
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
            flight_disruption.departure_airport,
            NVL(fault.chapter_code,'N/A') as chapter_code,
            fleet_movement.aircraft_id,
            fleet_movement.operator_id,
            COUNT(*) AS delayed_departures,
            sum(flight_disruption.maintenance_delay_time) as delay_time
            --'MAINTNEIX' as source_system
         FROM
            opr_rbl_flight_disruption flight_disruption
            LEFT JOIN opr_rbl_fault fault ON
               fault.fault_id      = flight_disruption.fault_id
            INNER JOIN opr_rbl_delay_category delay_category ON
               flight_disruption.maintenance_delay_time between delay_category.low_range and nvl(delay_category.high_range, flight_disruption.maintenance_delay_time)
            INNER JOIN opr_calendar_month report_period ON
               flight_disruption.scheduled_departure_date BETWEEN report_period.start_date AND report_period.end_date
            INNER JOIN opr_fleet_movement fleet_movement ON
               flight_disruption.serial_number = fleet_movement.serial_number AND
               (
                  flight_disruption.scheduled_departure_date >= fleet_movement.phase_in_date   AND
                  flight_disruption.scheduled_departure_date <= coalesce(fleet_movement.phase_out_date, flight_disruption.scheduled_departure_date)
               )
         WHERE
            TRUNC(flight_disruption.scheduled_departure_date) BETWEEN aid_start_date and aid_end_date AND
            disruption_type_code = 'DLY'
         GROUP BY
            report_period.year_code,
            report_period.month_code,
            fleet_movement.fleet_type,
            fleet_movement.operator_registration_code,
            fleet_movement.serial_number,
            fleet_movement.operator_code,
            delay_category.delay_category_code,
            flight_disruption.departure_airport,
            fault.chapter_code,
            fleet_movement.aircraft_id,
            fleet_movement.operator_id
      ) mx_delay
      ON (
              mx_delay.year_code                  = monthly_delay.year_code                  AND
              mx_delay.month_code                 = monthly_delay.month_code                 AND
              mx_delay.fleet_type                 = monthly_delay.fleet_type                 AND
              mx_delay.operator_registration_code = monthly_delay.operator_registration_code AND
              mx_delay.serial_number              = monthly_delay.serial_number              AND
              mx_delay.operator_code              = monthly_delay.operator_code              AND
              mx_delay.departure_airport          = monthly_delay.departure_airport_code     AND
              mx_delay.chapter_code               = monthly_delay.chapter_code               AND
              mx_delay.delay_category_code        = monthly_delay.delay_category_code
         )

      WHEN NOT MATCHED THEN
         INSERT
         (
            monthly_delay.year_code,
            monthly_delay.month_code,
            monthly_delay.fleet_type,
            monthly_delay.operator_registration_code,
            monthly_delay.serial_number,
            monthly_delay.operator_code,
            monthly_delay.departure_airport_code,
            monthly_delay.chapter_code,
            monthly_delay.delay_category_code,
            monthly_delay.delayed_departures,
            monthly_delay.delay_time,
            monthly_delay.maintenance_delay_time,
            --monthly_delay.source_system,
            monthly_delay.aircraft_id,
            monthly_delay.operator_id
         )
         VALUES
         (
            mx_delay.year_code,
            mx_delay.month_code,
            mx_delay.fleet_type,
            mx_delay.operator_registration_code,
            mx_delay.serial_number,
            mx_delay.operator_code,
            mx_delay.departure_airport,
            mx_delay.chapter_code,
            mx_delay.delay_category_code,
            mx_delay.delayed_departures,
            mx_delay.delay_time,
            mx_delay.delay_time,
            --mx_delay.source_system,
            mx_delay.aircraft_id,
            mx_delay.operator_id
         )
      WHEN MATCHED THEN
         UPDATE SET
            monthly_delay.delayed_departures     = mx_delay.delayed_departures,
            monthly_delay.delay_time             = mx_delay.delay_time,
            monthly_delay.maintenance_delay_time = mx_delay.delay_time;

--            monthly_delay.source_system      = mx_delay.source_system;
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
      ln_return   INTEGER;
      --
   BEGIN
      --
      MERGE INTO opr_rbl_monthly_incident monthly_incident
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number,
             fleet_movement.operator_code,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id,
             fleet_movement.fleet_type,
             fault_incident.incident_code,
             flight_disruption.departure_airport,
             flight_disruption.chapter_code,
             count(*) as number_of_incidents
          FROM
             opr_rbl_fault_incident fault_incident
             INNER JOIN opr_rbl_flight_disruption flight_disruption ON
               flight_disruption.serial_number = fault_incident.serial_number AND
               flight_disruption.chapter_code  = fault_incident.chapter_code  AND
               flight_disruption.found_on_date = fault_incident.found_on_date AND
               flight_disruption.fault_id      = fault_incident.fault_id
             INNER JOIN opr_calendar_month report_period ON
               flight_disruption.scheduled_departure_date BETWEEN
                  report_period.start_date AND report_period.end_date
             INNER JOIN opr_fleet_movement fleet_movement ON
               fault_incident.serial_number = fleet_movement.serial_number AND
               (
                   flight_disruption.scheduled_departure_date >= fleet_movement.phase_in_date   AND
                   flight_disruption.scheduled_departure_date <= coalesce(fleet_movement.phase_out_date, flight_disruption.scheduled_departure_date)
               )
          WHERE
             TRUNC(flight_disruption.scheduled_departure_date) BETWEEN aid_start_date and aid_end_date
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number,
             fault_incident.incident_code,
             flight_disruption.departure_airport,
             flight_disruption.chapter_code,
             fleet_movement.aircraft_id,
             fleet_movement.operator_id
        ) mx_incident
        ON (
              mx_incident.year_code                  = monthly_incident.year_code                  AND
              mx_incident.month_code                 = monthly_incident.month_code                 AND
              mx_incident.fleet_type                 = monthly_incident.fleet_type                 AND
              mx_incident.operator_registration_code = monthly_incident.operator_registration_code AND
              mx_incident.serial_number              = monthly_incident.serial_number              AND
              mx_incident.operator_code              = monthly_incident.operator_code              AND
              mx_incident.chapter_code               = monthly_incident.chapter_code               AND
              mx_incident.departure_airport          = monthly_incident.departure_airport_code     AND
              mx_incident.incident_code              = monthly_incident.incident_code
           )
        WHEN MATCHED THEN
           UPDATE SET
              monthly_incident.number_of_incidents   = mx_incident.number_of_incidents,
              monthly_incident.aircraft_id           = mx_incident.aircraft_id,
              monthly_incident.operator_id           = mx_incident.operator_id

        WHEN NOT MATCHED THEN
           INSERT
           (
              monthly_incident.year_code,
              monthly_incident.month_code,
              monthly_incident.fleet_type,
              monthly_incident.operator_registration_code,
              monthly_incident.serial_number,
              monthly_incident.operator_code,
              monthly_incident.departure_airport_code,
              monthly_incident.chapter_code,
              monthly_incident.incident_code,
              monthly_incident.number_of_incidents,
              monthly_incident.aircraft_id,
              monthly_incident.operator_id
           )
           VALUES
           (
              mx_incident.year_code,
              mx_incident.month_code,
              mx_incident.fleet_type,
              mx_incident.operator_registration_code,
              mx_incident.serial_number,
              mx_incident.operator_code,
              mx_incident.departure_airport,
              mx_incident.chapter_code,
              mx_incident.incident_code,
              mx_incident.number_of_incidents,
              mx_incident.aircraft_id,
              mx_incident.operator_id
           );
       l_row_count := SQL%ROWCOUNT;


       -- refresh relevant materialized view
       ln_return := opr_rbl_utility_pkg.opr_refresh_mview( aiv_category_or_frequency => 'USAGE',
                                                           aiv_refresh_type_code     => 'CATEGORY'
                                                          );



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
      MERGE INTO opr_rbl_monthly_usage monthly_dispatch
      USING
      (
          SELECT
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number,
             fleet_movement.operator_code,
             count(*) as completed_departures
          FROM
             opr_rbl_flight flight
             INNER JOIN opr_calendar_month report_period ON
               flight.scheduled_departure_date BETWEEN
                  report_period.start_date AND report_period.end_date
             INNER JOIN opr_fleet_movement fleet_movement ON
               flight.serial_number = fleet_movement.serial_number AND
               (
                   flight.scheduled_departure_date >= fleet_movement.phase_in_date   AND
                   flight.scheduled_departure_date <= coalesce(fleet_movement.phase_out_date, flight.scheduled_departure_date)
               )

          WHERE
             TRUNC(flight.scheduled_departure_date) BETWEEN aid_start_date and aid_end_date AND
             --
             -- the flight was completed at some point
             flight.flight_id IN
             (
                 SELECT
                    flight_id
                 FROM
                    fl_leg_status_log
                 WHERE
                    fl_leg_status_log.flight_leg_status_cd in ('MXCMPLT')
             ) AND
             --
             -- the aircraft accumulated a delta of 0.
             -- i.e. cycles and/or usage was not accumulated on the flight
             flight.usage_record_id IN
             (
                 SELECT
                    usage_record_id
                 FROM
                    opr_rbl_usage
                 WHERE
                    usage_value != 0
             )
          GROUP BY
             report_period.year_code,
             report_period.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.operator_code,
             fleet_movement.serial_number
       ) mx_complete
       ON (
              mx_complete.year_code                  = monthly_dispatch.year_code                  AND
              mx_complete.month_code                 = monthly_dispatch.month_code                 AND
              mx_complete.fleet_type                 = monthly_dispatch.fleet_type                 AND
              mx_complete.operator_registration_code = monthly_dispatch.operator_registration_code AND
              mx_complete.serial_number              = monthly_dispatch.serial_number              AND
              mx_complete.operator_code              = monthly_dispatch.operator_code
          )
       WHEN MATCHED THEN
          UPDATE SET
             monthly_dispatch.completed_departures = mx_complete.completed_departures;
          --WHERE
          --   monthly_dispatch.source_system = 'MAINTENIX';

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_completed_departures;

   ---------------------------------------------------------------------------------
   -- function
   --
   --      calcualate_dos
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
       --
       MERGE INTO opr_rbl_work_package_dos dos_calc
       USING
           (
              --
              -- generate a list of dates between the start and end of a work package
              -- if the work package has not been completed or does not have an actual end date
              -- only count the number of days until the end of the period
              --
              SELECT
                 to_char(actual_start_date + LEVEL - 1, 'YYYY') AS year_code,
                 to_char(actual_start_date + LEVEL - 1, 'MM')   AS month_code,
                 serial_number,
                 actual_start_date + LEVEL - 1                  AS out_of_service_date,
                 work_package_name,
                 work_package_barcode
              FROM
                 opr_rbl_work_package
              WHERE
                 NVL(actual_end_date,LAST_DAY(actual_start_date)) - actual_start_date >= 1 AND
                 TRUNC(actual_start_date) BETWEEN aid_start_date AND aid_end_date
              CONNECT BY
                 LEVEL <= NVL(actual_end_date,LAST_DAY(actual_start_date)) - actual_start_date      AND
                 PRIOR serial_number = serial_number               AND
                 PRIOR work_package_barcode = work_package_barcode AND
                 PRIOR dbms_random.value != 1
           ) work_package ON
             (
                 work_package.year_code            = dos_calc.year_code            AND
                 work_package.month_code           = dos_calc.month_code           AND
                 work_package.serial_number        = dos_calc.serial_number        AND
                 work_package.work_package_barcode = dos_calc.work_package_barcode AND
                 work_package.out_of_service_date  = dos_calc.out_of_service_date
             )
       WHEN MATCHED THEN
          UPDATE set
              dos_calc.work_package_name = work_package.work_package_name
       WHEN NOT MATCHED
       THEN
       --
          INSERT
          (
             dos_calc.year_code,
             dos_calc.month_code,
             dos_calc.serial_number,
             dos_calc.out_of_service_date,
             dos_calc.work_package_name,
             dos_calc.work_package_barcode
          )
          VALUES
          (
             work_package.year_code,
             work_package.month_code,
             work_package.serial_number,
             work_package.out_of_service_date,
             work_package.work_package_name,
             work_package.work_package_barcode
          );

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_dos_pt1;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate_dos_pt2
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
   FUNCTION calculate_dos_pt2
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
      MERGE INTO opr_rbl_monthly_usage monthly_usage
      USING
      (
          SELECT
             dos.year_code,
              dos.month_code,
              fleet_movement.fleet_type,
              fleet_movement.operator_registration_code,
              fleet_movement.serial_number,
              fleet_movement.operator_code,
              COUNT(distinct trunc(out_of_service_date, 'DD')) AS days_out_of_service
          FROM
             opr_rbl_work_package_dos dos
              INNER JOIN opr_fleet_movement fleet_movement ON
                 dos.serial_number = fleet_movement.serial_number AND
                 (
                     dos.out_of_service_date >= fleet_movement.phase_in_date   AND
                     dos.out_of_service_date <= coalesce(fleet_movement.phase_out_date, dos.out_of_service_date)
                 )
          WHERE
             TRUNC(dos.out_of_service_date) BETWEEN aid_start_date AND aid_end_date
          GROUP BY
             dos.year_code,
             dos.month_code,
             fleet_movement.fleet_type,
             fleet_movement.operator_registration_code,
             fleet_movement.serial_number,
             fleet_movement.operator_code
      ) mx_dos_calc
      ON
      (
           monthly_usage.year_code                  = mx_dos_calc.year_code                  AND
           monthly_usage.month_code                 = mx_dos_calc.month_code                 AND
           monthly_usage.fleet_type                 = mx_dos_calc.fleet_type                 AND
           monthly_usage.operator_registration_code = mx_dos_calc.operator_registration_code AND
           monthly_usage.serial_number              = mx_dos_calc.serial_number              AND
           monthly_usage.operator_code              = mx_dos_calc.operator_code
      )
      WHEN MATCHED THEN
         UPDATE SET
            monthly_usage.days_out_of_service = mx_dos_calc.days_out_of_service;

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_dos_pt2;
   ---------------------------------------------------------------------------------
   -- function
   --
   --      calculate_dos_pt3
   --
   -- Description:
   --
   --   calculate the list of work packages by year, month and serial number
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

   ) RETURN INTEGER

   IS
      --
      l_row_count INTEGER;
      --
   BEGIN
       --
        MERGE INTO opr_rbl_monthly_usage monthly_usage
        USING
        (
              SELECT
                  year_code,
                  month_code,
                  LISTAGG(work_package_name,',') WITHIN GROUP (ORDER BY year_code, month_code) AS work_package_list,
                  serial_number,
                  operator_registration_code,
                  operator_code
              FROM
              (
                  SELECT
                      dos.year_code,
                      dos.month_code,
                      dos.work_package_name,
                      dos.serial_number,
                      fleet_movement.operator_registration_code,
                      fleet_movement.operator_code,
                      COUNT(*)
                  FROM
                      opr_rbl_work_package_dos dos
                      INNER JOIN opr_fleet_movement fleet_movement ON
                      dos.serial_number = fleet_movement.serial_number AND
                      (
                         dos.out_of_service_date >= fleet_movement.phase_in_date   AND
                         dos.out_of_service_date <= coalesce(fleet_movement.phase_out_date, dos.out_of_service_date)
                      )
                  WHERE
                      TRUNC(dos.out_of_service_date) between aid_start_date AND aid_end_date
                  GROUP BY
                      dos.year_code,
                      dos.month_code,
                      dos.work_package_name,
                      dos.serial_number,
                      fleet_movement.operator_registration_code,
                      fleet_movement.operator_code

              ) wp_dos
              GROUP BY
                 year_code,month_code, serial_number, operator_registration_code,operator_code
        ) mx_dos_wp
        ON
        (
            monthly_usage.year_code                  = mx_dos_wp.year_code AND
            monthly_usage.month_code                 = mx_dos_wp.month_code AND
            monthly_usage.operator_registration_code = mx_dos_wp.operator_registration_code AND
            monthly_usage.serial_number              = mx_dos_wp.serial_number AND
            monthly_usage.operator_code              = mx_dos_wp.operator_code
        )
        WHEN matched then
           update set monthly_usage.work_package_list = mx_dos_wp.work_package_list;

       l_row_count := SQL%ROWCOUNT;

       RETURN (l_row_count);

   END calculate_dos_pt3;

END opr_fleet_summary_pkg;
/