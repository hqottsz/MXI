--liquibase formatted sql


--changeSet MX-17637:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      CONCAT_DATA
* Arguments:     p_cursor (sys_refcursor) -cursor/select of data
*                p_delimiter (varchar2)   -optional: delimiter to use between the data
* Description:   This function takes a query and a delimiter and produces a string
*                that is the total concatenation of all of the data.  Note that
*                the delimiter appears between data and not at the first or end.
*                ex. select join(cursor(select 1 from dual), ':') from dual
* Version:       1.0
*
* Orig.Coder:    J. Cimino
* Recent Coder:  W. Yuke
* Recent Date:   March 18, 2005
*
*********************************************************************************
*
* Confidential, proprietary and/or trade secret information of Mxi Technologies,
*    Ltd. Copyright 2005 Mxi Technologies, Ltd.  All Rights Reserved.
* Except as expressly provided by written license signed by a duly appointed 
*   officer of Mxi Technologies, Ltd., any disclosure, distribution, reproduction, 
*   compilation, modification, creation of derivative works and/or other use of 
*   the Mxi source code is strictly prohibited. 
* Inclusion of a copyright notice shall not be taken to indicate that the source
*   code has been published.
*
*********************************************************************************/
create or replace function concat_data
(
    p_cursor    sys_refcursor,
    p_delimiter varchar2 := ', '
) return varchar2
is
    l_value   varchar2(32767);
    l_result  varchar2(32767);
begin
    /* loop through the list of data provided */
    loop
        fetch p_cursor into l_value;
        exit when p_cursor%notfound;
        
        /* if this is the first value, do not add a delimiter */
        if l_result is not null then
            l_result := l_result || p_delimiter;
        end if;
        
        /* Add the value to the result string */
        l_result := l_result || l_value;
    end loop;
    
    /* close the cursor and return the results */
    close p_cursor;
    return l_result;
end concat_data;
/

--changeSet MX-17637:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      CONCAT_DATA_CLOB
* Arguments:     p_cursor (sys_refcursor) -cursor/select of data
*                p_delimiter (varchar2)   -optional: delimiter to use between the data
* Description:   This function takes a query and a delimiter and produces a string
*                that is the total concatenation of all of the data.  Note that
*                the delimiter appears between data and not at the first or end.
*                ex. select join(cursor(select 1 from dual), ':') from dual
*
*********************************************************************************
*
* Confidential, proprietary and/or trade secret information of Mxi Technologies,
*    Ltd. Copyright 2010 Mxi Technologies, Ltd.  All Rights Reserved.
* Except as expressly provided by written license signed by a duly appointed 
*   officer of Mxi Technologies, Ltd., any disclosure, distribution, reproduction, 
*   compilation, modification, creation of derivative works and/or other use of 
*   the Mxi source code is strictly prohibited. 
* Inclusion of a copyright notice shall not be taken to indicate that the source
*   code has been published.
*
*********************************************************************************/
create or replace function concat_data_clob
(
    p_cursor    sys_refcursor,
    p_delimiter varchar2 := ', '
) return CLOB
is
    l_value   varchar2(32767);
    l_result  varchar2(32767);
begin
    /* loop through the list of data provided */
    loop
        fetch p_cursor into l_value;
        exit when p_cursor%notfound;
        
        /* if this is the first value, do not add a delimiter */
        if l_result is not null then
            l_result := l_result || p_delimiter;
        end if;
        
        /* Add the value to the result string */
        l_result := l_result || l_value;
    end loop;
    
    /* close the cursor and return the results */
    close p_cursor;
    return l_result;
end concat_data_clob;
/