--liquibase formatted sql

--changeSet OPER-19593:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY USAGE_PKG
IS

   xc_BadEqnName EXCEPTION;
   gv_error_info  VARCHAR2(1000) := '';


/******************************************************************************
*
* Function:     calculate_calc_parm
*               (local function)
*
* Arguments:    ain_inv_no_db_id: inventory key
*               ain_inv_no_id:     "
*               ain_calc_db_id: calculated parameter key
*               ain_calc_id:     "
*               aitab_usages: table of usages (inv key, data type key, usage value)
*               aon_calc_usage: calculated usage value
*
* Description:  This function executes the equation for the provided
*               calculated parameter and returns its results.
*
*               Calculated parameters are configured to have:
*                - an equation (plsql function in the DB)
*                - usage parameters
*                - constants (defaults and part specific)
*               The equation and constants are pre-configured in the DB.
*               The usage parameter values (passed to the equation) are
*               extracted from the provided usages table.
*               When determining whether the default or a part specific constant
*               is to be used, the part of the provided inventory is checked.
*
*               Example: if the equations requires the value of the HOURS
*               usage parameter, then the aitab_usages is accessed to find
*               the value corresponding to the inventory-key and HOURS.
*               If the equation requires a particular constant then the
*               inventory-key is used to determine its part and if there exists
*               a part specific constant for that part then it is used
*               otherwise, the default constant is used.
*
*******************************************************************************/
PROCEDURE calculate_calc_parm(
   ain_inv_no_db_id   IN inv_inv.inv_no_db_id%TYPE,
   ain_inv_no_id      IN inv_inv.inv_no_id%TYPE,
   ain_calc_db_id     IN mim_calc.calc_db_id%TYPE,
   ain_calc_id        IN mim_calc.calc_id%TYPE,
   aitab_usages       IN STRSTRSTRSTRFLOATTABLE,
   aon_calc_usage     OUT NUMBER
)
IS
   lv_sql_str  VARCHAR2(4000);

   -- Note: the cursors used in this function were moved here from the existing
   --       function ApplyCustomCalc()

   -- Cursor used to get the equation and its arguments.
   -- Part specific constants are returned instead of the default constant
   -- based on the inventory's part number.
   CURSOR lcur_equation (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id,
         cn_CalcDbId       typn_Id,
         cn_CalcId         typn_Id
      ) IS
      SELECT mim_calc.eqn_ldesc,
             mim_calc.data_type_db_id,
             mim_calc.data_type_id,
             mim_data_type.data_type_cd,
             mim_data_type.entry_prec_qt
        FROM mim_calc,
             mim_data_type
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND mim_data_type.data_type_db_id = mim_calc.data_type_db_id
         AND mim_data_type.data_type_id    = mim_calc.data_type_id
         AND NOT EXISTS
             ( SELECT 1
                 FROM inv_inv,
                      mim_part_numdata
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.assmbl_bom_id <> 0
                  AND inv_inv.rstat_cd     = 0
                  AND mim_part_numdata.assmbl_db_id  = inv_inv.assmbl_db_id
                  AND mim_part_numdata.assmbl_cd     = inv_inv.assmbl_cd
                  AND mim_part_numdata.assmbl_bom_id = inv_inv.assmbl_bom_id
                  AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
                  AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
                  AND mim_part_numdata.eqn_ldesc IS NOT NULL
                UNION
                SELECT 1
                 FROM inv_inv,
                      mim_part_numdata
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.rstat_cd     = 0
                  AND mim_part_numdata.assmbl_db_id  = inv_inv.orig_assmbl_db_id
                  AND mim_part_numdata.assmbl_cd     = inv_inv.orig_assmbl_cd
                  AND mim_part_numdata.assmbl_bom_id = 0
                  AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
                  AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
                  AND mim_part_numdata.eqn_ldesc IS NOT NULL )
      UNION ALL
      SELECT mim_part_numdata.eqn_ldesc,
             mim_calc.data_type_db_id,
             mim_calc.data_type_id,
             mim_data_type.data_type_cd,
             mim_data_type.entry_prec_qt
        FROM mim_calc,
             inv_inv,
             mim_part_numdata,
             mim_data_type
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd     = 0
         AND inv_inv.assmbl_bom_id <> 0
         AND mim_part_numdata.assmbl_db_id  = inv_inv.assmbl_db_id
         AND mim_part_numdata.assmbl_cd     = inv_inv.assmbl_cd
         AND mim_part_numdata.assmbl_bom_id = inv_inv.assmbl_bom_id
         AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
         AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
         AND mim_part_numdata.eqn_ldesc IS NOT NULL
         AND mim_data_type.data_type_db_id = mim_calc.data_type_db_id
         AND mim_data_type.data_type_id    = mim_calc.data_type_id
       UNION
       SELECT mim_part_numdata.eqn_ldesc,
             mim_calc.data_type_db_id,
             mim_calc.data_type_id,
             mim_data_type.data_type_cd,
             mim_data_type.entry_prec_qt
        FROM mim_calc,
             inv_inv,
             mim_part_numdata,
             mim_data_type
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd     = 0
         AND mim_part_numdata.assmbl_db_id  = inv_inv.orig_assmbl_db_id
         AND mim_part_numdata.assmbl_cd     = inv_inv.orig_assmbl_cd
         AND mim_part_numdata.assmbl_bom_id = 0
         AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
         AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
         AND mim_part_numdata.eqn_ldesc IS NOT NULL
         AND mim_data_type.data_type_db_id = mim_calc.data_type_db_id
         AND mim_data_type.data_type_id    = mim_calc.data_type_id ;
   lrec_Equation lcur_equation%ROWTYPE;

   -- Cursor to get the input values for the equation.
   CURSOR lcur_InputValues (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id,
         cn_CalcDbId       typn_Id,
         cn_CalcId         typn_Id,
         ct_Usages         STRSTRSTRSTRFLOATTABLE
      ) IS
      -- Get data_type inputs (never part-specific).
      SELECT usages.usage_qt,
             mim_calc_input.input_ord
        FROM inv_inv,
             mim_calc_input,
            (SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as usage_qt
             FROM
                TABLE (ct_Usages)
            ) usages
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd     = 0
         AND mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_calc_input.constant_bool = 0
         AND usages.inv_no_db_id = inv_inv.inv_no_db_id
         AND usages.inv_no_id    = inv_inv.inv_no_id
         AND usages.data_type_db_id = mim_calc_input.data_type_db_id
         AND usages.data_type_id    = mim_calc_input.data_type_id
      UNION ALL
      -- Get non part-specific constant values.
      SELECT mim_calc_input.input_qt AS usage_qt,
             mim_calc_input.input_ord
        FROM mim_calc_input
       WHERE mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_calc_input.constant_bool = 1
         AND NOT EXISTS
             -- Make sure there are no part-specific constant values.
             ( SELECT 1
                 FROM inv_inv,
                      mim_part_input,
                      eqp_bom_part
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.assmbl_bom_id <> 0
                  AND inv_inv.rstat_cd      = 0
                  AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
                  AND mim_part_input.calc_id    = mim_calc_input.calc_id
                  AND mim_part_input.input_id   = mim_calc_input.input_id
                  AND eqp_bom_part.assmbl_db_id    = inv_inv.assmbl_db_id
                  AND eqp_bom_part.assmbl_cd       = inv_inv.assmbl_cd
                  AND eqp_bom_part.assmbl_bom_id   = inv_inv.assmbl_bom_id
                  AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
                  AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
                  AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
                  AND mim_part_input.part_no_id    = inv_inv.part_no_id
                UNION
                SELECT 1
                 FROM inv_inv,
                      mim_part_input,
                      eqp_bom_part
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.rstat_cd     = 0
                  AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
                  AND mim_part_input.calc_id    = mim_calc_input.calc_id
                  AND mim_part_input.input_id   = mim_calc_input.input_id
                  AND eqp_bom_part.assmbl_db_id    = inv_inv.orig_assmbl_db_id
                  AND eqp_bom_part.assmbl_cd       = inv_inv.orig_assmbl_cd
                  AND eqp_bom_part.assmbl_bom_id   = 0
                  AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
                  AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
                  AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
                  AND mim_part_input.part_no_id    = inv_inv.part_no_id)
      UNION ALL
      -- Get part-specific constant values.
      (SELECT mim_part_input.input_qt AS usage_qt,
             mim_calc_input.input_ord
        FROM inv_inv,
             mim_calc_input,
             mim_part_input,
             eqp_bom_part
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.assmbl_bom_id <> 0
         AND inv_inv.rstat_cd    = 0
         AND mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
         AND mim_part_input.calc_id    = mim_calc_input.calc_id
         AND mim_part_input.input_id   = mim_calc_input.input_id
         AND eqp_bom_part.assmbl_db_id    = inv_inv.assmbl_db_id
         AND eqp_bom_part.assmbl_cd       = inv_inv.assmbl_cd
         AND eqp_bom_part.assmbl_bom_id   = inv_inv.assmbl_bom_id
         AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
         AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
         AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
         AND mim_part_input.part_no_id    = inv_inv.part_no_id
       UNION
       SELECT mim_part_input.input_qt AS usage_qt,
             mim_calc_input.input_ord
        FROM inv_inv,
             mim_calc_input,
             mim_part_input,
             eqp_bom_part
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd    = 0
         AND mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
         AND mim_part_input.calc_id    = mim_calc_input.calc_id
         AND mim_part_input.input_id   = mim_calc_input.input_id
         AND eqp_bom_part.assmbl_db_id    = inv_inv.orig_assmbl_db_id
         AND eqp_bom_part.assmbl_cd       = inv_inv.orig_assmbl_cd
         AND eqp_bom_part.assmbl_bom_id   = 0
         AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
         AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
         AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
         AND mim_part_input.part_no_id    = inv_inv.part_no_id)
      ORDER BY 2 ASC;

