--liquibase formatted sql


--changeSet inv_status_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE INV_STATUS
IS



/********************************************************************************
*
* Function:       calculateStatus
* Arguments:      al_InvNoDbId (IN NUMBER): inventory for status calculation
*                 al_InvNoId   (IN NUMBER): ""
* Returns:        an InventoryStatus object with a new condition for all types of
*                 inventory, and a condition+operating status for aircraft.
*
* Description:    Calculates inventory condition/status based on related
*                 information (overdue tasks, open faults, etc.)
*
*********************************************************************************
*
* Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/


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
FUNCTION calculateStatusRecord(
                         al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         al_InvNoId   IN inv_inv.inv_no_id%TYPE
                        )
   RETURN InventoryStatus;


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
FUNCTION calculateStatus(
                         al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         al_InvNoId   IN inv_inv.inv_no_id%TYPE
                        )
   RETURN InventoryStatusTable PIPELINED;


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
   RETURN NUMBER;

END INV_STATUS;
/