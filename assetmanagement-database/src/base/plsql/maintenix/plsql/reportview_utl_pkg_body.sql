--liquibase formatted sql


--changeSet reportview_utl_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
