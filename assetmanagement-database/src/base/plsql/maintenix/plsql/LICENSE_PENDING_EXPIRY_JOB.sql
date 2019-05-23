--liquibase formatted sql


--changeSet LICENSE_PENDING_EXPIRY_JOB:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure LICENSE_PENDING_EXPIRY_JOB(
aNotify_days in number default 30,
aExpiredAlertTypeId in number,
aWillExpireAlertTypeId in number,
aUtlId in number default 4650) is

  tHr_db_id        org_hr_lic.hr_db_id%TYPE;
  tHr_id           org_hr_lic.hr_id%TYPE;
  tHr_lic_id       org_hr_lic.hr_lic_id%TYPE;
  tLic_db_id       lic_defn.lic_db_id%TYPE;
  tLic_id          lic_defn.lic_id%TYPE;
  tIsAlert         number(10);
  tAlertId         number(10);
  tUserId          org_hr.user_id%TYPE;
begin
  FOR licExpired IN (SELECT org_hr_lic.hr_db_id, org_hr_lic.hr_id,
                            org_hr_lic.hr_lic_id, org_hr_lic.lic_db_id,
                            org_hr_lic.lic_id, org_hr_lic.expiry_dt,
                            org_hr.user_id, ROUND(org_hr_lic.expiry_dt - SYSDATE) AS IS_ALERT
                            FROM org_hr_lic, org_hr
                            WHERE org_hr_lic.hr_lic_status_db_id = 0
                            AND
                            org_hr_lic.hr_lic_status_cd = 'ACTV'
                            AND
                            org_hr_lic.hr_db_id = org_hr.hr_db_id AND
                            org_hr_lic.hr_id = org_hr.hr_id
                            AND
                            org_hr.rstat_cd	= 0
                            AND
                            (ROUND(org_hr_lic.expiry_dt - SYSDATE)) < aNotify_days
                     )Loop

    tHr_db_id  := licExpired.lic_db_id;
    tHr_id     := licExpired.hr_id;
    tHr_lic_id := licExpired.hr_lic_id;
    tLic_db_id := licExpired.lic_db_id;
    tLic_id    := licExpired.lic_id;
    tIsAlert   := licExpired.IS_ALERT;
    tUserId    := licExpired.user_id;

      IF(tIsAlert = 0) THEN
          recalculate_prereq_tree_status (tLic_db_id, tLic_db_id, tHr_db_id, tHr_id);
          SELECT MAX(ALERT_ID) INTO tAlertId FROM UTL_ALERT;
          INSERT INTO UTL_ALERT(ALERT_ID, ALERT_TYPE_ID, PRIORITY, ALERT_TIMESTAMP, UTL_ID) VALUES( tAlertId+1,aExpiredAlertTypeId,0,CURRENT_TIMESTAMP,aUtlId);
          INSERT INTO UTL_USER_ALERT(ALERT_ID,USER_ID,UTL_ID) VALUES(tAlertId+1,tUserId,aUtlId);
      ELSE
          SELECT MAX(ALERT_ID) INTO tAlertId FROM UTL_ALERT;
          INSERT INTO UTL_ALERT(ALERT_ID, ALERT_TYPE_ID, PRIORITY, ALERT_TIMESTAMP, UTL_ID) VALUES( tAlertId+1,aWillExpireAlertTypeId,0,CURRENT_TIMESTAMP,aUtlId);
          INSERT INTO UTL_USER_ALERT(ALERT_ID,USER_ID,UTL_ID) VALUES(tAlertId+1,tUserId,aUtlId);
      END IF;
   END LOOP;
end LICENSE_PENDING_EXPIRY_JOB;
/