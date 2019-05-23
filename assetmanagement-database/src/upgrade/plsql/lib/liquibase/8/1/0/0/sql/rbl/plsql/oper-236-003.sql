--liquibase formatted sql


--changeSet oper-236-003:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE opr_rbl_fault_pkg IS
/******************************************************************************
* Original Coder   : Max Gabua
* Last Modified By :
* Last Modified On : 28-Nov-2013
*
* Code Review By   :
* Code Review Date :
*
* Script Description: Package for PIREP/MAREP/CABIN defects
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

-- procedure to extract raw data
FUNCTION opr_rbl_fault_xtract_proc (
   aidt_start_date   IN    DATE,
   aidt_end_date     IN    DATE
) RETURN NUMBER;

-- procedure to transform data
FUNCTION opr_rbl_fault_xform_proc (
   aidt_start_date   IN    DATE,
   aidt_end_date     IN    DATE
) RETURN NUMBER;

END opr_rbl_fault_pkg;
/