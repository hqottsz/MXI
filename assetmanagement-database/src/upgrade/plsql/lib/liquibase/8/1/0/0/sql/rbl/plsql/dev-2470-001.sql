--liquibase formatted sql


--changeSet dev-2470-001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE opr_mview_utility_pkg IS
/******************************************************************************
* Original Coder   : Max Gabua
* Last Modified By :
* Last Modified On : 14-Feb-2014
*
* Code Review By   :
* Code Review Date :
*
* Script Description: Package for refreshing operator  materialized view
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

-- procedure that will handle materialized view refresh
PROCEDURE opr_refresh_mview;

END opr_mview_utility_pkg;
/