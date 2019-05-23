--liquibase formatted sql


--changeSet event_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE EVENT_PKG
IS

/******************************************************************************
* TYPE DECLARATIONS
******************************************************************************/

-- subtype declarations
SUBTYPE typn_RetCode IS NUMBER;
SUBTYPE typn_Id      IS inv_inv.inv_no_id%TYPE;
SUBTYPE typv_Cd      IS ref_data_source.data_source_cd%TYPE;
SUBTYPE typf_Qt      IS inv_curr_usage.tsn_qt%TYPE;
SUBTYPE typn_Bool    IS evt_event.hist_bool%TYPE;
SUBTYPE typdt_Dt     IS evt_event.event_dt%TYPE;
SUBTYPE typv_Sdesc   IS inv_inv.inv_no_sdesc%TYPE;
SUBTYPE typv_Oem     IS inv_inv.serial_no_oem%TYPE;
SUBTYPE typv_LCd     IS eqp_assmbl_bom.assmbl_bom_cd%TYPE;

-- constant declarations (error codes)
icn_Success                CONSTANT NUMBER       := 1;
icn_NoProc                 CONSTANT typn_RetCode := 0;  -- No processing done
icn_DiffLengthArrays       CONSTANT NUMBER       := -1; -- used by RecordUsage
icn_EvtInvNotFound         CONSTANT NUMBER       := -2;
icn_InvCurrUsageNotFound   CONSTANT NUMBER       := -3;
icn_DidNotReachCap         CONSTANT NUMBER       := -4; 
icn_InvalidDataType        CONSTANT NUMBER       := -30;
icn_InvalidForecastModel   CONSTANT NUMBER       := -31;
icn_SequenceError          CONSTANT NUMBER       := -99;
icn_OracleError            CONSTANT NUMBER       := -100;

-- declare TABLE types (used like arrays)
TYPE typtabn_Id    IS TABLE OF typn_Id   NOT NULL INDEX BY BINARY_INTEGER;
TYPE typtabf_Qt    IS TABLE OF FLOAT     NOT NULL INDEX BY BINARY_INTEGER;
TYPE typtabn_Bool  IS TABLE OF typn_Bool NOT NULL INDEX BY BINARY_INTEGER;
TYPE hash_date_type  IS TABLE OF DATE             INDEX BY VARCHAR2(100);
TYPE hash_float_type IS TABLE OF FLOAT            INDEX BY VARCHAR2(100);

-- declare RECORD and TABLE types for lists of Blackout Periods
TYPE BlackoutPeriod IS RECORD (
   in_StartDate DATE,
   in_EndDate   DATE
   );
TYPE BlackoutPeriods IS TABLE OF BlackoutPeriod;

TYPE DepTasks IS RECORD (
      task_level NUMBER,
      rel_event_db_id NUMBER,
      rel_event_id NUMBER
   );

TYPE lDepTaskTable IS TABLE OF DepTasks;




