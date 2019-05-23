--liquibase formatted sql


--changeSet getUserPrimaryOrgInfo:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************
** Function that returnst he 'primary' organization details, specifically
** the row from org_org of the 'primary'
** 0-Level
** DATE: 3-JUN-08
*********************************************/
CREATE OR REPLACE FUNCTION getUserPrimaryOrgInfo(
   aUserId NUMBER
   )
   RETURN org_org%ROWTYPE

IS
   --lDefaultOrgInfo UserDefaultOrganizationInfo;
   lOrgHr org_hr%ROWTYPE;
   lDefaultOrgHr org_org_hr%ROWTYPE;
   lDefaultOrg org_org%ROWTYPE;
BEGIN

   SELECT * INTO lOrgHr FROM org_hr
   WHERE
      org_hr.user_id = aUserId
      AND
      org_hr.rstat_cd	= 0;

   -- now I need to find the
   SELECT * INTO lDefaultOrgHr FROM org_org_hr
   WHERE
      org_org_hr.hr_db_id = lOrgHr.hr_db_id
   AND
      org_org_hr.hr_id = lOrgHr.hr_id
   AND
      org_org_hr.default_org_bool = 1;


   -- get default company
   SELECT * INTO lDefaultOrg FROM org_org
   WHERE
      org_org.org_db_id = lDefaultOrgHr.org_db_id
   AND
      org_org.org_id = lDefaultOrgHr.org_id;

   RETURN lDefaultOrg;

END getUserPrimaryOrgInfo;

/