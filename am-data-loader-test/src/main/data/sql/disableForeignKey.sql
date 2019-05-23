BEGIN

   FOR i IN (SELECT constraint_name, table_name FROM user_constraints WHERE constraint_type IN ('R','C') AND status='ENABLED')
   LOOP
      dbms_utility.exec_ddl_statement('ALTER TABLE '||i.table_name||' DISABLE CONSTRAINT '|| '"' || i.constraint_name|| '"');
   END LOOP;

END;

/