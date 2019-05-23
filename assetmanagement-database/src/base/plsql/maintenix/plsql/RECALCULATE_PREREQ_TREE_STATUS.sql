--liquibase formatted sql


--changeSet RECALCULATE_PREREQ_TREE_STATUS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure RECALCULATE_PREREQ_TREE_STATUS(aLic_db_id in lic_defn.lic_db_id%TYPE,
                                                           aLic_id    in lic_defn.lic_id%TYPE,
                                                           aHr_db_id  in org_hr_lic.hr_db_id%TYPE,
                                                           aHr_id     in org_hr_lic.hr_id%TYPE

                                                         ) as
  tLic_cd            lic_defn.lic_cd%TYPE;
  tLic_status_cd     lic_defn.lic_status_cd%TYPE;
  tHr_db_id          org_hr_lic.hr_db_id%TYPE;
  tHr_id             org_hr_lic.hr_id%TYPE;
  tHr_lic_id         org_hr_lic.hr_lic_id%TYPE;
  tLic_db_id         lic_defn.lic_db_id%TYPE;
  tLic_id            lic_defn.lic_id%TYPE;
  tExpiry_dt         org_hr_lic.expiry_dt%TYPE;
  tEffect_dt         org_hr_lic.effect_dt%TYPE;
  tPrereq_expiry_dt  org_hr_lic.prereq_expiry_dt%TYPE;
  tPrereq_effect_dt  org_hr_lic.prereq_effect_dt%TYPE;
  tHr_status_db_id   org_hr_lic.hr_lic_status_db_id%TYPE;
  tHr_status_cd      org_hr_lic.hr_lic_status_cd%TYPE;
  tMissingPrereqNote varchar2(1024);
  tPrintCardAgain    Integer(1);
  tStatusDbId        Integer(10);
  tStatusCd          varchar2(30);
  tStageReasonDbId   Integer(10);
  tStageReasonCd     varchar2(30);
  tStage_note        org_hr_lic.stage_note%TYPE;
  tRowCount          number(10) := 0;

  CURSOR licToUpdate IS
      (SELECT org_hr_lic.hr_db_id,
             org_hr_lic.hr_id,
             org_hr_lic.hr_lic_id,
             org_hr_lic.lic_db_id,
             org_hr_lic.lic_id,
             org_hr_lic.expiry_dt,
             org_hr_lic.effect_dt,
             org_hr_lic.prereq_expiry_dt,
             org_hr_lic.prereq_effect_dt,
             org_hr_lic.hr_lic_status_db_id,
             org_hr_lic.hr_lic_status_cd,
             lic_defn.lic_cd,
             lic_defn.lic_status_cd,
             lic_defn.print_oncard_bool,
             search_missing_prerequisite(aLic_db_id, aLic_id, aHr_DB_ID, aHr_id) AS missingPrereqNote
      FROM org_hr_lic,
           lic_defn
      WHERE (org_hr_lic.lic_db_id, org_hr_lic.lic_id) = ((aLic_db_id, aLic_id))
            AND
            (org_hr_lic.hr_db_id, org_hr_lic.hr_id) = ((aHr_DB_ID, aHr_id))
            AND
            (org_hr_lic.lic_db_id, org_hr_lic.lic_id) = ((lic_defn.lic_db_id, lic_defn.lic_id))
            AND
            (org_hr_lic.hr_lic_status_db_id,org_hr_lic.hr_lic_status_cd) != (('0','SUSPEND')));

   TYPE licToUpdateTable IS TABLE OF licToUpdate%ROWTYPE INDEX BY PLS_INTEGER;
   l_licToUpdateTable   licToUpdateTable;
   l_licToUpdateParent   licToUpdateTable;

   --Holder for LIC ID AND LIC DB_ID
   TYPE licDbIdOfHR IS TABLE OF lic_defn.lic_db_id%TYPE;
   TYPE licIdOfHR IS TABLE OF lic_defn.lic_id%TYPE;

   l_licDbIdOfHR licDbIdOfHR;
   l_licIdOfHR licIdOfHR;

