--liquibase formatted sql


--changeSet part_no_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE PART_NO_PKG
IS
/********************************************************************************
*
* Function:      getMonthlyDemandForPart
* Arguments:     aPartDbId, aPartId - pk for the part
* Description:   This function calculates the monthly demand for the given part by
*                counting the number of installed inventories for that part within
*                the past year and dividing the sum by 12 months. If a part has not been
*                issued within the past year, we assign it 1 issue for that year.
*
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
FUNCTION getMonthlyDemandForPart(
    aPartDbId IN eqp_part_no.part_no_db_id%TYPE,
    aPartId   IN eqp_part_no.part_no_id%TYPE
) RETURN FLOAT;


/********************************************************************************
*
* Function:      getPartValue
* Arguments:     aPartDbId, aPartId - pk for the part
* Description:   This function calculates the part's value by multiplying
*                the average price times the monthly demand for the given part
*
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
FUNCTION getPartValue(
    aPartDbId IN eqp_part_no.part_no_db_id%TYPE,
    aPartId   IN eqp_part_no.part_no_id%TYPE
) RETURN FLOAT;


/********************************************************************************
*
* Function:      getTotalPartValue
* Description:   This function calculates the total part value for all part's in the table
*
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
FUNCTION getTotalPartValue
RETURN FLOAT;


/********************************************************************************
*
* Procedure:     recalculateABCClass
* Description:   This procedure calculates the abc class value for every part in eqp_part_no.
*                If a new ABC class value is calculated, and the calc_abc_class_bool is true,
*                the procedure updates the class key and updates the inv_loc_part_count.next_count_dt
*                value.
*                ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
PROCEDURE recalculateABCClass;
END PART_NO_PKG;
/