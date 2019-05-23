--liquibase formatted sql


--changeSet update_actuals_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE UPDATE_ACTUALS_PKG
IS

/* procedure used insert rows into inv_curr_usage if the
   baseline setup changes */
PROCEDURE UpdateInvCurrUsage(
        an_Return            OUT NUMBER
   );

END UPDATE_ACTUALS_PKG;
/