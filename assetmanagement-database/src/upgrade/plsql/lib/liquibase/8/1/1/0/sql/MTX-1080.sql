--liquibase formatted sql


--changeSet MTX-1080:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE REPORT_CONFIG_PKG
IS

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

--changeSet MTX-1080:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY REPORT_CONFIG_PKG IS
  
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
FUNCTION getMenuLaunchTypes RETURN VARCHAR2TABLE PIPELINED
AS
BEGIN
   PIPE ROW(ics_MenuViewPDF);
   PIPE ROW(ics_MenuViewExcel);
   PIPE ROW(ics_MenuViewWord);
   PIPE ROW(ics_MenuViewHTML);
   RETURN;
END;


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
FUNCTION getReportEngineTypes RETURN VARCHAR2TABLE PIPELINED
AS
BEGIN
   PIPE ROW(ics_LaunchBOE);
   PIPE ROW(ics_LaunchJasperREST);
   PIPE ROW(ics_LaunchJasperSSO);
   RETURN;
END;

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
FUNCTION getReportName(as_MenuURLLink IN utl_menu_item.menu_link_url%TYPE) RETURN VARCHAR2
IS
   lReturn      utl_menu_item.menu_link_url%TYPE;
   lAmpPos      INT;
   lTemplatePos INT;
BEGIN
   --search for the template name
   lTemplatePos := INSTR(as_MenuURLLink, 'aTemplate=');
   IF (lTemplatePos > 0) THEN
      lReturn := SUBSTR(as_MenuURLLink, lTemplatePos+10);

      --find the first character and get the report name (else just return the whole string)
      lAmpPos := INSTR(lReturn, chr(38));
      IF (lAmpPos > 0) THEN
         lReturn      := SUBSTR(lReturn, 1, lAmpPos-1);
       END IF;
   ELSE
      lReturn := '';
   END IF;

   RETURN lReturn;
END getReportName;

