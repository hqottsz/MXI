--liquibase formatted sql


--changeSet reportview_utl_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE REPORTVIEW_UTL_PKG IS
  PROCEDURE refreshReportViews;
end REPORTVIEW_UTL_PKG;

/
