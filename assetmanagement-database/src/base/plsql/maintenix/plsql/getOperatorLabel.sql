--liquibase formatted sql


--changeSet getOperatorLabel:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getOperatorLabel
* Arguments:     as_cd           - an operator's code
*                as_iataCd       - an operator's IATA code
*                as_icaoCd       - an operator's ICAO code
*                as_name         - an operator's name
*
* Description:   Constructs a label for an operator.  
*                Format is "operator code / ICAO / IATA (operator name)"
*                If as_cd is null, null is returned
*                All other parameters are optional.
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getOperatorLabel
(
    as_cd          org_carrier.carrier_cd   %TYPE,
    as_iataCd      org_carrier.iata_cd      %TYPE,
    as_icaoCd      org_carrier.icao_cd      %TYPE,
    as_name        org_org.org_sdesc        %TYPE

) RETURN STRING

IS
   ls_TextResult  VARCHAR2( 21 );
   ls_LabelResult VARCHAR2( 64 ) := '';

BEGIN

   ls_TextResult := getOperatorText( as_cd, as_iataCd, as_icaoCd );
   IF ( ls_TextResult IS NOT NULL ) THEN
      IF ( as_name IS NOT NULL ) THEN
         ls_LabelResult := toLabel(
            ls_TextResult,
            as_name
            );
      ELSE
         ls_LabelResult := ls_TextResult;
      END IF;
   END IF;

   RETURN ls_LabelResult;

END getOperatorLabel;
/