/********************************************************************************
*
* Function:       getReportView
* Arguments:      as_MenuURLLink (string): full menu link URL
*                 
* Description:    Pulls the report view output from the menu link URL               
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:   Helena Wang
* Recent Date:    April 16, 2014
*
*********************************************************************************
*
* Copyright 2014 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getReportView(as_MenuURLLink IN utl_menu_item.menu_link_url%TYPE) RETURN VARCHAR2
IS
   lReturn VARCHAR2(80);
   lTemplatePos INT;
BEGIN
   --search for the template name
   lTemplatePos := INSTR(as_MenuURLLink, 'aTemplate=');
   IF (lTemplatePos > 0) THEN
      --search for aViewPDF=true     
      IF (INSTR(as_MenuURLLink, 'aViewPDF=true') > 0) THEN
         lReturn := ics_MenuViewPDF;
      --search for aView=PDF    
      ELSIF (INSTR(as_MenuURLLink, 'aView=PDF') > 0) THEN
         lReturn := ics_MenuViewPDF;
      --search for aView=EXCEL    
      ELSIF (INSTR(as_MenuURLLink, 'aView=EXCEL') > 0) THEN
         lReturn := ics_MenuViewExcel;
      --search for aView=WORD    
      ELSIF (INSTR(as_MenuURLLink, 'aView=WORD') > 0) THEN
         lReturn := ics_MenuViewWord;
      --else return HTML
      ELSE
         lReturn := ics_MenuViewHTML;
      END IF;
   ELSE
      lReturn := '';
   END IF;

   RETURN lReturn;
END getReportView;

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
FUNCTION getURLSafeReportName(as_Report IN utl_report_type.report_name%TYPE) RETURN VARCHAR2
IS
   ls_URLSafeName utl_report_type.report_name%TYPE;
BEGIN
   -- replace all unsafe characters
   ls_URLSafeName := REPLACE(as_Report, ' ', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, chr(38), '.');  -- ampersand
   ls_URLSafeName := REPLACE(ls_URLSafeName, chr(39), '.');  -- '
   ls_URLSafeName := REPLACE(ls_URLSafeName, chr(96), '.');  -- `
   ls_URLSafeName := REPLACE(ls_URLSafeName, '+', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '/', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '?', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '#', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '\', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '%', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '^', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '{', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '}', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '|', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '"', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '<', '.');
   ls_URLSafeName := REPLACE(ls_URLSafeName, '>', '.');
   
   --return the updated name
   RETURN ls_URLSafeName;
   
END getURLSafeReportName;

/********************************************************************************
*
* Procedure       createUpdateReportConfig
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
* Recent Coder:   Wayne Yuke
* Recent Date:    May 13, 2014
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
                                  )
IS
   --cursor for all of the menu arguments to delete
   CURSOR lcur_DeleteMenuArgs(lMenuId utl_menu_item.menu_id%TYPE) IS
         SELECT arg_cd
           FROM utl_menu_item_arg
          WHERE utl_menu_item_arg.menu_id = lMenuId
          MINUS
         SELECT arg_cd
           FROM utl_menu_arg
          WHERE utl_menu_arg.arg_cd IN (as_MenuArgument1,as_MenuArgument2,as_MenuArgument3,as_MenuArgument4);

   --cursor for all of the menu arguments to delete
   CURSOR lcur_NewMenuArgs(lMenuId utl_menu_item.menu_id%TYPE) IS
         SELECT arg_cd
           FROM utl_menu_arg
          WHERE utl_menu_arg.arg_cd IN (as_MenuArgument1,as_MenuArgument2,as_MenuArgument3,as_MenuArgument4)
          MINUS
         SELECT arg_cd
           FROM utl_menu_item_arg
          WHERE utl_menu_item_arg.menu_id = lMenuId;

   /* *** DECLARE LOCAL VARIABLES *** */
   lb_CreateNew  boolean;
   lb_CreateMenu boolean;
   ls_ReportName utl_report_type.report_name%TYPE;
   ls_Path       utl_report_type.report_path%TYPE;
   lb_System     utl_report_type.system_bool%TYPE;
   ln_NewWindow  utl_menu_item.new_window_bool%TYPE;
   ln_MenuId     utl_menu_item.menu_id%TYPE;
   lrec_DeleteMenuArgs lcur_DeleteMenuArgs%ROWTYPE;
   lrec_NewMenuArgs    lcur_NewMenuArgs%ROWTYPE;
   
   EMPTY_REPORT_NAME   EXCEPTION;
   EMPTY_REPORT_ENGINE EXCEPTION;
   EMPTY_REPORT_PATH   EXCEPTION;
   EMPTY_MENU_NAME     EXCEPTION;
   INVALID_REPORT_ENGINE    EXCEPTION;
   INVALID_MENU_VIEW_OUTPUT EXCEPTION;
   
