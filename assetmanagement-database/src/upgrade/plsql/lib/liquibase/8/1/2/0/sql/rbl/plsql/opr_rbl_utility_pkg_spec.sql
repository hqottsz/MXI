--liquibase formatted sql


--changeSet opr_rbl_utility_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   aiv_error_message      IN    opr_report.last_error%TYPE    DEFAULT NULL,
   aidt_start_date        IN    opr_report.start_date%TYPE    DEFAULT NULL,
   aidt_end_date          IN    opr_report.end_date%TYPE      DEFAULT NULL
);

-- procedure that will handle regeneration logging
PROCEDURE opr_rbl_regen_logger_proc (
   airid_row_id           IN    ROWID,
   aiv_exec_stage_code    IN    VARCHAR2  DEFAULT 'START',
   aiv_exec_status_code   IN    VARCHAR2  DEFAULT 'ERROR',
   aiv_error_message      IN    opr_report_regen.error%TYPE DEFAULT NULL
);

-- function to test the existence of a configuration paramter
FUNCTION opr_get_config_param
RETURN BOOLEAN;

-- procedure that will handle the execution of extraction procedure
PROCEDURE opr_rbl_extract_proc;

-- procedure that will handle the regeneration procedure
PROCEDURE opr_rbl_regen_proc;

-- procedure that will handel the execution of transformation procedure
PROCEDURE opr_rbl_transform_proc;

-- function that will retrieve job from opr_report_job
FUNCTION opr_get_report_job(
   aiv_job_code     IN   opr_report_job.job_code%TYPE
)
RETURN opr_report_job%ROWTYPE
RESULT_CACHE;

-- function that will check if job is currently running
FUNCTION opr_check_running_job(
   aiv_job_code     IN   opr_report_job.job_code%TYPE
)
RETURN BOOLEAN;

-- function that will refresh materialized view as recorded in opr_report_mview
FUNCTION opr_refresh_mview (
   aiv_category_or_frequency  IN   opr_report_mview.category%TYPE,
   aiv_refresh_type_code      IN   VARCHAR2
)
RETURN NUMBER;

-- procedure that will handle the report job logging
PROCEDURE opr_report_job_log (
   aiv_job_code           IN    opr_report_job.job_code%TYPE,
   aiv_exec_stage_code    IN    VARCHAR2                           DEFAULT 'START',
   aiv_frequency_code     IN    opr_report_job.frequency_code%TYPE DEFAULT NULL,
   ain_interval           IN    opr_report_job.interval%TYPE       DEFAULT NULL
);

--
END opr_rbl_utility_pkg;
/