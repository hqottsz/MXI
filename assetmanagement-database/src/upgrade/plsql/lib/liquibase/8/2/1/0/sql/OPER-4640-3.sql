--liquibase formatted sql


--changeSet OPER-4640-3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
declare

       cursor cs_mvidx (iMviewName in varchar2) is
       select *
       from all_indexes ai
       where exists  (select mview_name
               from all_mviews  am
               where am.owner = ai.owner
               and   am.mview_name = ai.table_name
               and   am.mview_name = iMviewName )
        and ai.owner = user
        ;
        
        lt_cs_mvidx_buf               cs_mvidx%rowtype;
begin

        open  cs_mvidx ('MV_MATERIALS_REQUEST_STATUS');
       loop
               fetch cs_mvidx into lt_cs_mvidx_buf;
               exit when cs_mvidx%notfound;
               
               begin
                          execute immediate 'alter index '||lt_cs_mvidx_buf.owner||'.'||lt_cs_mvidx_buf.index_name||' rebuild online tablespace '||lt_cs_mvidx_buf.tablespace_name||' compute statistics ';
               exception
                          when others then
                                  null;
               end;
        end loop;
        close cs_mvidx;

        return ;

exception
        when others then
                null;

end;
/