--liquibase formatted sql


--changeSet GENERATE_INV_BARCODE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION generate_inv_barcode RETURN VARCHAR2 IS

   ldbid    VARCHAR2(30);
   lbarcode VARCHAR2(30);
   lnum     NUMBER;

BEGIN

   -- get the numerical portion of the barcode
   SELECT inv_barcode_seq.NEXTVAL
     INTO lnum
     FROM dual;

   -- Convert to base 34
   lbarcode := convertbase10to34(lnum);

   -- pad right hand side with 0's to make a 7 digit string
   SELECT lpad(lbarcode,
               7,
               '0')
     INTO lbarcode
     FROM dual;

   SELECT convertbase10to34(db_id)
     INTO ldbid
     FROM mim_local_db;

   -- Prefix the barcode with 'I' and return
   RETURN('I' || ldbid || lbarcode);

END generate_inv_barcode;
/