--liquibase formatted sql


--changeSet UPDATE_PREREQ_DATES:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure UPDATE_PREREQ_DATES(aLIC_DB_ID in NUMBER ,
                                                         aLIC_ID    in NUMBER,
                                                         aHr_DB_ID in NUMBER default -1,
                                                         aHR_ID in NUMBER default -1
                                                         ) is

  tHr_db_id         org_hr_lic.hr_db_id%TYPE;
  tHr_id            org_hr_lic.hr_id%TYPE;
  tHr_lic_id        org_hr_lic.hr_lic_id%TYPE;
  tLic_db_id        lic_defn.lic_db_id%TYPE;
  tLic_id           lic_defn.lic_id%TYPE;
  tPrereq_expiry_dt org_hr_lic.prereq_expiry_dt%TYPE;
  tPrereq_effect_dt org_hr_lic.prereq_effect_dt%TYPE;
  --tRowCount         number(10) :=0;

BEGIN

  FOR licToUpdate in ( SELECT lic_db_id, lic_id, hr_db_id, hr_id, hr_lic_id
                        	FROM org_hr_lic
                       WHERE ((lic_db_id, lic_id) = ((aLIC_DB_ID, aLIC_ID)) or (lic_db_id, lic_id) IN
                       (
                       SELECT lic_db_id, lic_id from lic_defn_prereq where (grp_defn_db_id,grp_defn_id)
                       IN ( select grp_defn_db_id,grp_defn_id FROM grp_defn_lic WHERE (lic_db_id, lic_id)
                       =((aLIC_DB_ID,aLIC_ID))
                       ))
                       )
                       AND ((hr_db_id,hr_id) = ((aHr_DB_ID,aHR_ID)) or (aHr_DB_ID,aHR_ID)=((-1,-1)) )
                       )LOOP

    tHr_db_id  := licToUpdate.hr_db_id;
    tHr_id     := licToUpdate.hr_Id;
    tHr_lic_id := licToUpdate.Hr_Lic_Id;
    tLic_db_id := licToUpdate.lic_db_Id;
    tLic_id    := licToUpdate.lic_Id;

--SELECT count(*) INTO   tRowCount  FROM org_hr_lic ;

    SELECT
    	expire_dt, effect_dt
    INTO
    	tPrereq_expiry_dt, tPrereq_effect_dt
    FROM (SELECT min(expire_dt) as expire_dt, max(effect_dt) as effect_dt
              FROM (SELECT max(org_hr_lic.expiry_dt) as expire_dt,
                           min(org_hr_lic.effect_dt) as effect_dt
                      FROM grp_defn_lic
                     INNER JOIN org_hr_lic on (grp_defn_lic.lic_db_id,
                                               grp_defn_lic.lic_id) =
                                              ((org_hr_lic.lic_db_id,
                                               org_hr_lic.lic_id))
                     WHERE (grp_defn_db_id, grp_defn_id) in
                           (SELECT grp_defn_db_id, grp_defn_id
                              FROM lic_defn_prereq
                             WHERE (lic_db_id, lic_id) =
                                   ((tLic_db_id, tLic_id)))
                       AND (org_hr_lic.hr_db_id, org_hr_lic.hr_id) =
                           ((tHr_db_id, tHr_id))
                     GROUP BY grp_defn_lic.grp_defn_db_id,
                              grp_defn_lic.grp_defn_id));

    UPDATE org_hr_lic
       set prereq_expiry_dt = tPrereq_expiry_dt,
           prereq_effect_dt = tPrereq_effect_dt
     WHERE (hr_db_id, hr_id, hr_lic_id) = ((tHr_db_id, tHr_id, tHr_lic_id));
    --tRowCount := tRowCount + 1;

  END LOOP;

 -- aResult := tRowCount || ' row(s) Updated Successfully.';

END UPDATE_PREREQ_DATES;
/