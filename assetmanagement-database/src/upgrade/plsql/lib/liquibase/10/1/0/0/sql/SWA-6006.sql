--liquibase formatted sql

--changeSet SWA6006-SP4:1 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
SELECT 'MX_REFRESH_MAT_COMP_AD_VIEWS', 'Refresh AD Compliance Materialized Views', null, 0, 120, 0, 0 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_job WHERE job_cd = 'MX_REFRESH_MAT_COMP_AD_VIEWS');

--changeSet SWA6006-SP4:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE REPORTVIEW_UTL_PKG IS
  PROCEDURE refreshReportViews;
end REPORTVIEW_UTL_PKG;


--changeSet SWA6006-SP4:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY REPORTVIEW_UTL_PKG
IS
  PROCEDURE refreshReportViews IS
    PRAGMA AUTONOMOUS_TRANSACTION;
    CURSOR lcur_mviews
        IS
        SELECT
           mview_name
        FROM
           user_mviews
        WHERE
           mview_name LIKE 'COMP%'
        ORDER BY mview_name;
    BEGIN
        FOR lrec_mviews IN lcur_mviews LOOP
           DBMS_MVIEW.REFRESH(lrec_mviews.mview_name);
        END LOOP;
    END refreshReportViews;
END REPORTVIEW_UTL_PKG;

/

--changeSet SWA6006-SP4:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE REFRESH_REPORT_VW
IS
BEGIN
   reportview_utl_pkg.refreshReportViews();
END REFRESH_REPORT_VW;


/
