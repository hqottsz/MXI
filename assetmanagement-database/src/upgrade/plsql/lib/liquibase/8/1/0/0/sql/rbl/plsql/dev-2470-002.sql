--liquibase formatted sql


--changeSet dev-2470-002:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_mview_utility_pkg IS

/******************************************************************************
*
*  Procedure:    opr_refresh_mview
*  Arguments:
*  Return:
*  Description:  Procedure for refreshing operator materialized view
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  14-Feb-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_refresh_mview AS

   -- materialized view cursor
   CURSOR lcur_mview IS
   SELECT
      mview_name
   FROM 
      user_mviews
   WHERE
      mview_name LIKE 'ACOR\_%' ESCAPE '\';

BEGIN

   --# refresh
   FOR lrec_mview IN lcur_mview LOOP
     
     BEGIN

        DBMS_MVIEW.refresh(lrec_mview.mview_name,'C');

     EXCEPTION
        WHEN OTHERS THEN
           DBMS_OUTPUT.PUT_LINE(SQLERRM);
     END;

   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
      RAISE;

END opr_refresh_mview;


END opr_mview_utility_pkg;
/