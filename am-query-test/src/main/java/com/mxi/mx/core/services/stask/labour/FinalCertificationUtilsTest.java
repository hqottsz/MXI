package com.mxi.mx.core.services.stask.labour;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Requirement;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.api.resource.maintenance.exec.task.dao.TaskDao;
import com.mxi.mx.core.exception.KeyConversionException;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.utils.KeyConversionUtilities;
import com.mxi.mx.domain.Id;


public class FinalCertificationUtilsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests
    * {@link com.mxi.mx.core.services.stask.labour.FinalCertificationUtils#getUncertificatedLabors(TaskKey)}
    * can get the incompleted and require certification but is not yet certified labour for a
    * completed task.
    *
    * @throws KeyConversionException
    *            if the labour alt_id cannot convert to be the SchedLabourKey
    */
   @Test
   public void getUncertificatedLabors_onlyOneLabourForCompletedTask()
         throws KeyConversionException {
      // DATA SETUP: one certification required incompleted labour under a completed task(the labour
      // had been auto-certified and edited work capture, now is waiting for certifying again)
      TaskKey lTask = Domain.createRequirement( ( Requirement aRequirement ) -> {
         aRequirement.setDefinition( Domain.createRequirementDefinition() );
         aRequirement.addLabour( aLabour -> {
            aLabour.setSkill( RefLabourSkillKey.PILOT );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
            aLabour.setCertifierRole(
                  certifierRole -> certifierRole.setScheduledHours( new BigDecimal( 3 ) ) );
         } );
      } );
      Set<SchedLabourKey> lLabourKeys = getLaboursByTask( lTask );
      assertEquals( "The only one labour is not under this task", 1, lLabourKeys.size() );
      setActualEndDateForLabours( lLabourKeys );

      // ACTION
      List<SchedLabourKey> lUncertificatedLabors =
            FinalCertificationUtils.getUncertificatedLabors( lTask );

      // ASSERTION
      assertEquals( 1, lUncertificatedLabors.size() );
   }


   /**
    * Tests
    * {@link com.mxi.mx.core.services.stask.labour.FinalCertificationUtils#getUncertificatedLabors(TaskKey)}
    * can get the incompleted and require certification but are not yet certified labours for a
    * completed task.
    *
    * @throws KeyConversionException
    *            if the labour alt_id cannot convert to be the SchedLabourKey
    */
   @Test
   public void getUncertificatedLabors_moreThanOneLaboursForCompletedTask()
         throws KeyConversionException {
      // DATA SETUP: two certification required incompleted labours under a completed task(the
      // labours had been auto-certified and edited work capture, now are waiting for certifying
      // again)
      TaskKey lTask = Domain.createRequirement( ( Requirement aRequirement ) -> {
         aRequirement.setDefinition( Domain.createRequirementDefinition() );
         aRequirement.addLabour( aCertificationRequiredLabourA -> {
            aCertificationRequiredLabourA.setSkill( RefLabourSkillKey.PILOT );
            aCertificationRequiredLabourA.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
            aCertificationRequiredLabourA
                  .setCertifierRole( certifierRole -> certifierRole.setScheduledHours( 3 ) );
         } );
         aRequirement.addLabour( aCertificationRequiredLabourB -> {
            aCertificationRequiredLabourB.setSkill( RefLabourSkillKey.ENG );
            aCertificationRequiredLabourB.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
            aCertificationRequiredLabourB
                  .setCertifierRole( certifierRole -> certifierRole.setScheduledHours( 3 ) );
         } );
      } );
      Set<SchedLabourKey> lLabourKeys = getLaboursByTask( lTask );
      assertEquals( "There have no two labours under the task", 2, lLabourKeys.size() );
      setActualEndDateForLabours( lLabourKeys );

      // ACTION
      List<SchedLabourKey> lUncertificatedLabors =
            FinalCertificationUtils.getUncertificatedLabors( lTask );

      // ASSERTION
      assertEquals( 2, lUncertificatedLabors.size() );
   }


   /**
    * When complete a task, the labours under it will be automated completed with an actual end date
    * in sched_labour_role table, @see
    * com.mxi.mx.core.services.stask.complete.CompleteService#completeBatch(com.mxi.mx.core.key.
    * HumanResourceKey, Date, boolean) Line#1064: <br>
    * LabourFacade.complete( iTask, aCompleteTasksDate );
    *
    * @param aLabourKeys
    *           the labour keys
    */
   private void setActualEndDateForLabours( Set<SchedLabourKey> aLabourKeys ) {
      aLabourKeys.stream().map( lLabourKey -> SchedLabourRoleTable.findByForeignKey( lLabourKey,
            RefLabourRoleTypeKey.CERT ) ).forEach( lCERTRoleTable -> {
               lCERTRoleTable.setActualEndDate( new Date() );
               lCERTRoleTable.update();
            } );
   }


   private Set<SchedLabourKey> getLaboursByTask( TaskKey aTask ) throws KeyConversionException {
      SchedStaskDao lSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      SchedStaskTable lTaskTable = lSchedStaskDao.findByPrimaryKey( aTask );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aTaskId", lTaskTable.getAlternateKey() );

      QuerySet lQueryResult = new TaskDao().executeFindLaboursByTaskIdQuery( lArgs );

      if ( !lQueryResult.hasNext() ) {
         throw new MxRuntimeException( "Cannot find labours for this task " + aTask );
      }
      Set<SchedLabourKey> lLabourKeys = new HashSet<>();
      while ( lQueryResult.next() ) {
         lLabourKeys.add( KeyConversionUtilities.idToKey(
               new Id<SchedLabourKey>( lQueryResult.getUuid( "labour_id" ) ),
               SchedLabourKey.class ) );
      }
      return lLabourKeys;
   }
}
