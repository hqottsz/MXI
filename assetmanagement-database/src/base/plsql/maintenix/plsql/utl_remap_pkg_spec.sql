--liquibase formatted sql


--changeSet utl_remap_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "UTL_REMAP_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : utl_remap_pkg
   -- Object Type : Package Body
   -- Date        : August 13, 2012
   -- Coder       : Mark Rutherford
   -- Recent Date :
   -- Recent Coder:
   -- Description :
   -- This is the remap package containing all generic methods and
   -- constants for remapping blobs and clobs with data pump.
   ----------------------------------------------------------------------------
   -- Copyright 2012 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   --
   ----------------------------------------------------------------------------
   -- Global types
   ----------------------------------------------------------------------------
   --
   --
   ----------------------------------------------------------------------------
   -- Global Constants
   ----------------------------------------------------------------------------
   --
   --
   ----------------------------------------------------------------------------
   -- Public Methods
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Function:        blob_to_empty
   -- Arguments:       p_blob (BLOB)          - Input blob
   -- Return:          BLOB - Empty blob
   -- Description:     Returns an empty blob.  This function is used with the
   --                  data pump remap_data parameter to change blob values to
   --                  null during exports.
   ----------------------------------------------------------------------------
   FUNCTION blob_to_empty(p_blob IN BLOB) RETURN BLOB;

   ----------------------------------------------------------------------------
   -- Function:        clob_to_empty
   -- Arguments:       p_clob (CLOB)          - Input clob
   -- Return:          CLOB - Empty clob
   -- Description:     Returns an empty clob.  This function is used with the
   --                  data pump remap_data parameter to change clob values to
   --                  null during exports.
   ----------------------------------------------------------------------------
   FUNCTION clob_to_empty(p_clob IN CLOB) RETURN CLOB;

END utl_remap_pkg;
/