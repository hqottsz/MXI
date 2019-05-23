--liquibase formatted sql


--changeSet array_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

   /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getTableOfIntIntTuple (
      aElement1Array INTEGERTABLE,
      aElement2Array INTEGERTABLE) RETURN IntIntTable
   PIPELINED AS
   BEGIN
      FOR i IN 1..aElement1Array.count LOOP
         PIPE ROW(IntIntTUPLE(
            aElement1Array(i),
            aElement2Array(i))
         );
      END LOOP;
      RETURN;
   END;

   /*****************************************************************************
   * @inheritDoc
   ******************************************************************************/
   FUNCTION getIntIntIntIntFloatTable (
      aElement1Array INTEGERTABLE,
      aElement2Array INTEGERTABLE,
      aElement3Array INTEGERTABLE,
      aElement4Array INTEGERTABLE,
      aElement5Array FLOATTABLE ) RETURN IntIntIntIntFloatTable
   PIPELINED AS
   BEGIN
      FOR i IN 1..aElement1Array.count LOOP
         PIPE ROW(IntIntIntIntFloatTUPLE(
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