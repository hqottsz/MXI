--liquibase formatted sql


--changeSet SCHED_STASK_PKG_UPDTPARTSTOOLS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_UPDTPARTSTOOLS(
    	     an_TaskDbId           IN sched_stask.task_db_id%TYPE,
             an_TaskId             IN sched_stask.task_id%TYPE,
             ab_PartReady          OUT INTEGER,
             ab_ToolReady          OUT INTEGER,
             on_Return             OUT INTEGER
        ) IS
     BEGIN
     
     
       SCHED_STASK_PKG.UPDATEPARTSANDTOOLSREADYBOOL(
                an_TaskDbId,
                an_TaskId,
                ab_PartReady,
           	ab_ToolReady,
                on_Return);
                
END SCHED_STASK_PKG_UPDTPARTSTOOLS;
/