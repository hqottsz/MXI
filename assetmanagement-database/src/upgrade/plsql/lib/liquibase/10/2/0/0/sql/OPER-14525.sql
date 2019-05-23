--liquibase formatted sql
--changeSet OPER-14525:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet OPER-14525:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet OPER-14525:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
-- Create Cache Table
UTL_MIGR_SCHEMA_PKG.table_create ('
		CREATE TABLE UTL_CACHE
        (
          ROOT_NODE_NAME  VARCHAR2 (40) NOT NULL,
          NODE_NAME       VARCHAR2 (40),
          LAST_MODIFIED_DT         TIMESTAMP (6) NOT NULL
        )
	');
END;
/
--changeSet OPER-14525:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
-- Create Cache Table
UTL_MIGR_SCHEMA_PKG.index_create ('
		CREATE UNIQUE INDEX IX_UTLCACHE_NODE ON UTL_CACHE
        (
         ROOT_NODE_NAME ASC , NODE_NAME ASC
        )
	');
END;
/

--changeSet OPER-14525:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- com.mxi.mx.common.cache.ConfigCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_CONFIG_PARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_CONFIG_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update CacheFactory.CONFIG_GLOBAL_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','Global');
   END AFTER STATEMENT;
END "TTRK_UTL_CONFIG_PARM_CACHE";
/

--changeSet OPER-14525:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ROLE_PARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ROLE_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_ROLE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','Role');
   END AFTER STATEMENT;
END "TTRK_UTL_ROLE_PARM_CACHE";
/

--changeSet OPER-14525:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_USER_PARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_USER_PARM"
   COMPOUND TRIGGER
   AFTER EACH ROW IS
   BEGIN
      -- Update  CacheFactory.CONFIG_USER_NODE_NAME cache timestamp
      CASE
      WHEN INSERTING THEN
         -- Ignore Session Parm Changes as they are not configuration
         IF (:NEW.PARM_TYPE <> 'SESSION') THEN
            UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','User');
         END IF;
      WHEN UPDATING THEN
         -- Ignore Session Parm Changes as they are not configuration
         IF (:NEW.PARM_TYPE <> 'SESSION') THEN
            UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','User');
         END IF;
      WHEN DELETING THEN
         -- Ignore Session Parm Changes as they are not configuration
         IF (:OLD.PARM_TYPE <> 'SESSION') THEN
            UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','User');
         END IF;
      END CASE;
   END AFTER EACH ROW;
END "TTRK_UTL_USER_PARM_CACHE";
/

--changeSet OPER-14525:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ACTION_CONFIG_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ACTION_CONFIG_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_GLOBAL_ACTION_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','GlobalAction');
   END AFTER STATEMENT;
END "TTRK_UTL_ACTION_CONFIG_CACHE";
/

--changeSet OPER-14525:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ACTIONROLEPARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ACTION_ROLE_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_ROLE_ACTION_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','RoleAction');
   END AFTER STATEMENT;
END "TTRK_UTL_ACTIONROLEPARM_CACHE";
/

--changeSet OPER-14525:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ACTIONUSERPARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ACTION_USER_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_USER_ACTION_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','UserAction');
   END AFTER STATEMENT;
END "TTRK_UTL_ACTIONUSERPARM_CACHE";
/

--changeSet OPER-14525:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.common.alert.AlertEngineCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_ALERT_TYPE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ALERT_TYPE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.ALERT_TYPE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertTypes');
      -- Update  CacheFactory.ALERT_NOTIFICATION_RULE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertNotificationRules');
      -- Update  CacheFactory.ALERT_PRIORITY_CALCULATOR_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertPriorityCalculators');
   END AFTER STATEMENT;
END "TTRK_UTL_ALERT_TYPE_CACHE";
/

--changeSet OPER-14525:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ALERT_TYPE_ROLE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ALERT_TYPE_ROLE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.ALERT_TYPE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertTypes');
   END AFTER STATEMENT;
END "TTRK_UTL_ALERT_TYPE_ROLE_CACHE";
/

--changeSet OPER-14525:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.common.timezone.TimeZoneCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_TIMEZONE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_TIMEZONE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.TIME_ZONE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('Timezone');
   END AFTER STATEMENT;
END "TTRK_UTL_TIMEZONE_CACHE";
/

--changeSet OPER-14525:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.core.timezone.LocationTimeZoneCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_INV_LOC_TIMEZONECD_CACHE"
   FOR INSERT OR UPDATE OR DELETE OF TIMEZONE_CD ON "INV_LOC"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.LOCATION_TIME_ZONE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('LocationTimeZones');
   END AFTER STATEMENT;
END "TTRK_INV_LOC_TIMEZONECD_CACHE";
/

--changeSet OPER-14525:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.integration.services.transport.logging.LoggingCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_INT_BP_LOOKUP_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "INT_BP_LOOKUP"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.BP_PROCESS_LOG_MAP_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('BPProcessLogMap');
   END AFTER STATEMENT;
END "TTRK_INT_BP_LOOKUP_CACHE";
/

--changeSet OPER-14525:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.common.report.ReportTypeCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_REPORT_TYPE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_REPORT_TYPE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.REPORT_TYPE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('ReportType');
   END AFTER STATEMENT;
END "TTRK_UTL_REPORT_TYPE_CACHE";
/

--changeSet OPER-14525:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'SESSION_PARM_CACHE_INVALIDATION_INTERVAL',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'The interval (in seconds) between automatic flushing of user session parm data. A value of 0 disables this feature.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'Number',
      p_default_value    =>  '1800',
      p_mand_config_bool =>  1,
      p_category         =>  'System',
      p_modified_in      =>  '8.2-SP5',
      p_utl_id           =>  0
   );

END;
/
