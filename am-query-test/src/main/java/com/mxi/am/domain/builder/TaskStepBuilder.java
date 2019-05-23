package com.mxi.am.domain.builder;

import java.util.ArrayList;
import java.util.Collection;

import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskStepSkillDao;
import com.mxi.mx.core.table.task.TaskStepSkillTable;
import com.mxi.mx.core.table.task.TaskStepTable;


/**
 * Domain builder for task step
 *
 */
public class TaskStepBuilder implements DomainBuilder<TaskStepKey> {

   private String iDescription;
   private String iApplicability;
   private TaskTaskKey iTaskTaskKey;

   private Collection<StepSkill> iStepSkillList = new ArrayList<StepSkill>();

   private TaskStepSkillDao iTaskStepSkillDao = InjectorContainer.get().getInstance(
         TaskStepSkillDao.class );


   public void withApplicability( String aApplicability ) {
      iApplicability = aApplicability;
   }


   public TaskStepBuilder withDescription( String aDescription ) {
      iDescription = aDescription;

      return this;
   }


   public TaskStepBuilder withStepSkill( RefLabourSkillKey aLabourSkill, boolean aInspReq ) {
      iStepSkillList.add( new StepSkill( aLabourSkill, aInspReq ) );

      return this;
   }


   public TaskStepBuilder withTaskTask( TaskTaskKey aTaskTask ) {
      iTaskTaskKey = aTaskTask;

      return this;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public TaskStepKey build() {

      if ( iTaskTaskKey == null ) {
         throw new RuntimeException( "Task Definition key is mandatory" );
      }

      TaskStepKey lTaskStepKey = TaskStepTable.generatePrimaryKey( iTaskTaskKey );
      TaskStepTable lTaskStepTable = TaskStepTable.create( lTaskStepKey );
      lTaskStepTable.setApplRangeLdesc( iApplicability );
      lTaskStepTable.setStepLdesc( iDescription );
      lTaskStepTable.insert();

      if ( !iStepSkillList.isEmpty() ) {

         for ( StepSkill lStepSkill : iStepSkillList ) {
            TaskStepSkillKey lKey = new TaskStepSkillKey( lTaskStepKey, lStepSkill.iLabourSkill );

            TaskStepSkillTable lTable = iTaskStepSkillDao.create( lKey );
            lTable.setInsp( lStepSkill.iInspReq );
            iTaskStepSkillDao.insert( lTable );
         }
      }

      return lTaskStepKey;
   }


   public class StepSkill {

      private boolean iInspReq;

      private RefLabourSkillKey iLabourSkill;


      public StepSkill(RefLabourSkillKey aLabourSkill, boolean aInspReq) {

         iLabourSkill = aLabourSkill;
         iInspReq = aInspReq;
      }

   }

}
