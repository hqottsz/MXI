--liquibase formatted sql


--changeSet lrp_report_silt_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW lrp_report_silt_v1
AS
SELECT
   plan_db_id,
   plan_id,
   plan_sdesc,
   day_dt,
   acft_cond,
   value
FROM
   (
      SELECT
         plan_db_id,
         plan_id,
         plan_sdesc,
         day_dt,
         avail_qt,
         in_maint_qt,
         overflow_qt,
         retired_qt
      FROM
         lrp_report_silt
    )
    UNPIVOT ( (VALUE) FOR acft_cond IN (
                                          avail_qt     AS 'Available',
                                          in_maint_qt  AS 'In Maintenance',
                                          overflow_qt  AS 'Overflow',
                                          retired_qt   AS 'Retired'
                                       )
             )
;             