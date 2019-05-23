--liquibase formatted sql


--changeSet OPER-4075:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY INV_STATUS
IS

/********************************************************************************
* Internal package type declarations.
********************************************************************************/

/*
* Return value for the GetListOfFaults function.
*/
TYPE FaultsInfo IS RECORD(
   has_AOG_faults      BOOLEAN := FALSE,
   has_unknown_faults  BOOLEAN := FALSE,
   has_open_faults     BOOLEAN := FALSE
);


/*
* Return value for the GetStatusFromFault function.
*/
TYPE StatusFromFaultInfo IS RECORD (

   operating_status mxCodeKey := NULL,
   condition        mxCodeKey := NULL
);


-- Map of ref_inv_oper keys to ref_inv_oper rows.
TYPE OperatingStatusToOrdMap IS TABLE OF ref_inv_oper%ROWTYPE INDEX BY VARCHAR2(20);


/*
* Type for "reason for change" status messages.
*/
iv_StatusChangeString	 VARCHAR2(100); -- strings size for status change keys.
SUBTYPE typv_StatusChangeStr IS iv_StatusChangeString%TYPE;  -- Subtype for status change strings.



/********************************************************************************
* Internal package constants and variables.
********************************************************************************/

-- 0-level aircraft operating statuses
AOG         CONSTANT mxCodeKey := mxCodeKey( 0, 'AOG'  );
AWR         CONSTANT mxCodeKey := mxCodeKey( 0, 'AWR'  );
INM         CONSTANT mxCodeKey := mxCodeKey( 0, 'INM'  );
NORM        CONSTANT mxCodeKey := mxCodeKey( 0, 'NORM' );
OPEN_STATUS CONSTANT mxCodeKey := mxCodeKey( 0, 'OPEN' );


-- 0-level inventory conditions
INREP       CONSTANT mxCodeKey := mxCodeKey( 0, 'INREP'   );
INSPREQ     CONSTANT mxCodeKey := mxCodeKey( 0, 'INSPREQ' );
REPREQ      CONSTANT mxCodeKey := mxCodeKey( 0, 'REPREQ'  );
RFI         CONSTANT mxCodeKey := mxCodeKey( 0, 'RFI'     );
INSRV       CONSTANT mxCodeKey := mxCodeKey( 0, 'INSRV'   );
QUAR        CONSTANT mxCodeKey := mxCodeKey( 0, 'QUAR'    );

-- "Reason for change" constants.
-- These are returned in InventoryStatus objects and translated into
-- localizable text by Java.
STATUS_FAULTS_AOG            CONSTANT typv_StatusChangeStr := 'FAULTS_AOG_ON_INVENTORY';
STATUS_FAULTS_UNKNOWN        CONSTANT typv_StatusChangeStr := 'FAULTS_UNKNOWN_ON_INVENTORY';
STATUS_FAULTS_ACTV_LOOSE     CONSTANT typv_StatusChangeStr := 'FAULTS_ACTV_UNASSIGNED';
STATUS_TASKS_INWORK          CONSTANT typv_StatusChangeStr := 'TASKS_INWORK_ON_INVENTORY';
STATUS_TASKS_OVERDUE         CONSTANT typv_StatusChangeStr := 'TASKS_OVERDUE_ON_INVENTORY';
STATUS_FAULTS_OPEN           CONSTANT typv_StatusChangeStr := 'FAULTS_OPEN_ON_INVENTORY';
STATUS_INVENTORY_CONDITION   CONSTANT typv_StatusChangeStr := 'INVENTORY_CONDITION';
STATUS_INCOMPLETE_INVENTORY  CONSTANT typv_StatusChangeStr := 'MISSING_MANDATORY_COMPONENTS_ON_INVENTORY';
STATUS_INVENTORY_SERVICEABLE CONSTANT typv_StatusChangeStr := 'INVENTORY_CONDITION_SERVICEABLE';
STATUS_PART_NOT_APPROVED     CONSTANT typv_StatusChangeStr := 'PART_NOT_APPROVED';


/*
* Map of ref_inv_oper keys to ref_inv_oper rows - filled during package initialization.
*
* Used by the MaxOpStatusSeverity function to determine which of two
* status objects has the highest severity (ref_inv_oper.oper_ord.)
*/
OP_STATUSES OperatingStatusToOrdMap;




/******************************************************************************
* Foward declarations for package private procedures/functions.
*
* See implementations for full documentation.
******************************************************************************/

/*
* For the given inventory key, returns its highest inventory plus the
* highest inv's current status/condition.
*/
FUNCTION GetHighestInvInitialStatus(
                         al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         al_InvNoId   IN inv_inv.inv_no_id%TYPE
                         )
   RETURN InventoryStatus;

-- Get list of open faults for inventory.
FUNCTION GetListOfFaults   ( arec_HInvKey IN mxKey )   RETURN FaultsInfo;


-- Gets the highest severity for open faults associated with the inventory.
FUNCTION GetStatusFromFault( arec_HInvKey IN mxKey )   RETURN StatusFromFaultInfo;


-- Returns true if the inventory has IN WORK tasks.
FUNCTION HasInWorkTasks    ( arec_HInvKey IN mxKey )   RETURN BOOLEAN;

-- Returns true if REPAIR shipment is completed for the inventory
FUNCTION HasRepairShipmentCompleted     ( arec_HInvKey mxKey )      RETURN BOOLEAN;