/********************************************************************************
*
* Procedure:      UpdateDeadline
* Arguments:      al_EventDbId (IN NUMBER): The event to be updated
*                 al_EventId (IN NUMBER):   ""
*                 ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:    This procedure used to update an event's priorities and
*                 deadlines
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Ling Soh
* Recent Date:    Dec 20, 2006
*
*********************************************************************************
*
* Copyright 2006 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadline(
        al_EventDbId    IN typn_Id,
        al_EventId      IN typn_Id,
        ol_Return       OUT NUMBER
   );

/********************************************************************************
*
* PROCEDURE:      PredictUsageBetweenDt
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aStartDate (IN DATE): the relative date to perform our calculations from. Normally call with SYSDATE
*                 ad_TargetDate (IN DATE): The date we want the deadline to be due
*                 an_StartUsageQt (IN NUMBER): the usages at the Start Date
*                 on_TsnQt (OUT NUMBER): The predicted TSN value at the ad_TargetDate
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Determine the forecasted usage quantity at the stpecified date for a deadline with the specified datatype on
*                 the given inventory. The forecasted deadline value(quantity) is calculated using the aircrafts
*                 flight plan and forecast model. It uses binary search to re-use existing function to calculate due date based on usage value
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:   N/A
* Recent Date:    March 19, 2008
*
*********************************************************************************
*
* Copyright 2008 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE PredictUsageBetweenDt(
            aAircraftDbId    IN  inv_inv.inv_no_db_id%TYPE,
            aAircraftId      IN  inv_inv.inv_no_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDate    IN  evt_sched_dead.start_dt%TYPE,
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_StartUsageQt IN  evt_sched_dead.start_qt%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   );

/********************************************************************************
*
* Procedure:      IsInstalledOnAircraft
* Arguments:      aInvNoDbId (IN NUMBER): pk of the inventory to check
*                 aInvNoId (IN NUMBER): ""
*                 aAircraftDbId (OUT NUMBER): if the inventory is installed on aircraft
*                 aAircraftId (OUT NUMBER): this is aicraft's PK.
* Description:    Tests whether the given inventory is installed on an aircraft, and returns the aircraft PK
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:
* Recent Date:    Mar 27, 2008
*
*********************************************************************************
*
* Copyright 2008 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
FUNCTION IsInstalledOnAircraft(
                           aInvNoDbId IN typn_Id,
                           aInvNoId IN typn_Id,
                           aAircraftDbId OUT typn_Id,
                           aAircraftId OUT typn_Id) RETURN NUMBER;


/********************************************************************************
*
* PROCEDURE:      FindTaskDueDate
* Arguments:      al_EventDbId (IN NUMBER): task primary key
*                 al_EventId (IN NUMBER):   ---//---
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Looks up task due date, uses task driving deadline to determine due date.
*                 If task does not have a deadline we follow the tree up to find it. If not
*                 found scheduled start date, or actual start date of a check or ro is used.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   N/A
* Recent Date:    October 1, 2005
*
*********************************************************************************
*
* Copyright 1998-2004 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE FindTaskDueDate(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ad_NeededByDate     OUT date,
      ol_Return           OUT NUMBER
   );
/********************************************************************************
*
* Procedure:      UpdateWormvlDeadline
* Arguments:      an_WorkPkgDbId (IN NUMBER): work order primary key
*                 an_WorkPkgId(IN NUMBER):    ---//---
*                 ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:   Update deadline of a work order replacement task which is shown
*                to have a relationship WORMVL. The deadline will be copied wrom
*                the work order driving deadline task.

*
* Orig.Coder:     Michal Bajer
* Recent Coder:   N/A
* Recent Date:    September 9, 2003
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateWormvlDeadline(
          an_WorkPkgDbId          IN  typn_Id,
          an_WorkPkgId            IN  typn_Id,
          on_Return                  OUT NUMBER);
/********************************************************************************
*
* Procedure:      UpdateRootDrivingDeadline
* Arguments:      an_RootEventDbId (IN NUMBER): Event in a tree that will have
*                                 it's driving deadline udpated
*                          al_RootEventId(IN NUMBER): ""
*                          ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:   This procedure is used to update the driving deadline
*                for a check or work order.  The task within the check that has
*                the nearest upcoming date is considered to the the driving
*                deadline.  The driving deadline relationship will be captured in the
*                EVT_EVENT_REL table.
*
* Orig.Coder:     slr
* Recent Coder:
* Recent Date:    March 27, 2003
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDrivingDeadline(
          an_SchedStaskDbId     IN  typn_Id,
          an_SchedStaskId       IN  typn_Id,
          on_Return             OUT NUMBER
          );

/********************************************************************************
*
* Procedure:      UpdateDeadlineForInvTree
* Arguments:      al_InvNoDbId (IN NUMBER): The top inventory in the tree
*                 al_InvNoId (IN NUMBER): ""
* Description:    Sometimes you need to update all of the deadlines against
*                 inventory in a given tree. If you pass in the top inventory
*                 item, this function will evaluate the deadlines for all
*                 subcomponents.
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Andrew Hircock
* Recent Date:    Aug 12,1999
*
*********************************************************************************
*
* Copyright 1998 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineForInvTree(
      an_InvNoDbId      typn_Id,
      an_InvNoId        typn_Id,
      on_Return         OUT NUMBER
   );


/********************************************************************************
*
* PROCEDURE:      SetPriority
* Arguments:      al_NewEventDbId (IN NUMBER): The event with the priority to rollup
*                 al_NewEventId (IN NUMBER):   ""
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Procedure to take a supplied event and update it's priority
*                 Evaluates to the highest of:
*                     - Priority as determined by the event's deadline
*                     - the maximum priority of it's children events
*                 This procedure will not update a priority that has been set to
*                 a non-system prioirity (i.e. manually set by the user).
*
* Orig.Coder:     Rob Vandenberg
* Recent Coder:   Rob Vandenberg
* Recent Date:    December 10, 2001
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE SetPriority(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ol_Return           OUT NUMBER
   );

/********************************************************************************
*
* PROCEDURE:      findForecastedDeadDt
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aRemainingUsageQt (IN NUMBER): amount of remaining usage before the deadline is due
*                 aStartDate (IN OUT DATE): the relative date to perform our calculations from.
*                    Also used to return the exact deadline date.
*                 aForecastDt (OUT DATE): the date that the deadline will become due.
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Determine the forecasted deadline date for a deadline with the specified datatype on
*                 the given aircraft. The forecasted deadline date is calculated using the aircrafts
*                 flight plan and forecast model.
*
* Orig.Coder:     Neil Ouellette
* Recent Coder:   N/A
* Recent Date:    Dec 19, 2006
*
*********************************************************************************
*
* Copyright 1998-2006 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE findForecastedDeadDt(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%type,
      aAircraftId IN inv_ac_reg.inv_no_id%type,
      aDataTypeDbId IN mim_data_type.data_type_db_id%type,
      aDataTypeId IN mim_data_type.data_type_id%type,
      aRemainingUsageQt IN evt_sched_dead.usage_rem_qt%type,
      aStartDt IN OUT date,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%type,
      ol_Return OUT NUMBER
);

/********************************************************************************
*
* Procedure: GetMainInventory
* Arguments:
*            an_EventDbId      (long) - event priamry key
*            an_EventId        (long) --//--
* Return:
*             an_InvNoDbId (long) -- main inventory primary key
*             an_InvNoId   (long)
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returns main inventory for a given event.
*
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:
* Recent Date:    March 13, 2008
*
*********************************************************************************
*
* Copyright 2008 Mxi Technologies.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE GetMainInventory(
            an_EventDbId    IN  evt_event.event_db_id%TYPE,
            an_EventId      IN  evt_event.event_id%TYPE,
            an_InvNoDbId   OUT inv_inv.inv_no_db_id%TYPE,
            an_InvNoId     OUT inv_inv.inv_no_id%TYPE,
            on_Return      OUT typn_RetCode
   );


/**
 *  Recalculates the deadlines on the given aircraft
 */
PROCEDURE UpdateDeadlinesForAircraft(
          an_AircraftDbId IN inv_ac_reg.inv_no_db_id%type,
          an_AircraftId   IN inv_ac_reg.inv_no_id%type,
          on_Return OUT NUMBER )   ;

/**
 *  Recalculates the CA deadlines on all loose inventory
 */
PROCEDURE UpdateCADeadlinesLooseInv(
          an_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
          an_InvNoId   IN inv_inv.inv_no_id%TYPE,
          on_Return OUT NUMBER );



/**
*  Procedure to use for testing UpdateDeadlinesForTask.
*  It does not require the in/out hash parameters.
*/
PROCEDURE UpdateDeadlinesForTask (
   an_AcftDbId  IN inv_ac_reg.inv_no_db_id%TYPE,
   an_AcftId    IN inv_ac_reg.inv_no_id%TYPE,
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_id%TYPE,
   an_Forecast  IN NUMBER,
   on_Return    OUT NUMBER );


END EVENT_PKG;
/