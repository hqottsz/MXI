--liquibase formatted sql


--changeSet ROUTINE_MANHOURS_PKG_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE ROUTINE_MANHOURS_PKG IS

-- Basic error handling codes
 	icn_Success CONSTANT NUMBER := 1;       -- Success
 	icn_NoProc  CONSTANT NUMBER := 0;       -- No processing done
 	icn_Error   CONSTANT NUMBER := -1;      -- Error

FUNCTION calculateRoutineManhrsForBlock
(
   an_BlockDbId task_task.task_db_id%TYPE,
   an_BlockId   task_task.task_id%TYPE
)RETURN tab_PTRoutineManHours;

END ROUTINE_MANHOURS_PKG;
/