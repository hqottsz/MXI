--liquibase formatted sql


--changeSet MX-26816-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:      getDeadlineFormat
* Arguments:     aDomainType - Domain type
*		 aUsageRemaining - usage remaining
*		 aEngineeringUnit - engineering unit
*                aMultiplier - muliplier
*		 aDeadlineDate - deadline date
*		 aDeviation - deviation
*
* Description:   This function will return the new deadline format like the following
*		 289.1 HOUR (301.1 HOUR)
*		 2008-04-10 11:59
*
* Orig.Coder:    ycho
* Recent Coder:  ycho
* Recent Date:   2008.02.15
*********************************************************************************/
CREATE OR REPLACE FUNCTION getDeadlineFormat
(
   aDomainType     	mim_data_type.domain_type_cd%TYPE,
   aUsageRemaining	evt_sched_dead.usage_rem_qt%TYPE,
   aEngineeringUnit	mim_data_type.eng_unit_cd%TYPE,
   aMultiplier      	ref_eng_unit.ref_mult_qt%TYPE,
   aDeadlineDate   	evt_sched_dead.sched_dead_dt%TYPE,
   aDeviation     	evt_sched_dead.deviation_qt%TYPE
   
) RETURN VARCHAR2
IS    

   /* local variables */
   lDeadline 		VARCHAR2(400);
   lUsageRemDev 	NUMBER;
   lMultiplier 		NUMBER;
   lMillisPerDay 	NUMBER;
   lMillisToAdd 	NUMBER;
        
BEGIN
   lDeadline := null;
   lUsageRemDev := aUsageRemaining + aDeviation;
   lMillisPerDay := 1000 * 60 * 60 * 24;
   lMillisToAdd := 0;
   
   IF (aMultiplier IS NOT NULL) THEN
      lMultiplier := aMultiplier;
   ELSE
      lMultiplier := 1.0;
   END IF;
      
   -- CASE 1: USAGED-BASED PARAMETER
   IF ( aDomainType = 'US' ) THEN
   	  lDeadline := TO_CHAR(aUsageRemaining) || ' ' || aEngineeringUnit;
        
      -- if the deviation is not null and 0
      IF ( ( aDeviation IS NOT NULL ) AND ( aDeviation != 0 ) ) THEN        
         lDeadline := lDeadline || ' (' || TO_CHAR(lUsageRemDev) || ' ' || aEngineeringUnit || ')' ; 
      END IF;     
      
      IF (aDeadlineDate IS NULL) THEN
         lDeadline := lDeadline || chr(13) || 'N/A';
      ELSE
         lDeadline := lDeadline || chr(13) || TO_CHAR(aDeadlineDate, 'DD-MON-YYYY HH24:MI') ;
      END IF; 
      
   ELSE
      IF (aDeadlineDate IS NOT NULL) THEN
         lDeadline := TO_CHAR(aDeadlineDate, 'DD-MON-YYYY HH24:MI') ;
      END IF;
       
      -- if the deviation is not null and 0
      IF ( ( aDeviation IS NOT NULL ) AND ( aDeviation != 0 ) ) THEN        
         lMillisToAdd := aDeviation * lMultiplier * lMillisPerDay ;
         IF (aDeadlineDate IS NULL) THEN
            lDeadline := lDeadline || chr(13) || 'N/A';
         ELSE
            lDeadline := lDeadline || chr(13) ||  ' (' || TO_CHAR( aDeadlineDate + (lMillisToAdd / 86400000), 'DD-MON-YYYY HH24:MI' ) || ')' ;
         END IF; 
      END IF;  
          
   END IF;
   
   
   RETURN lDeadline;

END getDeadlineFormat;
/