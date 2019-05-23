--liquibase formatted sql


--changeSet toLabel:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      toLabel
* Arguments:     as_name         - the name for a business entity
*                as_description  - the description for a business entity
* Description:   Constructs a label for a business entity using the entity's 
*                name and description: "Name (Description)".
*
* Orig.Coder:    Sylvain Charette
* Recent Coder:  Sylvain Charette
* Recent Date:   January 24, 2008
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION toLabel
(
    as_name        STRING,
    as_description STRING
) RETURN STRING
IS
BEGIN

   RETURN as_name || ' (' || as_description || ')';

END toLabel;
/