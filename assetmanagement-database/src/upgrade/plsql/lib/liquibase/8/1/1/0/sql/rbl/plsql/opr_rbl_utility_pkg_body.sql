--liquibase formatted sql


--changeSet opr_rbl_utility_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_rbl_utility_pkg IS

-- private constant
lcv_start_stage   CONSTANT  VARCHAR2(5) := 'START';
lcv_end_stage     CONSTANT  VARCHAR2(3) := 'END';
lcv_in_working    CONSTANT  VARCHAR2(7) := 'WORKING';
lcv_in_error      CONSTANT  VARCHAR2(5) := 'ERROR';
lcv_in_idle       CONSTANT  VARCHAR2(4) := 'IDLE';
lcv_in_complete   CONSTANT  VARCHAR2(8) := 'COMPLETE';

/******************************************************************************
*
*  Procedure:    opr_get_config_param
*  Arguments:
*  Return:
*  Description:  Function that will return the parallel mode parameter value
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  17-Dec-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

FUNCTION opr_get_config_param
RETURN BOOLEAN
IS

  lv_parm_value    utl_config_parm.parm_value%TYPE;

BEGIN

   SELECT
      NVL(parm_name,'FALSE')
   INTO
      lv_parm_value
   FROM
      utl_config_parm
   WHERE
      parm_name = 'MX_CORE_RELIABILITY_PARALLEL_MODE';

   IF lv_parm_value = 'TRUE' THEN
      RETURN TRUE;
   ELSE
      RETURN FALSE;
   END IF;

EXCEPTION
   WHEN no_data_found THEN
      RETURN FALSE;

END;


/******************************************************************************
*
*  Procedure:    opr_rbl_extract_proc
*  Arguments:
*  Return:
*  Description:  Procedure that will execute the extraction
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_rbl_extract_proc AS

   -- daily extraction cursor
   CURSOR lcur_report IS
   SELECT
      opr_report.ROWID row_id,
      opr_report.report_code,
      opr_report.program_name,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(start_date,SYSDATE)
         ELSE
            TRUNC((last_success_end_date - 1))
      END start_date,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(end_date,SYSDATE)
         ELSE
            TRUNC(SYSDATE)
      END end_date,
      opr_report.exec_parallel_flag
   FROM
      opr_report
   WHERE
      program_type_code = 'XTRACT'
      AND
      active_flag = 1
      AND -- not before the cut-off date
      start_date >= NVL(cutoff_date,start_date)
   ORDER BY
      execution_order;

   lcv_error_msg        opr_report.last_error%TYPE;
   ln_row_processed     opr_report.row_processed%TYPE;
   lbl_parallel_mode    BOOLEAN DEFAULT NULL;

BEGIN

   -- get parallel mode config
   lbl_parallel_mode := opr_get_config_param;

   --# daily extraction
   FOR lrec_report IN lcur_report LOOP

      BEGIN

         IF lbl_parallel_mode THEN
            --
            -- parallel mode
            -- submit an Oracle job
               NULL;

         ELSE

            -- start logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_start_stage,
               aiv_exec_status_code => lcv_in_working
            );

            -- normal daily extraction
            EXECUTE IMMEDIATE 'BEGIN :aon_rec_count := ' || lrec_report.program_name || '; END;' USING OUT ln_row_processed, lrec_report.start_date, lrec_report.end_date;

            -- commit the work
            COMMIT;

            -- end logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               ain_row_processed    => ln_row_processed,
               aiv_exec_status_code => lcv_in_idle,
               aidt_start_date      => lrec_report.start_date,
               aidt_end_date        => lrec_report.end_date
            );

         END IF;

      EXCEPTION
         WHEN OTHERS THEN
            lcv_error_msg := SQLERRM;
            -- error logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               aiv_error_message    => lcv_error_msg,
               aiv_exec_status_code => lcv_in_error
            );

           IF lrec_report.exec_parallel_flag = 0 THEN
              -- exit on this loop
              EXIT;
           END IF;

      END;

   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
     -- utl job error logging - to be developed
      ROLLBACK;
      RAISE;

END opr_rbl_extract_proc;

/******************************************************************************
*
*  Procedure:    opr_rbl_regen_proc
*  Arguments:
*  Return:
*  Description:  Procedure that will execute the regeneration
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_rbl_regen_proc AS

   -- regeneration cursor
   CURSOR lcur_regen_xtract IS
   SELECT
      opr_report_regen.ROWID row_id,
      opr_report.report_code,
      opr_report.program_name xtract_program_name,
      opr_report.exec_parallel_flag,
      program_type_code,
      opr_report_regen.start_date,
      opr_report_regen.end_date
   FROM
      opr_report
      INNER JOIN opr_report_regen ON
         opr_report.report_code = opr_report_regen.report_code
   WHERE
      opr_report.active_flag = 1
      AND
      opr_report_regen.start_date >= NVL(opr_report.cutoff_date,opr_report_regen.start_date)
      AND
      opr_report_regen.exec_end_date IS NULL
   ORDER BY
      -- extract should go first followed by transformation
      DECODE(program_type_code,'XTRACT',0,1),
      execution_order;

   lcv_error_msg        opr_report.last_error%TYPE;
   ln_row_processed     opr_report.row_processed%TYPE;
   lbl_parallel_mode    BOOLEAN DEFAULT NULL;
   lv_regen_stage       VARCHAR2(20);

BEGIN

   -- get parallel mode config
   lbl_parallel_mode := opr_get_config_param;

   --
   FOR lrec_regen IN lcur_regen_xtract LOOP

      BEGIN

         IF lbl_parallel_mode THEN
            --
            -- parallel mode
            -- submit an Oracle job
               NULL;

         ELSE

            -- start logging
            opr_rbl_regen_logger_proc (
               airid_row_id         => lrec_regen.row_id,
               aiv_exec_stage_code  => lcv_start_stage,
               aiv_exec_status_code => lcv_in_working
            );

            -- execute the extraction program
            EXECUTE IMMEDIATE 'BEGIN :aon_rec_count := ' || lrec_regen.xtract_program_name || '; END;' USING OUT ln_row_processed, lrec_regen.start_date, lrec_regen.end_date;

            -- success logging
            opr_rbl_regen_logger_proc (
               airid_row_id         => lrec_regen.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               aiv_exec_status_code => lcv_in_complete
            );

            -- commit the work
            COMMIT;

         END IF;

      EXCEPTION
         WHEN OTHERS THEN
            -- error logging
            lcv_error_msg := SQLERRM;
            opr_rbl_regen_logger_proc (
               airid_row_id        => lrec_regen.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               aiv_exec_status_code => lcv_in_error,
               aiv_error_message   => lv_regen_stage || ' ' ||lcv_error_msg
            );
      END;
      
  END LOOP;

EXCEPTION
   WHEN OTHERS THEN
     -- utl job error logging - to be developed
      ROLLBACK;
      RAISE;

END opr_rbl_regen_proc;


/******************************************************************************
*
*  Procedure:    opr_rbl_transform_proc
*  Arguments:
*  Return:
*  Description:  Procedure that will execute the transformation
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_rbl_transform_proc AS

   CURSOR lcur_report IS
   SELECT
      opr_report.ROWID row_id,
      opr_report.report_code,
      opr_report.program_name,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(start_date,SYSDATE)
         ELSE
            TRUNC(last_success_end_date,'MONTH')
      END start_date,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(end_date,SYSDATE)
         ELSE
            LAST_DAY(TRUNC(last_success_end_date))
      END end_date,
      opr_report.exec_parallel_flag
   FROM
      opr_report
   WHERE
      program_type_code = 'XFORM'
      AND
      active_flag = 1
      AND -- not before the cut-off date
      start_date >= NVL(cutoff_date,start_date)      
   ORDER BY
      execution_order;

   ln_row_processed     opr_report.row_processed%TYPE;
   lcv_error_msg        opr_report.last_error%TYPE;
   lv_parallel_mode     BOOLEAN;

BEGIN

   -- get parallel mode config
   lv_parallel_mode := opr_get_config_param;

   -- transformation
   FOR lrec_report IN lcur_report LOOP

      BEGIN

         IF lv_parallel_mode THEN
            --
            -- parallel mode
            -- submit an Oracle job
               NULL;

         ELSE

            -- start logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_start_stage,
               aiv_exec_status_code => lcv_in_working
            );

            -- normal daily extraction
            EXECUTE IMMEDIATE 'BEGIN :aon_rec_count := ' || lrec_report.program_name || '; END;' USING OUT ln_row_processed, lrec_report.start_date, lrec_report.end_date;

            -- commit the work
            COMMIT;

            -- end logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               ain_row_processed    => ln_row_processed,
               aiv_exec_status_code => lcv_in_idle,
               aidt_start_date      => lrec_report.start_date,
               aidt_end_date        => lrec_report.end_date
            );

         END IF;

      EXCEPTION
         WHEN OTHERS THEN
            lcv_error_msg := SQLERRM;
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               aiv_error_message    => lcv_error_msg,
               aiv_exec_status_code => lcv_in_error
            );

           IF lrec_report.exec_parallel_flag = 0 THEN
              -- exit on this loop
              EXIT;

           END IF;

      END;

   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
     -- utl job error logging - to be developed
      ROLLBACK;
      RAISE;

END opr_rbl_transform_proc;


/******************************************************************************
*
*  Procedure:    opr_rbl_logger_proc
*  Arguments:
*  Return:
*  Description:  procedure that will do the error logging
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_rbl_logger_proc (
   airid_row_id           IN    ROWID,
   aiv_exec_stage_code    IN    VARCHAR2  DEFAULT 'START',
   aiv_exec_status_code   IN    VARCHAR2  DEFAULT 'ERROR',
   ain_row_processed      IN    opr_report.row_processed%TYPE DEFAULT 0,
   aiv_error_message      IN    opr_report.last_error%TYPE    DEFAULT NULL,
   aidt_start_date        IN    opr_report.start_date%TYPE    DEFAULT NULL,
   aidt_end_date          IN    opr_report.end_date%TYPE      DEFAULT NULL
) IS

  PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

   -- f1: check for execution stage
   IF UPPER(aiv_exec_stage_code) = 'START' THEN

      -- start stage
      UPDATE
         opr_report
      SET
         last_start_date = SYSDATE,
         status_code     = aiv_exec_status_code
      WHERE
         ROWID = airid_row_id;

   ELSE

      -- end stage
      --
      -- f2: check the execution status -- [S]uccess | [E]rror
      IF aiv_exec_status_code = 'ERROR' THEN

         -- error status
         UPDATE
            opr_report
         SET
            last_error_end_date = SYSDATE,
            last_error          = aiv_error_message,
            status_code         = aiv_exec_status_code
         WHERE
            ROWID = airid_row_id;

      ELSE

         -- success status
         UPDATE
            opr_report
         SET
            start_date            = aidt_start_date,
            end_date              = aidt_end_date,
            last_success_end_date = SYSDATE,
            last_error_end_date   = NULL,
            last_error            = NULL,
            row_processed         = ain_row_processed,
            status_code           = aiv_exec_status_code
         WHERE
            ROWID = airid_row_id;

      END IF; -- f2

   END IF; -- f1

   -- commit the work
   COMMIT;

EXCEPTION
   WHEN OTHERS THEN
      RAISE;

END opr_rbl_logger_proc;


/******************************************************************************
*
*  Procedure:    opr_rbl_regen_logger_proc
*  Arguments:
*  Return:
*  Description:  procedure that will do the regeneration error logging
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_rbl_regen_logger_proc (
   airid_row_id           IN    ROWID,
   aiv_exec_stage_code    IN    VARCHAR2  DEFAULT 'START',
   aiv_exec_status_code   IN    VARCHAR2  DEFAULT 'ERROR',
   aiv_error_message      IN    opr_report_regen.error%TYPE DEFAULT NULL
) IS

  PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

   -- f1: check for execution stage
   IF UPPER(aiv_exec_stage_code) = lcv_start_stage THEN

      -- start stage
      UPDATE
         opr_report_regen
      SET
         exec_start_date = SYSDATE,
         status_code     = lcv_in_working
      WHERE
         ROWID = airid_row_id;

   ELSE

      IF aiv_exec_status_code = lcv_in_error THEN

         -- error status
         UPDATE
            opr_report_regen
         SET
            exec_end_date = SYSDATE,
            status_code   = lcv_in_error,
            error         = aiv_error_message
         WHERE
            ROWID = airid_row_id;

       ELSE
         -- success
         UPDATE
            opr_report_regen
         SET
            exec_end_date = SYSDATE,
            status_code   = lcv_in_idle,
            error         = NULL
         WHERE
            ROWID = airid_row_id;

       END IF;

   END IF;

      -- commit the work
      COMMIT;

EXCEPTION
   WHEN OTHERS THEN
      RAISE;

END opr_rbl_regen_logger_proc;

/******************************************************************************
*
*  Procedure:    opr_get_report_job
*  Arguments:
*  Return:
*  Description:  function that will return the report job information
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  01-Apr-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_get_report_job(
   aiv_job_code     IN   opr_report_job.job_code%TYPE
)
RETURN opr_report_job%ROWTYPE
RESULT_CACHE RELIES_ON (opr_report_job)
IS

  lrec_report_job    opr_report_job%ROWTYPE;

BEGIN

   -- select row
   SELECT
      *
   INTO
      lrec_report_job
   FROM
      opr_report_job
   WHERE
      job_code = aiv_job_code;

   -- return the record
   RETURN lrec_report_job;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
       -- return an empty record
       RETURN lrec_report_job;

END opr_get_report_job;


/******************************************************************************
*
*  Procedure:    opr_mview_refresh
*  Arguments:
*  Return:
*  Description:  Function for refreshing operator materialized view
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  01-Apr-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_refresh_mview (
   aiv_category_or_frequency  IN   opr_report_mview.category%TYPE,
   aiv_refresh_type_code      IN   VARCHAR2
)
RETURN NUMBER
IS

   -- collect the materialized view
   CURSOR lcur_mview IS
   SELECT
      mview_name
      -- # this can be used in feature as well as mview grouping methodology
      -- LISTAGG(mview_name,',') within GROUP (ORDER BY category, execution_order) AS mview_name
   FROM
      opr_report_mview
   WHERE
      (
        UPPER(category) = UPPER(aiv_category_or_frequency) AND
        aiv_refresh_type_code = 'CATEGORY'
      )
      OR
      (
        UPPER(refresh_frequency_code) = UPPER(aiv_category_or_frequency) AND
        aiv_refresh_type_code = 'FREQUENCY'
      )
   ORDER BY 
      execution_order;

BEGIN

   -- only process if NOT both of the parameters are empty
   IF aiv_category_or_frequency IS NOT NULL AND
      aiv_refresh_type_code IS NOT NULL THEN

      --
      FOR lrec_mview IN lcur_mview LOOP

          BEGIN

              -- refresh
              DBMS_MVIEW.refresh( list           => lrec_mview.mview_name,
                                  method         => 'C',
                                  atomic_refresh => FALSE
                                );

           -- display error and then continue
          EXCEPTION
             WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE(SQLERRM);
          END;

      END LOOP;

   END IF;

   --
   RETURN 1;

EXCEPTION
   WHEN OTHERS THEN
      RETURN -1;
      RAISE;

END opr_refresh_mview;


/******************************************************************************
*
*  Procedure:    opr_report_job_log
*  Arguments:
*  Return:
*  Description:  function for report job logging
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  01-Apr-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_report_job_log (
   aiv_job_code           IN    opr_report_job.job_code%TYPE,
   aiv_exec_stage_code    IN    VARCHAR2                           DEFAULT 'START',
   aiv_frequency_code     IN    opr_report_job.frequency_code%TYPE DEFAULT NULL,
   ain_interval           IN    opr_report_job.interval%TYPE       DEFAULT NULL
)
IS

   PRAGMA AUTONOMOUS_TRANSACTION;

   ldt_next_run_date       DATE;

BEGIN

   -- f1: check for execution stage
   IF UPPER(aiv_exec_stage_code) = 'START' THEN

      -- start stage
      UPDATE
         opr_report_job
      SET
         last_start_date = SYSDATE
      WHERE
         job_code = aiv_job_code;

   ELSE

      -- end stage

      --
      -- construct the next run date
      CASE
         -- daily
         WHEN aiv_frequency_code = 'DAILY'  THEN
            ldt_next_run_date := TRUNC(sysdate+1)-(1/(24*60*60));

         -- monthly
         WHEN aiv_frequency_code = 'MONTHLY'  THEN
            ldt_next_run_date := (LAST_DAY(TRUNC(SYSDATE))+1) - (1/(24*60*60));

         -- or dont run it again
         ELSE
            ldt_next_run_date := NULL;

      END CASE;

      --
      -- do the update
      UPDATE
         opr_report_job
      SET
         next_run_date = ldt_next_run_date,
         last_end_date = SYSDATE
      WHERE
         job_code = aiv_job_code;

   END IF;

   -- commit the work
   COMMIT;

END opr_report_job_log;

/******************************************************************************
*
*  Procedure:    opr_check_running_job
*  Arguments:
*  Return:
*  Description:  function that will check if an Oracle job is running
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  02-Apr-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

FUNCTION opr_check_running_job(
   aiv_job_code     IN   opr_report_job.job_code%TYPE
)
RETURN BOOLEAN
IS

   ln_count    NUMBER;

BEGIN

   SELECT
     COUNT(1)
   INTO
     ln_count
   FROM
     user_scheduler_running_jobs
   WHERE
     job_name = aiv_job_code;

   -- if running return true
   IF ln_count = 1 THEN
      --
      RETURN TRUE;
   ELSE
      --
      RETURN FALSE;

   END IF;

END;

---
END opr_rbl_utility_pkg;
/