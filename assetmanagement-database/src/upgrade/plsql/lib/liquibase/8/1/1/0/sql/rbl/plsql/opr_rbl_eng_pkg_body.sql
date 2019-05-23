--liquibase formatted sql


--changeSet opr_rbl_eng_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_rbl_eng_pkg IS
/******************************************************************************
*
*  Function:    opr_rbl_eng_xtract
*  Arguments:    aidt_start_date - start date
*                aidt_end_date   - end date
*  Return:
*  Description:  Function that will extract the  engine operation raw data.
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  26-Mar-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_eng_xtract (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

  ln_row_processed   NUMBER;

BEGIN

   -- # engine usage raw data
   --
   DELETE
   FROM
      opr_rbl_eng_usage_raw
   WHERE
      -- year and month input param
      TRUNC(usage_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_usage_raw
      (
         usage_record_id,
         inventory_id,
         assmbl_code,
         original_assmbl_code,
         serial_number,
         manufacturer,
         part_number,
         operator_id,
         operator_code,
         usage_type_code,
         data_type_code,
         usage_date,
         tsn_qt,
         tso_qt,
         tsi_qt,
         tsn_delta_qt,
         tso_delta_qt,
         tsi_delta_qt,
         negated_bool
      )
   SELECT
      usage_record_id,
      inventory_id,
      assmbl_code,
      original_assmbl_code,
      serial_number,
      manufacturer,
      part_number,
      operator_id,
      operator_code,
      usage_type_code,
      data_type_code,
      usage_date,
      tsn_qt,
      tso_qt,
      tsi_qt,
      tsn_delta_qt,
      tso_delta_qt,
      tsi_delta_qt,
      negated_bool
   FROM
      acor_rbl_eng_usage_v1
   WHERE
      -- year and month input param
      TRUNC(usage_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   ln_row_processed := SQL%ROWCOUNT;

   -- # engine indicator raw data
   --

   DELETE
   FROM
      opr_rbl_eng_indicator_raw
   WHERE
      -- year and month input param
      TRUNC(data_date) BETWEEN aidt_start_date AND aidt_end_date;

   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_indicator_raw
      (
        operator_id,
        inventory_id,
        leg_id,
        operator_code,
        fleet_type,
        engine_type,
        serial_number,
        manufacturer,
        part_number,
        condition_code,
        data_type_code,
        oper_indicator,
        data_date,
        data_value_code,
        data_quantity,
        data_flag
      )
   SELECT
      operator_id,
      acor_rbl_eng_v1.inventory_id,
      leg_id,
      operator_code,
      assmbl_code             AS fleet_type,
      original_assmbl_code    AS engine_type,
      serial_number,
      manufacturer,
      part_number,
      condition_code,
      --
      measurement.data_type_code,
      measurement.oper_indicator,
      data_date,
      data_value_code,
      data_quantity,
      data_flag
   FROM
      acor_rbl_eng_v1
      INNER JOIN (-- 
                     SELECT 
                        inv_inv.alt_id         AS inventory_id,
                        ind_leg.leg_id,
                        ind_leg.data_qt        AS data_quantity,
                        ind_leg.data_dt        AS data_date,
                        ind_leg.data_value_cd  AS data_value_code,
                        ind_leg.data_bool      AS data_flag,
                        --
                        ind_leg.data_type_code,
                        ind_leg.data_type_description  AS oper_indicator
                     FROM 
                        fl_leg_measurement 
                        INNER JOIN ( -- FPTAKEOFF: this is a measurement against ACFT
                                     -- find the flight leg with this measurement 
                                     -- use the flight leg to find the ENG inventory
                                    SELECT
                                       leg_id,
                                       data_dt,
                                       data_value_cd,
                                       data_qt,
                                       data_bool,
                                       --
                                       data_type_description,
                                       data_type_code
                                    FROM 
                                       fl_leg_measurement
                                       INNER JOIN mim_data_type ON 
                                          fl_leg_measurement.data_type_db_id = mim_data_type.data_type_db_id AND
                                          fl_leg_measurement.data_type_id    = mim_data_type.data_type_id
                                       -- indicator lookup table (OPR)
                                       INNER JOIN opr_rbl_eng_indicator_measure ind_measure ON 
                                          mim_data_type.data_type_cd = ind_measure.data_type_code
                                   ) ind_leg ON 
                           fl_leg_measurement.leg_id =  ind_leg.leg_id    
                        INNER JOIN inv_inv ON 
                           fl_leg_measurement.inv_no_db_id = inv_inv.inv_no_db_id AND
                           fl_leg_measurement.inv_no_id    = inv_inv.inv_no_id
                        INNER JOIN eqp_assmbl ON 
                           inv_inv.orig_assmbl_db_id = eqp_assmbl.assmbl_db_id AND
                           inv_inv.orig_assmbl_cd    = eqp_assmbl.assmbl_cd
                           AND
                           eqp_assmbl.assmbl_class_cd = 'ENG'      
                 ) measurement ON
         acor_rbl_eng_v1.inventory_id = measurement.inventory_id
   WHERE
      -- year and month input param
      TRUNC(data_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   ln_row_processed := ln_row_processed + SQL%ROWCOUNT;

   -- # engine related delays raw data
   --
   DELETE
   FROM
      opr_rbl_eng_cancellation_raw
   WHERE
      -- year and month input param
      TRUNC(found_on_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_cancellation_raw
      (
        operator_id,
        flight_id,
        operator_code,
        fleet_type,
        engine_type,
        chapter_code,
        delay_code,
        disruption_type_code,
        serial_number,
        found_on_date
      )
   SELECT
       eng_type.operator_id,
       flight_id,
       eng_type.operator_code,
       eng_type.fleet_type,
       eng_type.engine_type,
       chapter_code,
       delay_code,
       disruption_type_code,
       serial_number,
       found_on_date
   FROM
      aopr_rbl_acft_eng_type_v1 eng_type
      INNER JOIN (
                  SELECT  
                     flight_id,
                     operator_code,
                     chapter_code,
                     delay_code,
                     disruption_type_code,
                     flight_disruption.serial_number,
                     flight_disruption.found_on_date
                  FROM 
                     opr_fleet_movement
                     INNER JOIN opr_rbl_flight_disruption flight_disruption ON
                        opr_fleet_movement.serial_number = flight_disruption.serial_number AND
                        TRUNC(flight_disruption.found_on_date) BETWEEN opr_fleet_movement.phase_in_date AND NVL(opr_fleet_movement.phase_out_date,flight_disruption.found_on_date)
                 ) disruption ON 
        eng_type.operator_code      = disruption.operator_code AND
        eng_type.acft_serial_number = disruption.serial_number
   WHERE
      REGEXP_INSTR(chapter_code,'^(7([1-7]|9)|80).*') > 0
      AND
      disruption_type_code = 'CNX'
      AND
      -- year and month input param
      TRUNC(found_on_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   ln_row_processed := ln_row_processed + SQL%ROWCOUNT;

   -- # monthly result event - IFD (In flight shutdown) and RTO (rejected take off)
      --
   DELETE
   FROM
      opr_rbl_eng_ifdrto_event_raw
   WHERE
      -- year and month input param
      TRUNC(found_on_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_ifdrto_event_raw (
        operator_id,
        fault_id,
        operator_code,
        fleet_type,
        engine_type,
        serial_number,
        chapter_code,
        found_on_date,
        incident_code
      )
   SELECT
      operator_id,
      fault_id,
      eng_type.operator_code,
      fleet_type,
      engine_type,
      serial_number,
      chapter_code,
      found_on_date,
      incident_code
   FROM
      aopr_rbl_acft_eng_type_v1 eng_type
      INNER JOIN (
                  SELECT  
                     fault_id,
                     operator_code,
                     chapter_code,
                     incident_code,
                     fault_incident.serial_number,
                     fault_incident.found_on_date
                  FROM 
                     opr_fleet_movement
                     INNER JOIN opr_rbl_fault_incident fault_incident ON
                        opr_fleet_movement.serial_number = fault_incident.serial_number AND
                        TRUNC(fault_incident.found_on_date) BETWEEN opr_fleet_movement.phase_in_date AND NVL(opr_fleet_movement.phase_out_date,fault_incident.found_on_date)
                 ) incident ON 
        eng_type.operator_code      = incident.operator_code AND
        eng_type.acft_serial_number = incident.serial_number
   WHERE
      REGEXP_INSTR(chapter_code,'^(7([1-7]|9)|80).*') > 0
      AND
      incident_code IN ('IFD','RTO')
      AND
      -- year and month input param
      TRUNC(found_on_date) BETWEEN aidt_start_date AND aidt_end_date;


   -- # engine related delays raw data
   --
   DELETE
   FROM
      opr_rbl_eng_shopvisit_raw
   WHERE
      -- year and month input param
      TRUNC(completed_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_shopvisit_raw
      (
        operator_id,
        inventory_id,
        sched_id,
        operator_code,
        fleet_type,
        engine_type,
        serial_number,
        manufacturer,
        part_number,
        condition_code,
        location_code,
        location_type_code,
        completed_date
      )
   SELECT
      operator_id,
      inventory_id,
      sched_id,
      operator_code,
      assmbl_code,
      original_assmbl_code,
      serial_number,
      manufacturer,
      part_number,
      condition_code,
      shopvisit_loc.location_code,
      shopvisit_loc.location_type_code,
      completed_date
   FROM
      acor_rbl_eng_wp_loc_v1 wp_loc
      -- engine shopvisit location
      INNER JOIN opr_rbl_eng_shopvisit_loc shopvisit_loc ON 
         wp_loc.location_id = shopvisit_loc.location_id
   WHERE
      completed_date IS NOT NULL
      AND
      -- year and month input param
      TRUNC(completed_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   ln_row_processed := ln_row_processed + SQL%ROWCOUNT;

   RETURN ln_row_processed;

END opr_rbl_eng_xtract;

/******************************************************************************
*
*  Function:    opr_rbl_eng_xform
*  Arguments:    aidt_start_date - start date
*                aidt_end_date   - end date
*  Return:
  Description:  Function that will summarize the engine operation.
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  26-Mar-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_eng_xform (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

   ln_row_processed   NUMBER  := 0;
   ln_return          NUMBER;

BEGIN

   -- # number of engines
   --
   -- in service
   DELETE
   FROM
      opr_rbl_eng_inservice_mon
   WHERE
      -- year and month input param
      TRUNC(TO_DATE(year_code||month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_inservice_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        in_service
      )
   WITH
   rvw_eng_with_usage
   AS
      (
         SELECT
            operator_id,
            operator_code,
            assmbl_code,
            original_assmbl_code engine_type,
            serial_number,
            TO_CHAR(usage_date,'YYYY') year_code,
            TO_CHAR(usage_date,'MM') month_code,
            1 in_service
         FROM
            opr_rbl_eng_usage_raw
          WHERE
           -- year and month input param
            TRUNC(usage_date) BETWEEN aidt_start_date AND aidt_end_date
         GROUP BY
            operator_id,
            operator_code,
            assmbl_code,
            serial_number,
            original_assmbl_code,
            TO_CHAR(usage_date,'YYYY'),
            TO_CHAR(usage_date,'MM')
       )
   SELECT
      operator_id,
      operator_code,
      assmbl_code,
      engine_type,
      year_code,
      month_code,
      SUM(in_service) in_service
   FROM
      rvw_eng_with_usage
   GROUP BY
      operator_id,
      operator_code,
      assmbl_code,
      engine_type,
      year_code,
      month_code;

    --
    ln_row_processed := ln_row_processed + SQL%ROWCOUNT;
   
    -- commit is needed in order to query data
    -- which was inserted using direct path
    COMMIT;

   -- in fleet
    DELETE
    FROM
       opr_rbl_eng_infleet_mon
    WHERE
       -- year and month input param
       TRUNC(TO_DATE(year_code||month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_infleet_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        in_fleet
      )
   SELECT
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        (in_fleet-new_in_fleet) in_fleet
   FROM
      (
         SELECT
            opr_rbl_eng_inservice_mon.operator_id,
            opr_rbl_eng_inservice_mon.operator_code,
            opr_rbl_eng_inservice_mon.fleet_type,
            opr_rbl_eng_inservice_mon.engine_type,
            opr_rbl_eng_inservice_mon.year_code,
            opr_rbl_eng_inservice_mon.month_code,
            TO_NUMBER(year_code || month_code) year_month,
            rvw_curr_infleet.in_fleet,
            SUM(NVL(NVL(rvw_in_fleet_dt.new_in_fleet,0),0)) new_in_fleet
         FROM
            opr_rbl_eng_inservice_mon
            LEFT JOIN (
                         -- current count
                          SELECT
                              operator_id,
                              operator_code,
                              assmbl_code,
                              original_assmbl_code engine_type,
                              COUNT(1) in_fleet
                          FROM
                             acor_rbl_eng_v1
                          GROUP BY
                              operator_id,
                              operator_code,
                              assmbl_code,
                              original_assmbl_code
                       )rvw_curr_infleet ON
               opr_rbl_eng_inservice_mon.operator_id  = rvw_curr_infleet.operator_id AND
               opr_rbl_eng_inservice_mon.fleet_type  = rvw_curr_infleet.assmbl_code AND
               opr_rbl_eng_inservice_mon.engine_type  = rvw_curr_infleet.engine_type
            LEFT JOIN ( -- count of engine that was added using the recevied and installed date
                        SELECT
                           operator_id,
                           operator_code,
                           assmbl_code,
                           original_assmbl_code engine_type,
                           TO_NUMBER(TO_CHAR(in_fleet_date,'YYYYMM')) year_month,
                           COUNT(1) new_in_fleet
                        FROM
                           acor_rbl_eng_v1
                        WHERE
                           in_fleet_date IS NOT NULL
                        GROUP BY
                           operator_id,
                           operator_code,
                           assmbl_code,
                           original_assmbl_code,
                           TO_NUMBER(TO_CHAR(in_fleet_date,'YYYYMM'))
                     ) rvw_in_fleet_dt ON
               opr_rbl_eng_inservice_mon.operator_id  = rvw_in_fleet_dt.operator_id AND
               opr_rbl_eng_inservice_mon.fleet_type  = rvw_in_fleet_dt.assmbl_code AND
               opr_rbl_eng_inservice_mon.engine_type  = rvw_in_fleet_dt.engine_type AND
               (opr_rbl_eng_inservice_mon.year_code || opr_rbl_eng_inservice_mon.month_code)  <= TO_CHAR(ADD_MONTHS(TO_DATE(rvw_in_fleet_dt.year_month,'YYYYMM'),-1),'YYYYMM')
         GROUP BY
            opr_rbl_eng_inservice_mon.operator_id,
            opr_rbl_eng_inservice_mon.operator_code,
            opr_rbl_eng_inservice_mon.fleet_type,
            opr_rbl_eng_inservice_mon.engine_type,
            opr_rbl_eng_inservice_mon.year_code,
            opr_rbl_eng_inservice_mon.month_code,
            TO_NUMBER(year_code || month_code),
            rvw_curr_infleet.in_fleet
      )
   WHERE 
      TRUNC(TO_DATE(year_month,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

    --
    ln_row_processed := ln_row_processed + SQL%ROWCOUNT;

   -- # engine usage
   --

   DELETE
   FROM
      opr_rbl_eng_usage_mon
   WHERE
      -- year and month input param
      TRUNC(TO_DATE(year_code||month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_usage_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        data_type_code,
        year_code,
        month_code,
        quantity
      )
   SELECT
      operator_id,
      operator_code,
      assmbl_code fleet_type,
      original_assmbl_code engine_type,
      data_type_code,
      TO_CHAR(usage_date,'YYYY') year_code,
      TO_CHAR(usage_date,'MM') month_code,
      SUM(tsn_delta_qt) tsn
   FROM
      opr_rbl_eng_usage_raw
   WHERE
      usage_type_code IN ('MXACCRUAL','MXFLIGHT')
      AND
      data_type_code IN ('HOURS','CYCLES')
      AND
      -- year and month input param
      TRUNC(usage_date) BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
      operator_id,
      operator_code,
      assmbl_code,
      original_assmbl_code,
      data_type_code,
      TO_CHAR(usage_date,'YYYY'),
      TO_CHAR(usage_date,'MM');

    --
    ln_row_processed := ln_row_processed + SQL%ROWCOUNT;


   -- # engine delays
   --
   DELETE
   FROM
      opr_rbl_eng_delay_mon
   WHERE
      -- year and month input param
      TRUNC(TO_DATE(year_code||month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_delay_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        quantity,
        cycles
      )
   SELECT
      eng_type.operator_id,
      eng_type.operator_code,
      eng_type.fleet_type,
      eng_type.engine_type,
      monthly_delay.year_code,
      monthly_delay.month_code,
      SUM(delayed_departures) quantity,
      eng_usage.quantity AS cycles
   FROM
      opr_rbl_monthly_delay monthly_delay
      INNER JOIN aopr_rbl_acft_eng_type_v1 eng_type ON
         monthly_delay.fleet_type    = eng_type.fleet_type AND
         monthly_delay.serial_number = eng_type.acft_serial_number
      INNER JOIN opr_rbl_delay_category  delay_category ON
         monthly_delay.delay_category_code = delay_category.delay_category_code
         AND -- only delays that are greater than 15 minutes
         delay_category.low_range > 15
       -- usage
       LEFT JOIN aopr_rbl_eng_usage_mon_mv1 eng_usage ON
          eng_usage.operator_id = eng_type.operator_id AND
          eng_usage.fleet_type  = eng_type.fleet_type AND
          eng_usage.engine_type = eng_type.engine_type AND
          eng_usage.year_code   = monthly_delay.year_code AND
          eng_usage.month_code  = monthly_delay.month_code
          AND
          eng_usage.data_type_code = 'CYCLES'
   WHERE -- ATA chapter from 71 to 80 excluding 78
      REGEXP_INSTR(monthly_delay.chapter_code,'^(7([1-7]|9)|80).*') > 0
      AND
      TRUNC(TO_DATE(monthly_delay.year_code||monthly_delay.month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date      
   GROUP BY
      eng_type.operator_id,
      eng_type.operator_code,
      eng_type.fleet_type,
      eng_type.engine_type,
      monthly_delay.year_code,
      monthly_delay.month_code,
      eng_usage.quantity;

    --
    ln_row_processed := ln_row_processed + SQL%ROWCOUNT;


   -- # engine indicators
   --
   DELETE
   FROM
      opr_rbl_eng_indicator_mon
   WHERE
      -- year and month input param
      TRUNC(TO_DATE(year_code||month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_indicator_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        oper_indicator,
        year_code,
        month_code,
        quantity
      )
   SELECT
      operator_id,
      operator_code,
      fleet_type,
      engine_type,
      oper_indicator,
      TO_CHAR(data_date,'YYYY')  AS year_code,
      TO_CHAR(data_date,'MM')    AS month_code,
      COUNT(1) qty
   FROM
      opr_rbl_eng_indicator_raw
   WHERE
      -- year and month input param
      TRUNC(data_date) BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
      operator_id,
      operator_code,
      fleet_type,
      engine_type,
      oper_indicator,
      TO_CHAR(data_date,'YYYY'),
      TO_CHAR(data_date,'MM')
   -- above data is using flight measurments
   UNION ALL
   -- to Engine Related Flight Cancellations
   SELECT
       operator_id,
       operator_code,
       fleet_type,
       engine_type,
       'Engine Related Flight Cancellation' oper_indicator,
       TO_CHAR(found_on_date,'YYYY')  AS year_code,
       TO_CHAR(found_on_date,'MM')  AS month_code,
       COUNT(1) qty
   FROM
      opr_rbl_eng_cancellation_raw
   WHERE
      -- year and month input param
      TRUNC(found_on_date) BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
       operator_id,
       operator_code,
       fleet_type,
       engine_type,
       TO_CHAR(found_on_date,'YYYY'),
       TO_CHAR(found_on_date,'MM')
   --
   UNION ALL
   -- to IFD and RTO resulting events
   SELECT
       operator_id,
       operator_code,
       fleet_type,
       engine_type,
       CASE
         WHEN incident_code = 'IFD' THEN
            'IFSD'
         ELSE
            'Reject T/O'
       END   AS oper_indicator,
       TO_CHAR(found_on_date,'YYYY')  AS year_code,
       TO_CHAR(found_on_date,'MM')  AS month_code,
       COUNT(1) qty
   FROM
      opr_rbl_eng_ifdrto_event_raw
   WHERE
      -- year and month input param
      TRUNC(found_on_date) BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
       operator_id,
       operator_code,
       fleet_type,
       engine_type,
       incident_code,
       TO_CHAR(found_on_date,'YYYY'),
       TO_CHAR(found_on_date,'MM');


    --
    ln_row_processed := ln_row_processed + SQL%ROWCOUNT;


   -- # engine shopvisits
   --
   DELETE
   FROM
      opr_rbl_eng_shopvisit_mon
   WHERE
      -- year and month input param
      TRUNC(TO_DATE(year_code||month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_shopvisit_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        quantity
      )
   SELECT
      operator_id,
      operator_code,
      fleet_type,
      engine_type,
      TO_CHAR(completed_date,'YYYY')   AS year_code,
      TO_CHAR(completed_date,'MM')     AS month_code,
      COUNT(1) qty
   FROM
      opr_rbl_eng_shopvisit_raw
   WHERE
      -- year and month input param
      TRUNC(completed_date) BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
      operator_id,
      operator_code,
      fleet_type,
      engine_type,
      TO_CHAR(completed_date,'YYYY'),
      TO_CHAR(completed_date,'MM');

    --
    ln_row_processed := ln_row_processed + SQL%ROWCOUNT;

   -- # engine unplanned removals
   --
   DELETE
   FROM
      opr_rbl_eng_urmvl_mon
   WHERE
      -- year and month input param
      TRUNC(TO_DATE(year_code||month_code,'YYYYMM')) BETWEEN aidt_start_date AND aidt_end_date;

   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_urmvl_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        quantity
      )
   SELECT
      acor_rbl_eng_v1.operator_id,
      acor_rbl_eng_v1.operator_code,
      acor_rbl_eng_v1.assmbl_code     AS fleet_type,
      acor_rbl_eng_v1.original_assmbl_code AS engine_type,
       TO_CHAR(removal_date,'YYYY')   AS year_code,
       TO_CHAR(removal_date,'MM')     AS month_code,
      COUNT(1) rmvl_count
   FROM
      opr_rbl_comp_rmvl_raw
      INNER JOIN acor_rbl_eng_v1 ON
         opr_rbl_comp_rmvl_raw.inventory_id = acor_rbl_eng_v1.inventory_id
   WHERE -- only assembly removal from comp removal raw data
      opr_rbl_comp_rmvl_raw.inventory_class = 'ASSY'
      AND
      opr_rbl_comp_rmvl_raw.removal_type = 'U'
      AND
      -- year and month input param
      TRUNC(removal_date) BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
       acor_rbl_eng_v1.operator_id,
      acor_rbl_eng_v1.operator_code,
      acor_rbl_eng_v1.assmbl_code,
      acor_rbl_eng_v1.original_assmbl_code,
       TO_CHAR(removal_date,'YYYY'),
       TO_CHAR(removal_date,'MM');

    --
    ln_row_processed := ln_row_processed + SQL%ROWCOUNT;

    -- refresh relevant materialized view
   ln_return := opr_rbl_utility_pkg.opr_refresh_mview( aiv_category_or_frequency => 'ENGINE',
                                                       aiv_refresh_type_code     => 'CATEGORY'
                                                     );
    --
    RETURN ln_row_processed;


END opr_rbl_eng_xform;

/******************************************************************************
*  Function:    opr_rbl_hist_eng_xform
*
*  Return:       ln_row_processed - number of processed rows
*
*  Description:  Function that will transform the legacy engine operation data
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  26-Mar-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_hist_eng_xform
RETURN NUMBER
AS

  ln_return    NUMBER;
BEGIN

   -- # for ITS, only monthly data were provided
   -- the transformation is simply refreshing the materialized view

   -- # delay will be treated differently due to the dependency onthe monthly disruption
   DELETE
   FROM
      opr_rbl_eng_delay_mon;

   --
   INSERT /*+ APPEND */
   INTO
      opr_rbl_eng_delay_mon
      (
        operator_id,
        operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        quantity,
        cycles
      )
   SELECT
      eng_type.operator_id,
      eng_type.operator_code,
      eng_type.fleet_type,
      eng_type.engine_type,
      monthly_delay.year_code,
      monthly_delay.month_code,
      SUM(delayed_departures) quantity,
      eng_usage.quantity AS cycles
   FROM
      opr_rbl_monthly_delay monthly_delay
      INNER JOIN aopr_rbl_acft_eng_type_v1 eng_type ON
         monthly_delay.fleet_type    = eng_type.fleet_type AND
         monthly_delay.serial_number = eng_type.acft_serial_number
      INNER JOIN opr_rbl_delay_category delay_category ON
         monthly_delay.delay_category_code = delay_category.delay_category_code
         AND -- only delays that are greater than 15 minutes
         delay_category.low_range > 15
       -- usage
       LEFT JOIN aopr_rbl_eng_usage_mon_mv1 eng_usage ON
          eng_usage.operator_id = eng_type.operator_id AND
          eng_usage.fleet_type  = eng_type.fleet_type AND
          eng_usage.engine_type = eng_type.engine_type AND
          eng_usage.year_code   = monthly_delay.year_code AND
          eng_usage.month_code  = monthly_delay.month_code
          AND
          eng_usage.data_type_code = 'CYCLES'
   WHERE -- ATA chapter from 71 to 80 excluding 78
      REGEXP_INSTR(monthly_delay.chapter_code,'^(7([1-7]|9)|80).*') > 0
   GROUP BY
   eng_type.operator_id,
      eng_type.operator_code,
      eng_type.fleet_type,
      eng_type.engine_type,
      monthly_delay.year_code,
      monthly_delay.month_code,
      eng_usage.quantity;

   -- refresh relevant materialized view
   ln_return := opr_rbl_utility_pkg.opr_refresh_mview( aiv_category_or_frequency => 'ENGINE',
                                                       aiv_refresh_type_code     => 'CATEGORY'
                                                     );


   RETURN ln_return;

END opr_rbl_hist_eng_xform;

--
END opr_rbl_eng_pkg;
/