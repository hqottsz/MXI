DECLARE
   
   ln_user_id  NUMBER;
   
BEGIN

    ln_user_id := sys_context('USERENV', 'SESSION_USERID') ;
    EXECUTE IMMEDIATE 'CREATE OR REPLACE CONTEXT current_inv_no' || ln_user_id ||' USING context_package';
    
EXCEPTION 
   WHEN OTHERS THEN 
      RAISE;
END;
/

   
