--liquibase formatted sql


--changeSet 0ref_flight_leg_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FLIGHT_LEG_STATUS"
** 0-Level
** DATE: 30-SEP-2010
*********************************************/
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXPLAN', 0, 80, 'Planned', 'Flight has been planned', 'PLAN', 10, '0:FLPLAN',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:2 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXOUT', 0, 80, 'Left Gate', 'Flight time starts', 'OUT', 20, '0:FLOUT',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:3 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXOFF', 0, 80, 'Airborne', 'Air time starts', 'OFF', 30, '0:FLOFF',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:4 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXON', 0, 80, 'On Ground', 'Air time stops', 'ON', 40, '0:FLON',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:5 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXIN', 0, 80, 'At Gate', 'Flight time stops', 'IN', 50, '0:IN',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:6 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXCMPLT', 0, 83, 'Complete', 'Flight complete', 'COMPLETE', 60, '0:FLCMPLT',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:7 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXDELAY', 0, 83, 'Delay', 'Flight delay', 'DELAY', 65, '0:FLDELAY',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:8 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXCANCEL', 0, 83, 'Cancelled', 'Flight cancellation', 'CANCEL', 75, '0:FLCANCEL',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:9 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXERR', 0, 83, 'Error', 'Error was found in record', 'ERROR', 95, '0:FLERR',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:10 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXEDIT', 0, 83, 'Edit', 'Flight was edited', 'EDIT', 95, '0:FLEDIT',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:11 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXDIVERT', 0, 83, 'Divert', 'Flight has diverted to a station other than the scheduled arrival station', 'DIVERT', 35, '0:DIVERT',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:12 stripComments:false
INSERT INTO ref_flight_leg_status(ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
VALUES (0, 'MXRETURN', 0, 83, 'Return', 'Aircraft has returned to gate before taking-off', 'RETURN', 25, '0:RETURN',  0, sysdate, 0, sysdate, 0, 'MXI', 1);

--changeSet 0ref_flight_leg_status:13 stripComments:false
INSERT INTO ref_flight_leg_status (ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no)
values (0, 'MXBLKOUT', 0, 1, 'N/A', 'N/A', 'BLKOUT', 1, '0:FLBLKOUT', 3, sysdate, 0, sysdate, 0, 'MXI', 1);