BEGIN
   --validate report name is not empty
   IF ((as_Report IS NULL) OR (LENGTH(as_Report) <= 0)) THEN
      RAISE EMPTY_REPORT_NAME;
   END IF;
   
   --ensure the report name is URL safe
   ls_ReportName := getURLSafeReportName(as_Report);
   
   --verify if the report is new or already exists
   BEGIN
      SELECT system_bool
        INTO lb_System
        FROM utl_report_type
       WHERE report_name = ls_ReportName;
      lb_CreateNew := false;
   EXCEPTION
      WHEN OTHERS THEN
         lb_System    := 0;
         lb_CreateNew := true;
   END;
   
   --validate menu name is not empty
   IF ((lb_System = 0) AND ((as_MenuName IS NULL) OR (LENGTH(as_MenuName) <= 0))) THEN
      RAISE EMPTY_MENU_NAME;
   END IF;
   
   --validate report engine is not empty
   IF ((as_ReportEngine IS NULL) OR (LENGTH(as_ReportEngine) <= 0)) THEN
      RAISE EMPTY_REPORT_ENGINE;
   END IF;
   
   --validate report engine is a valid type
   IF ((as_ReportEngine <> ics_LaunchBOE) AND (as_ReportEngine <> ics_LaunchJasperREST) AND (as_ReportEngine <> ics_LaunchJasperSSO)) THEN
      RAISE INVALID_REPORT_ENGINE;
   END IF;
   
   --validate report path is not empty for Jasper reports
   IF (as_ReportPath IS NULL) OR (LENGTH(as_ReportPath) <= 0) THEN
      IF ((as_ReportEngine = ics_LaunchJasperREST) OR (as_ReportEngine = ics_LaunchJasperSSO)) THEN
         RAISE EMPTY_REPORT_PATH;
      END IF;
      
      --default the path to a backslash (Maintenix application server is expecting a value in this column)
      ls_Path := '\';
   ELSE 
      ls_Path := as_ReportPath;
   END IF;

   --validate the menu view type
   IF ((as_MenuViewOutput <> ics_MenuViewPDF) AND (as_MenuViewOutput <> ics_MenuViewExcel) AND (as_MenuViewOutput <> ics_MenuViewWord) AND (as_MenuViewOutput <> ics_MenuViewHTML)) THEN
      RAISE INVALID_MENU_VIEW_OUTPUT;
   END IF;
   
   --default the new window boolean
   IF ((an_MenuLaunchNewWindow IS NULL) OR (an_MenuLaunchNewWindow = 0)) THEN
      ln_NewWindow := 0;
   ELSE
      ln_NewWindow := 1;
   END IF;
   
   --create or update UTL_REPORT_TYPE
   IF lb_CreateNew THEN
      INSERT INTO 
         UTL_REPORT_TYPE(
               REPORT_NAME,
               REPORT_ENGINE_TYPE,
               REPORT_PATH,
               REPORT_DESC,
               ACTIVE_BOOL,
               SYSTEM_BOOL,
               REVISION_NO
               )
            VALUES (
               ls_ReportName,
               as_ReportEngine,
               ls_Path,
               as_ReportDesc,
               1,
               lb_System,
               1               
              );
   ELSE
      UPDATE UTL_REPORT_TYPE
         SET
               REPORT_ENGINE_TYPE = as_ReportEngine,
               REPORT_PATH        = ls_Path,
               REPORT_DESC        = as_ReportDesc
         WHERE       
               REPORT_NAME = ls_ReportName;   
   END IF;
   
   --create or update UTL_MENU_ITEM/UTL_MENU_ITEM_ARG (only for non system reports)
   IF (lb_System = 0) THEN
      BEGIN
         --get the menu id
         SELECT menu_id         
           INTO ln_MenuId
           FROM utl_menu_item
          WHERE menu_link_url like (ics_MenuUrlPrefix || ls_ReportName || chr(38) || '%' )
            AND rownum < 2;
         lb_CreateMenu := false;
      EXCEPTION
         WHEN OTHERS THEN
         lb_CreateMenu := true;
      END;

      --create or update the UTL_MENU_ITEM data
      IF lb_CreateMenu THEN
         SELECT UTL_MENU_ITEM_ID.nextval        
           INTO ln_MenuId
           FROM dual;
         
         INSERT INTO
            UTL_MENU_ITEM(
                  MENU_ID,
                  TODO_LIST_ID,
                  MENU_NAME,
                  MENU_LINK_URL,
                  NEW_WINDOW_BOOL,
                  UTL_ID
                  )
               SELECT
                  ln_MenuId,
                  NULL,
                  as_MenuName,
                  ics_MenuUrlPrefix || ls_ReportName || chr(38) || 'aView=' || as_MenuViewOutput,
                  ln_NewWindow,
                  0
                FROM
                  dual;
      ELSE
         UPDATE UTL_MENU_ITEM
            SET
                 MENU_NAME       = as_MenuName,
                 MENU_LINK_URL   = ics_MenuUrlPrefix || ls_ReportName || chr(38) || 'aView=' || as_MenuViewOutput,
                 NEW_WINDOW_BOOL = ln_NewWindow
            WHERE
                 MENU_LINK_URL like (ics_MenuUrlPrefix || ls_ReportName || chr(38) || '%' );
      END IF;
      
      --cleanup any inapplicable menu arguments
      FOR lrec_DeleteMenuArgs IN lcur_DeleteMenuArgs(ln_MenuId) LOOP
          DELETE utl_menu_item_arg
           WHERE menu_id = ln_MenuId
             AND arg_cd  = lrec_DeleteMenuArgs.arg_cd;
      END LOOP;
      
      --loop through and add any menu arguments that are missing
      FOR lrec_NewMenuArgs IN lcur_NewMenuArgs(ln_MenuId) LOOP
          INSERT INTO
             utl_menu_item_arg (
                   MENU_ID,
                   ARG_CD,
                   UTL_ID
                   )
                VALUES (
                   ln_MenuId,
                   lrec_NewMenuArgs.arg_cd,
                   0
                   );              
      END LOOP;
   END IF;
   
   --reture success
   on_Return := icn_Success;
      
   EXCEPTION
      WHEN EMPTY_REPORT_NAME THEN
         on_Return := icn_EmptyReportName;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_EmptyReportName,'report_config_pkg@@@createuUpdateReportConfig@@@Report name cannot be empty');
      WHEN EMPTY_REPORT_ENGINE THEN
         on_Return := icn_EmptyReportEngine;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_EmptyReportEngine,'report_config_pkg@@@createuUpdateReportConfig@@@Report engine type cannot be empty');
      WHEN EMPTY_REPORT_PATH THEN
         on_Return := icn_EmptyReportPath;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_EmptyReportPath,'report_config_pkg@@@createuUpdateReportConfig@@@Report path cannot be empty for Jasper reports');
      WHEN EMPTY_MENU_NAME THEN
         on_Return := icn_EmptyReportMenu;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_EmptyReportMenu,'report_config_pkg@@@createuUpdateReportConfig@@@Menu name cannot be empty');
      WHEN INVALID_REPORT_ENGINE THEN
         on_Return := icn_InvalidReportEngine;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_InvalidReportEngine,'report_config_pkg@@@createuUpdateReportConfig@@@Report engine type is not a valid type (' || ics_LaunchBOE || ',' || ics_LaunchJasperREST || ',' || ics_LaunchJasperSSO || ')');
      WHEN INVALID_MENU_VIEW_OUTPUT THEN
         on_Return := icn_InvalidMenuViewOutput;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_InvalidMenuViewOutput,'report_config_pkg@@@createuUpdateReportConfig@@@Menu view output is not a valid type (' || ics_MenuViewPDF || ',' || ics_MenuViewExcel || ',' || ics_MenuViewWord || ',' || ics_MenuViewHTML || ')');
      WHEN OTHERS THEN
         -- Unexpected error
         on_Return := icn_Error;
         APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','report_config_pkg@@@createuUpdateReportConfig@@@'||SQLERRM);
         RETURN;
