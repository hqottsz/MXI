--liquibase formatted sql


--changeSet SEARCH_MISSING_PREREQUISITE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace function SEARCH_MISSING_PREREQUISITE(aLic_db_id in lic_defn.lic_db_id%TYPE,
                                                       aLic_id    in lic_defn.lic_id%TYPE,
                                                       aHr_db_id  in org_hr_lic.hr_db_id%TYPE,
                                                       aHr_id     in org_hr_lic.hr_id%TYPE

                                                       ) return varchar2 as

  sLast               VARCHAR2(64);
  missingPrerequisite varchar2(1024);
BEGIN
  sLast := '<NULL>';
  For grp in (Select grp_defn_lic.grp_defn_db_id || ':' ||
                     grp_defn_lic.grp_defn_id as grpkey,
                     (select lic_cd
                        from lic_defn
                       where (lic_db_id, lic_id) =
                             ((grp_defn_lic.lic_db_id, grp_defn_lic.lic_id))) as liccode
                from grp_defn_lic
               Where (grp_defn_lic.grp_defn_db_id, grp_defn_lic.grp_defn_id) IN
                     (select grp_defn_db_id, grp_defn_id
                        from lic_defn_prereq
                       Where (lic_db_id, lic_id) = ((aLic_db_id, aLic_id)))
                 AND (grp_defn_lic.grp_defn_db_id, grp_defn_lic.grp_defn_id) NOT IN
                     (select grp_defn_db_id, grp_defn_id
                        FROM org_hr_lic,grp_defn_lic
                       where (hr_db_id, hr_id) = ((aHr_db_id, aHr_id))
                       and (grp_defn_lic.lic_db_id,grp_defn_lic.lic_id) = ((org_hr_lic.lic_db_id, org_hr_lic.lic_id))
                         and hr_lic_status_cd = 'ACTV')
                 AND (0,'ACTV') IN ((SELECT lic_status_db_id, lic_status_cd
                                     FROM lic_defn
                                     WHERE (lic_db_id, lic_id) = ((grp_defn_lic.lic_db_id, grp_defn_lic.lic_id))))
               order by grpkey) LOOP
    if (grp.grpkey = sLast) then
      missingPrerequisite := missingPrerequisite || ' or ' || grp.liccode;
    else
      if (sLast <> '<NULL>') then
        missingPrerequisite := missingPrerequisite || ' ) AND ';
      end if;
      missingPrerequisite := missingPrerequisite || '( ' || grp.liccode;
      sLast               := grp.grpkey;

    end if;
  END LOOP;

  if (sLast <> '<NULL>') then
    missingPrerequisite := missingPrerequisite || ' )';
  else
    missingPrerequisite := null;
  end if;

  return missingPrerequisite;
end SEARCH_MISSING_PREREQUISITE;
/