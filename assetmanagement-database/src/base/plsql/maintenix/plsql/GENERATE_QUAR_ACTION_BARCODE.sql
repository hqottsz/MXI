--liquibase formatted sql


--changeSet GENERATE_QUAR_ACTION_BARCODE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace function GENERATE_QUAR_ACTION_BARCODE return varchar2 is

  lBarcode VARCHAR2(30);
  lNum     NUMBER;

begin

  -- get the numerical portion of the barcode
  SELECT quar_action_barcode_seq.NEXTVAL INTO lNum FROM dual;

  -- Convert to base 34
  lBarcode := CONVERTBASE10TO34(lNum);

  -- pad right hand side with 0's to make a 5 digit string
  Select Lpad(lBarcode,5,'0') into lBarcode from dual;

  -- Prefix the barcode with 'QCA' and return
  return('QCA' || lBarcode);

end GENERATE_QUAR_ACTION_BARCODE;
/