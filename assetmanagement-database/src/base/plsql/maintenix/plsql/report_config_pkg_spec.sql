--liquibase formatted sql


--changeSet report_config_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE REPORT_CONFIG_PKG IS

/******************************************************************************
* TYPE DECLARATIONS
******************************************************************************/

/* subtype declarations */
 SUBTYPE typn_RetCode  IS NUMBER;
 SUBTYPE typn_ViewCode IS VARCHAR(8);

 /* constant declarations (error codes) */
 icn_Success CONSTANT typn_RetCode := 1;   -- Success
 icn_NoProc  CONSTANT typn_RetCode := 0;   -- No processing done
 icn_Error   CONSTANT typn_RetCode := -1;  -- Error

 /* list of menu view output types */
 ics_MenuViewPDF   CONSTANT typn_ViewCode := 'PDF';
 ics_MenuViewWord  CONSTANT typn_ViewCode := 'WORD';
 ics_MenuViewExcel CONSTANT typn_ViewCode := 'EXCEL';
 ics_MenuViewHTML  CONSTANT typn_ViewCode := 'HTML';

 /* list of report launch types */
 ics_LaunchBOE        CONSTANT utl_report_type.report_engine_type%TYPE := 'BOE';
 ics_LaunchJasperREST CONSTANT utl_report_type.report_engine_type%TYPE := 'JASPER_REST';
 ics_LaunchJasperSSO  CONSTANT utl_report_type.report_engine_type%TYPE := 'JASPER_SSO';
 ics_LaunchJasperDashboard CONSTANT utl_report_type.report_engine_type%TYPE := 'JASPER_DASH';
 ics_LaunchJasperLDashboard CONSTANT utl_report_type.report_engine_type%TYPE := 'JASPER_LEGACY_DASH';

 /* utl menu url */
 ics_MenuUrlPrefix CONSTANT VARCHAR2(80) := '/maintenix/servlet/report/generate?aTemplate=';

 /* exceptions */
 icn_EmptyReportName       CONSTANT NUMBER := -20200;
 icn_EmptyReportPath       CONSTANT NUMBER := -20201;
 icn_EmptyReportEngine     CONSTANT NUMBER := -20202;
 icn_EmptyReportMenu       CONSTANT NUMBER := -20203;
 icn_InvalidReportName     CONSTANT NUMBER := -20204;
 icn_InvalidReportEngine   CONSTANT NUMBER := -20206;
 icn_InvalidMenuViewOutput CONSTANT NUMBER := -20207;
 icn_SystemReportReadonly  CONSTANT NUMBER := -20208;

/* minimum menu id value */
icn_MinimumMenuId CONSTANT NUMBER := 150000;


