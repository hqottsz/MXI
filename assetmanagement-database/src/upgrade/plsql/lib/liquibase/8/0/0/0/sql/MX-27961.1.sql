--liquibase formatted sql


--changeSet MX-27961.1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   Create a package for accessing the CONTEXT
*/
CREATE OR REPLACE PACKAGE context_package AS
   -- define an exception for the package
   null_context_exception exception;
   pragma exception_init(null_context_exception, -20111);
   
   -- set the context
   PROCEDURE set_inv(aInvNoDbId IN NUMBER, aInvNoId IN NUMBER, aReturn OUT NUMBER);

   -- checks if context is set
   FUNCTION is_inv_set RETURN NUMBER;
   
   -- getters for the context
   FUNCTION get_inv_no_db_id RETURN NUMBER;
   FUNCTION get_inv_no_id RETURN NUMBER;

   FUNCTION get_carrier_db_id RETURN NUMBER;
   FUNCTION get_carrier_id RETURN NUMBER;
END context_package;
/

--changeSet MX-27961.1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY context_package AS
   
   -- set the current inventory key
   PROCEDURE set_inv(aInvNoDbId IN NUMBER, aInvNoId IN NUMBER, aReturn OUT NUMBER) IS
      lCarrierDbId NUMBER;
      lCarrierId NUMBER;
      CURSOR lcur_Carrier IS
         SELECT
            h_inv_inv.carrier_db_id,
            h_inv_inv.carrier_id
         FROM
            inv_inv
            INNER JOIN inv_inv h_inv_inv ON
               h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
               h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
         WHERE
            inv_inv.inv_no_db_id = aInvNoDbId AND
            inv_inv.inv_no_id = aInvNoId;
   BEGIN
      -- get inventory carrier
      OPEN lcur_Carrier;
      FETCH lcur_Carrier INTO lCarrierDbId, lCarrierId;
      IF lcur_Carrier%NOTFOUND THEN
         -- if inventory does not exist, set carrier to NULL
         lCarrierDbId := NULL;
         lCarrierId   := NULL;
      END IF;
      CLOSE lcur_Carrier;

      -- set context variables
      dbms_session.set_context(getContextName('current_inv_no'), 'inv_no_db_id', aInvNoDbId);
      dbms_session.set_context(getContextName('current_inv_no'), 'inv_no_id', aInvNoId);
      dbms_session.set_context(getContextName('current_inv_no'), 'carrier_db_id', lCarrierDbId);
      dbms_session.set_context(getContextName('current_inv_no'), 'carrier_id', lCarrierId);

      aReturn := 1;
      
      EXCEPTION
         WHEN OTHERS THEN
            aReturn := -1;
      	    application_object_pkg.SetMxiError('DEV-99999', SQLERRM);
   END set_inv;

   -- checks if the context is set
   FUNCTION is_inv_set RETURN NUMBER
   IS
      lInvNoDbId NUMBER;
   BEGIN
      -- get the db id
      lInvNoDbId := sys_context(getContextName('current_inv_no'), 'inv_no_db_id');
      
      -- if there is no value, raise an exception
      IF (lInvNoDbId IS NULL OR lInvNoDbId = '') THEN
         RETURN 0;
      ELSE
         RETURN 1;
      END IF;
   END is_inv_set;
   
   -- get the current inv_no_db_id
   FUNCTION get_inv_no_db_id RETURN NUMBER
   IS
      lInvNoDbId NUMBER;
   BEGIN
      -- get the db id
      lInvNoDbId := sys_context(getContextName('current_inv_no'), 'inv_no_db_id');
      
      -- if there is no value, raise an exception
      IF (lInvNoDbId IS NULL OR lInvNoDbId = '') THEN
         raise_application_error(-20111, 'Inventory context value cannot be null');
      END IF;
      RETURN lInvNoDbId; 
      
      EXCEPTION
         WHEN null_context_exception THEN
            RAISE;
   END get_inv_no_db_id;
   
   -- get the current inv_no_db_id
   FUNCTION get_inv_no_id RETURN NUMBER 
   IS
      lInvNoId NUMBER;
   BEGIN
      -- get the id
      lInvNoId := sys_context(getContextName('current_inv_no'), 'inv_no_id');
      
      -- if there is no value, raise an exception
      IF (lInvNoId IS NULL OR lInvNoId = '') THEN
         raise_application_error(-20111, 'Inventory context value cannot be null');
      END IF;
      RETURN lInvNoId; 
      
      EXCEPTION
         WHEN null_context_exception THEN
            RAISE;
            
   END get_inv_no_id;

   
   -- get the current carrier_db_id
   FUNCTION get_carrier_db_id RETURN NUMBER
   IS
   BEGIN
      -- get the db id
      RETURN sys_context(getContextName('current_inv_no'), 'carrier_db_id');
   END get_carrier_db_id;
   
   -- get the current carrier_db_id
   FUNCTION get_carrier_id RETURN NUMBER 
   IS
   BEGIN
      -- get the id
      RETURN sys_context(getContextName('current_inv_no'), 'carrier_id');
   END get_carrier_id;
END context_package;
/