BEGIN

   OPEN lcur_equation(ain_inv_no_db_id, ain_inv_no_id, ain_calc_db_id, ain_calc_id);
   FETCH lcur_equation INTO lrec_Equation;
   CLOSE lcur_equation;

   -- Generate the dynamic SQL for calling the equation.
   lv_sql_str := 'BEGIN :res := ' || lrec_Equation.eqn_ldesc || '(';
   
   FOR lrec_InputValues IN lcur_InputValues(ain_inv_no_db_id,
                                            ain_inv_no_id,
                                            ain_calc_db_id,
                                            ain_calc_id,
                                            aitab_usages)
   LOOP
      lv_sql_str := lv_sql_str ||  REPLACE(lrec_InputValues.usage_qt,',','.') || ',';
   END LOOP;

   lv_sql_str := RTRIM(lv_sql_str, ',') || ');END;';

   -- Execute the dynamic SQL and get the result.
   Calculate(lv_sql_str, lrec_Equation.Entry_Prec_Qt, aon_calc_usage);

END calculate_calc_parm;


/******************************************************************************
*
* Procedure:    update_calc_parms
* Arguments:    ait_evt_inv_key: evt_inv key as a tuple
* Description:  This procedure finds any calculated parameters in the usage 
*               usage snapshot associated with the inventory event.
*               It then recaclulates the values for those calculated parameters
*               and replaces those values in the snapshot.
*
*******************************************************************************/
PROCEDURE update_calc_parms(
   ait_evt_inv_key IN StrStrStrTuple
)
IS

   ln_event_db_id NUMBER;
   ln_event_id NUMBER;
   ln_event_inv_id NUMBER;
   ltab_usages STRSTRSTRSTRFLOATTABLE;
   ln_tsn NUMBER;

   CURSOR lcur_calc_parm_list IS
      SELECT
         evt_inv.event_db_id AS event_db_id,
         evt_inv.event_id AS event_id,
         evt_inv.event_inv_id,
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         evt_inv_usage.data_type_db_id,
         evt_inv_usage.data_type_id,
         mim_calc.calc_db_id,
         mim_calc.calc_id
      FROM
         evt_inv_usage
      INNER JOIN mim_calc ON
         mim_calc.data_type_db_id = evt_inv_usage.data_type_db_id AND
         mim_calc.data_type_id    = evt_inv_usage.data_type_id
      INNER JOIN evt_inv ON
         evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
         evt_inv_usage.event_id     = evt_inv.event_id AND
         evt_inv_usage.event_inv_id = evt_inv.event_inv_id 
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
         inv_inv.inv_no_id    = evt_inv.inv_no_id
      WHERE
         evt_inv_usage.event_db_id  = ln_event_db_id AND
         evt_inv_usage.event_id     = ln_event_id AND
         evt_inv_usage.event_inv_id = ln_event_inv_id
         AND
         (
            (
               -- For SER,KIT,BATCH the assembly cannot be determed, so return all matching mim_calc rows.
               inv_inv.inv_class_db_id = 0 AND
               inv_inv.inv_class_cd    IN ('SER','KIT','BATCH')
            )
            OR 
            (
               -- For ASSY use the original assembly of the inventory.
               inv_inv.inv_class_db_id = 0 AND
               inv_inv.inv_class_cd    = 'ASSY'
               AND
               mim_calc.assmbl_db_id = inv_inv.orig_assmbl_db_id AND
               mim_calc.assmbl_cd    = inv_inv.orig_assmbl_cd
            )
            OR
            (
               -- For any other class use the assembly of the inventory.
               NOT 
               (
                  inv_inv.inv_class_db_id = 0 AND
                  inv_inv.inv_class_cd    = 'ASSY'
               )
               AND
               mim_calc.assmbl_db_id = inv_inv.assmbl_db_id AND
               mim_calc.assmbl_cd    = inv_inv.assmbl_cd                  
            )
         );

