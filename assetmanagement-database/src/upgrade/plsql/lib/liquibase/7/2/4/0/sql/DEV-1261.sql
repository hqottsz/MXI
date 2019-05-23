--liquibase formatted sql


--changeSet DEV-1261:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP( 'T_VENDOR_PRICE');
END;
/

--changeSet DEV-1261:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP( 'T_TAB_VENDOR_PRICE');
END;
/

--changeSet DEV-1261:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_VENDOR_PRICE as object
(
  -- Author  : SLEVERT
  -- Created : 10/17/2011 11:41:15 AM
  -- Purpose : Table to Store Vendor Prices
  
  -- Attributes
part_vendor_price_db_id NUMBER(10),
part_vendor_price_id NUMBER(10),
part_no_db_id NUMBER(10),
part_no_id NUMBER(10),
price_type_db_id NUMBER(10),
price_type_cd VARCHAR2(8),
qty_unit_db_id NUMBER(10),
qty_unit_cd VARCHAR2(8),
unit_price FLOAT,
lead_time FLOAT,
effective_to_dt DATE,
vendor_note VARCHAR2(4000),
min_order_qty FLOAT,
discount_pct FLOAT,
contract_ref_sdesc VARCHAR2(80)
);
/

--changeSet DEV-1261:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_TAB_VENDOR_PRICE as TABLE of T_VENDOR_PRICE;
/

--changeSet DEV-1261:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE PART_PRICE_PKG IS
   ----------------------------------------------------------------------------
   -- Object Name : PART_PRICE_PKG
   -- Object Type : Package Header
   -- Date        : Oct 10, 2011
   -- Coder       : yvakulenko
   -- Recent Date :
   -- Recent Coder:
   -- Description :
   -- This package contains methods/functions for Part Price Management
   ----------------------------------------------------------------------------
   -- Copyright © 2010-2011 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
/********************************************************************************
*
* Function: getLowestPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*
*
*
* Description: Returns the lowest price for a vendor and part combination,
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
function getLowestPartPrice( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                             aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                             aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                             aPartId     eqp_part_vendor_price.part_no_id%TYPE,
                             aQuantity   eqp_part_vendor_price.min_order_qt%TYPE)
       return T_TAB_VENDOR_PRICE;

/********************************************************************************
*
* Function: getVolumePartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE
*
*
* Description: Returns the lowest price for a vendor and part combination with
*              a specified quantity and is not a contract.
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getVolumePartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE,
                            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE )
  return T_VENDOR_PRICE;

/********************************************************************************
*
* Function: getVolumeContractPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE
*
*
* Description: Returns the lowest price for a vendor and part combination with
*              a specified quantity and is a contract.
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getVolumeContractPartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE,
                            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE )
  return T_VENDOR_PRICE;

/********************************************************************************
*
* Procedure: validatePartPrice
* Arguments:
*            as_UUID  (STRING) -- Transaction's unique ID
*
*
* Description: Validates the part prices, previously saved into PART_PRICE_SP
*              temporary table under given transaction ID
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Oct 10, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE validatePartPrices(
             as_UUID  IN STRING,
             aReturn  OUT STRING
         );

/********************************************************************************
*
* Procedure: saveValidPartPrices
* Arguments:
*            as_UUID  (STRING) -- Transaction's unique ID
*
*
* Description: Saves all valid part prices from PART_PRICE_SP into
*              EQP_PART_VENDOR_PRICE. 'Valid part price' means the part
*              price's record has all validation flags (see PART_PRICE_SP table)
*              set to 0.
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Oct 10, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE saveValidPartPrices (
             as_UUID  STRING
);

/********************************************************************************
*
* Procedure: hasIntersectingContract
* Arguments:
*      aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*      aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*      aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*      aPartId     eqp_part_vendor_price.part_no_id%TYPE
*      aEffectiveFrom DATE - candidate contract effective from date, may not be null
*      aEffectiveTo DATE - candidate contract effective to date, may be null
*
*
* Description: Returns a contract_ref_desc if the provided contract effective 
*              dates overlaps with an existing contracts effective dates, 
*              otherwise null.
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Nov 11, 2011
*
*********************************************************************************/
FUNCTION hasIntersectingContract(
      aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
      aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
      aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
      aPartId     eqp_part_vendor_price.part_no_id%TYPE,
      aEffectiveFrom DATE,
      aEffectiveTo DATE
      )
      RETURN STRING;

/********************************************************************************
*
* Procedure: isContractPriceUnique
* Arguments:
*      aPartNoOEM (eqp_part_no.part_no_oem%TYPE) -- Part number OEM code
*      aManufactCd (eqp_part_no.manufact_cd%TYPE)-- Manufacturer code
*      aVendorCd (org_vendor.vendor_cd%TYPE)     -- Vendoe code
*      aEffectiveFromDt (Date)                   -- Effective from date
*      aEffectiveToDt (Date)                   -- Effective to date
*
*
* Description: Returns 1 if given part of given manufacturer from given vendor has
               contract price on given time frame. 0 - otherwise.
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Nov 07, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isContractPriceUnique(
      aPartNoOEM eqp_part_no.part_no_oem%TYPE,
      aManufactCd eqp_part_no.manufact_cd%TYPE,
      aVendorCd org_vendor.vendor_cd%TYPE,
      aEffectiveFromDt eqp_part_vendor_price.effective_from_dt%TYPE,
      aEffectiveToDt eqp_part_vendor_price.effective_to_dt%TYPE
      )
   RETURN NUMBER;

/********************************************************************************
*
* Procedure: isOverlappingAllowed
*
*
* Description: Returns TRUE if and only if the configuration parameter is set and 
*              has TRUE as value. Otherwise returns FALSE.
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Nov 09, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isOverlappingAllowed
   RETURN BOOLEAN;
   
/********************************************************************************
*
* Procedure: hasIntersectingContract
* Arguments:
*     aPartPriceDbId   eqp_part_vendor_price.part_vendor_price_db_id%TYPE,
*     aPartPriceId     eqp_part_vendor_price.part_vendor_price_id%TYPE, 
*     aEffectiveFrom DATE - candidate contract effective from date, may not be null
*     aEffectiveTo DATE - candidate contract effective to date, may be null
*
*
* Description: Returns a contract_ref_desc if the provided contract effective 
*              dates overlaps with an existing contracts effective dates, 
*              otherwise null.
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Nov 11, 2011
*
*********************************************************************************/
FUNCTION hasIntersectingContract(
      aPartPriceDbId   eqp_part_vendor_price.part_vendor_price_db_id%TYPE,
      aPartPriceId     eqp_part_vendor_price.part_vendor_price_id%TYPE, 
      aEffectiveFrom DATE,
      aEffectiveTo DATE
      )
      RETURN STRING;

