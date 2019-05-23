--liquibase formatted sql


--changeSet MX-17624:1 stripComments:false
INSERT INTO
   UTL_MENU_ARG
   (
      ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID
   )
   SELECT 'aRFQDbId', 1, null , 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_MENU_ARG WHERE ARG_CD = 'aRFQDbId' );    

--changeSet MX-17624:2 stripComments:false
INSERT INTO
   UTL_MENU_ARG
   (
      ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID
   )
   SELECT 'aRFQId', 1, null , 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_MENU_ARG WHERE ARG_CD = 'aRFQId' );                     