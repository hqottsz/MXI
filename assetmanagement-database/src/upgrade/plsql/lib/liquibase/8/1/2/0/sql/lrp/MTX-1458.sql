--liquibase formatted sql


--changeSet MTX-1458:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
	Create table "LRP_REPORT_LOAD_CONTROL" (
	"REPORT_TABLE" Varchar2 (100) NOT NULL ,
	"PLAN_DB_ID" Number(10,0) NOT NULL ,
	"PLAN_ID" Number(10,0) NOT NULL ,
	"LAST_LOAD_DT" Date NOT NULL ,
	Constraint "PK_LRP_REPORT_LOAD_CONTROL" primary key ("REPORT_TABLE","PLAN_DB_ID","PLAN_ID") 
	)
');
END;
/

--changeSet MTX-1458:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
	Create table "LRP_REPORT_PART_FORECAST" (
		"PLAN_DB_ID" Number(10,0) NOT NULL ,
		"PLAN_ID" Number(10,0) NOT NULL ,
		"PLAN_SDESC" Varchar2 (80),
		"PART_GROUP_CD" Varchar2 (40),
		"PART_GROUP_NAME" Varchar2 (80),
		"PART_QTY" Float,
		"EVENT_SDESC" Varchar2 (80),
		"EVENT_START_DT" Date,
		"EVENT_END_DT" Date,
		"EVENT_DURATION_DAYS" Float,
		"AC_REG_CD" Varchar2 (10),
		"AC_SERIAL_NO_OEM" Varchar2 (40),
		"AC_ASSMBL_CD" Varchar2 (8),
		"AC_ASSMBL_SUBTYPE_CD" Varchar2 (8),
		"AC_CARRIER_CD" Varchar2 (8),
		"LOC_CD" Varchar2 (2000),
		"WORKSCOPE_CD" Varchar2 (200),
		"WORKSCOPE_NAME" Varchar2 (200),
		"WORKSCOPE_DEADLINE_DT" Date,
		"WORKSCOPE_MAN_HOURS" Number(8,2),
		"WORKSCOPE_YIELD_PCT" Float
	) 
');
END;
/

--changeSet MTX-1458:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
	Create Index "IX_PLAN_LRPREPORTPARTFCST" ON "LRP_REPORT_PART_FORECAST" ("PLAN_DB_ID","PLAN_ID") 
');
END;
/

--changeSet MTX-1458:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE LRP_REPORT_PKG IS
/********************************************************************************
*
* Package:     LRP_REPORT_PKG (SPEC)
* Description: This package is used to load the various reporting tables used by 
*              Long Range Planner (LRP). The intent is that reports run in LRP
*              will query these reporting tables rather than creating elaborate
*              SQL statements on the source LRP tables. This should make reports
*              easier to write, and improve performance.
*
*              Every LRP report table will have a procedure used to load that 
*              table. For example, the LRP_REPORT_PART_FORECAST table will have 
*              a procedure called LOAD_PART_FORECAST. Before running a report that
*              uses one of the reporting tables, you should call the LOAD_XXX 
*              procedure.
*
*              We are using date-checking techniques, so that we don't refresh 
*              the report tables unnecessarily. We use the LRP_REPORT_LOAD_CONTROL 
*              table to manage this.
*
*********************************************************************************/

/********************************************************************************
*
* Procedure:   LOAD_PART_FORECAST
* Description: This procedure is used to load the LRP_PLAN_PART_FORECAST table 
*              with the data for a particular plan
*
* Parameters:  an_LrpPlanDbId, an_LrpPlanId - This is the ID of the saved LRP 
*                    plan that is being loaded into the reporting table. Only 
*                    one LRP Plan can be loaded at a time with this procedure, 
*                    although this table can hold the data for many LRP Plans 
*                    at once.
*              on_Return (output) - Return 1 on success, -1 if there was an 
*                    unknown failure and the error code (negative number) if 
*                    there was a known error
*
*********************************************************************************/
PROCEDURE LOAD_PART_FORECAST(
an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
an_LrpPlanId         lrp_plan.lrp_id%TYPE,
on_Return            OUT NUMBER );

END LRP_REPORT_PKG;
/

--changeSet MTX-1458:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
      INSERT INTO lrp_report_part_forecast (
         /*+ APPEND */
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