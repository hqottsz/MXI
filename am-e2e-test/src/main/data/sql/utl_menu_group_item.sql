/**********************************************
** INSERT SCRIPT FOR UTL_MENU_GROUP_ITEM
***********************************************/

-- Purchase Manager menu group
INSERT INTO
   utl_menu_group_item (group_id, menu_id, menu_order, break_bool, utl_id)
SELECT
   10019, 120943, 7, 0, 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_menu_group_item WHERE group_id = 10019 AND menu_id=120943);


  /* Add Organization Search by Type Menu item to Administrator*/
INSERT INTO utl_menu_group_item (group_id, menu_id, menu_order, break_bool, utl_id)
SELECT
   utl_menu_group.group_id,
   utl_menu_item.menu_id,
   (
      SELECT
         MAX(utl_menu_group_item.menu_order)
      FROM
         utl_menu_group_item
         INNER JOIN utl_menu_group ON
            utl_menu_group.group_id = utl_menu_group_item.group_id
      WHERE
         utl_menu_group.group_name = 'Administrator'
   ) + 1,
   0,
   10
FROM
   utl_menu_group
   CROSS JOIN utl_menu_item
   CROSS JOIN (
      SELECT
         MAX(utl_menu_group_item.menu_order)
      FROM
         utl_menu_group_item
         INNER JOIN utl_menu_group ON
            utl_menu_group.group_id = utl_menu_group_item.group_id
      WHERE
         utl_menu_group.group_name = 'Administrator'
   )
WHERE
   utl_menu_group.group_name = 'Administrator'
   AND
   utl_menu_item.menu_name = 'web.menuitem.ORGANIZATION_SEARCH_BY_TYPE'
   AND
   NOT EXISTS (
      SELECT
         1
      FROM
         utl_menu_group_item
         INNER JOIN utl_menu_group ON
            utl_menu_group.group_id = utl_menu_group_item.group_id
         INNER JOIN utl_menu_item ON
            utl_menu_item.menu_id = utl_menu_group_item.menu_id
      WHERE
         utl_menu_group.group_name = 'Administrator'
         AND
         utl_menu_item.menu_name = 'web.menuitem.ORGANIZATION_SEARCH_BY_TYPE'
   );

--put Manage Aircraft Groups into Line Planner Menu
INSERT INTO
   utl_menu_group_item (group_id, menu_id, menu_order, break_bool, utl_id)
SELECT
   10023, 120942, 5, 0, 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_menu_group_item WHERE group_id = 10023 AND menu_id=120942);

--put Flight Search into Line Planner Menu
INSERT INTO
   utl_menu_group_item (group_id, menu_id, menu_order, break_bool, utl_id)
SELECT
   10023, 10109, 6, 0, 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_menu_group_item WHERE group_id = 10023 AND menu_id=10109);


-- Heavy Planner menu group
-- assign Work Package Loader menu
INSERT INTO
   utl_menu_group_item (group_id, menu_id, menu_order, break_bool, utl_id)
SELECT
   10024, 120949, 6, 0, 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_menu_group_item WHERE group_id = 10024 AND menu_id=120949);
