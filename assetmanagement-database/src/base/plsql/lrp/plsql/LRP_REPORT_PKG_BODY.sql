--liquibase formatted sql


--changeSet LRP_REPORT_PKG_BODY:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY LRP_REPORT_PKG IS
/********************************************************************************
*
* Package:     LRP_REPORT_PKG (BODY)
* Description: For a description of this package, see the spec
*
*********************************************************************************/

   /********************************************************************************1
   *
   * Function:    IsRefreshedSinceLastSave
   * Description: This function is used to determine if the report table needs to
   *              be reloaded because it has not been refreshed since the last save
   *
   * Parameters:  av_TableName - The name of the table that you are checking, for
   *                 example 'LRP_REPORT_PART_FORECAST'
   *              an_LrpPlanDbId, an_LrpPlanId - This is the ID of the saved LRP
   *                 plan that is being loaded into the reporting table. Only
   *                 one LRP Plan can be loaded at a time with this procedure,
   *                 although this table can hold the data for many LRP Plans
   *                 at once.
   * Return:      NUMBER - returns 1 if the report needs to be reloaded. Returns
   *                 0 if it does not.
   *
   *********************************************************************************/
   FUNCTION IsRefreshedSinceLastSave (
         av_TableName         IN varchar2,
         an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
         an_LrpPlanId         lrp_plan.lrp_id%TYPE
         ) RETURN NUMBER;


   /********************************************************************************
   *
   * Procedure Body: LOAD_PART_FORECAST
   *
   *********************************************************************************/
   PROCEDURE LOAD_PART_FORECAST(
      an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
      an_LrpPlanId         lrp_plan.lrp_id%TYPE,
      on_Return            OUT NUMBER
      ) IS

      /* local variables */
      lcv_TableName CONSTANT VARCHAR2(40) := 'LRP_REPORT_PART_FORECAST'; /* used with the lrp_report_load_control table */
      ln_IsRefreshed NUMBER; /* used to check whether the table needs to be updated */

   BEGIN

      /* If the procedure doesn't finish properly, return a -1 error code. We will set
         this to 1 before exiting if it works */
      on_Return := -1;

      /* If the table has already been loaded since the plan was last saved, then do
         nothing */
      ln_IsRefreshed := IsRefreshedSinceLastSave ( lcv_TableName, an_LrpPlanDbId, an_LrpPlanId );
      IF ln_IsRefreshed = 1 THEN
         on_Return := 1;
         RETURN;
      END IF;

      /* Clear out the table for this plan */
      DELETE FROM lrp_report_part_forecast
      WHERE plan_db_id = an_LrpPlanDbId AND plan_id = an_LrpPlanId;

      /* *** Insert into the lrp_report_part_forecast table all of the parts needed by this plan *** */
      /* Use the "APPEND" hint to improve performance of the mass insert in Oracle */
      INSERT /*+ APPEND */ INTO lrp_report_part_forecast (
         plan_db_id,
         plan_id,
         plan_sdesc,
         event_sdesc,
         event_start_dt,
         event_end_dt,
         event_duration_days,
         loc_cd,
         workscope_cd,
         workscope_name,
         workscope_deadline_dt,
         workscope_man_hours,
         workscope_yield_pct,
         part_group_cd,
         part_group_name,
         part_qty,
         ac_reg_cd,
         ac_serial_no_oem,
         ac_assmbl_cd,
         ac_assmbl_subtype_cd,
         ac_carrier_cd
      )
         /* This query will return all of the parts needed for out-of-phase REQS (not in BLOCKS) */
         SELECT
            /* plan information */
            lrp_plan.lrp_db_id AS plan_db_id,
            lrp_plan.lrp_id AS plan_id,
            lrp_plan.desc_sdesc AS plan_sdesc,

            /* event information */
            lrp_event.lrp_sdesc AS event_sdesc,
            lrp_event.start_dt AS event_start_dt,
            lrp_event.end_dt AS event_end_dt,
            lrp_event.end_dt - lrp_event.start_dt AS event_duration_days,

            /* location information */
            inv_loc.loc_cd AS loc_cd,

            /* workscope information */
            task_task.task_cd AS workscope_cd,
            task_task.task_name AS workscope_name,
            lrp_event_workscope.deadline_dt AS workscope_deadline_dt,
            lrp_event_workscope.man_hours AS workscope_man_hours,
            lrp_event_workscope.yield_pct AS workscope_yield_pct,

            /* part information */
            eqp_bom_part.bom_part_cd AS part_group_cd,
            eqp_bom_part.bom_part_name AS part_group_name,
            task_part_list.req_qt AS part_qty,

            /* if this is an existing aircraft, you must look in INV_INV for all of the attributes
               If this is a "future" aircraft, you must look in LRP_INV_INV for all of the attributes */
            NVL( inv_ac_reg.ac_reg_cd, lrp_inv_inv.ac_reg_cd ) AS ac_reg_cd,
            NVL( inv_inv.serial_no_oem, lrp_inv_inv.serial_no_oem ) AS ac_serial_no_oem,
            NVL( inv_inv.assmbl_cd, lrp_inv_inv.assmbl_cd ) AS ac_assmbl_cd,
            NVL( eas1.subtype_cd, eas2.subtype_cd ) AS ac_assmbl_subtype_cd,
            NVL( oc1.carrier_cd, oc2.carrier_cd ) AS ac_carrier_cd

         FROM
            lrp_plan

            /* find all events in the plan */
            INNER JOIN lrp_event ON
               lrp_event.lrp_db_id = lrp_plan.lrp_db_id AND
               lrp_event.lrp_id = lrp_plan.lrp_id

            /* find the location for those events */
            LEFT OUTER JOIN inv_loc ON
               inv_loc.loc_db_id = lrp_event.lrp_loc_db_id AND
               inv_loc.loc_id = lrp_event.lrp_loc_id

            /* if this is an existing aircraft, you must look in INV_INV for all of the attributes
               If this is a "future" aircraft, you must look in LRP_INV_INV for all of the attributes */
            INNER JOIN lrp_inv_inv ON
               lrp_inv_inv.lrp_db_id = lrp_event.lrp_db_id AND
               lrp_inv_inv.lrp_id = lrp_event.lrp_id AND
               lrp_inv_inv.lrp_inv_inv_id = lrp_event.lrp_inv_inv_id
            LEFT OUTER JOIN inv_inv ON
               inv_inv.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
               inv_inv.inv_no_id = lrp_inv_inv.inv_no_id
            LEFT OUTER JOIN inv_ac_reg ON
               inv_ac_reg.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
               inv_ac_reg.inv_no_id = lrp_inv_inv.inv_no_id
            LEFT OUTER JOIN eqp_assmbl_subtype eas1 ON
               eas1.assmbl_subtype_db_id = inv_inv.assmbl_subtype_db_id AND
               eas1.assmbl_subtype_id = inv_inv.assmbl_subtype_id
            LEFT OUTER JOIN eqp_assmbl_subtype eas2 ON
               eas2.assmbl_subtype_db_id = lrp_inv_inv.assmbl_subtype_db_id AND
               eas2.assmbl_subtype_id = lrp_inv_inv.assmbl_subtype_id
            LEFT OUTER JOIN org_carrier oc1 ON
               oc1.carrier_db_id = inv_inv.carrier_db_id AND
               oc1.carrier_id = inv_inv.carrier_id
            LEFT OUTER JOIN org_carrier oc2 ON
               oc2.carrier_db_id = lrp_inv_inv.carrier_db_id AND
               oc2.carrier_id = lrp_inv_inv.carrier_id

            /* join to the task definitions that are in the plan, always join to the active task definition */
            INNER JOIN lrp_event_workscope ON
               lrp_event_workscope.lrp_event_db_id = lrp_event.lrp_event_db_id AND
               lrp_event_workscope.lrp_event_id = lrp_event.lrp_event_id
            INNER JOIN task_defn ON
               task_defn.task_defn_db_id = lrp_event_workscope.task_defn_db_id AND
               task_defn.task_defn_id = lrp_event_workscope.task_defn_id
            INNER JOIN task_task ON
               task_task.task_defn_db_id = task_defn.task_defn_db_id AND
               task_task.task_defn_id = task_defn.task_defn_id AND
               task_task.task_def_status_cd = 'ACTV'

            /* find the list of part groups for the task definitions */
            INNER JOIN task_part_list ON
               task_part_list.task_db_id = task_task.task_db_id AND
               task_part_list.task_id = task_task.task_id
            INNER JOIN eqp_bom_part ON
               eqp_bom_part.bom_part_db_id = task_part_list.bom_part_db_id AND
               eqp_bom_part.bom_part_id = task_part_list.bom_part_id

         WHERE
            lrp_plan.lrp_db_id = an_LrpPlanDbId AND
            lrp_plan.lrp_id    = an_LrpPlanId

         UNION ALL

         /* This query will return all of the parts needed for REQS that are nested under BLOCKS */
         SELECT
            /* plan information */
            lrp_plan.lrp_db_id AS plan_db_id,
            lrp_plan.lrp_id AS plan_id,
            lrp_plan.desc_sdesc AS plan_sdesc,

            /* event information */
            lrp_event.lrp_sdesc AS event_sdesc,
            lrp_event.start_dt AS event_start_dt,
            lrp_event.end_dt AS event_end_dt,
            lrp_event.end_dt - lrp_event.start_dt AS event_duration_days,

            /* location information */
            inv_loc.loc_cd AS loc_cd,

            /* workscope information */
            task_task.task_cd AS workscope_cd,
            task_task.task_name AS workscope_name,
            lrp_event_workscope.deadline_dt AS workscope_deadline_dt,
            lrp_event_workscope.man_hours AS workscope_man_hours,
            lrp_event_workscope.yield_pct AS workscope_yield_pct,

            /* part information */
            eqp_bom_part.bom_part_cd AS part_group_cd,
            eqp_bom_part.bom_part_name AS part_group_name,
            task_part_list.req_qt AS part_qty,

            /* if this is an existing aircraft, you must look in INV_INV for all of the attributes
               If this is a "future" aircraft, you must look in LRP_INV_INV for all of the attributes */
            NVL( inv_ac_reg.ac_reg_cd, lrp_inv_inv.ac_reg_cd ) AS ac_reg_cd,
            NVL( inv_inv.serial_no_oem, lrp_inv_inv.serial_no_oem ) AS ac_serial_no_oem,
            NVL( inv_inv.assmbl_cd, lrp_inv_inv.assmbl_cd ) AS ac_assmbl_cd,
            NVL( eas1.subtype_cd, eas2.subtype_cd ) AS ac_assmbl_subtype_cd,
            NVL( oc1.carrier_cd, oc2.carrier_cd ) AS ac_carrier_cd

         FROM
            lrp_plan

            /* find all events in the plan */
            INNER JOIN lrp_event ON
               lrp_event.lrp_db_id = lrp_plan.lrp_db_id AND
               lrp_event.lrp_id = lrp_plan.lrp_id

            /* find the location for those events */
            LEFT OUTER JOIN inv_loc ON
               inv_loc.loc_db_id = lrp_event.lrp_loc_db_id AND
               inv_loc.loc_id = lrp_event.lrp_loc_id

            /* if this is an existing aircraft, you must look in INV_INV for all of the attributes
               If this is a "future" aircraft, you must look in LRP_INV_INV for all of the attributes */
            INNER JOIN lrp_inv_inv ON
               lrp_inv_inv.lrp_db_id = lrp_event.lrp_db_id AND
               lrp_inv_inv.lrp_id = lrp_event.lrp_id AND
               lrp_inv_inv.lrp_inv_inv_id = lrp_event.lrp_inv_inv_id
            LEFT OUTER JOIN inv_inv ON
               inv_inv.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
               inv_inv.inv_no_id = lrp_inv_inv.inv_no_id
            LEFT OUTER JOIN inv_ac_reg ON
               inv_ac_reg.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
               inv_ac_reg.inv_no_id = lrp_inv_inv.inv_no_id
            LEFT OUTER JOIN eqp_assmbl_subtype eas1 ON
               eas1.assmbl_subtype_db_id = inv_inv.assmbl_subtype_db_id AND
               eas1.assmbl_subtype_id = inv_inv.assmbl_subtype_id
            LEFT OUTER JOIN eqp_assmbl_subtype eas2 ON
               eas2.assmbl_subtype_db_id = lrp_inv_inv.assmbl_subtype_db_id AND
               eas2.assmbl_subtype_id = lrp_inv_inv.assmbl_subtype_id
            LEFT OUTER JOIN org_carrier oc1 ON
               oc1.carrier_db_id = inv_inv.carrier_db_id AND
               oc1.carrier_id = inv_inv.carrier_id
            LEFT OUTER JOIN org_carrier oc2 ON
               oc2.carrier_db_id = lrp_inv_inv.carrier_db_id AND
               oc2.carrier_id = lrp_inv_inv.carrier_id

            /* join to the task definitions that are in the plan, always join to the active task definition */
            INNER JOIN lrp_event_workscope ON
               lrp_event_workscope.lrp_event_db_id = lrp_event.lrp_event_db_id AND
               lrp_event_workscope.lrp_event_id = lrp_event.lrp_event_id
            INNER JOIN task_defn ON
               task_defn.task_defn_db_id = lrp_event_workscope.task_defn_db_id AND
               task_defn.task_defn_id = lrp_event_workscope.task_defn_id
            INNER JOIN task_task ON
               task_task.task_defn_db_id = task_defn.task_defn_db_id AND
               task_task.task_defn_id = task_defn.task_defn_id AND
               task_task.task_def_status_cd = 'ACTV'

            /* if the workscope item is a BLOCK, then you will also need to join to the requirements under that block */
            INNER JOIN task_block_req_map ON
               task_block_req_map.block_task_db_id = task_task.task_db_id AND
               task_block_req_map.block_task_id = task_task.task_id
            INNER JOIN task_task req_task_defn ON
               req_task_defn.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
               req_task_defn.task_defn_id = task_block_req_map.req_task_defn_id
            INNER JOIN task_task req_task_task ON
               req_task_task.task_defn_db_id = req_task_defn.task_defn_db_id AND
               req_task_task.task_defn_id = req_task_defn.task_defn_id AND
               req_task_task.task_def_status_cd = 'ACTV'

            /* find the list of part groups for the task definitions */
            INNER JOIN task_part_list ON
               task_part_list.task_db_id = req_task_task.task_db_id AND
               task_part_list.task_id = req_task_task.task_id
            INNER JOIN eqp_bom_part ON
               eqp_bom_part.bom_part_db_id = task_part_list.bom_part_db_id AND
               eqp_bom_part.bom_part_id = task_part_list.bom_part_id

         WHERE
            lrp_plan.lrp_db_id = an_LrpPlanDbId AND
            lrp_plan.lrp_id    = an_LrpPlanId;

      /* indicate that the report table has been successfully refreshed */
      UPDATE lrp_report_load_control
      SET last_load_dt = SYSDATE
      WHERE
         report_table = lcv_TableName AND
         plan_db_id   = an_LrpPlanDbId AND
         plan_id      = an_LrpPlanId;


      /* The procedure completed successfully */
      on_Return := 1;

   END LOAD_PART_FORECAST;


   /********************************************************************************
   *
   * Procedure Body: LOAD_SCHEDULE
   *
   *********************************************************************************/
   PROCEDURE LOAD_SCHEDULE(
      an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
      an_LrpPlanId         lrp_plan.lrp_id%TYPE,
      on_Return            OUT NUMBER
      ) IS

      /* local variables */
      lcv_TableName CONSTANT VARCHAR2(40) := 'LRP_REPORT_SCHEDULE'; /* used with the lrp_report_load_control table */
      ln_IsRefreshed NUMBER; /* used to check whether the table needs to be updated */

   BEGIN

      /* If the procedure doesn't finish properly, return a -1 error code. We will set
         this to 1 before exiting if it works */
      on_Return := -1;

      /* If the table has already been loaded since the plan was last saved, then do
         nothing */
      ln_IsRefreshed := IsRefreshedSinceLastSave ( lcv_TableName, an_LrpPlanDbId, an_LrpPlanId );
      IF ln_IsRefreshed = 1 THEN
         on_Return := 1;
         RETURN;
      END IF;

      /* Clear out the table for this plan */
      DELETE FROM lrp_report_schedule
      WHERE plan_db_id = an_LrpPlanDbId AND plan_id = an_LrpPlanId;

      /* *** Insert into the lrp_report_schedule table all of the parts needed by this plan *** */
      /* Use the "APPEND" hint to improve performance of the mass insert in Oracle */
      INSERT /*+ APPEND */ INTO lrp_report_schedule (
         plan_db_id,
         plan_id,
         plan_sdesc,
         event_sdesc,
         event_start_dt,
         event_end_dt,
         event_duration_days,
         loc_cd,
         workscope_cd,
         workscope_name,
         workscope_deadline_dt,
         workscope_man_hours,
         workscope_yield_pct,
         ac_reg_cd,
         ac_serial_no_oem,
         ac_assmbl_cd,
         ac_assmbl_subtype_cd,
         ac_carrier_cd
      )
         /* This query will return all of the workscope items in the plan */
         SELECT
            /* plan information */
            lrp_plan.lrp_db_id AS plan_db_id,
            lrp_plan.lrp_id AS plan_id,
            lrp_plan.desc_sdesc AS plan_sdesc,

            /* event information */
            lrp_event.lrp_sdesc AS event_sdesc,
            lrp_event.start_dt AS event_start_dt,
            lrp_event.end_dt AS event_end_dt,
            lrp_event.end_dt - lrp_event.start_dt AS event_duration_days,

            /* location information */
            inv_loc.loc_cd AS loc_cd,

            /* workscope information */
            task_task.task_cd AS workscope_cd,
            task_task.task_name AS workscope_name,
            lrp_event_workscope.deadline_dt AS workscope_deadline_dt,
            lrp_event_workscope.man_hours AS workscope_man_hours,
            lrp_event_workscope.yield_pct AS workscope_yield_pct,

            /* if this is an existing aircraft, you must look in INV_INV for all of the attributes
               If this is a "future" aircraft, you must look in LRP_INV_INV for all of the attributes */
            NVL( inv_ac_reg.ac_reg_cd, lrp_inv_inv.ac_reg_cd ) AS ac_reg_cd,
            NVL( inv_inv.serial_no_oem, lrp_inv_inv.serial_no_oem ) AS ac_serial_no_oem,
            NVL( inv_inv.assmbl_cd, lrp_inv_inv.assmbl_cd ) AS ac_assmbl_cd,
            NVL( eas1.subtype_cd, eas2.subtype_cd ) AS ac_assmbl_subtype_cd,
            NVL( oc1.carrier_cd, oc2.carrier_cd ) AS ac_carrier_cd

         FROM
            lrp_plan

            /* find all events in the plan */
            INNER JOIN lrp_event ON
               lrp_event.lrp_db_id = lrp_plan.lrp_db_id AND
               lrp_event.lrp_id = lrp_plan.lrp_id

            /* find the location for those events */
            LEFT OUTER JOIN inv_loc ON
               inv_loc.loc_db_id = lrp_event.lrp_loc_db_id AND
               inv_loc.loc_id = lrp_event.lrp_loc_id

            /* find the aircraft for those events
               if this is an existing aircraft, you must look in INV_INV for all of the attributes
               If this is a "future" aircraft, you must look in LRP_INV_INV for all of the attributes */
            INNER JOIN lrp_inv_inv ON
               lrp_inv_inv.lrp_db_id = lrp_event.lrp_db_id AND
               lrp_inv_inv.lrp_id = lrp_event.lrp_id AND
               lrp_inv_inv.lrp_inv_inv_id = lrp_event.lrp_inv_inv_id
            LEFT OUTER JOIN inv_inv ON
               inv_inv.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
               inv_inv.inv_no_id = lrp_inv_inv.inv_no_id
            LEFT OUTER JOIN inv_ac_reg ON
               inv_ac_reg.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
               inv_ac_reg.inv_no_id = lrp_inv_inv.inv_no_id
            LEFT OUTER JOIN eqp_assmbl_subtype eas1 ON
               eas1.assmbl_subtype_db_id = inv_inv.assmbl_subtype_db_id AND
               eas1.assmbl_subtype_id = inv_inv.assmbl_subtype_id
            LEFT OUTER JOIN eqp_assmbl_subtype eas2 ON
               eas2.assmbl_subtype_db_id = lrp_inv_inv.assmbl_subtype_db_id AND
               eas2.assmbl_subtype_id = lrp_inv_inv.assmbl_subtype_id
            LEFT OUTER JOIN org_carrier oc1 ON
               oc1.carrier_db_id = inv_inv.carrier_db_id AND
               oc1.carrier_id = inv_inv.carrier_id
            LEFT OUTER JOIN org_carrier oc2 ON
               oc2.carrier_db_id = lrp_inv_inv.carrier_db_id AND
               oc2.carrier_id = lrp_inv_inv.carrier_id

            /* find the workscope items for those events
               join to the task definitions that are in the plan, always join to the active task definition */
            INNER JOIN lrp_event_workscope ON
               lrp_event_workscope.lrp_event_db_id = lrp_event.lrp_event_db_id AND
               lrp_event_workscope.lrp_event_id = lrp_event.lrp_event_id
            INNER JOIN task_defn ON
               task_defn.task_defn_db_id = lrp_event_workscope.task_defn_db_id AND
               task_defn.task_defn_id = lrp_event_workscope.task_defn_id
            INNER JOIN task_task ON
               task_task.task_defn_db_id = task_defn.task_defn_db_id AND
               task_task.task_defn_id = task_defn.task_defn_id AND
               task_task.task_def_status_cd = 'ACTV'

         WHERE
            lrp_plan.lrp_db_id = an_LrpPlanDbId AND
            lrp_plan.lrp_id    = an_LrpPlanId;

      /* The procedure completed successfully */
      on_Return := 1;

   END LOAD_SCHEDULE;


   /********************************************************************************
   *
   * Procedure Body: LOAD_SILT
   *
   *********************************************************************************/
   PROCEDURE LOAD_SILT(
      an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
      an_LrpPlanId         lrp_plan.lrp_id%TYPE,
      on_Return            OUT NUMBER
      ) IS

      /* local variables */
      lcv_TableName CONSTANT VARCHAR2(40) := 'LRP_REPORT_SILT'; /* used with the lrp_report_load_control table */
      ln_IsRefreshed NUMBER; /* used to check whether the table needs to be updated */
      ldt_PlanStartDt lrp_plan.updated_dt%TYPE; /* The first day in the plan, the report will statr from here */
      lv_PlanSdesc lrp_plan.desc_sdesc%TYPE; /* this is used to store the name of the plan */
      ldt_LoopDt DATE; /* we will insert a row for every day in the plan, this is the day being processed */

      /* Create an array to hold the daily status of every aircraft, and index it by the aircraft's ID
         The status will be an enumerated type, defined by constants */
      TYPE AIRCRAFT_RECORD IS RECORD ( status NUMBER, num_events NUMBER, num_overflow NUMBER );
      TYPE AIRCRAFT_LIST IS TABLE OF AIRCRAFT_RECORD INDEX BY PLS_INTEGER;
      lrec_AircraftList AIRCRAFT_LIST;
      lcn_StatusAvail CONSTANT NUMBER := 1;
      lcn_StatusInMaint CONSTANT NUMBER := 2; /* event scheduled in a real location */
      lcn_StatusNotInduct CONSTANT NUMBER := 3;
      lcn_StatusRetired CONSTANT NUMBER := 4;
      lcn_StatusOverflow CONSTANT NUMBER := 5; /* event schedule in an overflow location */

      /* maintain the current counts of the various status of aircraft */
      ln_CountAvail NUMBER := 0;
      ln_CountInMaint NUMBER := 0; /* scheduled at a real location */
      ln_CountOverflow NUMBER := 0; /* scheduled in overflow */
      ln_CountNotInduct NUMBER := 0;
      ln_CountRetired NUMBER := 0;

      /* This variable is used to prepare all of the records that will be inserted into the
         LRP_REPORT_SILT table. Then we can do a bulk insert at the end */
      TYPE report_insert IS TABLE OF lrp_report_silt%ROWTYPE;
      lrec_ReportInsert report_insert := report_insert();
      ln_Index NUMBER := 0; /* This is the current index into the array of the lrec_ReportInsert table */

      /* this query gets the list of aircraft in the plan */
      CURSOR lcur_PlanInventory (
            an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
            an_LrpPlanId         lrp_plan.lrp_id%TYPE
         ) IS
         SELECT
            lrp_inv_inv_id,
            induction_dt,
            retirement_dt
         FROM lrp_inv_inv
         WHERE
            lrp_db_id = an_LrpPlanDbId AND
            lrp_id    = an_LrpPlanId;

      /* this query gets the list of all events in the plan. We will use an enumerated type
         (constant number) to indicate what type of event it is */
      lcn_EventStart CONSTANT NUMBER := 1; /* start of an event in a real location */
      lcn_EventEnd CONSTANT NUMBER := 2; /* end of an event in a real location */
      lcn_EventInduct CONSTANT NUMBER := 3;
      lcn_EventRetire CONSTANT NUMBER := 4;
      lcn_EventOverflowStart CONSTANT NUMBER := 5; /* start of an event in overflow */
      lcn_EventOverflowEnd CONSTANT NUMBER := 6; /* end of an event in overflow */
      CURSOR lcur_PlanEvent (
            an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
            an_LrpPlanId         lrp_plan.lrp_id%TYPE,
            adt_PlanStartDt      lrp_plan.updated_dt%TYPE
         ) IS

         /* retrieve all event starts */
         SELECT
            DECODE( lrp_loc_db_id, NULL, lcn_EventOverflowStart, lcn_EventStart ) AS event_type, /* indicates whether this event is scheduled in an overflow location */
            lrp_inv_inv_id,
            start_dt AS event_dt
         FROM lrp_event
         WHERE
            lrp_db_id = an_LrpPlanDbId AND
            lrp_id    = an_LrpPlanId

         /* retrieve all event ends */
         UNION ALL
         SELECT
            DECODE( lrp_loc_db_id, NULL, lcn_EventOverflowEnd, lcn_EventEnd ) AS event_type, /* indicates whether this event is scheduled in an overflow location */
            lrp_inv_inv_id,
            end_dt + 1 AS event_dt /* mark end dates on the following day, so that the aircraft looks like it is maintenance on the day it ends */
         FROM lrp_event
         WHERE
            lrp_db_id = an_LrpPlanDbId AND
            lrp_id    = an_LrpPlanId

         /* retrieve all induction events */
         UNION ALL
         SELECT
            lcn_EventInduct AS event_type,
            lrp_inv_inv_id,
            induction_dt AS event_dt
         FROM lrp_inv_inv
         WHERE
            lrp_db_id = an_LrpPlanDbId AND
            lrp_id    = an_LrpPlanId AND
            induction_dt >= adt_PlanStartDt

         /* retrieve all retirement events */
         UNION ALL
         SELECT
            lcn_EventRetire AS event_type,
            lrp_inv_inv_id,
            retirement_dt AS event_dt
         FROM lrp_inv_inv
         WHERE
            lrp_db_id = an_LrpPlanDbId AND
            lrp_id    = an_LrpPlanId AND
            retirement_dt >= adt_PlanStartDt

         /* order the events by their date
            in certain circumstances, start and end dates are the same so order by event type next */
         ORDER BY
            3, 1;
      lrec_PlanEvent lcur_PlanEvent%ROWTYPE;


   BEGIN

      /* If the procedure doesn't finish properly, return a -1 error code. We will set
         this to 1 before exiting if it works */
      on_Return := -1;

      /* If the table has already been loaded since the plan was last saved, then do nothing */
      ln_IsRefreshed := IsRefreshedSinceLastSave ( lcv_TableName, an_LrpPlanDbId, an_LrpPlanId );
      IF ln_IsRefreshed = 1 THEN
         on_Return := 1;
         RETURN;
      END IF;

      /* Clear out the table for this plan */
      DELETE FROM lrp_report_silt
      WHERE plan_db_id = an_LrpPlanDbId AND plan_id = an_LrpPlanId;

      -- get the "start date" of the plan
      SELECT trunc(updated_dt, 'DD'), desc_sdesc INTO ldt_PlanStartDt, lv_PlanSdesc
      FROM lrp_plan
      WHERE
         lrp_db_id = an_LrpPlanDbId AND
         lrp_id    = an_LrpPlanId;

      /* Initialize the lrec_AircraftList array. There will be one entry for every aircraft, indexed by
         the lrp_inv_inv_id and the value will be the status of "avail", "not inducted yet" or "retired" */
      FOR lrec_PlanInventory IN lcur_PlanInventory( an_LrpPlanDbId, an_LrpPlanId ) LOOP

         /* start with no events on the aircraft */
         lrec_AircraftList(lrec_PlanInventory.Lrp_Inv_Inv_Id).num_events := 0;
         lrec_AircraftList(lrec_PlanInventory.Lrp_Inv_Inv_Id).num_overflow := 0;

         -- the aircraft has not been inducted yet
         IF lrec_PlanInventory.induction_dt >= ldt_PlanStartDt THEN
            lrec_AircraftList(lrec_PlanInventory.Lrp_Inv_Inv_Id).status := lcn_StatusNotInduct;
            ln_CountNotInduct := ln_CountNotInduct + 1;

         -- the aircraft was retired before the plan started
         ELSIF lrec_PlanInventory.Retirement_Dt < ldt_PlanStartDt THEN
            lrec_AircraftList(lrec_PlanInventory.Lrp_Inv_Inv_Id).status := lcn_StatusRetired;
            ln_CountRetired := ln_CountRetired + 1;

         -- if neither of those, then start as available
         ELSE
            lrec_AircraftList(lrec_PlanInventory.Lrp_Inv_Inv_Id).status := lcn_StatusAvail;
            ln_CountAvail := ln_CountAvail + 1;
         END IF;
      END LOOP;

      -- retrieve the list of all events from LRP, including induction and retirement of aircraft
      OPEN lcur_PlanEvent( an_LrpPlanDbId, an_LrpPlanId, ldt_PlanStartDt );
      FETCH lcur_PlanEvent INTO lrec_PlanEvent;

      -- loop through every day in the plan and continue until you reach the last event
      ldt_LoopDt := ldt_PlanStartDt;
      WHILE NOT lcur_PlanEvent%NOTFOUND LOOP

         -- loop through every event that occured on this day
         WHILE lrec_PlanEvent.event_dt < ldt_LoopDt + 1 AND NOT lcur_PlanEvent%NOTFOUND LOOP

            /* Based on the type of event that occurred, update the status of the aircraft and adjust the counts */
            CASE lrec_PlanEvent.Event_Type

               /* *** START OF AN EVENT */
               WHEN lcn_EventStart THEN

                  /* Find out the aircraft's current status and adjust the counts accordingly */
                  CASE lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status

                     /* if available, then mark as in maintenance */
                     WHEN lcn_StatusAvail THEN
                        ln_CountAvail := ln_CountAvail - 1;
                        ln_CountInMaint := ln_CountInMaint + 1;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusInMaint;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events := 1;

                     /* if already in maintenance, then increment the number of events */
                     WHEN lcn_StatusInMaint THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events + 1;

                     /* if in overflow, then mark as in maintenance */
                     WHEN lcn_StatusOverflow THEN
                        ln_CountOverflow := ln_CountOverflow - 1;
                        ln_CountInMaint := ln_CountInMaint + 1;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusInMaint;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events := 1;

                     /* if not inducted yet, then do not change status but indicate that there is an ongoing event */
                     WHEN lcn_StatusNotInduct THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events + 1;

                     /* if retired, then do nothing */
                     WHEN lcn_StatusRetired THEN NULL;

                  END CASE;

               /* *** END OF AN EVENT */
               WHEN lcn_EventEnd THEN

                  /* Find out the aircraft's current status and adjust the counts accordingly */
                  CASE lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status

                     /* if in maintenance, decrement the number of events. If no more ongoing events, the aircraft it is available */
                     WHEN lcn_StatusInMaint THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events - 1;
                        IF lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events = 0 THEN
                           ln_CountInMaint := ln_CountInMaint - 1;

                           /* if there are overflow events, then mark as in overflow. Otherwise mark as avail */
                           IF lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow > 0 THEN
                              ln_CountOverflow := ln_CountOverflow + 1;
                              lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusOverflow;
                           ELSE
                              ln_CountAvail := ln_CountAvail + 1;
                              lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusAvail;
                           END IF;

                        END IF;

                     /* if not inducted yet, then do not change status but indicate that there is one less ongoing event */
                     WHEN lcn_StatusNotInduct THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events - 1;

                     /* if any other situation, something weird has happened, do nothing */
                     ELSE NULL;

                  END CASE;

               /* *** START OF OVERFLOW */
               WHEN lcn_EventOverflowStart THEN

                  /* Find out the aircraft's current status and adjust the counts accordingly */
                  CASE lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status

                     /* if available, then mark as in overflow */
                     WHEN lcn_StatusAvail THEN
                        ln_CountAvail := ln_CountAvail - 1;
                        ln_CountOverflow := ln_CountOverflow + 1;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusOverflow;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow := 1;

                     /* if already in maintenance, then increment the number of overflow */
                     WHEN lcn_StatusInMaint THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow + 1;

                     /* if already in overflow, then increment the number of overflow */
                     WHEN lcn_StatusOverflow THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow + 1;

                     /* if not inducted yet, then do not change status but indicate that there is an ongoing event */
                     WHEN lcn_StatusNotInduct THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow + 1;

                     /* if retired, then do nothing */
                     WHEN lcn_StatusRetired THEN NULL;

                  END CASE;

               /* *** END OF AN OVERFLOW */
               WHEN lcn_EventOverflowEnd THEN

                  /* Find out the aircraft's current status and adjust the counts accordingly */
                  CASE lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status

                     /* if in maintenance, decrement the number of overflow */
                     WHEN lcn_StatusInMaint THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow - 1;

                     /* if in overflow, decrement the number of overflow. If no more ongoing events, the aircraft it is available */
                     WHEN lcn_StatusOverflow THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow - 1;
                        IF lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_overflow = 0 THEN
                           ln_CountOverflow := ln_CountOverflow - 1;
                           ln_CountAvail := ln_CountAvail + 1;
                           lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusAvail;
                        END IF;

                     /* if not inducted yet, then do not change status but indicate that there is one less ongoing event */
                     WHEN lcn_StatusNotInduct THEN
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events := lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events - 1;

                     /* if any other situation, something weird has happened, do nothing */
                     ELSE NULL;

                  END CASE;

               /* *** INDUCTED */
               WHEN lcn_EventInduct THEN

                  /* Find out the aircraft's current status and adjust the counts accordingly */
                  CASE lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status

                     /* if it has not been inducted yet, then check to see if there is any ongoing maintenance and adjust the counts accordingly */
                     WHEN lcn_StatusNotInduct THEN
                        ln_CountNotInduct := ln_CountNotInduct - 1;
                        IF lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).num_events > 0 THEN
                           ln_CountInMaint := ln_CountInMaint + 1;
                           lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusInMaint;
                        ELSE
                           ln_CountAvail := ln_CountAvail + 1;
                           lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusAvail;
                        END IF;

                     /* if any other situation, something weird has happened, do nothing */
                     ELSE NULL;

                  END CASE;

               /* *** RETIRED */
               WHEN lcn_EventRetire THEN

                  /* Find out the aircraft's current status and adjust the counts accordingly */
                  CASE lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status

                     /* if available, adjust the counts and flag the aircraft */
                     WHEN lcn_StatusAvail THEN
                        ln_CountAvail := ln_CountAvail - 1;
                        ln_CountRetired := ln_CountRetired + 1;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusRetired;

                     /* if in maintenance, adjust the counts and flag the aircraft. */
                     WHEN lcn_StatusInMaint THEN
                        ln_CountInMaint := ln_CountInMaint - 1;
                        ln_CountRetired := ln_CountRetired + 1;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusRetired;

                     /* if in maintenance, adjust the counts and flag the aircraft. */
                     WHEN lcn_StatusOverflow THEN
                        ln_CountOverflow := ln_CountOverflow - 1;
                        ln_CountRetired := ln_CountRetired + 1;
                        lrec_AircraftList(lrec_PlanEvent.Lrp_Inv_Inv_Id).status := lcn_StatusRetired;

                     /* if any other situation, something weird has happened, do nothing */
                     ELSE NULL;

                  END CASE;

            END CASE;

            /* get the next event in the list */
            FETCH lcur_PlanEvent INTO lrec_PlanEvent;

         END LOOP;

         -- create a record for this day, summing up the number of aircraft that are in each status
         ln_Index := ln_Index + 1;
         lrec_ReportInsert.EXTEND;
         lrec_ReportInsert(ln_Index).plan_db_id := an_LrpPlanDbId;
         lrec_ReportInsert(ln_Index).plan_id := an_LrpPlanId;
         lrec_ReportInsert(ln_Index).plan_sdesc := lv_PlanSdesc;
         lrec_ReportInsert(ln_Index).day_dt := ldt_LoopDt;
         lrec_ReportInsert(ln_Index).avail_qt := ln_CountAvail;
         lrec_ReportInsert(ln_Index).in_maint_qt := ln_CountInMaint;
         lrec_ReportInsert(ln_Index).overflow_qt := ln_CountOverflow;
         lrec_ReportInsert(ln_Index).retired_qt := ln_CountRetired;

         /* start processing the next day */
         ldt_LoopDt := ldt_LoopDt + 1;
      END LOOP;

      /* close the cursor */
      CLOSE lcur_PlanEvent;

      /* Insert into the lrp_report_silt table all of the daily counts tallied up in this procedure
         Use the "APPEND" hint to improve performance of the mass insert in Oracle */
      FORALL ln_Index IN 1..lrec_ReportInsert.COUNT
         INSERT /*+APPEND_VALUES */ INTO lrp_report_silt VALUES lrec_ReportInsert(ln_Index);

      /* The procedure completed successfully */
      on_Return := 1;

   END LOAD_SILT;


   /********************************************************************************
   *
   * Procedure Body: IsRefreshedSinceLastSave
   *
   *********************************************************************************/
   FUNCTION IsRefreshedSinceLastSave (
         av_TableName         IN varchar2,
         an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
         an_LrpPlanId         lrp_plan.lrp_id%TYPE
         ) RETURN NUMBER
         IS

      /* local variables */
      ld_LastLoadDt DATE;
      ld_LastSaveDt DATE;

   BEGIN

      /* get the date when this report_table was last loaded */
      BEGIN

         SELECT last_load_dt INTO ld_LastLoadDt
         FROM lrp_report_load_control
         WHERE
            report_table = av_TableName AND
            plan_db_id   = an_LrpPlanDbId AND
            plan_id      = an_LrpPlanId;

      /* if the row does not exist in lrp_report_load_control, then this must be the
         first time that the table has ever been refreshed for this plan. Create the
         row and indicate that it has not been refreshed */
      EXCEPTION WHEN NO_DATA_FOUND THEN

         INSERT INTO lrp_report_load_control (
            report_table,
            plan_db_id,
            plan_id,
            last_load_dt )
         VALUES (
            av_TableName,
            an_LrpPlanDbId,
            an_LrpPlanId,
            SYSDATE );

         RETURN 0;

      END;

      /* get the date when this plan was last saved */
      SELECT lrp_plan.updated_dt INTO ld_LastSaveDt
      FROM lrp_plan
      WHERE
         lrp_db_id   = an_LrpPlanDbId AND
         lrp_id      = an_LrpPlanId;

      /* return based on whether the plan been refreshed more recently than the save */
      IF ld_LastLoadDt > ld_LastSaveDt THEN
         RETURN 1; /* is refreshed */
      ELSE
         RETURN 0; /* is not refreshed */
      END IF;

      /* Shouldn't get to this line, but return "not refreshed" just in case */
      RETURN 0;

   END IsRefreshedSinceLastSave;


END LRP_REPORT_PKG;
/