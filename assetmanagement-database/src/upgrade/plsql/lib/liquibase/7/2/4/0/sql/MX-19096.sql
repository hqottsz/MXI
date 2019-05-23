--liquibase formatted sql


--changeSet MX-19096:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getDueDate_PlanByDate
(
   aSchedDeadDt    evt_sched_dead.sched_dead_dt%TYPE,
   aPlanByDate     sched_stask.plan_by_dt%TYPE

) RETURN DATE
IS
   lDueDt DATE;

BEGIN 
   IF
      aPlanByDate IS NOT NULL THEN
      IF 
         aSchedDeadDt IS NOT NULL AND aPlanByDate > aSchedDeadDt THEN
         lDueDt := aSchedDeadDt;
      ELSE
         lDueDt := aPlanByDate;
      END IF;
   ELSE
      lDueDt := aSchedDeadDt;
   END IF;

RETURN lDueDt;

END getDueDate_PlanByDate;
/