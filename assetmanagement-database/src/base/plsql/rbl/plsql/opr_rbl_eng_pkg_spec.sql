--liquibase formatted sql


--changeSet opr_rbl_eng_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE opr_rbl_eng_pkg IS
/******************************************************************************
* Original Coder   : Max Gabua
* Last Modified By :
* Last Modified On : 26-Nov-2014
*
* Code Review By   :
* Code Review Date :
*
* Script Description: Package for Engine Operation Summary
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

-- function to extract the Maintenix raw data
FUNCTION opr_rbl_eng_xtract (
   aidt_start_date   IN    DATE,
   aidt_end_date     IN    DATE
) RETURN NUMBER;

-- function to transform the Maintenix data
FUNCTION opr_rbl_eng_xform (
   aidt_start_date   IN    DATE,
   aidt_end_date     IN    DATE
) RETURN NUMBER;

-- function to transform the legacy historical data
FUNCTION opr_rbl_hist_eng_xform
RETURN NUMBER;

--
END opr_rbl_eng_pkg;
/