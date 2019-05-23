--liquibase formatted sql


--changeSet acor_operator_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_operator_v1
AS
SELECT
   org_carrier.alt_id      AS operator_id,
   iata_cd                 AS iata_code,
   icao_cd                 AS icao_code,
   callsign_sdesc          AS callsign_description,
   extrn_ctrl_bool         AS external_control_flag,
   carrier_cd              AS operator_code,
   org_org.alt_id          AS organization_id,
   org_org.org_sdesc       AS operator_name,
   org_org.org_sub_type_cd AS organization_sub_type_code,
   blob_data.blob_data     AS operator_logo,
   org_carrier.rstat_cd    AS rstat_code
FROM
   org_carrier
   LEFT JOIN org_org ON
      org_carrier.org_db_id = org_org.org_db_id AND
      org_carrier.org_id    = org_org.org_id
   LEFT JOIN org_logo ON
      org_carrier.Org_Db_Id = org_logo.org_db_id AND
      org_carrier.org_id    = org_logo.org_id
   LEFT JOIN cor_perm_blob blob_data ON
      org_logo.blob_db_id = blob_data.blob_db_id AND
      org_logo.blob_id    = blob_data.blob_id
WHERE
   org_carrier.rstat_cd <> 3
;