-- faults and corrective tasks (with references)
INSERT INTO sd_fault(fault_db_id, fault_id, alt_id, eval_bool, defer_cd_sdesc) VALUES (4650, 1, 'E34DDEC77100452487ED91C6DE0A8CD2', 1,  '10010');
INSERT INTO sched_stask(sched_db_id, sched_id, alt_id, main_inv_no_db_id, main_inv_no_id, fault_db_id, fault_id) VALUES (4650, 100, '59720662DDEF479DAA9038BB9B10B878', 4650, 1, 4650, 1);

INSERT INTO sd_fault(fault_db_id, fault_id, alt_id, eval_bool, defer_cd_sdesc) VALUES (4650, 3, 'E34DDEC77100452487ED91C6DE0A8CD3', 1,  '10012');
INSERT INTO sched_stask(sched_db_id, sched_id, alt_id, main_inv_no_db_id, main_inv_no_id, fault_db_id, fault_id) VALUES (4650, 300, '59720662DDEF479DAA9038BB9B10B880', 4650, 3, 4650, 3);

-- fault and corrective task (without references)
INSERT INTO sd_fault(fault_db_id, fault_id, alt_id, eval_bool, defer_cd_sdesc) VALUES (4650, 2, '59720662DDEF479DAA9038BB9B10B879', 1,  '10011');
INSERT INTO sched_stask(sched_db_id, sched_id, alt_id, main_inv_no_db_id, main_inv_no_id, fault_db_id, fault_id) VALUES (4650, 200, '59720662DDEF479DAA9038BB9B10B879', 4650, 2, 4650, 2);

-- repair reference - with request
INSERT INTO sd_fault_reference (fault_ref_db_id, fault_ref_id, fault_db_id, fault_id, rep_ref_db_id, rep_ref_id, notes, current_bool, stage_reason_cd, stage_reason_db_id) VALUES ('1', '123', '4650', '1', '1', '754', 'notes repair', '1', 'REASON', 1);
INSERT INTO sd_fault_reference_request (fault_ref_req_db_id, fault_ref_req_id, hr_db_id, hr_id, request_status_cd, date_requested) VALUES ('1', '123', '1', '455', 'PENDING', TO_DATE('11-SEP-18', 'DD-MON-RR'));

-- deferral reference - no request
INSERT INTO sd_fault_reference (fault_ref_db_id, fault_ref_id, fault_db_id, fault_id, defer_ref_db_id, defer_ref_id, notes, current_bool, stage_reason_cd, stage_reason_db_id) VALUES ('1', '124', '4650', '1', '1', '341', 'notes deferral', '0', 'REASON', 1);

-- deferral reference - with request
INSERT INTO sd_fault_reference (fault_ref_db_id, fault_ref_id, fault_db_id, fault_id, defer_ref_db_id, defer_ref_id, notes, current_bool, stage_reason_cd, stage_reason_db_id) VALUES ('1', '125', '4650', '3', '1', '342', 'notes deferral 2', '1', 'REASON', 1);
INSERT INTO sd_fault_reference_request (fault_ref_req_db_id, fault_ref_req_id, hr_db_id, hr_id, request_status_cd, date_requested) VALUES ('1', '125', '1', '455', 'PENDING', TO_DATE('10-SEP-18', 'DD-MON-RR'));

-- fault events
INSERT INTO evt_event(event_db_id, event_id) VALUES (4650, 1);
INSERT INTO evt_event(event_db_id, event_id) VALUES (4650, 2);
INSERT INTO evt_event(event_db_id, event_id) VALUES (4650, 3);

-- relations between fault events and corrective task events
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id) VALUES (4650, 1, 1, 4650, 100);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id) VALUES (4650, 2, 1, 4650, 200);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id) VALUES (4650, 3, 1, 4650, 300);

-- corrective task events
INSERT INTO evt_event (event_db_id, event_id, event_ldesc) VALUES (4650, 100, 'name');
INSERT INTO evt_event (event_db_id, event_id, event_ldesc) VALUES (4650, 200, 'name2');
INSERT INTO evt_event (event_db_id, event_id, event_ldesc) VALUES (4650, 300, 'name3');
