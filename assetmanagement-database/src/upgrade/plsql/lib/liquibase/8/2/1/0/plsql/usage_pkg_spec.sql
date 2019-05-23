--liquibase formatted sql


--changeSet usage_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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