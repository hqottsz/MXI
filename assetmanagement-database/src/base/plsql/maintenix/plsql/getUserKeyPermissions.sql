--liquibase formatted sql


--changeSet getUserKeyPermissions:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getUserKeyPermissions(
   aUserId NUMBER,
   aCurrentUserID NUMBER)
   RETURN NUMBER

IS

   lUserOrgInfo org_org%ROWTYPE;
   lCurrentUserOrgInfo org_org%ROWTYPE;

   lMask NUMBER := 0;

   lMySubOrg NUMBER := 0;
   lAdmin NUMBER := 0;

BEGIN

   lUserOrgInfo := getUserPrimaryOrgInfo(aUserID);
   lCurrentUserOrgInfo := getUserPrimaryOrgInfo(aCurrentUserID);

   -- Does the user work for my organization?
   IF lCurrentUserOrgInfo.Org_Db_Id = lUserOrgInfo.Org_Db_Id AND lCurrentUserOrgInfo.Org_Id = lUserOrgInfo.Org_Id THEN
      lMask := lMask + 1;
   END IF;

   -- shift the mask by one bit.
   lMask := lMask * 2;

   -- Does the user work for my company?
   IF lCurrentUserOrgInfo.Company_Org_Db_Id = lUserOrgInfo.Company_Org_Db_Id AND lCurrentUserOrgInfo.Company_Org_Id = lUserOrgInfo.Company_Org_Id THEN
      lMask := lMask + 1;
   END IF;

   -- shift the mask by one bit.
   lMask := lMask * 2;

   -- Does the user work for my sub-organizations?
   SELECT
      COUNT(*) INTO lMySubOrg
   FROM
      org_org
   WHERE
      org_db_id = lUserOrgInfo.Org_Db_Id AND org_id = lUserOrgInfo.Org_Id
   START WITH
      org_org.org_db_id = lCurrentUserOrgInfo.Org_Db_Id AND
      org_org.org_id = lCurrentUserOrgInfo.Org_Id
   CONNECT BY
      org_org.nh_org_db_id = PRIOR org_org.org_db_id  AND
      org_org.nh_org_id = PRIOR org_org.org_id;

   IF lMySubOrg > 0 THEN
      lMask := lMask + 1;
   END IF;

   -- shift the mask by one bit.
   lMask := lMask * 2;

   -- What type of company do I work for - specifically ADMIN?
   SELECT
      COUNT(*) INTO lAdmin
   FROM
      org_org
   WHERE
      org_org.org_db_id = lCurrentUserOrgInfo.Org_Db_Id AND
      org_org.org_id = lCurrentUserOrgInfo.Org_Id
   AND
      org_org.org_type_db_id = 0 AND
      org_org.org_type_cd = 'ADMIN';

   IF lAdmin > 0 THEN
      lMask := lMask + 1;
   END IF;

   RETURN lMask;

END getUserKeyPermissions;

/