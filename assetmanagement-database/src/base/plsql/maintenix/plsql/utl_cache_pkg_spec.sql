--liquibase formatted sql
--changeSet utl_cache_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   Package:       utl_cache_pkg
   Description:   This package is used to manipulate the cache modification time stamps

   Author:        Matt de Souza
   Created Date:  Jan 4th, 2018
*/
CREATE OR REPLACE PACKAGE UTL_CACHE_PKG IS

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
    );

	/**
		Procedure:	update_cache_tree_root_node

		Arguments:	aRootNodeName       The root node name in JBoss Cache

		Description: Updates the last modified date on a root node in JBoss Cache

      Author:  Matt de Souza
      Create:  Jan 4th, 2018
	*/
	PROCEDURE update_cache_tree_root_node(
	    aRootNodeName in VARCHAR
    );

END UTL_CACHE_PKG;
/