-- Returns true if the inventory has overdue (O/D) tasks.
FUNCTION HasOverdueTasks( arec_HInvKey    IN mxKey )   RETURN BOOLEAN;


-- Returns true if the inventory's current condition is
-- AWR (ref_inv_cond.wo_complete_bool.)
FUNCTION IsAwaitingRelease( ao_Condition IN mxCodeKey ) RETURN BOOLEAN;


-- Returns true if the inventory is complete.
FUNCTION IsInventoryComplete( arec_HInvKey IN mxKey   ) RETURN BOOLEAN;


/*
* From the two status parameters, returns the one with the highest severity
* (Severity is determined by ref_inv_oper.oper_ord.)
*/
FUNCTION MaxOpStatusSeverity( ao_OperatingStatus1 IN mxCodeKey, ao_OperatingStatus2 IN mxCodeKey )
   RETURN mxCodeKey;


-- Returns true if the inventory's part is approved.
FUNCTION PartIsApproved( arec_HInvKey IN mxKey ) RETURN BOOLEAN;

-- Returns true if it is kit inventory and it has unserviceable sub-kit inventory
FUNCTION IsKitWithUnsvcbleSubKitInv( arec_HInvKey IN mxKey ) RETURN BOOLEAN;

/********************************************************************************
* Public functions and procedure implementations.
********************************************************************************/


/********************************************************************************
*
* Function:       CalculateStatusRecord
*
* Arguments:      al_InvNoDbId (IN NUMBER): inventory for status calculation
*                 al_InvNoId   (IN NUMBER): ""
*
* Returns:        an InventoryStatus object containing:
*                    - current operating status/condition of al_InvNo's highest
*                      inventory
*                    - new operating/status/condition of al_InvNo's highest
*                      inventory
*                    - if the highest inventory is loose, operating status will
*                      be null
*
* Description:    Calculates *highest inventory's* operating status and condition based on
*                 related information (overdue tasks, open faults, etc.)
*/
FUNCTION CalculateStatusRecord(
                         al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         al_InvNoId   IN inv_inv.inv_no_id%TYPE
                        )
   RETURN InventoryStatus

IS
  -- Return value
  orec_Status         InventoryStatus := NULL;

  -- Current information for the highest inventory.
  lrec_HInvKey         mxKey     := NULL;
  lo_OldCond           mxCodeKey := NULL;
  lb_IsAircraft        BOOLEAN   := FALSE;
  lb_HasInWorkTasks    BOOLEAN   := FALSE;
  lb_IsKit             BOOLEAN   := FALSE;
  lb_HasRepairShipmentCompleted    BOOLEAN   := FALSE;

  -- Highest inventory information determined during CalculateStatus.
  lo_NewOpStatus          mxCodeKey     := NORM;
  lo_NewCond              mxCodeKey     := INSRV;
  ls_ReasonForChange      typv_StatusChangeStr := '';
  ls_ListOfReasons        typv_StatusChangeStr := '';


  -- For storing function results.
  lrec_FaultStatus    StatusFromFaultInfo;
  lrec_FaultsInfo     FaultsInfo;


