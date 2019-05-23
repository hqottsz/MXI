--liquibase formatted sql

/**************************************************************************************
* 
* OPER-10459 Create a Log Table to Capture Error Messages Encountered by Users 
*
* Table Name:  UTL_ERROR_LOG
* Primary Key: No
* Foreign Key: No
* TRIGGERS: No
*
***************************************************************************************/

--changeSet OPER-10459:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

  -- create the new mapping table
  utl_migr_schema_pkg.table_create('
   CREATE TABLE UTL_ERROR_LOG(
    SOURCE_SDESC    VARCHAR2 (500) NOT NULL ,
    EXCEPTION_SDESC VARCHAR2 (500) NOT NULL ,
    ERROR_CD        VARCHAR2 (80) ,
    ERROR_SDESC     VARCHAR2 (1000) ,
    LOG_DT          DATE NOT NULL
   )
  ');
  
END;
/
