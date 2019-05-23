--liquibase formatted sql


--changeSet BLT_UTL_ERR_CONST_PKG_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "BLT_UTL_ERR_CONST_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : blt_utl_err_const_pkg
   -- Object Type : Package Header
   -- Date        : January 04, 2010
   -- Coder       : Ed Irvine
   -- Recent Date : January 04, 2010
   -- Recent Coder: Ed Irvine
   -- Description :
   -- This package contains the error constants for the BLT function.
   ----------------------------------------------------------------------------
   -- Copyright @ 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------

   -- Exception code must be unique and between -20001 and -20999
   ----------------------------------------------------------------------------
   gv_err_msg_gen CONSTANT VARCHAR2(2000) := 'An Oracle error has occurred; details are as follows: ';

   gv_err_wf_error CONSTANT VARCHAR2(2000) := 'One or more workflows failed to finish successfully.';

   ----------------------------------------------------------------------------
   -- Common exceptions
   ----------------------------------------------------------------------------

   gc_ex_wf_controller CONSTANT NUMBER := -20100;
   gex_wf_controller EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_wf_controller,
                         -20100);
   -- LOAD EXCEPTIONS
   gc_ex_import_running CONSTANT NUMBER := -20101;
   gex_import_running EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_import_running,
                         -20101);
   gv_err_msg_import_running CONSTANT VARCHAR2(2001) := 'A load process has already been started.';

   gc_ex_amu_no_data CONSTANT NUMBER := -20102;
   gex_amu_no_data EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_amu_no_data,
                         -20102);
   gv_err_msg_amu_no_data CONSTANT VARCHAR2(2001) := 'There is currently no valid data to be processed. Make sure you have run AMU successfully.';

   -- VALIDATE EXCEPTIONS
   gc_ex_validate CONSTANT NUMBER := -20103;
   gex_validate EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_validate,
                         -20103);
   -- LOOKUP EXCEPTIONS
   gc_ex_lookup CONSTANT NUMBER := -20104;
   gex_lookup EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_lookup,
                         -20104);
END blt_utl_err_const_pkg;
/