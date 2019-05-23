--liquibase formatted sql


--changeSet 0ref_material_req_status:1 stripComments:false
/***********************************************************
** INSERT SCRIPT FOR TABLE "REF_MATERIALS_REQUEST_STATUS"
** 0-Level
** DATE: 08-MAR-2017 TIME: 15:51:00
************************************************************/
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'N/A', 'N/A', 'Material request status not applicable for the task.', 'Indicates that material request status is not applicable for the task, as there are no part requests, or the part requests have been completed.', 0,'PRISSUED',1, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:2 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'PENDING', 'PENDING', 'Material request pending.', 'Indicates that at least one request is awaiting processing.', NULL, NULL, 10, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:3 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'ACKNOWLEDGED', 'ACKNOWLEDGED', 'Material request receiving acknoweleged.', 'Indicates that at least one request has been received and acknowledged by materials, though the request has not been processed.',0,'PRACKNOWLEDGED', 9, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:4 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'NOT AVAILABLE', 'NOT AVAILABLE', 'Material request is not available.', 'Indicates that at least one request is not available.', 0, 'PRNOTAVAILABLE', 8, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:5 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'ON ORDER', 'ON ORDER', 'Material request is on order.', 'Indicates that at least one request is on order.', 0, 'PRONORDER', 7, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:6 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'IN REPAIR', 'IN REPAIR', 'Material request is in repair.', 'Indicates that at least one request is in repair.', 0, 'PRINREPAIR', 6, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:7 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'IN TRANSIT', 'IN TRANSIT', 'Material request is in transit.', 'Indicates that at least one request is in transit.', 0, 'PRINTRANSIT', 5, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:8 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'ON HAND', 'ON HAND', 'All requested parts are available, but have not been individually reserved.', 'Indicates that all components are available, but have not been individually reserved as the needed by date is not for some time.', 0, 'PRONHAND', 4, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:9 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'READY TO PICK UP', 'READY TO PICK UP', 'All requested parts for this task and subtasks are available to pick up.', 'Indicates that all the requested parts for this task and subtasks are available to pick up.', 0, 'PRREADYFORPICKUP', 3, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:10 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'NOT KNOWN', 'N/A', 'The status of part request is not known.', 'Indicates that the status of part request is not known.', NULL, NULL, 2, 0, TO_DATE('2016-06-06', 'YYYY-MM-DD'), TO_DATE('2016-06-06', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_material_req_status:11 stripComments:false
INSERT INTO ref_material_req_status(request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,event_status_db_id,event_status_cd,priority_ord,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'OPEN', 'OPEN', 'Material request is open.', 'Indicates that at least one request is open.', 0, 'PROPEN', 0, 0, TO_DATE('2016-07-22', 'YYYY-MM-DD'), TO_DATE('2016-07-22', 'YYYY-MM-DD'), 100, 'MXI');
