--liquibase formatted sql


--changeSet array_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

   /*****************************************************************************
   *
   * Arguments:     Arrays of Integer-Integer tuple data.
   * Description:   Converts the given arrays into table for further data select.
   *
   ******************************************************************************/
   FUNCTION getTableOfIntIntTuple (
      aElement1Array INTEGERTABLE,
      aElement2Array INTEGERTABLE) RETURN IntIntTable
     PIPELINED;

   /*****************************************************************************
   *
   * Arguments:     Arrays of Integer-Integer-Integer-Integer-Float tuple data.
   * Description:   Converts the given arrays into table for further data select.
   *
   ******************************************************************************/
   FUNCTION getIntIntIntIntFloatTable (
      aElement1Array INTEGERTABLE,
      aElement2Array INTEGERTABLE,
      aElement3Array INTEGERTABLE,
      aElement4Array INTEGERTABLE,
      aElement5Array FLOATTABLE) RETURN IntIntIntIntFloatTable
     PIPELINED;

END ARRAY_PKG;
/