--liquibase formatted sql


--changeSet DEV-2227:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRSTRTUPLE');
END;
/

--changeSet DEV-2227:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE STRSTRSTRTUPLE AS OBJECT (
  element1  VARCHAR2(256),
  element2  VARCHAR2(256),
  element3  VARCHAR2(256)
);
/

--changeSet DEV-2227:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('RAW16TABLE');
END;
/

--changeSet DEV-2227:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE RAW16TABLE IS TABLE OF RAW(16);
/

--changeSet DEV-2227:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('INTEGERTABLE');
END;
/

--changeSet DEV-2227:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE INTEGERTABLE IS TABLE OF INTEGER;
/

--changeSet DEV-2227:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('VARCHAR2TABLE');
END;
/

--changeSet DEV-2227:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE VARCHAR2TABLE IS TABLE OF varchar2(256);
/

--changeSet DEV-2227:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRSTRTABLE');
END;
/

--changeSet DEV-2227:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE STRSTRSTRTABLE IS TABLE OF StrStrStrTuple;
/

--changeSet DEV-2227:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRTUPLE');
END;
/

--changeSet DEV-2227:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE STRSTRTUPLE AS OBJECT (
  element1  VARCHAR2(256),
  element2  VARCHAR2(256)
);
/

--changeSet DEV-2227:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRTABLE');
END;
/

--changeSet DEV-2227:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE STRSTRTABLE IS TABLE OF StrStrTuple;
/

--changeSet DEV-2227:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE ARRAY_PKG
   IS

   /*****************************************************************************
   *
   * Arguments:     Array of UUID keys in RAW16 format.
   * Description:   Converts the given array into table for further data select.
   *
   * Orig.Coder:    Yuriy Vakulenko
   * Recent Coder:
   * Recent Date:   June 07, 2013
   *
   ******************************************************************************/
   FUNCTION getRaw16Table (aRaw16Array RAW16TABLE) RETURN RAW16TABLE
     PIPELINED;
  /*****************************************************************************
   *
   * Arguments:     Array of String data.
   * Description:   Converts the given array into table for further data select.
   *
   * Orig.Coder:    HWang
   * Recent Coder:
   * Recent Date:   June 21, 2013
   *
   ******************************************************************************/
   FUNCTION getStrTable (
      aElementArray VARCHAR2TABLE) RETURN VARCHAR2TABLE
   PIPELINED;
   /*****************************************************************************
   *
   * Arguments:     Arrays of String-String tuple data.
   * Description:   Converts the given arrays into table for further data select.
   *
   * Orig.Coder:    Yuriy Vakulenko
   * Recent Coder:
   * Recent Date:   June 07, 2013
   *
   ******************************************************************************/
   FUNCTION getStrStrTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE) RETURN StrStrTable
   PIPELINED;

   /*****************************************************************************
   *
   * Arguments:     Arrays of Strin-String-String tuple data.
   * Description:   Converts the given arrays into table for further data select.
   *
   * Orig.Coder:    Yuriy Vakulenko
   * Recent Coder:
   * Recent Date:   June 07, 2013
   *
   ******************************************************************************/
   FUNCTION getStrStrStrTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE,
      aElement3Array VARCHAR2TABLE) RETURN StrStrStrTable
   PIPELINED;

   /*****************************************************************************
   *
   * Arguments:     Arrays of integers, representing the DB_ID:ID tuples 
   *                of standard MX_KEY object.
   * Description:   Converts the given arrays into table for further data select.
   *
   * Orig.Coder:    Yuriy Vakulenko
   * Recent Coder:
   * Recent Date:   June 07, 2013
   *
   ******************************************************************************/
   FUNCTION getIntIntTable (
      aDbIdArray INTEGERTABLE,
      aIdArray   INTEGERTABLE) RETURN MXKEYTABLE
     PIPELINED;

END Array_PKG;
/

--changeSet DEV-2227:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY ARRAY_PKG IS

   /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getRaw16Table (aRaw16Array RAW16TABLE) RETURN RAW16TABLE 
   PIPELINED AS
   BEGIN
      FOR i IN 1..aRaw16Array.count LOOP
         PIPE ROW(aRaw16Array(i));   
      END LOOP;
      RETURN;
   END;
   
  /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getStrTable (
      aElementArray VARCHAR2TABLE) RETURN VARCHAR2TABLE
   PIPELINED AS
   BEGIN
      FOR i IN 1..aElementArray.count LOOP
         PIPE ROW(aElementArray(i));
      END LOOP;
      RETURN;
   END;
   
   /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getStrStrTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE) RETURN StrStrTable
   PIPELINED AS
   BEGIN
      FOR i IN 1..aElement1Array.count LOOP
         PIPE ROW(StrStrTuple(
            aElement1Array(i), 
            aElement2Array(i))
         );
      END LOOP;
      RETURN;
   END;

   /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getStrStrStrTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE,
      aElement3Array VARCHAR2TABLE) RETURN StrStrStrTable
   PIPELINED AS
   BEGIN
      FOR i IN 1..aElement1Array.count LOOP
         PIPE ROW(StrStrStrTuple(
            aElement1Array(i), 
            aElement2Array(i), 
            aElement3Array(i))
         );
      END LOOP;
      RETURN;
   END;

   /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getIntIntTable (
      aDbIdArray INTEGERTABLE,
      aIdArray   INTEGERTABLE) RETURN MXKEYTABLE
   PIPELINED AS
   BEGIN
      FOR i IN 1..aDbIdArray.count LOOP
         PIPE ROW(MXKEY(aDbIdArray(i), aIdArray(i)));
      END LOOP;
      RETURN;
   END;

END ARRAY_PKG;
/