--liquibase formatted sql


--changeSet GENERATE_TASK_BARCODE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION generate_task_barcode RETURN VARCHAR2 IS

   ldbid    VARCHAR2(30);
   lbarcode VARCHAR2(30);
   lnum     NUMBER;

   CURSOR lcur_existingbarcode(as_barcode VARCHAR2) IS
      SELECT sched_stask.barcode_sdesc
        FROM sched_stask
       WHERE sched_stask.barcode_sdesc = as_barcode
         AND sched_stask.rstat_cd = 0;
   lrec_existingbarcode lcur_existingbarcode%ROWTYPE;
BEGIN

   SELECT convertbase10to34(db_id)
     INTO ldbid
     FROM mim_local_db;

   WHILE TRUE
   LOOP
      -- get the numerical portion of the barcode
      SELECT task_barcode_seq.NEXTVAL
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

      -- Prefix the barcode with T
      lbarcode := 'T' || ldbid || lbarcode;

      -- verify that it does not already exist
      OPEN lcur_existingbarcode(lbarcode);
      FETCH lcur_existingbarcode
         INTO lrec_existingbarcode;

      IF NOT lcur_existingbarcode%FOUND
      THEN
         CLOSE lcur_existingbarcode;
         RETURN(lbarcode);
      END IF;
      CLOSE lcur_existingbarcode;
   END LOOP;
END generate_task_barcode;
/