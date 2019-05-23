package com.mxi.mx.web.query.report;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefEventStatusKey.IN_WORK;
import static com.mxi.mx.core.key.RefTaskDepActionKey.COMPLIES;
import static com.mxi.mx.core.key.RefTaskDepActionKey.CRT;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RecurringSchedulingRule;
import com.mxi.am.domain.ReferenceDocumentDefinition;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class GetLastDoneTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private TaskTaskKey iReferenceDocumentDef;
   private TaskTaskKey iFollowingReqDefn;
   private TaskTaskKey iLeadReqDefn;
   private RecurringSchedulingRule iSchedulingRule;

   private InventoryKey iAircraft;
   private TaskKey iLeadReq;
   private TaskKey iFollowingReq;


   @Before
   public void setupData() throws Exception {
      // Create a reference document definition
      iReferenceDocumentDef = Domain.createReferenceDocumentDefinition(
            new DomainConfiguration<ReferenceDocumentDefinition>() {

               @Override
               public void configure( ReferenceDocumentDefinition aRefDocDefinition ) {
                  aRefDocDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }

            } );

      // Create a recurring requirement definition
      iSchedulingRule = new RecurringSchedulingRule();
      iSchedulingRule.setInterval( 90 );
      iSchedulingRule.setUsageParameter( CDY );

      iFollowingReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addRecurringSchedulingRule( iSchedulingRule );
                  aReqDefinition.setScheduledFromManufacturedDate();
                  aReqDefinition.setRecurring( true );
               }
            } );

      // Create a non-recurring requirement definition
      iLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addLinkedTaskDefinition( COMPLIES, iReferenceDocumentDef );
                  aReqDefinition.addSchedulingRule( CDY, new BigDecimal( 30 ) );
                  aReqDefinition.addFollowingTaskDefinition( CRT, iFollowingReqDefn );
                  aReqDefinition.setScheduledFromManufacturedDate();
                  aReqDefinition.setRecurring( false );
               }
            } );
   }


   @Test
   public void testLastDoneTaskIsCorrectlyReturned() throws Exception {

      // Create actual tasks against an aircraft based on those definitions and set these tasks as
      // complete.
      iAircraft = Domain.createAircraft();

      iLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( iAircraft );
            aRequirement.setDefinition( iLeadReqDefn );
            aRequirement.setStatus( RefEventStatusKey.COMPLETE );
         }
      } );

      iFollowingReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( iAircraft );
            aRequirement.setDefinition( iFollowingReqDefn );
            aRequirement.setStatus( RefEventStatusKey.COMPLETE );
         }
      } );

      // Given an in-work work package against the aircraft with the lead task.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( iAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( iLeadReq );
         }
      } );

      // Given an in-work work package against the aircraft with the following task.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( iAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( iFollowingReq );
         }
      } );

      // Get the last done task
      DataSetArgument lArgs = new DataSetArgument();
      QuerySet lDataSet = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.report.GetLastDoneTask", lArgs );

      // Assertion
      assertEquals( 1, lDataSet.getRowCount() );

      if ( lDataSet.first() ) {
         RefTaskDepActionKey lStatusKey =
               new RefTaskDepActionKey( lDataSet.getString( "eventstatuskey" ) );
         TaskKey lLastDoneTaskKey = new TaskKey( lDataSet.getString( "last_task_key" ) );

         assertEquals( RefTaskDepActionKey.COMPLETE, lStatusKey );
         assertEquals( lLastDoneTaskKey, iLeadReq );
      }
   }
}