BEGIN
   -- Get the highest inventory and its status info.
   orec_Status   := GetHighestInvInitialStatus( al_InvNoDbId, al_InvNoId );

   -- Store highest inv information in local variables to keep the code readable.
   lrec_HInvKey  := mxKey    ( orec_Status.h_inv_db_id   , orec_Status.h_inv_id );
   lo_OldCond   := mxCodeKey( orec_Status.old_cond_db_id, orec_Status.old_cond_cd );
   lb_IsAircraft := 1 = orec_Status.aircraft_bool;
   lb_HasInWorkTasks := HasInWorkTasks( lrec_HInvKey );
   lb_HasRepairShipmentCompleted := HasRepairShipmentCompleted( lrec_HInvKey );

   -- Process fault information for the highest inventory.
   lrec_FaultsInfo := GetListOfFaults( lrec_HInvKey );
   IF ( lrec_FaultsInfo.has_AOG_faults
        AND
        (
            lb_IsAircraft=TRUE
            OR
            ( lb_IsAircraft=FALSE AND NOT lb_HasInWorkTasks)
        )
      )
      THEN
      -- AOG faults ground an aircraft and flip status of components that are not in work
      lo_NewOpStatus     := AOG;
      lo_NewCond         := REPREQ;
      ls_ReasonForChange := STATUS_FAULTS_AOG;
      ls_ListOfReasons   :=  STATUS_FAULTS_AOG;

   ELSIF ( lrec_FaultsInfo.has_unknown_faults AND lb_IsAircraft=FALSE AND NOT lb_HasInWorkTasks) THEN
      --For components not INREP, we want to change their status to REPREQ
      lo_NewOpStatus     := AOG;
      lo_NewCond         := REPREQ;
      ls_ReasonForChange := STATUS_FAULTS_UNKNOWN;
      ls_ListOfReasons   :=  STATUS_FAULTS_UNKNOWN;

   ELSIF ( lrec_FaultsInfo.has_open_faults ) THEN
      -- Open, loose faults are flagged as OPEN with INSRV condition
      -- The condition may be overridden by subsequent logic (overdue tasks, etc.)
      lo_NewOpStatus     := OPEN_STATUS;
      lo_NewCond         := INSRV;
      ls_ReasonForChange := STATUS_FAULTS_ACTV_LOOSE;
      ls_ListOfReasons   := STATUS_FAULTS_ACTV_LOOSE;
   END IF;


   -- Faults have been processed.
   -- AOG status will cause other status logic to be skipped.
   IF ( lo_NewOpStatus.cd <> 'AOG' ) THEN


      -- Check for IN_WORK work packages, overdue tasks, and missing parts.
      IF ( lb_HasInWorkTasks ) THEN
         IF ( lo_NewOpStatus.cd = 'NORM' ) THEN
            -- NORM status should be overridden (OPEN should not.)
            lo_NewOpStatus   := INM;
         END IF;

         -- If the inventory is received via REPAIR shipment and condition is INSPREQ then leave it INSPREQ
         IF ( lb_HasRepairShipmentCompleted AND lo_OldCond.cd = INSPREQ.cd ) THEN
            lo_NewCond         := INSPREQ;
            ls_ReasonForChange := STATUS_INVENTORY_CONDITION;
         ELSE
            lo_NewCond          := INREP;
            ls_ReasonForChange  := STATUS_TASKS_INWORK;
            ls_ListOfReasons    := STATUS_TASKS_INWORK;
         END IF;

      ELSIF ( hasoverduetasks( lrec_hinvkey ) ) THEN
         -- Inventory has overdue tasks - ground it.
         lo_NewOpStatus     := AOG;
         lo_NewCond         := REPREQ;
         ls_ReasonForChange := STATUS_TASKS_OVERDUE;

      ELSIF ( NOT IsInventoryComplete( lrec_HInvKey ) ) THEN
         -- Inventory has missing parts - ground it.
         lo_NewOpStatus     := AOG;
         lo_NewCond         := REPREQ;
         ls_ReasonForChange := STATUS_INCOMPLETE_INVENTORY;

      ELSE
         -- No major problems have been found so far.  At this point,
         -- lo_NewOpStatus may be NORM or OPEN.  Subsequent logic has to
         -- ensure lower severity statuses do not override OPEN status.

         -- Determine inventory's status based on the severity of its
         -- open faults. This is known as the "fault escalation model"
         lrec_FaultStatus := GetStatusFromFault( lrec_HInvKey );
         IF  ( lrec_FaultStatus.operating_status IS NOT NULL ) THEN
            -- At least one open fault was found.
            lo_NewOpStatus     := MaxOpStatusSeverity( lrec_FaultStatus.operating_status, lo_NewOpStatus );
            lo_NewCond         := lrec_FaultStatus.condition;
            ls_ReasonForChange := STATUS_FAULTS_OPEN;
            ls_ListOfReasons   := ls_ListOfReasons || ',' || STATUS_FAULTS_OPEN;

         END IF;

         -- Fault escalation can only produce a condition of REPREQ or INSRV.
         -- If the new condition is REPREQ, we can skip the rest.
         IF ( lo_NewCond.cd <> REPREQ.cd ) THEN

            IF ( NOT PartIsApproved( lrec_HInvKey ) ) THEN

                lo_NewOpStatus     := AOG;
                lo_NewCond         := INSPREQ;
                ls_ReasonForChange := STATUS_PART_NOT_APPROVED;
                ls_ListOfReasons   := ls_ListOfReasons || ',' || STATUS_PART_NOT_APPROVED;

            -- Inventory is ready for issue/serviceable by this point.
            ELSIF ( lo_OldCond.cd = RFI.cd OR IsAwaitingRelease( lo_OldCond ) ) THEN

               IF ( lo_NewOpStatus.cd = NORM.cd ) THEN
                  lo_NewOpStatus     := AWR;
               ELSE
                  lo_NewOpStatus     := MaxOpStatusSeverity( AWR, lo_NewOpStatus );
               END IF;

               lo_NewCond         := lo_OldCond;
               ls_ReasonForChange := STATUS_INVENTORY_CONDITION;
            ELSE

               lo_NewOpStatus := MaxOpStatusSeverity( NORM, lo_NewOpStatus );
               
               IF ( lb_IsAircraft ) THEN
                  lo_NewCond := INSRV;
			      -- Inventory is a Kit inventory and it has unserviceable sub kit inventory
               ELSIF ( IsKitWithUnsvcbleSubKitInv( lrec_HInvKey ) ) THEN
			         lo_NewCond := INSPREQ;
               ELSE
                  lo_NewCond := RFI;
               END IF;

               IF ( ls_ReasonForChange IS NULL ) THEN
                  ls_ReasonForChange := STATUS_INVENTORY_SERVICEABLE;
               END IF;
            END IF;
         END IF;
      END IF;
   END IF;
   -- ignore case INM
   IF (lo_NewOpStatus.cd  <> 'INM') THEN
     IF ( HasOverdueTasks( lrec_HInvKey ) ) THEN
            ls_ListOfReasons   := ls_ListOfReasons || ',' || STATUS_TASKS_OVERDUE;
     END IF;
     IF ( NOT IsInventoryComplete( lrec_HInvKey ) ) THEN
            -- Inventory has missing parts - ground it.
            ls_ListOfReasons   := ls_ListOfReasons || ',' || STATUS_INCOMPLETE_INVENTORY;
     END IF;
   END IF;

   -- Fill in new status/condition and reason and return.
   orec_Status.new_op_status_db_id := lo_NewOpStatus.db_id;
   orec_Status.new_op_status_cd    := lo_NewOpStatus.cd;
   orec_Status.new_cond_db_id      := lo_NewCond.db_id;
   orec_Status.new_cond_cd         := lo_NewCond.cd;
   orec_Status.reason_for_change   := ls_ReasonForChange;
   orec_Status.list_of_reasons     := ls_ListOfReasons;
   RETURN orec_Status;