/********************************************************************************
*
* FUNCTION: Determines if the effective dates overlap.  
*           If true, returns the contract.
*           If false, return null.
* Arguments:
*           aCandidateFrom DATE - The effective 'from' date of an existing price
*           aCandidateTo DATE - The effective 'to' date of an existing price
*           aContract STRING - The contract ref sdesc of an existing price
*           aEffectiveFrom DATE - The effective 'from' date of an candidate price
*           aEffectiveTo DATE - The effective 'to' date of an candidate price
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Nov 10, 2011
*
*********************************************************************************/
FUNCTION getConflictingContract( aExistingEffectiveFrom DATE, 
                                 aExistingEffectiveTo DATE, 
                                 aContract STRING, 
                                 aCandidateEffectiveFrom DATE, 
                                 aCandidateEffectiveTo DATE 
                               )
RETURN STRING;

/********************************************************************************
*
* Function: getContractPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*
*
*
* Description: Returns the lowest price for a vendor and part combination 
*              that has a contract.
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getContractPartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE )
  RETURN T_VENDOR_PRICE;

/********************************************************************************
*
* Function: getPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*
*
*
* Description: Returns the lowest price for a vendor and part combination 
*              that does not have a contract.
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Nov 11, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getPartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE )
RETURN T_VENDOR_PRICE;


END PART_PRICE_PKG;
/

--changeSet DEV-1261:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PART_PRICE_PKG IS

MAX_DATE CONSTANT DATE := TO_DATE('31.12.9999 23:59:59', 'dd.mm.yyyy hh24:mi:ss');

CHANGE_CODE_NEW CONSTANT STRING(1) := 'N';
CHANGE_CODE_REVISION CONSTANT STRING(1) := 'R';
CHANGE_CODE_DELETE CONSTANT STRING(1) := 'D';

SUMMARY_CODE_SUCCESS CONSTANT STRING(7) := 'SUCCESS';
SUMMARY_CODE_PARTIAL CONSTANT STRING(7) := 'PARTIAL';
SUMMARY_CODE_ERROR CONSTANT STRING(5) := 'ERROR';

REJECTED CONSTANT NUMBER := 0;
PASSED CONSTANT NUMBER := 1;

CONFIG_PARM CONSTANT STRING(45) := 'ACTION_ALLOW_OVERLAPPING_PART_PRICE_CONTRACTS';

/********************************************************************************
*
* Procedure: validatePartPrice
* Arguments:
*            as_UUID  (STRING) -- Transaction's unique ID
*
*
* Description: Validates the part prices, previously saved into PART_PRICE_SP
*              temporary table under given transaction ID
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Oct 10, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE validatePartPrices
(
   as_UUID   IN STRING,
   aReturn   OUT STRING
)IS

   ln_Total NUMBER;
   ln_Rejected NUMBER;
BEGIN
   UPDATE part_price_sp
   SET part_price_sp.vendor_exists = PASSED
   WHERE part_price_sp.uuid = as_UUID
   AND EXISTS (SELECT 1 FROM org_vendor WHERE org_vendor.vendor_cd = part_price_sp.vendor_cd);

   UPDATE part_price_sp
   SET part_price_sp.manufact_exists = PASSED
   WHERE part_price_sp.uuid = as_UUID
   AND EXISTS (SELECT 1 FROM eqp_manufact WHERE eqp_manufact.manufact_cd = part_price_sp.manufact_cd);

   UPDATE part_price_sp
   SET part_price_sp.part_no_exists = PASSED
   WHERE part_price_sp.uuid = as_UUID
   AND EXISTS ( SELECT 1 FROM eqp_part_no WHERE eqp_part_no.part_no_oem = part_price_sp.part_no_oem );

   UPDATE part_price_sp
   SET part_price_sp.qty_unit_exists = PASSED
   WHERE part_price_sp.uuid = as_UUID
   AND EXISTS (SELECT 1 FROM ref_qty_unit WHERE ref_qty_unit.qty_unit_cd = part_price_sp.qty_unit_cd);

   UPDATE part_price_sp
   SET part_price_sp.currency_exists = PASSED
   WHERE part_price_sp.uuid = as_UUID
   AND (
      part_price_sp.change_cd <> CHANGE_CODE_DELETE
      OR
      EXISTS (SELECT 1 FROM ref_currency WHERE ref_currency.currency_cd = part_price_sp.currency_cd)
   );

   UPDATE part_price_sp
   SET part_price_sp.manufact_applies = PASSED
   WHERE part_price_sp.uuid = as_UUID
      AND
   EXISTS (SELECT 1 FROM eqp_part_no WHERE eqp_part_no.part_no_oem = part_price_sp.part_no_oem AND eqp_part_no.manufact_cd = part_price_sp.manufact_cd);

   UPDATE part_price_sp
   SET part_price_sp.qty_unit_applies = PASSED
   WHERE part_price_sp.uuid = as_UUID
      AND
   EXISTS (
      SELECT
         eqp_part_no.qty_unit_db_id,
         eqp_part_no.qty_unit_cd
      FROM
         eqp_part_no
      WHERE
         eqp_part_no.part_no_oem = part_price_sp.part_no_oem
         AND
         eqp_part_no.manufact_cd = part_price_sp.manufact_cd
         AND
         eqp_part_no.qty_unit_cd = part_price_sp.qty_unit_cd

      UNION

      SELECT
         eqp_part_alt_unit.qty_unit_db_id,
         eqp_part_alt_unit.qty_unit_cd
      FROM
         eqp_part_no
         JOIN eqp_part_alt_unit ON eqp_part_no.part_no_db_id = eqp_part_alt_unit.part_no_db_id AND
                                   eqp_part_no.part_no_id = eqp_part_alt_unit.part_no_id
      WHERE
         eqp_part_no.part_no_oem = part_price_sp.part_no_oem
         AND
         eqp_part_no.manufact_cd = part_price_sp.manufact_cd
   );

   UPDATE part_price_sp
   SET part_price_sp.prt_vd_applies = PASSED
   WHERE part_price_sp.uuid = as_UUID
   AND EXISTS (
      SELECT
         1
      FROM
         eqp_part_vendor
      JOIN eqp_part_no ON eqp_part_no.part_no_db_id = eqp_part_vendor.part_no_db_id AND
                          eqp_part_no.part_no_id = eqp_part_vendor.part_no_id
      JOIN org_vendor ON org_vendor.vendor_db_id = eqp_part_vendor.vendor_db_id AND
                         org_vendor.vendor_id = eqp_part_vendor.vendor_id
      WHERE
         eqp_part_no.part_no_oem = part_price_sp.part_no_oem
         AND
         eqp_part_no.manufact_cd = part_price_sp.manufact_cd
         AND
         org_vendor.vendor_cd = part_price_sp.vendor_cd
      );

   IF isOverlappingAllowed THEN
      UPDATE part_price_sp
      SET part_price_sp.cnr_uniq_applies = PASSED
      WHERE part_price_sp.uuid = as_UUID;
   ELSE
      UPDATE part_price_sp
      SET part_price_sp.cnr_uniq_applies = 
             CASE WHEN 
                part_price_sp.change_cd = CHANGE_CODE_DELETE
             THEN 
                PASSED 
             ELSE 
                CASE WHEN 
                   EXISTS (
                      SELECT 
                         eqp_part_vendor_price.*
                      FROM 
                         eqp_part_vendor_price
                         INNER JOIN org_vendor ON org_vendor.vendor_db_id = eqp_part_vendor_price.vendor_db_id AND
                                                  org_vendor.vendor_id = eqp_part_vendor_price.vendor_id
                         INNER JOIN eqp_part_no ON eqp_part_no.part_no_db_id = eqp_part_vendor_price.part_no_db_id AND
                                                   eqp_part_no.part_no_id = eqp_part_vendor_price.part_no_id
                         INNER JOIN ref_qty_unit ON ref_qty_unit.qty_unit_db_id = eqp_part_vendor_price.qty_unit_db_id AND
                                                    ref_qty_unit.qty_unit_cd = eqp_part_vendor_price.qty_unit_cd
                      WHERE 
                         org_vendor.vendor_cd = part_price_sp.vendor_cd
                         AND
                         eqp_part_no.part_no_oem = part_price_sp.part_no_oem
                         AND
                         eqp_part_no.manufact_cd = part_price_sp.manufact_cd
                         AND
                         eqp_part_vendor_price.effective_from_dt = part_price_sp.effective_from_dt
                         AND
                         eqp_part_vendor_price.min_order_qt = part_price_sp.min_order_qt
                         AND
                         ref_qty_unit.qty_unit_cd = part_price_sp.qty_unit_cd
                         AND
                         eqp_part_vendor_price.hist_bool = 0
                   )
                THEN
                   PASSED
                ELSE
                   isContractPriceUnique(part_price_sp.part_no_oem,
                                         part_price_sp.manufact_cd,
                                         part_price_sp.vendor_cd,
                                         part_price_sp.effective_from_dt,
                                         part_price_sp.effective_to_dt
                                         )
                END
             END
      WHERE 
         part_price_sp.uuid = as_UUID;
   END IF;

   SELECT
     COUNT(1) as total_rows,
     SUM(
        CASE WHEN t.vendor_exists = REJECTED OR
                  t.manufact_exists = REJECTED OR
                  t.part_no_exists = REJECTED OR
                  t.qty_unit_exists = REJECTED OR
                  t.currency_exists = REJECTED OR
                  t.qty_unit_applies = REJECTED OR
                  t.manufact_applies = REJECTED OR
                  t.prt_vd_applies = REJECTED OR
                  t.cnr_uniq_applies = REJECTED
        THEN 1 ELSE 0 END) AS rejected
   INTO
      ln_total, ln_rejected
   FROM
      part_price_sp t
   WHERE
      uuid = as_UUID;

   aReturn := SUMMARY_CODE_SUCCESS;

   IF ln_Rejected > 0 THEN

      IF ln_Rejected < ln_Total THEN
         aReturn := SUMMARY_CODE_PARTIAL;
      ELSE
         aReturn := SUMMARY_CODE_ERROR;
      END IF;

   END IF;
END validatePartPrices;

/********************************************************************************
*
* Function: getLowestPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE
*
*
*
* Description: Returns the lowesrt price for a vendor and part combination,
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
function getLowestPartPrice(aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE,
                            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE )
  RETURN T_TAB_VENDOR_PRICE IS

    lLowestPrice T_VENDOR_PRICE;

  BEGIN

  IF aQuantity IS NULL THEN
    lLowestPrice := getContractPartPrice( aVendorDbId, aVendorId, aPartDbId, aPartId );
    IF lLowestPrice IS NOT NULL THEN
       RETURN T_TAB_VENDOR_PRICE ( lLowestPrice );    
    ELSE
        lLowestPrice := getPartPrice( aVendorDbId, aVendorId, aPartDbId, aPartId );
        IF lLowestPrice IS NOT NULL THEN
           RETURN T_TAB_VENDOR_PRICE ( lLowestPrice );    
        END IF;
    END IF;
  ELSE
    lLowestPrice := getVolumeContractPartPrice( aVendorDbId, aVendorId, aPartDbId, aPartId, aQuantity );
    IF lLowestPrice IS NOT NULL THEN
       RETURN T_TAB_VENDOR_PRICE ( lLowestPrice );    
    ELSE
       lLowestPrice := getVolumePartPrice( aVendorDbId, aVendorId, aPartDbId, aPartId, aQuantity );
       IF lLowestPrice IS NOT NULL THEN
          RETURN T_TAB_VENDOR_PRICE ( lLowestPrice );
       END IF;
    END IF;
  END IF;

  RETURN NULL;

END getLowestPartPrice;

/********************************************************************************
*
* Function: getVolumePartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE
*
* Description: Returns the lowesrt price for a vendor and part combination,
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getVolumePartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE,
                            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE )
  return T_VENDOR_PRICE is

  lRecord T_VENDOR_PRICE;

BEGIN
  SELECT
     T_VENDOR_PRICE(
       min_price.part_vendor_price_db_id,
       min_price.part_vendor_price_id,
       min_price.part_no_db_id,
       min_price.part_no_id,
       min_price.price_type_db_id,
       min_price.price_type_cd,
       min_price.qty_unit_db_id,
       min_price.qty_unit_cd,
       min_price.unit_price,
       min_price.lead_time,
       min_price.effective_to_dt,
       min_price.vendor_note,
       min_price.min_order_qt,
       min_price.discount_pct,
       min_price.contract_ref_sdesc
     )
 INTO lRecord
 FROM
  ( SELECT MIN( price.standard_unit_price )
           OVER (
                  PARTITION BY
                    price.part_no_db_id,
                    price.part_no_id,
                    price.vendor_db_id,
                    price.vendor_id
           ) AS min_standard_unit_price,
           price.standard_unit_price,
           price.part_no_db_id,
           price.part_no_id,
           price.vendor_db_id,
           price.vendor_id,
           price.part_vendor_price_db_id,
           price.part_vendor_price_id,
           price.price_type_db_id,
           price.price_type_cd,
           price.qty_unit_db_id,
           price.qty_unit_cd,
           price.unit_price,
           price.lead_time,
           price.effective_to_dt,
           price.vendor_note,
           price.min_order_qt,
           price.discount_pct,
           price.contract_ref_sdesc
  FROM
  (
    SELECT
      CASE
          WHEN vendor_price.qty_unit_cd = alternate_unit.qty_unit_cd THEN
              -- the vendor unit cd matches the alternate unit cd so divide by the alternate units qty
              ( vendor_price.unit_price / currency.exchg_qt ) / alternate_unit.qty_convert_qt
          ELSE
              -- using the standard unit of measure so not unit conversion required
              vendor_price.unit_price / currency.exchg_qt
      END as standard_unit_price,
       vendor_price.part_no_db_id,
       vendor_price.part_no_id,
       vendor_price.vendor_db_id,
       vendor_price.vendor_id,
       vendor_price.part_vendor_price_db_id,
       vendor_price.part_vendor_price_id,
       vendor_price.price_type_db_id,
       vendor_price.price_type_cd,
       vendor_price.qty_unit_db_id,
       vendor_price.qty_unit_cd,
       vendor_price.unit_price,
       vendor_price.lead_time,
       vendor_price.effective_to_dt,
       vendor_price.vendor_note,
       vendor_price.min_order_qt,
       vendor_price.discount_pct,
       vendor_price.contract_ref_sdesc
    FROM
       eqp_part_vendor_price vendor_price
    RIGHT JOIN
       ref_currency currency
       ON (
         vendor_price.currency_db_id = currency.currency_db_id AND
         vendor_price.currency_cd    = currency.currency_cd
       )
    LEFT JOIN
       eqp_part_alt_unit alternate_unit
       ON (
         vendor_price.part_no_db_id = alternate_unit.part_no_db_id AND
         vendor_price.part_no_id = alternate_unit.part_no_id
       )
   WHERE
       vendor_price.hist_bool = 0 AND
       vendor_price.vendor_db_id = aVendorDbId AND
       vendor_price.vendor_id = aVendorId AND
       vendor_price.part_no_db_id = aPartDbId AND
       vendor_price.part_no_id = aPartId AND
       ( vendor_price.effective_to_dt IS NULL OR vendor_price.effective_to_dt > SYSDATE) AND
       vendor_price.effective_from_dt <= SYSDATE AND
       vendor_price.contract_ref_sdesc IS NULL AND
       vendor_price.min_order_qt <= aQuantity
  ) price
) min_price
WHERE
  min_price.min_standard_unit_price = standard_unit_price;

  dbms_output.put_line( 'LowestVolumePrice: '||lRecord.part_vendor_price_db_id||lRecord.part_vendor_price_id);

return lRecord;

  EXCEPTION
  WHEN NO_DATA_FOUND THEN
  return null;


END getVolumePartPrice;

/********************************************************************************
*
* Function: getVolumeContractPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE 
*
* Description: Returns the lowest price for a vendor and part combination,
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getVolumeContractPartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE,
                            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE )
  RETURN T_VENDOR_PRICE is

  lRecord T_VENDOR_PRICE;

BEGIN
  SELECT
     T_VENDOR_PRICE(
       min_price.part_vendor_price_db_id,
       min_price.part_vendor_price_id,
       min_price.part_no_db_id,
       min_price.part_no_id,
       min_price.price_type_db_id,
       min_price.price_type_cd,
       min_price.qty_unit_db_id,
       min_price.qty_unit_cd,
       min_price.unit_price,
       min_price.lead_time,
       min_price.effective_to_dt,
       min_price.vendor_note,
       min_price.min_order_qt,
       min_price.discount_pct,
       min_price.contract_ref_sdesc
     )
 INTO lRecord
 FROM
  ( SELECT MIN( price.standard_unit_price )
           OVER (
                  PARTITION BY
                    price.part_no_db_id,
                    price.part_no_id,
                    price.vendor_db_id,
                    price.vendor_id
           ) AS min_standard_unit_price,
           price.standard_unit_price,
           price.part_no_db_id,
           price.part_no_id,
           price.vendor_db_id,
           price.vendor_id,
           price.part_vendor_price_db_id,
           price.part_vendor_price_id,
           price.price_type_db_id,
           price.price_type_cd,
           price.qty_unit_db_id,
           price.qty_unit_cd,
           price.unit_price,
           price.lead_time,
           price.effective_to_dt,
           price.vendor_note,
           price.min_order_qt,
           price.discount_pct,
           price.contract_ref_sdesc
  FROM
  (
    SELECT
      CASE
          WHEN vendor_price.qty_unit_cd = alternate_unit.qty_unit_cd THEN
              -- the vendor unit cd matches the alternate unit cd so divide by the alternate units qty
              ( vendor_price.unit_price / currency.exchg_qt ) / alternate_unit.qty_convert_qt
          ELSE
              -- using the standard unit of measure so not unit conversion required
              vendor_price.unit_price / currency.exchg_qt
      END as standard_unit_price,
       vendor_price.part_no_db_id,
       vendor_price.part_no_id,
       vendor_price.vendor_db_id,
       vendor_price.vendor_id,
       vendor_price.part_vendor_price_db_id,
       vendor_price.part_vendor_price_id,
       vendor_price.price_type_db_id,
       vendor_price.price_type_cd,
       vendor_price.qty_unit_db_id,
       vendor_price.qty_unit_cd,
       vendor_price.unit_price,
       vendor_price.lead_time,
       vendor_price.effective_to_dt,
       vendor_price.vendor_note,
       vendor_price.min_order_qt,
       vendor_price.discount_pct,
       vendor_price.contract_ref_sdesc
    FROM
       eqp_part_vendor_price vendor_price
    RIGHT JOIN
       ref_currency currency
       ON (
         vendor_price.currency_db_id = currency.currency_db_id AND
         vendor_price.currency_cd    = currency.currency_cd
       )
    LEFT JOIN
       eqp_part_alt_unit alternate_unit
       ON (
         vendor_price.part_no_db_id = alternate_unit.part_no_db_id AND
         vendor_price.part_no_id = alternate_unit.part_no_id
       )
   WHERE
       vendor_price.hist_bool = 0 AND
       vendor_price.vendor_db_id = aVendorDbId AND
       vendor_price.vendor_id = aVendorId AND
       vendor_price.part_no_db_id = aPartDbId AND
       vendor_price.part_no_id = aPartId AND
       ( vendor_price.effective_to_dt IS NULL OR vendor_price.effective_to_dt > SYSDATE) AND
       vendor_price.effective_from_dt <= SYSDATE AND
       vendor_price.contract_ref_sdesc IS NOT NULL AND
       vendor_price.min_order_qt <= aQuantity
  ) price
) min_price
WHERE
  min_price.min_standard_unit_price = standard_unit_price;

  dbms_output.put_line( 'LowestVolumeContractPrice: '||lRecord.part_vendor_price_db_id||lRecord.part_vendor_price_id);

return lRecord;

  EXCEPTION
  WHEN NO_DATA_FOUND THEN
  return null;


END getVolumeContractPartPrice;

/********************************************************************************
*
* Function: getContractPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*
* Description: Returns the lowest price for a vendor and part combination 
*              that has a contract.
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getContractPartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                            aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                            aPartId     eqp_part_vendor_price.part_no_id%TYPE )
  RETURN T_VENDOR_PRICE is

  lRecord T_VENDOR_PRICE;

BEGIN
  SELECT
     T_VENDOR_PRICE(
       min_price.part_vendor_price_db_id,
       min_price.part_vendor_price_id,
       min_price.part_no_db_id,
       min_price.part_no_id,
       min_price.price_type_db_id,
       min_price.price_type_cd,
       min_price.qty_unit_db_id,
       min_price.qty_unit_cd,
       min_price.unit_price,
       min_price.lead_time,
       min_price.effective_to_dt,
       min_price.vendor_note,
       min_price.min_order_qt,
       min_price.discount_pct,
       min_price.contract_ref_sdesc
     )
 INTO lRecord
 FROM
  ( SELECT MIN( price.standard_unit_price )
           OVER (
                  PARTITION BY
                    price.part_no_db_id,
                    price.part_no_id,
                    price.vendor_db_id,
                    price.vendor_id
           ) AS min_standard_unit_price,
           price.standard_unit_price,
           price.part_no_db_id,
           price.part_no_id,
           price.vendor_db_id,
           price.vendor_id,
           price.part_vendor_price_db_id,
           price.part_vendor_price_id,
           price.price_type_db_id,
           price.price_type_cd,
           price.qty_unit_db_id,
           price.qty_unit_cd,
           price.unit_price,
           price.lead_time,
           price.effective_to_dt,
           price.vendor_note,
           price.min_order_qt,
           price.discount_pct,
           price.contract_ref_sdesc
  FROM
  (
    SELECT
      CASE
          WHEN vendor_price.qty_unit_cd = alternate_unit.qty_unit_cd THEN
              -- the vendor unit cd matches the alternate unit cd so divide by the alternate units qty
              ( vendor_price.unit_price / currency.exchg_qt ) / alternate_unit.qty_convert_qt
          ELSE
              -- using the standard unit of measure so not unit conversion required
              vendor_price.unit_price / currency.exchg_qt
      END as standard_unit_price,
       vendor_price.part_no_db_id,
       vendor_price.part_no_id,
       vendor_price.vendor_db_id,
       vendor_price.vendor_id,
       vendor_price.part_vendor_price_db_id,
       vendor_price.part_vendor_price_id,
       vendor_price.price_type_db_id,
       vendor_price.price_type_cd,
       vendor_price.qty_unit_db_id,
       vendor_price.qty_unit_cd,
       vendor_price.unit_price,
       vendor_price.lead_time,
       vendor_price.effective_to_dt,
       vendor_price.vendor_note,
       vendor_price.min_order_qt,
       vendor_price.discount_pct,
       vendor_price.contract_ref_sdesc
    FROM
       eqp_part_vendor_price vendor_price
    RIGHT JOIN
       ref_currency currency
       ON (
         vendor_price.currency_db_id = currency.currency_db_id AND
         vendor_price.currency_cd    = currency.currency_cd
       )
    LEFT JOIN
       eqp_part_alt_unit alternate_unit
       ON (
         vendor_price.part_no_db_id = alternate_unit.part_no_db_id AND
         vendor_price.part_no_id = alternate_unit.part_no_id
       )
   WHERE
       vendor_price.hist_bool = 0 AND
       vendor_price.vendor_db_id = aVendorDbId AND
       vendor_price.vendor_id = aVendorId AND
       vendor_price.part_no_db_id = aPartDbId AND
       vendor_price.part_no_id = aPartId AND
       ( vendor_price.effective_to_dt IS NULL OR vendor_price.effective_to_dt > SYSDATE) AND
       vendor_price.effective_from_dt <= SYSDATE AND
       vendor_price.contract_ref_sdesc IS NOT NULL
  ) price
) min_price
WHERE
  min_price.min_standard_unit_price = standard_unit_price;

  dbms_output.put_line( 'LowestContractPrice: '||lRecord.part_vendor_price_db_id||lRecord.part_vendor_price_id);

return lRecord;

  EXCEPTION
  WHEN NO_DATA_FOUND THEN
  return null;


END getContractPartPrice;

/********************************************************************************
*
* Function: getPartPrice
* Arguments: aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*            aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*            aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*            aPartId     eqp_part_vendor_price.part_no_id%TYPE
*
* Description: Returns the lowest price for a vendor and part combination,
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Oct 19, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getPartPrice ( aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
                        aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
                        aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
                        aPartId     eqp_part_vendor_price.part_no_id%TYPE )
  RETURN T_VENDOR_PRICE IS

  lRecord T_VENDOR_PRICE;

BEGIN
  SELECT
     T_VENDOR_PRICE(
       min_price.part_vendor_price_db_id,
       min_price.part_vendor_price_id,
       min_price.part_no_db_id,
       min_price.part_no_id,
       min_price.price_type_db_id,
       min_price.price_type_cd,
       min_price.qty_unit_db_id,
       min_price.qty_unit_cd,
       min_price.unit_price,
       min_price.lead_time,
       min_price.effective_to_dt,
       min_price.vendor_note,
       min_price.min_order_qt,
       min_price.discount_pct,
       min_price.contract_ref_sdesc
     )
 INTO lRecord
 FROM
  ( SELECT MIN( price.standard_unit_price )
           OVER (
                  PARTITION BY
                    price.part_no_db_id,
                    price.part_no_id,
                    price.vendor_db_id,
                    price.vendor_id
           ) AS min_standard_unit_price,
           price.standard_unit_price,
           price.part_no_db_id,
           price.part_no_id,
           price.vendor_db_id,
           price.vendor_id,
           price.part_vendor_price_db_id,
           price.part_vendor_price_id,
           price.price_type_db_id,
           price.price_type_cd,
           price.qty_unit_db_id,
           price.qty_unit_cd,
           price.unit_price,
           price.lead_time,
           price.effective_to_dt,
           price.vendor_note,
           price.min_order_qt,
           price.discount_pct,
           price.contract_ref_sdesc
  FROM
  (
    SELECT
      CASE
          WHEN vendor_price.qty_unit_cd = alternate_unit.qty_unit_cd THEN
              -- the vendor unit cd matches the alternate unit cd so divide by the alternate units qty
              ( vendor_price.unit_price / currency.exchg_qt ) / alternate_unit.qty_convert_qt
          ELSE
              -- using the standard unit of measure so not unit conversion required
              vendor_price.unit_price / currency.exchg_qt
      END as standard_unit_price,
       vendor_price.part_no_db_id,
       vendor_price.part_no_id,
       vendor_price.vendor_db_id,
       vendor_price.vendor_id,
       vendor_price.part_vendor_price_db_id,
       vendor_price.part_vendor_price_id,
       vendor_price.price_type_db_id,
       vendor_price.price_type_cd,
       vendor_price.qty_unit_db_id,
       vendor_price.qty_unit_cd,
       vendor_price.unit_price,
       vendor_price.lead_time,
       vendor_price.effective_to_dt,
       vendor_price.vendor_note,
       vendor_price.min_order_qt,
       vendor_price.discount_pct,
       vendor_price.contract_ref_sdesc
    FROM
       eqp_part_vendor_price vendor_price
    RIGHT JOIN
       ref_currency currency
       ON (
         vendor_price.currency_db_id = currency.currency_db_id AND
         vendor_price.currency_cd    = currency.currency_cd
       )
    LEFT JOIN
       eqp_part_alt_unit alternate_unit
       ON (
         vendor_price.part_no_db_id = alternate_unit.part_no_db_id AND
         vendor_price.part_no_id = alternate_unit.part_no_id
       )
   WHERE
       vendor_price.hist_bool = 0 AND
       vendor_price.vendor_db_id = aVendorDbId AND
       vendor_price.vendor_id = aVendorId AND
       vendor_price.part_no_db_id = aPartDbId AND
       vendor_price.part_no_id = aPartId AND
       ( vendor_price.effective_to_dt IS NULL OR vendor_price.effective_to_dt > SYSDATE) AND
       vendor_price.effective_from_dt <= SYSDATE AND
       vendor_price.contract_ref_sdesc IS NULL
  ) price
) min_price
WHERE
  ROWNUM = 1
  AND
  min_price.min_standard_unit_price = standard_unit_price;

  DBMS_OUTPUT.PUT_LINE( 'LowestContractPrice: '||lRecord.part_vendor_price_db_id||lRecord.part_vendor_price_id);

RETURN lRecord;

  EXCEPTION
  WHEN NO_DATA_FOUND THEN
  RETURN null;

END getPartPrice;

/********************************************************************************
*
* Procedure: saveValidPartPrices
* Arguments:
*            as_UUID  (STRING) -- Transaction's unique ID
*
*
* Description: Saves all valid part prices from PART_PRICE_SP into
*              EQP_PART_VENDOR_PRICE. 'Valid part price' means the part
*              price's record has all validation flags (see PART_PRICE_SP table)
*              set to 0.
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Oct 10, 2011
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE saveValidPartPrices (
             as_UUID  STRING
) IS

BEGIN
   MERGE INTO eqp_part_vendor_price t USING (
      SELECT
         vr.vendor_db_id,     -- Vendor (SPL)
         vr.vendor_id,
         sp.vendor_cd,
         sp.change_cd,        -- Change code (CHG)
         pn.part_no_db_id,    -- Part number (PNR)
         pn.part_no_id,
         sp.part_no_oem,
         sp.unit_price,       -- Unit price (UNP)
         un.qty_unit_db_id,   -- Unit of measure (UNT)
         sp.qty_unit_cd,
         cr.currency_db_id,   -- Currency (ICR)
         sp.currency_cd,
         sp.effective_from_dt,-- Price effective date (PED)
         sp.effective_to_dt,  -- Price held firm date (PFD)
         sp.lead_time,        -- Lead time (LTM)
         pt.price_type_db_id, -- Price type (PTP)
         sp.price_type_cd,
         sp.discount_pct,
         sp.min_order_qt,
         sp.std_sale_qt,
         sp.contract_no,
         sp.doc_ref_sdesc,
         sp.rfq_ref,
         sp.vendor_note,
         mim_local_db.db_id
      FROM
         part_price_sp sp
         INNER JOIN org_vendor vr ON
            vr.vendor_cd = sp.vendor_cd
         INNER JOIN eqp_part_no pn ON
            pn.part_no_oem = sp.part_no_oem
            AND
            pn.manufact_cd = sp.manufact_cd
         INNER JOIN ref_qty_unit un ON
            un.qty_unit_cd = sp.qty_unit_cd
         INNER JOIN ref_currency cr ON
            cr.currency_cd = sp.currency_cd
         LEFT OUTER JOIN ref_price_type pt ON
            pt.price_type_cd = sp.price_type_cd,
         Mim_Local_Db
      WHERE
         sp.uuid = as_Uuid
         AND
         (sp.change_cd =CHANGE_CODE_NEW OR sp.change_cd = CHANGE_CODE_REVISION)
         AND
         sp.vendor_exists = PASSED
         AND
         sp.manufact_exists = PASSED
         AND
         sp.part_no_exists = PASSED
         AND
         sp.qty_unit_exists = PASSED
         AND
         sp.currency_exists = PASSED
         AND
         sp.qty_unit_applies = PASSED
         AND
         sp.manufact_applies = PASSED
         AND
         sp.prt_vd_applies = PASSED
         AND
         sp.cnr_uniq_applies = PASSED

   ) s ON (
         t.vendor_db_id = s.vendor_db_id AND
         t.vendor_id = s.vendor_id
         AND
         t.part_no_db_id = s.part_no_db_id AND
         t.part_no_id = s.part_no_id
         AND
         t.effective_from_dt = s.effective_from_dt
         AND
         t.min_order_qt = s.min_order_qt
         AND
         t.qty_unit_db_id = s.qty_unit_db_id AND
         t.qty_unit_cd = s.qty_unit_cd
         AND
         t.hist_bool = 0
   )
   WHEN MATCHED THEN
      UPDATE SET
         t.unit_price = s.unit_price,
         t.currency_db_id = s.currency_db_id,
         t.currency_cd = s.currency_cd,
         t.effective_to_dt = s.effective_to_dt,
         t.lead_time = s.lead_time,
         t.discount_pct = s.discount_pct,
         t.std_sale_qt = s.std_sale_qt,
         t.price_type_db_id = s.price_type_db_id,
         t.price_type_cd = s.price_type_cd,
         t.vendor_note = s.vendor_note,
         t.doc_ref_sdesc = s.doc_ref_sdesc,
         t.contract_ref_sdesc = s.contract_no
   WHEN NOT MATCHED THEN
      INSERT (
         t.part_vendor_price_db_id,
         t.part_vendor_price_id,
         t.part_no_db_id,
         t.part_no_id,
         t.vendor_db_id,
         t.vendor_id,
         t.unit_price,
         t.currency_db_id,
         t.currency_cd,
         t.qty_unit_db_id,
         t.qty_unit_cd,
         t.lead_time,
         t.effective_from_dt,
         t.effective_to_dt,
         t.discount_pct,
         t.std_sale_qt,
         t.min_order_qt,
         t.price_type_db_id,
         t.price_type_cd,
         t.vendor_note,
         t.doc_ref_sdesc,
         t.contract_ref_sdesc
      )
      VALUES (
         s.db_id,
         -- get the sequence number
         (EQP_PART_VENDR_PRI_SEQ.nextval),
         s.part_no_db_id,
         s.part_no_id,
         s.vendor_db_id,
         s.vendor_id,
         s.unit_price,
         s.currency_db_id,
         s.currency_cd,
         s.qty_unit_db_id,
         s.qty_unit_cd,
         s.lead_time,
         s.effective_from_dt,
         s.effective_to_dt,
         s.discount_pct,
         s.std_sale_qt,
         s.min_order_qt,
         s.price_type_db_id,
         s.price_type_cd,
         s.vendor_note,
         s.doc_ref_sdesc,
         s.contract_no
      );

   MERGE INTO eqp_part_vendor_price t USING (
      SELECT
         vr.vendor_db_id,     -- Vendor (SPL)
         vr.vendor_id,
         pn.part_no_db_id,    -- Part number (PNR)
         pn.part_no_id,
         sp.effective_from_dt,-- Price effective date (PED)
         sp.min_order_qt,
         un.qty_unit_db_id,
         un.qty_unit_cd
      FROM
         part_price_sp sp
         INNER JOIN org_vendor vr ON
            vr.vendor_cd = sp.vendor_cd
         INNER JOIN eqp_part_no pn ON
            pn.part_no_oem = sp.part_no_oem
            AND
            pn.manufact_cd = sp.manufact_cd
         INNER JOIN ref_qty_unit un ON
            un.qty_unit_cd = sp.qty_unit_cd
      WHERE
         sp.uuid = as_Uuid
         AND
         sp.change_cd =CHANGE_CODE_DELETE
         AND
         sp.vendor_exists = PASSED
         AND
         sp.manufact_exists = PASSED
         AND
         sp.part_no_exists = PASSED
         AND
         sp.qty_unit_exists = PASSED
         AND
         sp.currency_exists = PASSED
         AND
         sp.qty_unit_applies = PASSED
         AND
         sp.manufact_applies = PASSED
         AND
         sp.prt_vd_applies = PASSED
         AND
         sp.cnr_uniq_applies = PASSED

   ) s ON (
         t.vendor_db_id = s.vendor_db_id AND
         t.vendor_id = s.vendor_id
         AND
         t.part_no_db_id = s.part_no_db_id AND
         t.part_no_id = s.part_no_id
         AND
         t.effective_from_dt = s.effective_from_dt
         AND
         t.min_order_qt = s.min_order_qt
         AND
         t.qty_unit_db_id = s.qty_unit_db_id AND
         t.qty_unit_cd = s.qty_unit_cd
   )
   WHEN MATCHED THEN
      UPDATE SET
         t.hist_bool = 1;

END saveValidPartPrices;

/********************************************************************************
*
* Procedure: hasIntersectingContract
* Arguments:
*     aPartPriceDbId   eqp_part_vendor_price.part_vendor_price_db_id%TYPE,
*     aPartPriceId     eqp_part_vendor_price.part_vendor_price_id%TYPE,
*     aContract STRING,
*     aEffectiveFrom DATE - candidate contract effective from date, may not be null
*     aEffectiveTo DATE - candidate contract effective to date, may be null
*
*
* Description: Returns a contract_ref_desc if the provided contract effective 
*              dates overlaps with an existing contracts effective dates, 
*              otherwise null.
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Nov 11, 2011
*
*********************************************************************************/
FUNCTION hasIntersectingContract(
      aPartPriceDbId   eqp_part_vendor_price.part_vendor_price_db_id%TYPE,
      aPartPriceId     eqp_part_vendor_price.part_vendor_price_id%TYPE, 
      aEffectiveFrom DATE,
      aEffectiveTo DATE
      )
      RETURN STRING 
      IS
      
      CURSOR lcur_VendorPrices
       IS 
            SELECT price.contract_ref_sdesc,
                     price.effective_from_dt,
                     price.effective_to_dt
            FROM  eqp_part_vendor_price price 
            WHERE 
                  NOT (price.part_vendor_price_db_id = aPartPriceDbId AND
                          price.part_vendor_price_id    = aPartPriceId) 
                  AND
                  price.contract_ref_sdesc IS NOT NULL 
                  AND
                  price.hist_bool = 0 ;
      lContract VARCHAR2(20);
      
  BEGIN

  FOR lVendorPrice IN lcur_VendorPrices LOOP

      lContract := getConflictingContract( lVendorPrice.Effective_From_Dt, lVendorPrice.Effective_To_Dt, lVendorPrice.Contract_Ref_Sdesc, aEffectiveFrom, aEffectiveTo );
      
      IF lContract IS NOT NULL THEN
        RETURN lContract;
      END IF;

  END LOOP;
  
  return lContract;
      
