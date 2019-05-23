/******************************************
** 10-Level INSERT SCRIPT FOR UTL_TODO_LIST
*******************************************/
-- Maintenance Controller To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10001, 10000, 'To Do List', 'Maintenance Controller', 'Main to do list', 1, 10);

-- Production Controller To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10002, 10006, 'To Do List', 'Production Controller', 'Main to do list', 1, 10);

-- Line Supervisor To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10003, 10001, 'To Do List', 'Line Supervisor', 'Main to do list', 1, 10);

-- Line Technician To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10004, 10011, 'To Do List', 'Line Technician', 'Main to do list', 1, 10);

-- Shop Controller To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10005, 10004, 'To Do List', 'Shop Controller', 'Main to do list', 1, 10);

-- Engineer To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10006, 10009, 'To Do List', 'Engineer', 'Main to do list', 1, 10);

-- Progress Monitor To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10007, 10010, 'To Do List', 'Progress Monitor', 'Main to do list', 1, 10);

-- Heavy Technician To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10008, 10002, 'To Do List', 'Heavy Technician', 'Main to do list', 1, 10);

-- Quality Control Inspector To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10010, 10013, 'To Do List', 'Quality Control Inspector', 'Main to do list', 1, 10);

-- Technical Records To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10011, 10014, 'To Do List', 'Technical Records', 'Main to do list', 1, 10);

-- Shop Technician To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10013, 10015, 'To Do List', 'Shop Technician', 'Main to do list', 1, 10);

-- Repair Router To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10014, 10021, 'To Do List', 'Repair Router', 'Main to do list', 1, 10);

-- Material Control To Do List (To Do List 1)
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10015, 10020, 'Control To Do List', 'Material Controller', 'To do list with controller type information', 1, 10);

-- Administration To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10016, 19000, 'web.todolist.TO_DO_LIST', 'web.todolist.ADMINISTRATION', 'web.todolist.ADMINISTRATION_TO_DO_LIST', 1, 10);

-- Storeroom Clerk To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10017, 10017, 'To Do List', 'Storeroom Clerk', 'Main to do list', 1, 10);

-- Purchasing Agent To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10018, 10018, 'To Do List', 'Purchasing Agent', 'Main to do list', 1, 10);

-- Purchasing Manager To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10019, 10019, 'To Do List', 'Purchasing Manager', 'Main to do list', 1, 10);

--  Storeroom Clerk To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10021, 10017, 'Issue To Do List', 'Storeroom Clerk', 'Issue to do list', 2, 10);

-- Crew Lead To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10022, 10007, 'To Do List', 'Crew Lead', 'Main to do list', 1, 10);

-- Heavy Planner To Do Lists
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10023, 10023, 'To Do List', 'Heavy Planner', 'Main to do list', 1, 10);

--- Invoicing Agent To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10024, 10024, 'To Do List', 'Invoicing Agent', 'Main to do list', 1, 10);

--- Finance To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10025, 10025, 'To Do List', 'Finance', 'Main to do list', 1, 10);

-- Line Planner To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10026, 10008, 'To Do List', 'Line Planner', 'Main to do list', 1, 10);

-- Heavy Planner To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10027, 10023, 'To Do List', 'Heavy Planner', 'Main to do list', 1, 10);

-- Storeroom Supervisor To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10028, 10022, 'To Do List', 'Storeroom Supervisor', 'Main to do list', 1, 10);

-- Material Contoller To Do List (To Do List 2-5)
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10031, 10020, 'Storeroom To Do List', 'Material Controller', 'To do list with storeroom clerk type information', 2, 10);
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10032, 10020, 'Inspection To Do List', 'Material Controller', 'To do list with QC inspector type information', 3, 10);
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10030, 10020, 'Repair To Do List', 'Material Controller', 'To do list with repair router type information', 4, 10);
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10029, 10020, 'Overstock To Do List', 'Material Controller', 'To do list with overstock information', 5, 10);

-- Long Range Planner To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10033, 10029, 'web.todolist.TO_DO_LIST', 'web.todolist.LONG_RANGE_PLANNER', 'web.todolist.LONG_RANGE_PLANNER_TO_DO_LIST', 1, 10);

-- Warranty Agent To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
values (10034, 10030, 'web.todolist.TO_DO_LIST', 'web.todolist.WARRANTY_AGENT', 'web.todolist.WARRANTY_AGENT', 1, 10);


