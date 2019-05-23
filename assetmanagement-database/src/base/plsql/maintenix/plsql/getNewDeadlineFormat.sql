--liquibase formatted sql


--changeSet getNewDeadlineFormat:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:      getNewDeadlineFormat
* Arguments:     aNewDeadLine - VARCHAR2
*
* Description:   This function will return the new deadline format like the following
*		 289.1 HOUR (301.1 HOUR)
*		 10-APR-2008 11:59
* Orig.Coder:    ycho
* Recent Coder:  ycho
* Recent Date:   2008.02.19
*********************************************************************************/
CREATE OR REPLACE FUNCTION getNewDeadlineFormat
(
  aNewDeadLine     	VARCHAR2
) RETURN VARCHAR2
IS
   /* local variables */
   lDomainType     	mim_data_type.domain_type_cd%TYPE;
   lUsageRemaining	evt_sched_dead.usage_rem_qt%TYPE;
   lEngineeringUnit	mim_data_type.eng_unit_cd%TYPE;
   lMultiplier      	ref_eng_unit.ref_mult_qt%TYPE;
   lDeadlineDate   	evt_sched_dead.sched_dead_dt%TYPE;
   lDeviation     	evt_sched_dead.deviation_qt%TYPE;

BEGIN

   lDomainType := SUBSTR(aNewDeadLine, 1, INSTR(aNewDeadLine, '*', 1, 1)-1);
   lUsageRemaining := SUBSTR(aNewDeadLine, INSTR(aNewDeadLine, '*', 1, 1)+1,
                           INSTR(aNewDeadLine,'*', 1, 2) - INSTR(aNewDeadLine, '*', 1, 1)-1);
   lEngineeringUnit := SUBSTR(aNewDeadLine, INSTR(aNewDeadLine, '*', 1, 2)+1,
                           INSTR(aNewDeadLine,'*', 1, 3) - INSTR(aNewDeadLine, '*', 1, 2)-1);
   lMultiplier := SUBSTR(aNewDeadLine, INSTR(aNewDeadLine, '*', 1, 3)+1,
                           INSTR(aNewDeadLine,'*', 1, 4) - INSTR(aNewDeadLine, '*', 1, 3)-1);
   lDeadlineDate := TO_DATE( SUBSTR(aNewDeadLine, INSTR(aNewDeadLine, '*', 1, 4)+1,
                           INSTR(aNewDeadLine,'*', 1, 5) - INSTR(aNewDeadLine, '*', 1, 4)-1), 'DD-MM-YYYY HH24:MI:SS');
   lDeviation := SUBSTR(aNewDeadLine, INSTR(aNewDeadLine,'*', 1, 5)+1,
                       INSTR(aNewDeadLine,'*', 1, 6) - INSTR(aNewDeadLine, '*', 1, 5)-1);

   RETURN getDeadlineFormat(lDomainType, lUsageRemaining, lEngineeringUnit, lMultiplier, lDeadlineDate, lDeviation);

END getNewDeadlineFormat;
/