END CalculateStatusRecord;



/********************************************************************************
*
* Function:       CalculateStatus
*
* Arguments:      al_InvNoDbId (IN NUMBER): inventory for status calculation
*                 al_InvNoId   (IN NUMBER): ""
*
* Returns:        an InventoryStatus object containing:
*                    - current operating status/condition of al_InvNo's highest
*                      inventory
*                    - new operating/status/condition of al_InvNo's highest
*                      inventory
*                    - if the highest inventory is loose, operating status will
*                      be null
*
* Description:    Pipelined version of CalculateStatusRecord.
*                 Calculates *highest inventory's* operating status and condition based on
*                 related information (overdue tasks, open faults, etc) and returns it
*                 for use in TABLE() queries.
*/
FUNCTION CalculateStatus(
                         al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         al_InvNoId   IN inv_inv.inv_no_id%TYPE
                        )
   RETURN InventoryStatusTable PIPELINED
IS

BEGIN
   PIPE ROW( CalculateStatusRecord( al_InvNoDbId, al_InvNoId ) );

   RETURN;
END CalculateStatus;


/********************************************************************************
*
* Function:       IsAircraft
*
* Arguments:      al_InvNoDbId (IN NUMBER): inventory for status calculation
*                 al_InvNoId   (IN NUMBER): ""
*
* Returns:        true if the inventory class is 0:ACFT.
*
* Description:    Determines whether the parameters represent the aircraft inventory
*                 class.
*/
FUNCTION IsAircraft(
                    al_InvClassDbId IN ref_inv_class.inv_class_db_id%TYPE,
                    al_InvClassCd   IN ref_inv_class.inv_class_cd   %TYPE
                   )
   RETURN NUMBER
IS

BEGIN

   IF ( al_InvClassDbId = 0 AND al_InvClassCd = 'ACFT' ) THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;

END IsAircraft;



/********************************************************************************
* Package private function/procedure implementations.
********************************************************************************/

/********************************************************************************
*
* Function:       GetListOfFaults
*
* Arguments:      arec_HInvKey IN mxKey: inventory for status calculation
*
* Returns:        A summary of the inventory's faults:
*                    - whether AOG faults exist
*                    - whether UNKNOWN faults exist (only set when no AOG
*                      faults exist)
*                    - whether loose, CFACTV faults exists (only set when no AOG
*                      faults exist)
*
* Description:    Builds a summary of the inventory (and sub-inventorys') faults.
*/
FUNCTION GetListOfFaults( arec_HInvKey mxKey )
   RETURN FaultsInfo
AS

   -- Gets a list of open faults, their severity, and work status.
   CURSOR lcur_FaultInfo (
      cl_InvNoDbId       inv_inv.inv_no_db_id%TYPE,
      cl_InvNoId         inv_inv.inv_no_id   %TYPE
   ) IS

      SELECT
           DECODE( sev_type_cd, 'AOG'    , 1, 0 ) AS is_aog,
           DECODE( sev_type_cd, 'UNKNOWN', 1, 0 ) AS is_unknown,
           DECODE( corr_root.event_status_cd, 'IN WORK', 0, 1 ) AS is_loose,
           DECODE( evt_event.event_status_cd, 'CFACTV',  1, 0 ) AS is_active
      FROM
         inv_inv
         -- Join inventory to its faults via evt tables.
         INNER JOIN sched_stask   ON sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
                                     sched_stask.main_inv_no_id    = inv_inv.inv_no_id
         INNER JOIN evt_event     ON evt_event.event_db_id = sched_stask.fault_db_id AND
                                     evt_event.event_id    = sched_stask.fault_id
         INNER JOIN sd_fault      ON sd_fault.fault_db_id  = evt_event.event_db_id AND
                                     sd_fault.fault_id     = evt_event.event_id
         INNER JOIN ref_fail_sev  ON ref_fail_sev.fail_sev_db_id = sd_fault.fail_sev_db_id AND
                                     ref_fail_sev.fail_sev_cd    = sd_fault.fail_sev_cd
         -- Join to the fault's corrective task
         LEFT  JOIN  evt_event_rel        ON evt_event_rel.event_db_id = sd_fault.fault_db_id AND
                                             evt_event_rel.event_id    = sd_fault.fault_id
         LEFT  JOIN  evt_event corr_event ON corr_event.event_db_id = evt_event_rel.rel_event_db_id AND
                                             corr_event.event_id    = evt_event_rel.rel_event_id
         LEFT  JOIN  evt_event corr_root  ON corr_root .event_db_id = corr_event.h_event_db_id      AND
                                            corr_root .event_id    = corr_event.h_event_id
      WHERE
         -- all sub inventory of our inventory
         inv_inv.h_inv_no_db_id = cl_InvNoDbId AND
         inv_inv.h_inv_no_id    = cl_InvNoId   AND
         inv_inv.rstat_cd	= 0
         AND
         sched_stask.rstat_cd     = 0
         -- where fault is not a possible fault
         AND NOT
         (
            evt_event.event_status_db_id = 0 AND
            evt_event.event_status_cd    = 'CFPOSS'
         )
         AND
         -- where events are non-historic
         evt_event.hist_bool   = 0
         AND
         evt_event.rstat_cd    = 0
         AND
         (
            -- limit to corrective actions
            evt_event_rel.rel_type_cd = 'CORRECT'
            OR
            -- ... or faults with no relationships
            evt_event_rel.event_db_id IS NULL
         )
         AND
         sd_fault.rstat_cd     = 0
   ;


   lrec_Fault lcur_FaultInfo%ROWTYPE;

   orec_FaultsInfo FaultsInfo;