BEGIN

   ln_event_db_id  := ait_evt_inv_key.element1;
   ln_event_id     := ait_evt_inv_key.element2;
   ln_event_inv_id := ait_evt_inv_key.element3;

   FOR lrec_calc_parm_list IN lcur_calc_parm_list  LOOP
   
      -- Get all usages for the event on the inventory
      -- which may be used by the calculate parms equation.
      SELECT
         StrStrStrStrFloatTuple(
              lrec_calc_parm_list.inv_no_db_id,
              lrec_calc_parm_list.inv_no_id,
              evt_inv_usage.data_type_db_id,
              evt_inv_usage.data_type_id,
              evt_inv_usage.tsn_qt
          )
      BULK COLLECT INTO ltab_usages
      FROM
         evt_inv_usage
      WHERE
         evt_inv_usage.event_db_id  = lrec_calc_parm_list.event_db_id AND
         evt_inv_usage.event_id     = lrec_calc_parm_list.event_id AND
         evt_inv_usage.event_inv_id = lrec_calc_parm_list.event_inv_id;

      -- Use the equation of the calculated parameter to calculate the new TSN value.
      -- For event snapshots we are only concerned with TSN (not tso or tsi).
      calculate_calc_parm(lrec_calc_parm_list.inv_no_db_id,
                          lrec_calc_parm_list.inv_no_id,
                          lrec_calc_parm_list.calc_db_id,
                          lrec_calc_parm_list.calc_id,
                          ltab_usages,
                          ln_tsn
                          );

      -- Update the calculated parameter's TSN with the new value.
      UPDATE
         evt_inv_usage
      SET
         evt_inv_usage.tsn_qt = ln_tsn
      WHERE
         evt_inv_usage.event_db_id     = lrec_calc_parm_list.event_db_id AND
         evt_inv_usage.event_id        = lrec_calc_parm_list.event_id AND
         evt_inv_usage.event_inv_id    = lrec_calc_parm_list.event_inv_id AND
         evt_inv_usage.data_type_db_id = lrec_calc_parm_list.data_type_db_id AND
         evt_inv_usage.data_type_id    = lrec_calc_parm_list.data_type_id;
   END LOOP;
   
END update_calc_parms;

