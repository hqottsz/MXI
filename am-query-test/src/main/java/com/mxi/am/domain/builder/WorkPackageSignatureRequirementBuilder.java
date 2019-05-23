
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.sched.JdbcSchedWPSignReqDao;
import com.mxi.mx.core.table.sched.SchedWPSignReqDao;
import com.mxi.mx.core.table.sched.SchedWPSignReqTable;


/**
 * This is a builder for work package signature requirements.
 */
public class WorkPackageSignatureRequirementBuilder implements DomainBuilder<SchedWPSignReqKey> {

   private RefLabourSkillKey iLabourSkill;
   private TaskKey iTaskKey;
   private SchedWPSignReqDao iSchedWPSignReqDao = new JdbcSchedWPSignReqDao();


   /**
    * {@inheritDoc}
    */
   @Override
   public SchedWPSignReqKey build() {

      SchedWPSignReqTable lWPSignReq = iSchedWPSignReqDao.create();
      lWPSignReq.setLabourSkill( iLabourSkill );
      if ( iTaskKey != null ) {
         lWPSignReq.setTask( iTaskKey );
      }
      return iSchedWPSignReqDao.insert( lWPSignReq );
   }


   /**
    * Sets the labour skill
    *
    * @param aLabourSkill
    *           The labour skill
    *
    * @return The builder
    */
   public WorkPackageSignatureRequirementBuilder withLabourSkill( RefLabourSkillKey aLabourSkill ) {
      iLabourSkill = aLabourSkill;

      return this;
   }


   public WorkPackageSignatureRequirementBuilder withWorkPackage( TaskKey aTaskKey ) {
      iTaskKey = aTaskKey;
      return this;
   }
}
