--liquibase formatted sql

--changeSet OPER-11914:1 stripComments:false
--comment insert into table int_event_config
INSERT INTO 
	int_event_config 
	(
		event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
	'MX_ATA_SPARES_ORDER_MESSAGE',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM int_event_config WHERE event_type_cd = 'MX_ATA_SPARES_ORDER_MESSAGE' );