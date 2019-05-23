--liquibase formatted sql


--changeSet update_actuals_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY UPDATE_ACTUALS_PKG
IS
/********************************************************************************
*
* Procedure:    UpdateInvCurrUsage
* Arguments:    none
* Return:       on_Return    (number) - success/failure of the procedure
*
* Description:  When you change the mim_part_numdata table in the baseline, you will
*           have to add the new datatypes to the inv_curr_usage table in the
*           actuals for unlocked inventories.
*           This function determines what rows need to be inserted, and then
*           inserts them
* Orig.Coder:   ???
* Recent Coder: ysotozaki
* Recent Date:  2007-02-13
*
*********************************************************************************
*
* Copyright ? 1998-2007 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateInvCurrUsage(
        an_Return            OUT NUMBER
   ) IS

   /* local variables */
   ln_FuncReturn     NUMBER;  /* return value from procedure */

   /* cursor declarations */
   CURSOR lcur_InvList IS
      SELECT inv_inv.inv_no_db_id,
             inv_inv.inv_no_id
        FROM inv_inv
       WHERE  inv_inv.locked_bool = 0	AND
              inv_inv.rstat_cd	  = 0;
   lrec_InvList lcur_InvList%ROWTYPE;

BEGIN

   /* retrieve a list of all inventory in the database */
   FOR lrec_InvList IN lcur_InvList LOOP

      -- initialize the usage parms for the inventory
      inv_create_pkg.InitializeCurrentUsage(
                        lrec_InvList.inv_no_db_id,
                        lrec_InvList.inv_no_id,
                        ln_FuncReturn);
      IF ln_FuncReturn < 0 THEN
         an_Return := ln_FuncReturn;
         RETURN;
      END IF;

   END LOOP;

   /* return success */
   an_Return := 1;

EXCEPTION
   WHEN OTHERS THEN
      an_Return := -1;
      application_object_pkg.SetMxiError('DEV-99999', SQLERRM);
END UpdateInvCurrUsage;

END UPDATE_ACTUALS_PKG;
/