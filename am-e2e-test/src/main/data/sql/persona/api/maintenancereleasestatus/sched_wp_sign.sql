/**************************************************
****** INSERT SCRIPT FOR TABLE SCHED_WP_SIGN ******
***************************************************/
-- create a sched_wp_sign record for the sched_wp_sign_req
INSERT INTO
   sched_wp_sign (sign_req_db_id, sign_req_id, hr_db_id, hr_id,signoff_dt,rstat_cd)
SELECT
   sign_req.sign_req_db_id,
   sign_req.sign_req_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   TO_DATE('2018/05/03 08:02:44', 'yyyy/mm/dd hh24:mi:ss'),
   0
FROM
   (
      SELECT sched_wp_sign_req.sign_req_db_id, sched_wp_sign_req.sign_req_id, ROWNUM AS rn
      FROM
         sched_wp
         INNER JOIN evt_event ON
            sched_wp.sched_db_id = evt_event.event_db_id AND
            sched_wp.sched_id = evt_event.event_id AND
            evt_event.event_sdesc = 'Maintenance Release Status WKP'
         INNER JOIN sched_wp_sign_req ON
            sched_wp.sched_db_id = sched_wp_sign_req.sched_db_id AND
            sched_wp.sched_id = sched_wp_sign_req.sched_id
	) sign_req, 
	org_hr
WHERE 
   sign_req.rn = 1 AND
   org_hr.hr_cd = '1000090' AND
   NOT EXISTS
      (
         SELECT
            1 
         FROM 
            sched_wp_sign 
         WHERE 
			sign_req_db_id = sign_req.sign_req_db_id AND
			sign_req_id = sign_req.sign_req_id
      );
	 
INSERT INTO
   sched_wp_sign (sign_req_db_id, sign_req_id, hr_db_id, hr_id,signoff_dt,rstat_cd)
SELECT
   sign_req.sign_req_db_id,
   sign_req.sign_req_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   TO_DATE('2018/05/04 08:02:44', 'yyyy/mm/dd hh24:mi:ss'),
   0
FROM
   (
      SELECT sched_wp_sign_req.sign_req_db_id, sched_wp_sign_req.sign_req_id, ROWNUM AS rn
      FROM
         sched_wp
         INNER JOIN evt_event ON
            sched_wp.sched_db_id = evt_event.event_db_id AND
            sched_wp.sched_id = evt_event.event_id AND
            evt_event.event_sdesc = 'Maintenance Release Status WKP'
         INNER JOIN sched_wp_sign_req ON
            sched_wp.sched_db_id = sched_wp_sign_req.sched_db_id AND
            sched_wp.sched_id = sched_wp_sign_req.sched_id
	) sign_req, 
	org_hr
WHERE 
   sign_req.rn = 2 AND
   org_hr.hr_cd = '1000090' AND
   NOT EXISTS
      (
         SELECT
            1 
         FROM 
            sched_wp_sign 
         WHERE 
			sign_req_db_id = sign_req.sign_req_db_id AND
			sign_req_id = sign_req.sign_req_id
      );
			
			