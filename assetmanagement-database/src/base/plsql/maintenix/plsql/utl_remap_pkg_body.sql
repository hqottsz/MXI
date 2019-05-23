--liquibase formatted sql


--changeSet utl_remap_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "UTL_REMAP_PKG" IS
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

   ----------------------------------------------------------------------------
   -- Private variables
   ----------------------------------------------------------------------------
   c_pkg_name CONSTANT VARCHAR2(30) := 'utl_remap_pkg';

   v_err_msg     VARCHAR2(2000);
   v_err_code    VARCHAR2(200);
   v_method_name VARCHAR2(30);

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Private method specs
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------


   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public method bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Function:        blob_to_empty
   -- Arguments:       p_blob (BLOB)          - Input blob
   -- Return:          BLOB - Empty blob
   -- Description:     Returns an empty blob.  This function is used with the
   --                  data pump remap_data parameter to change blob values to
   --                  null during exports.
   ----------------------------------------------------------------------------
   FUNCTION blob_to_empty(p_blob IN BLOB) RETURN BLOB IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      RETURN EMPTY_BLOB();

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'blob_to_empty';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END blob_to_empty;

   ----------------------------------------------------------------------------
   -- Function:        clob_to_empty
   -- Arguments:       p_clob (CLOB)          - Input clob
   -- Return:          CLOB - Empty clob
   -- Description:     Returns an empty clob.  This function is used with the
   --                  data pump remap_data parameter to change clob values to
   --                  null during exports.
   ----------------------------------------------------------------------------
   FUNCTION clob_to_empty(p_clob IN CLOB) RETURN CLOB IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      RETURN EMPTY_CLOB();

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'clob_to_empty';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END clob_to_empty;

END utl_remap_pkg;
/