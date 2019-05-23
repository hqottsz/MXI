--liquibase formatted sql
 

--changeSet OPER-4834-1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new types for forecast period and a table of forecast period
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('FORECASTPERIOD');
END;
/ 

--changeSet OPER-4834-1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE ForecastPeriod
AS
  OBJECT
  (
    in_StartDate DATE ,
    in_EndDate   DATE ,
    in_Rate      NUMBER ) NOT FINAL ;
  /

--changeSet OPER-4834-1:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('FORECASTPERIODS');
END;
/ 

--changeSet OPER-4834-1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE ForecastPeriods
IS
  TABLE OF ForecastPeriod ;
  /