--liquibase formatted sql


--changeSet isLocationCapableForCheck:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isLocationCapableForCheck
* Arguments:      aCheckDbId: the DbId of the check
*                 aCheckId: the Id of the check
*                 aAssemblyDbId: the DbId of the assembly
*                 aAssemblyCd: the code of the assembly
*                 aLocDbId: the DbId of the location 
*                 aLocId: the Id of the location
* Description:    This function checks if the location has the capabilites to
*                 perform the check. 
*                 The return value should be treated as a boolean: 0 (false), 1 (true)
*********************************************************************************/
CREATE OR REPLACE FUNCTION isLocationCapableForCheck(
   aCheckDbId   IN evt_event.event_db_id%TYPE,
   aCheckId     IN evt_event.event_id%TYPE,
   aLocDbId   IN inv_loc.loc_db_id%TYPE,
   aLocId     IN inv_loc.loc_id%TYPE
)

RETURN NUMBER AS

lNotCapable NUMBER;

BEGIN

lNotCapable := 0;

         SELECT   COUNT(*)
         INTO     lNotCapable
         FROM
                  sched_work_type,
                  inv_inv,
                  evt_event,
                  sched_stask
         WHERE
                  sched_stask.sched_db_id = aCheckDbId AND
                  sched_stask.sched_id    = aCheckId AND
                  sched_stask.rstat_cd = 0
                  AND
                  evt_event.h_event_db_id = sched_stask.sched_db_id AND
                  evt_event.h_event_id    = sched_stask.sched_id    AND
                  evt_event.hist_bool     = 0
                  AND
                  inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
                  inv_inv.inv_no_id    = sched_stask.main_inv_no_id
                  AND
                  sched_work_type.sched_db_id = evt_event.event_db_id AND
                  sched_work_type.sched_id    = evt_event.event_id
                  AND NOT EXISTS
                  (--and where these work types are not supported by the work location
                   SELECT 1
                   FROM   inv_loc_capability
                   WHERE  inv_loc_capability.loc_db_id = aLocDbId AND
                          inv_loc_capability.loc_id    = aLocId
                          AND
                          inv_loc_capability.assmbl_db_id = inv_inv.assmbl_db_id AND
                          inv_loc_capability.assmbl_cd    = inv_inv.assmbl_cd
                          AND
                          inv_loc_capability.work_type_db_id = sched_work_type.work_type_db_id AND
                          inv_loc_capability.work_type_cd    = sched_work_type.work_type_cd
                  );

   IF ( lNotCapable > 0) THEN
      RETURN 0;
   END IF;

   RETURN 1;
END isLocationCapableForCheck;
/