END hasIntersectingContract;      

/********************************************************************************
*
* FUNCTION: hasIntersectingContract
* Arguments:
*      aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE
*      aVendorId   eqp_part_vendor_price.vendor_id%TYPE
*      aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE
*      aPartId     eqp_part_vendor_price.part_no_id%TYPE
*      aEffectiveFrom DATE - candidate contract effective from date, may not be null
*      aEffectiveTo DATE - candidate contract effective to date, may be null
*
*
* Description: Returns a contract_ref_desc if the provided contract effective 
*              dates overlaps with an existing contracts effective dates, 
*              otherwise null.
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Nov 09, 2011
*
*********************************************************************************/
FUNCTION hasIntersectingContract(
      aVendorDbId eqp_part_vendor_price.vendor_db_id%TYPE,
      aVendorId   eqp_part_vendor_price.vendor_id%TYPE,
      aPartDbId   eqp_part_vendor_price.part_no_db_id%TYPE,
      aPartId     eqp_part_vendor_price.part_no_id%TYPE,
      aEffectiveFrom DATE,
      aEffectiveTo DATE
      )
      RETURN STRING 
      IS
      
      CURSOR lcur_VendorPrices
      IS 
        SELECT price.contract_ref_sdesc,
               price.effective_from_dt,
               price.effective_to_dt
        FROM   eqp_part_vendor_price price
        WHERE  price.part_no_db_id = aPartDbId AND
               price.part_no_id = aPartId AND
               price.vendor_db_id = aVendorDbId AND
               price.vendor_id = aVendorId AND
               price.contract_ref_sdesc IS NOT NULL AND
               price.hist_bool = 0;
      lContract VARCHAR2(20);
      
  BEGIN

  FOR lVendorPrice IN lcur_VendorPrices LOOP

      lContract := getConflictingContract( lVendorPrice.Effective_From_Dt, lVendorPrice.Effective_To_Dt, lVendorPrice.Contract_Ref_Sdesc, aEffectiveFrom, aEffectiveTo );
      
      IF lContract IS NOT NULL THEN
        RETURN lContract;
      END IF;
      
  END LOOP;
  
  return lContract;
      