BEGIN
  -- Call update prerequisite expiry and effective date procedure
  UPDATE_PREREQ_DATES(aLic_db_id, aLic_id,aHr_DB_ID ,aHr_id);

  OPEN licToUpdate;
   LOOP
   FETCH licToUpdate BULK COLLECT INTO l_licToUpdateTable;
      FOR indx IN 1 .. l_licToUpdateTable.COUNT LOOP
          tHr_db_id          := l_licToUpdateTable(indx).hr_db_Id;
          tHr_id             := l_licToUpdateTable(indx).hr_Id;
          tHr_lic_id         := l_licToUpdateTable(indx).Hr_Lic_Id;
          tLic_db_id         := l_licToUpdateTable(indx).lic_db_Id;
          tLic_id            := l_licToUpdateTable(indx).lic_Id;
          tLic_cd            := l_licToUpdateTable(indx).lic_cd;
          tLic_status_cd     := l_licToUpdateTable(indx).lic_status_cd;
          tEffect_dt         := l_licToUpdateTable(indx).effect_dt;
          tExpiry_dt         := l_licToUpdateTable(indx).expiry_dt;
          tPrereq_effect_dt  := l_licToUpdateTable(indx).prereq_effect_dt;
          tPrereq_expiry_dt  := l_licToUpdateTable(indx).prereq_expiry_dt;
          tHr_status_db_id   := l_licToUpdateTable(indx).hr_lic_status_db_id;
          tHr_status_cd      := l_licToUpdateTable(indx).hr_lic_status_cd;
          tMissingPrereqNote := l_licToUpdateTable(indx).missingprereqnote;
          tPrintCardAgain    := l_licToUpdateTable(indx).print_oncard_bool;
          tRowCount          := 0;

      IF(tLic_status_cd <> 'OBSLT') THEN
          IF(tMissingPrereqNote IS NOT NULL) THEN
            tStage_note      := NULL;
            tStatusDbId      := 0;
            tStatusCd        := 'INVALID';
            tStageReasonDbId := 0;
            tStageReasonCd   := 'NOTCMPLT';
          ELSIF tEffect_dt > CURRENT_DATE THEN
            tStage_note      := NULL;
            tStatusDbId      := 0;
            tStatusCd        := 'INVALID';
            tStageReasonDbId := 0;
            tStageReasonCd   := 'UNEFF';
          ELSIF tPrereq_effect_dt > CURRENT_DATE THEN
            tStage_note      := NULL;
            tStatusDbId      := 0;
            tStatusCd        := 'INVALID';
            tStageReasonDbId := 0;
            tStageReasonCd   := 'PRQUNEFF';
          ELSIF NVL (tExpiry_dt, SYSDATE + 1) < CURRENT_DATE THEN
            tStage_note      := NULL;
            tStatusDbId      := 0;
            tStatusCd        := 'INVALID';
            tStageReasonDbId := 0;
            tStageReasonCd   := 'EXP';
          ELSIF tPrereq_expiry_dt < CURRENT_DATE THEN
            tStage_note      := NULL;
            tStatusDbId      := 0;
            tStatusCd        := 'INVALID';
            tStageReasonDbId := 0;
            tStageReasonCd   := 'PRQEXP';
          ELSE
            tStage_note      := NULL;
            tStatusDbId      := 0;
            tStatusCd        := 'ACTV';
            tStageReasonDbId := 0;
            tStageReasonCd   := 'READY';
          END IF;

          --update org_hr_lic table
          IF (tStage_note IS NULL) THEN
            IF(aHr_DB_ID IS NOT NULL) THEN
               UPDATE org_hr_lic
                 SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                     (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                     (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                     (org_hr_lic.stage_reason_cd) = (tStageReasonCd)
                 WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                       AND
                       org_hr_lic.lic_id = (tLic_Id)
                       AND
                       org_hr_lic.hr_db_id = aHr_DB_ID
                       AND
                       org_hr_lic.hr_id = aHr_id;
            ELSE
               UPDATE org_hr_lic
                 SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                     (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                     (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                     (org_hr_lic.stage_reason_cd) = (tStageReasonCd)
                 WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                       AND
                       org_hr_lic.lic_id = (tLic_Id);
            END IF;

          ELSE
            IF(aHr_DB_ID IS NOT NULL) THEN
               UPDATE org_hr_lic
                 SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                     (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                     (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                     (org_hr_lic.stage_reason_cd) = (tStageReasonCd),
                     (org_hr_lic.stage_note) = (tStage_note)
                 WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                       AND
                       org_hr_lic.lic_id = (tLic_Id)
                       AND
                       org_hr_lic.hr_db_id = aHr_DB_ID
                       AND
                       org_hr_lic.hr_id = aHr_id;
            ELSE
               UPDATE org_hr_lic
                 SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                     (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                     (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                     (org_hr_lic.stage_reason_cd) = (tStageReasonCd),
                     (org_hr_lic.stage_note) = (tStage_note)
                 WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                       AND
                       org_hr_lic.lic_id = (tLic_Id);
            END IF;

          END IF;
        END IF;
      END LOOP;
      EXIT WHEN licToUpdate%NOTFOUND;
   END LOOP;
   CLOSE licToUpdate;

--For rest of the parents
SELECT HR.LIC_ID, HR.LIC_DB_ID BULK COLLECT INTO l_licIdOfHR, l_licDbIdOfHR
            FROM
            (SELECT LIC_KEY,NODE,LEVEL AS D_LEVEL
              FROM(
               SELECT
               LIC_DEFN_PREREQ.LIC_DB_ID || ':' || LIC_DEFN_PREREQ.LIC_ID AS LIC_KEY,
               GRP_DEFN_LIC.LIC_DB_ID || ':' || GRP_DEFN_LIC.LIC_ID AS NODE
               FROM
               LIC_DEFN_PREREQ,GRP_DEFN_LIC
               WHERE
               GRP_DEFN_LIC.GRP_DEFN_DB_ID = LIC_DEFN_PREREQ.GRP_DEFN_DB_ID AND
               GRP_DEFN_LIC.GRP_DEFN_ID = LIC_DEFN_PREREQ.GRP_DEFN_ID)
               CONNECT BY PRIOR LIC_KEY = NODE
               START WITH NODE = aLic_db_id || ':' || aLic_id)NEW_TREE,
               (SELECT
               ORG_HR_LIC.LIC_DB_ID || ':' || ORG_HR_LIC.LIC_ID AS LIC_KEY,
               ORG_HR_LIC.*
               FROM ORG_HR_LIC
               WHERE
               ORG_HR_LIC.HR_DB_ID = aHr_DB_ID AND
               ORG_HR_LIC.HR_ID = aHr_id)HR,LIC_DEFN
            WHERE
            HR.LIC_KEY = NEW_TREE.LIC_KEY
            AND
            (hr.lic_db_id, HR.lic_id) = ((lic_defn.lic_db_id, lic_defn.lic_id))
            AND
            (HR.hr_lic_status_db_id,HR.hr_lic_status_cd) != (('0','SUSPEND'))
            ORDER BY NEW_TREE.D_LEVEL;
  --Itrate with for
  IF(l_licIdOfHR.COUNT > 0) THEN
  FOR i IN l_licIdOfHR.FIRST .. l_licIdOfHR.LAST LOOP
               SELECT org_hr_lic.hr_db_id,
                      org_hr_lic.hr_id,
                      org_hr_lic.hr_lic_id,
                     org_hr_lic.lic_db_id,
                     org_hr_lic.lic_id,
                     org_hr_lic.expiry_dt,
                     org_hr_lic.effect_dt,
                     org_hr_lic.prereq_expiry_dt,
                     org_hr_lic.prereq_effect_dt,
                     org_hr_lic.hr_lic_status_db_id,
                     org_hr_lic.hr_lic_status_cd,
                     lic_defn.lic_cd,
                     lic_defn.lic_status_cd,
                     lic_defn.print_oncard_bool,
                     search_missing_prerequisite(l_licDbIdOfHR(i), l_licIdOfHR(i), aHr_DB_ID, aHr_id) AS missingPrereqNote
                BULK COLLECT INTO l_licToUpdateParent
                FROM org_hr_lic, lic_defn
               WHERE (org_hr_lic.lic_db_id, org_hr_lic.lic_id) = ((l_licDbIdOfHR(i), l_licIdOfHR(i)))
                     AND
                     (org_hr_lic.hr_db_id, org_hr_lic.hr_id) = ((aHr_DB_ID, aHr_id))
                     AND
                     (org_hr_lic.lic_db_id, org_hr_lic.lic_id) = ((lic_defn.lic_db_id, lic_defn.lic_id));

             FOR j IN l_licToUpdateParent.FIRST .. l_licToUpdateParent.LAST LOOP

                        tHr_db_id          := l_licToUpdateParent(j).hr_db_Id;
                        tHr_id             := l_licToUpdateParent(j).hr_Id;
                        tHr_lic_id         := l_licToUpdateParent(j).Hr_Lic_Id;
                        tLic_db_id         := l_licToUpdateParent(j).lic_db_Id;
                        tLic_id            := l_licToUpdateParent(j).lic_Id;
                        tLic_cd            := l_licToUpdateParent(j).lic_cd;
                        tLic_status_cd     := l_licToUpdateParent(j).lic_status_cd;
                        tEffect_dt         := l_licToUpdateParent(j).effect_dt;
                        tExpiry_dt         := l_licToUpdateParent(j).expiry_dt;
                        tPrereq_effect_dt  := l_licToUpdateParent(j).prereq_effect_dt;
                        tPrereq_expiry_dt  := l_licToUpdateParent(j).prereq_expiry_dt;
                        tHr_status_db_id   := l_licToUpdateParent(j).hr_lic_status_db_id;
                        tHr_status_cd      := l_licToUpdateParent(j).hr_lic_status_cd;
                        tMissingPrereqNote := l_licToUpdateParent(j).missingprereqnote;
                        tPrintCardAgain    := l_licToUpdateParent(j).print_oncard_bool;
                        tRowCount          := 0;

                    IF(tLic_status_cd <> 'OBSLT') THEN
                        IF (tMissingPrereqNote IS NOT NULL) THEN
                          tStage_note      :=  NULL;
                          tStatusDbId      := 0;
                          tStatusCd        := 'INVALID';
                          tStageReasonDbId := 0;
                          tStageReasonCd   := 'NOTCMPLT';
                        ELSIF tEffect_dt > CURRENT_DATE THEN
                          tStage_note      := NULL;
                          tStatusDbId      := 0;
                          tStatusCd        := 'INVALID';
                          tStageReasonDbId := 0;
                          tStageReasonCd   := 'UNEFF';
                        ELSIF tPrereq_effect_dt > CURRENT_DATE THEN
                          tStage_note      := NULL;
                          tStatusDbId      := 0;
                          tStatusCd        := 'INVALID';
                          tStageReasonDbId := 0;
                          tStageReasonCd   := 'PRQUNEFF';
                        ELSIF NVL (tExpiry_dt, SYSDATE + 1) < CURRENT_DATE THEN
                          tStage_note      := NULL;
                          tStatusDbId      := 0;
                          tStatusCd        := 'INVALID';
                          tStageReasonDbId := 0;
                          tStageReasonCd   := 'EXP';
                        ELSIF tPrereq_expiry_dt < CURRENT_DATE THEN
                          tStage_note      := NULL;
                          tStatusDbId      := 0;
                          tStatusCd        := 'INVALID';
                          tStageReasonDbId := 0;
                          tStageReasonCd   := 'PRQEXP';
                        ELSE
                          tStage_note      := NULL;
                          tStatusDbId      := 0;
                          tStatusCd        := 'ACTV';
                          tStageReasonDbId := 0;
                          tStageReasonCd   := 'READY';
                        END IF;

                 --update org_hr_lic table

                    IF (tStage_note IS NULL) THEN
                      IF(aHr_DB_ID IS NOT NULL) THEN
                         UPDATE org_hr_lic
                           SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                               (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                               (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                               (org_hr_lic.stage_reason_cd) = (tStageReasonCd)
                           WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                                 AND
                                 org_hr_lic.lic_id = (tLic_Id)
                                 AND
                                 org_hr_lic.hr_db_id = aHr_DB_ID
                                 AND
                                 org_hr_lic.hr_id = aHr_id;
                      ELSE
                         UPDATE org_hr_lic
                           SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                               (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                               (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                               (org_hr_lic.stage_reason_cd) = (tStageReasonCd)
                           WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                                 AND
                                 org_hr_lic.lic_id = (tLic_Id);
                      END IF;
                    ELSE
                      IF(aHr_DB_ID IS NOT NULL) THEN
                         UPDATE org_hr_lic
                           SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                               (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                               (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                               (org_hr_lic.stage_reason_cd) = (tStageReasonCd),
                               (org_hr_lic.stage_note) = (tStage_note)
                           WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                                 AND
                                 org_hr_lic.lic_id = (tLic_Id)
                                 AND
                                 org_hr_lic.hr_db_id = aHr_DB_ID
                                 AND
                                 org_hr_lic.hr_id = aHr_id;
                      ELSE
                         UPDATE org_hr_lic
                           SET (org_hr_lic.hr_lic_status_db_id) = (tStatusDbId),
                               (org_hr_lic.hr_lic_status_cd) = (tStatusCd),
                               (org_hr_lic.stage_reason_db_id) = (tStageReasonDbId),
                               (org_hr_lic.stage_reason_cd) = (tStageReasonCd),
                               (org_hr_lic.stage_note) = (tStage_note)
                           WHERE org_hr_lic.lic_db_id = (tLic_db_id)
                                 AND
                                 org_hr_lic.lic_id = (tLic_Id);
                      END IF;
                    END IF;
                END IF;
              END LOOP;
  END LOOP;
  END IF;

   --update org_hr

          UPDATE org_hr
             SET (org_hr.lic_card_change_dt) = (current_date)
           WHERE (org_hr.hr_db_id, org_hr.hr_id) = ((aHr_db_id, aHr_id))
                 AND
                 org_hr.rstat_cd = 0 ;

END recalculate_prereq_tree_status;
/