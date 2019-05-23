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