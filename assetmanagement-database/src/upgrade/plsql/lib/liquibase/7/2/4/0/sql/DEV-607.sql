--liquibase formatted sql


--changeSet DEV-607:1 stripComments:false
-- Worker to update IETMs of Task
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, utl_id )
   SELECT 'UPDATE_IETM','com.mxi.mx.core.worker.ietm.UpdateIETMWorker','wm/Maintenix-UpdateIETMWorkManager',1, 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'UPDATE_IETM' );

--changeSet DEV-607:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION isIETMApplicable
(
    aInvClassCd inv_inv.inv_class_cd%TYPE,
    aInvApplCd  inv_inv.appl_eff_cd%TYPE,
    aHInvApplCd inv_inv.appl_eff_cd%TYPE,
    aAssmblInvNoDbId inv_inv.assmbl_inv_no_db_id%TYPE,
    aAssmblInvApplCd inv_inv.appl_eff_cd%TYPE,
    as_range STRING
)  RETURN NUMBER
IS
   ln_Applicable inv_inv.appl_eff_cd%TYPE;

BEGIN
   -- initialize variables
   ln_Applicable := 0;

  IF( (aInvClassCd = 'ACFT' ) OR (aInvClassCd = 'ASSY' ) ) THEN
     ln_Applicable := aInvApplCd;

  ELSE IF aAssmblInvNoDbId IS NOT NULL THEN
          ln_Applicable := aAssmblInvApplCd;

       ELSE
          ln_Applicable := aHInvApplCd;
       END IF;

  END IF;



   -- return the applicability
   RETURN isApplicable(as_range,ln_Applicable);

END isIETMApplicable;
/