/******************************************************************************
*
* Procedure:    UsageCalculations
* Arguments:    an_InvNoDbId (number): The inv_no whose
*                          calculations will be updated
*               an_InvNoId (number):   ""
*               on_Return (number): Return 1 means success, <0 means failure
* Description:  This procedure will calculate and apply any calculated parms
*               for a given inventory. It will trigger part-specific
*               calculations where required (note: if the inventory is an
*           assembly, all subcomponents will also be updated.
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*******************************************************************************
*
* Copyright 1999-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UsageCalculations (
      an_InvNoDbId      IN typn_Id,
      an_InvNoId        IN typn_Id,
      on_Return         OUT NUMBER
   ) IS

   /* CURSOR DECLARATIONS */
   /* list of calculations for a given inventory */
   CURSOR lcur_CalcList (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id
      ) IS
      SELECT mim_calc.calc_db_id,
             mim_calc.calc_id
      FROM inv_inv,
             mim_calc
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId AND
             inv_inv.inv_no_id    = cn_InvNoId   AND
             inv_inv.rstat_cd     = 0
             AND
             mim_calc.assmbl_db_id = inv_inv.assmbl_db_id AND
             mim_calc.assmbl_cd    = inv_inv.assmbl_cd
      UNION
      SELECT mim_calc.calc_db_id,
             mim_calc.calc_id
      FROM inv_inv,
             mim_calc
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId AND
             inv_inv.inv_no_id    = cn_InvNoId   AND
             inv_inv.rstat_cd     = 0
             AND
             mim_calc.assmbl_db_id = inv_inv.orig_assmbl_db_id AND
             mim_calc.assmbl_cd    = inv_inv.orig_assmbl_cd;

   /* get a list of all inventory which require custom calculation */
   CURSOR lcur_CalcInv (
         cn_AssmblInvNoDbId   typn_Id,
         cn_AssmblInvNoId     typn_Id,
         cn_CalcDbId          typn_Id,
         cn_CalcId            typn_Id
      ) IS
      /* list the assembly itself */
      SELECT inv_curr_usage.inv_no_db_id,
             inv_curr_usage.inv_no_id
        FROM mim_calc,
             inv_curr_usage
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_curr_usage.inv_no_db_id = cn_AssmblInvNoDbId
         AND inv_curr_usage.inv_no_id    = cn_AssmblInvNoId
         AND inv_curr_usage.data_type_db_id = mim_calc.data_type_db_id
         AND inv_curr_usage.data_type_id    = mim_calc.data_type_id
      UNION
     /* list sub-inventory which have this calc */
      SELECT inv_inv.inv_no_db_id,
             inv_inv.inv_no_id
        FROM inv_inv,
             mim_calc,
             inv_curr_usage
       WHERE inv_inv.assmbl_inv_no_db_id = cn_AssmblInvNoDbId
         AND inv_inv.assmbl_inv_no_id    = cn_AssmblInvNoId
         AND inv_inv.rstat_cd            = 0
         AND mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_curr_usage.inv_no_db_id = inv_inv.inv_no_db_id
         AND inv_curr_usage.inv_no_id    = inv_inv.inv_no_id
         AND inv_curr_usage.data_type_db_id = mim_calc.data_type_db_id
         AND inv_curr_usage.data_type_id    = mim_calc.data_type_id;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all of the discovered calculations */
   FOR lrec_CalcList IN lcur_CalcList(an_InvNoDbId, an_InvNoId) Loop
     /* *** PERFORM CUSTOM CALCULATIONS *** */

      /* get a list of all inventory which require custom calculation of the
         result */
      FOR lrec_CalcInv IN lcur_CalcInv(
                        an_InvNoDbId,
                        an_InvNoId,
                        lrec_CalcList.calc_db_id,
                        lrec_CalcList.calc_id) LOOP

         /* run the custom calculation for each inventory */
          ApplyCustomCalc(lrec_CalcInv.inv_no_db_id,
                      lrec_CalcInv.inv_no_id,
                      lrec_CalcList.calc_db_id,
                      lrec_CalcList.calc_id,
                      on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END LOOP;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_OracleError;
      application_object_pkg.SetMxiError('DEV-99999', 'USAGE_PKG@@@UsageCalculations@@@' || SQLERRM);
END UsageCalculations;


/******************************************************************************
*
* Procedure:    ApplyCustomCalc
* Arguments:    an_InvNoDbId (number): The inv_no to update
*               an_InvNoId (number):   ""
*               an_CalcDbId (number): The calculation
*               an_CalcId (number):   ""
*               on_Return (number): Return 1 means success, <0 means failure
* Description:  This procedure is used to perform part-specific calculations
*               at a given location
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*******************************************************************************
*
* Copyright 1999-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE ApplyCustomCalc (
      an_InvNoDbId            IN typn_Id,
      an_InvNoId              IN typn_Id,
      an_CalcDbId             IN typn_Id,
      an_CalcId               IN typn_Id,
      on_Return               OUT NUMBER
   ) IS

   lv_MissingDataType    mim_data_type.data_type_cd%TYPE;
   ltab_currentUsages    STRSTRSTRSTRFLOATTABLE;
   xc_MissingInputOnInv  EXCEPTION;
   ln_tsn NUMBER;
   ln_tso NUMBER;
   ln_tsi NUMBER;

BEGIN
   -- Initialize the return value.
   on_Return := icn_NoProc;

   -- Make sure that all of the input parms are specified for the inventory.
   BEGIN
      SELECT
         LISTAGG(mim_data_type.data_type_cd,',') Within GROUP (ORDER BY 1)
      INTO
         lv_MissingDataType
      FROM
         mim_calc_input
         INNER JOIN mim_data_type ON
            mim_data_type.data_type_db_id = mim_calc_input.data_type_db_id AND
            mim_data_type.data_type_id    = mim_calc_input.data_type_id
      WHERE
         mim_calc_input.calc_db_id = an_CalcDbId AND
         mim_calc_input.calc_id    = an_CalcId
         AND
         mim_calc_input.constant_bool = 0
         AND
         NOT EXISTS
         (
            SELECT
               1
            FROM
               inv_curr_usage
            WHERE
               inv_no_db_id    = an_InvNoDbId AND
               inv_no_id       = an_InvNoId AND
               data_type_db_id = mim_calc_input.data_type_db_id AND
               data_type_id    = mim_calc_input.data_type_id
         );

      IF lv_MissingDataType IS NOT NULL THEN
         RAISE xc_MissingInputOnInv;
      END IF;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         NULL;
      WHEN xc_MissingInputOnInv THEN
         RAISE xc_MissingInputOnInv;
   END;

   -- Collect the TSN for all the current inventory usages
   -- that the calculated parameter may need.
   SELECT
      StrStrStrStrFloatTuple(
           inv_curr_usage.inv_no_db_id,
           inv_curr_usage.inv_no_id,
           inv_curr_usage.data_type_db_id,
           inv_curr_usage.data_type_id,
           inv_curr_usage.tsn_qt
       )
   BULK COLLECT INTO ltab_currentUsages
   FROM
      inv_curr_usage
   WHERE
      inv_curr_usage.inv_no_db_id = an_InvNoDbId AND
      inv_curr_usage.inv_no_id    = an_InvNoId
   ;

   -- Calculate the TSN value of the calculated parameter.
   calculate_calc_parm(an_InvNoDbId,
                       an_InvNoId,
                       an_CalcDbId,
                       an_CalcId,
                       ltab_currentUsages,
                       ln_tsn);

   -- Repeat for the TSO.
   SELECT
      StrStrStrStrFloatTuple(
           inv_curr_usage.inv_no_db_id,
           inv_curr_usage.inv_no_id,
           inv_curr_usage.data_type_db_id,
           inv_curr_usage.data_type_id,
           inv_curr_usage.tso_qt
       )
   BULK COLLECT INTO ltab_currentUsages
   FROM
      inv_curr_usage
   WHERE
      inv_curr_usage.inv_no_db_id = an_InvNoDbId AND
      inv_curr_usage.inv_no_id    = an_InvNoId
   ;

   calculate_calc_parm(an_InvNoDbId,
                       an_InvNoId,
                       an_CalcDbId,
                       an_CalcId,
                       ltab_currentUsages,
                       ln_tso);

   -- Repeat for the TSI.
   SELECT
      StrStrStrStrFloatTuple(
           inv_curr_usage.inv_no_db_id,
           inv_curr_usage.inv_no_id,
           inv_curr_usage.data_type_db_id,
           inv_curr_usage.data_type_id,
           inv_curr_usage.tsi_qt
       )
   BULK COLLECT INTO ltab_currentUsages
   FROM
      inv_curr_usage
   WHERE
      inv_curr_usage.inv_no_db_id = an_InvNoDbId AND
      inv_curr_usage.inv_no_id    = an_InvNoId
   ;

   calculate_calc_parm(an_InvNoDbId,
                       an_InvNoId,
                       an_CalcDbId,
                       an_CalcId,
                       ltab_currentUsages,
                       ln_tsi);

   -- Apply the new values to all inventory in the assembly.
   -- Use the same calculated value for all TSN/TSI/TSO.
   UPDATE
      inv_curr_usage
   SET
      tsn_qt = ln_tsn,
      tso_qt = ln_tso,
      tsi_qt = ln_tsi
   WHERE
      inv_no_db_id    = an_InvNoDbId AND
      inv_no_id       = an_InvNoId AND
      (data_type_db_id, data_type_id) IN (
         -- use the data type key for the calc parm
         SELECT
            mim_calc.data_type_db_id,
            mim_calc.data_type_id
         FROM
            mim_calc
         WHERE
            mim_calc.calc_db_id = an_CalcDbId AND
            mim_calc.calc_id    = an_CalcId
      )
   ;

   on_Return := icn_Success;

EXCEPTION
   WHEN xc_MissingInputOnInv THEN
      on_Return := icn_MissingInputOnInv;
      application_object_pkg.SetMxiError(
         'BUS-00275',
         an_InvNoDbId || ':' || an_InvNoId || '|' || an_CalcDbId  || ':' || an_CalcId || '|' || lv_MissingDataType);
   WHEN xc_BadEqnName THEN
      on_Return := icn_BadEqnName;
      application_object_pkg.SetMxiError(
         'BUS-00276',
         an_InvNoDbId || ':' || an_InvNoId || '|' || an_CalcDbId  || ':' || an_CalcId || '|' || gv_error_info);
   WHEN OTHERS THEN
      on_Return := icn_OracleError;
      application_object_pkg.SetMxiError('DEV-99999', 'USAGE_PKG@@@ApplyCustomCalc@@@' || SQLERRM);
END ApplyCustomCalc;


/******************************************************************************
*
* Procedure:    Calculate
* Arguments:    av_CalcFunction     plsql block containing the calculation to execute
*               ai_Precision        precision to round the result
*               on_Return (number): Result of the calculation rounded with the specified precision                                   significant digits
* Description:  This procedure is used to perform a calculation
*
* Orig.Coder:   Vince Chan
* Recent Coder: Marius Secrieru
* Recent Date:  02.JUN.2011
*
*******************************************************************************
*
* Copyright 1998-2011 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE Calculate (
      av_CalcFunction            IN VARCHAR,
      ai_Precision               IN INTEGER,
      on_Return                  OUT NUMBER
   ) IS
    li_FuncCursor         INTEGER; /* variables used to hold DBMS info */
    ln_CalcResult         NUMBER; /* Result from the calculation*/
    li_RowsProcessed      INTEGER;
BEGIN

   li_FuncCursor := DBMS_SQL.open_cursor;
   DBMS_SQL.parse(li_FuncCursor, av_CalcFunction, DBMS_SQL.v7);

   /* bind all variables used by the dynamic SQL statement */
   DBMS_SQL.bind_variable(li_FuncCursor, ':res', ln_CalcResult);

   /* execute the dynamic SQL statement */
   BEGIN
      li_RowsProcessed := DBMS_SQL.execute(li_FuncCursor);
   EXCEPTION
      WHEN OTHERS THEN
         gv_error_info := av_CalcFunction;
         RAISE xc_BadEqnName;
   END;

   /* pull out any PL/SQL variables from the dynamic SQL statement */
   DBMS_SQL.variable_value(li_FuncCursor, ':res', ln_CalcResult);

   /* close the dynamic SQL statement */
   DBMS_SQL.close_cursor(li_FuncCursor);

   /* Round the result to specified precision */
   on_Return := ROUND(ln_CalcResult, ai_Precision);

   EXCEPTION
   WHEN OTHERS THEN
      /*close any open cursor*/
      IF DBMS_SQL.is_OPEN(li_FuncCursor) THEN
         DBMS_SQL.close_cursor(li_FuncCursor);
      END IF;
      /*Reraise the exception*/
      RAISE ;

END Calculate;


/******************************************************************************
*
* Procedure:    takeCurrentUsageSnapshotByEvent
*
* Description:  This procedure inserts records into evt_inv_usage for the
*               provided event using the usage of the inventory associated
*               to the event at the time of the event.
*
* Arguments:    an_event_db_id  event key
*               an_event_id     "
*
*******************************************************************************/
PROCEDURE takeUsageSnapshotOfEvent(
   an_event_db_id   IN NUMBER,
   an_event_id      IN NUMBER
)
IS
   ld_date               evt_event.event_dt%TYPE;

   ltb_inv_no_db_ids     VARCHAR2TABLE;
   ltb_inv_no_ids        VARCHAR2TABLE;
   ltb_trk_usages        STRSTRSTRSTRFLOATTABLE;
   ltb_sys_usages        STRSTRSTRSTRFLOATTABLE;
   ltb_ac_usages         STRSTRSTRSTRFLOATTABLE;
   ltb_evt_inv           STRSTRSTRTABLE;
   ln_calc_parm_count	 NUMBER;

BEGIN

   SELECT
      evt_event.event_dt
   INTO
      ld_date
   FROM
      evt_event
   WHERE
      evt_event.event_db_id = an_event_db_id AND
      evt_event.event_id    = an_event_id;

   IF (ld_date IS NULL) THEN
      RETURN;
   END IF;

   SELECT
      StrStrStrTuple(
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         evt_inv.event_inv_id
      )
   BULK COLLECT INTO ltb_evt_inv
   FROM
      evt_inv
   WHERE
      evt_inv.event_db_id = an_event_db_id AND
      evt_inv.event_id    = an_event_id;

   SELECT
      element1
   BULK COLLECT INTO ltb_inv_no_db_ids
   FROM
      TABLE(ltb_evt_inv)
   ORDER BY
      element3;

   SELECT
      element2
   BULK COLLECT INTO ltb_inv_no_ids
   FROM
      TABLE(ltb_evt_inv)
   ORDER BY
      element3;

   -- Get the TRK or SER usages.
   ltb_trk_usages := USAGE_PKG.getTrkSerUsagesOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, ld_date);

   -- Get the ACFT or ASSY usages.
   ltb_ac_usages := USAGE_PKG.getAcftAssyUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, ld_date);

   -- Get the SYS usages.
   ltb_sys_usages := USAGE_PKG.getSysUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, ld_date);


   -- Upsert a row in the evt_inv_usage table
   -- to represent the usage for all the inventory associated to the event.
   MERGE INTO evt_inv_usage
   USING
      (
         SELECT
            an_event_db_id AS event_db_id,
            an_event_id AS event_id,
            tb_evt_inv.event_inv_id,
            usage.data_type_db_id,
            usage.data_type_id,
            usage.hist_tsn AS tsn_qt
         FROM
            -- Hint, wrapping in a select will make it faster (and readable).
            (SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as event_inv_id
             FROM
             TABLE (ltb_evt_inv)
            ) tb_evt_inv
         INNER JOIN
            (
             SELECT
                element1 as inv_no_db_id,
                element2 as inv_no_id,
                element3 as data_type_db_id,
                element4 as data_type_id,
                element5 as hist_tsn
             FROM TABLE(ltb_trk_usages)
             UNION ALL
             SELECT
                element1 as inv_no_db_id,
                element2 as inv_no_id,
                element3 as data_type_db_id,
                element4 as data_type_id,
                element5 as hist_tsn
             FROM TABLE(ltb_ac_usages)
             UNION ALL
             SELECT
                element1 as inv_no_db_id,
                element2 as inv_no_id,
                element3 as data_type_db_id,
                element4 as data_type_id,
                element5 as hist_tsn
             FROM TABLE(ltb_sys_usages)
            ) usage
            ON
               usage.inv_no_db_id  = tb_evt_inv.inv_no_db_id AND
               usage.inv_no_id     = tb_evt_inv.inv_no_id
      ) usage_at_date
   ON
      (
         evt_inv_usage.event_db_id     = usage_at_date.event_db_id AND
         evt_inv_usage.event_id        = usage_at_date.event_id AND
         evt_inv_usage.event_inv_id    = usage_at_date.event_inv_id AND
         evt_inv_usage.data_type_db_id = usage_at_date.data_type_db_id AND
         evt_inv_usage.data_type_id    = usage_at_date.data_type_id
      )
   WHEN MATCHED THEN
      UPDATE SET
         -- tso and tsi are no longer captured, so set to the tsn
         evt_inv_usage.tsn_qt = usage_at_date.tsn_qt,
         evt_inv_usage.tso_qt = usage_at_date.tsn_qt,
         evt_inv_usage.tsi_qt = usage_at_date.tsn_qt
   WHEN NOT MATCHED THEN
      INSERT
         (
            evt_inv_usage.event_db_id,
            evt_inv_usage.event_id,
            evt_inv_usage.event_inv_id,
            evt_inv_usage.data_type_db_id,
            evt_inv_usage.data_type_id,
            evt_inv_usage.tsn_qt,
            evt_inv_usage.tso_qt,
            evt_inv_usage.tsi_qt
         )
      VALUES
         (
            -- tso and tsi are no longer captured, so set to the tsn
            usage_at_date.event_db_id,
            usage_at_date.event_id,
            usage_at_date.event_inv_id,
            usage_at_date.data_type_db_id,
            usage_at_date.data_type_id,
            usage_at_date.tsn_qt,
            usage_at_date.tsn_qt,
            usage_at_date.tsn_qt
         )
   ;
   
    -- recalculate calculated params if any inventory has calculated param usages  
   SELECT
      COUNT(*)
   INTO
      ln_calc_parm_count     
   FROM
      (
         SELECT
            element3 as data_type_db_id,
            element4 as data_type_id
         FROM TABLE(ltb_trk_usages)
         UNION ALL
         SELECT
            element3 as data_type_db_id,
            element4 as data_type_id
         FROM TABLE(ltb_ac_usages)
         UNION ALL
         SELECT
            element3 as data_type_db_id,
            element4 as data_type_id
         FROM TABLE(ltb_sys_usages)
       ) inv_data_type      
   INNER JOIN mim_calc ON
      inv_data_type.data_type_db_id = mim_calc.data_type_db_id AND
      inv_data_type.data_type_id = mim_calc.data_type_id;
       
   IF (ln_calc_parm_count != 0) THEN
      recalculateEventCalcParms(an_event_db_id,an_event_id);
   END IF; 

