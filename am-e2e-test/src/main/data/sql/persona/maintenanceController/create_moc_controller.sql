
-- Create MOC Controller user.

INSERT
    INTO
        UTL_USER(
            USER_ID,
            USERNAME,
            PASSWORD,
            FIRST_NAME,
            LAST_NAME,
            UTL_ID,
            CTRL_DB_ID
        ) SELECT
            UTL_USER_ID.nextval,
            'maintcontrol',
            'password',
            'maintenance',
            'controller',
            4650,
            4650
        FROM
            dual;


-- Create an organisation user record and link to the user.

INSERT
    INTO
        ORG_HR(
            HR_DB_ID,
            HR_ID,
            USER_ID,
            HR_CD,
            PAY_METHOD_DB_ID,
            PAY_METHOD_CD
        )
    SELECT
        4650,
        ORG_HR_ID.nextval,
        (SELECT USER_ID FROM UTL_USER WHERE USERNAME = 'maintcontrol'),
        'MCTEST',
        0,
        'SALARY'
 FROM
            dual;


-- Create an association between the organisation user record and and an organisation. Uses the pre-existing ADMIN organisation.

INSERT
    INTO
        ORG_ORG_HR(
            HR_DB_ID,
            HR_ID,
            ORG_DB_ID,
            ORG_ID,
            DEFAULT_ORG_BOOL
        )
    SELECT
        4650,
        (SELECT oh.HR_ID FROM org_hr oh INNER JOIN utl_user uu ON oh.USER_ID = uu.USER_ID WHERE uu.USERNAME = 'maintcontrol' AND oh.HR_CD = 'MCTEST'),
        0,
        1,
        1
    FROM
        dual;


-- Link the user to the Maintenance Controller role.

INSERT
    INTO
        UTL_USER_ROLE(
            USER_ID,
            ROLE_ID,
            ROLE_ORDER,
            UTL_ID
        ) SELECT
            (SELECT USER_ID FROM UTL_USER WHERE USERNAME = 'maintcontrol'),
            (SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
            0,
            4650
        FROM
            dual;


-- Associate Maintenance Controller menu group with the Phone Up Deferral menu item.
INSERT
    INTO
        UTL_MENU_GROUP_ITEM(
            GROUP_ID,
            MENU_ID,
            MENU_ORDER,
            UTL_ID
        )
    SELECT
        (SELECT GROUP_ID FROM UTL_MENU_GROUP WHERE GROUP_NAME = 'Maintenance Controller'),
        (SELECT umi.MENU_ID FROM UTL_MENU_ITEM umi WHERE umi.MENU_NAME = 'web.menuitem.PHONE_UP_DEFERRAL'),
        1,
        4650
        FROM
            dual;

-- Associate Maintenance Controller menu group with the Deferral Reference Configuration menu item.
INSERT
    INTO
        UTL_MENU_GROUP_ITEM(
            GROUP_ID,
            MENU_ID,
            MENU_ORDER,
            UTL_ID
        )
    SELECT
        (SELECT GROUP_ID FROM UTL_MENU_GROUP WHERE GROUP_NAME = 'Maintenance Controller'),
        (SELECT umi.MENU_ID FROM UTL_MENU_ITEM umi WHERE umi.MENU_NAME = 'web.menuitem.DEFERRAL_REFERENCE_SEARCH'),
        1,
        4650
        FROM
            dual;

-- Associate Maintenance Controller menu group with the Pending Deferral Requests menu item.
INSERT
    INTO
        UTL_MENU_GROUP_ITEM(
            GROUP_ID,
            MENU_ID,
            MENU_ORDER,
            UTL_ID
        )
    SELECT
        (SELECT GROUP_ID FROM UTL_MENU_GROUP WHERE GROUP_NAME = 'Maintenance Controller'),
        (SELECT umi.MENU_ID FROM UTL_MENU_ITEM umi WHERE umi.MENU_NAME = 'web.menuitem.REFERENCE_APPROVAL'),
        1,
        4650
        FROM
            dual;

-- Give the Maintenance Controller role access to the APIs required for the Phone Up Deferral feature. Assumes the parms have already been added to the database.

-- API_AIRCRAFT_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_AIRCRAFT_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_AIRCRAFT_GROUP_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_AIRCRAFT_GROUP_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_SEARCH_DEFERRAL_REFERENCE_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_SEARCH_DEFERRAL_REFERENCE_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_SEARCH_REFERENCE_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_SEARCH_REFERENCE_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_AUTHORIZE_REJECT_REFERENCE_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_AUTHORIZE_REJECT_REFERENCE_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_CREATE_REFERENCE_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_CREATE_REFERENCE_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_CREATE_DEFERRAL_REFERENCE_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_ACTION_CREATE_DEFER_REF',
		'true',
		4650
        FROM
            dual;

-- API_EDIT_DEFERRAL_REFERENCE_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_ACTION_EDIT_DEFER_REF',
		'true',
		4650
        FROM
            dual;
			
-- API_TASKDEF_PARM
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_TASKDEF_PARM',
		'true',
		4650
        FROM
            dual;
			
-- API_TASK_PARM
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_TASK_PARM',
		'true',
		4650
        FROM
            dual;

-- API_SEARCH_ASSEMBLY_CAPABILITY_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_SEARCH_ASSEMBLY_CAPABILITY_REQUEST',
		'true',
		4650
        FROM
            dual;

-- ACTION_EDIT_CONFIGURED_CAPABILITY_LEVEL
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'ACTION_EDIT_CONFIGURED_CAPABILITY_LEVEL',
		'true',
		4650
        FROM
            dual;


-- API_FAULT_SOURCE_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_FAULT_SOURCE_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_FLIGHT_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_FLIGHT_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_GLOBAL_PARAMETER_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_GLOBAL_PARAMETER_REQUEST',
		'true',
		4650
        FROM
            dual;

-- ACTION_GETINVENTORY
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'ACTION_GETINVENTORY',
		'true',
		4650
        FROM
            dual;

-- API_PART_REQUEST_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_PART_REQUEST_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_USER_ACCOUNT_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_USER_ACCOUNT_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_DEFER_MEL_LOGBOOK_FAULT_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_DEFER_MEL_LOGBOOK_FAULT_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_CREATE_LOGBOOK_FAULT_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_CREATE_LOGBOOK_FAULT_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_SEARCH_CONFIG_SLOT_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_SEARCH_CONFIG_SLOT_REQUEST',
		'true',
		4650
        FROM
            dual;

-- API_FAULT_SEVERITY_REQUEST
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'API_FAULT_SEVERITY_REQUEST',
		'true',
		4650
        FROM
            dual;

-- ACTION_CREATE_DEFER_REF
INSERT
	INTO
		utl_action_role_parm(
			ROLE_ID,
			PARM_NAME,
			PARM_VALUE,
			UTL_ID
		)
	SELECT
		(SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'LINECTRL'),
		'ACTION_CREATE_DEFER_REF',
		'true',
		4650
        FROM
            dual;
