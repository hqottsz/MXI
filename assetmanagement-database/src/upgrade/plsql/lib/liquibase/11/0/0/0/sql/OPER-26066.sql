--liquibase formatted sql

--changeSet OPER-26066:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "MX_KEY_PKG" IS

/********************************************************************************
*
* Function:       new_uuid
* Returns:        A new UUID value
*
* Description:    This function returns a new Type 1 UUID value using the
*                 java-uuid-generator library.  This is loaded into Oracle via
*                 the Type1UUIDGenerator.sql script.
*
*********************************************************************************/
FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE;
    
END MX_KEY_PKG;
/

--changeSet OPER-26066:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "MX_KEY_PKG" IS

  last_epoch number(38) := null;

  counter number(6) := 0;
  random number(38) := TRUNC(DBMS_RANDOM.value(low => 1, high => 1073741823));
  ip number(38) := null;

  -- --------------------------------------------------------------------------
  -- Inspiration for conversion functions from
  -- https://oracle-base.com/dba/miscellaneous/conversion_api.sql
  -- ----------------------------------------------------------------------------
  FUNCTION to_base(p_dec   IN  NUMBER,
                   p_base  IN  NUMBER) RETURN VARCHAR2 PARALLEL_ENABLE IS
    l_str VARCHAR2(255) DEFAULT NULL;
    l_num NUMBER        DEFAULT p_dec;
    l_hex VARCHAR2(16)  DEFAULT '0123456789ABCDEF';
  BEGIN
    IF p_dec is null OR (TRUNC(p_dec) <> p_dec OR p_dec < 0) THEN
      -- cannot raise errors in parallel ... so return null
      return null;
    END IF;
    
    LOOP
      l_str := SUBSTR(l_hex, MOD(l_num,p_base)+1, 1) || l_str;
      l_num := TRUNC(l_num/p_base);
      EXIT WHEN (l_num = 0);
    END LOOP;
    RETURN l_str;
  END to_base;

  FUNCTION to_hex(p_dec  IN  NUMBER) RETURN VARCHAR2 PARALLEL_ENABLE IS
  BEGIN
    RETURN to_base(p_dec, 16);
  END to_hex;


  FUNCTION to_dec (p_str        IN  VARCHAR2,
                   p_from_base  IN  NUMBER DEFAULT 16) RETURN NUMBER PARALLEL_ENABLE IS
    l_num   NUMBER       DEFAULT 0;
    l_hex   VARCHAR2(16) DEFAULT '0123456789ABCDEF';
  BEGIN
    if p_str is null then
      -- cannot raise errors in parallel ... so return null
      return null;
    end if;
    
    FOR i IN 1 .. LENGTH(p_str) LOOP
      l_num := l_num * p_from_base + INSTR(l_hex,UPPER(SUBSTR(p_str,i,1)))-1;
    END LOOP;
    RETURN l_num;
  END to_dec;
  -- ----------------------------------------------------------------------------

  /********************************************************************************
  *
  * Function:       get_ip
  *
  * Description:    This function looks up and caches the configured seed value.
  * This value is used in place of the MAC or IP address within the generation of Typw 1 GUIDs.
  *
  * Parallel was not enabled because it should only be called once per session
  *
  *********************************************************************************/
  function get_ip return number IS
  BEGIN
    
    if ip is null then
      begin
        select db_id into ip
        from mim_local_db;
      exception when no_data_found then
        ip := 0;
      end;
    end if;
    
    return ip;
  END;

  /********************************************************************************
  *
  * Function:       calcMostSigBits
  *
  * Description:    This function returns the most significant 8 bytes of a type 1 guid.
  *
  * Loose translation of time based uuid generation as orginally implemented by fasterxml.com
  * https://github.com/cowtowncoder/java-uuid-generator
  *********************************************************************************/
  FUNCTION calcMostSigBits(epoch number, l_counter number) RETURN VARCHAR2
    PARALLEL_ENABLE IS
    nibble       number(38) := null;

    hex_raw     varchar2(16) := null;

    ab varchar2(4) := null;
    cd varchar2(4) := null;
    efgh varchar2(8) := null;

  BEGIN
    -- offset to gregorian calendar, multiply to 100ns
    hex_raw := lpad(to_hex(epoch * 10000 + 122192928000000000 + l_counter),16,0);

    -- hex tuples of ABCDEFGH should be rearanged EFGHCDAB for indexing; A also needs to be altered to meet the UUID spec
    ab := substr(hex_raw, 0, 4);
    cd := substr(hex_raw, 5, 4);
    efgh := substr(hex_raw, 9, 8);

    -- remove high nibble of 6th byte
    -- ~0xF000 => -61441
    nibble := BitAnd(to_dec(ab), -61441);

    -- set uuid to type 1
    -- 0x1000 => 4096
    -- BitOr isn't consistently included so implement OR(X,Y) as X + Y - AND(X,Y)
    ab := to_hex(nibble + 4096 - BitAnd(nibble, 4096));

    return lpad(efgh || cd || ab,16,'0');
  END;

  /********************************************************************************
  *
  * Function:       calcLeastSigBits
  *
  * Description:    This function returns the most significant 8 bytes of a type 1 guid.
  *
  * Loose translation of time based uuid generation as orginally implemented by fasterxml.com
  * https://github.com/cowtowncoder/java-uuid-generator
  *********************************************************************************/
  FUNCTION calcLeastSigBits(l_random number) RETURN VARCHAR2
    PARALLEL_ENABLE IS

    leastSigBits number(38) := 0;

  BEGIN

    if ip is null then
      ip := get_ip();
    end if;
    
    if ip is not null then
      -- mask to 16 bytes 0xFFFFFFFF
      leastSigBits := BitAnd(ip,4294967295) + l_random*4294967296;

      -- remove 2 MSB
      -- mask is 0X7FFFFFFFFFFFFFFF
      leastSigBits := BitAnd(leastSigBits, 9223372036854775807);

      -- set 2 MSB to '10'
      -- '2L << 62' => mask is 0X8000000000000000
      leastSigBits := leastSigBits + 9223372036854775808;

      return lpad(to_hex(leastSigBits),16,'0');
    end if;
    
    -- make this safe if the system wasn't configured
    return null;
  END;


  /********************************************************************************
  *
  * Function:       resetRandom
  *
  * Description:    This function resets the cached random value. Cannot be parallel.
  *
  *********************************************************************************/
  PROCEDURE resetRandom IS
  BEGIN
    random := TRUNC(DBMS_RANDOM.value(low => 1, high => 1073741823));
  END;

  /********************************************************************************
  *
  * Function:       calcCounter
  *
  * Description:    Calculates the counter for the epoch. Cannot be parallel.
  *
  * Loose translation of time based uuid generation as orginally implemented by fasterxml.com
  * https://github.com/cowtowncoder/java-uuid-generator
  *********************************************************************************/
  PROCEDURE calcCounter(epoch number, l_counter out number, l_random out number)
    IS
  BEGIN
    -- SCN ticks only occur every 3ms so safe to raise the counter to 16k
    if last_epoch <= epoch then
      if counter < (16384 - 1) then
        counter := counter + 1;
      else
        -- this is meant to be a seed ... so global to the db but only has so many 'counter' tokens it can use
        -- should last until there is a collision within the timestamp
        -- altered to use 7bytes + 6 bits 0x3FFFFFFF
        resetRandom;
        counter := 0;
      end if;
    else
      last_epoch := epoch;
      counter := 0;
    end if;

    l_counter := counter;
    l_random := random;
  END;

  /**
   * creates a new uuid while exposing internal steps for debugging
   */
  PROCEDURE new_uuid(l_epoch OUT NUMBER, l_random OUT NUMBER, l_counter OUT NUMBER, 
    mostSigBits OUT VARCHAR2, leastSigBits OUT VARCHAR2, uuid OUT RAW)
    PARALLEL_ENABLE IS 
    l_time timestamp;
  BEGIN
    
    -- we need to calculate the seconds with milliseconds to avoid roll over between the evaluation of sysdate and systimestamp
    l_time := systimestamp;
    l_epoch := (trunc(l_time) - to_date('1970-01-01', 'YYYY-MM-DD')) * (24 * 60 * 60 * 1000) + to_number(to_char(l_time, 'SSSSSFF3'));

    -- need to pre-calculate counter and keep random key to avoid timing issues creating duplicates
    calcCounter(l_epoch, l_counter, l_random);

    mostSigBits := calcMostSigBits(l_epoch, l_counter);
    leastSigBits := calcLeastSigBits(l_random);
    
    -- safety related to seed configuration
    if leastSigBits is not null then
      uuid := hextoraw(mostSigBits || leastSigBits);
    end if;
  END;
  
  /**
   * Public version to create a UUID
   */
  FUNCTION new_uuid RETURN RAW
    PARALLEL_ENABLE IS
    l_epoch       number(38) := null;
    l_random      number(38);
    l_counter     number(6);

    mostSigBits     varchar2(16) := null;
    leastSigBits    varchar2(16) := null;
    uuid            RAW(16) := null;
  BEGIN
    new_uuid(l_epoch,
             l_random,
             l_counter,
             mostSigBits,
             leastSigBits,
             uuid);
    return uuid;
  END;

END MX_KEY_PKG;
/