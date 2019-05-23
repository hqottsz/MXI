--liquibase formatted sql

--changeset OPER-9182:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
);


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
) RETURN STRSTRSTRSTRFLOATTABLE;


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
) RETURN STRSTRSTRSTRFLOATTABLE;


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
) RETURN STRSTRSTRSTRFLOATTABLE;


END USAGE_PKG;
/
--changeset OPER-9182:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   lv_MissingDataType    mim_data_type.data_type_cd%TYPE;

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
      WHERE calc_db_id = an_CalcDbId
        AND calc_id    = an_CalcId
        AND constant_bool = 0
        AND NOT EXISTS
            ( SELECT 1
                FROM inv_curr_usage
               WHERE inv_no_db_id = an_InvNoDbId
                 AND inv_no_id    = an_InvNoId
                 AND data_type_db_id = mim_calc_input.data_type_db_id
                 AND data_type_id    = mim_calc_input.data_type_id
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
      application_object_pkg.SetMxiError('BUS-00275',  an_InvNoDbId || ':' || an_InvNoId || '|' || lv_CalcName || '|' || lv_MissingDataType);
   WHEN xc_BadEqnName THEN
      on_Return := icn_BadEqnName;
      application_object_pkg.SetMxiError('BUS-00276', an_InvNoDbId || ':' || an_InvNoId || '|' || lv_CalcName || '|' || lv_CalcTSIFunction);
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
             UNION
             SELECT
                element1 as inv_no_db_id,
                element2 as inv_no_id,
                element3 as data_type_db_id,
                element4 as data_type_id,
                element5 as hist_tsn
             FROM TABLE(ltb_ac_usages)
             UNION
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

END takeUsageSnapshotOfEvent;


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
   v_ttb_usage_recs  STRSTRSTRSTRFLOATTABLE;
   v_ttb_usages      STRSTRSTRSTRFLOATTABLE;

BEGIN

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
      INNER JOIN
         TABLE (
            ARRAY_PKG.getStrStrTable (
               aittb_inv_no_db_id,
               aittb_inv_no_id
            )
         ) inv
         ON
         inv.element1 = usg_usage_data.inv_no_db_id AND
         inv.element2 = usg_usage_data.inv_no_id
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = usg_usage_data.inv_no_db_id AND
         inv_inv.inv_no_id    = usg_usage_data.inv_no_id
         AND
         inv_inv.inv_class_db_id = 0 AND
         inv_inv.inv_class_cd IN ('ACFT','ASSY')
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
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         NVL(usage_recs.data_type_db_id, inv_curr_usage.data_type_db_id),
         NVL(usage_recs.data_type_id, inv_curr_usage.data_type_id),
         NVL(NVL(usage_recs.tsn_qt, inv_curr_usage.tsn_qt),0)
      )
   BULK COLLECT INTO v_ttb_usages      
   FROM
      TABLE (
         ARRAY_PKG.getStrStrTable (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      ) inv
   INNER JOIN inv_inv ON
      inv.element1 = inv_inv.inv_no_db_id AND
      inv.element2 = inv_inv.inv_no_id
   INNER JOIN inv_curr_usage ON
      inv_curr_usage.inv_no_db_id = inv_inv.inv_no_db_id AND
      inv_curr_usage.inv_no_id    = inv_inv.inv_no_id
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
   WHERE
      inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd IN ('ACFT','ASSY')
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
   v_ttb_usage_recs    STRSTRSTRSTRFLOATTABLE;
   v_ttb_usages        STRSTRSTRSTRFLOATTABLE;

BEGIN

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
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         usg_usage_data.tsn_qt - usg_usage_data.tsn_delta_qt AS tsn_qt,
         row_number() over (
            PARTITION BY
               inv_inv.inv_no_db_id,
               inv_inv.inv_no_id,
               usg_usage_data.data_type_db_id,
               usg_usage_data.data_type_id
            ORDER BY
               usg_usage_record.usage_dt ASC) rn
      FROM
         usg_usage_data
      INNER JOIN usg_usage_record ON
         usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
      INNER JOIN inv_inv ON
         inv_inv.assmbl_inv_no_db_id = usg_usage_data.inv_no_db_id AND
         inv_inv.assmbl_inv_no_id    = usg_usage_data.inv_no_id
         AND
         inv_inv.inv_class_db_id = 0 AND
         inv_inv.inv_class_cd = 'SYS'
      INNER JOIN
         TABLE (
            ARRAY_PKG.getStrStrTable (
               aittb_inv_no_db_id,
               aittb_inv_no_id
            )
         ) inv
         ON
         inv.element1 = inv_inv.inv_no_db_id AND
         inv.element2 = inv_inv.inv_no_id
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
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         NVL(usage_recs.data_type_db_id, inv_curr_usage.data_type_db_id),
         NVL(usage_recs.data_type_id, inv_curr_usage.data_type_id),
         NVL(NVL(usage_recs.tsn_qt, inv_curr_usage.tsn_qt),0)
      )
   BULK COLLECT INTO v_ttb_usages      
   FROM
      TABLE (
         ARRAY_PKG.getStrStrTable (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      ) inv
   INNER JOIN inv_inv ON
      inv.element1 = inv_inv.inv_no_db_id AND
      inv.element2 = inv_inv.inv_no_id
   INNER JOIN inv_curr_usage ON
      inv_curr_usage.inv_no_db_id = inv_inv.inv_no_db_id AND
      inv_curr_usage.inv_no_id    = inv_inv.inv_no_id
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
   WHERE
      inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd = 'SYS'
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

END USAGE_PKG;

/
--changeset OPER-9182:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY FLIGHT_OUT_OF_SEQUENCE_PKG IS

   c_pkg_name CONSTANT VARCHAR2(30) := 'FLIGHT_OUT_OF_SEQUENCE_PKG';

   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);


FUNCTION get_adjusted_usage_delta(
   airaw_usage_adjustment_id   IN RAW,
   aittb_assy_usage_deltadata  IN STRSTRSTRSTRFLOATTABLE
) RETURN STRSTRSTRSTRFLOATTABLE
IS
   v_method_name VARCHAR2(30) := 'get_adjusted_usage_delta';

   v_ttb_adjusted_usage_delta STRSTRSTRSTRFLOATTABLE;

BEGIN

   v_step := 10;

   SELECT
      StrStrStrStrFloatTuple(
         usg_usage_data.inv_no_db_id,
         usg_usage_data.inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         new_usage_delta.new_delta - usg_usage_data.tsn_delta_qt
      )
   BULK COLLECT INTO v_ttb_adjusted_usage_delta
   FROM
      (SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id,
         element3 as data_type_db_id,
         element4 as data_type_id,
         element5 as new_delta
       FROM
       TABLE (aittb_assy_usage_deltadata)
      ) new_usage_delta
      INNER JOIN usg_usage_data ON
         usg_usage_data.inv_no_db_id = new_usage_delta.assmbl_inv_db_id AND
         usg_usage_data.inv_no_id    = new_usage_delta.assmbl_inv_id
         AND
         usg_usage_data.data_type_db_id = new_usage_delta.data_type_db_id AND
         usg_usage_data.data_type_id    = new_usage_delta.data_type_id
   WHERE
      usg_usage_data.usage_record_id = airaw_usage_adjustment_id
      AND
      usg_usage_data.tsn_delta_qt != new_usage_delta.new_delta;

   RETURN v_ttb_adjusted_usage_delta;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END get_adjusted_usage_delta;


