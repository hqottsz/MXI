--liquibase formatted sql


--changeSet MTX-220_2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
 * Confidential, proprietary and/or trade secret information of
 * Mxi Technologies, Ltd.
 *
 * Copyright 2000-2010 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed
 * officer of Mxi Technologies, Ltd., any disclosure, distribution,
 * reproduction, compilation, modification, creation of derivative works and/or
 * other use of the Mxi source code is strictly prohibited.  Inclusion of a
 * copyright notice shall not be taken to indicate that the source code has
 * been published.
 */
CREATE OR REPLACE PACKAGE BODY purge_pkg IS

	/****************************
   	Local Package Constants
	*****************************/
   c_pkg_name     CONSTANT VARCHAR2(30)   := 'PURGE_PKG';
   gv_err_msg_gen CONSTANT VARCHAR2(2000) := 'An Oracle error has occurred: ';


	/****************************
		Local Package Variables
	*****************************/
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);


	/********************************************************************************

		Procedure:	purge_records

		Arguments:


		Description:

			Purges stale records from the database.

			The records to be deleted are determined by the purging policies stored in
			UTL_PURGE_POLICY. Each policy specifies a retention period for a business
			entity, and provides a strategy for deleting records from all the tables
			that form the entity.

			The purging strategy is represented as an set of rows in the
			UTL_PURGE_STRATEGY table, which specify the ordered set of tables from
			which to delete records. For each such table, a SQL predicate to use as
			part of the WHERE clause of the delete statement.

	*********************************************************************************/
	PROCEDURE purge_records IS

			v_delete_statement VARCHAR2(4050);
			v_wherequery       VARCHAR2(4000);
			v_tablename        VARCHAR2(30);
			v_archive_statement VARCHAR2(4050);
			v_column_exists    NUMBER(1);
			v_condition        VARCHAR2(12);
      
			CURSOR cur_get_policy IS
				SELECT
					upp.purge_policy_cd,
					upp.retention_period
			   FROM
			   	utl_purge_policy  upp
				WHERE
					upp.active_bool = 1;

		   CURSOR cur_get_policy_strategy (a_policy_cd IN utl_purge_policy.purge_policy_cd%TYPE ) IS
				SELECT
					ups.predicate_sql,
					ups.purge_table_cd,
	        ups.archive_table
				FROM
					utl_purge_strategy ups
				WHERE
					ups.purge_policy_cd = a_policy_cd
				ORDER BY
					ups.purge_ord ASC;

		BEGIN

		-- Process each active purging policy, one by one
		FOR rec_purge_policy IN cur_get_policy LOOP

		   -- For each policy, delete records according to the strategy for that policy
		   FOR rec_purge_strategy IN cur_get_policy_strategy ( rec_purge_policy.purge_policy_cd ) LOOP

					-- Dynamically generate and then execute a bulk delete statement against the
					-- table specified in the strategy. The WHERE clause of this statement ensures
					-- that only active records are deleted, and that the retention
					-- period is passed as a bind variable to the predicate defined in the strategy.

					v_wherequery := rec_purge_strategy.predicate_sql;
					v_tablename  := rec_purge_strategy.purge_table_cd;
          
	        -- check if a column exists before using it
	        SELECT COUNT(*) INTO v_column_exists
	        FROM user_tab_cols
	        WHERE column_name = 'RSTAT_CD'  AND
                      table_name  = v_tablename;
                
	        -- The column doesn't exist there
	        IF (v_column_exists = 0) THEN
	           -- build a dummy condition
	           v_condition := '0 = 0';
	        ELSE -- the column exists there
	           -- build a real condition
	           v_condition := 'rstat_cd = 0';
	        END IF;

	        IF rec_purge_strategy.archive_table IS NOT NULL

	        THEN

	        v_archive_statement := 'INSERT INTO ' || rec_purge_strategy.archive_table
			                || ' SELECT * FROM ' || v_tablename || ' WHERE ' || v_condition || ' AND '
	                                || v_wherequery;




	        EXECUTE IMMEDIATE v_archive_statement USING rec_purge_policy.retention_period;

	        END IF;

	        v_delete_statement := 'DELETE FROM ' || v_tablename
				       || ' WHERE ' || v_condition || ' AND '
				       || v_wherequery;


		EXECUTE IMMEDIATE v_delete_statement USING rec_purge_policy.retention_period ;


		END LOOP;

		COMMIT;

		END LOOP;

		EXCEPTION
			-- Raise an application error if an unexpected problem is encountered
			WHEN OTHERS THEN
				v_err_code := SQLCODE;
				v_err_msg  := SQLERRM;

				raise_application_error(
						gc_ex_purge_err,
						substr(	c_pkg_name || '.purge_records : ' ||
									gv_err_msg_gen || v_err_code || ' ' ||
									v_err_msg, 1, 2000),
						TRUE );

	END purge_records;

END purge_pkg;
/