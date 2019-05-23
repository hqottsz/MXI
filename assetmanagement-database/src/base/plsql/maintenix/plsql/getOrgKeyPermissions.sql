--liquibase formatted sql


--changeSet getOrgKeyPermissions:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getOrgKeyPermissions(
   aOrgDbId NUMBER,
   aOrgId NUMBER,
   aCurrentUserID NUMBER)
   RETURN NUMBER

IS

   lCurrentUserOrgInfo org_org%ROWTYPE;

   lOrgInfo org_org%ROWTYPE;


   lMask NUMBER := 0;
   lMySubOrg NUMBER := 0;
   lAdmin NUMBER := 0;

BEGIN

   lCurrentUserOrgInfo := getUserPrimaryOrgInfo(aCurrentUserID);

   -- get organization data for passed in organization
   SELECT
   	* INTO lOrgInfo
   FROM
   	org_org
   WHERE
   	org_org.org_db_id = aOrgDbId AND
   	org_org.org_id = aOrgId;

   -- Is it my organization?
   IF lCurrentUserOrgInfo.Org_Db_Id = aOrgDbId AND lCurrentUserOrgInfo.Org_Id = aOrgId THEN
      lMask := lMask + 1;
   END IF;

   -- shift the mask by one bit.
   lMask := lMask * 2;

   -- It it my company?
   -- CHECK TO SEE IF THIS ORGID IS MY COMPANY, AND IF NOT, CHECK TO SEE IF ORG's COMPANY IS MY COMPANY.
   IF (lCurrentUserOrgInfo.Company_Org_Db_Id = aOrgDbId AND lCurrentUserOrgInfo.Company_Org_Id = aOrgId) OR
      (lCurrentUserOrgInfo.Company_Org_Db_Id = lOrgInfo.Company_Org_Db_Id AND lCurrentUserOrgInfo.Company_Org_Id = lOrgInfo.Company_Org_Id) THEN
      lMask := lMask + 1;
   END IF;

   -- shift the mask by one bit.
   lMask := lMask * 2;

   -- Is this my sub-organizations?
   SELECT
      COUNT(*) INTO lMySubOrg
   FROM
      org_org
   WHERE
      org_db_id = aOrgDbId AND org_id = aOrgId
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

END getOrgKeyPermissions;

/