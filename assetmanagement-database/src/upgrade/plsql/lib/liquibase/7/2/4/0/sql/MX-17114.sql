--liquibase formatted sql


--changeSet MX-17114:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Procedue:      SBRefreshQueryData
* Arguments:     aStatusBoardDbId, aStatusBoardId, aQueryId, aColumnId  - pk for the sb_column table
*
* Description:   This procedure updates the inventory stored in the table status_board_xxx mapped to a
*                sb_status_board where xxx is equal to the value in the sb_status_board.status_board_cd
*                This procedure assumes that the sb_column that is being input into the procedure is supposed
*                to be updated at the moment.  A dynamic update is created for data driving the column based
*                on if any modifications have occured.  If no data has changed, then an update is not performed.
*
* Orig.Coder:    cdaley
* Created Date:  2007-11-12
* Last Modified: 2007-12-14
       Modified procedure to run query only a single time
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE SBRefreshQueryInventory(
       aStatusBoardDbId IN sb_status_board.status_board_db_id%TYPE,
       aStatusBoardId   IN sb_status_board.status_board_id%TYPE,
       aQueryId         IN sb_query.query_id%TYPE,
       an_Return        OUT NUMBER
)
IS
-- data types
  TYPE col_display IS RECORD(
       columnCd         sb_column.column_cd%TYPE,
       colDisplay       sb_column.col_display%TYPE,
       colType          sb_column.col_display_data_type%TYPE,
       colSort          sb_column.col_sort%TYPE,
       colBgColor       sb_column.col_bg_color%TYPE,
       colTextColor     sb_column.col_text_color%TYPE
  );

  TYPE col_display_table IS TABLE OF col_display INDEX BY BINARY_INTEGER;

  -- cursors
  queryCursor int;

  colCollection col_display_table;

  -- the status board code
  statusBoardCd   varchar2(8);
  sqlQuery        CLOB;
  sqlUpdate       varchar2(32000);
  sqlWhere        varchar2(32000);
  tempQuery       CLOB;

  -- loop variables
  lColumn int;
  lRowsProcessed int;

  -- data storage
  invNoDbId int;
  invNoId int;
  colDisplay      VARCHAR2(4000);
  colType         VARCHAR2(8);
  colSort         VARCHAR2(4000);
  colBgColor      VARCHAR2(6);
  colTextColor    VARCHAR2(6);
  err_msg         VARCHAR2(200);


