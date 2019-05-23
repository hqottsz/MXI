--liquibase formatted sql


--changeSet refreshReportView:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      Refresh_Report_VW
*
* Description:   This function calls the refreshReportViews function, which
                  will refresh materialized views.
*
* Orig.Coder:    Nicholas Bale
* Recent Coder:
* Recent Date:   July 20, 2017
*
*********************************************************************************/
create or replace procedure REFRESH_REPORT_VW
IS
BEGIN
   reportview_utl_pkg.refreshReportViews();
END REFRESH_REPORT_VW;

/
