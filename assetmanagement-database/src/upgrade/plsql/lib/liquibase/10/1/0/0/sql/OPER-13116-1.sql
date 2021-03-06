--liquibase formatted sql

--changeSet OPER-13116-SP4:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      calculateUsageRemaining
* Arguments:     aDueUsageQt - usage due quantity
*                aUsageQt - inventory's current usage quantity
				 aPrecisionQt - data type's precision

* Description:   This function returns usage remaining which is the "Due Usage" minus the "Usage" rounded off to the number of decimal places dictated by the "Precision"
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION calculateUsageRemaining
(  
    aDueUsageQt FLOAT,
	aUsageQt FLOAT,
    aPrecisionQt NUMBER
)  RETURN FLOAT
IS
   lUsageRemaining FLOAT;
BEGIN
   lUsageRemaining := ROUND(aDueUsageQt - aUsageQt,aPrecisionQt);
RETURN lUsageRemaining;   
END calculateUsageRemaining;
/