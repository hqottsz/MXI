--liquibase formatted sql


--changeSet FlightInterface_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE FlightInterface IS
/********************************************************************************
*
* Package:     FlightInterface
* Description: This package is used to perform various actions on flight events
*              1) Execute aircraft swapping
*
* Author:   Hong Zheng
* Created Date:  19.Aug.2008
*
*********************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

/* constant codes for error handling*/
iSuccess                          CONSTANT NUMBER := 1;
iNoProc                           CONSTANT NUMBER := 0;
iCreateEvtInvEntryError           CONSTANT NUMBER := -1;
iUsageCalculationsError           CONSTANT NUMBER := -2;
iUpdateDeadlineForInvTreeError    CONSTANT NUMBER := -3;
iOracleError                      CONSTANT NUMBER := -100;


/******************************************************************************
*
* Procedure:    aircraftSwap
* Arguments:    aFlightLegId (raw16):
*               aInvNoDbId (number):
*               aInvNoId (number):
*               aAssmblDbId (number):
*               aAssmblCd (varchar2):
*               aReturn (number): Return 1 means success, <0 means failure
* Description:  This procedure will perform aircraft swapping. Inside this procedure,
*               private procedure 'generateFlightPlanForAircraft' will be invoked, and also
*               'USAGE_PKG.UsageCalculations' and 'EVENT_PKG.UpdateDeadlineForInvTree'.
*
* Author:   Hong Zheng
* Created Date:  19.Aug.2008
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE aircraftSwap(aFlightLegId IN fl_leg.leg_id%TYPE,
                       aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                       aInvNoId IN inv_inv.inv_no_id%TYPE,
                       aAssmblDbId IN eqp_assmbl.assmbl_db_id%TYPE,
                       aAssmblCd IN eqp_assmbl.assmbl_cd%TYPE,
                       aReturn OUT NUMBER
                       );

END FlightInterface;
/