END takeUsageSnapshotOfEvent;


/******************************************************************************
*
* Procedure:    recalculateEventCalcParms
*
* Description:  This procedure recalculates all the calculated parameters 
*               within the inventory usage snapshots associated with an event.
*
* Arguments:    an_event_db_id  event key
*               an_event_id     "
*
*******************************************************************************/
PROCEDURE recalculateEventCalcParms(
   an_event_db_id   IN NUMBER,
   an_event_id      IN NUMBER
)
IS
   ltbl_evt_inv StrStrStrTable;
BEGIN

   -- Get all the corresponding event_inv_id for the provided event.
   SELECT
      StrStrStrTuple(
         evt_inv.event_db_id,
         evt_inv.event_id,
         evt_inv.event_inv_id
      )
   BULK COLLECT INTO 
      ltbl_evt_inv
   FROM 
      evt_inv
   WHERE
      evt_inv.event_db_id = an_event_db_id AND
      evt_inv.event_id    = an_event_id
   ;

   -- Recalculate the calc parms within the usage snapshots 
   -- for each inventory associated to the event.
   FOR i in 1..ltbl_evt_inv.COUNT LOOP
      USAGE_PKG.update_calc_parms( ltbl_evt_inv(i) );
   END LOOP;

END recalculateEventCalcParms;