FUNCTION get_acft_hist_config(
   airaw_usage_adjustment_id IN RAW
) RETURN STRSTRSTRSTRTABLE
IS
   v_method_name VARCHAR2(30) := 'get_acft_hist_config';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;
   v_ttb_assmbl STRSTRTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;

BEGIN

   v_step := 10;

   SELECT
      inv_no_db_id, inv_no_id, usage_dt
   INTO
      vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt
   FROM
      usg_usage_record
   WHERE
      usage_record_id = airaw_usage_adjustment_id;

   v_step := 20;

   SELECT
      StrStrTuple(inv_no_db_id, inv_no_id)
   BULK COLLECT INTO
      v_ttb_assmbl
   FROM (
      SELECT DISTINCT
         inv_no_db_id, inv_no_id
      FROM
         usg_usage_data
      WHERE
         usage_record_id = airaw_usage_adjustment_id
   );

   v_step := 30;

   SELECT
      StrStrStrStrTuple(
         inv_hist.inv_no_db_id,
         inv_hist.inv_no_id,
         -- if inv_no_db_id/inv_no_id = element1/2, it is an assy itself
         -- if assmbl_inv_no_db_id/id is null, using acft instead
         CASE WHEN inv_hist.inv_no_db_id = usage_assmbl.assmbl_inv_db_id AND
                   inv_hist.inv_no_id    = usage_assmbl.assmbl_inv_id
              THEN inv_hist.inv_no_db_id
              ELSE NVL(inv_hist.assmbl_inv_no_db_id, vi_acft_inv_db_id)
         END,
         CASE WHEN inv_hist.inv_no_db_id = usage_assmbl.assmbl_inv_db_id AND
                   inv_hist.inv_no_id    = usage_assmbl.assmbl_inv_id
              THEN inv_hist.inv_no_id
              ELSE NVL(inv_hist.assmbl_inv_no_id, vi_acft_inv_id)
         END
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM
      (
      SELECT
         ROW_NUMBER() OVER (PARTITION BY inv_no_db_id, inv_no_id, nh_inv_no_db_id, nh_inv_no_id, assmbl_inv_no_db_id, assmbl_inv_no_id  ORDER BY 1) AS rn,
         all_rec.*
      FROM
      (
         -- current
         SELECT
            inv_inv.inv_no_db_id,        inv_inv.inv_no_id,
            inv_inv.nh_inv_no_db_id,     inv_inv.nh_inv_no_id,
            inv_inv.assmbl_inv_no_db_id, inv_inv.assmbl_inv_no_id
         FROM inv_inv
         WHERE
            inv_inv.h_inv_no_db_id = vi_acft_inv_db_id AND
            inv_inv.h_inv_no_id    = vi_acft_inv_id
         UNION ALL
         -- removed
         SELECT
            inv_remove.inv_no_db_id,        inv_remove.inv_no_id,
            inv_remove.nh_inv_no_db_id,     inv_remove.nh_inv_no_id,
            inv_remove.assmbl_inv_no_db_id, inv_remove.assmbl_inv_no_id
         FROM inv_remove
         WHERE
            inv_remove.event_dt >= vi_usage_dt
            AND
            inv_remove.h_inv_no_db_id = vi_acft_inv_db_id AND
            inv_remove.h_inv_no_id    = vi_acft_inv_id

      ) all_rec
      MINUS
      -- installed
      SELECT
         ROW_NUMBER() OVER (PARTITION BY inv_install.inv_no_db_id, inv_install.inv_no_id, inv_install.nh_inv_no_db_id, inv_install.nh_inv_no_id, inv_install.assmbl_inv_no_db_id, inv_install.assmbl_inv_no_id  ORDER BY 1) AS rn,
         inv_install.inv_no_db_id,        inv_install.inv_no_id,
         inv_install.nh_inv_no_db_id,     inv_install.nh_inv_no_id,
         inv_install.assmbl_inv_no_db_id, inv_install.assmbl_inv_no_id
      FROM inv_install
      WHERE
         inv_install.event_dt >= vi_usage_dt
         AND
         inv_install.h_inv_no_db_id = vi_acft_inv_db_id AND
         inv_install.h_inv_no_id    = vi_acft_inv_id
      ) inv_hist
   LEFT JOIN
      (SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id
       FROM
          TABLE(v_ttb_assmbl)
      ) usage_assmbl
      ON
         usage_assmbl.assmbl_inv_db_id = inv_hist.inv_no_db_id AND
         usage_assmbl.assmbl_inv_id    = inv_hist.inv_no_id
   ;

   RETURN v_ttb_inv_assmbl;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END get_acft_hist_config;


FUNCTION filterInventoryHavingOverhaul(
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
) RETURN STRSTRSTRSTRTABLE
IS
   v_method_name VARCHAR2(30) := 'filterInventoryHavingOverhaul';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

BEGIN

   v_step := 10;

   WITH
   inv_assmbl_list AS
   (
      SELECT
         element1 as inv_no_db_id,
         element2 as inv_no_id,
         element3 as assmbl_inv_db_id,
         element4 as assmbl_inv_id
       FROM
          TABLE (aittb_inv_assmbl)
   )
   SELECT
      StrStrStrStrTuple(
         inv_assmbl_list.inv_no_db_id,
         inv_assmbl_list.inv_no_id,
         inv_assmbl_list.assmbl_inv_db_id,
         inv_assmbl_list.assmbl_inv_id
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM inv_assmbl_list
   INNER JOIN (
     SELECT
        to_number(inv_no_db_id) as inv_no_db_id,
        to_number(inv_no_id) as inv_no_id
     FROM inv_assmbl_list
     MINUS
     SELECT DISTINCT
        sched_stask.main_inv_no_db_id,
        sched_stask.main_inv_no_id
     FROM
        sched_stask
     INNER JOIN evt_event ON
        sched_stask.sched_db_id = evt_event.event_db_id AND
        sched_stask.sched_id    = evt_event.event_id
     WHERE
        sched_stask.task_class_db_id = 0 AND
        sched_stask.task_class_cd = 'OVHL'
        AND
        evt_event.event_status_db_id = 0 AND
        evt_event.event_status_cd    = 'COMPLETE'
        AND
        -- select any overhauls occurring after the date ... so that their inventories may be filtered from the list
        evt_event.event_dt >= ai_usage_dt
   ) filtered ON
      inv_assmbl_list.inv_no_db_id = filtered.inv_no_db_id AND
      inv_assmbl_list.inv_no_id    = filtered.inv_no_id
   ;

   RETURN v_ttb_inv_assmbl;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END filterInventoryHavingOverhaul;


FUNCTION filterInventoryReinstalled(
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
) RETURN STRSTRSTRSTRTABLE
IS
   v_method_name VARCHAR2(30) := 'filterInventoryReinstalled';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

BEGIN

   v_step := 10;

   WITH
   inv_assmbl_list AS
   (
      SELECT
         element1 as inv_no_db_id,
         element2 as inv_no_id,
         element3 as assmbl_inv_db_id,
         element4 as assmbl_inv_id
       FROM
          TABLE (aittb_inv_assmbl)
   )
   SELECT
      StrStrStrStrTuple(
         inv_assmbl_list.inv_no_db_id,
         inv_assmbl_list.inv_no_id,
         inv_assmbl_list.assmbl_inv_db_id,
         inv_assmbl_list.assmbl_inv_id
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM inv_assmbl_list
   INNER JOIN (
     SELECT
        to_number(inv_no_db_id) as inv_no_db_id,
        to_number(inv_no_id) as inv_no_id
     FROM inv_assmbl_list
     MINUS
     SELECT DISTINCT
        evt_inv.inv_no_db_id,
        evt_inv.inv_no_id
     FROM
        evt_event
     INNER JOIN evt_inv ON
        evt_event.event_db_id = evt_inv.event_db_id AND
        evt_event.event_id    = evt_inv.event_id
     WHERE
        evt_event.event_status_db_id = 0 AND
        evt_event.event_status_cd = 'FGINST'
        AND
        -- select any installation occurring after the date ... so that their inventories may be filtered from the list
        evt_event.event_dt >= ai_usage_dt
        AND
        --select main inventory only
        evt_inv.main_inv_bool = 1
   ) filtered ON
      inv_assmbl_list.inv_no_db_id = filtered.inv_no_db_id AND
      inv_assmbl_list.inv_no_id    = filtered.inv_no_id
   ;

   RETURN v_ttb_inv_assmbl;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END filterInventoryReinstalled;


PROCEDURE updateFollowingTaskDeadlines (
   airaw_usage_adjustment_id  IN RAW,
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl           IN STRSTRSTRSTRTABLE,
   ai_usage_dt                IN DATE,
   aiv_system_note            IN VARCHAR2,
   aiv_deadline_system_note   IN VARCHAR2,
   ain_hr_db_id               IN NUMBER,
   ain_hr_id                  IN NUMBER,
   aottb_deadlines            OUT STRSTRSTRSTRTABLE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingTaskDeadlines';

   v_ttb_inv_no_db_ids VARCHAR2TABLE;
   v_ttb_inv_no_ids    VARCHAR2TABLE;
   v_ttb_trkser_hist_tsn STRSTRSTRSTRFLOATTABLE;

   CURSOR lcur_snapshot IS
   SELECT
         evt_inv.event_db_id,
         evt_inv.event_id,
         evt_event.event_status_db_id,
         evt_event.event_status_cd,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta,
         ain_hr_db_id AS hr_db_id,
         ain_hr_id AS hr_id,
         ROW_NUMBER() OVER (PARTITION BY evt_inv.event_id, evt_inv.event_db_id  ORDER BY evt_inv.event_id) AS rn
      FROM
         (SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
      INNER JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as assmbl_inv_db_id,
               element4 as assmbl_inv_id
             FROM
             TABLE (aittb_inv_assmbl)
         ) invlist
         ON
            invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
            invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      INNER JOIN usg_usage_data
         ON
            usg_usage_data.usage_record_id = airaw_usage_adjustment_id
            AND
            usg_usage_data.inv_no_db_id    = adjusted_usage_delta.assmbl_inv_db_id AND
            usg_usage_data.inv_no_id       = adjusted_usage_delta.assmbl_inv_id
            AND
            usg_usage_data.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
            usg_usage_data.data_type_id    = adjusted_usage_delta.data_type_id
      LEFT JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as hist_tsn
            FROM TABLE (v_ttb_trkser_hist_tsn)
         ) trkser_tsn
         ON
            trkser_tsn.inv_no_db_id = invlist.inv_no_db_id AND
            trkser_tsn.inv_no_id    = invlist.inv_no_id
            AND
            adjusted_usage_delta.data_type_db_id = trkser_tsn.data_type_db_id AND
            adjusted_usage_delta.data_type_id    = trkser_tsn.data_type_id
      INNER JOIN evt_inv
         ON
            evt_inv.inv_no_db_id = invlist.inv_no_db_id AND
            evt_inv.inv_no_id    = invlist.inv_no_id
            AND
            evt_inv.main_inv_bool = 1
      INNER JOIN evt_event
         ON
            evt_inv.event_db_id = evt_event.event_db_id AND
            evt_inv.event_id    = evt_event.event_id
            AND
            evt_event.event_type_db_id = 0 AND
            evt_event.event_type_cd    = 'TS'
            AND
            evt_event.event_status_db_id = 0 AND
            evt_event.event_status_cd in ('ACTV', 'COMPLETE', 'IN WORK', 'PAUSE')
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_id    = evt_event.event_id AND
            evt_sched_dead.event_db_id = evt_event.event_db_id
            AND
            evt_sched_dead.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
            evt_sched_dead.data_type_id    = adjusted_usage_delta.data_type_id
            AND
            evt_sched_dead.sched_from_cd != 'BIRTH'
      LEFT JOIN evt_event_rel
         ON -- look for previous task
            evt_event_rel.rel_event_db_id = evt_event.event_db_id AND
            evt_event_rel.rel_event_id    = evt_event.event_id 
            AND
            evt_event_rel.rel_type_db_id = 0 AND
            evt_event_rel.rel_type_cd    = 'DEPT'
      LEFT JOIN evt_event previous_task_event
         ON
            previous_task_event.event_db_id = evt_event_rel.event_db_id AND
            previous_task_event.event_id    = evt_event_rel.event_id
      LEFT JOIN sched_stask
         ON -- if the task for a fault
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
            AND
            sched_stask.fault_id IS NOT null
      LEFT JOIN evt_event fault_event
         ON -- look for fault found date
           sched_stask.fault_db_id = fault_event.event_db_id AND
           sched_stask.fault_id    = fault_event.event_id
      WHERE
         (
            evt_sched_dead.start_qt > NVL(trkser_tsn.hist_tsn, usg_usage_data.tsn_qt)
            AND
            (
               evt_sched_dead.sched_from_cd != 'CUSTOM'
               OR
               (
                  -- for CUSTOM, only update when it is task of fault
                  evt_sched_dead.sched_from_cd = 'CUSTOM'
                  AND
                  sched_stask.sched_id IS NOT null
               )
                
            )
         )
         OR
         (
            evt_sched_dead.start_qt = NVL(trkser_tsn.hist_tsn, usg_usage_data.tsn_qt)
            AND
            (
               (
                  evt_sched_dead.sched_from_cd = 'EFFECTIV'
                  AND
                  -- compare definition start date to usage date
                  evt_sched_dead.start_dt >= ai_usage_dt
               )
               OR
               (
                  evt_sched_dead.sched_from_cd IN ('WPSTART', 'WPEND', 'LASTEND', 'LASTDUE')
                  AND
                  -- compare previous task complete date to usage date
                  previous_task_event.event_dt >= ai_usage_dt
               )
               OR
               (
                  evt_sched_dead.sched_from_cd = 'CUSTOM'
                  AND
                  sched_stask.sched_id IS NOT null
                  AND
                  -- compare fault found date to usage date
                  fault_event.actual_start_dt >= ai_usage_dt
               )
            )
         )
      ;

      TYPE ltyp_snapshot IS TABLE OF lcur_snapshot%ROWTYPE;
      ltab_snapshots ltyp_snapshot;

      ltab_deadlines STRSTRSTRSTRTABLE := STRSTRSTRSTRTABLE();
      ltpl_deadline STRSTRSTRSTRTUPLE;

BEGIN
   v_step := 10;

   -- Extract the inventory keys from the provided aittb_inv_assmbl
   SELECT element1 BULK COLLECT INTO v_ttb_inv_no_db_ids 
   FROM TABLE (aittb_inv_assmbl)
   ORDER BY element1, element2;

   SELECT element2 BULK COLLECT INTO v_ttb_inv_no_ids 
   FROM TABLE (aittb_inv_assmbl)
   ORDER BY element1, element2;

   v_step := 15;
   
   -- Get the TSN snapshots for only the TRK and SER inventory on the provided date.
   v_ttb_trkser_hist_tsn := USAGE_PKG.getTrkSerUsagesOnDate(v_ttb_inv_no_db_ids, v_ttb_inv_no_ids, ai_usage_dt);

   v_step := 20;

    OPEN lcur_snapshot;
    LOOP

       FETCH lcur_snapshot BULK COLLECT INTO ltab_snapshots LIMIT 10000;

       EXIT WHEN ltab_snapshots.COUNT = 0;

       --update usage
       FORALL i IN 1..ltab_snapshots.COUNT
         UPDATE
            evt_sched_dead
         SET
            evt_sched_dead.start_qt      = evt_sched_dead.start_qt + ltab_snapshots(i).adjusted_delta,
            evt_sched_dead.sched_dead_qt = evt_sched_dead.sched_dead_qt + ltab_snapshots(i).adjusted_delta
         WHERE
            ltab_snapshots(i).event_db_id  = evt_sched_dead.event_db_id AND
            ltab_snapshots(i).event_id     = evt_sched_dead.event_id
            AND
            ltab_snapshots(i).data_type_db_id = evt_sched_dead.data_type_db_id AND
            ltab_snapshots(i).data_type_id    = evt_sched_dead.data_type_id;

       --insert system note
       IF aiv_system_note IS NOT NULL THEN
         FORALL i IN 1..ltab_snapshots.COUNT
             INSERT
             INTO
                evt_stage (
                  event_db_id,
                  event_id,
                  stage_id,
                  event_status_db_id,
                  event_status_cd,
                  stage_dt,
                  stage_gdt,
                  hr_db_id,
                  hr_id,
                  user_stage_note,
                  system_bool
                )
             SELECT
               ltab_snapshots(i).event_db_id,
               ltab_snapshots(i).event_id,
               evt_stage_id_seq.NEXTVAL AS stage_id,
               ltab_snapshots(i).event_status_db_id,
               ltab_snapshots(i).event_status_cd,
               SYSDATE,
               SYSDATE,
               ltab_snapshots(i).hr_db_id,
               ltab_snapshots(i).hr_id,
               aiv_deadline_system_note||aiv_system_note,
               1
             FROM
               dual
             WHERE
               --insert one record per eventid for affected deadlines
               ltab_snapshots(i).rn = 1;
       END IF;

       -- pass back the updated deadlines keys
       FOR i IN 1..ltab_snapshots.COUNT
       LOOP
          ltpl_deadline := STRSTRSTRSTRTUPLE(
             CAST(ltab_snapshots(i).event_db_id AS VARCHAR2),
             CAST(ltab_snapshots(i).event_id AS VARCHAR2),
             ltab_snapshots(i).data_type_db_id,
             ltab_snapshots(i).data_type_id
          );
          ltab_deadlines.EXTEND;
          ltab_deadlines(i) := ltpl_deadline;
       END LOOP;
       aottb_deadlines := ltab_deadlines;

   END LOOP;
   CLOSE lcur_snapshot;



EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingTaskDeadlines;

PROCEDURE updateFollowingEventTsn(
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt      IN DATE,
   aiv_system_note  IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
)
AS

      v_method_name VARCHAR2(30) := 'updateFollowingEventTsn';

      CURSOR lcur_snapshot IS
      SELECT
         evt_inv.event_db_id,
         evt_inv.event_id,
         evt_inv.event_inv_id,
         evt_event.event_status_db_id,
         evt_event.event_status_cd,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta,
         NVL(ain_hr_db_id, NULL) AS hr_db_id,
         NVL(ain_hr_id, NULL) AS hr_id,
         CASE
            --for fault found
            WHEN (evt_event.event_type_db_id = 0 AND
                  evt_event.event_type_cd = 'CF') THEN
                     aiv_fault_system_note
            --for fault/task completed
            WHEN (evt_event.event_type_db_id = 0 AND
                  evt_event.event_type_cd = 'TS'
                  AND
                  (sched_stask.task_class_db_id != 0 OR
                   sched_stask.task_class_cd NOT IN ('CHECK', 'RO'))) THEN
                     aiv_task_wrkpkg_system_note
            --for work packages
            WHEN (evt_event.event_type_db_id = 0 AND
                  evt_event.event_type_cd = 'TS'
                  AND
                 (sched_stask.task_class_db_id = 0 AND
                  sched_stask.task_class_cd IN ('CHECK', 'RO'))) THEN
                     aiv_task_wrkpkg_system_note
         END AS system_note,
         ROW_NUMBER() OVER (PARTITION BY evt_inv.event_id, evt_inv.event_db_id  ORDER BY evt_inv.event_id) AS rn
      FROM
         (SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM
             TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
      INNER JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as assmbl_inv_db_id,
               element4 as assmbl_inv_id
             FROM
                TABLE (aittb_inv_assmbl)
         ) invlist
         ON
            invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
            invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      INNER JOIN evt_inv
         ON
            evt_inv.inv_no_db_id = invlist.inv_no_db_id AND
            evt_inv.inv_no_id    = invlist.inv_no_id
      INNER JOIN evt_event
         ON
            evt_inv.event_db_id = evt_event.event_db_id AND
            evt_inv.event_id    = evt_event.event_id
      INNER JOIN evt_inv_usage
         ON
            evt_inv.event_db_id  = evt_inv_usage.event_db_id AND
            evt_inv.event_id     = evt_inv_usage.event_id    AND
            evt_inv.event_inv_id = evt_inv_usage.event_inv_id
            AND
            adjusted_usage_delta.data_type_db_id = evt_inv_usage.data_type_db_id AND
            adjusted_usage_delta.data_type_id    = evt_inv_usage.data_type_id
      LEFT JOIN sched_stask
         ON
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
      WHERE
           ( -- looking for fault after found date (evt_event.actual_start_dt)
             ( evt_event.event_type_db_id = 0 AND
               evt_event.event_type_cd = 'CF'
               AND
               evt_event.actual_start_dt >= ai_usage_dt
             )
             OR
             -- looking for work package after start date (evt_event.actual_start_dt)
             ( evt_event.event_type_db_id = 0 AND
               evt_event.event_type_cd = 'TS'
               AND
               sched_stask.task_class_db_id = 0 AND
               sched_stask.task_class_cd IN ('CHECK', 'RO')
               AND
               evt_event.actual_start_dt >= ai_usage_dt
             )
             OR
             -- looking for task, not include work package after complete date (evt_event.event_dt)
             ( evt_event.event_type_db_id = 0 AND
               evt_event.event_type_cd = 'TS'
               AND
               (sched_stask.task_class_db_id != 0 OR
                sched_stask.task_class_cd NOT IN ('CHECK', 'RO')
               )
               AND
               evt_event.event_dt >= ai_usage_dt
             )
             OR
             -- looking for other events after event date (evt_event.event_dt)
             ( (evt_event.event_type_db_id != 0 OR
                evt_event.event_type_cd NOT IN ('TS', 'CF')
               )
               AND
               evt_event.event_dt >= ai_usage_dt
             )
           );

      TYPE ltyp_snapshot IS TABLE OF lcur_snapshot%ROWTYPE;
      ltab_snapshots ltyp_snapshot;

BEGIN
    v_step := 10;

    OPEN lcur_snapshot;
    LOOP

       FETCH lcur_snapshot BULK COLLECT INTO ltab_snapshots LIMIT 10000;

       EXIT WHEN ltab_snapshots.COUNT = 0;

       --update usage
       FORALL i IN 1..ltab_snapshots.COUNT
         UPDATE
            evt_inv_usage
         SET
            evt_inv_usage.tsn_qt = evt_inv_usage.tsn_qt + ltab_snapshots(i).adjusted_delta
         WHERE
           evt_inv_usage.event_db_id = ltab_snapshots(i).event_db_id AND
           evt_inv_usage.event_id    = ltab_snapshots(i).event_id   AND
           evt_inv_usage.event_inv_id = ltab_snapshots(i).event_inv_id
           AND
           evt_inv_usage.data_type_db_id = ltab_snapshots(i).data_type_db_id  AND
           evt_inv_usage.data_type_id    = ltab_snapshots(i).data_type_id;

      --insert system note
       IF aiv_system_note IS NOT NULL THEN
         FORALL i IN 1..ltab_snapshots.COUNT
             INSERT
             INTO
                evt_stage (
                  event_db_id,
                  event_id,
                  stage_id,
                  event_status_db_id,
                  event_status_cd,
                  stage_dt,
                  stage_gdt,
                  hr_db_id,
                  hr_id,
                  user_stage_note,
                  system_bool
                )
             SELECT
               ltab_snapshots(i).event_db_id,
               ltab_snapshots(i).event_id,
               evt_stage_id_seq.NEXTVAL AS stage_id,
               ltab_snapshots(i).event_status_db_id,
               ltab_snapshots(i).event_status_cd,
               SYSDATE,
               SYSDATE,
               ltab_snapshots(i).hr_db_id,
               ltab_snapshots(i).hr_id,
               ltab_snapshots(i).system_note||aiv_system_note,
               1
             FROM
               dual
             WHERE
               --insert one record per eventid
               ltab_snapshots(i).rn = 1 ;
       END IF;

   END LOOP;
   CLOSE lcur_snapshot;


EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingEventTsn;


PROCEDURE updateFollowingEditInvOfTrkSer(
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingEditInvOfTrkSer';

BEGIN
   v_step := 10;

   MERGE INTO
      usg_usage_data
   USING
   (
      SELECT
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      FROM
         (SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM
             TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
      INNER JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as assmbl_inv_db_id,
               element4 as assmbl_inv_id
             FROM
                TABLE (aittb_inv_assmbl)
         ) invlist
         ON
            invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
            invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      INNER JOIN inv_inv
         ON
            inv_inv.inv_no_db_id = invlist.inv_no_db_id AND
            inv_inv.inv_no_id    = invlist.inv_no_id
            AND
            inv_inv.inv_class_db_id = 0 AND
            inv_inv.inv_class_cd IN ('TRK', 'SER')
   ) rvw_new_delta
   ON
   (
      rvw_new_delta.inv_no_db_id  = usg_usage_data.inv_no_db_id AND
      rvw_new_delta.inv_no_id     = usg_usage_data.inv_no_id
      AND
      rvw_new_delta.data_type_db_id = usg_usage_data.data_type_db_id AND
      rvw_new_delta.data_type_id    = usg_usage_data.data_type_id
   )
   WHEN MATCHED THEN
      UPDATE
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + rvw_new_delta.adjusted_delta
      WHERE
         EXISTS
         (
            SELECT 1
            FROM usg_usage_record
            WHERE
               usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
               AND
               usg_usage_record.usage_dt >= ai_usage_dt
         )
   ;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingEditInvOfTrkSer;


PROCEDURE updateFollowingUsgAdjustment(
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingUsgAdjustment';

BEGIN
   v_step := 10;

   MERGE INTO
      usg_usage_data
   USING
   (
      SELECT
         adjusted_usage_delta.inv_no_db_id,
         adjusted_usage_delta.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      FROM
         (SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM
             TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
   ) rvw_new_delta
   ON
   (
      rvw_new_delta.inv_no_db_id  = usg_usage_data.inv_no_db_id AND
      rvw_new_delta.inv_no_id     = usg_usage_data.inv_no_id
      AND
      rvw_new_delta.data_type_db_id = usg_usage_data.data_type_db_id AND
      rvw_new_delta.data_type_id    = usg_usage_data.data_type_id
   )
   WHEN MATCHED THEN
      UPDATE
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + rvw_new_delta.adjusted_delta
      WHERE
         EXISTS
         (
            SELECT 1
            FROM usg_usage_record
            WHERE
               usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
               AND
               usg_usage_record.usage_dt >= ai_usage_dt
         )
   ;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingUsgAdjustment;


PROCEDURE updateFollowingSchedToPlan (
   aittb_deadlines  IN STRSTRSTRSTRTABLE
)
AS

   CURSOR lcur_orderedDeadlines IS
      SELECT
         deadlines.event_db_id,
         deadlines.event_id,
         deadlines.data_type_db_id,
         deadlines.data_type_id
      FROM
         (
            SELECT
               element1 as event_db_id,
               element2 as event_id,
               element3 as data_type_db_id,
               element4 as data_type_id
             FROM
                TABLE (aittb_deadlines)
         ) deadlines
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_db_id     = deadlines.event_db_id AND
            evt_sched_dead.event_id        = deadlines.event_id AND
            evt_sched_dead.data_type_db_id = deadlines.data_type_db_id AND
            evt_sched_dead.data_type_id    = deadlines.data_type_id
      INNER JOIN evt_event
         ON
            evt_event.event_db_id = deadlines.event_db_id AND
            evt_event.event_id    = deadlines.event_id
      ORDER BY
         -- order by usage parameter (since we are dealing with deadlines which are per usage parm)
         -- then by completion date and if no completion date then by scheduled date
         deadlines.data_type_db_id, deadlines.data_type_id,
         NVL(evt_event.event_dt, evt_sched_dead.sched_dead_dt)
      ;

   v_method_name        VARCHAR2(30) := 'updateFollowingSchedToPlan';
   v_invalidDeadline    NUMBER;
   v_eventDbId          evt_sched_dead.event_db_id%TYPE;
   v_eventId            evt_sched_dead.event_id%TYPE;
   v_dataTypeDbId       evt_sched_dead.data_type_db_id%TYPE;
   v_dataTypeId         evt_sched_dead.data_type_id%TYPE;
   v_taskDbId           sched_stask.task_db_id%TYPE;
   v_taskId             sched_stask.task_id%TYPE;
   v_return             PREP_DEADLINE_PKG.typn_RetCode;

BEGIN

   FOR lrec_orderedDeadline IN lcur_orderedDeadlines
   LOOP

      -- Pessimistically assume the deadline is invalid.
      v_invalidDeadline := 1;

      BEGIN
         -- Check if the deadline is invalid.
         -- (see WHERE clause for definition of invalid)
         SELECT
            evt_sched_dead.event_db_id,
            evt_sched_dead.event_id,
            evt_sched_dead.data_type_db_id,
            evt_sched_dead.data_type_id,
            sched_stask.task_db_id,
            sched_stask.task_id
         INTO
            v_eventDbId,
            v_eventId,
            v_dataTypeDbId,
            v_dataTypeId,
            v_taskDbId,
            v_taskId
         FROM
            sched_stask
            -- Get task's deadline.
            INNER JOIN evt_sched_dead
               ON
                  evt_sched_dead.event_db_id     = lrec_orderedDeadline.event_db_id AND
                  evt_sched_dead.event_id        = lrec_orderedDeadline.event_id AND
                  evt_sched_dead.data_type_db_id = lrec_orderedDeadline.data_type_db_id AND
                  evt_sched_dead.data_type_id    = lrec_orderedDeadline.data_type_id
            -- Get task's previous task.
            INNER JOIN evt_event_rel
               ON
                  evt_event_rel.rel_event_db_id = evt_sched_dead.event_db_id AND
                  evt_event_rel.rel_event_id    = evt_sched_dead.event_id
                  AND
                  evt_event_rel.rel_type_db_id  = 0 AND
                  evt_event_rel.rel_type_cd     = 'DEPT'
            -- Get previous task's deadline.
            INNER JOIN evt_sched_dead prev_task_sched_dead
               ON
                  prev_task_sched_dead.event_db_id = evt_event_rel.event_db_id AND
                  prev_task_sched_dead.event_id    = evt_event_rel.event_id
                  AND
                  prev_task_sched_dead.data_type_db_id = lrec_orderedDeadline.data_type_db_id AND
                  prev_task_sched_dead.data_type_id    = lrec_orderedDeadline.data_type_id
            -- Get previous task's completion usage.
            INNER JOIN evt_inv prev_task_evt_inv
               ON
                  prev_task_evt_inv.event_db_id   = evt_event_rel.event_db_id AND
                  prev_task_evt_inv.event_id      = evt_event_rel.event_id AND
                  prev_task_evt_inv.main_inv_bool = 1
            INNER JOIN evt_inv_usage prev_task_usage
               ON
                  prev_task_usage.event_db_id  = prev_task_evt_inv.event_db_id AND
                  prev_task_usage.event_id     = prev_task_evt_inv.event_id AND
                  prev_task_usage.event_inv_id = prev_task_evt_inv.event_inv_id
                  AND
                  prev_task_usage.data_type_db_id = lrec_orderedDeadline.data_type_db_id AND
                  prev_task_usage.data_type_id    = lrec_orderedDeadline.data_type_id
         WHERE
            sched_stask.sched_db_id = lrec_orderedDeadline.event_db_id AND
            sched_stask.sched_id    = lrec_orderedDeadline.event_id
            AND
            (
               (
                  -- LASTEND and previous task completed inside its sched-to-plan window
                  -- (this is an incorrect state, as it should be LASTDUE)
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd    = 'LASTEND'
                  AND
                  -- If on or after window begin
                  prev_task_usage.tsn_qt >= (prev_task_sched_dead.sched_dead_qt - prev_task_sched_dead.prefixed_qt)
                  AND
                  -- and on or before window end
                  prev_task_usage.tsn_qt <= (prev_task_sched_dead.sched_dead_qt + prev_task_sched_dead.postfixed_qt)
               )
               OR
               (
                  -- LASTDUE and previous task completed outside its sched-to-plan window
                  -- (this is an incorrect state, as it should be LASTEND)
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd    = 'LASTDUE'
                  AND
                  (
                     -- If before window begin
                     prev_task_usage.tsn_qt < (prev_task_sched_dead.sched_dead_qt - prev_task_sched_dead.prefixed_qt)
                     OR
                     -- and after window end
                     prev_task_usage.tsn_qt > (prev_task_sched_dead.sched_dead_qt + prev_task_sched_dead.postfixed_qt)
                  )
               )
               OR
               (
                  -- LASTDUE and start value does not equal previous task due value
                  -- (this is a correct state but incorrect start value, 
                  --  as the start value must always equal the previous due value when LASTDUE)
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd    = 'LASTDUE'
                  AND
                  evt_sched_dead.start_qt != prev_task_sched_dead.sched_dead_qt
               )
            )
         ;
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            -- If select returns no rows then the deadline is valid.
            v_invalidDeadline := 0;
      END;

      IF v_invalidDeadline = 1 THEN
         PREP_DEADLINE_PKG.PrepareUsageDeadline(
            v_eventDbId,
            v_eventId,
            v_taskDbId,
            v_taskId,
            v_dataTypeDbId,
            v_dataTypeId,
            false,
            NULL,
            v_return
         );
         IF v_return < 0 THEN
            raise_application_error(-20001, 'PREP_DEADLINE_PKG.PrepareUsageDeadline() failed with return='||v_return);
         END IF;
      END IF;

   END LOOP; -- loop over lcur_orderedDeadlines

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', ERROR: ' || SQLERRM,
                         1,
                         2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingSchedToPlan;


PROCEDURE updateCurrentTsn (
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE
)
AS

   v_method_name VARCHAR2(30) := 'updateCurrentTsn';

BEGIN
   v_step := 10;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT
            invlist.inv_no_db_id,
            invlist.inv_no_id,
            adjusted_usage_delta.data_type_db_id,
            adjusted_usage_delta.data_type_id,
            adjusted_usage_delta.adjusted_delta
         FROM
            (SELECT
               element1 as assmbl_inv_db_id,
               element2 as assmbl_inv_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as adjusted_delta
             FROM
                TABLE (aittb_adjusted_usage_delta)
            ) adjusted_usage_delta
         INNER JOIN
            (
               SELECT
                  element1 as inv_no_db_id,
                  element2 as inv_no_id,
                  element3 as assmbl_inv_db_id,
                  element4 as assmbl_inv_id
                FROM
                   TABLE (aittb_inv_assmbl)
            ) invlist
            ON
               invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
               invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tsn_qt = inv_curr_usage.tsn_qt + usage_to_update.adjusted_delta
   ;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateCurrentTsn;


PROCEDURE updateCurrentTso (
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTso';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

BEGIN
   v_step := 10;

   v_ttb_inv_assmbl := filterInventoryHavingOverhaul(aittb_inv_assmbl, ai_usage_dt);

   v_step := 20;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT
            invlist.inv_no_db_id,
            invlist.inv_no_id,
            adjusted_usage_delta.data_type_db_id,
            adjusted_usage_delta.data_type_id,
            adjusted_usage_delta.adjusted_delta
         FROM
            (SELECT
               element1 as assmbl_inv_db_id,
               element2 as assmbl_inv_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as adjusted_delta
             FROM
                TABLE (aittb_adjusted_usage_delta)
            ) adjusted_usage_delta
         INNER JOIN
            (
               SELECT
                  element1 as inv_no_db_id,
                  element2 as inv_no_id,
                  element3 as assmbl_inv_db_id,
                  element4 as assmbl_inv_id
                FROM
                   TABLE (v_ttb_inv_assmbl)
            ) invlist
            ON
               invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
               invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tso_qt = inv_curr_usage.tso_qt + usage_to_update.adjusted_delta
   ;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateCurrentTso;


PROCEDURE updateCurrentTsi (
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTsi';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

BEGIN
   v_step := 10;

   v_ttb_inv_assmbl := filterInventoryReinstalled(aittb_inv_assmbl, ai_usage_dt);

   v_step := 20;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT
            invlist.inv_no_db_id,
            invlist.inv_no_id,
            adjusted_usage_delta.data_type_db_id,
            adjusted_usage_delta.data_type_id,
            adjusted_usage_delta.adjusted_delta
         FROM
            (SELECT
               element1 as assmbl_inv_db_id,
               element2 as assmbl_inv_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as adjusted_delta
             FROM
                TABLE (aittb_adjusted_usage_delta)
            ) adjusted_usage_delta
         INNER JOIN
            (
               SELECT
                  element1 as inv_no_db_id,
                  element2 as inv_no_id,
                  element3 as assmbl_inv_db_id,
                  element4 as assmbl_inv_id
                FROM
                   TABLE (v_ttb_inv_assmbl)
            ) invlist
            ON
               invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
               invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tsi_qt = inv_curr_usage.tsi_qt + usage_to_update.adjusted_delta
   ;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateCurrentTsi;


FUNCTION isnotLatestUsageAdjustment(
   ai_acft_inv_db_id IN INTEGER,
   ai_acft_inv_id    IN INTEGER,
   ai_usage_dt       IN DATE
) RETURN INTEGER
IS
   v_method_name VARCHAR2(30) := 'isnotLatestUsageAdjustment';

   vi_is_not_latest_usage INTEGER;

BEGIN

   v_step := 10;

   SELECT
      CASE
         WHEN EXISTS
            (
               SELECT
                  1
               FROM
                  usg_usage_record
               INNER JOIN inv_inv
                  ON
                     inv_inv.inv_no_db_id = usg_usage_record.inv_no_db_id AND
                     inv_inv.inv_no_id    = usg_usage_record.inv_no_id
               WHERE
                  inv_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
                  inv_inv.h_inv_no_id    = ai_acft_inv_id
                  AND
                  usg_usage_record.usage_dt > ai_usage_dt
                  AND
                  rownum = 1
             )
         THEN 1
         ELSE 0
      END
   INTO
      vi_is_not_latest_usage
   FROM dual;

   v_step := 20;

   IF vi_is_not_latest_usage > 0 THEN
      RETURN vi_is_not_latest_usage;
   END IF;

   v_step := 30;

   SELECT
      CASE
         WHEN EXISTS
            (
               SELECT
                  1
               FROM
                  evt_event
               INNER JOIN evt_inv
                  ON
                     evt_inv.event_db_id = evt_event.event_db_id AND
                     evt_inv.event_id    = evt_event.event_id
               LEFT JOIN sched_stask
                  ON
                     sched_stask.sched_db_id = evt_event.event_db_id AND
                     sched_stask.sched_id    = evt_event.event_id
               WHERE
                  evt_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
                  evt_inv.h_inv_no_id    = ai_acft_inv_id
                  AND
                  (
                      -- looking for fault after found date (evt_event.actual_start_dt)
                      ( evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'CF'
                        AND
                        evt_event.actual_start_dt >= ai_usage_dt
                      )
                      OR
                      -- looking for other events after event date (evt_event.event_dt)
                      (
                        (
                          evt_event.event_type_db_id != 0 OR
                          evt_event.event_type_cd NOT IN ('TS', 'CF')
                        )
                        AND
                        evt_event.event_dt >= ai_usage_dt
                      )
                      OR
                      -- looking for work package after start date (evt_event.actual_start_dt)
                      (
                        evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'TS'
                        AND
                        sched_stask.task_class_db_id = 0 AND
                        sched_stask.task_class_cd IN ('CHECK', 'RO')
                        AND
                        evt_event.event_status_db_id = 0 AND
                        evt_event.event_status_cd IN ('COMPLETE', 'IN WORK')
                        AND
                        evt_event.actual_start_dt >= ai_usage_dt
                      )
                      OR
                      -- looking for task, not include work package after complete date (evt_event.event_dt)
                      (
                        evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'TS'
                        AND
                        (
                         sched_stask.task_class_db_id != 0 OR
                         sched_stask.task_class_cd NOT IN ('CHECK', 'RO')
                        )
                        AND
                        evt_event.event_status_db_id = 0 AND
                        evt_event.event_status_cd = 'COMPLETE'
                        AND
                        evt_event.event_dt >= ai_usage_dt
                      )
                  )
                  AND
                  rownum = 1
             )
         THEN 1
         ELSE 0
      END
   INTO
      vi_is_not_latest_usage
   FROM dual;

   RETURN vi_is_not_latest_usage;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END isnotLatestUsageAdjustment;


PROCEDURE processLatestUsage (
   ai_acft_inv_db_id IN INTEGER,
   ai_acft_inv_id    IN INTEGER,
   airaw_usage_adjustment_id   IN RAW,
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE
)
AS

   v_method_name VARCHAR2(30) := 'processLatestUsage';

BEGIN
   v_step := 10;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id,
            adjusted_usage_delta.data_type_db_id,
            adjusted_usage_delta.data_type_id,
            adjusted_usage_delta.adjusted_delta
         FROM
            (SELECT
               element1 as assmbl_inv_db_id,
               element2 as assmbl_inv_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as adjusted_delta
             FROM
                TABLE (aittb_adjusted_usage_delta)
            ) adjusted_usage_delta
         INNER JOIN inv_inv
            ON
               inv_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
               inv_inv.h_inv_no_id    = ai_acft_inv_id
               AND
               DECODE(inv_inv.inv_class_cd, 'ASSY', inv_inv.inv_no_db_id, NVL(inv_inv.assmbl_inv_no_db_id, inv_inv.h_inv_no_db_id)) = adjusted_usage_delta.assmbl_inv_db_id AND
               DECODE(inv_inv.inv_class_cd, 'ASSY', inv_inv.inv_no_id, NVL(inv_inv.assmbl_inv_no_id, inv_inv.h_inv_no_id))          = adjusted_usage_delta.assmbl_inv_id

      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tsn_qt = inv_curr_usage.tsn_qt + usage_to_update.adjusted_delta,
        inv_curr_usage.tso_qt = inv_curr_usage.tso_qt + usage_to_update.adjusted_delta,
        inv_curr_usage.tsi_qt = inv_curr_usage.tsi_qt + usage_to_update.adjusted_delta
   ;

   MERGE INTO
      usg_usage_data
   USING
   (
      SELECT
         adjusted_usage_delta.inv_no_db_id,
         adjusted_usage_delta.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      FROM
         (SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM
             TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
   ) rvw_new_delta
   ON
   (
      usg_usage_data.inv_no_db_id = rvw_new_delta.inv_no_db_id AND
      usg_usage_data.inv_no_id    = rvw_new_delta.inv_no_id
      AND
      usg_usage_data.data_type_db_id = rvw_new_delta.data_type_db_id AND
      usg_usage_data.data_type_id    = rvw_new_delta.data_type_id
      AND
      usg_usage_data.usage_record_id = airaw_usage_adjustment_id
   )
   WHEN MATCHED THEN
      UPDATE
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + rvw_new_delta.adjusted_delta
   ;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END processLatestUsage;


PROCEDURE process_usage_data (
   airaw_usage_adjustment_id   IN RAW,
   aittb_assmbl_inv_db_id      IN VARCHAR2TABLE,
   aittb_assmbl_inv_id         IN VARCHAR2TABLE,
   aittb_data_type_db_id       IN VARCHAR2TABLE,
   aittb_data_type_id          IN VARCHAR2TABLE,
   aittb_new_delta             IN FLOATTABLE,
   aiv_system_note             IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   aiv_deadline_system_note    IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
)
AS
   v_method_name VARCHAR2(30) := 'process_usage_data';

   v_ttb_assy_usage_deltadata STRSTRSTRSTRFLOATTABLE;
   v_ttb_adjusted_usage_delta STRSTRSTRSTRFLOATTABLE;
   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;
   v_ttb_deadlines STRSTRSTRSTRTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;
   vi_flight_leg RAW(16);
   vi_system_note VARCHAR2(4000);

   vi_is_not_latest_usage INTEGER;

BEGIN
   v_step := 10;

   -- combine sst of input arraies into a table
   SELECT
      StrStrStrStrFloatTuple(
         element1, element2, element3, element4, element5
      )
   BULK COLLECT INTO v_ttb_assy_usage_deltadata
   FROM
      TABLE (
         ARRAY_PKG.getStrStrStrStrFloatTable (
            aittb_assmbl_inv_db_id,
            aittb_assmbl_inv_id,
            aittb_data_type_db_id,
            aittb_data_type_id,
            aittb_new_delta
         )
      );

   v_step := 15;

   v_ttb_adjusted_usage_delta := get_adjusted_usage_delta(airaw_usage_adjustment_id, v_ttb_assy_usage_deltadata);

   v_step := 16;

   -- no delta changed, exit
   IF v_ttb_adjusted_usage_delta.COUNT = 0 THEN
      RETURN;
   END IF;

   v_step := 20;

   SELECT
      inv_no_db_id, inv_no_id, usage_dt
   INTO
      vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt
   FROM
      usg_usage_record
   WHERE
      usage_record_id = airaw_usage_adjustment_id;

   v_step := 23;
   vi_is_not_latest_usage := isnotLatestUsageAdjustment(vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt);
   -- latest flight, only update current usage
   IF vi_is_not_latest_usage = 0 THEN
      processLatestUsage(vi_acft_inv_db_id, vi_acft_inv_id, airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta);
      RETURN;
   END IF;

   v_step := 25;

   IF(aiv_system_note IS NOT NULL) THEN
      SELECT
         leg_id
      INTO
         vi_flight_leg
      FROM
         fl_leg
      WHERE
         usage_record_id = airaw_usage_adjustment_id;
      --add flight info to the system note
      vi_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_system_note;
   END IF ;

   v_step := 30;

   v_ttb_inv_assmbl := get_acft_hist_config(airaw_usage_adjustment_id);

   v_step := 40;

   -- Important!
   -- We have to update deadline first, the calculation of historic Tsn for subcomponent relays
   -- on the unmodified current tsn value and unmodified flight Tsn value.
   updateFollowingTaskDeadlines(airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt, vi_system_note, aiv_deadline_system_note, ain_hr_db_id, ain_hr_id, v_ttb_deadlines);

   v_step := 50;
   updateCurrentTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl);

   v_step := 60;
   updateCurrentTso(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 70;
   updateCurrentTsi(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 80;
   updateFollowingEventTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt, vi_system_note, aiv_task_wrkpkg_system_note, aiv_fault_system_note, ain_hr_db_id, ain_hr_id);

   v_step := 90;
   updateFollowingEditInvOfTrkSer(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 100;
   updateFollowingUsgAdjustment(v_ttb_adjusted_usage_delta, vi_usage_dt);

   v_step := 110;
   -- Important!
   -- Prior to re-adjusting task deadlines that have a schedule-to-plan window, 
   -- both the task deadlines themselves and the event TSNs all need to be updated.
   -- This is because the adjusting of the task deadlines may move that deadline 
   -- either into or out of the window.
   -- Therefore, it is important that updateFollowingSchedToPlan() be called after 
   -- updateFollowingTaskDeadlines() and updateFollowingEventTsn().
   updateFollowingSchedToPlan(v_ttb_deadlines);

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END process_usage_data;

/*----------------------- End of Package -----------------------------------*/
END FLIGHT_OUT_OF_SEQUENCE_PKG;
/