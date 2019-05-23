--liquibase formatted sql


--changeSet pbdbms_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace package body pbdbms as
  enabled         boolean        := TRUE;
  buf_size        binary_integer;
  tmpbuf          varchar2(500)  := '';
  putidx          binary_integer := 1;
  amtleft         binary_integer := 0;
  getidx          binary_integer := 2;
  getpos          binary_integer := 1;
  get_in_progress boolean := TRUE;
  type            char_arr is table of varchar2(512) index by binary_integer;
  buf             char_arr;
  idxlimit        binary_integer;
  procedure enable (buffer_size in integer default 20000) is
    lstatus integer;
    lockid  integer;
  begin
    enabled := TRUE;
    if buffer_size < 2000 then
      buf_size := 2000;
    elsif buffer_size > 1000000 then
      buf_size := 1000000;
    else
      buf_size := buffer_size;
    end if;
    idxlimit := trunc((buf_size+499) / 500);
  end;
  procedure disable is
  begin
    enabled := FALSE;
  end;
  procedure put(a varchar2) is
  begin
    if enabled then
      tmpbuf := tmpbuf || a;
    end if;
  end;
  procedure put(a number) is
  begin
    if enabled then
      tmpbuf := tmpbuf || to_char(a);
    end if;
  end;
  procedure put(a date) is
  begin
    if enabled then
      tmpbuf := tmpbuf || to_char(a);
    end if;
  end;
  procedure put_line(a varchar2) is
  begin
    if enabled then
      tmpbuf := tmpbuf || a;
      new_line;
    end if;
  end;
  procedure put_line(a number) is
  begin
    if enabled then
      tmpbuf := tmpbuf || to_char(a);
      new_line;
    end if;
  end;
  procedure put_line(a date) is
  begin
    if enabled then
      tmpbuf := tmpbuf || to_char(a);
      new_line;
    end if;
  end;
  procedure new_line is
    strlen  binary_integer;
  begin
    if enabled then
      if get_in_progress then
        get_in_progress := FALSE;
        putidx := 1;
        amtleft := 500;
        buf(putidx) := '';
      end if;
      strlen := lengthb(tmpbuf);
      if strlen > 255 then
        tmpbuf := '';
        raise_application_error(-20000, 'ORU-10028: line length overflow, ' ||
          'limit of 255 bytes per line');
      end if;
      if strlen > amtleft then
        if putidx >= idxlimit then
          tmpbuf := '';
          raise_application_error(-20000, 'ORU-10027: buffer overflow, ' ||
            'limit of ' || to_char(buf_size) || ' bytes');
        end if;
        buf(putidx) := buf(putidx) || '  -1';
        putidx := putidx + 1;
        amtleft := 500;
        buf(putidx) := '';
      end if;

      buf(putidx) := buf(putidx) || to_char(strlen,'999') || tmpbuf;
      amtleft := amtleft - strlen - 4;
      tmpbuf := '';
    end if;
  end;
  procedure get_line(line out varchar2, status out integer) is
    strlen   binary_integer;
  begin
    if not enabled then
      status := 1;
      return;
    end if;
    if not get_in_progress then
      buf(putidx) := buf(putidx) || '  -1';
      putidx := putidx + 1;
      get_in_progress := TRUE;
      getidx := 1;
      getpos := 1;
      tmpbuf := '';
    end if;
    while getidx < putidx loop
      strlen := to_number(substrb(buf(getidx),getpos,4));
      if strlen >= 0 then
        line := substrb(buf(getidx), getpos+4, strlen);
        getpos := getpos + strlen + 4;
        status := 0;
        return;
      else
        getidx := getidx + 1;
        getpos := 1;
      end if;
    end loop;
    status := 1;
    return;
  end;
  procedure get_lines(lines out chararr, numlines in out integer) is
    linecnt integer := 1;
    s       integer;
  begin
    if not enabled then
      numlines := 0;
      return;
    end if;
    while linecnt <= numlines loop
      get_line(lines(linecnt), s);
      if s = 1 then
        numlines := linecnt - 1;
        return;
      end if;
      linecnt := linecnt + 1;
    end loop;
    numlines := linecnt - 1;
    return;
  end;
end;
/