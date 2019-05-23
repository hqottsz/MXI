--liquibase formatted sql


--changeSet purge_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
 CREATE OR REPLACE PACKAGE purge_pkg IS

	/****************************
   	Package Exceptions
	*****************************/
	gc_ex_purge_err CONSTANT NUMBER := -20700;


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
	PROCEDURE purge_records;

END purge_pkg;
/