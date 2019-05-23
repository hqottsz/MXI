--liquibase formatted sql


--changeSet oper-236-001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE opr_rbl_utility_pkg IS
/******************************************************************************
* Original Coder   : Max Gabua
* Last Modified By :
* Last Modified On : 28-Nov-2013
*
* Code Review By   :
* Code Review Date :
*
* Script Description: Package for Reliability that will handle the execution of
*                     extraction and transformation procedure
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

-- procedure that will handle the report logging
PROCEDURE opr_rbl_logger_proc (
   airid_row_id           IN    ROWID,
   aiv_exec_stage_code    IN    VARCHAR2  DEFAULT 'START',
   aiv_exec_status_code   IN    VARCHAR2  DEFAULT 'ERROR',
   ain_row_processed      IN    opr_report.row_processed%TYPE DEFAULT 0,
   aiv_error_message      IN    opr_report.last_error%TYPE DEFAULT NULL
);

-- procedure that will handle regeneration logging
PROCEDURE opr_rbl_regen_logger_proc (
   airid_row_id           IN    ROWID,
   aiv_execute_type_cd    IN    VARCHAR DEFAULT 'XT',
   aiv_error_message      IN    opr_report_regen.error%TYPE DEFAULT NULL
);

FUNCTION opr_get_config_param RETURN BOOLEAN;

-- procedure that will handle the execution of extraction procedure
PROCEDURE opr_rbl_extract_proc;

-- procedure that will handel the execution of transformation procedure
PROCEDURE opr_rbl_transform_proc;

END opr_rbl_utility_pkg;
/