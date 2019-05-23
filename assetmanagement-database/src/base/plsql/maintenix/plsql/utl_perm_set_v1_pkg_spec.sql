--liquibase formatted sql


--changeSet utl_perm_set_v1_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**
 This is a versioned API for utl_perm_set to assist with upgrade scripts.
 */
CREATE OR REPLACE PACKAGE utl_perm_set_v1_pkg IS

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
      aDescription IN utl_perm_set.description%TYPE );

   /**
    Associates an action parameter to a role

    Arguments:
       aPermSetId the permission set's code
       aParmName  the action parameter name
    */
   PROCEDURE AssociateActionParameter(
      aPermSetId IN utl_perm_set.id%TYPE,
      aParmName  IN utl_perm_set_action_parm.parm_name%TYPE );

   /**
    Dissociates an action parameter from a role

    Arguments:
       aPermSetId the permission set's code
       aParmName  the parameter name
    */
   PROCEDURE DissociateActionParameter(
      aPermSetId IN utl_perm_set.id%TYPE,
      aParmName  IN utl_perm_set_action_parm.parm_name%TYPE );

   /**
    Deletes a permission set

    Arguments:
       aPermSetId the permission set's code
    */
   PROCEDURE DeletePermissionSet(
      aPermSetId IN utl_perm_set.id%TYPE );
END utl_perm_set_v1_pkg;
/