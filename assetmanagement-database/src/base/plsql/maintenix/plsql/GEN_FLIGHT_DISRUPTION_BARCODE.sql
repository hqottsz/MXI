--liquibase formatted sql


--changeSet GEN_FLIGHT_DISRUPTION_BARCODE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION GEN_FLIGHT_DISRUPTION_BARCODE RETURN VARCHAR2 is

  lBarcode          VARCHAR2(30);
  lNum              NUMBER;

  CURSOR lcur_ExistingBarcode ( as_Barcode VARCHAR2) IS
        SELECT FL_LEG_DISRUPT.DISRUPTION_DESC
        FROM   FL_LEG_DISRUPT
        WHERE  FL_LEG_DISRUPT.DISRUPTION_DESC = as_Barcode;
  lrec_ExistingBarcode lcur_ExistingBarcode%ROWTYPE;
BEGIN
  WHILE TRUE LOOP
        -- get the numerical portion of the barcode
        SELECT FLIGHT_DISRUPTION_BARCODE_SEQ.NEXTVAL INTO lNum FROM dual;

        -- Convert to base 34
        lBarcode := CONVERTBASE10TO34(lNum);

        -- pad right hand side with 0's to make a 7 digit string
        SELECT Lpad(lBarcode,7,'0') into lBarcode from dual;

        -- Prefix the barcode with T
        lBarcode := 'F' || lBarcode;

        -- verify that it does not already exist
        OPEN lcur_ExistingBarcode(lBarcode);
        FETCH lcur_ExistingBarcode INTO lrec_ExistingBarcode;

        IF NOT lcur_ExistingBarcode%FOUND THEN
            RETURN(lBarcode);
        END IF;
        CLOSE lcur_ExistingBarcode;
  END LOOP;
END GEN_FLIGHT_DISRUPTION_BARCODE;
/