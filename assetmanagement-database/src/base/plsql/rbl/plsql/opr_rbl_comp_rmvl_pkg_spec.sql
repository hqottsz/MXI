--liquibase formatted sql


--changeSet opr_rbl_comp_rmvl_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE opr_rbl_comp_rmvl_pkg IS
/******************************************************************************
* Original Coder   : Max Gabua
* Last Modified By :
* Last Modified On : 28-Nov-2013
*
* Code Review By   :
* Code Review Date :
*
* Script Description: Package for component removal
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

-- function to extract Maintenx component removal raw data
FUNCTION opr_rbl_comp_rmvl_xtract (
   aidt_start_date   IN    DATE,
   aidt_end_date     IN    DATE
) RETURN NUMBER;

-- function to transform Maintenix component removal data
FUNCTION opr_rbl_comp_rmvl_xform (
   aidt_start_date   IN    DATE,
   aidt_end_date     IN    DATE
) RETURN NUMBER;

-- function to transform historical component removal data
FUNCTION opr_rbl_hist_comp_rmvl_xform
RETURN NUMBER;

--
END opr_rbl_comp_rmvl_pkg;
/