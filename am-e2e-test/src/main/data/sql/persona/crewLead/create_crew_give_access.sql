/* creat two crews*/
insert into org_work_dept(dept_db_id, dept_id, dept_cd, dept_type_db_id,dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc) values (4650,100010, 'CREW001',0,'CREW',0,1,'crew-001','test auto dept');
insert into org_work_dept(dept_db_id, dept_id, dept_cd, dept_type_db_id,dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc) values (4650,100011, 'CREW002',0,'CREW',0,1,'crew-002','test auto dept');
/* insert user1 to crew001*/
insert into org_dept_hr(dept_db_id, dept_id, hr_db_id, hr_id) values(4650,100010,(SELECT oh.HR_DB_ID FROM org_hr oh INNER JOIN utl_user uu ON oh.USER_ID = uu.USER_ID WHERE uu.USERNAME = 'user1'),(SELECT oh.HR_ID FROM org_hr oh INNER JOIN utl_user uu ON oh.USER_ID = uu.USER_ID WHERE uu.USERNAME = 'user1'));
/* we give access of “Department Search” and “User Search” to crew lead*/
insert into utl_menu_group_item (group_id, menu_id, menu_order, break_bool, utl_id) values (10007,10115,2,0,10);
insert into utl_menu_group_item (group_id, menu_id, menu_order, break_bool, utl_id) values (10007,10114,3,0,10);