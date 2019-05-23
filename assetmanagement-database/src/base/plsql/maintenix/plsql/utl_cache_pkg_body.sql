--liquibase formatted sql


--changeSet utl_cache_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   Package:       utl_cache_pkg
   Description:   This package is used to manipulate the cache modification time stamps

   Author:        Matt de Souza
   Created Date:  Jan 4th, 2018
*/
CREATE OR REPLACE PACKAGE BODY UTL_CACHE_PKG IS

	/**
		Procedure:	update_cache_tree_node

		Arguments:	aRootNodeName       The root node name in JBoss Cache
                    aNodeName           The node name in JBoss Cache

		Description: Updates the last modified date on a node in JBoss Cache

      Author:  Matt de Souza
      Create:  Jan 4th, 2018
	*/
	PROCEDURE update_cache_tree_node(
	    aRootNodeName in VARCHAR,
		aNodeName in VARCHAR
    ) AS
    PRAGMA AUTONOMOUS_TRANSACTION;
    BEGIN
       MERGE INTO utl_cache DST
       USING ( SELECT aRootNodeName AS root_node_name, aNodeName AS node_name FROM dual ) src
       ON (dst.node_name = src.node_name AND
           dst.root_node_name = src.root_node_name)
       WHEN MATCHED THEN UPDATE SET dst.last_modified_dt = sysdate
       WHEN NOT MATCHED THEN INSERT (dst.root_node_name, dst.node_name, dst.last_modified_dt)
                             VALUES (src.root_node_name, src.node_name, sysdate);
       COMMIT;
    EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
       UPDATE utl_cache
       SET last_modified_dt = sysdate
       WHERE root_node_name = aRootNodeName AND
             node_name = aNodeName;
       COMMIT;
    WHEN OTHERS THEN
       ROLLBACK;
    END update_cache_tree_node;

	/**
		Procedure:	update_cache_tree_root_node

		Arguments:	aRootNodeName      The root node name in JBoss Cache

		Description: Updates the last modified date on a root node in JBoss Cache

      Author:  Matt de Souza
      Create:  Jan 4th, 2018
	*/
	PROCEDURE update_cache_tree_root_node(
	    aRootNodeName in VARCHAR
    ) AS
    PRAGMA AUTONOMOUS_TRANSACTION;
    BEGIN
       MERGE INTO utl_cache DST
       USING ( SELECT aRootNodeName AS root_node_name, NULL AS node_name FROM dual ) SRC
       ON (dst.node_name = src.node_name AND
           dst.root_node_name = src.root_node_name)
       WHEN MATCHED THEN UPDATE SET dst.last_modified_dt = sysdate
       WHEN NOT MATCHED THEN INSERT (dst.root_node_name, dst.node_name, dst.last_modified_dt)
                         VALUES (src.root_node_name, src.node_name, sysdate);
       COMMIT;
    EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
       UPDATE utl_cache
       SET LAST_MODIFIED_DT = sysdate
       WHERE root_node_name = aRootNodeName AND
             node_name IS NULL;
       COMMIT;
    WHEN OTHERS THEN
       ROLLBACK;
    END update_cache_tree_root_node;

END UTL_CACHE_PKG;
/