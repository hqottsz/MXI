--liquibase formatted sql


--changeSet getOperatorText:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getOperatorText
* Arguments:     as_cd           - an operator's code
*                as_iataCd       - an operator's IATA code
*                as_icaoCd       - an operator's ICAO code
*
* Description:   Constructs a textual description for an operator.  
*                Format is "operator code / ICAO / IATA"
*                If as_cd is null, null is returned
*                All other parameters are optional.
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getOperatorText
(
    as_cd          org_carrier.carrier_cd   %TYPE,
    as_iataCd      org_carrier.iata_cd      %TYPE,
    as_icaoCd      org_carrier.icao_cd      %TYPE

) RETURN STRING

IS
   ls_Result VARCHAR2( 21 ) := '';

BEGIN

   IF ( as_cd IS NOT NULL ) THEN
      SELECT
         as_cd ||
            DECODE( as_iataCd, NULL, '', ' / ' || as_iataCd ) ||
            DECODE( as_icaoCd, NULL, '', ' / ' || as_icaoCd )
      INTO
         ls_Result
      FROM DUAL;
   END IF;

   RETURN ls_Result;

END getOperatorText;
/ 