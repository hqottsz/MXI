package com.mxi.am.domain.builder;

import org.apache.commons.lang.StringUtils;

import com.mxi.am.domain.TaskSubClass;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.table.ref.RefTaskClass;


public class TaskSubClassBuilder {

   public static RefTaskSubclassKey build( TaskSubClass taskSubClass ) {

      DataSetArgument args = taskSubClass.getId().getPKWhereArg();
      if ( !StringUtils.isEmpty( taskSubClass.getName() ) ) {
         args.add( "desc_sdesc", taskSubClass.getName() );
      }

      if ( taskSubClass.getTaskClassKey() != null ) {
         args.add( taskSubClass.getTaskClassKey(),
               RefTaskClass.ColumnName.TASK_CLASS_DB_ID.toString(),
               RefTaskClass.ColumnName.TASK_CLASS_CD.toString() );
      }
      MxDataAccess.getInstance().executeInsert( "ref_task_subclass", args );
      return taskSubClass.getId();
   }
}