END hasIntersectingContract;      
      
/********************************************************************************
*
* Procedure: doesIntersect
* Arguments:
*      aEffectiveFrom1 (DATE) -- Existing 'Effective From' date
*      aEffectiveTo1 (DATE)   -- Existing 'Effective To' date
*      aEffectiveFrom2 (DATE)-- Candidate 'Effective From' date
*      aEffectiveTo2 (DATE)  -- Candidate 'Effective To' date
*
*
* Description: Returns TRUE if given time frames (Effective_from - Effective_to)
*              intersects (overlaps). Otherwise false.
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Nov 07, 2011
*
*********************************************************************************/
FUNCTION doesIntersect(
      aEffectiveFrom1 DATE,
      aEffectiveTo1 DATE,
      aEffectiveFrom2 DATE,
      aEffectiveTo2 DATE)
   RETURN BOOLEAN IS
      lEffectiveTo1 DATE;
      lEffectiveTo2 DATE;
   BEGIN
     IF aEffectiveTo1 IS NULL THEN
       lEffectiveTo1:=MAX_DATE;
     ELSE
       lEffectiveTo1:=aEffectiveTo1;
     END IF;
     IF aEffectiveTo2 IS NULL THEN
       lEffectiveTo2:=MAX_DATE;
     ELSE
       lEffectiveTo2:=aEffectiveTo2;
     END IF;
     RETURN (aEffectiveFrom2 < lEffectiveTo1 AND lEffectiveTo2 > aEffectiveFrom1)
            OR
            (aEffectiveFrom1 < lEffectiveTo2 AND lEffectiveTo1 > aEffectiveFrom2);
   END DoesIntersect;

