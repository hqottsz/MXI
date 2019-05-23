--liquibase formatted sql


--changeSet OPER-8127:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('FLOATTABLE');
END;
/

--changeSet OPER-8127:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE FloatTable
IS
  TABLE OF FLOAT ;
  /

--changeSet OPER-8127:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRSTRSTRFLOATTUPLE');
END;
/

--changeSet OPER-8127:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE StrStrStrStrFloatTuple
AS
  OBJECT
  (
    element1 VARCHAR2 (256) ,
    element2 VARCHAR2 (256) ,
    element3 VARCHAR2 (256) ,
    element4 VARCHAR2 (256) ,
    element5 FLOAT ) NOT FINAL ;
  /

--changeSet OPER-8127:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRSTRSTRFLOATTABLE');
END;
/

--changeSet OPER-8127:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE StrStrStrStrFloatTable
IS
  TABLE OF StrStrStrStrFloatTuple ;
  /

--changeSet OPER-8127:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   * Arguments:     Arrays of String-String-String-String tuple data.
   * Description:   Converts the given arrays into table for further data select.
   *
   * Orig.Coder:    Nicholas Bale
   * Recent Coder:
   * Recent Date:   March 3, 2015
   *
   ******************************************************************************/
   FUNCTION getStrStrStrStrTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE,
      aElement3Array VARCHAR2TABLE,
      aElement4Array VARCHAR2TABLE) RETURN StrStrStrStrTable
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

   /*****************************************************************************
   *
   * Arguments:     Arrays of String-String-String-String-Float tuple data.
   * Description:   Converts the given arrays into table for further data select.
   *
   * Orig.Coder:    Joe Liu
   * Recent Coder:
   * Recent Date:   Oct 17, 2016
   *
   ******************************************************************************/
   FUNCTION getStrStrStrStrFloatTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE,
      aElement3Array VARCHAR2TABLE,
      aElement4Array VARCHAR2TABLE,
      aElement5Array FLOATTABLE) RETURN StrStrStrStrFloatTable
     PIPELINED;

END Array_PKG;
/

--changeSet OPER-8127:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   FUNCTION getStrStrStrStrTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE,
      aElement3Array VARCHAR2TABLE,
      aElement4Array VARCHAR2TABLE) RETURN StrStrStrStrTable
   PIPELINED AS
   BEGIN
      FOR i IN 1..aElement1Array.count LOOP
         PIPE ROW(StrStrStrStrTuple(
            aElement1Array(i),
            aElement2Array(i),
            aElement3Array(i),
            aElement4Array(i))
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

   /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getStrStrStrStrFloatTable (
      aElement1Array VARCHAR2TABLE,
      aElement2Array VARCHAR2TABLE,
      aElement3Array VARCHAR2TABLE,
      aElement4Array VARCHAR2TABLE,
      aElement5Array FLOATTABLE ) RETURN StrStrStrStrFloatTable
   PIPELINED AS
   BEGIN
      FOR i IN 1..aElement1Array.count LOOP
         PIPE ROW(StrStrStrStrFloatTuple(
            aElement1Array(i),
            aElement2Array(i),
            aElement3Array(i),
            aElement4Array(i),
            aElement5Array(i))
         );
      END LOOP;
      RETURN;
   END;

END ARRAY_PKG;
/