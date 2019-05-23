--liquibase formatted sql


--changeSet DEV-482:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT '', 'sDeferralReferenceOperator', 'SESSION','Deferral Reference Operator parameter','USER', 'String', null, 0, 'Deferral Reference', '1012', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'sDeferralReferenceOperator' AND parm_type = 'SESSION');    

--changeSet DEV-482:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************************
   *  Function to check deferral reference is applicable
********************************************************/
CREATE OR REPLACE FUNCTION isDeferReferApplicable
(
  aFailDeferDbId      fail_defer_ref.fail_defer_ref_db_id%TYPE,
  aFailDeferId        fail_defer_ref.fail_defer_ref_id%TYPE,  
  aInvNoDbId          inv_inv.inv_no_db_id%TYPE,
  aInvNoId            inv_inv.inv_no_id%TYPE

) RETURN  number
IS
  ls_carriers VARCHAR2(4000);
  ls_check number;

   CURSOR lcur_Carriers (
         cn_FailDeferDbId         IN fail_defer_ref.fail_defer_ref_db_id%TYPE,
         cn_FailDeferId           IN fail_defer_ref.fail_defer_ref_id%TYPE,        
         cn_InvNoDbId             IN inv_inv.inv_no_db_id%TYPE,
         cn_InvNoId               IN inv_inv.inv_no_id%TYPE
      ) IS
      SELECT
        fail_defer_ref.defer_ref_sdesc
      FROM
         inv_inv,
         fail_defer_ref,
         inv_inv h_inv_inv,
         inv_inv assmbl_inv_inv
      WHERE
         fail_defer_ref.fail_defer_ref_db_id    = cn_FailDeferDbId AND
         fail_defer_ref.fail_defer_ref_id       = cn_FailDeferId 
         AND
         inv_inv.inv_no_db_id = cn_InvNoDbId    AND
         inv_inv.inv_no_id    = cn_InvNoId
         AND
         h_inv_inv.inv_no_db_id    = inv_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id       = inv_inv.h_inv_no_id    AND
         h_inv_inv.rstat_cd        =  0
         AND
         assmbl_inv_inv.inv_no_db_id    (+)= inv_inv.assmbl_inv_no_db_id AND
         assmbl_inv_inv.inv_no_id       (+)= inv_inv.assmbl_inv_no_id    AND
         assmbl_inv_inv.rstat_cd        (+)= 0
         AND
         (
             h_inv_inv.carrier_db_id IS NULL
            OR
            EXISTS
            ( SELECT
                 1
              FROM
                 fail_defer_carrier
              WHERE
                 fail_defer_carrier.carrier_db_id           =  h_inv_inv.carrier_db_id             AND
                 fail_defer_carrier.carrier_id              =  h_inv_inv.carrier_id                AND
                 fail_defer_carrier.fail_defer_ref_db_id    =  fail_defer_ref.fail_defer_ref_db_id AND
                 fail_defer_carrier.fail_defer_ref_id       =  fail_defer_ref.fail_defer_ref_id               
            )

         )
         ;
   lrec_Carriers lcur_Carriers%ROWTYPE;
BEGIN

   FOR lrec_Carriers IN lcur_Carriers (aFailDeferDbId,aFailDeferId,aInvNoDbId,aInvNoId) LOOP
       ls_carriers := ls_carriers || ', ' || lrec_Carriers.defer_ref_sdesc;
   END LOOP;

   IF ls_carriers IS NULL THEN
      ls_check := 0;
      ELSE
       ls_check := 1;
       END IF;


  RETURN ls_check;
END isDeferReferApplicable;
/