END createUpdateReportConfig;


/********************************************************************************
*
* Procedure       deleteReportConfig
* Arguments:      as_Report (string): report name to delete
*                 on_Return (number): Return 1 means success, <0 means failure
*                 
* Description:    Delete utl_report_type data and corresponding utl_menu_item data.
*                 Will not delete system required reports.
*                 Will not throw error if report does not exist                 
*
* Orig.Coder:     Wayne Yuke
* Recent Coder:   Wayne Yuke
* Recent Date:    May 13, 2014
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
                             )
IS

   /* *** DECLARE LOCAL VARIABLES *** */
   ls_ReportName             utl_report_type.report_name%TYPE;
   lb_System                 utl_report_type.system_bool%TYPE;
   SYSTEM_REPORT_UNDELETABLE EXCEPTION;
BEGIN
   --ensure the report name is URL safe
   ls_ReportName := getURLSafeReportName(as_Report);

   --verify the report name is found
   SELECT system_bool
     INTO lb_System
     FROM utl_report_type
    WHERE report_name = ls_ReportName;
   
   --verify the report is not a system report
   IF (lb_System = 1) THEN
      RAISE SYSTEM_REPORT_UNDELETABLE;
   END IF;
   
   --delete any menu items for the report
   DELETE utl_menu_item_arg WHERE menu_id IN
     (SELECT menu_id FROM utl_menu_item
       WHERE menu_link_url like (ics_MenuUrlPrefix||ls_ReportName|| chr(38) || '%'));
   DELETE utl_menu_group_item WHERE menu_id IN
     (SELECT menu_id FROM utl_menu_item
       WHERE menu_link_url like (ics_MenuUrlPrefix||ls_ReportName|| chr(38) || '%'));
   DELETE utl_menu_item
       WHERE menu_link_url like (ics_MenuUrlPrefix||ls_ReportName|| chr(38) || '%');
   
   --delete the report configuration
   DELETE utl_report_type
    WHERE report_name = ls_ReportName;

   -- return success
   on_Return := icn_Success;
   
   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- invalid report name
         on_Return := icn_InvalidReportName;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_InvalidReportName,'report_config_pkg@@@deleteReportConfig@@@Report name '||ls_ReportName||' was not found in the UTL_REPORT_TYPE table.');
      WHEN SYSTEM_REPORT_UNDELETABLE THEN
         -- invalid report name
         on_Return := icn_SystemReportReadonly;
         APPLICATION_OBJECT_PKG.SetMxiError(icn_SystemReportReadonly,'report_config_pkg@@@deleteReportConfig@@@Report '||ls_ReportName||' is a system required report and cannot be deleted.');
      WHEN OTHERS THEN
         -- Unexpected error
         on_Return := icn_Error;
         APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','report_config_pkg@@@deleteReportConfig@@@'||SQLERRM);
         RETURN;
