--liquibase formatted sql


--changeSet SCHED_STASK_PKG_ADDASSMMNTS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_ADDASSMMNTS(
    an_TaskDbId           IN sched_stask.task_db_id%TYPE,     
    an_TaskId             IN sched_stask.task_id%TYPE,     
    an_DataTypeDbId       IN mim_data_type.data_type_db_id%TYPE,     
    an_DataTypeId         IN mim_data_type.data_type_id%TYPE,   
    an_RecParmQty         IN inv_parm_data.rec_parm_qt% TYPE,
    on_Return             OUT NUMBER
) IS
begin

     SCHED_STASK_PKG.ADDASSEMBLYMEASUREMENTS(
         an_TaskDbId,
         an_TaskId,
         an_DataTypeDbId,
         an_DataTypeId,
         an_RecParmQty,
         on_Return
     );

end SCHED_STASK_PKG_ADDASSMMNTS;
/