BEGIN
  -- Loop over the faults and fault info flags for return.
  FOR lrec_Fault IN lcur_FaultInfo( arec_HInvKey.db_id, arec_HInvKey.id ) LOOP

     IF    ( lrec_Fault.is_aog     = 1 ) THEN
        -- AOG is highest severity; other results aren't interesting so return immediately.
        orec_FaultsInfo.has_AOG_faults    := TRUE;
        EXIT;
     ELSE
        IF ( lrec_Fault.is_unknown = 1 ) THEN
           -- UNKNOWN fault
           orec_FaultsInfo.has_unknown_faults := TRUE;
        END IF;

        IF ( lrec_Fault.is_loose   = 1 AND lrec_Fault.is_active = 1 ) THEN
           -- Fault is not in work and active.
           orec_FaultsInfo.has_open_faults    := TRUE;
        END IF;
     END IF;
  END LOOP;

  RETURN orec_FaultsInfo;

END GetListOfFaults;



/********************************************************************************
*
* Function:       GetHighestInvInitialStatus
*
* Arguments:      al_InvNoDbId IN NUMBER: inventory key of interest.
*                 al_InvNoId   IN NUMBER: ""
*
*
* Returns:        An InventoryRecord with current status/condition of the
*                 highest inventory.
*
* Description:    Initializes and fetches current information for the al_InvNo's
*                 highest inventory.
*/
FUNCTION GetHighestInvInitialStatus(
                         al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         al_InvNoId   IN inv_inv.inv_no_id%TYPE
                         )
   RETURN InventoryStatus
IS

   orec_Status InventoryStatus := NEW InventoryStatus(
         -- Highest inventory info.
         h_inv_db_id              => 0,
         h_inv_id                 => 0,
         aircraft_bool            => 0,

         -- Current h_inv status/condition.
         old_op_status_db_id      => NULL,
         old_op_status_cd         => NULL,
         old_cond_db_id           => NULL,
         old_cond_cd              => NULL,

         -- Calculated h_inv status/condition.
         new_op_status_db_id      => NULL,
         new_op_status_cd         => NULL,
         new_cond_db_id           => NULL,
         new_cond_cd              => NULL,

         -- Reason for change between old/new status/condition.
         reason_for_change        => '',
         list_of_reasons          => ''
  );

BEGIN
   SELECT
      highest_inv.inv_no_db_id,
      highest_inv.inv_no_id,
      highest_inv.inv_cond_db_id,
      highest_inv.inv_cond_cd,
      inv_ac_reg.inv_oper_db_id,
      inv_ac_reg.inv_oper_cd,
      IsAircraft( highest_inv.inv_class_db_id, highest_inv.inv_class_cd )
      INTO
      orec_Status.h_inv_db_id,
      orec_Status.h_inv_id,
      orec_Status.old_cond_db_id,
      orec_Status.old_cond_cd,
      orec_Status.old_op_status_db_id,
      orec_Status.old_op_status_cd,
      orec_Status.aircraft_bool
   FROM
      inv_inv this_inv
      -- Join input inventory key to its highest inventory.
      INNER JOIN inv_inv highest_inv ON highest_inv.inv_no_db_id = this_inv.h_inv_no_db_id AND
                                        highest_inv.inv_no_id    = this_inv.h_inv_no_id
      -- Get aircraft info (if applicable)
      LEFT JOIN  inv_ac_reg          ON inv_ac_reg.inv_no_db_id  = highest_inv.inv_no_db_id AND
                                        inv_ac_reg.inv_no_id     = highest_inv.inv_no_id
   WHERE
      this_inv.inv_no_db_id = al_InvNoDbId AND
      this_inv.inv_no_id    = al_InvNoId
   ;

   RETURN orec_Status;
END GetHighestInvInitialStatus;


/********************************************************************************
*
* Function:       GetStatusFromFault
*
* Arguments:      arec_HInvKey IN mxKey: highest inventory key for the fault query.
*
* Returns:        The highest severity/condition from the inventory's list of faults.
*
* Description:    Uses the fault escalation model to find the highest severity
*                 operating status/condition for this inventory's tree.
*
*/
FUNCTION GetStatusFromFault( arec_HInvKey mxKey )
   RETURN StatusFromFaultInfo
AS

   lrec_StatusFromFault StatusFromFaultInfo;

