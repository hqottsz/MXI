--liquibase formatted sql


--changeSet GetSequenceNextValue:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION GetSequenceNextValue
(
   sequenceCode IN utl_sequence.sequence_cd%TYPE
) RETURN utl_sequence.next_value%TYPE AS PRAGMA AUTONOMOUS_TRANSACTION;

  result NUMBER;

BEGIN

   SELECT next_value INTO result FROM utl_sequence WHERE sequence_cd = sequenceCode;
   UPDATE utl_sequence SET next_value = next_value + 1 WHERE sequence_cd = sequenceCode;

   COMMIT;

   RETURN result;

END GetSequenceNextValue;
/