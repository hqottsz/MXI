/**************************************************
** INSERT SCRIPT FOR TABLE "UTL_USER_ROLE"
***************************************************/

-- assign mxintegration user to ADMIN role
INSERT INTO
   utl_user_role (user_id, role_id, role_order, utl_id)
SELECT
   15, 19000, 1, 0
FROM
   dual
WHERE
   NOT EXISTS (SELECT 1 FROM utl_user_role WHERE user_id = 15 AND role_id = 19000);

-- assign mxintegration user to PURCHMAN role
INSERT INTO
   utl_user_role (user_id, role_id, role_order, utl_id)
SELECT
   15, 10019, 2, 0
FROM
   dual
WHERE
   NOT EXISTS (SELECT 1 FROM utl_user_role WHERE user_id = 15 AND role_id = 10019);