BEGIN
   BEGIN

      SELECT
         mxCodeKey( inv_oper_db_id, inv_oper_cd ),
         mxCodeKey( 0, new_inv_cond_cd )
      INTO
         lrec_StatusFromFault.operating_status,
         lrec_StatusFromFault.condition
      FROM
         (
         SELECT
            ref_inv_oper.inv_oper_db_id,
            ref_inv_oper.inv_oper_cd,
            DECODE( ref_inv_oper.inv_oper_cd, 'AOG', 'REPREQ', 'INSRV' ) as new_inv_cond_cd,
            ref_inv_oper.oper_ord
         FROM
            ref_inv_oper,
            ref_fail_sev,
            sd_fault,
            evt_event,
            evt_inv,
            inv_inv
         WHERE
            inv_inv.h_inv_no_db_id = arec_HInvKey.db_id AND
            inv_inv.h_inv_no_id    = arec_HInvKey.id    AND
            inv_inv.rstat_cd	     = 0
            AND
            evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
            evt_inv.inv_no_id     = inv_inv.inv_no_id   AND
            evt_inv.main_inv_bool = 1
            AND
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id
            AND
            NOT (
               evt_event.event_status_db_id = 0 AND
               evt_event.event_status_cd    = 'CFPOSS'
            )
            AND
            evt_event.hist_bool  = 0		AND
            evt_event.rstat_cd	 = 0
            AND
            sd_fault.fault_db_id = evt_event.event_db_id AND
            sd_fault.fault_id    = evt_event.event_id		AND
            sd_fault.rstat_cd	   = 0
            AND
            ref_fail_sev.fail_sev_db_id = sd_fault.fail_sev_db_id AND
            ref_fail_sev.fail_sev_cd    = sd_fault.fail_sev_cd
            AND
            ref_inv_oper.oper_ord <= ref_fail_sev.fail_sev_ord
         ORDER BY oper_ord DESC
      )
      WHERE
         rownum = 1;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- Not really an exception; no tasks are in work.
         lrec_StatusFromFault.operating_status := NULL;
         lrec_StatusFromFault.condition := NULL;
   END;

   RETURN lrec_StatusFromFault;

END GetStatusFromFault;


/********************************************************************************
*
* Function:       HasInWorkTasks
*
* Arguments:      arec_HInvKey IN mxKey: highest inventory key for the query.
*
* Returns:        TRUE if IN_WORK tasks exist in this inventory's tree.
*
*/
FUNCTION HasInWorkTasks( arec_HInvKey mxKey )
   RETURN BOOLEAN
AS

   li_IsInWork INTEGER;

BEGIN
   BEGIN

      SELECT
         1
      INTO
         li_IsInWork
      FROM
         evt_event,
         evt_inv
      WHERE
         evt_inv.h_inv_no_db_id = arec_HInvKey.db_id AND
         evt_inv.h_inv_no_id    = arec_HInvKey.id   AND
         evt_inv.main_inv_bool  = 1
         AND
         evt_event.event_db_id = evt_inv.event_db_id AND
         evt_event.event_id 	 = evt_inv.event_id
         AND
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd  = 'IN WORK'
         AND
         evt_event.hist_bool   = 0
         AND
         ROWNUM = 1;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- Not really an exception; no tasks are in work.
         li_IsInWork := 0;
   END;

   RETURN li_IsInWork = 1;

END HasInWorkTasks;

/********************************************************************************
*
* Function:       HasRepairShipmentCompleted
*
* Arguments:      arec_HInvKey IN mxKey: highest inventory key for the query.
*
* Returns:        TRUE if the inventory has an RO
*
********************************************************************************/
FUNCTION HasRepairShipmentCompleted( arec_HInvKey mxKey )
   RETURN BOOLEAN
AS

   li_IsROReceived INTEGER;

BEGIN
   BEGIN
      -- find repair shipment is completed for the inventory
      SELECT
         1
      INTO
         li_IsROReceived
      FROM
         sched_stask
         INNER JOIN po_line ON
            po_line.sched_db_id = sched_stask.sched_db_id AND
            po_line.sched_id    = sched_stask.sched_id
         INNER JOIN ship_shipment_line ON
            ship_shipment_line.po_db_id = po_line.po_db_id AND
            ship_shipment_line.po_id    = po_line.po_id AND
            ship_shipment_line.po_line_id = po_line.po_line_id
         INNER JOIN ship_shipment ON
            ship_shipment.shipment_db_id = ship_shipment_line.shipment_db_id AND
            ship_shipment.shipment_id    = ship_shipment_line.shipment_id
         INNER JOIN evt_event ON
            evt_event.event_db_id = ship_shipment.shipment_db_id AND
            evt_event.event_id    = ship_shipment.shipment_id
      WHERE
         sched_stask.main_inv_no_db_id = arec_HInvKey.db_id AND
         sched_stask.main_inv_no_id    = arec_HInvKey.id
         AND
         po_line.po_line_type_db_id = 0 AND
         po_line.po_line_type_cd    = 'REPAIR'
         AND
         ship_shipment.shipment_type_db_id = 0 AND
         ship_shipment.shipment_type_cd    = 'REPAIR'
         AND
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd    = 'IXCMPLT'
         AND
         ROWNUM = 1;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- Not really an exception; no RO is found
         li_IsROReceived := 0;
   END;

   RETURN li_IsROReceived = 1;

END HasRepairShipmentCompleted;

/********************************************************************************
*
* Function:       HasOverdueTasks
*
* Arguments:      arec_HInvKey IN mxKey: highest inventory key for the query.
*
* Returns:        True if overdue tasks exist in the inventory's tree.
*
* Description:    Searches the inventory's tree to find overdue tasks.
*                 However, overdue tasks with invalid start dates will be ignored.
*                 Specifically, the following tasks will not be considered overdue;
*                  - tasks based on task definitions with effective dates in the future
*                  - tasks with a driving deadline that is scheduled from an effective date,
*                    whose start quantity is 0, and whose usage remaining is negative
*
*/
FUNCTION HasOverdueTasks( arec_HInvKey mxKey )
   RETURN BOOLEAN
AS

   li_HasOverdueTasks INTEGER;

