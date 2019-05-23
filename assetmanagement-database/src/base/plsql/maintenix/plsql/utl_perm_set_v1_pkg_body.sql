--liquibase formatted sql


--changeSet utl_perm_set_v1_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**
 This is a versioned API for utl_perm_set to assist with upgrade scripts.
 */
CREATE OR REPLACE PACKAGE BODY utl_perm_set_v1_pkg IS

   /**
    Creates a permission set

    Arguments:
       aPermSetId   the permission set's code
       aCategory    the category
       aLabel       the label
       aDescription the description
   */
   PROCEDURE CreatePermissionSet(
      aPermSetId   IN utl_perm_set.id%TYPE,
      aCategory    IN utl_perm_set.category%TYPE,
      aLabel       IN utl_perm_set.label%TYPE,
      aDescription IN utl_perm_set.description%TYPE )
   IS
   BEGIN
      INSERT INTO utl_perm_set (id, category, label, description)
      SELECT
         aPermSetId,
         aCategory,
         aLabel,
         aDescription
      FROM
         dual
      WHERE
         NOT EXISTS(
            SELECT 1 FROM utl_perm_set
            WHERE
               utl_perm_set.id = aPermSetId
         );

      IF sql%rowcount <> 0 THEN
         DBMS_OUTPUT.put_line('INFO: The permission set '''|| aPermSetId ||''' was successfuly added.');
      ELSE
         DBMS_OUTPUT.put_line('INFO: The permission set '''|| aPermSetId ||''' already exists, therefore, it was not added.');
      END IF;
   END CreatePermissionSet;

   /**
    Associates an action parameter to a role

    Arguments:
       aPermSetId the permission set's code
       aParmName  the action parameter name
    */
   PROCEDURE AssociateActionParameter(
      aPermSetId IN utl_perm_set.id%TYPE,
      aParmName  IN utl_perm_set_action_parm.parm_name%TYPE )
   IS
      lPermSetExists NUMBER;
      lParmExists    NUMBER;
      lDefaultValue  utl_action_config_parm.parm_value%TYPE;
   BEGIN
      SELECT COUNT(1) INTO lPermSetExists FROM utl_perm_set WHERE id = aPermSetId;
      IF lPermSetExists = 1 THEN
         SELECT COUNT(1) INTO lParmExists FROM utl_action_config_parm WHERE parm_name = aParmName;
         IF lParmExists = 1 THEN
            SELECT parm_value INTO lDefaultValue FROM utl_action_config_parm WHERE parm_name = aParmName;

            IF upper(lDefaultValue) = 'FALSE'
            THEN
               INSERT INTO utl_perm_set_action_parm (perm_set_id, parm_name, parm_value)
               SELECT
                  aPermSetId,
                  aParmName,
                  'true'
               FROM
                  dual
               WHERE
                  NOT EXISTS(
                     SELECT 1 FROM utl_perm_set_action_parm
                     WHERE
                        perm_set_id   = aPermSetId AND
                        parm_name = aParmName
                  );
            ELSE
               DELETE FROM utl_perm_set_action_parm
               WHERE
                  perm_set_id = aPermSetId AND
                  parm_name   = aParmName;
            END IF;

            IF sql%rowcount <> 0 THEN
               DBMS_OUTPUT.put_line('INFO: The action parameter '''|| aParmName ||''' was successfully added to the permission set '''|| aPermSetId ||'''.');
            ELSE
               DBMS_OUTPUT.put_line('INFO: The action parameter '''|| aParmName ||''' was was already added to the permission set '''|| aPermSetId ||''', therefore, it could not be added.');
            END IF;
         ELSE
            DBMS_OUTPUT.put_line('INFO: The action parameter '''|| aParmName ||''' does not exists, therefore, it could not be added to the permission set '''|| aPermSetId ||'''.');
         END IF;
      ELSE
         DBMS_OUTPUT.put_line('INFO: The permission set '''|| aPermSetId ||''' does not exist, therefore, the action parameter '''|| aParmName ||''' was not added.');
      END IF;
   END AssociateActionParameter;

   /**
    Dissociates an action parameter from a role

    Arguments:
       aPermSetId the permission set's code
       aParmName  the parameter name
    */
   PROCEDURE DissociateActionParameter(
      aPermSetId IN utl_perm_set.id%TYPE,
      aParmName  IN utl_perm_set_action_parm.parm_name%TYPE )
   IS
      lPermSetExists   NUMBER;
      lParmExists   NUMBER;
      lDefaultValue utl_action_config_parm.parm_value%TYPE;
   BEGIN
      SELECT COUNT(1) INTO lPermSetExists FROM utl_perm_set WHERE id = aPermSetId;
      IF lPermSetExists = 1 THEN
         SELECT COUNT(1) INTO lParmExists FROM utl_action_config_parm WHERE parm_name = aParmName;
         IF lParmExists = 1 THEN
            SELECT parm_value INTO lDefaultValue FROM utl_action_config_parm WHERE parm_name = aParmName;

            IF upper(lDefaultValue) = 'FALSE'
            THEN
               DELETE FROM utl_perm_set_action_parm
               WHERE
                  perm_set_id = aPermSetId AND
                  parm_name   = aParmName;
            ELSE
               INSERT INTO utl_perm_set_action_parm (perm_set_id, parm_name, parm_value)
               SELECT
                  aPermSetId,
                  aParmName,
                  'false'
               FROM
                  dual
               WHERE
                  NOT EXISTS(
                     SELECT 1 FROM utl_perm_set_action_parm
                     WHERE
                        perm_set_id = aPermSetId AND
                        parm_name   = aParmName
                  );
            END IF;

            IF sql%rowcount <> 0 THEN
               DBMS_OUTPUT.put_line('INFO: The action parameter '''|| aParmName ||''' was successfully removed to the permission set '''|| aPermSetId ||'''.');
            ELSE
               DBMS_OUTPUT.put_line('INFO: The action parameter '''|| aParmName ||''' was was already removed to the permission set '''|| aPermSetId ||''', therefore, it could not be removed.');
            END IF;
         ELSE
            DBMS_OUTPUT.put_line('INFO: The action parameter '''|| aParmName ||''' does not exists, therefore, it could not be removed from the permission set '''|| aPermSetId ||'''.');
         END IF;
      ELSE
         DBMS_OUTPUT.put_line('INFO: The permission set '''|| aPermSetId ||''' does not exist, therefore, the action parameter '''|| aParmName ||''' was not removed.');
      END IF;
   END DissociateActionParameter;

   /**
    Deletes a permission set

    Arguments:
       aPermSetId the permission set's code
    */
   PROCEDURE DeletePermissionSet(
      aPermSetId IN utl_perm_set.id%TYPE )
   IS
      lPermSetExists   NUMBER;
   BEGIN
      SELECT COUNT(1) INTO lPermSetExists FROM utl_perm_set WHERE id = aPermSetId;
      IF lPermSetExists = 1 THEN
         DELETE FROM
            utl_perm_set_action_parm
         WHERE
            perm_set_id = aPermSetId;

         DELETE FROM
            utl_perm_set
         WHERE
           id = aPermSetId;

         DBMS_OUTPUT.put_line('INFO: The permission set '''|| aPermSetId ||''' was successfuly deleted.');
      ELSE
         DBMS_OUTPUT.put_line('INFO: The permission set '''|| aPermSetId ||''' does not exist, therefore, it was not deleted.');
      END IF;
   END DeletePermissionSet;
END utl_perm_set_v1_pkg;
/