/******************************************************************************
*
* Function:     getAcftAssyUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided aircraft or assembly on the provided date.
*
* Arguments:    ain_inv_no_db_id  inventory key
*               ain_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
* Note: query copied from
*       mxcoreejb\src\main\resources\com\mxi\mx\core\query\usage\FindUsageAtDate.qrx
*
*******************************************************************************/
FUNCTION getAcftAssyUsageOnDate(
   aittb_inv_no_db_id  IN VARCHAR2TABLE,
   aittb_inv_no_id     IN VARCHAR2TABLE,
   aidt_date           IN DATE
) RETURN STRSTRSTRSTRFLOATTABLE
IS
   v_ttb_inv         STRSTRTABLE;
   v_ttb_usage_recs  STRSTRSTRSTRFLOATTABLE;
   v_ttb_usages      STRSTRSTRSTRFLOATTABLE;

BEGIN

   -- Filter the provided inventory keys to only those that are aircraft or assemblies.
   SELECT
      StrStrTuple(
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id
      )
   BULK COLLECT INTO v_ttb_inv  
   FROM
      TABLE (
         ARRAY_PKG.getStrStrTable (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      ) inv
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = inv.element1 AND
         inv_inv.inv_no_id    = inv.element2
         AND
         inv_inv.inv_class_db_id = 0 AND
         inv_inv.inv_class_cd IN ('ACFT','ASSY')
   ;
   
   IF v_ttb_inv.count = 0 THEN
      RETURN v_ttb_usages;      
   END IF;

   
   SELECT
      StrStrStrStrFloatTuple(
         usage.inv_no_db_id,
         usage.inv_no_id,
         usage.data_type_db_id,
         usage.data_type_id,
         NVL(usage.tsn_qt, 0)
      )
   BULK COLLECT INTO v_ttb_usage_recs
   FROM (
      SELECT
         usg_usage_data.inv_no_db_id,
         usg_usage_data.inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         usg_usage_data.tsn_qt - usg_usage_data.tsn_delta_qt AS tsn_qt,
         row_number() over (
            PARTITION BY
               usg_usage_data.inv_no_db_id,
               usg_usage_data.inv_no_id,
               usg_usage_data.data_type_db_id,
               usg_usage_data.data_type_id
            ORDER BY
               usg_usage_record.usage_dt ASC) rn
      FROM
         usg_usage_data
      INNER JOIN usg_usage_record ON
         usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
      INNER JOIN TABLE (v_ttb_inv) inv ON
         inv.element1 = usg_usage_data.inv_no_db_id AND
         inv.element2 = usg_usage_data.inv_no_id
      WHERE
         usg_usage_record.usage_dt >= aidt_date
   ) usage
   WHERE
      usage.rn = 1
   ;

   -- For any aircraft that do not have usage records after the provided date
   -- use their current usage.
   SELECT
      StrStrStrStrFloatTuple(
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id,
         NVL(usage_recs.data_type_db_id, inv_curr_usage.data_type_db_id),
         NVL(usage_recs.data_type_id, inv_curr_usage.data_type_id),
         NVL(NVL(usage_recs.tsn_qt, inv_curr_usage.tsn_qt),0)
      )
   BULK COLLECT INTO v_ttb_usages
   FROM
      TABLE (v_ttb_inv) inv
   INNER JOIN inv_curr_usage ON
      inv_curr_usage.inv_no_db_id = inv.element1 AND
      inv_curr_usage.inv_no_id    = inv.element2
   LEFT OUTER JOIN (
      (SELECT
          element1 as inv_no_db_id,
          element2 as inv_no_id,
          element3 as data_type_db_id,
          element4 as data_type_id,
          element5 as tsn_qt
       FROM
       TABLE (v_ttb_usage_recs)
      ) usage_recs
   ) ON
      usage_recs.inv_no_db_id    = inv_curr_usage.inv_no_db_id AND
      usage_recs.inv_no_id       = inv_curr_usage.inv_no_id AND
      usage_recs.data_type_db_id = inv_curr_usage.data_type_db_id AND
      usage_recs.data_type_id    = inv_curr_usage.data_type_id
   ;

   RETURN v_ttb_usages;

END getAcftAssyUsageOnDate;


/******************************************************************************
*
* Function:     getSysUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided SYS "inventory" on the provided date.
*
*               It is assumed that the aircraft/assembly under which the
*               SYS inventory is located will never move.
*               And that the usage of the SYS will always equal that of
*               the aircraft/assembly which it is under.
*
* Arguments:    ain_inv_no_db_id  inventory key
*               ain_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
* Note: query copied from
*       mxcoreejb\src\main\resources\com\mxi\mx\core\query\usage\FindUsageAtDate.qrx
*
*******************************************************************************/
FUNCTION getSysUsageOnDate(
   aittb_inv_no_db_id  IN VARCHAR2TABLE,
   aittb_inv_no_id     IN VARCHAR2TABLE,
   aidt_date           IN DATE
) RETURN STRSTRSTRSTRFLOATTABLE
IS
   v_ttb_inv           STRSTRSTRSTRTABLE;
   v_ttb_usage_recs    STRSTRSTRSTRFLOATTABLE;
   v_ttb_usages        STRSTRSTRSTRFLOATTABLE;

BEGIN

   SELECT
      StrStrStrStrTuple(
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         inv_inv.assmbl_inv_no_db_id,
         inv_inv.assmbl_inv_no_id
      )
   BULK COLLECT INTO v_ttb_inv  
   FROM
      TABLE (
         ARRAY_PKG.getStrStrTable (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      ) inv
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv.element1 AND
      inv_inv.inv_no_id    = inv.element2
      AND
      inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd = 'SYS'
   ;         
         
   IF v_ttb_inv.count = 0 THEN
      RETURN v_ttb_usages;
   END IF;
   
   
   SELECT
      StrStrStrStrFloatTuple(
         usage.inv_no_db_id,
         usage.inv_no_id,
         usage.data_type_db_id,
         usage.data_type_id,
         NVL(usage.tsn_qt, 0)
      )
   BULK COLLECT INTO v_ttb_usage_recs
   FROM (
      SELECT
         inv.element1 AS inv_no_db_id,
         inv.element2 AS inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         usg_usage_data.tsn_qt - usg_usage_data.tsn_delta_qt AS tsn_qt,
         row_number() over (
            PARTITION BY
               inv.element1,
               inv.element2,
               usg_usage_data.data_type_db_id,
               usg_usage_data.data_type_id
            ORDER BY
               usg_usage_record.usage_dt ASC) rn
      FROM
         usg_usage_data
      INNER JOIN usg_usage_record ON
         usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
      INNER JOIN TABLE(v_ttb_inv) inv ON
         inv.element3 = usg_usage_data.inv_no_db_id AND
         inv.element4 = usg_usage_data.inv_no_id
      WHERE
         usg_usage_record.usage_dt >= aidt_date
   ) usage
   WHERE
      usage.rn = 1
   ;

   -- For any systems that do not have usage records after the provided date
   -- use their current usage.
   SELECT
      StrStrStrStrFloatTuple(
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id,
         NVL(usage_recs.data_type_db_id, inv_curr_usage.data_type_db_id),
         NVL(usage_recs.data_type_id, inv_curr_usage.data_type_id),
         NVL(NVL(usage_recs.tsn_qt, inv_curr_usage.tsn_qt),0)
      )
   BULK COLLECT INTO v_ttb_usages
   FROM
      TABLE (v_ttb_inv) inv
   INNER JOIN inv_curr_usage ON
      inv_curr_usage.inv_no_db_id = inv.element1 AND
      inv_curr_usage.inv_no_id    = inv.element2
   LEFT OUTER JOIN (
      (SELECT
          element1 as inv_no_db_id,
          element2 as inv_no_id,
          element3 as data_type_db_id,
          element4 as data_type_id,
          element5 as tsn_qt
       FROM
       TABLE (v_ttb_usage_recs)
      ) usage_recs
   ) ON
      usage_recs.inv_no_db_id    = inv_curr_usage.inv_no_db_id AND
      usage_recs.inv_no_id       = inv_curr_usage.inv_no_id AND
      usage_recs.data_type_db_id = inv_curr_usage.data_type_db_id AND
      usage_recs.data_type_id    = inv_curr_usage.data_type_id
   ;

   RETURN v_ttb_usages;

END getSysUsageOnDate;


/******************************************************************************
*
* Function:     getTrkSerUsagesOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for any TRK or SER inventory contained within
*               the provided inventories on the provided date.
*
* Arguments:    aittb_inv_no_db_id  table of inventory keys
*               aittb_inv_no_id     "
*               aidt_date           date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages for any TRK or SER inventories
*
*******************************************************************************/
FUNCTION getTrkSerUsagesOnDate(
   aittb_inv_no_db_id  IN VARCHAR2TABLE,
   aittb_inv_no_id     IN VARCHAR2TABLE,
   aidt_date           IN DATE
) RETURN STRSTRSTRSTRFLOATTABLE
IS
   v_ttb_inventories STRSTRTABLE;
   v_ttb_usages  STRSTRSTRSTRFLOATTABLE;

BEGIN

   -- combine inventory key input arrays into a table
   SELECT
      StrStrTuple(
         element1, element2
      )
   BULK COLLECT INTO v_ttb_inventories
   FROM
      TABLE (
         ARRAY_PKG.getStrStrTable (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      );

    WITH
    -- provide a list of TRK / SER only
    trk_ser_list AS
    (
       SELECT
               inv_inv.inv_no_db_id,
               inv_inv.inv_no_id,
               inv_inv.nh_inv_no_db_id,
               inv_inv.nh_inv_no_id,
               inv_inv.assmbl_inv_no_db_id,
               inv_inv.assmbl_inv_no_id,
               inv_inv.h_inv_no_db_id,
               inv_inv.h_inv_no_id
       FROM
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id
             FROM
             TABLE (v_ttb_inventories)
         ) invlist
       INNER JOIN inv_inv
          ON
             inv_inv.inv_no_db_id = invlist.inv_no_db_id AND
             inv_inv.inv_no_id    = invlist.inv_no_id
       WHERE
          inv_inv.inv_class_db_id = 0 AND
          inv_inv.inv_class_cd IN ('TRK', 'SER')
    ),
    -- consolidate inventory install-remove paired list for trk/ser
    inv_onoff_list AS
    (
       SELECT
          rmvl.inv_no_db_id,
          rmvl.inv_no_id,
          rmvl.assmbl_inv_no_db_id,
          rmvl.assmbl_inv_no_id,
          NVL((
             SELECT
                MAX(inv_install.event_dt)
             FROM
                inv_install
             WHERE
                inv_install.inv_no_id    = rmvl.inv_no_id AND
                inv_install.inv_no_db_id = rmvl.inv_no_db_id
                AND
                inv_install.nh_inv_no_id    = rmvl.nh_inv_no_id AND
                inv_install.nh_inv_no_db_id = rmvl.nh_inv_no_db_id
                AND
                inv_install.assmbl_inv_no_id    = rmvl.assmbl_inv_no_id AND
                inv_install.assmbl_inv_no_db_id = rmvl.assmbl_inv_no_db_id
                AND
                inv_install.h_inv_no_id    = rmvl.h_inv_no_id AND
                inv_install.h_inv_no_db_id = rmvl.h_inv_no_db_id
                AND
                rmvl.remove_date >= inv_install.event_dt
                AND
                inv_install.event_dt >= aidt_date
          ), aidt_date ) AS install_date,
          rmvl.remove_date
       FROM
          (
             SELECT
                inv_remove.inv_no_db_id,
                inv_remove.inv_no_id,
                inv_remove.nh_inv_no_db_id,
                inv_remove.nh_inv_no_id,
                inv_remove.assmbl_inv_no_db_id,
                inv_remove.assmbl_inv_no_id,
                inv_remove.h_inv_no_db_id,
                inv_remove.h_inv_no_id,
                inv_remove.event_dt as remove_date
             FROM
                inv_remove
             INNER JOIN trk_ser_list
                ON
                   inv_remove.inv_no_db_id = trk_ser_list.inv_no_db_id AND
                   inv_remove.inv_no_id    = trk_ser_list.inv_no_id
             WHERE
                inv_remove.event_dt > aidt_date
             -- If our inventory is currently installed then there won't be a remove record for it
             -- create a record with removal date set to the current date
             UNION
             SELECT
                trk_ser_list.inv_no_db_id,
                trk_ser_list.inv_no_id,
                trk_ser_list.nh_inv_no_db_id,
                trk_ser_list.nh_inv_no_id,
                trk_ser_list.assmbl_inv_no_db_id,
                trk_ser_list.assmbl_inv_no_id,
                trk_ser_list.h_inv_no_db_id,
                trk_ser_list.h_inv_no_id,
                sysdate
             FROM
                trk_ser_list
             WHERE
             -- assmble inv may be null but it still installed. We found such data in client database,
             -- we can NOT confirm if it is bad data (due to migration) or somewhere in maintenix will create such data.
                (
                trk_ser_list.assmbl_inv_no_id IS NOT NULL
                OR
                   (
                    trk_ser_list.inv_no_db_id != trk_ser_list.h_inv_no_db_id OR
                    trk_ser_list.inv_no_id    != trk_ser_list.h_inv_no_id
                   )
                )
          ) rmvl
    ),
    -- get total tsn delta for trk/ser
    inv_usage_delta AS
    (
       SELECT
          inv_onoff_list.inv_no_db_id,
          inv_onoff_list.inv_no_id,
          usg_usage_data.data_type_db_id,
          usg_usage_data.data_type_id,
          SUM (usg_usage_data.tsn_delta_qt) AS tsn_delta
       FROM
          usg_usage_record
       INNER JOIN usg_usage_data
          ON
             usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
       INNER JOIN inv_onoff_list
          ON
             usg_usage_record.usage_dt >= inv_onoff_list.install_date AND
             usg_usage_record.usage_dt <= inv_onoff_list.remove_date
       WHERE
             usg_usage_record.usage_dt > aidt_date
             AND
                (
                   (
                      inv_onoff_list.inv_no_db_id = usg_usage_data.inv_no_db_id AND
                      inv_onoff_list.inv_no_id    = usg_usage_data.inv_no_id
                      AND
                      usg_usage_record.usage_type_cd = 'MXCORRECTION'  -- edit inv
                   )
                   OR
                   (
                      inv_onoff_list.assmbl_inv_no_db_id = usg_usage_data.inv_no_db_id AND
                      inv_onoff_list.assmbl_inv_no_id    = usg_usage_data.inv_no_id
                      AND
                      usg_usage_record.usage_type_cd IN ('MXFLIGHT', 'MXACCRUAL')  -- flight or usage record
                   )
                )
       GROUP BY
          inv_onoff_list.inv_no_db_id, inv_onoff_list.inv_no_id, usg_usage_data.data_type_db_id, usg_usage_data.data_type_id
    )
    -- finally, we calculate historic tsn at that moment
    SELECT
       StrStrStrStrFloatTuple(
          inv_curr_usage.inv_no_db_id,
          inv_curr_usage.inv_no_id,
          inv_curr_usage.data_type_db_id,
          inv_curr_usage.data_type_id,
          inv_curr_usage.tsn_qt - nvl(inv_usage_delta.tsn_delta, 0)
       )
    BULK COLLECT INTO v_ttb_usages
    FROM inv_curr_usage
    INNER JOIN trk_ser_list
       ON
          inv_curr_usage.inv_no_db_id = trk_ser_list.inv_no_db_id AND
          inv_curr_usage.inv_no_id    = trk_ser_list.inv_no_id
    LEFT JOIN inv_usage_delta
       ON
          inv_curr_usage.inv_no_db_id = inv_usage_delta.inv_no_db_id AND
          inv_curr_usage.inv_no_id    = inv_usage_delta.inv_no_id
          AND
          inv_curr_usage.data_type_db_id = inv_usage_delta.data_type_db_id AND
          inv_curr_usage.data_type_id    = inv_usage_delta.data_type_id
    ;

   RETURN v_ttb_usages;

EXCEPTION
   WHEN OTHERS THEN
      application_object_pkg.SetMxiError('DEV-99999', 'USAGE_PKG@@@getTrkSerUsageOnDate@@@' || SQLERRM);

END getTrkSerUsagesOnDate;


/******************************************************************************
*
* Function:     getInvUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided inventory on the provided date.
*
* Arguments:    ai_inv_no_db_id  inventory key
*               ai_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
*******************************************************************************/
FUNCTION getInvUsageOnDate(
   ai_inv_no_db_id  IN NUMBER,
   ai_inv_no_id     IN NUMBER,
   aidt_date        IN DATE
) RETURN STRSTRSTRSTRFLOATTABLE
IS
   v_ttb_usages        STRSTRSTRSTRFLOATTABLE;
   ltb_inv_no_db_ids   VARCHAR2TABLE;
   ltb_inv_no_ids      VARCHAR2TABLE;

   ld_inv_class_cd     inv_inv.inv_class_cd%TYPE;

BEGIN

   SELECT
      ai_inv_no_db_id
   BULK COLLECT INTO ltb_inv_no_db_ids
   FROM
      dual;

   SELECT
      ai_inv_no_id
   BULK COLLECT INTO ltb_inv_no_ids
   FROM
      dual;

   SELECT
      inv_inv.inv_class_cd
   INTO
      ld_inv_class_cd
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = ai_inv_no_db_id AND
      inv_inv.inv_no_id    = ai_inv_no_id;

   IF (ld_inv_class_cd = 'ACFT') OR (ld_inv_class_cd = 'ASSY') THEN
      v_ttb_usages := getAcftAssyUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, aidt_date);
   
   ELSIF (ld_inv_class_cd = 'SYS') THEN
      v_ttb_usages := getSysUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, aidt_date);   
      
   ELSE
      v_ttb_usages := getTrkSerUsagesOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, aidt_date);

   END IF;

   RETURN v_ttb_usages;

END getInvUsageOnDate;


END USAGE_PKG;
/