/********************************************************************************
*
* Function:       getMenuLaunchTypes
* Arguments:      (none)
*
* Description:    Pulls the report view output from the menu link URL
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    Feb 13, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getMenuLaunchTypes RETURN VARCHAR2TABLE PIPELINED;

/********************************************************************************
*
* Function:       getReportEngineTypes
* Arguments:      (none)
*
* Description:    Pulls the report engine types for report launching
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    Feb 13, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getReportEngineTypes RETURN VARCHAR2TABLE PIPELINED;

/********************************************************************************
*
* Function:       getReportName
* Arguments:      as_MenuURLLink (string): full menu link URL
*
* Description:    Pulls the report name out from the menu link URL
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    Feb 10, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getReportName(as_MenuURLLink IN utl_menu_item.menu_link_url%TYPE) RETURN VARCHAR2;

/********************************************************************************
*
* Function:       getReportView
* Arguments:      as_MenuURLLink (string): full menu link URL
*
* Description:    Pulls the report view output from the menu link URL
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    Feb 10, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getReportView(as_MenuURLLink IN utl_menu_item.menu_link_url%TYPE) RETURN VARCHAR2;

/********************************************************************************
*
* Function:       getReportOutputPrompt
* Arguments:      as_MenuURLLink (string): full menu link URL
*
* Description:    Pulls the report prompt for output from the menu link URL
*
* Orig.Coder:     Chris McGee
* Recent Coder:
* Recent Date:    Sep 07, 2016
*
*********************************************************************************
*
* Copyright 2016 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getReportOutputPrompt(as_MenuURLLink IN utl_menu_item.menu_link_url%TYPE) RETURN NUMBER;

/********************************************************************************
*
* Function:       getReportRestrictedOutput
* Arguments:      as_MenuURLLink (string): full menu link URL
*
* Description:    Pulls the report restricted output from the menu link URL
*
* Orig.Coder:     Yong Zhong
* Recent Coder:
* Recent Date:    Sep 30, 2016
*
*********************************************************************************
*
* Copyright 2016 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getReportRestrictedOutput(as_MenuURLLink IN utl_menu_item.menu_link_url%TYPE) RETURN VARCHAR2;

/********************************************************************************
*
* Function:       getURLSafeReportName
* Arguments:      (none)
*
* Description:    Returns a URL safe version of the report name.  This appears
*                 as part of the menu item URL.  Removes: space ampersand '`+/?#\%^{}|"<>
*                 Several will not allow a user to login if menu is assigned
*                 where others will truncate the URL lookup in UTL_REPORT_TYPE.
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    May 13, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getURLSafeReportName(as_Report IN utl_report_type.report_name%TYPE) RETURN VARCHAR2;

/********************************************************************************
*
* Procedure:      createUpdateReportConfig
* Arguments:      as_Report (string) IN: report name to delete
*                 as_ReportEngine (string) IN: report engine to use
*                 as_ReportPath (string) IN: jasper report path
*                 as_ReportDesc (string) IN: description of report
*                 as_MenuName (string) IN: menu text name
*                 as_MenuViewOutput (string) IN: report output type (PDF, etc)
*                 an_MenuLaunchNewWindow (number) IN: report should be in a new window
*                 as_MenuArgument1 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 as_MenuArgument2 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 as_MenuArgument3 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 as_MenuArgument4 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 on_Return (number): Return 1 means success, <0 means failure
*
* Description:    Create utl_report_type data if it doesn't already exist,
*                 otherwise it will update values.  It will create menu items if
*                 they don't exist (menus can update output type (PDF,excel,html).
*                 Menu items will default to PDF.
*                 It will create/delete menu arguments to match the passed in
*                 list of arguments (system required reports cannot have their
*                 arguments changed)
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    Feb 10, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE createUpdateReportConfig(as_Report              IN  utl_report_type.report_name%TYPE,
                                   as_ReportEngine        IN  utl_report_type.report_engine_type%TYPE,
                                   as_ReportPath          IN  utl_report_type.report_path%TYPE,
                                   as_ReportDesc          IN  utl_report_type.report_desc%TYPE,
                                   as_MenuName            IN  utl_menu_item.menu_name%TYPE,
                                   as_MenuViewOutput      IN  VARCHAR2,
                                   an_MenuLaunchNewWindow IN  utl_menu_item.new_window_bool%TYPE,
                                   as_MenuArgument1       IN  VARCHAR2,
                                   as_MenuArgument2       IN  VARCHAR2,
                                   as_MenuArgument3       IN  VARCHAR2,
                                   as_MenuArgument4       IN  VARCHAR2,
                                   on_Return              OUT typn_RetCode
                                  );

/********************************************************************************
*
* Procedure:      createUpdateReportConfig
* Arguments:      as_Report (string) IN: report name to delete
*                 as_ReportEngine (string) IN: report engine to use
*                 as_ReportPath (string) IN: jasper report path
*                 as_ReportDesc (string) IN: description of report
*                 as_MenuName (string) IN: menu text name
*                 as_MenuViewOutput (string) IN: report output type (PDF, etc)
*                 an_PromptViewOutput (number) IN: report should prompt for report output type.
*                   If true, the default is the menu view output.
*                 an_PromptRestrictedOutput IN: restricted report output types
*                 an_MenuLaunchNewWindow (number) IN: report should be in a new window
*                 as_MenuArgument1 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 as_MenuArgument2 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 as_MenuArgument3 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 as_MenuArgument4 (string) IN: menu arguments (as per UTL_MENU_ARG)
*                 on_Return (number): Return 1 means success, <0 means failure
*
* Description:    Create utl_report_type data if it doesn't already exist,
*                 otherwise it will update values.  It will create menu items if
*                 they don't exist (menus can update output type (PDF,excel,html).
*                 Menu items will default to PDF.
*                 It will create/delete menu arguments to match the passed in
*                 list of arguments (system required reports cannot have their
*                 arguments changed)
*
* Orig.Coder:     Chris McGee
* Recent Coder:   Yong Zhong
* Recent Date:    Sep 29, 2016
*
*********************************************************************************
*
* Copyright 2016 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE createUpdateReportConfig(as_Report                 IN  utl_report_type.report_name%TYPE,
                                   as_ReportEngine           IN  utl_report_type.report_engine_type%TYPE,
                                   as_ReportPath             IN  utl_report_type.report_path%TYPE,
                                   as_ReportDesc             IN  utl_report_type.report_desc%TYPE,
                                   as_MenuName               IN  utl_menu_item.menu_name%TYPE,
                                   as_MenuViewOutput         IN  VARCHAR2,
                                   an_PromptViewOutput       IN  NUMBER,
                                   an_PromptRestrictedOutput IN  VARCHAR2,
                                   an_MenuLaunchNewWindow    IN  utl_menu_item.new_window_bool%TYPE,
                                   as_MenuArgument1          IN  VARCHAR2,
                                   as_MenuArgument2          IN  VARCHAR2,
                                   as_MenuArgument3          IN  VARCHAR2,
                                   as_MenuArgument4          IN  VARCHAR2,
                                   on_Return                 OUT typn_RetCode
                                  );

/********************************************************************************
*
* Procedure:      deleteReportConfig
* Arguments:      as_Report (string): report name to delete
*                 on_Return (number): Return 1 means success, <0 means failure
*
* Description:    Delete utl_report_type data and corresponding utl_menu_item data.
*                 Will not delete system required reports.
*                 Will not throw error if report does not exist
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    Feb 10, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE deleteReportConfig(as_Report IN  utl_report_type.report_name%TYPE,
                             on_Return OUT typn_RetCode
                             );

/********************************************************************************
*
* Procedure:      validateMenuSequence
*
* Description:    Ensures the menu sequence id is valid
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:
* Recent Date:    Feb 10, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE validateMenuSequence;

END REPORT_CONFIG_PKG;
/