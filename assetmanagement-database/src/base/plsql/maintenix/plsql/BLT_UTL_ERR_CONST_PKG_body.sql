--liquibase formatted sql


--changeSet BLT_UTL_ERR_CONST_PKG_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "BLT_UTL_ERR_CONST_PKG" IS
----------------------------------------------------------------------------
-- Object Name : blt_utl_err_const_pkg
-- Object Type : Package Body
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
END blt_utl_err_const_pkg;
/