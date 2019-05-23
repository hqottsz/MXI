--liquibase formatted sql


--changeSet pbdbms_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace package pbdbms as
  procedure disable;
  procedure put(a varchar2);
  procedure put(a number);
  procedure put(a date);
  procedure put_line(a varchar2);
  procedure put_line(a number);
  procedure put_line(a date);
  procedure new_line;
  procedure get_line(line out varchar2, status out integer);
  type chararr is table of varchar2(255) index by binary_integer;
  procedure get_lines(lines out chararr, numlines in out integer);
end;
/