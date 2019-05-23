--liquibase formatted sql


--changeSet part_price_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
*            aQuantity   eqp_part_vendor_price.min_order_qt%TYPE - May be null.
*
* Description: Returns the lowesrt price for a vendor/part combination and a specified qunatity,
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