/********************************************************************************
* Inherits doc (see the header)
*********************************************************************************/
FUNCTION isOverlappingAllowed
   RETURN BOOLEAN
   IS
      ls_ConfigParmValue STRING(5);
BEGIN
   SELECT
      utl_config_parm.parm_value
   INTO
      ls_ConfigParmValue
   FROM
      utl_config_parm
   WHERE
      utl_config_parm.parm_name = CONFIG_PARM;
   IF UPPER(ls_ConfigParmValue) = 'TRUE' THEN
      RETURN true;
   ELSE
      RETURN false;
   END IF;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
      RETURN false;
END isOverlappingAllowed;


/********************************************************************************
* Inherits doc (see the header)
*********************************************************************************/
FUNCTION isContractPriceUnique(
      aPartNoOEM eqp_part_no.part_no_oem%TYPE,
      aManufactCd eqp_part_no.manufact_cd%TYPE,
      aVendorCd org_vendor.vendor_cd%TYPE,
      aEffectiveFromDt eqp_part_vendor_price.effective_from_dt%TYPE,
      aEffectiveToDt eqp_part_vendor_price.effective_to_dt%TYPE
      )
   RETURN NUMBER IS

     CURSOR lcur_VendorPartPrice
      IS
         SELECT
             eqp_part_vendor_price.effective_from_dt,
             eqp_part_vendor_price.effective_to_dt
         FROM
            eqp_part_vendor_price
         JOIN eqp_part_no ON eqp_part_no.part_no_db_id = eqp_part_vendor_price.part_no_db_id AND
                             eqp_part_no.part_no_id = eqp_part_vendor_price.part_no_id
         JOIN org_vendor ON org_vendor.vendor_db_id = eqp_part_vendor_price.vendor_db_id AND
                            org_vendor.vendor_id = eqp_part_vendor_price.vendor_id
         WHERE
            eqp_part_no.part_no_oem = aPartNoOEM
            AND
            eqp_part_no.manufact_cd = aManufactCd
            AND
            org_vendor.vendor_cd = aVendorCd
            AND
            eqp_part_vendor_price.hist_bool = 0
            AND
            eqp_part_vendor_price.contract_ref_sdesc IS NOT NULL;
   BEGIN
     FOR lVendorPartPriceRec IN lcur_VendorPartPrice LOOP
        IF (doesIntersect(aEffectiveFromDt,
                          aEffectiveToDt,
                          lVendorPartPriceRec.Effective_From_Dt,
                          lVendorPartPriceRec.Effective_To_Dt)) THEN
           RETURN REJECTED;
        END IF;
     END LOOP;
     RETURN PASSED;
   END isContractPriceUnique;

/********************************************************************************
*
* FUNCTION: Determines if the effective dates overlap.  
*           If true, returns the contract.
*           If false, return null.
* Arguments:
*           aCandidateFrom DATE - The effective 'from' date of an existing price
*           aCandidateTo DATE - The effective 'to' date of an existing price
*           aContract STRING - The contract ref sdesc of an existing price
*           aEffectiveFrom DATE - The effective 'from' date of an candidate price
*           aEffectiveTo DATE - The effective 'to' date of an candidate price
*
*
* Orig.Coder:     Stephane Levert
* Recent Coder:
* Recent Date:    Nov 10, 2011
*
*********************************************************************************/
FUNCTION getConflictingContract( aExistingEffectiveFrom DATE, 
                                 aExistingEffectiveTo DATE, 
                                 aContract STRING, 
                                 aCandidateEffectiveFrom DATE, 
                                 aCandidateEffectiveTo DATE 
                               )
RETURN STRING IS

BEGIN
   IF doesIntersect(aExistingEffectiveFrom, aExistingEffectiveTo, aCandidateEffectiveFrom, aCandidateEffectiveTo) THEN
      RETURN aContract;
   ELSE
      RETURN NULL;
   END IF;
END getConflictingContract;

END PART_PRICE_PKG;
/