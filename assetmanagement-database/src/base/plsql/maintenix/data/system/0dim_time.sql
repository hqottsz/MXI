--liquibase formatted sql


--changeSet 0dim_time:1 stripComments:false
/***************************************************************
** INSERT SCRIPT FOR TABLE "DIM_TIME"
** NOTE: Populates the dim_time table with 5 years worth of 
**       time dimentsion information, beginning Jan 01, 2009, 
**       with a granularity of days.
** DATE: Dec 11 2009
****************************************************************/
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090101, '2009-01-01 00:00:00',4,1,1,1,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:2 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090102, '2009-01-02 00:00:00',5,2,2,1,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:3 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090103, '2009-01-03 00:00:00',6,3,3,1,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:4 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090104, '2009-01-04 00:00:00',7,4,4,1,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:5 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090105, '2009-01-05 00:00:00',1,5,5,2,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:6 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090106, '2009-01-06 00:00:00',2,6,6,2,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:7 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090107, '2009-01-07 00:00:00',3,7,7,2,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:8 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090108, '2009-01-08 00:00:00',4,8,8,2,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:9 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090109, '2009-01-09 00:00:00',5,9,9,2,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:10 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090110, '2009-01-10 00:00:00',6,10,10,2,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:11 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090111, '2009-01-11 00:00:00',7,11,11,2,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:12 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090112, '2009-01-12 00:00:00',1,12,12,3,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:13 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090113, '2009-01-13 00:00:00',2,13,13,3,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:14 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090114, '2009-01-14 00:00:00',3,14,14,3,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:15 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090115, '2009-01-15 00:00:00',4,15,15,3,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:16 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090116, '2009-01-16 00:00:00',5,16,16,3,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:17 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090117, '2009-01-17 00:00:00',6,17,17,3,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:18 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090118, '2009-01-18 00:00:00',7,18,18,3,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:19 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090119, '2009-01-19 00:00:00',1,19,19,4,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:20 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090120, '2009-01-20 00:00:00',2,20,20,4,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:21 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090121, '2009-01-21 00:00:00',3,21,21,4,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:22 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090122, '2009-01-22 00:00:00',4,22,22,4,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:23 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090123, '2009-01-23 00:00:00',5,23,23,4,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:24 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090124, '2009-01-24 00:00:00',6,24,24,4,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:25 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090125, '2009-01-25 00:00:00',7,25,25,4,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:26 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090126, '2009-01-26 00:00:00',1,26,26,5,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:27 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090127, '2009-01-27 00:00:00',2,27,27,5,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:28 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090128, '2009-01-28 00:00:00',3,28,28,5,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:29 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090129, '2009-01-29 00:00:00',4,29,29,5,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:30 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090130, '2009-01-30 00:00:00',5,30,30,5,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:31 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090131, '2009-01-31 00:00:00',6,31,31,5,1,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:32 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090201, '2009-02-01 00:00:00',7,1,32,5,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:33 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090202, '2009-02-02 00:00:00',1,2,33,6,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:34 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090203, '2009-02-03 00:00:00',2,3,34,6,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:35 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090204, '2009-02-04 00:00:00',3,4,35,6,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:36 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090205, '2009-02-05 00:00:00',4,5,36,6,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:37 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090206, '2009-02-06 00:00:00',5,6,37,6,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:38 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090207, '2009-02-07 00:00:00',6,7,38,6,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:39 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090208, '2009-02-08 00:00:00',7,8,39,6,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:40 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090209, '2009-02-09 00:00:00',1,9,40,7,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:41 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090210, '2009-02-10 00:00:00',2,10,41,7,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:42 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090211, '2009-02-11 00:00:00',3,11,42,7,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:43 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090212, '2009-02-12 00:00:00',4,12,43,7,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:44 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090213, '2009-02-13 00:00:00',5,13,44,7,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:45 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090214, '2009-02-14 00:00:00',6,14,45,7,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:46 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090215, '2009-02-15 00:00:00',7,15,46,7,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:47 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090216, '2009-02-16 00:00:00',1,16,47,8,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:48 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090217, '2009-02-17 00:00:00',2,17,48,8,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:49 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090218, '2009-02-18 00:00:00',3,18,49,8,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:50 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090219, '2009-02-19 00:00:00',4,19,50,8,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:51 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090220, '2009-02-20 00:00:00',5,20,51,8,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:52 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090221, '2009-02-21 00:00:00',6,21,52,8,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:53 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090222, '2009-02-22 00:00:00',7,22,53,8,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:54 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090223, '2009-02-23 00:00:00',1,23,54,9,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:55 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090224, '2009-02-24 00:00:00',2,24,55,9,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:56 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090225, '2009-02-25 00:00:00',3,25,56,9,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:57 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090226, '2009-02-26 00:00:00',4,26,57,9,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:58 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090227, '2009-02-27 00:00:00',5,27,58,9,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:59 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090228, '2009-02-28 00:00:00',6,28,59,9,2,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:60 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090301, '2009-03-01 00:00:00',7,1,60,9,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:61 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090302, '2009-03-02 00:00:00',1,2,61,10,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:62 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090303, '2009-03-03 00:00:00',2,3,62,10,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:63 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090304, '2009-03-04 00:00:00',3,4,63,10,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:64 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090305, '2009-03-05 00:00:00',4,5,64,10,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:65 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090306, '2009-03-06 00:00:00',5,6,65,10,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:66 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090307, '2009-03-07 00:00:00',6,7,66,10,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:67 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090308, '2009-03-08 00:00:00',7,8,67,10,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:68 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090309, '2009-03-09 00:00:00',1,9,68,11,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:69 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090310, '2009-03-10 00:00:00',2,10,69,11,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:70 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090311, '2009-03-11 00:00:00',3,11,70,11,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:71 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090312, '2009-03-12 00:00:00',4,12,71,11,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:72 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090313, '2009-03-13 00:00:00',5,13,72,11,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:73 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090314, '2009-03-14 00:00:00',6,14,73,11,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:74 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090315, '2009-03-15 00:00:00',7,15,74,11,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:75 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090316, '2009-03-16 00:00:00',1,16,75,12,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:76 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090317, '2009-03-17 00:00:00',2,17,76,12,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:77 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090318, '2009-03-18 00:00:00',3,18,77,12,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:78 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090319, '2009-03-19 00:00:00',4,19,78,12,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:79 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090320, '2009-03-20 00:00:00',5,20,79,12,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:80 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090321, '2009-03-21 00:00:00',6,21,80,12,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:81 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090322, '2009-03-22 00:00:00',7,22,81,12,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:82 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090323, '2009-03-23 00:00:00',1,23,82,13,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:83 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090324, '2009-03-24 00:00:00',2,24,83,13,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:84 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090325, '2009-03-25 00:00:00',3,25,84,13,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:85 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090326, '2009-03-26 00:00:00',4,26,85,13,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:86 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090327, '2009-03-27 00:00:00',5,27,86,13,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:87 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090328, '2009-03-28 00:00:00',6,28,87,13,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:88 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090329, '2009-03-29 00:00:00',7,29,88,13,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:89 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090330, '2009-03-30 00:00:00',1,30,89,14,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:90 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090331, '2009-03-31 00:00:00',2,31,90,14,3,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:91 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090401, '2009-04-01 00:00:00',3,1,91,14,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:92 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090402, '2009-04-02 00:00:00',4,2,92,14,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:93 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090403, '2009-04-03 00:00:00',5,3,93,14,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:94 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090404, '2009-04-04 00:00:00',6,4,94,14,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:95 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090405, '2009-04-05 00:00:00',7,5,95,14,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:96 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090406, '2009-04-06 00:00:00',1,6,96,15,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:97 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090407, '2009-04-07 00:00:00',2,7,97,15,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:98 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090408, '2009-04-08 00:00:00',3,8,98,15,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:99 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090409, '2009-04-09 00:00:00',4,9,99,15,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:100 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090410, '2009-04-10 00:00:00',5,10,100,15,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:101 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090411, '2009-04-11 00:00:00',6,11,101,15,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:102 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090412, '2009-04-12 00:00:00',7,12,102,15,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:103 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090413, '2009-04-13 00:00:00',1,13,103,16,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:104 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090414, '2009-04-14 00:00:00',2,14,104,16,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:105 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090415, '2009-04-15 00:00:00',3,15,105,16,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:106 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090416, '2009-04-16 00:00:00',4,16,106,16,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:107 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090417, '2009-04-17 00:00:00',5,17,107,16,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:108 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090418, '2009-04-18 00:00:00',6,18,108,16,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:109 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090419, '2009-04-19 00:00:00',7,19,109,16,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:110 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090420, '2009-04-20 00:00:00',1,20,110,17,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:111 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090421, '2009-04-21 00:00:00',2,21,111,17,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:112 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090422, '2009-04-22 00:00:00',3,22,112,17,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:113 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090423, '2009-04-23 00:00:00',4,23,113,17,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:114 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090424, '2009-04-24 00:00:00',5,24,114,17,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:115 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090425, '2009-04-25 00:00:00',6,25,115,17,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:116 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090426, '2009-04-26 00:00:00',7,26,116,17,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:117 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090427, '2009-04-27 00:00:00',1,27,117,18,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:118 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090428, '2009-04-28 00:00:00',2,28,118,18,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:119 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090429, '2009-04-29 00:00:00',3,29,119,18,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:120 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090430, '2009-04-30 00:00:00',4,30,120,18,4,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:121 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090501, '2009-05-01 00:00:00',5,1,121,18,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:122 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090502, '2009-05-02 00:00:00',6,2,122,18,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:123 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090503, '2009-05-03 00:00:00',7,3,123,18,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:124 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090504, '2009-05-04 00:00:00',1,4,124,19,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:125 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090505, '2009-05-05 00:00:00',2,5,125,19,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:126 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090506, '2009-05-06 00:00:00',3,6,126,19,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:127 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090507, '2009-05-07 00:00:00',4,7,127,19,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:128 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090508, '2009-05-08 00:00:00',5,8,128,19,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:129 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090509, '2009-05-09 00:00:00',6,9,129,19,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:130 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090510, '2009-05-10 00:00:00',7,10,130,19,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:131 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090511, '2009-05-11 00:00:00',1,11,131,20,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:132 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090512, '2009-05-12 00:00:00',2,12,132,20,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:133 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090513, '2009-05-13 00:00:00',3,13,133,20,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:134 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090514, '2009-05-14 00:00:00',4,14,134,20,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:135 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090515, '2009-05-15 00:00:00',5,15,135,20,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:136 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090516, '2009-05-16 00:00:00',6,16,136,20,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:137 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090517, '2009-05-17 00:00:00',7,17,137,20,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:138 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090518, '2009-05-18 00:00:00',1,18,138,21,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:139 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090519, '2009-05-19 00:00:00',2,19,139,21,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:140 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090520, '2009-05-20 00:00:00',3,20,140,21,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:141 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090521, '2009-05-21 00:00:00',4,21,141,21,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:142 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090522, '2009-05-22 00:00:00',5,22,142,21,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:143 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090523, '2009-05-23 00:00:00',6,23,143,21,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:144 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090524, '2009-05-24 00:00:00',7,24,144,21,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:145 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090525, '2009-05-25 00:00:00',1,25,145,22,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:146 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090526, '2009-05-26 00:00:00',2,26,146,22,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:147 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090527, '2009-05-27 00:00:00',3,27,147,22,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:148 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090528, '2009-05-28 00:00:00',4,28,148,22,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:149 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090529, '2009-05-29 00:00:00',5,29,149,22,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:150 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090530, '2009-05-30 00:00:00',6,30,150,22,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:151 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090531, '2009-05-31 00:00:00',7,31,151,22,5,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:152 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090601, '2009-06-01 00:00:00',1,1,152,23,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:153 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090602, '2009-06-02 00:00:00',2,2,153,23,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:154 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090603, '2009-06-03 00:00:00',3,3,154,23,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:155 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090604, '2009-06-04 00:00:00',4,4,155,23,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:156 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090605, '2009-06-05 00:00:00',5,5,156,23,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:157 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090606, '2009-06-06 00:00:00',6,6,157,23,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:158 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090607, '2009-06-07 00:00:00',7,7,158,23,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:159 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090608, '2009-06-08 00:00:00',1,8,159,24,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:160 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090609, '2009-06-09 00:00:00',2,9,160,24,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:161 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090610, '2009-06-10 00:00:00',3,10,161,24,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:162 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090611, '2009-06-11 00:00:00',4,11,162,24,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:163 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090612, '2009-06-12 00:00:00',5,12,163,24,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:164 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090613, '2009-06-13 00:00:00',6,13,164,24,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:165 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090614, '2009-06-14 00:00:00',7,14,165,24,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:166 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090615, '2009-06-15 00:00:00',1,15,166,25,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:167 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090616, '2009-06-16 00:00:00',2,16,167,25,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:168 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090617, '2009-06-17 00:00:00',3,17,168,25,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:169 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090618, '2009-06-18 00:00:00',4,18,169,25,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:170 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090619, '2009-06-19 00:00:00',5,19,170,25,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:171 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090620, '2009-06-20 00:00:00',6,20,171,25,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:172 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090621, '2009-06-21 00:00:00',7,21,172,25,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:173 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090622, '2009-06-22 00:00:00',1,22,173,26,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:174 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090623, '2009-06-23 00:00:00',2,23,174,26,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:175 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090624, '2009-06-24 00:00:00',3,24,175,26,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:176 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090625, '2009-06-25 00:00:00',4,25,176,26,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:177 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090626, '2009-06-26 00:00:00',5,26,177,26,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:178 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090627, '2009-06-27 00:00:00',6,27,178,26,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:179 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090628, '2009-06-28 00:00:00',7,28,179,26,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:180 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090629, '2009-06-29 00:00:00',1,29,180,27,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:181 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090630, '2009-06-30 00:00:00',2,30,181,27,6,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:182 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090701, '2009-07-01 00:00:00',3,1,182,27,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:183 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090702, '2009-07-02 00:00:00',4,2,183,27,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:184 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090703, '2009-07-03 00:00:00',5,3,184,27,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:185 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090704, '2009-07-04 00:00:00',6,4,185,27,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:186 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090705, '2009-07-05 00:00:00',7,5,186,27,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:187 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090706, '2009-07-06 00:00:00',1,6,187,28,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:188 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090707, '2009-07-07 00:00:00',2,7,188,28,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:189 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090708, '2009-07-08 00:00:00',3,8,189,28,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:190 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090709, '2009-07-09 00:00:00',4,9,190,28,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:191 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090710, '2009-07-10 00:00:00',5,10,191,28,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:192 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090711, '2009-07-11 00:00:00',6,11,192,28,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:193 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090712, '2009-07-12 00:00:00',7,12,193,28,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:194 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090713, '2009-07-13 00:00:00',1,13,194,29,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:195 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090714, '2009-07-14 00:00:00',2,14,195,29,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:196 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090715, '2009-07-15 00:00:00',3,15,196,29,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:197 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090716, '2009-07-16 00:00:00',4,16,197,29,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:198 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090717, '2009-07-17 00:00:00',5,17,198,29,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:199 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090718, '2009-07-18 00:00:00',6,18,199,29,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:200 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090719, '2009-07-19 00:00:00',7,19,200,29,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:201 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090720, '2009-07-20 00:00:00',1,20,201,30,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:202 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090721, '2009-07-21 00:00:00',2,21,202,30,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:203 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090722, '2009-07-22 00:00:00',3,22,203,30,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:204 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090723, '2009-07-23 00:00:00',4,23,204,30,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:205 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090724, '2009-07-24 00:00:00',5,24,205,30,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:206 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090725, '2009-07-25 00:00:00',6,25,206,30,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:207 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090726, '2009-07-26 00:00:00',7,26,207,30,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:208 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090727, '2009-07-27 00:00:00',1,27,208,31,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:209 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090728, '2009-07-28 00:00:00',2,28,209,31,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:210 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090729, '2009-07-29 00:00:00',3,29,210,31,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:211 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090730, '2009-07-30 00:00:00',4,30,211,31,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:212 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090731, '2009-07-31 00:00:00',5,31,212,31,7,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:213 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090801, '2009-08-01 00:00:00',6,1,213,31,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:214 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090802, '2009-08-02 00:00:00',7,2,214,31,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:215 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090803, '2009-08-03 00:00:00',1,3,215,32,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:216 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090804, '2009-08-04 00:00:00',2,4,216,32,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:217 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090805, '2009-08-05 00:00:00',3,5,217,32,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:218 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090806, '2009-08-06 00:00:00',4,6,218,32,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:219 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090807, '2009-08-07 00:00:00',5,7,219,32,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:220 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090808, '2009-08-08 00:00:00',6,8,220,32,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:221 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090809, '2009-08-09 00:00:00',7,9,221,32,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:222 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090810, '2009-08-10 00:00:00',1,10,222,33,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:223 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090811, '2009-08-11 00:00:00',2,11,223,33,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:224 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090812, '2009-08-12 00:00:00',3,12,224,33,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:225 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090813, '2009-08-13 00:00:00',4,13,225,33,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:226 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090814, '2009-08-14 00:00:00',5,14,226,33,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:227 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090815, '2009-08-15 00:00:00',6,15,227,33,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:228 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090816, '2009-08-16 00:00:00',7,16,228,33,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:229 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090817, '2009-08-17 00:00:00',1,17,229,34,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:230 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090818, '2009-08-18 00:00:00',2,18,230,34,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:231 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090819, '2009-08-19 00:00:00',3,19,231,34,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:232 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090820, '2009-08-20 00:00:00',4,20,232,34,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:233 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090821, '2009-08-21 00:00:00',5,21,233,34,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:234 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090822, '2009-08-22 00:00:00',6,22,234,34,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:235 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090823, '2009-08-23 00:00:00',7,23,235,34,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:236 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090824, '2009-08-24 00:00:00',1,24,236,35,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:237 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090825, '2009-08-25 00:00:00',2,25,237,35,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:238 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090826, '2009-08-26 00:00:00',3,26,238,35,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:239 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090827, '2009-08-27 00:00:00',4,27,239,35,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:240 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090828, '2009-08-28 00:00:00',5,28,240,35,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:241 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090829, '2009-08-29 00:00:00',6,29,241,35,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:242 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090830, '2009-08-30 00:00:00',7,30,242,35,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:243 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090831, '2009-08-31 00:00:00',1,31,243,36,8,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:244 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090901, '2009-09-01 00:00:00',2,1,244,36,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:245 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090902, '2009-09-02 00:00:00',3,2,245,36,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:246 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090903, '2009-09-03 00:00:00',4,3,246,36,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:247 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090904, '2009-09-04 00:00:00',5,4,247,36,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:248 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090905, '2009-09-05 00:00:00',6,5,248,36,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:249 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090906, '2009-09-06 00:00:00',7,6,249,36,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:250 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090907, '2009-09-07 00:00:00',1,7,250,37,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:251 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090908, '2009-09-08 00:00:00',2,8,251,37,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:252 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090909, '2009-09-09 00:00:00',3,9,252,37,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:253 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090910, '2009-09-10 00:00:00',4,10,253,37,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:254 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090911, '2009-09-11 00:00:00',5,11,254,37,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:255 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090912, '2009-09-12 00:00:00',6,12,255,37,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:256 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090913, '2009-09-13 00:00:00',7,13,256,37,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:257 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090914, '2009-09-14 00:00:00',1,14,257,38,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:258 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090915, '2009-09-15 00:00:00',2,15,258,38,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:259 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090916, '2009-09-16 00:00:00',3,16,259,38,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:260 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090917, '2009-09-17 00:00:00',4,17,260,38,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:261 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090918, '2009-09-18 00:00:00',5,18,261,38,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:262 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090919, '2009-09-19 00:00:00',6,19,262,38,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:263 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090920, '2009-09-20 00:00:00',7,20,263,38,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:264 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090921, '2009-09-21 00:00:00',1,21,264,39,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:265 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090922, '2009-09-22 00:00:00',2,22,265,39,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:266 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090923, '2009-09-23 00:00:00',3,23,266,39,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:267 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090924, '2009-09-24 00:00:00',4,24,267,39,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:268 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090925, '2009-09-25 00:00:00',5,25,268,39,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:269 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090926, '2009-09-26 00:00:00',6,26,269,39,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:270 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090927, '2009-09-27 00:00:00',7,27,270,39,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:271 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090928, '2009-09-28 00:00:00',1,28,271,40,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:272 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090929, '2009-09-29 00:00:00',2,29,272,40,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:273 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20090930, '2009-09-30 00:00:00',3,30,273,40,9,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:274 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091001, '2009-10-01 00:00:00',4,1,274,40,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:275 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091002, '2009-10-02 00:00:00',5,2,275,40,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:276 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091003, '2009-10-03 00:00:00',6,3,276,40,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:277 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091004, '2009-10-04 00:00:00',7,4,277,40,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:278 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091005, '2009-10-05 00:00:00',1,5,278,41,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:279 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091006, '2009-10-06 00:00:00',2,6,279,41,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:280 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091007, '2009-10-07 00:00:00',3,7,280,41,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:281 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091008, '2009-10-08 00:00:00',4,8,281,41,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:282 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091009, '2009-10-09 00:00:00',5,9,282,41,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:283 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091010, '2009-10-10 00:00:00',6,10,283,41,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:284 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091011, '2009-10-11 00:00:00',7,11,284,41,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:285 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091012, '2009-10-12 00:00:00',1,12,285,42,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:286 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091013, '2009-10-13 00:00:00',2,13,286,42,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:287 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091014, '2009-10-14 00:00:00',3,14,287,42,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:288 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091015, '2009-10-15 00:00:00',4,15,288,42,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:289 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091016, '2009-10-16 00:00:00',5,16,289,42,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:290 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091017, '2009-10-17 00:00:00',6,17,290,42,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:291 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091018, '2009-10-18 00:00:00',7,18,291,42,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:292 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091019, '2009-10-19 00:00:00',1,19,292,43,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:293 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091020, '2009-10-20 00:00:00',2,20,293,43,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:294 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091021, '2009-10-21 00:00:00',3,21,294,43,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:295 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091022, '2009-10-22 00:00:00',4,22,295,43,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:296 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091023, '2009-10-23 00:00:00',5,23,296,43,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:297 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091024, '2009-10-24 00:00:00',6,24,297,43,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:298 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091025, '2009-10-25 00:00:00',7,25,298,43,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:299 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091026, '2009-10-26 00:00:00',1,26,299,44,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:300 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091027, '2009-10-27 00:00:00',2,27,300,44,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:301 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091028, '2009-10-28 00:00:00',3,28,301,44,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:302 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091029, '2009-10-29 00:00:00',4,29,302,44,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:303 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091030, '2009-10-30 00:00:00',5,30,303,44,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:304 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091031, '2009-10-31 00:00:00',6,31,304,44,10,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:305 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091101, '2009-11-01 00:00:00',7,1,305,44,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:306 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091102, '2009-11-02 00:00:00',1,2,306,45,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:307 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091103, '2009-11-03 00:00:00',2,3,307,45,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:308 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091104, '2009-11-04 00:00:00',3,4,308,45,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:309 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091105, '2009-11-05 00:00:00',4,5,309,45,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:310 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091106, '2009-11-06 00:00:00',5,6,310,45,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:311 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091107, '2009-11-07 00:00:00',6,7,311,45,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:312 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091108, '2009-11-08 00:00:00',7,8,312,45,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:313 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091109, '2009-11-09 00:00:00',1,9,313,46,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:314 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091110, '2009-11-10 00:00:00',2,10,314,46,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:315 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091111, '2009-11-11 00:00:00',3,11,315,46,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:316 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091112, '2009-11-12 00:00:00',4,12,316,46,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:317 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091113, '2009-11-13 00:00:00',5,13,317,46,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:318 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091114, '2009-11-14 00:00:00',6,14,318,46,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:319 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091115, '2009-11-15 00:00:00',7,15,319,46,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:320 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091116, '2009-11-16 00:00:00',1,16,320,47,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:321 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091117, '2009-11-17 00:00:00',2,17,321,47,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:322 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091118, '2009-11-18 00:00:00',3,18,322,47,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:323 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091119, '2009-11-19 00:00:00',4,19,323,47,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:324 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091120, '2009-11-20 00:00:00',5,20,324,47,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:325 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091121, '2009-11-21 00:00:00',6,21,325,47,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:326 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091122, '2009-11-22 00:00:00',7,22,326,47,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:327 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091123, '2009-11-23 00:00:00',1,23,327,48,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:328 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091124, '2009-11-24 00:00:00',2,24,328,48,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:329 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091125, '2009-11-25 00:00:00',3,25,329,48,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:330 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091126, '2009-11-26 00:00:00',4,26,330,48,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:331 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091127, '2009-11-27 00:00:00',5,27,331,48,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:332 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091128, '2009-11-28 00:00:00',6,28,332,48,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:333 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091129, '2009-11-29 00:00:00',7,29,333,48,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:334 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091130, '2009-11-30 00:00:00',1,30,334,49,11,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:335 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091201, '2009-12-01 00:00:00',2,1,335,49,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:336 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091202, '2009-12-02 00:00:00',3,2,336,49,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:337 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091203, '2009-12-03 00:00:00',4,3,337,49,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:338 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091204, '2009-12-04 00:00:00',5,4,338,49,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:339 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091205, '2009-12-05 00:00:00',6,5,339,49,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:340 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091206, '2009-12-06 00:00:00',7,6,340,49,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:341 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091207, '2009-12-07 00:00:00',1,7,341,50,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:342 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091208, '2009-12-08 00:00:00',2,8,342,50,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:343 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091209, '2009-12-09 00:00:00',3,9,343,50,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:344 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091210, '2009-12-10 00:00:00',4,10,344,50,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:345 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091211, '2009-12-11 00:00:00',5,11,345,50,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:346 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091212, '2009-12-12 00:00:00',6,12,346,50,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:347 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091213, '2009-12-13 00:00:00',7,13,347,50,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:348 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091214, '2009-12-14 00:00:00',1,14,348,51,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:349 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091215, '2009-12-15 00:00:00',2,15,349,51,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:350 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091216, '2009-12-16 00:00:00',3,16,350,51,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:351 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091217, '2009-12-17 00:00:00',4,17,351,51,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:352 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091218, '2009-12-18 00:00:00',5,18,352,51,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:353 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091219, '2009-12-19 00:00:00',6,19,353,51,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:354 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091220, '2009-12-20 00:00:00',7,20,354,51,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:355 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091221, '2009-12-21 00:00:00',1,21,355,52,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:356 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091222, '2009-12-22 00:00:00',2,22,356,52,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:357 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091223, '2009-12-23 00:00:00',3,23,357,52,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:358 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091224, '2009-12-24 00:00:00',4,24,358,52,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:359 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091225, '2009-12-25 00:00:00',5,25,359,52,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:360 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091226, '2009-12-26 00:00:00',6,26,360,52,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:361 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091227, '2009-12-27 00:00:00',7,27,361,52,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:362 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091228, '2009-12-28 00:00:00',1,28,362,53,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:363 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091229, '2009-12-29 00:00:00',2,29,363,53,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:364 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091230, '2009-12-30 00:00:00',3,30,364,53,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:365 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20091231, '2009-12-31 00:00:00',4,31,365,53,12,2009,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:366 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100101, '2010-01-01 00:00:00',5,1,1,53,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:367 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100102, '2010-01-02 00:00:00',6,2,2,53,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:368 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100103, '2010-01-03 00:00:00',7,3,3,53,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:369 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100104, '2010-01-04 00:00:00',1,4,4,1,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:370 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100105, '2010-01-05 00:00:00',2,5,5,1,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:371 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100106, '2010-01-06 00:00:00',3,6,6,1,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:372 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100107, '2010-01-07 00:00:00',4,7,7,1,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:373 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100108, '2010-01-08 00:00:00',5,8,8,1,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:374 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100109, '2010-01-09 00:00:00',6,9,9,1,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:375 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100110, '2010-01-10 00:00:00',7,10,10,1,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:376 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100111, '2010-01-11 00:00:00',1,11,11,2,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:377 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100112, '2010-01-12 00:00:00',2,12,12,2,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:378 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100113, '2010-01-13 00:00:00',3,13,13,2,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:379 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100114, '2010-01-14 00:00:00',4,14,14,2,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:380 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100115, '2010-01-15 00:00:00',5,15,15,2,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:381 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100116, '2010-01-16 00:00:00',6,16,16,2,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:382 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100117, '2010-01-17 00:00:00',7,17,17,2,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:383 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100118, '2010-01-18 00:00:00',1,18,18,3,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:384 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100119, '2010-01-19 00:00:00',2,19,19,3,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:385 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100120, '2010-01-20 00:00:00',3,20,20,3,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:386 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100121, '2010-01-21 00:00:00',4,21,21,3,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:387 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100122, '2010-01-22 00:00:00',5,22,22,3,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:388 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100123, '2010-01-23 00:00:00',6,23,23,3,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:389 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100124, '2010-01-24 00:00:00',7,24,24,3,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:390 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100125, '2010-01-25 00:00:00',1,25,25,4,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:391 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100126, '2010-01-26 00:00:00',2,26,26,4,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:392 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100127, '2010-01-27 00:00:00',3,27,27,4,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:393 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100128, '2010-01-28 00:00:00',4,28,28,4,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:394 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100129, '2010-01-29 00:00:00',5,29,29,4,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:395 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100130, '2010-01-30 00:00:00',6,30,30,4,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:396 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100131, '2010-01-31 00:00:00',7,31,31,4,1,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:397 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100201, '2010-02-01 00:00:00',1,1,32,5,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:398 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100202, '2010-02-02 00:00:00',2,2,33,5,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:399 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100203, '2010-02-03 00:00:00',3,3,34,5,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:400 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100204, '2010-02-04 00:00:00',4,4,35,5,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:401 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100205, '2010-02-05 00:00:00',5,5,36,5,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:402 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100206, '2010-02-06 00:00:00',6,6,37,5,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:403 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100207, '2010-02-07 00:00:00',7,7,38,5,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:404 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100208, '2010-02-08 00:00:00',1,8,39,6,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:405 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100209, '2010-02-09 00:00:00',2,9,40,6,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:406 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100210, '2010-02-10 00:00:00',3,10,41,6,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:407 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100211, '2010-02-11 00:00:00',4,11,42,6,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:408 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100212, '2010-02-12 00:00:00',5,12,43,6,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:409 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100213, '2010-02-13 00:00:00',6,13,44,6,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:410 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100214, '2010-02-14 00:00:00',7,14,45,6,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:411 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100215, '2010-02-15 00:00:00',1,15,46,7,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:412 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100216, '2010-02-16 00:00:00',2,16,47,7,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:413 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100217, '2010-02-17 00:00:00',3,17,48,7,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:414 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100218, '2010-02-18 00:00:00',4,18,49,7,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:415 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100219, '2010-02-19 00:00:00',5,19,50,7,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:416 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100220, '2010-02-20 00:00:00',6,20,51,7,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:417 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100221, '2010-02-21 00:00:00',7,21,52,7,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:418 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100222, '2010-02-22 00:00:00',1,22,53,8,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:419 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100223, '2010-02-23 00:00:00',2,23,54,8,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:420 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100224, '2010-02-24 00:00:00',3,24,55,8,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:421 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100225, '2010-02-25 00:00:00',4,25,56,8,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:422 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100226, '2010-02-26 00:00:00',5,26,57,8,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:423 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100227, '2010-02-27 00:00:00',6,27,58,8,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:424 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100228, '2010-02-28 00:00:00',7,28,59,8,2,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:425 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100301, '2010-03-01 00:00:00',1,1,60,9,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:426 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100302, '2010-03-02 00:00:00',2,2,61,9,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:427 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100303, '2010-03-03 00:00:00',3,3,62,9,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:428 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100304, '2010-03-04 00:00:00',4,4,63,9,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:429 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100305, '2010-03-05 00:00:00',5,5,64,9,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:430 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100306, '2010-03-06 00:00:00',6,6,65,9,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:431 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100307, '2010-03-07 00:00:00',7,7,66,9,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:432 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100308, '2010-03-08 00:00:00',1,8,67,10,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:433 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100309, '2010-03-09 00:00:00',2,9,68,10,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:434 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100310, '2010-03-10 00:00:00',3,10,69,10,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:435 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100311, '2010-03-11 00:00:00',4,11,70,10,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:436 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100312, '2010-03-12 00:00:00',5,12,71,10,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:437 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100313, '2010-03-13 00:00:00',6,13,72,10,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:438 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100314, '2010-03-14 00:00:00',7,14,73,10,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:439 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100315, '2010-03-15 00:00:00',1,15,74,11,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:440 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100316, '2010-03-16 00:00:00',2,16,75,11,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:441 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100317, '2010-03-17 00:00:00',3,17,76,11,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:442 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100318, '2010-03-18 00:00:00',4,18,77,11,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:443 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100319, '2010-03-19 00:00:00',5,19,78,11,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:444 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100320, '2010-03-20 00:00:00',6,20,79,11,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:445 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100321, '2010-03-21 00:00:00',7,21,80,11,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:446 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100322, '2010-03-22 00:00:00',1,22,81,12,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:447 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100323, '2010-03-23 00:00:00',2,23,82,12,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:448 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100324, '2010-03-24 00:00:00',3,24,83,12,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:449 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100325, '2010-03-25 00:00:00',4,25,84,12,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:450 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100326, '2010-03-26 00:00:00',5,26,85,12,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:451 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100327, '2010-03-27 00:00:00',6,27,86,12,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:452 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100328, '2010-03-28 00:00:00',7,28,87,12,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:453 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100329, '2010-03-29 00:00:00',1,29,88,13,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:454 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100330, '2010-03-30 00:00:00',2,30,89,13,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:455 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100331, '2010-03-31 00:00:00',3,31,90,13,3,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:456 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100401, '2010-04-01 00:00:00',4,1,91,13,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:457 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100402, '2010-04-02 00:00:00',5,2,92,13,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:458 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100403, '2010-04-03 00:00:00',6,3,93,13,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:459 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100404, '2010-04-04 00:00:00',7,4,94,13,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:460 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100405, '2010-04-05 00:00:00',1,5,95,14,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:461 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100406, '2010-04-06 00:00:00',2,6,96,14,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:462 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100407, '2010-04-07 00:00:00',3,7,97,14,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:463 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100408, '2010-04-08 00:00:00',4,8,98,14,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:464 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100409, '2010-04-09 00:00:00',5,9,99,14,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:465 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100410, '2010-04-10 00:00:00',6,10,100,14,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:466 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100411, '2010-04-11 00:00:00',7,11,101,14,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:467 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100412, '2010-04-12 00:00:00',1,12,102,15,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:468 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100413, '2010-04-13 00:00:00',2,13,103,15,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:469 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100414, '2010-04-14 00:00:00',3,14,104,15,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:470 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100415, '2010-04-15 00:00:00',4,15,105,15,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:471 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100416, '2010-04-16 00:00:00',5,16,106,15,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:472 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100417, '2010-04-17 00:00:00',6,17,107,15,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:473 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100418, '2010-04-18 00:00:00',7,18,108,15,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:474 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100419, '2010-04-19 00:00:00',1,19,109,16,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:475 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100420, '2010-04-20 00:00:00',2,20,110,16,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:476 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100421, '2010-04-21 00:00:00',3,21,111,16,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:477 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100422, '2010-04-22 00:00:00',4,22,112,16,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:478 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100423, '2010-04-23 00:00:00',5,23,113,16,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:479 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100424, '2010-04-24 00:00:00',6,24,114,16,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:480 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100425, '2010-04-25 00:00:00',7,25,115,16,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:481 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100426, '2010-04-26 00:00:00',1,26,116,17,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:482 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100427, '2010-04-27 00:00:00',2,27,117,17,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:483 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100428, '2010-04-28 00:00:00',3,28,118,17,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:484 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100429, '2010-04-29 00:00:00',4,29,119,17,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:485 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100430, '2010-04-30 00:00:00',5,30,120,17,4,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:486 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100501, '2010-05-01 00:00:00',6,1,121,17,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:487 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100502, '2010-05-02 00:00:00',7,2,122,17,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:488 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100503, '2010-05-03 00:00:00',1,3,123,18,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:489 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100504, '2010-05-04 00:00:00',2,4,124,18,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:490 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100505, '2010-05-05 00:00:00',3,5,125,18,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:491 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100506, '2010-05-06 00:00:00',4,6,126,18,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:492 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100507, '2010-05-07 00:00:00',5,7,127,18,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:493 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100508, '2010-05-08 00:00:00',6,8,128,18,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:494 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100509, '2010-05-09 00:00:00',7,9,129,18,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:495 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100510, '2010-05-10 00:00:00',1,10,130,19,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:496 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100511, '2010-05-11 00:00:00',2,11,131,19,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:497 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100512, '2010-05-12 00:00:00',3,12,132,19,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:498 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100513, '2010-05-13 00:00:00',4,13,133,19,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:499 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100514, '2010-05-14 00:00:00',5,14,134,19,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:500 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100515, '2010-05-15 00:00:00',6,15,135,19,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:501 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100516, '2010-05-16 00:00:00',7,16,136,19,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:502 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100517, '2010-05-17 00:00:00',1,17,137,20,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:503 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100518, '2010-05-18 00:00:00',2,18,138,20,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:504 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100519, '2010-05-19 00:00:00',3,19,139,20,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:505 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100520, '2010-05-20 00:00:00',4,20,140,20,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:506 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100521, '2010-05-21 00:00:00',5,21,141,20,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:507 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100522, '2010-05-22 00:00:00',6,22,142,20,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:508 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100523, '2010-05-23 00:00:00',7,23,143,20,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:509 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100524, '2010-05-24 00:00:00',1,24,144,21,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:510 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100525, '2010-05-25 00:00:00',2,25,145,21,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:511 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100526, '2010-05-26 00:00:00',3,26,146,21,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:512 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100527, '2010-05-27 00:00:00',4,27,147,21,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:513 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100528, '2010-05-28 00:00:00',5,28,148,21,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:514 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100529, '2010-05-29 00:00:00',6,29,149,21,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:515 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100530, '2010-05-30 00:00:00',7,30,150,21,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:516 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100531, '2010-05-31 00:00:00',1,31,151,22,5,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:517 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100601, '2010-06-01 00:00:00',2,1,152,22,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:518 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100602, '2010-06-02 00:00:00',3,2,153,22,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:519 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100603, '2010-06-03 00:00:00',4,3,154,22,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:520 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100604, '2010-06-04 00:00:00',5,4,155,22,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:521 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100605, '2010-06-05 00:00:00',6,5,156,22,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:522 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100606, '2010-06-06 00:00:00',7,6,157,22,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:523 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100607, '2010-06-07 00:00:00',1,7,158,23,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:524 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100608, '2010-06-08 00:00:00',2,8,159,23,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:525 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100609, '2010-06-09 00:00:00',3,9,160,23,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:526 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100610, '2010-06-10 00:00:00',4,10,161,23,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:527 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100611, '2010-06-11 00:00:00',5,11,162,23,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:528 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100612, '2010-06-12 00:00:00',6,12,163,23,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:529 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100613, '2010-06-13 00:00:00',7,13,164,23,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:530 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100614, '2010-06-14 00:00:00',1,14,165,24,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:531 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100615, '2010-06-15 00:00:00',2,15,166,24,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:532 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100616, '2010-06-16 00:00:00',3,16,167,24,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:533 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100617, '2010-06-17 00:00:00',4,17,168,24,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:534 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100618, '2010-06-18 00:00:00',5,18,169,24,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:535 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100619, '2010-06-19 00:00:00',6,19,170,24,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:536 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100620, '2010-06-20 00:00:00',7,20,171,24,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:537 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100621, '2010-06-21 00:00:00',1,21,172,25,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:538 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100622, '2010-06-22 00:00:00',2,22,173,25,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:539 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100623, '2010-06-23 00:00:00',3,23,174,25,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:540 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100624, '2010-06-24 00:00:00',4,24,175,25,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:541 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100625, '2010-06-25 00:00:00',5,25,176,25,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:542 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100626, '2010-06-26 00:00:00',6,26,177,25,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:543 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100627, '2010-06-27 00:00:00',7,27,178,25,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:544 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100628, '2010-06-28 00:00:00',1,28,179,26,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:545 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100629, '2010-06-29 00:00:00',2,29,180,26,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:546 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100630, '2010-06-30 00:00:00',3,30,181,26,6,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:547 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100701, '2010-07-01 00:00:00',4,1,182,26,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:548 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100702, '2010-07-02 00:00:00',5,2,183,26,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:549 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100703, '2010-07-03 00:00:00',6,3,184,26,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:550 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100704, '2010-07-04 00:00:00',7,4,185,26,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:551 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100705, '2010-07-05 00:00:00',1,5,186,27,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:552 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100706, '2010-07-06 00:00:00',2,6,187,27,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:553 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100707, '2010-07-07 00:00:00',3,7,188,27,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:554 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100708, '2010-07-08 00:00:00',4,8,189,27,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:555 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100709, '2010-07-09 00:00:00',5,9,190,27,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:556 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100710, '2010-07-10 00:00:00',6,10,191,27,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:557 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100711, '2010-07-11 00:00:00',7,11,192,27,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:558 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100712, '2010-07-12 00:00:00',1,12,193,28,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:559 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100713, '2010-07-13 00:00:00',2,13,194,28,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:560 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100714, '2010-07-14 00:00:00',3,14,195,28,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:561 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100715, '2010-07-15 00:00:00',4,15,196,28,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:562 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100716, '2010-07-16 00:00:00',5,16,197,28,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:563 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100717, '2010-07-17 00:00:00',6,17,198,28,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:564 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100718, '2010-07-18 00:00:00',7,18,199,28,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:565 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100719, '2010-07-19 00:00:00',1,19,200,29,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:566 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100720, '2010-07-20 00:00:00',2,20,201,29,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:567 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100721, '2010-07-21 00:00:00',3,21,202,29,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:568 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100722, '2010-07-22 00:00:00',4,22,203,29,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:569 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100723, '2010-07-23 00:00:00',5,23,204,29,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:570 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100724, '2010-07-24 00:00:00',6,24,205,29,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:571 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100725, '2010-07-25 00:00:00',7,25,206,29,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:572 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100726, '2010-07-26 00:00:00',1,26,207,30,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:573 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100727, '2010-07-27 00:00:00',2,27,208,30,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:574 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100728, '2010-07-28 00:00:00',3,28,209,30,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:575 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100729, '2010-07-29 00:00:00',4,29,210,30,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:576 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100730, '2010-07-30 00:00:00',5,30,211,30,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:577 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100731, '2010-07-31 00:00:00',6,31,212,30,7,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:578 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100801, '2010-08-01 00:00:00',7,1,213,30,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:579 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100802, '2010-08-02 00:00:00',1,2,214,31,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:580 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100803, '2010-08-03 00:00:00',2,3,215,31,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:581 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100804, '2010-08-04 00:00:00',3,4,216,31,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:582 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100805, '2010-08-05 00:00:00',4,5,217,31,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:583 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100806, '2010-08-06 00:00:00',5,6,218,31,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:584 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100807, '2010-08-07 00:00:00',6,7,219,31,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:585 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100808, '2010-08-08 00:00:00',7,8,220,31,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:586 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100809, '2010-08-09 00:00:00',1,9,221,32,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:587 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100810, '2010-08-10 00:00:00',2,10,222,32,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:588 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100811, '2010-08-11 00:00:00',3,11,223,32,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:589 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100812, '2010-08-12 00:00:00',4,12,224,32,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:590 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100813, '2010-08-13 00:00:00',5,13,225,32,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:591 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100814, '2010-08-14 00:00:00',6,14,226,32,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:592 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100815, '2010-08-15 00:00:00',7,15,227,32,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:593 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100816, '2010-08-16 00:00:00',1,16,228,33,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:594 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100817, '2010-08-17 00:00:00',2,17,229,33,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:595 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100818, '2010-08-18 00:00:00',3,18,230,33,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:596 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100819, '2010-08-19 00:00:00',4,19,231,33,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:597 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100820, '2010-08-20 00:00:00',5,20,232,33,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:598 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100821, '2010-08-21 00:00:00',6,21,233,33,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:599 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100822, '2010-08-22 00:00:00',7,22,234,33,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:600 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100823, '2010-08-23 00:00:00',1,23,235,34,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:601 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100824, '2010-08-24 00:00:00',2,24,236,34,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:602 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100825, '2010-08-25 00:00:00',3,25,237,34,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:603 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100826, '2010-08-26 00:00:00',4,26,238,34,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:604 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100827, '2010-08-27 00:00:00',5,27,239,34,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:605 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100828, '2010-08-28 00:00:00',6,28,240,34,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:606 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100829, '2010-08-29 00:00:00',7,29,241,34,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:607 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100830, '2010-08-30 00:00:00',1,30,242,35,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:608 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100831, '2010-08-31 00:00:00',2,31,243,35,8,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:609 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100901, '2010-09-01 00:00:00',3,1,244,35,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:610 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100902, '2010-09-02 00:00:00',4,2,245,35,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:611 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100903, '2010-09-03 00:00:00',5,3,246,35,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:612 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100904, '2010-09-04 00:00:00',6,4,247,35,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:613 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100905, '2010-09-05 00:00:00',7,5,248,35,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:614 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100906, '2010-09-06 00:00:00',1,6,249,36,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:615 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100907, '2010-09-07 00:00:00',2,7,250,36,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:616 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100908, '2010-09-08 00:00:00',3,8,251,36,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:617 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100909, '2010-09-09 00:00:00',4,9,252,36,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:618 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100910, '2010-09-10 00:00:00',5,10,253,36,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:619 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100911, '2010-09-11 00:00:00',6,11,254,36,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:620 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100912, '2010-09-12 00:00:00',7,12,255,36,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:621 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100913, '2010-09-13 00:00:00',1,13,256,37,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:622 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100914, '2010-09-14 00:00:00',2,14,257,37,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:623 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100915, '2010-09-15 00:00:00',3,15,258,37,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:624 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100916, '2010-09-16 00:00:00',4,16,259,37,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:625 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100917, '2010-09-17 00:00:00',5,17,260,37,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:626 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100918, '2010-09-18 00:00:00',6,18,261,37,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:627 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100919, '2010-09-19 00:00:00',7,19,262,37,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:628 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100920, '2010-09-20 00:00:00',1,20,263,38,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:629 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100921, '2010-09-21 00:00:00',2,21,264,38,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:630 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100922, '2010-09-22 00:00:00',3,22,265,38,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:631 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100923, '2010-09-23 00:00:00',4,23,266,38,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:632 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100924, '2010-09-24 00:00:00',5,24,267,38,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:633 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100925, '2010-09-25 00:00:00',6,25,268,38,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:634 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100926, '2010-09-26 00:00:00',7,26,269,38,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:635 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100927, '2010-09-27 00:00:00',1,27,270,39,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:636 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100928, '2010-09-28 00:00:00',2,28,271,39,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:637 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100929, '2010-09-29 00:00:00',3,29,272,39,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:638 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20100930, '2010-09-30 00:00:00',4,30,273,39,9,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:639 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101001, '2010-10-01 00:00:00',5,1,274,39,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:640 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101002, '2010-10-02 00:00:00',6,2,275,39,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:641 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101003, '2010-10-03 00:00:00',7,3,276,39,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:642 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101004, '2010-10-04 00:00:00',1,4,277,40,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:643 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101005, '2010-10-05 00:00:00',2,5,278,40,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:644 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101006, '2010-10-06 00:00:00',3,6,279,40,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:645 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101007, '2010-10-07 00:00:00',4,7,280,40,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:646 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101008, '2010-10-08 00:00:00',5,8,281,40,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:647 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101009, '2010-10-09 00:00:00',6,9,282,40,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:648 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101010, '2010-10-10 00:00:00',7,10,283,40,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:649 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101011, '2010-10-11 00:00:00',1,11,284,41,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:650 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101012, '2010-10-12 00:00:00',2,12,285,41,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:651 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101013, '2010-10-13 00:00:00',3,13,286,41,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:652 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101014, '2010-10-14 00:00:00',4,14,287,41,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:653 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101015, '2010-10-15 00:00:00',5,15,288,41,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:654 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101016, '2010-10-16 00:00:00',6,16,289,41,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:655 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101017, '2010-10-17 00:00:00',7,17,290,41,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:656 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101018, '2010-10-18 00:00:00',1,18,291,42,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:657 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101019, '2010-10-19 00:00:00',2,19,292,42,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:658 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101020, '2010-10-20 00:00:00',3,20,293,42,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:659 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101021, '2010-10-21 00:00:00',4,21,294,42,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:660 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101022, '2010-10-22 00:00:00',5,22,295,42,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:661 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101023, '2010-10-23 00:00:00',6,23,296,42,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:662 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101024, '2010-10-24 00:00:00',7,24,297,42,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:663 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101025, '2010-10-25 00:00:00',1,25,298,43,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:664 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101026, '2010-10-26 00:00:00',2,26,299,43,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:665 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101027, '2010-10-27 00:00:00',3,27,300,43,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:666 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101028, '2010-10-28 00:00:00',4,28,301,43,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:667 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101029, '2010-10-29 00:00:00',5,29,302,43,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:668 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101030, '2010-10-30 00:00:00',6,30,303,43,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:669 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101031, '2010-10-31 00:00:00',7,31,304,43,10,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:670 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101101, '2010-11-01 00:00:00',1,1,305,44,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:671 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101102, '2010-11-02 00:00:00',2,2,306,44,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:672 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101103, '2010-11-03 00:00:00',3,3,307,44,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:673 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101104, '2010-11-04 00:00:00',4,4,308,44,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:674 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101105, '2010-11-05 00:00:00',5,5,309,44,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:675 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101106, '2010-11-06 00:00:00',6,6,310,44,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:676 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101107, '2010-11-07 00:00:00',7,7,311,44,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:677 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101108, '2010-11-08 00:00:00',1,8,312,45,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:678 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101109, '2010-11-09 00:00:00',2,9,313,45,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:679 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101110, '2010-11-10 00:00:00',3,10,314,45,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:680 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101111, '2010-11-11 00:00:00',4,11,315,45,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:681 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101112, '2010-11-12 00:00:00',5,12,316,45,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:682 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101113, '2010-11-13 00:00:00',6,13,317,45,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:683 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101114, '2010-11-14 00:00:00',7,14,318,45,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:684 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101115, '2010-11-15 00:00:00',1,15,319,46,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:685 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101116, '2010-11-16 00:00:00',2,16,320,46,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:686 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101117, '2010-11-17 00:00:00',3,17,321,46,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:687 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101118, '2010-11-18 00:00:00',4,18,322,46,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:688 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101119, '2010-11-19 00:00:00',5,19,323,46,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:689 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101120, '2010-11-20 00:00:00',6,20,324,46,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:690 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101121, '2010-11-21 00:00:00',7,21,325,46,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:691 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101122, '2010-11-22 00:00:00',1,22,326,47,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:692 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101123, '2010-11-23 00:00:00',2,23,327,47,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:693 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101124, '2010-11-24 00:00:00',3,24,328,47,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:694 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101125, '2010-11-25 00:00:00',4,25,329,47,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:695 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101126, '2010-11-26 00:00:00',5,26,330,47,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:696 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101127, '2010-11-27 00:00:00',6,27,331,47,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:697 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101128, '2010-11-28 00:00:00',7,28,332,47,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:698 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101129, '2010-11-29 00:00:00',1,29,333,48,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:699 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101130, '2010-11-30 00:00:00',2,30,334,48,11,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:700 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101201, '2010-12-01 00:00:00',3,1,335,48,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:701 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101202, '2010-12-02 00:00:00',4,2,336,48,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:702 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101203, '2010-12-03 00:00:00',5,3,337,48,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:703 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101204, '2010-12-04 00:00:00',6,4,338,48,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:704 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101205, '2010-12-05 00:00:00',7,5,339,48,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:705 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101206, '2010-12-06 00:00:00',1,6,340,49,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:706 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101207, '2010-12-07 00:00:00',2,7,341,49,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:707 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101208, '2010-12-08 00:00:00',3,8,342,49,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:708 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101209, '2010-12-09 00:00:00',4,9,343,49,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:709 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101210, '2010-12-10 00:00:00',5,10,344,49,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:710 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101211, '2010-12-11 00:00:00',6,11,345,49,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:711 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101212, '2010-12-12 00:00:00',7,12,346,49,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:712 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101213, '2010-12-13 00:00:00',1,13,347,50,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:713 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101214, '2010-12-14 00:00:00',2,14,348,50,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:714 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101215, '2010-12-15 00:00:00',3,15,349,50,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:715 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101216, '2010-12-16 00:00:00',4,16,350,50,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:716 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101217, '2010-12-17 00:00:00',5,17,351,50,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:717 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101218, '2010-12-18 00:00:00',6,18,352,50,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:718 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101219, '2010-12-19 00:00:00',7,19,353,50,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:719 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101220, '2010-12-20 00:00:00',1,20,354,51,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:720 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101221, '2010-12-21 00:00:00',2,21,355,51,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:721 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101222, '2010-12-22 00:00:00',3,22,356,51,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:722 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101223, '2010-12-23 00:00:00',4,23,357,51,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:723 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101224, '2010-12-24 00:00:00',5,24,358,51,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:724 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101225, '2010-12-25 00:00:00',6,25,359,51,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:725 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101226, '2010-12-26 00:00:00',7,26,360,51,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:726 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101227, '2010-12-27 00:00:00',1,27,361,52,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:727 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101228, '2010-12-28 00:00:00',2,28,362,52,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:728 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101229, '2010-12-29 00:00:00',3,29,363,52,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:729 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101230, '2010-12-30 00:00:00',4,30,364,52,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:730 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20101231, '2010-12-31 00:00:00',5,31,365,52,12,2010,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:731 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110101, '2011-01-01 00:00:00',6,1,1,52,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:732 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110102, '2011-01-02 00:00:00',7,2,2,52,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:733 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110103, '2011-01-03 00:00:00',1,3,3,1,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:734 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110104, '2011-01-04 00:00:00',2,4,4,1,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:735 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110105, '2011-01-05 00:00:00',3,5,5,1,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:736 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110106, '2011-01-06 00:00:00',4,6,6,1,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:737 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110107, '2011-01-07 00:00:00',5,7,7,1,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:738 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110108, '2011-01-08 00:00:00',6,8,8,1,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:739 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110109, '2011-01-09 00:00:00',7,9,9,1,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:740 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110110, '2011-01-10 00:00:00',1,10,10,2,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:741 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110111, '2011-01-11 00:00:00',2,11,11,2,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:742 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110112, '2011-01-12 00:00:00',3,12,12,2,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:743 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110113, '2011-01-13 00:00:00',4,13,13,2,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:744 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110114, '2011-01-14 00:00:00',5,14,14,2,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:745 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110115, '2011-01-15 00:00:00',6,15,15,2,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:746 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110116, '2011-01-16 00:00:00',7,16,16,2,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:747 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110117, '2011-01-17 00:00:00',1,17,17,3,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:748 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110118, '2011-01-18 00:00:00',2,18,18,3,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:749 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110119, '2011-01-19 00:00:00',3,19,19,3,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:750 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110120, '2011-01-20 00:00:00',4,20,20,3,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:751 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110121, '2011-01-21 00:00:00',5,21,21,3,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:752 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110122, '2011-01-22 00:00:00',6,22,22,3,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:753 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110123, '2011-01-23 00:00:00',7,23,23,3,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:754 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110124, '2011-01-24 00:00:00',1,24,24,4,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:755 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110125, '2011-01-25 00:00:00',2,25,25,4,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:756 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110126, '2011-01-26 00:00:00',3,26,26,4,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:757 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110127, '2011-01-27 00:00:00',4,27,27,4,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:758 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110128, '2011-01-28 00:00:00',5,28,28,4,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:759 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110129, '2011-01-29 00:00:00',6,29,29,4,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:760 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110130, '2011-01-30 00:00:00',7,30,30,4,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:761 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110131, '2011-01-31 00:00:00',1,31,31,5,1,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:762 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110201, '2011-02-01 00:00:00',2,1,32,5,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:763 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110202, '2011-02-02 00:00:00',3,2,33,5,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:764 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110203, '2011-02-03 00:00:00',4,3,34,5,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:765 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110204, '2011-02-04 00:00:00',5,4,35,5,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:766 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110205, '2011-02-05 00:00:00',6,5,36,5,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:767 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110206, '2011-02-06 00:00:00',7,6,37,5,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:768 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110207, '2011-02-07 00:00:00',1,7,38,6,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:769 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110208, '2011-02-08 00:00:00',2,8,39,6,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:770 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110209, '2011-02-09 00:00:00',3,9,40,6,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:771 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110210, '2011-02-10 00:00:00',4,10,41,6,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:772 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110211, '2011-02-11 00:00:00',5,11,42,6,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:773 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110212, '2011-02-12 00:00:00',6,12,43,6,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:774 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110213, '2011-02-13 00:00:00',7,13,44,6,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:775 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110214, '2011-02-14 00:00:00',1,14,45,7,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:776 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110215, '2011-02-15 00:00:00',2,15,46,7,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:777 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110216, '2011-02-16 00:00:00',3,16,47,7,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:778 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110217, '2011-02-17 00:00:00',4,17,48,7,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:779 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110218, '2011-02-18 00:00:00',5,18,49,7,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:780 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110219, '2011-02-19 00:00:00',6,19,50,7,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:781 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110220, '2011-02-20 00:00:00',7,20,51,7,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:782 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110221, '2011-02-21 00:00:00',1,21,52,8,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:783 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110222, '2011-02-22 00:00:00',2,22,53,8,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:784 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110223, '2011-02-23 00:00:00',3,23,54,8,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:785 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110224, '2011-02-24 00:00:00',4,24,55,8,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:786 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110225, '2011-02-25 00:00:00',5,25,56,8,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:787 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110226, '2011-02-26 00:00:00',6,26,57,8,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:788 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110227, '2011-02-27 00:00:00',7,27,58,8,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:789 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110228, '2011-02-28 00:00:00',1,28,59,9,2,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:790 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110301, '2011-03-01 00:00:00',2,1,60,9,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:791 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110302, '2011-03-02 00:00:00',3,2,61,9,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:792 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110303, '2011-03-03 00:00:00',4,3,62,9,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:793 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110304, '2011-03-04 00:00:00',5,4,63,9,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:794 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110305, '2011-03-05 00:00:00',6,5,64,9,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:795 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110306, '2011-03-06 00:00:00',7,6,65,9,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:796 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110307, '2011-03-07 00:00:00',1,7,66,10,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:797 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110308, '2011-03-08 00:00:00',2,8,67,10,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:798 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110309, '2011-03-09 00:00:00',3,9,68,10,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:799 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110310, '2011-03-10 00:00:00',4,10,69,10,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:800 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110311, '2011-03-11 00:00:00',5,11,70,10,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:801 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110312, '2011-03-12 00:00:00',6,12,71,10,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:802 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110313, '2011-03-13 00:00:00',7,13,72,10,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:803 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110314, '2011-03-14 00:00:00',1,14,73,11,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:804 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110315, '2011-03-15 00:00:00',2,15,74,11,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:805 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110316, '2011-03-16 00:00:00',3,16,75,11,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:806 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110317, '2011-03-17 00:00:00',4,17,76,11,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:807 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110318, '2011-03-18 00:00:00',5,18,77,11,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:808 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110319, '2011-03-19 00:00:00',6,19,78,11,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:809 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110320, '2011-03-20 00:00:00',7,20,79,11,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:810 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110321, '2011-03-21 00:00:00',1,21,80,12,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:811 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110322, '2011-03-22 00:00:00',2,22,81,12,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:812 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110323, '2011-03-23 00:00:00',3,23,82,12,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:813 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110324, '2011-03-24 00:00:00',4,24,83,12,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:814 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110325, '2011-03-25 00:00:00',5,25,84,12,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:815 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110326, '2011-03-26 00:00:00',6,26,85,12,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:816 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110327, '2011-03-27 00:00:00',7,27,86,12,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:817 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110328, '2011-03-28 00:00:00',1,28,87,13,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:818 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110329, '2011-03-29 00:00:00',2,29,88,13,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:819 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110330, '2011-03-30 00:00:00',3,30,89,13,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:820 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110331, '2011-03-31 00:00:00',4,31,90,13,3,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:821 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110401, '2011-04-01 00:00:00',5,1,91,13,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:822 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110402, '2011-04-02 00:00:00',6,2,92,13,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:823 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110403, '2011-04-03 00:00:00',7,3,93,13,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:824 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110404, '2011-04-04 00:00:00',1,4,94,14,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:825 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110405, '2011-04-05 00:00:00',2,5,95,14,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:826 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110406, '2011-04-06 00:00:00',3,6,96,14,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:827 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110407, '2011-04-07 00:00:00',4,7,97,14,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:828 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110408, '2011-04-08 00:00:00',5,8,98,14,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:829 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110409, '2011-04-09 00:00:00',6,9,99,14,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:830 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110410, '2011-04-10 00:00:00',7,10,100,14,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:831 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110411, '2011-04-11 00:00:00',1,11,101,15,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:832 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110412, '2011-04-12 00:00:00',2,12,102,15,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:833 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110413, '2011-04-13 00:00:00',3,13,103,15,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:834 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110414, '2011-04-14 00:00:00',4,14,104,15,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:835 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110415, '2011-04-15 00:00:00',5,15,105,15,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:836 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110416, '2011-04-16 00:00:00',6,16,106,15,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:837 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110417, '2011-04-17 00:00:00',7,17,107,15,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:838 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110418, '2011-04-18 00:00:00',1,18,108,16,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:839 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110419, '2011-04-19 00:00:00',2,19,109,16,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:840 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110420, '2011-04-20 00:00:00',3,20,110,16,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:841 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110421, '2011-04-21 00:00:00',4,21,111,16,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:842 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110422, '2011-04-22 00:00:00',5,22,112,16,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:843 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110423, '2011-04-23 00:00:00',6,23,113,16,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:844 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110424, '2011-04-24 00:00:00',7,24,114,16,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:845 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110425, '2011-04-25 00:00:00',1,25,115,17,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:846 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110426, '2011-04-26 00:00:00',2,26,116,17,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:847 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110427, '2011-04-27 00:00:00',3,27,117,17,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:848 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110428, '2011-04-28 00:00:00',4,28,118,17,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:849 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110429, '2011-04-29 00:00:00',5,29,119,17,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:850 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110430, '2011-04-30 00:00:00',6,30,120,17,4,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:851 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110501, '2011-05-01 00:00:00',7,1,121,17,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:852 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110502, '2011-05-02 00:00:00',1,2,122,18,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:853 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110503, '2011-05-03 00:00:00',2,3,123,18,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:854 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110504, '2011-05-04 00:00:00',3,4,124,18,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:855 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110505, '2011-05-05 00:00:00',4,5,125,18,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:856 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110506, '2011-05-06 00:00:00',5,6,126,18,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:857 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110507, '2011-05-07 00:00:00',6,7,127,18,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:858 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110508, '2011-05-08 00:00:00',7,8,128,18,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:859 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110509, '2011-05-09 00:00:00',1,9,129,19,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:860 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110510, '2011-05-10 00:00:00',2,10,130,19,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:861 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110511, '2011-05-11 00:00:00',3,11,131,19,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:862 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110512, '2011-05-12 00:00:00',4,12,132,19,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:863 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110513, '2011-05-13 00:00:00',5,13,133,19,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:864 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110514, '2011-05-14 00:00:00',6,14,134,19,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:865 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110515, '2011-05-15 00:00:00',7,15,135,19,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:866 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110516, '2011-05-16 00:00:00',1,16,136,20,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:867 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110517, '2011-05-17 00:00:00',2,17,137,20,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:868 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110518, '2011-05-18 00:00:00',3,18,138,20,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:869 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110519, '2011-05-19 00:00:00',4,19,139,20,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:870 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110520, '2011-05-20 00:00:00',5,20,140,20,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:871 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110521, '2011-05-21 00:00:00',6,21,141,20,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:872 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110522, '2011-05-22 00:00:00',7,22,142,20,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:873 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110523, '2011-05-23 00:00:00',1,23,143,21,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:874 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110524, '2011-05-24 00:00:00',2,24,144,21,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:875 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110525, '2011-05-25 00:00:00',3,25,145,21,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:876 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110526, '2011-05-26 00:00:00',4,26,146,21,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:877 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110527, '2011-05-27 00:00:00',5,27,147,21,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:878 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110528, '2011-05-28 00:00:00',6,28,148,21,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:879 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110529, '2011-05-29 00:00:00',7,29,149,21,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:880 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110530, '2011-05-30 00:00:00',1,30,150,22,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:881 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110531, '2011-05-31 00:00:00',2,31,151,22,5,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:882 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110601, '2011-06-01 00:00:00',3,1,152,22,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:883 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110602, '2011-06-02 00:00:00',4,2,153,22,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:884 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110603, '2011-06-03 00:00:00',5,3,154,22,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:885 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110604, '2011-06-04 00:00:00',6,4,155,22,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:886 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110605, '2011-06-05 00:00:00',7,5,156,22,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:887 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110606, '2011-06-06 00:00:00',1,6,157,23,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:888 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110607, '2011-06-07 00:00:00',2,7,158,23,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:889 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110608, '2011-06-08 00:00:00',3,8,159,23,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:890 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110609, '2011-06-09 00:00:00',4,9,160,23,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:891 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110610, '2011-06-10 00:00:00',5,10,161,23,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:892 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110611, '2011-06-11 00:00:00',6,11,162,23,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:893 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110612, '2011-06-12 00:00:00',7,12,163,23,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:894 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110613, '2011-06-13 00:00:00',1,13,164,24,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:895 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110614, '2011-06-14 00:00:00',2,14,165,24,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:896 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110615, '2011-06-15 00:00:00',3,15,166,24,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:897 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110616, '2011-06-16 00:00:00',4,16,167,24,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:898 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110617, '2011-06-17 00:00:00',5,17,168,24,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:899 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110618, '2011-06-18 00:00:00',6,18,169,24,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:900 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110619, '2011-06-19 00:00:00',7,19,170,24,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:901 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110620, '2011-06-20 00:00:00',1,20,171,25,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:902 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110621, '2011-06-21 00:00:00',2,21,172,25,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:903 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110622, '2011-06-22 00:00:00',3,22,173,25,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:904 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110623, '2011-06-23 00:00:00',4,23,174,25,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:905 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110624, '2011-06-24 00:00:00',5,24,175,25,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:906 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110625, '2011-06-25 00:00:00',6,25,176,25,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:907 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110626, '2011-06-26 00:00:00',7,26,177,25,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:908 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110627, '2011-06-27 00:00:00',1,27,178,26,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:909 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110628, '2011-06-28 00:00:00',2,28,179,26,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:910 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110629, '2011-06-29 00:00:00',3,29,180,26,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:911 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110630, '2011-06-30 00:00:00',4,30,181,26,6,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:912 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110701, '2011-07-01 00:00:00',5,1,182,26,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:913 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110702, '2011-07-02 00:00:00',6,2,183,26,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:914 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110703, '2011-07-03 00:00:00',7,3,184,26,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:915 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110704, '2011-07-04 00:00:00',1,4,185,27,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:916 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110705, '2011-07-05 00:00:00',2,5,186,27,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:917 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110706, '2011-07-06 00:00:00',3,6,187,27,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:918 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110707, '2011-07-07 00:00:00',4,7,188,27,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:919 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110708, '2011-07-08 00:00:00',5,8,189,27,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:920 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110709, '2011-07-09 00:00:00',6,9,190,27,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:921 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110710, '2011-07-10 00:00:00',7,10,191,27,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:922 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110711, '2011-07-11 00:00:00',1,11,192,28,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:923 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110712, '2011-07-12 00:00:00',2,12,193,28,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:924 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110713, '2011-07-13 00:00:00',3,13,194,28,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:925 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110714, '2011-07-14 00:00:00',4,14,195,28,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:926 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110715, '2011-07-15 00:00:00',5,15,196,28,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:927 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110716, '2011-07-16 00:00:00',6,16,197,28,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:928 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110717, '2011-07-17 00:00:00',7,17,198,28,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:929 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110718, '2011-07-18 00:00:00',1,18,199,29,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:930 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110719, '2011-07-19 00:00:00',2,19,200,29,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:931 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110720, '2011-07-20 00:00:00',3,20,201,29,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:932 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110721, '2011-07-21 00:00:00',4,21,202,29,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:933 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110722, '2011-07-22 00:00:00',5,22,203,29,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:934 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110723, '2011-07-23 00:00:00',6,23,204,29,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:935 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110724, '2011-07-24 00:00:00',7,24,205,29,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:936 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110725, '2011-07-25 00:00:00',1,25,206,30,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:937 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110726, '2011-07-26 00:00:00',2,26,207,30,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:938 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110727, '2011-07-27 00:00:00',3,27,208,30,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:939 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110728, '2011-07-28 00:00:00',4,28,209,30,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:940 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110729, '2011-07-29 00:00:00',5,29,210,30,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:941 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110730, '2011-07-30 00:00:00',6,30,211,30,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:942 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110731, '2011-07-31 00:00:00',7,31,212,30,7,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:943 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110801, '2011-08-01 00:00:00',1,1,213,31,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:944 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110802, '2011-08-02 00:00:00',2,2,214,31,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:945 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110803, '2011-08-03 00:00:00',3,3,215,31,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:946 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110804, '2011-08-04 00:00:00',4,4,216,31,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:947 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110805, '2011-08-05 00:00:00',5,5,217,31,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:948 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110806, '2011-08-06 00:00:00',6,6,218,31,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:949 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110807, '2011-08-07 00:00:00',7,7,219,31,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:950 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110808, '2011-08-08 00:00:00',1,8,220,32,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:951 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110809, '2011-08-09 00:00:00',2,9,221,32,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:952 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110810, '2011-08-10 00:00:00',3,10,222,32,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:953 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110811, '2011-08-11 00:00:00',4,11,223,32,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:954 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110812, '2011-08-12 00:00:00',5,12,224,32,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:955 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110813, '2011-08-13 00:00:00',6,13,225,32,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:956 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110814, '2011-08-14 00:00:00',7,14,226,32,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:957 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110815, '2011-08-15 00:00:00',1,15,227,33,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:958 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110816, '2011-08-16 00:00:00',2,16,228,33,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:959 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110817, '2011-08-17 00:00:00',3,17,229,33,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:960 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110818, '2011-08-18 00:00:00',4,18,230,33,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:961 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110819, '2011-08-19 00:00:00',5,19,231,33,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:962 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110820, '2011-08-20 00:00:00',6,20,232,33,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:963 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110821, '2011-08-21 00:00:00',7,21,233,33,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:964 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110822, '2011-08-22 00:00:00',1,22,234,34,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:965 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110823, '2011-08-23 00:00:00',2,23,235,34,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:966 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110824, '2011-08-24 00:00:00',3,24,236,34,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:967 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110825, '2011-08-25 00:00:00',4,25,237,34,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:968 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110826, '2011-08-26 00:00:00',5,26,238,34,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:969 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110827, '2011-08-27 00:00:00',6,27,239,34,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:970 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110828, '2011-08-28 00:00:00',7,28,240,34,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:971 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110829, '2011-08-29 00:00:00',1,29,241,35,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:972 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110830, '2011-08-30 00:00:00',2,30,242,35,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:973 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110831, '2011-08-31 00:00:00',3,31,243,35,8,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:974 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110901, '2011-09-01 00:00:00',4,1,244,35,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:975 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110902, '2011-09-02 00:00:00',5,2,245,35,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:976 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110903, '2011-09-03 00:00:00',6,3,246,35,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:977 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110904, '2011-09-04 00:00:00',7,4,247,35,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:978 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110905, '2011-09-05 00:00:00',1,5,248,36,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:979 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110906, '2011-09-06 00:00:00',2,6,249,36,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:980 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110907, '2011-09-07 00:00:00',3,7,250,36,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:981 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110908, '2011-09-08 00:00:00',4,8,251,36,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:982 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110909, '2011-09-09 00:00:00',5,9,252,36,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:983 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110910, '2011-09-10 00:00:00',6,10,253,36,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:984 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110911, '2011-09-11 00:00:00',7,11,254,36,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:985 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110912, '2011-09-12 00:00:00',1,12,255,37,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:986 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110913, '2011-09-13 00:00:00',2,13,256,37,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:987 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110914, '2011-09-14 00:00:00',3,14,257,37,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:988 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110915, '2011-09-15 00:00:00',4,15,258,37,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:989 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110916, '2011-09-16 00:00:00',5,16,259,37,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:990 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110917, '2011-09-17 00:00:00',6,17,260,37,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:991 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110918, '2011-09-18 00:00:00',7,18,261,37,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:992 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110919, '2011-09-19 00:00:00',1,19,262,38,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:993 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110920, '2011-09-20 00:00:00',2,20,263,38,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:994 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110921, '2011-09-21 00:00:00',3,21,264,38,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:995 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110922, '2011-09-22 00:00:00',4,22,265,38,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:996 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110923, '2011-09-23 00:00:00',5,23,266,38,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:997 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110924, '2011-09-24 00:00:00',6,24,267,38,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:998 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110925, '2011-09-25 00:00:00',7,25,268,38,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:999 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110926, '2011-09-26 00:00:00',1,26,269,39,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1000 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110927, '2011-09-27 00:00:00',2,27,270,39,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1001 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110928, '2011-09-28 00:00:00',3,28,271,39,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1002 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110929, '2011-09-29 00:00:00',4,29,272,39,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1003 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20110930, '2011-09-30 00:00:00',5,30,273,39,9,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1004 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111001, '2011-10-01 00:00:00',6,1,274,39,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1005 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111002, '2011-10-02 00:00:00',7,2,275,39,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1006 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111003, '2011-10-03 00:00:00',1,3,276,40,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1007 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111004, '2011-10-04 00:00:00',2,4,277,40,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1008 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111005, '2011-10-05 00:00:00',3,5,278,40,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1009 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111006, '2011-10-06 00:00:00',4,6,279,40,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1010 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111007, '2011-10-07 00:00:00',5,7,280,40,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1011 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111008, '2011-10-08 00:00:00',6,8,281,40,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1012 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111009, '2011-10-09 00:00:00',7,9,282,40,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1013 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111010, '2011-10-10 00:00:00',1,10,283,41,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1014 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111011, '2011-10-11 00:00:00',2,11,284,41,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1015 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111012, '2011-10-12 00:00:00',3,12,285,41,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1016 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111013, '2011-10-13 00:00:00',4,13,286,41,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1017 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111014, '2011-10-14 00:00:00',5,14,287,41,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1018 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111015, '2011-10-15 00:00:00',6,15,288,41,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1019 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111016, '2011-10-16 00:00:00',7,16,289,41,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1020 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111017, '2011-10-17 00:00:00',1,17,290,42,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1021 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111018, '2011-10-18 00:00:00',2,18,291,42,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1022 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111019, '2011-10-19 00:00:00',3,19,292,42,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1023 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111020, '2011-10-20 00:00:00',4,20,293,42,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1024 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111021, '2011-10-21 00:00:00',5,21,294,42,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1025 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111022, '2011-10-22 00:00:00',6,22,295,42,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1026 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111023, '2011-10-23 00:00:00',7,23,296,42,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1027 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111024, '2011-10-24 00:00:00',1,24,297,43,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1028 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111025, '2011-10-25 00:00:00',2,25,298,43,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1029 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111026, '2011-10-26 00:00:00',3,26,299,43,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1030 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111027, '2011-10-27 00:00:00',4,27,300,43,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1031 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111028, '2011-10-28 00:00:00',5,28,301,43,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1032 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111029, '2011-10-29 00:00:00',6,29,302,43,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1033 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111030, '2011-10-30 00:00:00',7,30,303,43,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1034 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111031, '2011-10-31 00:00:00',1,31,304,44,10,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1035 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111101, '2011-11-01 00:00:00',2,1,305,44,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1036 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111102, '2011-11-02 00:00:00',3,2,306,44,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1037 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111103, '2011-11-03 00:00:00',4,3,307,44,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1038 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111104, '2011-11-04 00:00:00',5,4,308,44,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1039 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111105, '2011-11-05 00:00:00',6,5,309,44,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1040 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111106, '2011-11-06 00:00:00',7,6,310,44,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1041 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111107, '2011-11-07 00:00:00',1,7,311,45,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1042 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111108, '2011-11-08 00:00:00',2,8,312,45,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1043 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111109, '2011-11-09 00:00:00',3,9,313,45,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1044 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111110, '2011-11-10 00:00:00',4,10,314,45,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1045 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111111, '2011-11-11 00:00:00',5,11,315,45,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1046 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111112, '2011-11-12 00:00:00',6,12,316,45,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1047 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111113, '2011-11-13 00:00:00',7,13,317,45,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1048 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111114, '2011-11-14 00:00:00',1,14,318,46,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1049 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111115, '2011-11-15 00:00:00',2,15,319,46,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1050 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111116, '2011-11-16 00:00:00',3,16,320,46,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1051 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111117, '2011-11-17 00:00:00',4,17,321,46,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1052 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111118, '2011-11-18 00:00:00',5,18,322,46,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1053 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111119, '2011-11-19 00:00:00',6,19,323,46,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1054 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111120, '2011-11-20 00:00:00',7,20,324,46,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1055 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111121, '2011-11-21 00:00:00',1,21,325,47,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1056 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111122, '2011-11-22 00:00:00',2,22,326,47,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1057 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111123, '2011-11-23 00:00:00',3,23,327,47,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1058 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111124, '2011-11-24 00:00:00',4,24,328,47,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1059 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111125, '2011-11-25 00:00:00',5,25,329,47,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1060 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111126, '2011-11-26 00:00:00',6,26,330,47,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1061 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111127, '2011-11-27 00:00:00',7,27,331,47,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1062 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111128, '2011-11-28 00:00:00',1,28,332,48,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1063 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111129, '2011-11-29 00:00:00',2,29,333,48,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1064 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111130, '2011-11-30 00:00:00',3,30,334,48,11,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1065 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111201, '2011-12-01 00:00:00',4,1,335,48,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1066 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111202, '2011-12-02 00:00:00',5,2,336,48,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1067 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111203, '2011-12-03 00:00:00',6,3,337,48,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1068 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111204, '2011-12-04 00:00:00',7,4,338,48,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1069 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111205, '2011-12-05 00:00:00',1,5,339,49,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1070 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111206, '2011-12-06 00:00:00',2,6,340,49,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1071 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111207, '2011-12-07 00:00:00',3,7,341,49,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1072 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111208, '2011-12-08 00:00:00',4,8,342,49,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1073 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111209, '2011-12-09 00:00:00',5,9,343,49,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1074 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111210, '2011-12-10 00:00:00',6,10,344,49,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1075 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111211, '2011-12-11 00:00:00',7,11,345,49,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1076 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111212, '2011-12-12 00:00:00',1,12,346,50,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1077 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111213, '2011-12-13 00:00:00',2,13,347,50,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1078 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111214, '2011-12-14 00:00:00',3,14,348,50,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1079 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111215, '2011-12-15 00:00:00',4,15,349,50,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1080 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111216, '2011-12-16 00:00:00',5,16,350,50,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1081 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111217, '2011-12-17 00:00:00',6,17,351,50,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1082 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111218, '2011-12-18 00:00:00',7,18,352,50,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1083 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111219, '2011-12-19 00:00:00',1,19,353,51,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1084 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111220, '2011-12-20 00:00:00',2,20,354,51,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1085 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111221, '2011-12-21 00:00:00',3,21,355,51,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1086 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111222, '2011-12-22 00:00:00',4,22,356,51,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1087 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111223, '2011-12-23 00:00:00',5,23,357,51,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1088 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111224, '2011-12-24 00:00:00',6,24,358,51,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1089 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111225, '2011-12-25 00:00:00',7,25,359,51,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1090 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111226, '2011-12-26 00:00:00',1,26,360,52,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1091 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111227, '2011-12-27 00:00:00',2,27,361,52,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1092 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111228, '2011-12-28 00:00:00',3,28,362,52,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1093 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111229, '2011-12-29 00:00:00',4,29,363,52,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1094 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111230, '2011-12-30 00:00:00',5,30,364,52,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1095 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20111231, '2011-12-31 00:00:00',6,31,365,52,12,2011,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1096 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120101, '2012-01-01 00:00:00',7,1,1,52,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1097 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120102, '2012-01-02 00:00:00',1,2,2,1,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1098 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120103, '2012-01-03 00:00:00',2,3,3,1,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1099 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120104, '2012-01-04 00:00:00',3,4,4,1,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1100 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120105, '2012-01-05 00:00:00',4,5,5,1,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1101 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120106, '2012-01-06 00:00:00',5,6,6,1,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1102 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120107, '2012-01-07 00:00:00',6,7,7,1,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1103 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120108, '2012-01-08 00:00:00',7,8,8,1,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1104 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120109, '2012-01-09 00:00:00',1,9,9,2,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1105 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120110, '2012-01-10 00:00:00',2,10,10,2,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1106 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120111, '2012-01-11 00:00:00',3,11,11,2,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1107 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120112, '2012-01-12 00:00:00',4,12,12,2,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1108 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120113, '2012-01-13 00:00:00',5,13,13,2,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1109 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120114, '2012-01-14 00:00:00',6,14,14,2,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1110 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120115, '2012-01-15 00:00:00',7,15,15,2,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1111 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120116, '2012-01-16 00:00:00',1,16,16,3,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1112 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120117, '2012-01-17 00:00:00',2,17,17,3,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1113 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120118, '2012-01-18 00:00:00',3,18,18,3,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1114 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120119, '2012-01-19 00:00:00',4,19,19,3,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1115 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120120, '2012-01-20 00:00:00',5,20,20,3,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1116 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120121, '2012-01-21 00:00:00',6,21,21,3,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1117 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120122, '2012-01-22 00:00:00',7,22,22,3,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1118 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120123, '2012-01-23 00:00:00',1,23,23,4,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1119 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120124, '2012-01-24 00:00:00',2,24,24,4,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1120 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120125, '2012-01-25 00:00:00',3,25,25,4,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1121 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120126, '2012-01-26 00:00:00',4,26,26,4,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1122 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120127, '2012-01-27 00:00:00',5,27,27,4,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1123 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120128, '2012-01-28 00:00:00',6,28,28,4,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1124 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120129, '2012-01-29 00:00:00',7,29,29,4,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1125 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120130, '2012-01-30 00:00:00',1,30,30,5,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1126 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120131, '2012-01-31 00:00:00',2,31,31,5,1,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1127 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120201, '2012-02-01 00:00:00',3,1,32,5,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1128 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120202, '2012-02-02 00:00:00',4,2,33,5,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1129 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120203, '2012-02-03 00:00:00',5,3,34,5,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1130 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120204, '2012-02-04 00:00:00',6,4,35,5,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1131 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120205, '2012-02-05 00:00:00',7,5,36,5,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1132 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120206, '2012-02-06 00:00:00',1,6,37,6,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1133 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120207, '2012-02-07 00:00:00',2,7,38,6,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1134 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120208, '2012-02-08 00:00:00',3,8,39,6,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1135 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120209, '2012-02-09 00:00:00',4,9,40,6,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1136 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120210, '2012-02-10 00:00:00',5,10,41,6,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1137 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120211, '2012-02-11 00:00:00',6,11,42,6,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1138 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120212, '2012-02-12 00:00:00',7,12,43,6,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1139 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120213, '2012-02-13 00:00:00',1,13,44,7,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1140 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120214, '2012-02-14 00:00:00',2,14,45,7,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1141 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120215, '2012-02-15 00:00:00',3,15,46,7,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1142 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120216, '2012-02-16 00:00:00',4,16,47,7,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1143 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120217, '2012-02-17 00:00:00',5,17,48,7,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1144 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120218, '2012-02-18 00:00:00',6,18,49,7,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1145 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120219, '2012-02-19 00:00:00',7,19,50,7,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1146 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120220, '2012-02-20 00:00:00',1,20,51,8,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1147 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120221, '2012-02-21 00:00:00',2,21,52,8,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1148 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120222, '2012-02-22 00:00:00',3,22,53,8,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1149 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120223, '2012-02-23 00:00:00',4,23,54,8,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1150 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120224, '2012-02-24 00:00:00',5,24,55,8,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1151 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120225, '2012-02-25 00:00:00',6,25,56,8,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1152 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120226, '2012-02-26 00:00:00',7,26,57,8,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1153 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120227, '2012-02-27 00:00:00',1,27,58,9,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1154 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120228, '2012-02-28 00:00:00',2,28,59,9,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1155 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120229, '2012-02-29 00:00:00',3,29,60,9,2,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1156 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120301, '2012-03-01 00:00:00',4,1,61,9,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1157 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120302, '2012-03-02 00:00:00',5,2,62,9,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1158 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120303, '2012-03-03 00:00:00',6,3,63,9,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1159 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120304, '2012-03-04 00:00:00',7,4,64,9,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1160 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120305, '2012-03-05 00:00:00',1,5,65,10,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1161 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120306, '2012-03-06 00:00:00',2,6,66,10,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1162 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120307, '2012-03-07 00:00:00',3,7,67,10,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1163 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120308, '2012-03-08 00:00:00',4,8,68,10,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1164 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120309, '2012-03-09 00:00:00',5,9,69,10,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1165 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120310, '2012-03-10 00:00:00',6,10,70,10,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1166 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120311, '2012-03-11 00:00:00',7,11,71,10,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1167 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120312, '2012-03-12 00:00:00',1,12,72,11,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1168 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120313, '2012-03-13 00:00:00',2,13,73,11,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1169 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120314, '2012-03-14 00:00:00',3,14,74,11,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1170 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120315, '2012-03-15 00:00:00',4,15,75,11,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1171 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120316, '2012-03-16 00:00:00',5,16,76,11,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1172 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120317, '2012-03-17 00:00:00',6,17,77,11,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1173 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120318, '2012-03-18 00:00:00',7,18,78,11,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1174 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120319, '2012-03-19 00:00:00',1,19,79,12,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1175 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120320, '2012-03-20 00:00:00',2,20,80,12,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1176 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120321, '2012-03-21 00:00:00',3,21,81,12,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1177 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120322, '2012-03-22 00:00:00',4,22,82,12,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1178 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120323, '2012-03-23 00:00:00',5,23,83,12,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1179 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120324, '2012-03-24 00:00:00',6,24,84,12,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1180 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120325, '2012-03-25 00:00:00',7,25,85,12,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1181 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120326, '2012-03-26 00:00:00',1,26,86,13,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1182 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120327, '2012-03-27 00:00:00',2,27,87,13,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1183 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120328, '2012-03-28 00:00:00',3,28,88,13,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1184 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120329, '2012-03-29 00:00:00',4,29,89,13,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1185 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120330, '2012-03-30 00:00:00',5,30,90,13,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1186 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120331, '2012-03-31 00:00:00',6,31,91,13,3,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1187 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120401, '2012-04-01 00:00:00',7,1,92,13,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1188 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120402, '2012-04-02 00:00:00',1,2,93,14,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1189 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120403, '2012-04-03 00:00:00',2,3,94,14,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1190 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120404, '2012-04-04 00:00:00',3,4,95,14,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1191 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120405, '2012-04-05 00:00:00',4,5,96,14,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1192 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120406, '2012-04-06 00:00:00',5,6,97,14,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1193 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120407, '2012-04-07 00:00:00',6,7,98,14,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1194 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120408, '2012-04-08 00:00:00',7,8,99,14,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1195 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120409, '2012-04-09 00:00:00',1,9,100,15,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1196 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120410, '2012-04-10 00:00:00',2,10,101,15,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1197 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120411, '2012-04-11 00:00:00',3,11,102,15,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1198 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120412, '2012-04-12 00:00:00',4,12,103,15,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1199 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120413, '2012-04-13 00:00:00',5,13,104,15,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1200 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120414, '2012-04-14 00:00:00',6,14,105,15,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1201 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120415, '2012-04-15 00:00:00',7,15,106,15,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1202 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120416, '2012-04-16 00:00:00',1,16,107,16,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1203 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120417, '2012-04-17 00:00:00',2,17,108,16,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1204 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120418, '2012-04-18 00:00:00',3,18,109,16,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1205 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120419, '2012-04-19 00:00:00',4,19,110,16,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1206 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120420, '2012-04-20 00:00:00',5,20,111,16,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1207 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120421, '2012-04-21 00:00:00',6,21,112,16,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1208 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120422, '2012-04-22 00:00:00',7,22,113,16,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1209 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120423, '2012-04-23 00:00:00',1,23,114,17,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1210 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120424, '2012-04-24 00:00:00',2,24,115,17,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1211 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120425, '2012-04-25 00:00:00',3,25,116,17,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1212 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120426, '2012-04-26 00:00:00',4,26,117,17,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1213 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120427, '2012-04-27 00:00:00',5,27,118,17,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1214 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120428, '2012-04-28 00:00:00',6,28,119,17,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1215 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120429, '2012-04-29 00:00:00',7,29,120,17,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1216 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120430, '2012-04-30 00:00:00',1,30,121,18,4,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1217 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120501, '2012-05-01 00:00:00',2,1,122,18,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1218 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120502, '2012-05-02 00:00:00',3,2,123,18,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1219 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120503, '2012-05-03 00:00:00',4,3,124,18,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1220 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120504, '2012-05-04 00:00:00',5,4,125,18,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1221 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120505, '2012-05-05 00:00:00',6,5,126,18,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1222 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120506, '2012-05-06 00:00:00',7,6,127,18,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1223 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120507, '2012-05-07 00:00:00',1,7,128,19,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1224 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120508, '2012-05-08 00:00:00',2,8,129,19,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1225 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120509, '2012-05-09 00:00:00',3,9,130,19,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1226 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120510, '2012-05-10 00:00:00',4,10,131,19,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1227 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120511, '2012-05-11 00:00:00',5,11,132,19,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1228 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120512, '2012-05-12 00:00:00',6,12,133,19,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1229 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120513, '2012-05-13 00:00:00',7,13,134,19,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1230 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120514, '2012-05-14 00:00:00',1,14,135,20,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1231 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120515, '2012-05-15 00:00:00',2,15,136,20,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1232 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120516, '2012-05-16 00:00:00',3,16,137,20,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1233 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120517, '2012-05-17 00:00:00',4,17,138,20,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1234 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120518, '2012-05-18 00:00:00',5,18,139,20,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1235 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120519, '2012-05-19 00:00:00',6,19,140,20,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1236 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120520, '2012-05-20 00:00:00',7,20,141,20,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1237 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120521, '2012-05-21 00:00:00',1,21,142,21,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1238 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120522, '2012-05-22 00:00:00',2,22,143,21,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1239 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120523, '2012-05-23 00:00:00',3,23,144,21,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1240 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120524, '2012-05-24 00:00:00',4,24,145,21,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1241 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120525, '2012-05-25 00:00:00',5,25,146,21,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1242 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120526, '2012-05-26 00:00:00',6,26,147,21,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1243 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120527, '2012-05-27 00:00:00',7,27,148,21,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1244 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120528, '2012-05-28 00:00:00',1,28,149,22,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1245 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120529, '2012-05-29 00:00:00',2,29,150,22,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1246 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120530, '2012-05-30 00:00:00',3,30,151,22,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1247 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120531, '2012-05-31 00:00:00',4,31,152,22,5,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1248 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120601, '2012-06-01 00:00:00',5,1,153,22,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1249 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120602, '2012-06-02 00:00:00',6,2,154,22,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1250 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120603, '2012-06-03 00:00:00',7,3,155,22,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1251 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120604, '2012-06-04 00:00:00',1,4,156,23,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1252 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120605, '2012-06-05 00:00:00',2,5,157,23,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1253 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120606, '2012-06-06 00:00:00',3,6,158,23,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1254 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120607, '2012-06-07 00:00:00',4,7,159,23,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1255 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120608, '2012-06-08 00:00:00',5,8,160,23,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1256 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120609, '2012-06-09 00:00:00',6,9,161,23,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1257 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120610, '2012-06-10 00:00:00',7,10,162,23,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1258 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120611, '2012-06-11 00:00:00',1,11,163,24,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1259 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120612, '2012-06-12 00:00:00',2,12,164,24,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1260 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120613, '2012-06-13 00:00:00',3,13,165,24,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1261 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120614, '2012-06-14 00:00:00',4,14,166,24,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1262 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120615, '2012-06-15 00:00:00',5,15,167,24,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1263 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120616, '2012-06-16 00:00:00',6,16,168,24,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1264 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120617, '2012-06-17 00:00:00',7,17,169,24,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1265 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120618, '2012-06-18 00:00:00',1,18,170,25,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1266 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120619, '2012-06-19 00:00:00',2,19,171,25,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1267 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120620, '2012-06-20 00:00:00',3,20,172,25,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1268 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120621, '2012-06-21 00:00:00',4,21,173,25,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1269 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120622, '2012-06-22 00:00:00',5,22,174,25,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1270 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120623, '2012-06-23 00:00:00',6,23,175,25,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1271 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120624, '2012-06-24 00:00:00',7,24,176,25,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1272 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120625, '2012-06-25 00:00:00',1,25,177,26,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1273 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120626, '2012-06-26 00:00:00',2,26,178,26,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1274 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120627, '2012-06-27 00:00:00',3,27,179,26,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1275 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120628, '2012-06-28 00:00:00',4,28,180,26,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1276 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120629, '2012-06-29 00:00:00',5,29,181,26,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1277 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120630, '2012-06-30 00:00:00',6,30,182,26,6,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1278 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120701, '2012-07-01 00:00:00',7,1,183,26,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1279 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120702, '2012-07-02 00:00:00',1,2,184,27,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1280 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120703, '2012-07-03 00:00:00',2,3,185,27,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1281 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120704, '2012-07-04 00:00:00',3,4,186,27,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1282 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120705, '2012-07-05 00:00:00',4,5,187,27,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1283 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120706, '2012-07-06 00:00:00',5,6,188,27,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1284 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120707, '2012-07-07 00:00:00',6,7,189,27,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1285 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120708, '2012-07-08 00:00:00',7,8,190,27,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1286 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120709, '2012-07-09 00:00:00',1,9,191,28,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1287 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120710, '2012-07-10 00:00:00',2,10,192,28,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1288 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120711, '2012-07-11 00:00:00',3,11,193,28,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1289 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120712, '2012-07-12 00:00:00',4,12,194,28,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1290 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120713, '2012-07-13 00:00:00',5,13,195,28,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1291 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120714, '2012-07-14 00:00:00',6,14,196,28,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1292 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120715, '2012-07-15 00:00:00',7,15,197,28,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1293 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120716, '2012-07-16 00:00:00',1,16,198,29,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1294 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120717, '2012-07-17 00:00:00',2,17,199,29,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1295 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120718, '2012-07-18 00:00:00',3,18,200,29,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1296 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120719, '2012-07-19 00:00:00',4,19,201,29,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1297 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120720, '2012-07-20 00:00:00',5,20,202,29,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1298 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120721, '2012-07-21 00:00:00',6,21,203,29,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1299 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120722, '2012-07-22 00:00:00',7,22,204,29,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1300 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120723, '2012-07-23 00:00:00',1,23,205,30,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1301 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120724, '2012-07-24 00:00:00',2,24,206,30,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1302 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120725, '2012-07-25 00:00:00',3,25,207,30,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1303 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120726, '2012-07-26 00:00:00',4,26,208,30,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1304 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120727, '2012-07-27 00:00:00',5,27,209,30,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1305 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120728, '2012-07-28 00:00:00',6,28,210,30,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1306 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120729, '2012-07-29 00:00:00',7,29,211,30,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1307 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120730, '2012-07-30 00:00:00',1,30,212,31,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1308 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120731, '2012-07-31 00:00:00',2,31,213,31,7,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1309 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120801, '2012-08-01 00:00:00',3,1,214,31,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1310 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120802, '2012-08-02 00:00:00',4,2,215,31,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1311 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120803, '2012-08-03 00:00:00',5,3,216,31,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1312 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120804, '2012-08-04 00:00:00',6,4,217,31,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1313 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120805, '2012-08-05 00:00:00',7,5,218,31,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1314 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120806, '2012-08-06 00:00:00',1,6,219,32,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1315 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120807, '2012-08-07 00:00:00',2,7,220,32,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1316 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120808, '2012-08-08 00:00:00',3,8,221,32,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1317 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120809, '2012-08-09 00:00:00',4,9,222,32,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1318 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120810, '2012-08-10 00:00:00',5,10,223,32,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1319 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120811, '2012-08-11 00:00:00',6,11,224,32,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1320 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120812, '2012-08-12 00:00:00',7,12,225,32,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1321 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120813, '2012-08-13 00:00:00',1,13,226,33,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1322 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120814, '2012-08-14 00:00:00',2,14,227,33,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1323 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120815, '2012-08-15 00:00:00',3,15,228,33,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1324 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120816, '2012-08-16 00:00:00',4,16,229,33,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1325 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120817, '2012-08-17 00:00:00',5,17,230,33,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1326 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120818, '2012-08-18 00:00:00',6,18,231,33,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1327 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120819, '2012-08-19 00:00:00',7,19,232,33,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1328 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120820, '2012-08-20 00:00:00',1,20,233,34,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1329 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120821, '2012-08-21 00:00:00',2,21,234,34,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1330 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120822, '2012-08-22 00:00:00',3,22,235,34,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1331 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120823, '2012-08-23 00:00:00',4,23,236,34,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1332 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120824, '2012-08-24 00:00:00',5,24,237,34,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1333 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120825, '2012-08-25 00:00:00',6,25,238,34,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1334 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120826, '2012-08-26 00:00:00',7,26,239,34,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1335 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120827, '2012-08-27 00:00:00',1,27,240,35,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1336 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120828, '2012-08-28 00:00:00',2,28,241,35,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1337 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120829, '2012-08-29 00:00:00',3,29,242,35,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1338 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120830, '2012-08-30 00:00:00',4,30,243,35,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1339 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120831, '2012-08-31 00:00:00',5,31,244,35,8,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1340 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120901, '2012-09-01 00:00:00',6,1,245,35,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1341 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120902, '2012-09-02 00:00:00',7,2,246,35,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1342 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120903, '2012-09-03 00:00:00',1,3,247,36,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1343 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120904, '2012-09-04 00:00:00',2,4,248,36,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1344 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120905, '2012-09-05 00:00:00',3,5,249,36,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1345 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120906, '2012-09-06 00:00:00',4,6,250,36,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1346 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120907, '2012-09-07 00:00:00',5,7,251,36,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1347 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120908, '2012-09-08 00:00:00',6,8,252,36,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1348 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120909, '2012-09-09 00:00:00',7,9,253,36,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1349 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120910, '2012-09-10 00:00:00',1,10,254,37,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1350 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120911, '2012-09-11 00:00:00',2,11,255,37,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1351 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120912, '2012-09-12 00:00:00',3,12,256,37,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1352 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120913, '2012-09-13 00:00:00',4,13,257,37,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1353 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120914, '2012-09-14 00:00:00',5,14,258,37,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1354 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120915, '2012-09-15 00:00:00',6,15,259,37,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1355 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120916, '2012-09-16 00:00:00',7,16,260,37,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1356 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120917, '2012-09-17 00:00:00',1,17,261,38,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1357 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120918, '2012-09-18 00:00:00',2,18,262,38,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1358 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120919, '2012-09-19 00:00:00',3,19,263,38,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1359 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120920, '2012-09-20 00:00:00',4,20,264,38,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1360 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120921, '2012-09-21 00:00:00',5,21,265,38,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1361 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120922, '2012-09-22 00:00:00',6,22,266,38,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1362 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120923, '2012-09-23 00:00:00',7,23,267,38,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1363 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120924, '2012-09-24 00:00:00',1,24,268,39,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1364 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120925, '2012-09-25 00:00:00',2,25,269,39,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1365 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120926, '2012-09-26 00:00:00',3,26,270,39,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1366 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120927, '2012-09-27 00:00:00',4,27,271,39,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1367 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120928, '2012-09-28 00:00:00',5,28,272,39,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1368 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120929, '2012-09-29 00:00:00',6,29,273,39,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1369 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20120930, '2012-09-30 00:00:00',7,30,274,39,9,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1370 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121001, '2012-10-01 00:00:00',1,1,275,40,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1371 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121002, '2012-10-02 00:00:00',2,2,276,40,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1372 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121003, '2012-10-03 00:00:00',3,3,277,40,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1373 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121004, '2012-10-04 00:00:00',4,4,278,40,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1374 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121005, '2012-10-05 00:00:00',5,5,279,40,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1375 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121006, '2012-10-06 00:00:00',6,6,280,40,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1376 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121007, '2012-10-07 00:00:00',7,7,281,40,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1377 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121008, '2012-10-08 00:00:00',1,8,282,41,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1378 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121009, '2012-10-09 00:00:00',2,9,283,41,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1379 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121010, '2012-10-10 00:00:00',3,10,284,41,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1380 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121011, '2012-10-11 00:00:00',4,11,285,41,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1381 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121012, '2012-10-12 00:00:00',5,12,286,41,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1382 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121013, '2012-10-13 00:00:00',6,13,287,41,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1383 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121014, '2012-10-14 00:00:00',7,14,288,41,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1384 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121015, '2012-10-15 00:00:00',1,15,289,42,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1385 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121016, '2012-10-16 00:00:00',2,16,290,42,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1386 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121017, '2012-10-17 00:00:00',3,17,291,42,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1387 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121018, '2012-10-18 00:00:00',4,18,292,42,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1388 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121019, '2012-10-19 00:00:00',5,19,293,42,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1389 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121020, '2012-10-20 00:00:00',6,20,294,42,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1390 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121021, '2012-10-21 00:00:00',7,21,295,42,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1391 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121022, '2012-10-22 00:00:00',1,22,296,43,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1392 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121023, '2012-10-23 00:00:00',2,23,297,43,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1393 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121024, '2012-10-24 00:00:00',3,24,298,43,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1394 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121025, '2012-10-25 00:00:00',4,25,299,43,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1395 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121026, '2012-10-26 00:00:00',5,26,300,43,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1396 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121027, '2012-10-27 00:00:00',6,27,301,43,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1397 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121028, '2012-10-28 00:00:00',7,28,302,43,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1398 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121029, '2012-10-29 00:00:00',1,29,303,44,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1399 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121030, '2012-10-30 00:00:00',2,30,304,44,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1400 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121031, '2012-10-31 00:00:00',3,31,305,44,10,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1401 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121101, '2012-11-01 00:00:00',4,1,306,44,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1402 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121102, '2012-11-02 00:00:00',5,2,307,44,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1403 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121103, '2012-11-03 00:00:00',6,3,308,44,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1404 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121104, '2012-11-04 00:00:00',7,4,309,44,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1405 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121105, '2012-11-05 00:00:00',1,5,310,45,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1406 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121106, '2012-11-06 00:00:00',2,6,311,45,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1407 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121107, '2012-11-07 00:00:00',3,7,312,45,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1408 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121108, '2012-11-08 00:00:00',4,8,313,45,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1409 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121109, '2012-11-09 00:00:00',5,9,314,45,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1410 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121110, '2012-11-10 00:00:00',6,10,315,45,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1411 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121111, '2012-11-11 00:00:00',7,11,316,45,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1412 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121112, '2012-11-12 00:00:00',1,12,317,46,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1413 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121113, '2012-11-13 00:00:00',2,13,318,46,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1414 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121114, '2012-11-14 00:00:00',3,14,319,46,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1415 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121115, '2012-11-15 00:00:00',4,15,320,46,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1416 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121116, '2012-11-16 00:00:00',5,16,321,46,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1417 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121117, '2012-11-17 00:00:00',6,17,322,46,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1418 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121118, '2012-11-18 00:00:00',7,18,323,46,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1419 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121119, '2012-11-19 00:00:00',1,19,324,47,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1420 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121120, '2012-11-20 00:00:00',2,20,325,47,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1421 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121121, '2012-11-21 00:00:00',3,21,326,47,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1422 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121122, '2012-11-22 00:00:00',4,22,327,47,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1423 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121123, '2012-11-23 00:00:00',5,23,328,47,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1424 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121124, '2012-11-24 00:00:00',6,24,329,47,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1425 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121125, '2012-11-25 00:00:00',7,25,330,47,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1426 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121126, '2012-11-26 00:00:00',1,26,331,48,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1427 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121127, '2012-11-27 00:00:00',2,27,332,48,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1428 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121128, '2012-11-28 00:00:00',3,28,333,48,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1429 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121129, '2012-11-29 00:00:00',4,29,334,48,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1430 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121130, '2012-11-30 00:00:00',5,30,335,48,11,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1431 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121201, '2012-12-01 00:00:00',6,1,336,48,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1432 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121202, '2012-12-02 00:00:00',7,2,337,48,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1433 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121203, '2012-12-03 00:00:00',1,3,338,49,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1434 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121204, '2012-12-04 00:00:00',2,4,339,49,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1435 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121205, '2012-12-05 00:00:00',3,5,340,49,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1436 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121206, '2012-12-06 00:00:00',4,6,341,49,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1437 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121207, '2012-12-07 00:00:00',5,7,342,49,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1438 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121208, '2012-12-08 00:00:00',6,8,343,49,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1439 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121209, '2012-12-09 00:00:00',7,9,344,49,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1440 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121210, '2012-12-10 00:00:00',1,10,345,50,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1441 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121211, '2012-12-11 00:00:00',2,11,346,50,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1442 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121212, '2012-12-12 00:00:00',3,12,347,50,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1443 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121213, '2012-12-13 00:00:00',4,13,348,50,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1444 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121214, '2012-12-14 00:00:00',5,14,349,50,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1445 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121215, '2012-12-15 00:00:00',6,15,350,50,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1446 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121216, '2012-12-16 00:00:00',7,16,351,50,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1447 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121217, '2012-12-17 00:00:00',1,17,352,51,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1448 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121218, '2012-12-18 00:00:00',2,18,353,51,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1449 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121219, '2012-12-19 00:00:00',3,19,354,51,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1450 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121220, '2012-12-20 00:00:00',4,20,355,51,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1451 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121221, '2012-12-21 00:00:00',5,21,356,51,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1452 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121222, '2012-12-22 00:00:00',6,22,357,51,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1453 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121223, '2012-12-23 00:00:00',7,23,358,51,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1454 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121224, '2012-12-24 00:00:00',1,24,359,52,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1455 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121225, '2012-12-25 00:00:00',2,25,360,52,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1456 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121226, '2012-12-26 00:00:00',3,26,361,52,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1457 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121227, '2012-12-27 00:00:00',4,27,362,52,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1458 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121228, '2012-12-28 00:00:00',5,28,363,52,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1459 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121229, '2012-12-29 00:00:00',6,29,364,52,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1460 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121230, '2012-12-30 00:00:00',7,30,365,52,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1461 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20121231, '2012-12-31 00:00:00',1,31,366,1,12,2012,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1462 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130101, '2013-01-01 00:00:00',2,1,1,1,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1463 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130102, '2013-01-02 00:00:00',3,2,2,1,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1464 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130103, '2013-01-03 00:00:00',4,3,3,1,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1465 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130104, '2013-01-04 00:00:00',5,4,4,1,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1466 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130105, '2013-01-05 00:00:00',6,5,5,1,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1467 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130106, '2013-01-06 00:00:00',7,6,6,1,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1468 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130107, '2013-01-07 00:00:00',1,7,7,2,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1469 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130108, '2013-01-08 00:00:00',2,8,8,2,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1470 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130109, '2013-01-09 00:00:00',3,9,9,2,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1471 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130110, '2013-01-10 00:00:00',4,10,10,2,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1472 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130111, '2013-01-11 00:00:00',5,11,11,2,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1473 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130112, '2013-01-12 00:00:00',6,12,12,2,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1474 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130113, '2013-01-13 00:00:00',7,13,13,2,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1475 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130114, '2013-01-14 00:00:00',1,14,14,3,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1476 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130115, '2013-01-15 00:00:00',2,15,15,3,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1477 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130116, '2013-01-16 00:00:00',3,16,16,3,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1478 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130117, '2013-01-17 00:00:00',4,17,17,3,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1479 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130118, '2013-01-18 00:00:00',5,18,18,3,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1480 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130119, '2013-01-19 00:00:00',6,19,19,3,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1481 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130120, '2013-01-20 00:00:00',7,20,20,3,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1482 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130121, '2013-01-21 00:00:00',1,21,21,4,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1483 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130122, '2013-01-22 00:00:00',2,22,22,4,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1484 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130123, '2013-01-23 00:00:00',3,23,23,4,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1485 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130124, '2013-01-24 00:00:00',4,24,24,4,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1486 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130125, '2013-01-25 00:00:00',5,25,25,4,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1487 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130126, '2013-01-26 00:00:00',6,26,26,4,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1488 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130127, '2013-01-27 00:00:00',7,27,27,4,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1489 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130128, '2013-01-28 00:00:00',1,28,28,5,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1490 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130129, '2013-01-29 00:00:00',2,29,29,5,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1491 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130130, '2013-01-30 00:00:00',3,30,30,5,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1492 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130131, '2013-01-31 00:00:00',4,31,31,5,1,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1493 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130201, '2013-02-01 00:00:00',5,1,32,5,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1494 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130202, '2013-02-02 00:00:00',6,2,33,5,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1495 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130203, '2013-02-03 00:00:00',7,3,34,5,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1496 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130204, '2013-02-04 00:00:00',1,4,35,6,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1497 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130205, '2013-02-05 00:00:00',2,5,36,6,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1498 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130206, '2013-02-06 00:00:00',3,6,37,6,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1499 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130207, '2013-02-07 00:00:00',4,7,38,6,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1500 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130208, '2013-02-08 00:00:00',5,8,39,6,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1501 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130209, '2013-02-09 00:00:00',6,9,40,6,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1502 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130210, '2013-02-10 00:00:00',7,10,41,6,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1503 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130211, '2013-02-11 00:00:00',1,11,42,7,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1504 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130212, '2013-02-12 00:00:00',2,12,43,7,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1505 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130213, '2013-02-13 00:00:00',3,13,44,7,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1506 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130214, '2013-02-14 00:00:00',4,14,45,7,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1507 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130215, '2013-02-15 00:00:00',5,15,46,7,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1508 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130216, '2013-02-16 00:00:00',6,16,47,7,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1509 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130217, '2013-02-17 00:00:00',7,17,48,7,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1510 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130218, '2013-02-18 00:00:00',1,18,49,8,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1511 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130219, '2013-02-19 00:00:00',2,19,50,8,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1512 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130220, '2013-02-20 00:00:00',3,20,51,8,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1513 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130221, '2013-02-21 00:00:00',4,21,52,8,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1514 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130222, '2013-02-22 00:00:00',5,22,53,8,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1515 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130223, '2013-02-23 00:00:00',6,23,54,8,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1516 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130224, '2013-02-24 00:00:00',7,24,55,8,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1517 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130225, '2013-02-25 00:00:00',1,25,56,9,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1518 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130226, '2013-02-26 00:00:00',2,26,57,9,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1519 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130227, '2013-02-27 00:00:00',3,27,58,9,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1520 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130228, '2013-02-28 00:00:00',4,28,59,9,2,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1521 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130301, '2013-03-01 00:00:00',5,1,60,9,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1522 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130302, '2013-03-02 00:00:00',6,2,61,9,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1523 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130303, '2013-03-03 00:00:00',7,3,62,9,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1524 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130304, '2013-03-04 00:00:00',1,4,63,10,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1525 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130305, '2013-03-05 00:00:00',2,5,64,10,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1526 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130306, '2013-03-06 00:00:00',3,6,65,10,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1527 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130307, '2013-03-07 00:00:00',4,7,66,10,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1528 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130308, '2013-03-08 00:00:00',5,8,67,10,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1529 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130309, '2013-03-09 00:00:00',6,9,68,10,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1530 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130310, '2013-03-10 00:00:00',7,10,69,10,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1531 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130311, '2013-03-11 00:00:00',1,11,70,11,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1532 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130312, '2013-03-12 00:00:00',2,12,71,11,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1533 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130313, '2013-03-13 00:00:00',3,13,72,11,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1534 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130314, '2013-03-14 00:00:00',4,14,73,11,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1535 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130315, '2013-03-15 00:00:00',5,15,74,11,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1536 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130316, '2013-03-16 00:00:00',6,16,75,11,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1537 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130317, '2013-03-17 00:00:00',7,17,76,11,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1538 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130318, '2013-03-18 00:00:00',1,18,77,12,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1539 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130319, '2013-03-19 00:00:00',2,19,78,12,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1540 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130320, '2013-03-20 00:00:00',3,20,79,12,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1541 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130321, '2013-03-21 00:00:00',4,21,80,12,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1542 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130322, '2013-03-22 00:00:00',5,22,81,12,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1543 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130323, '2013-03-23 00:00:00',6,23,82,12,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1544 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130324, '2013-03-24 00:00:00',7,24,83,12,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1545 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130325, '2013-03-25 00:00:00',1,25,84,13,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1546 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130326, '2013-03-26 00:00:00',2,26,85,13,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1547 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130327, '2013-03-27 00:00:00',3,27,86,13,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1548 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130328, '2013-03-28 00:00:00',4,28,87,13,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1549 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130329, '2013-03-29 00:00:00',5,29,88,13,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1550 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130330, '2013-03-30 00:00:00',6,30,89,13,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1551 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130331, '2013-03-31 00:00:00',7,31,90,13,3,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1552 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130401, '2013-04-01 00:00:00',1,1,91,14,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1553 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130402, '2013-04-02 00:00:00',2,2,92,14,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1554 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130403, '2013-04-03 00:00:00',3,3,93,14,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1555 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130404, '2013-04-04 00:00:00',4,4,94,14,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1556 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130405, '2013-04-05 00:00:00',5,5,95,14,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1557 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130406, '2013-04-06 00:00:00',6,6,96,14,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1558 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130407, '2013-04-07 00:00:00',7,7,97,14,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1559 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130408, '2013-04-08 00:00:00',1,8,98,15,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1560 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130409, '2013-04-09 00:00:00',2,9,99,15,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1561 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130410, '2013-04-10 00:00:00',3,10,100,15,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1562 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130411, '2013-04-11 00:00:00',4,11,101,15,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1563 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130412, '2013-04-12 00:00:00',5,12,102,15,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1564 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130413, '2013-04-13 00:00:00',6,13,103,15,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1565 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130414, '2013-04-14 00:00:00',7,14,104,15,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1566 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130415, '2013-04-15 00:00:00',1,15,105,16,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1567 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130416, '2013-04-16 00:00:00',2,16,106,16,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1568 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130417, '2013-04-17 00:00:00',3,17,107,16,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1569 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130418, '2013-04-18 00:00:00',4,18,108,16,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1570 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130419, '2013-04-19 00:00:00',5,19,109,16,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1571 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130420, '2013-04-20 00:00:00',6,20,110,16,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1572 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130421, '2013-04-21 00:00:00',7,21,111,16,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1573 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130422, '2013-04-22 00:00:00',1,22,112,17,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1574 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130423, '2013-04-23 00:00:00',2,23,113,17,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1575 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130424, '2013-04-24 00:00:00',3,24,114,17,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1576 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130425, '2013-04-25 00:00:00',4,25,115,17,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1577 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130426, '2013-04-26 00:00:00',5,26,116,17,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1578 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130427, '2013-04-27 00:00:00',6,27,117,17,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1579 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130428, '2013-04-28 00:00:00',7,28,118,17,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1580 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130429, '2013-04-29 00:00:00',1,29,119,18,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1581 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130430, '2013-04-30 00:00:00',2,30,120,18,4,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1582 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130501, '2013-05-01 00:00:00',3,1,121,18,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1583 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130502, '2013-05-02 00:00:00',4,2,122,18,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1584 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130503, '2013-05-03 00:00:00',5,3,123,18,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1585 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130504, '2013-05-04 00:00:00',6,4,124,18,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1586 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130505, '2013-05-05 00:00:00',7,5,125,18,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1587 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130506, '2013-05-06 00:00:00',1,6,126,19,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1588 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130507, '2013-05-07 00:00:00',2,7,127,19,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1589 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130508, '2013-05-08 00:00:00',3,8,128,19,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1590 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130509, '2013-05-09 00:00:00',4,9,129,19,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1591 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130510, '2013-05-10 00:00:00',5,10,130,19,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1592 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130511, '2013-05-11 00:00:00',6,11,131,19,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1593 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130512, '2013-05-12 00:00:00',7,12,132,19,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1594 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130513, '2013-05-13 00:00:00',1,13,133,20,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1595 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130514, '2013-05-14 00:00:00',2,14,134,20,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1596 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130515, '2013-05-15 00:00:00',3,15,135,20,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1597 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130516, '2013-05-16 00:00:00',4,16,136,20,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1598 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130517, '2013-05-17 00:00:00',5,17,137,20,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1599 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130518, '2013-05-18 00:00:00',6,18,138,20,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1600 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130519, '2013-05-19 00:00:00',7,19,139,20,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1601 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130520, '2013-05-20 00:00:00',1,20,140,21,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1602 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130521, '2013-05-21 00:00:00',2,21,141,21,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1603 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130522, '2013-05-22 00:00:00',3,22,142,21,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1604 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130523, '2013-05-23 00:00:00',4,23,143,21,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1605 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130524, '2013-05-24 00:00:00',5,24,144,21,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1606 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130525, '2013-05-25 00:00:00',6,25,145,21,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1607 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130526, '2013-05-26 00:00:00',7,26,146,21,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1608 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130527, '2013-05-27 00:00:00',1,27,147,22,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1609 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130528, '2013-05-28 00:00:00',2,28,148,22,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1610 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130529, '2013-05-29 00:00:00',3,29,149,22,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1611 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130530, '2013-05-30 00:00:00',4,30,150,22,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1612 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130531, '2013-05-31 00:00:00',5,31,151,22,5,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1613 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130601, '2013-06-01 00:00:00',6,1,152,22,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1614 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130602, '2013-06-02 00:00:00',7,2,153,22,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1615 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130603, '2013-06-03 00:00:00',1,3,154,23,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1616 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130604, '2013-06-04 00:00:00',2,4,155,23,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1617 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130605, '2013-06-05 00:00:00',3,5,156,23,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1618 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130606, '2013-06-06 00:00:00',4,6,157,23,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1619 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130607, '2013-06-07 00:00:00',5,7,158,23,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1620 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130608, '2013-06-08 00:00:00',6,8,159,23,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1621 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130609, '2013-06-09 00:00:00',7,9,160,23,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1622 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130610, '2013-06-10 00:00:00',1,10,161,24,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1623 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130611, '2013-06-11 00:00:00',2,11,162,24,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1624 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130612, '2013-06-12 00:00:00',3,12,163,24,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1625 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130613, '2013-06-13 00:00:00',4,13,164,24,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1626 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130614, '2013-06-14 00:00:00',5,14,165,24,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1627 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130615, '2013-06-15 00:00:00',6,15,166,24,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1628 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130616, '2013-06-16 00:00:00',7,16,167,24,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1629 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130617, '2013-06-17 00:00:00',1,17,168,25,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1630 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130618, '2013-06-18 00:00:00',2,18,169,25,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1631 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130619, '2013-06-19 00:00:00',3,19,170,25,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1632 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130620, '2013-06-20 00:00:00',4,20,171,25,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1633 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130621, '2013-06-21 00:00:00',5,21,172,25,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1634 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130622, '2013-06-22 00:00:00',6,22,173,25,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1635 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130623, '2013-06-23 00:00:00',7,23,174,25,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1636 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130624, '2013-06-24 00:00:00',1,24,175,26,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1637 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130625, '2013-06-25 00:00:00',2,25,176,26,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1638 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130626, '2013-06-26 00:00:00',3,26,177,26,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1639 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130627, '2013-06-27 00:00:00',4,27,178,26,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1640 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130628, '2013-06-28 00:00:00',5,28,179,26,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1641 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130629, '2013-06-29 00:00:00',6,29,180,26,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1642 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130630, '2013-06-30 00:00:00',7,30,181,26,6,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1643 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130701, '2013-07-01 00:00:00',1,1,182,27,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1644 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130702, '2013-07-02 00:00:00',2,2,183,27,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1645 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130703, '2013-07-03 00:00:00',3,3,184,27,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1646 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130704, '2013-07-04 00:00:00',4,4,185,27,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1647 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130705, '2013-07-05 00:00:00',5,5,186,27,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1648 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130706, '2013-07-06 00:00:00',6,6,187,27,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1649 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130707, '2013-07-07 00:00:00',7,7,188,27,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1650 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130708, '2013-07-08 00:00:00',1,8,189,28,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1651 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130709, '2013-07-09 00:00:00',2,9,190,28,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1652 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130710, '2013-07-10 00:00:00',3,10,191,28,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1653 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130711, '2013-07-11 00:00:00',4,11,192,28,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1654 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130712, '2013-07-12 00:00:00',5,12,193,28,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1655 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130713, '2013-07-13 00:00:00',6,13,194,28,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1656 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130714, '2013-07-14 00:00:00',7,14,195,28,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1657 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130715, '2013-07-15 00:00:00',1,15,196,29,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1658 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130716, '2013-07-16 00:00:00',2,16,197,29,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1659 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130717, '2013-07-17 00:00:00',3,17,198,29,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1660 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130718, '2013-07-18 00:00:00',4,18,199,29,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1661 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130719, '2013-07-19 00:00:00',5,19,200,29,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1662 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130720, '2013-07-20 00:00:00',6,20,201,29,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1663 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130721, '2013-07-21 00:00:00',7,21,202,29,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1664 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130722, '2013-07-22 00:00:00',1,22,203,30,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1665 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130723, '2013-07-23 00:00:00',2,23,204,30,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1666 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130724, '2013-07-24 00:00:00',3,24,205,30,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1667 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130725, '2013-07-25 00:00:00',4,25,206,30,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1668 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130726, '2013-07-26 00:00:00',5,26,207,30,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1669 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130727, '2013-07-27 00:00:00',6,27,208,30,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1670 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130728, '2013-07-28 00:00:00',7,28,209,30,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1671 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130729, '2013-07-29 00:00:00',1,29,210,31,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1672 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130730, '2013-07-30 00:00:00',2,30,211,31,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1673 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130731, '2013-07-31 00:00:00',3,31,212,31,7,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1674 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130801, '2013-08-01 00:00:00',4,1,213,31,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1675 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130802, '2013-08-02 00:00:00',5,2,214,31,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1676 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130803, '2013-08-03 00:00:00',6,3,215,31,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1677 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130804, '2013-08-04 00:00:00',7,4,216,31,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1678 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130805, '2013-08-05 00:00:00',1,5,217,32,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1679 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130806, '2013-08-06 00:00:00',2,6,218,32,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1680 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130807, '2013-08-07 00:00:00',3,7,219,32,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1681 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130808, '2013-08-08 00:00:00',4,8,220,32,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1682 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130809, '2013-08-09 00:00:00',5,9,221,32,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1683 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130810, '2013-08-10 00:00:00',6,10,222,32,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1684 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130811, '2013-08-11 00:00:00',7,11,223,32,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1685 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130812, '2013-08-12 00:00:00',1,12,224,33,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1686 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130813, '2013-08-13 00:00:00',2,13,225,33,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1687 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130814, '2013-08-14 00:00:00',3,14,226,33,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1688 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130815, '2013-08-15 00:00:00',4,15,227,33,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1689 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130816, '2013-08-16 00:00:00',5,16,228,33,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1690 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130817, '2013-08-17 00:00:00',6,17,229,33,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1691 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130818, '2013-08-18 00:00:00',7,18,230,33,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1692 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130819, '2013-08-19 00:00:00',1,19,231,34,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1693 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130820, '2013-08-20 00:00:00',2,20,232,34,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1694 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130821, '2013-08-21 00:00:00',3,21,233,34,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1695 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130822, '2013-08-22 00:00:00',4,22,234,34,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1696 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130823, '2013-08-23 00:00:00',5,23,235,34,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1697 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130824, '2013-08-24 00:00:00',6,24,236,34,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1698 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130825, '2013-08-25 00:00:00',7,25,237,34,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1699 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130826, '2013-08-26 00:00:00',1,26,238,35,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1700 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130827, '2013-08-27 00:00:00',2,27,239,35,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1701 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130828, '2013-08-28 00:00:00',3,28,240,35,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1702 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130829, '2013-08-29 00:00:00',4,29,241,35,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1703 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130830, '2013-08-30 00:00:00',5,30,242,35,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1704 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130831, '2013-08-31 00:00:00',6,31,243,35,8,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1705 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130901, '2013-09-01 00:00:00',7,1,244,35,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1706 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130902, '2013-09-02 00:00:00',1,2,245,36,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1707 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130903, '2013-09-03 00:00:00',2,3,246,36,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1708 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130904, '2013-09-04 00:00:00',3,4,247,36,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1709 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130905, '2013-09-05 00:00:00',4,5,248,36,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1710 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130906, '2013-09-06 00:00:00',5,6,249,36,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1711 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130907, '2013-09-07 00:00:00',6,7,250,36,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1712 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130908, '2013-09-08 00:00:00',7,8,251,36,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1713 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130909, '2013-09-09 00:00:00',1,9,252,37,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1714 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130910, '2013-09-10 00:00:00',2,10,253,37,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1715 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130911, '2013-09-11 00:00:00',3,11,254,37,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1716 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130912, '2013-09-12 00:00:00',4,12,255,37,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1717 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130913, '2013-09-13 00:00:00',5,13,256,37,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1718 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130914, '2013-09-14 00:00:00',6,14,257,37,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1719 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130915, '2013-09-15 00:00:00',7,15,258,37,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1720 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130916, '2013-09-16 00:00:00',1,16,259,38,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1721 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130917, '2013-09-17 00:00:00',2,17,260,38,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1722 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130918, '2013-09-18 00:00:00',3,18,261,38,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1723 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130919, '2013-09-19 00:00:00',4,19,262,38,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1724 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130920, '2013-09-20 00:00:00',5,20,263,38,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1725 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130921, '2013-09-21 00:00:00',6,21,264,38,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1726 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130922, '2013-09-22 00:00:00',7,22,265,38,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1727 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130923, '2013-09-23 00:00:00',1,23,266,39,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1728 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130924, '2013-09-24 00:00:00',2,24,267,39,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1729 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130925, '2013-09-25 00:00:00',3,25,268,39,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1730 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130926, '2013-09-26 00:00:00',4,26,269,39,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1731 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130927, '2013-09-27 00:00:00',5,27,270,39,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1732 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130928, '2013-09-28 00:00:00',6,28,271,39,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1733 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130929, '2013-09-29 00:00:00',7,29,272,39,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1734 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20130930, '2013-09-30 00:00:00',1,30,273,40,9,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1735 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131001, '2013-10-01 00:00:00',2,1,274,40,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1736 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131002, '2013-10-02 00:00:00',3,2,275,40,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1737 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131003, '2013-10-03 00:00:00',4,3,276,40,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1738 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131004, '2013-10-04 00:00:00',5,4,277,40,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1739 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131005, '2013-10-05 00:00:00',6,5,278,40,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1740 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131006, '2013-10-06 00:00:00',7,6,279,40,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1741 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131007, '2013-10-07 00:00:00',1,7,280,41,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1742 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131008, '2013-10-08 00:00:00',2,8,281,41,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1743 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131009, '2013-10-09 00:00:00',3,9,282,41,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1744 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131010, '2013-10-10 00:00:00',4,10,283,41,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1745 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131011, '2013-10-11 00:00:00',5,11,284,41,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1746 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131012, '2013-10-12 00:00:00',6,12,285,41,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1747 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131013, '2013-10-13 00:00:00',7,13,286,41,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1748 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131014, '2013-10-14 00:00:00',1,14,287,42,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1749 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131015, '2013-10-15 00:00:00',2,15,288,42,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1750 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131016, '2013-10-16 00:00:00',3,16,289,42,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1751 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131017, '2013-10-17 00:00:00',4,17,290,42,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1752 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131018, '2013-10-18 00:00:00',5,18,291,42,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1753 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131019, '2013-10-19 00:00:00',6,19,292,42,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1754 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131020, '2013-10-20 00:00:00',7,20,293,42,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1755 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131021, '2013-10-21 00:00:00',1,21,294,43,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1756 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131022, '2013-10-22 00:00:00',2,22,295,43,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1757 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131023, '2013-10-23 00:00:00',3,23,296,43,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1758 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131024, '2013-10-24 00:00:00',4,24,297,43,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1759 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131025, '2013-10-25 00:00:00',5,25,298,43,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1760 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131026, '2013-10-26 00:00:00',6,26,299,43,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1761 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131027, '2013-10-27 00:00:00',7,27,300,43,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1762 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131028, '2013-10-28 00:00:00',1,28,301,44,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1763 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131029, '2013-10-29 00:00:00',2,29,302,44,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1764 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131030, '2013-10-30 00:00:00',3,30,303,44,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1765 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131031, '2013-10-31 00:00:00',4,31,304,44,10,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1766 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131101, '2013-11-01 00:00:00',5,1,305,44,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1767 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131102, '2013-11-02 00:00:00',6,2,306,44,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1768 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131103, '2013-11-03 00:00:00',7,3,307,44,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1769 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131104, '2013-11-04 00:00:00',1,4,308,45,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1770 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131105, '2013-11-05 00:00:00',2,5,309,45,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1771 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131106, '2013-11-06 00:00:00',3,6,310,45,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1772 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131107, '2013-11-07 00:00:00',4,7,311,45,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1773 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131108, '2013-11-08 00:00:00',5,8,312,45,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1774 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131109, '2013-11-09 00:00:00',6,9,313,45,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1775 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131110, '2013-11-10 00:00:00',7,10,314,45,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1776 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131111, '2013-11-11 00:00:00',1,11,315,46,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1777 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131112, '2013-11-12 00:00:00',2,12,316,46,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1778 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131113, '2013-11-13 00:00:00',3,13,317,46,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1779 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131114, '2013-11-14 00:00:00',4,14,318,46,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1780 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131115, '2013-11-15 00:00:00',5,15,319,46,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1781 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131116, '2013-11-16 00:00:00',6,16,320,46,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1782 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131117, '2013-11-17 00:00:00',7,17,321,46,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1783 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131118, '2013-11-18 00:00:00',1,18,322,47,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1784 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131119, '2013-11-19 00:00:00',2,19,323,47,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1785 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131120, '2013-11-20 00:00:00',3,20,324,47,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1786 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131121, '2013-11-21 00:00:00',4,21,325,47,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1787 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131122, '2013-11-22 00:00:00',5,22,326,47,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1788 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131123, '2013-11-23 00:00:00',6,23,327,47,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1789 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131124, '2013-11-24 00:00:00',7,24,328,47,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1790 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131125, '2013-11-25 00:00:00',1,25,329,48,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1791 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131126, '2013-11-26 00:00:00',2,26,330,48,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1792 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131127, '2013-11-27 00:00:00',3,27,331,48,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1793 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131128, '2013-11-28 00:00:00',4,28,332,48,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1794 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131129, '2013-11-29 00:00:00',5,29,333,48,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1795 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131130, '2013-11-30 00:00:00',6,30,334,48,11,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1796 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131201, '2013-12-01 00:00:00',7,1,335,48,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1797 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131202, '2013-12-02 00:00:00',1,2,336,49,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1798 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131203, '2013-12-03 00:00:00',2,3,337,49,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1799 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131204, '2013-12-04 00:00:00',3,4,338,49,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1800 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131205, '2013-12-05 00:00:00',4,5,339,49,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1801 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131206, '2013-12-06 00:00:00',5,6,340,49,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1802 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131207, '2013-12-07 00:00:00',6,7,341,49,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1803 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131208, '2013-12-08 00:00:00',7,8,342,49,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1804 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131209, '2013-12-09 00:00:00',1,9,343,50,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1805 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131210, '2013-12-10 00:00:00',2,10,344,50,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1806 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131211, '2013-12-11 00:00:00',3,11,345,50,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1807 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131212, '2013-12-12 00:00:00',4,12,346,50,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1808 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131213, '2013-12-13 00:00:00',5,13,347,50,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1809 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131214, '2013-12-14 00:00:00',6,14,348,50,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1810 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131215, '2013-12-15 00:00:00',7,15,349,50,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1811 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131216, '2013-12-16 00:00:00',1,16,350,51,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1812 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131217, '2013-12-17 00:00:00',2,17,351,51,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1813 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131218, '2013-12-18 00:00:00',3,18,352,51,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1814 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131219, '2013-12-19 00:00:00',4,19,353,51,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1815 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131220, '2013-12-20 00:00:00',5,20,354,51,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1816 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131221, '2013-12-21 00:00:00',6,21,355,51,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1817 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131222, '2013-12-22 00:00:00',7,22,356,51,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1818 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131223, '2013-12-23 00:00:00',1,23,357,52,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1819 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131224, '2013-12-24 00:00:00',2,24,358,52,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1820 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131225, '2013-12-25 00:00:00',3,25,359,52,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1821 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131226, '2013-12-26 00:00:00',4,26,360,52,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1822 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131227, '2013-12-27 00:00:00',5,27,361,52,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1823 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131228, '2013-12-28 00:00:00',6,28,362,52,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1824 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131229, '2013-12-29 00:00:00',7,29,363,52,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1825 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131230, '2013-12-30 00:00:00',1,30,364,1,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');

--changeSet 0dim_time:1826 stripComments:false
INSERT INTO dim_time (DIM_TIME_ID,Day_Timestamp,Day_NumberInWeek,Day_NumberInMonth,Day_NumberInYear,Week_NumberInYear,Month_NumberInYear,Year_Key,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) VALUES (20131231, '2013-12-31 00:00:00',2,31,365,1,12,2013,0,'11-DEC-09','11-DEC-09', 0,'MXI');