BEGIN
   BEGIN

      SELECT
         1
      INTO
         li_HasOverdueTasks
      FROM
         evt_inv
         INNER JOIN evt_event ON
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id
         INNER JOIN sched_stask ON
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
         LEFT OUTER JOIN task_task ON
           task_task.task_db_id = sched_stask.task_db_id AND
           task_task.task_id    = sched_stask.task_id
      WHERE
         evt_inv.h_inv_no_db_id = arec_HInvKey.db_id AND
         evt_inv.h_inv_no_id    = arec_HInvKey.id
		 AND
         evt_inv.main_inv_bool  = 1
         AND
         evt_event.sched_priority_db_id = 0 AND
         evt_event.sched_priority_cd    = 'O/D'
         AND
         evt_event.hist_bool=0
         AND
         sched_stask.orphan_frct_bool = 0
         AND
         -- ignore tasks with invalid start dates, as they are not actually overdue
         (
            sched_stask.task_db_id IS NULL
            OR
            task_task.effective_dt IS NULL
            OR
            task_task.effective_dt <= SYSDATE
            OR
            NOT EXISTS (
               SELECT
                  1
               FROM
                  evt_sched_dead
               WHERE
                  evt_sched_dead.event_db_id = evt_event.event_db_id AND
                  evt_sched_dead.event_id    = evt_event.event_id
                  AND
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd    = 'EFFECTIV'
                  AND
                  evt_sched_dead.sched_driver_bool = 1
                  AND
                  evt_sched_dead.start_qt = 0
                  AND
                  evt_sched_dead.usage_rem_qt < 0
            )
         )
		 AND
         ROWNUM = 1;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- Not really an exception; no overdue tasks were found.
         li_HasOverdueTasks := 0;
   END;

   RETURN li_HasOverdueTasks = 1;

END HasOverdueTasks;


/********************************************************************************
*
* Function:       IsInventoryComplete
*
* Arguments:      arec_HInvKey IN mxKey: inventory key for the query.
*
* Returns:        True if the inventory is complete (inv_inv.complete_bool = 1)
*
*/
FUNCTION IsInventoryComplete( arec_HInvKey IN mxKey )
   RETURN BOOLEAN
IS
   ll_Result inv_inv.complete_bool%TYPE;

BEGIN

   SELECT
      complete_bool
   INTO
      ll_Result
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = arec_HInvKey.db_id AND
      inv_inv.inv_no_id    = arec_HInvKey.id
   ;

   RETURN ll_Result = 1;

END IsInventoryComplete;


/********************************************************************************
*
* Function:       IsAwaitingRelease
*
* Arguments:      ao_Condition IN mxCodeKey: condition to query.
*
* Returns:        True if the condition corresponds to an AWR operational status
*                 (i.e. ref_inv_cond.wo_complete_bool = 1.)
*
*/
FUNCTION IsAwaitingRelease( ao_Condition IN mxCodeKey )
   RETURN BOOLEAN
AS

   al_Result INTEGER;

BEGIN

   SELECT
      ref_inv_cond.wo_complete_bool
   INTO
      al_Result
   FROM
      ref_inv_cond
   WHERE
      ref_inv_cond.inv_cond_cd = ao_Condition.cd
   ;

   RETURN al_Result = 1;
END IsAwaitingRelease;


/********************************************************************************
*
* Function:       GetOpStatusKeyStr
*
* Arguments:      al_OpStatusDbId (IN NUMBER): inventory status key to convert.
*                 al_OpStatusCd   (IN NUMBER): ""
*
* Returns:        string value of the status key's values.
*
* Description:    Converts ref_inv_oper keys to string values.  This is used by
*                 the MaxOpStatusSeverity function to index the OP_STATUSES table.
*
*/
FUNCTION GetOpStatusKeyStr(
                    al_OpStatusDbId IN ref_inv_oper.inv_oper_db_id%TYPE,
                    al_OpStatusCd   IN ref_inv_oper.inv_oper_cd   %TYPE
                    )
   RETURN VARCHAR2
IS

BEGIN
   RETURN al_OpStatusDbId || ':' || al_OpStatusCd;
END GetOpStatusKeyStr;


/********************************************************************************
*
* Function:       GetOpStatusKeyStr
*
* Arguments:      aobj_OpStatusKey (IN mxCodeKey): inventory status key to convert.
*
* Returns:        string value of the status key's values.
*
* Description:    Converts ref_inv_oper keys to string values.  This is used by
*                 the MaxOpStatusSeverity function to index the OP_STATUSES table.
*
*/
FUNCTION GetOpStatusKeyStr2( aobj_OpStatusKey IN mxCodeKey )
   RETURN VARCHAR2
IS

BEGIN
   RETURN aobj_OpStatusKey.db_id || ':' || aobj_OpStatusKey.cd;
END GetOpStatusKeyStr2;


/********************************************************************************
*
* Function:       MaxOpStatusSeverity
*
* Arguments:      ao_OperatingStatus1 (IN mxCodeKey): inventory condition for
*                                                     comparison
*                 ao_OperatingStatus2 (IN mxCodeKey): inventory condition for
*                                                     comparison
*
* Returns:        The condition with the highest severity (ref_inv_oper.oper_ord.)
*
* Description:    Compares the severity of ao_OperatingStatus1 with ao_OperatingStatus2
*                 and returns the key with the highest severity.
*
*/
FUNCTION MaxOpStatusSeverity( ao_OperatingStatus1 IN mxCodeKey, ao_OperatingStatus2 IN mxCodeKey )
   RETURN mxCodeKey
