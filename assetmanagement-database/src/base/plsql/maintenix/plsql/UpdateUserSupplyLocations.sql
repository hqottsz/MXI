--liquibase formatted sql


--changeSet UpdateUserSupplyLocations:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:      UPDATE_USER_SUPPLY_LOCATIONS
* Arguments:     aUserId             - Primary key of the utl_user
*                aHrDbId   : aHrId   - Primary key of the org_hr
*                aLocDbId  : aLocId  - Primary key of the inv_loc
*
* Description:   This procedure will refresh the contents of the org_hr_supply table
*		 if no arguments are specified, it will refresh the whole table, otherwise
*		 the refresh will filter on the specified parms.
*
* Orig.Coder:    jbajer
* Recent Coder:  
* Recent Date:   2009.09.17
*********************************************************************************/
CREATE OR REPLACE PROCEDURE UPDATE_USER_SUPPLY_LOCATIONS (
an_UserId         utl_user.user_id%TYPE,
an_HrDbId         org_hr.hr_db_id%TYPE,
an_HrId           org_hr.hr_id%TYPE,
an_LocDbId        inv_loc.loc_db_id%TYPE,
an_LocId          inv_loc.loc_id%TYPE,
on_Return         OUT NUMBER
) IS

   ls_Current        VARCHAR2(32767);
   ls_Table          VARCHAR2(32767);

   ls_Query          VARCHAR2(32767);
   lc_Cursor         SYS_REFCURSOR;

   ln_LocDbId        inv_loc.loc_db_id%TYPE;
   ln_LocId          inv_loc.loc_id%TYPE;
   ln_HrDbId         org_hr.hr_db_id%TYPE;
   ln_HrId           org_hr.hr_id%TYPE;
   ln_UserId         utl_user.user_id%TYPE;

 BEGIN
     on_Return := 0;

     ls_Current :=
     'SELECT                                                         ' ||
     '       inv_loc.supply_loc_db_id AS loc_db_id,                  ' ||
     '       inv_loc.supply_loc_id    AS loc_id,                     ' ||
     '       org_hr.hr_db_id,                                        ' ||
     '       org_hr.hr_id,                                           ' ||
     '       utl_user.user_id                                        ' ||
     'FROM                                                           ' ||
     '       inv_loc,                                                ' ||
     '       inv_loc_dept,                                           ' ||
     '       org_dept_hr,                                            ' ||
     '       org_hr,                                                 ' ||
     '       utl_user                                                ' ||
     'WHERE                                                          ' ||
     '       org_hr.user_id = utl_user.user_id                       ' ||
     '       AND                                                     ' ||
     '       org_dept_hr.hr_db_id = org_hr.hr_db_id AND              ' ||
     '       org_dept_hr.hr_id    = org_hr.hr_id                     ' ||
     '       AND                                                     ' ||
     '       inv_loc_dept.dept_db_id = org_dept_hr.dept_db_id AND    ' ||
     '       inv_loc_dept.dept_id    = org_dept_hr.dept_id           ' ||
     '       AND                                                     ' ||
     '       inv_loc.loc_db_id     = inv_loc_dept.loc_db_id AND      ' ||
     '       inv_loc.loc_id        = inv_loc_dept.loc_id    AND      ' ||
     '       inv_loc.supply_loc_id IS NOT NULL                       ';


     ls_Table :=
     'SELECT org_hr_supply.loc_db_id,                                ' ||
     '       org_hr_supply.loc_id,                                   ' ||
     '       org_hr_supply.hr_db_id,                                 ' ||
     '       org_hr_supply.hr_id,                                    ' ||
     '       org_hr_supply.user_id                                   ' ||
     'FROM                                                           ' ||
     '       org_hr_supply                                           ';

     -- build the select clause according to the arguments passed in
     IF an_UserId IS NOT NULL THEN
        ls_Current := ls_Current || ' AND utl_user.user_id = ' || an_UserId;

        ls_Table := ls_Table || ' WHERE org_hr_supply.user_id = ' || an_UserId;

     ELSIF an_HrDbId IS NOT NULL AND an_HrId IS NOT NULL THEN
        ls_Current := ls_Current || ' AND org_hr.hr_db_id = ' || an_HrDbId;
        ls_Current := ls_Current || ' AND org_hr.hr_id    = ' || an_HrId;

        ls_Table := ls_Table || ' WHERE org_hr_supply.hr_db_id = ' || an_HrDbId;
        ls_Table := ls_Table || '   AND org_hr_supply.hr_id    = ' || an_HrId;

     ELSIF an_LocDbId IS NOT NULL AND an_LocId IS NOT NULL THEN
        SELECT inv_loc.supply_loc_db_id,
               inv_loc.supply_loc_id
        INTO   ln_LocDbId,
               ln_LocId
        FROM   inv_loc
        WHERE  inv_loc.loc_db_id = an_LocDbId AND
               inv_loc.loc_id    = an_LocId;
        IF ln_LocDbId IS NULL THEN
           RETURN;
        END IF;
        ls_Current := ls_Current || ' AND inv_loc.supply_loc_db_id = ' || ln_LocDbId;
        ls_Current := ls_Current || ' AND inv_loc.supply_loc_id    = ' || ln_LocId;

        ls_Table := ls_Table || ' WHERE org_hr_supply.loc_db_id = ' || ln_LocDbId;
        ls_Table := ls_Table || '   AND org_hr_supply.loc_id    = ' || ln_LocId;

     END IF;


     -- Delete from the table, rows that should no longer be there
     ls_Query := ls_Table || ' MINUS ' || ls_Current;

     OPEN lc_Cursor FOR ls_Query ;
     LOOP
     FETCH lc_Cursor INTO ln_LocDbId ,
                          ln_LocId   ,
                          ln_HrDbId  ,
                          ln_HrId    ,
                          ln_UserId  ;

        EXIT WHEN lc_Cursor%NOTFOUND;

        DELETE
        FROM   org_hr_supply
        WHERE  org_hr_supply.user_id    = ln_UserId
               AND
               org_hr_supply.hr_db_id   = ln_HrDbId AND
               org_hr_supply.hr_id      = ln_HrId
               AND
               org_hr_supply.loc_db_id  = ln_LocDbId AND
               org_hr_supply.loc_id     = ln_LocId;

        -- track how many records were inserted/deleted
        on_Return := on_Return + 1;

     END LOOP;
     CLOSE lc_Cursor;

     -- Insert into the table values that are missing
     ls_Query := ls_Current || ' MINUS ' || ls_Table;

     OPEN lc_Cursor FOR ls_Query;
     LOOP
     FETCH lc_Cursor INTO ln_LocDbId ,
                          ln_LocId   ,
                          ln_HrDbId  ,
                          ln_HrId    ,
                          ln_UserId  ;

        EXIT WHEN lc_Cursor%NOTFOUND;

         INSERT INTO org_hr_supply (
                user_id,
                hr_db_id,
                hr_id,
                loc_db_id,
                loc_id
                )
         VALUES (
                ln_UserId,
                ln_HrDbId,
                ln_HrId,
                ln_LocDbId,
                ln_LocId
                );
        -- track how many records were inserted/deleted
        on_Return := on_Return + 1;

     END LOOP;
     CLOSE lc_Cursor;

EXCEPTION
   WHEN OTHERS THEN

    on_Return := -1;
    APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','MX_CORE_UPDATEUSERSUPPLYLOCATIONS@@@UPDATE_USER_SUPPLY_LOCATIONS@@@'||SQLERRM);
      /* close dynamic cursor */
      IF lc_Cursor%ISOPEN THEN
           CLOSE lc_Cursor;
      END IF;

END UPDATE_USER_SUPPLY_LOCATIONS;
/