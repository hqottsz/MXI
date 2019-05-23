--liquibase formatted sql


--changeSet OPER-2848:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add API action config parms
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
    'API_BATCH_ASSET_REQUEST', 
	  'Permission to search Batch Assets',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ASSET', 
	  '8.1-SP3', 
	  0, 
	  0,
    UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-2848:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add the new string 4-tuple types
****************************************************************/
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRSTRSTRTUPLE');
END;
/

--changeSet OPER-2848:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE "STRSTRSTRSTRTUPLE" AS OBJECT (
  element1  VARCHAR2(256),
  element2  VARCHAR2(256),
  element3  VARCHAR2(256),
  element4  VARCHAR2(256)
);
/

--changeSet OPER-2848:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('STRSTRSTRSTRTABLE');
END;
/

--changeSet OPER-2848:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE "STRSTRSTRSTRTABLE" IS TABLE OF StrStrStrStrTuple;
/

--changeSet OPER-2848:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add the newly-modified array package
****************************************************************/
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

END ARRAY_PKG;
/

--changeSet OPER-2848:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

END Array_PKG;
/