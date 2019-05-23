package com.mxi.am.domain.reader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.table.sched.SchedStaskDao;


/**
 * Reader for retrieving Labour Requirement information.
 *
 */
public class LabourRequirementReader {

   public static SchedLabourKey read( TaskKey task, RefLabourSkillKey skill,
         BigDecimal scheduledHours ) {

      if ( task == null || skill == null || scheduledHours == null ) {
         throw new RuntimeException( "task, skill, and scheduledHours must all be provided" );
      }

      // Retrieve all labours matching the task and skill,
      DataSetArgument args = new DataSetArgument();
      args.add( task, "sched_db_id", "sched_id" );
      args.add( skill, "labour_skill_db_id", "labour_skill_cd" );
      QuerySet schedLabourQs =
            QuerySetFactory.getInstance().executeQueryTable( "sched_labour", args );

      while ( schedLabourQs.next() ) {
         SchedLabourKey schedLabour =
               schedLabourQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

         args.clear();
         args.add( schedLabour, "labour_db_id", "labour_id" );
         // Use hours to distinct between skill, if hour matching then return first.
         QuerySet qs1 =
               QuerySetFactory.getInstance().executeQueryTable( "sched_labour_role", args );

         while ( qs1.next() ) {
            if ( scheduledHours.equals( qs1.getBigDecimal( "sched_hr" ) ) ) {
               return schedLabour;
            } ;

         }
      }

      return null;
   }


   public static List<SchedLabourKey> read( TaskKey task ) {

      List<SchedLabourKey> labourKeys = new ArrayList<>();

      DataSetArgument args = new DataSetArgument();
      args.add( task, SchedStaskDao.ColumnName.SCHED_DB_ID.toString(),
            SchedStaskDao.ColumnName.SCHED_ID.toString() );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( SchedLabourTable.TABLE, args );

      while ( qs.next() ) {

         labourKeys.add(
               qs.getKey( SchedLabourKey.class, SchedLabourTable.ColumnName.LABOUR_DB_ID.toString(),
                     SchedLabourTable.ColumnName.LABOUR_ID.toString() ) );

      }
      return labourKeys;

   }
}
