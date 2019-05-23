--liquibase formatted sql


--changeSet MX-21434:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- This script:
--    * Increases org_carrier.iata_cd from 2 => 3 characters
--    * Increases org_carrier.icao_cd from 3 => 4 characters
--    * Adds org_carrier.carrier_cd
--       * Populates it with iata_cd + '-' + icao_cd
--       * (Note that carrier_cd is not made uniquenot-null.  Rows with rstat_cd > 0 prevent this.)
-- Increase iata_cd from 2 to 3 chars.
BEGIN
    utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE "ORG_CARRIER"
      MODIFY (
         "IATA_CD" Varchar2(3)
      )
    ');
END;
/

--changeSet MX-21434:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Increase icao_cd from 3 to 4 chars.
BEGIN
    utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE "ORG_CARRIER"
      MODIFY (
         "ICAO_CD" Varchar2(4)
      )
    ');
END;
/

--changeSet MX-21434:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add carrier_cd field.
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE "ORG_CARRIER"
      ADD (
         "CARRIER_CD" Varchar2 (8)
      )
   ');
END;
/

--changeSet MX-21434:4 stripComments:false
-- Set default value for carrier code.
UPDATE "ORG_CARRIER" SET
   CARRIER_CD = IATA_CD || '-' || ICAO_CD
WHERE
   rstat_cd = 0
;

--changeSet MX-21434:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Construct a lable for an operator
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

--changeSet MX-21434:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constructs a textual description for an operator.
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