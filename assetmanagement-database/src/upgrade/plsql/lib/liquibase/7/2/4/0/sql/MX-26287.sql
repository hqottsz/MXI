--liquibase formatted sql


--changeSet MX-26287:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**
 * Deletes all flight plans for the specified aircraft
 **/
CREATE OR REPLACE PROCEDURE deleteFlightPlans(
   aInvNoDbId IN evt_inv.inv_no_db_id%TYPE,
   aInvNoId   IN evt_inv.inv_no_id%TYPE
) IS 

BEGIN
   LOOP
      --SELECT FOR UPDATE SKIP LOCKED doesn't lock a single row but fetching a row does lock the row. 
      --SELECT FOR UPDATE allows a process to do row-locks,but a process will fail when it tries to 
      --lock the same row other process still locks. Using DELETE FROM directly causes a table-lock 
      --that leads to contention problems. Adding an index was considered, however it was not used 
      --since it may lead to further contention problems.     
      DELETE
      FROM  (SELECT *
             FROM   inv_ac_flight_plan
             WHERE  inv_ac_flight_plan.inv_no_db_id = aInvNoDbId AND
                    inv_ac_flight_plan.inv_no_id    = aInvNoId
             ORDER BY inv_no_db_id, inv_no_id, flight_plan_ord
            );
      IF (SQL%ROWCOUNT = 0) THEN
              EXIT;
      END IF;
   END LOOP;                       
END;
/