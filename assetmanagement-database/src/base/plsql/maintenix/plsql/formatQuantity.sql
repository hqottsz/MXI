--liquibase formatted sql


--changeSet formatQuantity:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      formatQuantity
* Arguments:     aQuantity         - the quantity (can be null).
*                aDecimalPlaces    - Formats the quantity/unit of measure to the correct number of decimal places.
*                aUnitCd           - the unit code.
* Description:   Formats the quantity of measure using number of decimal places
*
* Orig.Coder:    hmuradyan
* Recent Coder:  hmuradyan
* Recent Date:   June 9, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION formatQuantity
(
    aQuantity        FLOAT,
    aDecimalPlaces   NUMBER,
    aUnitCd          STRING
) RETURN STRING
IS
  lQuantity    FLOAT;
  lFormat      STRING(255);
  lDecimalPart STRING(255);
BEGIN
   lQuantity := nvl(aQuantity, 0);
   lQuantity := round(lQuantity, aDecimalPlaces);

   lDecimalPart := '';

   IF aDecimalPlaces > 0 THEN
     lFormat := 'D';
     lFormat := RPAD( lFormat, aDecimalPlaces + 1, '0' );

     lDecimalPart := trim(to_char(lQuantity - trunc(lQuantity), lFormat));
   END IF;


   RETURN trunc(lQuantity) || lDecimalPart || ' ' || nvl(aUnitCd, '');

END formatQuantity;
/