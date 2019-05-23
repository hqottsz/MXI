--liquibase formatted sql


--changeSet LRP_REPORT_PKG_SPEC:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   * Description: This procedure is used to load the LRP_REPORT_PART_FORECAST table
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

   /********************************************************************************
   *
   * Procedure:   LOAD_SCHEDULE
   * Description: This procedure is used to load the LRP_REPORT_SCHEDULE table
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
   PROCEDURE LOAD_SCHEDULE(
      an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
      an_LrpPlanId         lrp_plan.lrp_id%TYPE,
      on_Return            OUT NUMBER );

   /********************************************************************************
   *
   * Procedure:   LOAD_SILT
   * Description: This procedure is used to load the LRP_REPORT_SILT table
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
   PROCEDURE LOAD_SILT(
      an_LrpPlanDbId       lrp_plan.lrp_db_id%TYPE,
      an_LrpPlanId         lrp_plan.lrp_id%TYPE,
      on_Return            OUT NUMBER );

END LRP_REPORT_PKG;
/