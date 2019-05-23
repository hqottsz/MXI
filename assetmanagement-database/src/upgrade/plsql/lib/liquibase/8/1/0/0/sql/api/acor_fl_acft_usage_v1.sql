--liquibase formatted sql


--changeSet acor_fl_acft_usage_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fl_acft_usage_v1
AS 
SELECT
   fl_leg.leg_id,
   mim_data_type.data_type_cd,
   usg_usage_data.tsn_qt
FROM
   fl_leg
   -- usage
   INNER JOIN usg_usage_record ON
      fl_leg.usage_record_id = usg_usage_record.usage_record_id AND
      fl_leg.aircraft_db_id  = usg_usage_record.inv_no_db_id    AND
      fl_leg.aircraft_id     = usg_usage_record.inv_no_id
   INNER JOIN usg_usage_data ON
      usg_usage_record.usage_record_id = usg_usage_data.usage_record_id AND
      usg_usage_record.inv_no_db_id    = usg_usage_data.inv_no_db_id    AND
      usg_usage_record.inv_no_id       = usg_usage_data.inv_no_id
   INNER JOIN mim_data_type ON
      usg_usage_data.data_type_db_id = mim_data_type.data_type_db_id AND
      usg_usage_data.data_type_id    = mim_data_type.data_type_id;