END deleteReportConfig;

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
PROCEDURE validateMenuSequence
IS
   ln_NextSeqValue NUMBER;
   ln_MaxMenuID    NUMBER;
   ln_NewValue     NUMBER;
   ln_Delta        NUMBER;
BEGIN
   --initialize the new value to the minimum menu value
   ln_NewValue := icn_MinimumMenuId;
   
   --get the max menu id number and, if larger, set that as the new minimum
   SELECT max(menu_id)
     INTO ln_MaxMenuID
     FROM utl_menu_item;
   IF (ln_NewValue < ln_MaxMenuID) THEN
      ln_NewValue := ln_MaxMenuID;
   END IF;

   --get the next sequence value and, if smaller then update the sequence
   SELECT utl_menu_item_id.nextval
     INTO ln_NextSeqValue
     FROM dual;
   ln_Delta := ln_NewValue - ln_NextSeqValue + 1;
   IF (ln_Delta > 0) THEN
      EXECUTE IMMEDIATE 'ALTER SEQUENCE utl_menu_item_id INCREMENT BY ' || ln_Delta;
      SELECT utl_menu_item_id.nextval
        INTO ln_NextSeqValue
        FROM dual;
      EXECUTE IMMEDIATE 'ALTER SEQUENCE utl_menu_item_id INCREMENT BY 1';
   END IF;

   EXCEPTION
      WHEN OTHERS THEN
         -- Unexpected error
         APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','report_config_pkg@@@validateMenuSequence@@@'||SQLERRM);
         RETURN;
END validateMenuSequence;
  

END REPORT_CONFIG_PKG;
/

--changeSet MTX-1080:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************* recompile the packages (also clears cached versions of the packages ****************************/
/************* ensure that any custom create menu items adhere to the new naming restriction ******************/
DECLARE
   --cursor for all of the menu arguments to delete
   CURSOR lcur_MalformedReportName IS
         SELECT report_name,
                report_config_pkg.getURLSafeReportName(report_name) as fixed_name
           FROM utl_report_type
          WHERE report_name <> report_config_pkg.getURLSafeReportName(report_name);

   /* *** DECLARE LOCAL VARIABLES *** */
   lrec_MalformedReportName lcur_MalformedReportName%ROWTYPE;
BEGIN
   --loop through fix any malformed report names
   FOR lrec_MalformedReportName IN lcur_MalformedReportName() LOOP
      --update the utl_report_type table
      UPDATE utl_report_type
         SET report_name = lrec_MalformedReportName.fixed_name
       WHERE report_name = lrec_MalformedReportName.report_name;
       
      --update the utl_menu_item URL
      UPDATE utl_menu_item
         SET menu_link_url = (report_config_pkg.ics_MenuUrlPrefix || lrec_MalformedReportName.fixed_name || 
                              SUBSTR(menu_link_url, LENGTH(report_config_pkg.ics_MenuUrlPrefix || lrec_MalformedReportName.report_name) + 1
                                   )
                             )
       WHERE menu_link_url like (report_config_pkg.ics_MenuUrlPrefix || lrec_MalformedReportName.report_name || chr(38) || '%' );

   END LOOP;   
END;
/