-- Create Crew Lead user.
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
            'crewlead',
            'password',
            'crew',
            'lead',
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
        (SELECT USER_ID FROM UTL_USER WHERE USERNAME = 'crewlead'),
        'BMETEST',
        0,
        'SALARY'
 FROM
            dual;

-- Create an association between the organisation user record and an organisation. Uses the pre-existing ADMIN organisation.
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
        (SELECT oh.HR_ID FROM org_hr oh INNER JOIN utl_user uu ON oh.USER_ID = uu.USER_ID WHERE uu.USERNAME = 'crewlead' AND oh.HR_CD = 'BMETEST'),
        0,
        1,
        1
    FROM
        dual;

-- Link the user to the Heavy Lead role.
INSERT
    INTO
        UTL_USER_ROLE(
            USER_ID,
            ROLE_ID,
            ROLE_ORDER,
            UTL_ID
        ) SELECT
            (SELECT USER_ID FROM UTL_USER WHERE USERNAME = 'crewlead'),
            (SELECT ROLE_ID FROM UTL_ROLE WHERE ROLE_CD = 'HVYLEAD'),
            0,
            4650
        FROM
            dual;
