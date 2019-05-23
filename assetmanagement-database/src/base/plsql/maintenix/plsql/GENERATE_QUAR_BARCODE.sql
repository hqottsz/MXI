--liquibase formatted sql


--changeSet GENERATE_QUAR_BARCODE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace function GENERATE_QUAR_BARCODE return varchar2 is

  lBarcode VARCHAR2(30);
  lNum     NUMBER;

begin

  -- get the numerical portion of the barcode
  SELECT quar_barcode_seq.NEXTVAL INTO lNum FROM dual;

  -- Convert to base 34
  lBarcode := CONVERTBASE10TO34(lNum);

  -- pad right hand side with 0's to make a 7 digit string
  Select Lpad(lBarcode,7,'0') into lBarcode from dual;

  -- Prefix the barcode with 'Q' and return
  return('Q' || lBarcode);

end GENERATE_QUAR_BARCODE;
/