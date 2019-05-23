--liquibase formatted sql


--changeSet MX-22059:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE USAGE_PKG
IS

/******************************************************************************
* TYPE DECLARATIONS
******************************************************************************/

/* constant declarations (error codes) */
icn_Success                CONSTANT NUMBER := 1;
icn_NoProc                 CONSTANT NUMBER := 0;
icn_MissingInputOnInv      CONSTANT NUMBER := -4;
icn_BadEqnName             CONSTANT NUMBER := -5;
icn_OracleError            CONSTANT NUMBER := -100;

/* subtype declarations */
SUBTYPE typn_Id    IS inv_inv.inv_no_id%TYPE;
SUBTYPE typv_Cd    IS ref_data_source.data_source_cd%TYPE;
SUBTYPE typv_Sdesc IS inv_inv.inv_no_sdesc%TYPE;

/******************************************************************************
*
* Procedure:    UsageCalculations
* Arguments:    an_InvNoDbId (number): The inv_no whose
*                          calculations will be updated
*               an_InvNoId (number):   ""
*               on_Return (number): Return 1 means success, <0 means failure
* Description:  This procedure will calculate and apply any calculated parms
*               for a given inventory. It will trigger part-specific
*               calculations where required.
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: Hayley Clark
* Recent Date:  9.JUN.2000
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UsageCalculations (
      an_InvNoDbId      IN typn_Id,
      an_InvNoId        IN typn_Id,
      on_Return         OUT NUMBER
   );


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
* Recent Coder: Hayley Clark
* Recent Date:  31.MAY.2000
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
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
   );



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
   );

END USAGE_PKG;
/

--changeSet MX-22059:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY USAGE_PKG
IS

   /*Package Exceptions*/	
   xc_BadEqnName           EXCEPTION;
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
             inv_inv.rstat_cd	= 0
             AND
             mim_calc.assmbl_db_id = inv_inv.assmbl_db_id AND
             mim_calc.assmbl_cd    = inv_inv.assmbl_cd
      UNION
      SELECT mim_calc.calc_db_id,
             mim_calc.calc_id
      FROM inv_inv,
             mim_calc
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId AND
             inv_inv.inv_no_id    = cn_InvNoId	 AND
             inv_inv.rstat_cd	= 0
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
         AND inv_inv.rstat_cd 	         = 0
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

   /* LOCAL VARIABLES */
   lv_CalcTSNFunction       VARCHAR2(32760); /* dynamic SQL statement to retreive calculated TSN */
   lv_CalcTSOFunction       VARCHAR2(32760); /* dynamic SQL statement to retrieve calculated TSO*/
   lv_CalcTSIFunction       VARCHAR2(32760); /* dynamic SQL statement to retreive calculated TSI*/
   
   ln_CalcTSNResult      NUMBER;      /* TSN result from the calculation function */
   ln_CalcTSOResult      NUMBER;      /* TSO result from the calculation function */
   ln_CalcTSIResult      NUMBER;      /* TSI result from the calculation function */

   ln_ApplyDeltaQt       NUMBER;  /* usage delta to apply to the inv tree */
   ln_NumRows            NUMBER;  /* used as return from COUNT(*) queries */
   lv_Inventory          typv_Sdesc; /* names used if an error occurs */
   lv_CalcName           VARCHAR2(80);    /* value from mim_data_type.data_type_cd  */

   /* EXCEPTIONS */
   xc_MissingInputOnInv    EXCEPTION;


   /* CURSOR DECLARATIONS */
   /* cursor used to get the equation's BOM specific calculation */
   CURSOR lcur_Equation (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id,
         cn_CalcDbId       typn_Id,
         cn_CalcId         typn_Id
      ) IS
      /* get the default eqn if there is no BOM specific equations */
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
               /* search for custom eqns */
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
                  AND inv_inv.rstat_cd	   = 0
                  AND mim_part_numdata.assmbl_db_id  = inv_inv.orig_assmbl_db_id
                  AND mim_part_numdata.assmbl_cd     = inv_inv.orig_assmbl_cd
                  AND mim_part_numdata.assmbl_bom_id = 0
                  AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
                  AND mim_part_numdata.data_type_id    = mim_calc.data_type_id                  
                  AND mim_part_numdata.eqn_ldesc IS NOT NULL )
      UNION ALL
      /* get the custom eqn */
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
         AND inv_inv.rstat_cd	  = 0
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
         AND inv_inv.rstat_cd	  = 0
         AND mim_part_numdata.assmbl_db_id  = inv_inv.orig_assmbl_db_id
         AND mim_part_numdata.assmbl_cd     = inv_inv.orig_assmbl_cd
         AND mim_part_numdata.assmbl_bom_id = 0
         AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
         AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
         AND mim_part_numdata.eqn_ldesc IS NOT NULL
         AND mim_data_type.data_type_db_id = mim_calc.data_type_db_id
         AND mim_data_type.data_type_id    = mim_calc.data_type_id ;
   lrec_Equation lcur_Equation%ROWTYPE;

   /* cursor used to get the custom input values for tsn */
   CURSOR lcur_InputValues (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id,
         cn_CalcDbId       typn_Id,
         cn_CalcId         typn_Id
      ) IS
      /* get data_type inputs (never part-specific) */
      SELECT inv_curr_usage.tsn_qt tsn_value, 
             mim_calc_input.input_ord,
             inv_curr_usage.tso_qt tso_value,
             inv_curr_usage.tsi_qt tsi_value
        FROM inv_inv,
             mim_calc_input,
             inv_curr_usage
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd	  = 0
         AND mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_calc_input.constant_bool = 0
         AND inv_curr_usage.inv_no_db_id = inv_inv.inv_no_db_id
         AND inv_curr_usage.inv_no_id    = inv_inv.inv_no_id
         AND inv_curr_usage.data_type_db_id = mim_calc_input.data_type_db_id
         AND inv_curr_usage.data_type_id    = mim_calc_input.data_type_id
      UNION ALL
      /* get non part-specific constant values */
      SELECT mim_calc_input.input_qt tsn_value, 
             mim_calc_input.input_ord,
             mim_calc_input.input_qt tso_value,
             mim_calc_input.input_qt tsi_value
        FROM mim_calc_input
       WHERE mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_calc_input.constant_bool = 1
         AND NOT EXISTS
             /* make sure there are no part-specific constant values */
             ( SELECT 1
                 FROM inv_inv,
                      mim_part_input,
                      eqp_bom_part
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.assmbl_bom_id <> 0
                  AND inv_inv.rstat_cd	  = 0
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
                  AND inv_inv.rstat_cd	   = 0
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
      /* get part-specific constant values */
      (SELECT mim_part_input.input_qt tsn_value,
              mim_calc_input.input_ord,
              mim_part_input.input_qt tso_value,
              mim_part_input.input_qt tsi_value
        FROM inv_inv,
             mim_calc_input,
             mim_part_input,
             eqp_bom_part
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.assmbl_bom_id <> 0
         AND inv_inv.rstat_cd	  = 0
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
       SELECT mim_part_input.input_qt tsn_value,
             mim_calc_input.input_ord,
             mim_part_input.input_qt tso_value,
             mim_part_input.input_qt tsi_value
        FROM inv_inv,
             mim_calc_input,
             mim_part_input,
             eqp_bom_part
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd	  = 0
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

   -- Initialize the return value
   on_return := icn_NoProc;

   /* get the inventory name */
   SELECT inv_no_sdesc
     INTO lv_Inventory
     FROM inv_inv
    WHERE inv_no_db_id = an_InvNoDbId
      AND inv_no_id    = an_InvNoId
      AND inv_inv.rstat_cd	  = 0;

   /* get the equation */
   OPEN lcur_Equation(an_InvNoDbId, an_InvNoId, an_CalcDbId, an_CalcId);
   FETCH lcur_Equation INTO lrec_Equation;
   CLOSE lcur_Equation;

   /* record the calculation name */
   lv_CalcName := lrec_Equation.data_type_cd;

   /* make sure that all of the input parms are specified for the
      inventory */
   SELECT COUNT(calc_db_id)
     INTO ln_NumRows
     FROM mim_calc_input
    WHERE calc_db_id = an_CalcDbId
      AND calc_id    = an_CalcId
      AND constant_bool = 0
      AND NOT EXISTS
          ( SELECT 1
              FROM inv_curr_usage
             WHERE inv_no_db_id = an_InvNoDbId
               AND inv_no_id    = an_InvNoId
               AND data_type_db_id = mim_calc_input.data_type_db_id
               AND data_type_id    = mim_calc_input.data_type_id );
   IF ln_NumRows > 0 THEN
      RAISE xc_MissingInputOnInv;
   END IF;

   /* create the dynamic SQL statements */
   lv_CalcTSNFunction := 'BEGIN :res := ' || lrec_Equation.eqn_ldesc || '(';
   lv_CalcTSOFunction := 'BEGIN :res := ' || lrec_Equation.eqn_ldesc || '(';
   lv_CalcTSIFunction := 'BEGIN :res := ' || lrec_Equation.eqn_ldesc || '(';
   FOR lrec_InputValues IN lcur_InputValues(
                     an_InvNoDbId, an_InvNoId,
                     an_CalcDbId, an_CalcId)
   LOOP
      lv_CalcTSNFunction := lv_CalcTSNFunction ||  REPLACE(lrec_InputValues.tsn_value,',','.') || ',';      
      lv_CalcTSOFunction := lv_CalcTSOFunction ||  REPLACE(lrec_InputValues.tso_value,',','.') || ',';
      lv_CalcTSIFunction := lv_CalcTSIFunction ||  REPLACE(lrec_InputValues.tsi_value,',','.') || ',';
   END LOOP;
   lv_CalcTSNFunction := RTRIM(lv_CalcTSNFunction, ',') || ');END;';
   lv_CalcTSOFunction := RTRIM(lv_CalcTSOFunction, ',') || ');END;';
   lv_CalcTSIFunction := RTRIM(lv_CalcTSIFunction, ',') || ');END;';
   
   /*GET THE CALCULATED TSN*/      
   Calculate(lv_CalcTSNFunction, lrec_Equation.Entry_Prec_Qt, ln_CalcTSNResult);

   /*GET THE CALCULATED TSO*/
   Calculate(lv_CalcTSOFunction, lrec_Equation.Entry_Prec_Qt, ln_CalcTSOResult);
   
   /*GET THE CALCULATED TSI*/
   Calculate(lv_CalcTSIFunction, lrec_Equation.Entry_Prec_Qt, ln_CalcTSIResult);
   

   /* apply the new values to all inventory in the assembly */
   UPDATE inv_curr_usage
      SET tsn_qt = ln_CalcTSNResult,
          tso_qt = ln_CalcTSOResult,
          tsi_qt = ln_CalcTSIResult
    WHERE data_type_db_id = lrec_Equation.data_type_db_id
      AND data_type_id    = lrec_Equation.data_type_id
      AND inv_no_db_id    = an_InvNoDbId
      AND inv_no_id       = an_InvNoId;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN xc_MissingInputOnInv THEN
      on_Return := icn_MissingInputOnInv;
      application_object_pkg.SetMxiError('BUS-00275',  lv_CalcName);
   WHEN xc_BadEqnName THEN
      on_Return := icn_BadEqnName;      
      application_object_pkg.SetMxiError('BUS-00276', lv_CalcName);      
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

END USAGE_PKG;

/