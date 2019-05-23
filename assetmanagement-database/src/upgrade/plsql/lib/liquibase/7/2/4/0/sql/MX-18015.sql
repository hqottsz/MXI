--liquibase formatted sql


--changeSet MX-18015:1 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelExportImages';

--changeSet MX-18015:2 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelExportImages';

--changeSet MX-18015:3 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelExportImages';

--changeSet MX-18015:4 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelExportImages';

--changeSet MX-18015:5 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelExportObjectFormatting';

--changeSet MX-18015:6 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelExportObjectFormatting';

--changeSet MX-18015:7 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelExportObjectFormatting';

--changeSet MX-18015:8 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelExportObjectFormatting';

--changeSet MX-18015:9 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelExportPageHeaderAndFooter';

--changeSet MX-18015:10 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelExportPageHeaderAndFooter';

--changeSet MX-18015:11 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelExportPageHeaderAndFooter';

--changeSet MX-18015:12 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelExportPageHeaderAndFooter';

--changeSet MX-18015:13 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelMaintainColumnAlign';

--changeSet MX-18015:14 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelMaintainColumnAlign';

--changeSet MX-18015:15 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelMaintainColumnAlign';

--changeSet MX-18015:16 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelMaintainColumnAlign';

--changeSet MX-18015:17 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelMaintainRelativeObjectPosition';

--changeSet MX-18015:18 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelMaintainRelativeObjectPosition';

--changeSet MX-18015:19 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelMaintainRelativeObjectPosition';

--changeSet MX-18015:20 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelMaintainRelativeObjectPosition';

--changeSet MX-18015:21 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelShowGroupOutlines';

--changeSet MX-18015:22 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelShowGroupOutlines';

--changeSet MX-18015:23 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelShowGroupOutlines';

--changeSet MX-18015:24 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelShowGroupOutlines';

--changeSet MX-18015:25 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelSimplifyPageHeaders';

--changeSet MX-18015:26 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelSimplifyPageHeaders';

--changeSet MX-18015:27 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelSimplifyPageHeaders';

--changeSet MX-18015:28 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelSimplifyPageHeaders';

--changeSet MX-18015:29 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'sReportExcelUseWorksheetFunctionsForSummaries';

--changeSet MX-18015:30 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'sReportExcelUseWorksheetFunctionsForSummaries';

--changeSet MX-18015:31 stripComments:false
DELETE FROM db_type_config_parm t where t.parm_name = 'sReportExcelUseWorksheetFunctionsForSummaries';

--changeSet MX-18015:32 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'sReportExcelUseWorksheetFunctionsForSummaries';