BEGIN
  --Update the last refresh start for the query
  EXECUTE IMMEDIATE 'UPDATE sb_query SET last_refresh_start = SYSDATE WHERE sb_query.status_board_db_id =' || aStatusBoardDbId || ' AND sb_query.status_board_id = ' || aStatusBoardId || ' AND sb_query.query_id = ' || aQueryId;

  --load the status board query details
  SELECT
       sb_query.query_sql,
       sb_status_board.status_board_cd
  INTO
       tempQuery,
       statusBoardCd
  FROM
       sb_query,
       sb_status_board
  WHERE
       sb_query.status_board_db_id = aStatusBoardDbId AND
       sb_query.status_board_id = aStatusBoardId AND
       sb_query.query_id = aQueryId
       AND
       sb_status_board.status_board_db_id = sb_query.status_board_db_id AND
       sb_status_board.status_board_id = sb_query.status_board_id;

  -- load the column details
  SELECT
        sb_column.column_cd,
        sb_column.col_display,
        sb_column.col_display_data_type,
        DECODE(sb_column.col_sort, null, 'null', sb_column.col_sort),
        DECODE(sb_column.col_bg_color, null, 'null', sb_column.col_bg_color),
        DECODE(sb_column.col_text_color, null, 'null', sb_column.col_text_color)
  BULK COLLECT INTO
        colCollection
  FROM
        sb_status_board,
        sb_column,
        sb_query
  WHERE
        sb_column.status_board_db_id = aStatusBoardDbId AND
        sb_column.status_board_id    = aStatusBoardId AND
        sb_column.query_id           = aQueryId
        AND
        sb_status_board.status_board_db_id = sb_column.status_board_db_id AND
        sb_status_board.status_board_id    = sb_column.status_board_id
        AND
        sb_query.status_board_db_id = sb_column.status_board_db_id AND
        sb_query.status_board_id    = sb_column.status_board_id AND
        sb_query.query_id           = sb_column.query_id;

    --create the sql query cursor

    IF(colCollection.COUNT > 0) THEN

      -- create the sql statement
      sqlQuery := 'SELECT inv_no_db_id, inv_no_id ';
      lColumn := colCollection.FIRST;
      WHILE lColumn IS NOT NULL LOOP
            sqlQuery := sqlQuery                            || ', ';
            IF (colCollection(lColumn).colType = 'Date' OR colCollection(lColumn).colType = 'DateTime' ) THEN
               sqlQuery := sqlQuery || ' to_char(' || colCollection(lColumn).colDisplay   || ', ''yyyy/mm/dd:hh:mi:ssam'' ) AS ' || colCollection(lColumn).colDisplay || ', ';
            ELSE
               sqlQuery := sqlQuery || colCollection(lColumn).colDisplay   || ', ';
            END IF;
            sqlQuery := sqlQuery || colCollection(lColumn).colSort      || ', ';
            sqlQuery := sqlQuery || ' ''' || colCollection(lColumn).colType   || ''', ';
            sqlQuery := sqlQuery || colCollection(lColumn).colBgColor   || ', ';
            sqlQuery := sqlQuery || colCollection(lColumn).colTextColor;
            lColumn := colCollection.NEXT(lColumn);
      END LOOP;

      sqlQuery := sqlQuery || ' FROM ( ' || tempQuery || ' )';

      -- open cursor for clob
      queryCursor := dbms_sql.open_cursor;
      dbms_sql.parse(queryCursor, sqlQuery, dbms_sql.native);

      -- define inventory key
      dbms_sql.define_column(queryCursor, 1, invNoDbId);
      dbms_sql.define_column(queryCursor, 2, invNoId);

      --define the columns
      lColumn := colCollection.FIRST;
      WHILE lColumn IS NOT NULL LOOP
            dbms_sql.define_column(queryCursor, ((lColumn - 1) * 5) + 3, colDisplay, 4000);
            dbms_sql.define_column(queryCursor, ((lColumn - 1) * 5) + 4, colSort, 4000);
            dbms_sql.define_column(queryCursor, ((lColumn - 1) * 5) + 5, colType, 8);
            dbms_sql.define_column(queryCursor, ((lColumn - 1) * 5) + 6, colBgColor, 6);
            dbms_sql.define_column(queryCursor, ((lColumn - 1) * 5) + 7, colTextColor, 6);
            lColumn := colCollection.NEXT(lColumn);
      END LOOP;

      lRowsProcessed := dbms_sql.execute(queryCursor);

      --open the cursor and update rows
      LOOP
          IF dbms_sql.fetch_rows(queryCursor) > 0 THEN
             -- get the inv key
             dbms_sql.column_value(queryCursor, 1, invNoDbId);
             dbms_sql.column_value(queryCursor, 2, invNoId);

             sqlUpdate := 'UPDATE STATUS_BOARD_' || statusBoardCd || ' SET ';
             sqlWhere := '';

             FOR i IN 1 .. colCollection.COUNT LOOP
                 dbms_sql.column_value(queryCursor, ((i - 1) * 5) + 3, colDisplay);
                 dbms_sql.column_value(queryCursor, ((i - 1) * 5) + 4, colSort);
                 dbms_sql.column_value(queryCursor, ((i - 1) * 5) + 5, colType);
                 dbms_sql.column_value(queryCursor, ((i - 1) * 5) + 6, colBgColor);
                 dbms_sql.column_value(queryCursor, ((i - 1) * 5) + 7, colTextColor);

                 IF i > 1 THEN
                    sqlUpdate := sqlUpdate || ', ';
                    sqlWhere := sqlWhere || ' OR ';
                 END IF;

                 IF (colType = 'Date' OR colType = 'DateTime' ) THEN
                     sqlUpdate := sqlUpdate || colCollection(i).columnCd || '  = to_date(q''@' || colDisplay || '@'', ''yyyy/mm/dd:hh:mi:ssam'')';
                     sqlWhere  := sqlWhere  || colCollection(i).columnCd || ' != to_date(q''@' || colDisplay || '@'', ''yyyy/mm/dd:hh:mi:ssam'')';
                 ELSE
                     sqlUpdate := sqlUpdate || colCollection(i).columnCd || ' = q''@' || colDisplay || '@''';
                     sqlWhere  := sqlWhere  || colCollection(i).columnCd || ' != q''@' || colDisplay || '@''';
                 END IF;
                 IF colDisplay != '' OR colDisplay IS NOT NULL THEN
                    sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || ' IS NULL';
                 ELSE
                    sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || ' IS NOT NULL';
                 END IF;

                 IF (colCollection(i).colSort != 'null') THEN
                    sqlUpdate := sqlUpdate || ', ' || colCollection(i).columnCd || '_SORT = q''@' || colSort || '@''';
                    sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_SORT != q''@' || colSort || '@''';
                    IF colSort = '' OR colSort IS NULL THEN
                       sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_SORT IS NOT NULL ';
                    ELSE
                       sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_SORT IS NULL ';
                    END IF;
                 END IF;

                 IF (colCollection(i).colBgColor != 'null')   THEN
                    sqlUpdate := sqlUpdate || ', ' || colCollection(i).columnCd || '_BG_COLOR = q''@' || colBgColor || '@''';
                    sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_BG_COLOR != q''@' || colBgColor || '@''';
                    IF colBgColor = '' OR colBgColor IS NULL THEN
                       sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_BG_COLOR IS NOT NULL ';
                    ELSE
                       sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_BG_COLOR IS NULL ';
                    END IF;
                 END IF;

                 IF (colCollection(i).colTextColor != 'null')  THEN
                    sqlUpdate := sqlUpdate || ', ' || colCollection(i).columnCd || '_TEXT_COLOR = q''@' || colTextColor || '@''';
                    sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_TEXT_COLOR != q''@' || colTextColor || '@''';
                    IF colTextColor = '' OR colTextColor IS NULL THEN
                       sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_TEXT_COLOR IS NOT NULL ';
                    ELSE
                       sqlWhere := sqlWhere || ' OR ' || colCollection(i).columnCd || '_TEXT_COLOR IS NULL ';
                    END IF;
                 END IF;
             END LOOP;

             sqlUpdate:= RTRIM(sqlUpdate, ',') || ' WHERE inv_no_db_id = ' || invNoDbId || ' AND inv_no_id = ' || invNoId || ' AND (' || sqlWhere || ')';
             EXECUTE IMMEDIATE sqlUpdate;
           ELSE
               EXIT;
           END IF;
      END LOOP;

      dbms_sql.close_cursor(queryCursor);
    END IF;

  --Update the last refresh end for the query
  EXECUTE IMMEDIATE 'UPDATE sb_query SET last_refresh_end = SYSDATE WHERE sb_query.status_board_db_id =' || aStatusBoardDbId || ' AND sb_query.status_board_id = ' || aStatusBoardId || ' AND sb_query.query_id = ' || aQueryId;

  an_Return := 1;

EXCEPTION
WHEN OTHERS THEN
   IF dbms_sql.is_open(queryCursor) THEN
      dbms_sql.close_cursor(queryCursor);
   END IF;
   an_Return := -1;
   APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', '@@@SBQueryRefresh@@@' || SQLERRM);
END;
/