IS
   lo_Result mxCodeKey;

   lrec_Status1 ref_inv_oper%ROWTYPE;
   lrec_Status2 ref_inv_oper%ROWTYPE;

BEGIN

   lrec_Status1 := OP_STATUSES( GetOpStatusKeyStr2( ao_OperatingStatus1 ) );
   lrec_Status2 := OP_STATUSES( GetOpStatusKeyStr2( ao_OperatingStatus2 ) );

   IF ( lrec_Status1.oper_ord > lrec_Status2.oper_ord ) THEN
      lo_Result := ao_OperatingStatus1;
   ELSE
      -- status2 is greater than or equal to status1's ord
      lo_Result := ao_OperatingStatus2;
   END IF;

   RETURN lo_Result;
END MaxOpStatusSeverity;


/********************************************************************************
*
* Function:       PartApproved
*
* Arguments:      arec_HInvKey (IN mxKey): inventory for approval check.
*
* Returns:        true if the inventory's part is approved.
*
* Description:    Verifies the approval status of inventorys' parts.
*
*/
FUNCTION PartIsApproved( arec_HInvKey IN mxKey )
   RETURN BOOLEAN
IS

   ln_PartStatusDbId eqp_part_no.part_status_db_id%TYPE;
   ln_PartStatusCd   eqp_part_no.part_status_cd   %TYPE;

BEGIN

   SELECT
      DECODE( eqp_part_no.part_status_db_id, NULL, -1, eqp_part_no.part_status_db_id ),
      DECODE( eqp_part_no.part_status_cd   , NULL, '', eqp_part_no.part_status_cd    )
   INTO
      ln_PartStatusDbId,
      ln_PartStatusCd
   FROM
      inv_inv
      LEFT JOIN eqp_part_no ON eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
                               eqp_part_no.part_no_id    = inv_inv.part_no_id
   WHERE
      inv_inv.inv_no_db_id = arec_HInvKey.db_id AND
      inv_inv.inv_no_id    = arec_HInvKey.id
   ;

   RETURN ln_PartStatusDbId = 0 AND ln_PartStatusCd = 'ACTV';
END PartIsApproved;

/********************************************************************************
*
* Function:       IsKitWithUnsvcbleSubKitInv
*
* Arguments:      arec_HInvKey IN mxKey: highest inventory key for the query.
*
* Returns:        True the inventory is kit and it has unserviceable sub-kit inventory
*
*/
FUNCTION IsKitWithUnsvcbleSubKitInv( arec_HInvKey IN mxKey )
   RETURN BOOLEAN
AS
   li_IsKit                     INTEGER;
   li_HasUnserviceableSubKitInv INTEGER;

BEGIN

   BEGIN

      SELECT
         1
      INTO
         li_IsKit
      FROM
         inv_inv
      WHERE
         inv_inv.inv_no_db_id = arec_HInvKey.db_id AND
         inv_inv.inv_no_id    = arec_HInvKey.id   AND
         inv_inv.inv_class_db_id = 0 AND
         inv_inv.inv_class_cd = 'KIT'
         AND
         ROWNUM = 1;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- Not really an exception; not a kit inventory.
         li_IsKit := 0;
   END;
   
   IF ( li_IsKit = 0 ) THEN
      RETURN FALSE;
   END IF;
   
   BEGIN
      SELECT
         1
      INTO li_HasUnserviceableSubKitInv
      FROM
         inv_kit_map
         INNER JOIN inv_inv      ON inv_inv.inv_no_db_id   = inv_kit_map.inv_no_db_id AND
                                    inv_inv.inv_no_id      = inv_kit_map.inv_no_id  AND
                                    inv_inv.rstat_cd         = 0
         INNER JOIN ref_inv_cond ON inv_inv.inv_cond_db_id = ref_inv_cond.inv_cond_db_id AND
                                    inv_inv.inv_cond_cd    = ref_inv_cond.inv_cond_cd
      WHERE
         inv_kit_map.kit_inv_no_db_id = arec_HInvKey.Db_Id AND
         inv_kit_map.kit_inv_no_id    = arec_HInvKey.Id AND
         inv_kit_map.rstat_cd            = 0
         AND
         ref_inv_cond.srv_bool = 0
         AND
         ROWNUM = 1;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         RETURN FALSE;
   END;
   RETURN li_HasUnserviceableSubKitInv = 1;
END IsKitWithUnsvcbleSubKitInv;

/******************************************************************************
* Package initialization (happens once per compilation)
*    - Creates a map of operating status keys to ref_inv_oper records
*/
BEGIN

   DECLARE
      -- Query to find all inventory operational statuses.
      CURSOR lcur_OpStatuses IS
         SELECT
            *
         FROM
            ref_inv_oper
         ;

      lrec_InvOpStatus lcur_OpStatuses%ROWTYPE;

      -- Temporary string holder.
      ls_StatusKey VARCHAR2(20);

   BEGIN
      FOR lrec_InvOpStatus IN lcur_OpStatuses LOOP

         -- Convert the inv_oper key to a string value
         ls_StatusKey := GetOpStatusKeyStr(
            lrec_InvOpStatus.inv_oper_db_id,
            lrec_InvOpStatus.inv_oper_cd
            );

         -- Associate the string key with the ref_inv_oper record.
         OP_STATUSES( ls_StatusKey ) := lrec_InvOpStatus;
      END LOOP;
   END;
END INV_STATUS;
/