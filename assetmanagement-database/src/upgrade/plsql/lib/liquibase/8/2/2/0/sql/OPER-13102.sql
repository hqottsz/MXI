--liquibase formatted sql

--changeSet OPER-13102:1 stripComments:false
-- Deferral Reference Search
INSERT
    INTO
        UTL_MENU_ITEM(
            MENU_ID,
            TODO_LIST_ID,
            MENU_NAME,
            MENU_LINK_URL,
            NEW_WINDOW_BOOL,
            MENU_LDESC,
            REPL_APPROVED,
            UTL_ID
        ) SELECT
            120948,
            NULL,
            'web.menuitem.DEFERRAL_REFERENCE_SEARCH',
            '/lmocweb/deferral-reference/index.html',
            0,
            'Deferral Reference Search',
            0,
            0
        FROM
            dual
        WHERE
            NOT EXISTS(
                SELECT
                    1
                FROM
                    UTL_MENU_ITEM
                WHERE
                    MENU_ID = 120948
            );

--changeSet OPER-13102:2 stripComments:false
-- Launch LMOC Deferral Reference Search
INSERT
    INTO
        UTL_TODO_BUTTON(
            TODO_BUTTON_ID,
            PARM_NAME,
            BUTTON_NAME,
            ICON,
            ACTION,
            TOOLTIP,
            TODO_BUTTON_LDESC,
            UTL_ID
        ) SELECT
            10072,
            NULL,
            'web.todobutton.DEFERRAL_REF_SEARCH_NAME',
            NULL,
            '/../lmocweb/deferral-reference/index.html',
            'web.todobutton.DEFERRAL_REF_SEARCH_TOOLTIP',
            'web.todobutton.DEFERRAL_REF_SEARCH_LDESC',
            0
        FROM
            dual
        WHERE
            NOT EXISTS(
                SELECT
                    1
                FROM
                    UTL_TODO_BUTTON
                WHERE
                    TODO_BUTTON_ID = 10072
            );