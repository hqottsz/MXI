--liquibase formatted sql


--changeSet CONVERTBASE10TO34:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace function CONVERTBASE10TO34(aBase10Num IN NUMBER) return varchar2 is
begin
  IF aBase10Num > 0
   THEN
      RETURN CONVERTBASE10TO34 (TRUNC (aBase10Num / 34)) ||
             SUBSTR ('0123456789ABCDEFGHJKLMNPQRSTUVWXYZ', MOD (aBase10Num, 34) + 1, 1);
   ELSE
      RETURN NULL;
   END IF;
end CONVERTBASE10TO34;
/