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
* Procedure:     calculate_calc_parm
*
* Description:  This function executes the equation for the provided
*               calculated parameter and returns its results.
*
* Arguments:    ain_inv_no_db_id: inventory key
*               ain_inv_no_id:     "
*               ain_calc_db_id: calculated parameter key
*               ain_calc_id:     "
*               aitab_usages: table of usages (inv key, data type key, usage value)
*               aon_calc_usage: calculated usage value
*
*******************************************************************************/

PROCEDURE calculate_calc_parm(
   ain_inv_no_db_id   IN inv_inv.inv_no_db_id%TYPE,
   ain_inv_no_id      IN inv_inv.inv_no_id%TYPE,
   ain_calc_db_id     IN mim_calc.calc_db_id%TYPE,
   ain_calc_id        IN mim_calc.calc_id%TYPE,
   aitab_usages       IN STRSTRSTRSTRFLOATTABLE,
   aon_calc_usage     OUT NUMBER
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
) RETURN STRSTRSTRSTRFLOATTABLE;

END USAGE_PKG;
/