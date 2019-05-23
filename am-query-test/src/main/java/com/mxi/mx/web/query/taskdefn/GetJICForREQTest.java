package com.mxi.mx.web.query.taskdefn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This is a unit test to test GetJICForREQ.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetJICForREQTest {

   private static final Integer REVISION_1 = 1;
   private static final Integer REVISION_2 = 2;
   private static final Integer REVISION_3 = 3;

   private static InventoryKey iAircraftKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void itReturnsLatestJicRevisionOfReq() {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );
      final ConfigSlotKey lAcftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Create a JIC1 with build status, to be added to REQ
      final TaskTaskKey lJic1Rev1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC1" );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      // Create a JIC2 with revision status, to be added to REQ
      final TaskTaskKey lJic2Rev1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC2" );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );
      final TaskTaskKey lJic2Rev2 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC2" );
                  aJicDefn.setPreviousRevision( lJic2Rev1 );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_2 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      // Create a JIC3 with revision status, to be removed from REQ
      final TaskTaskKey lJic3Rev1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC3" );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );
      final TaskTaskKey lJic3Rev2 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC3" );
                  aJicDefn.setPreviousRevision( lJic3Rev1 );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_2 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      // Create a JIC4 with revision status, to be removed from REQ
      final TaskTaskKey lJic4Rev1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC4" );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );
      final TaskTaskKey lJic4Rev2 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC4" );
                  aJicDefn.setPreviousRevision( lJic4Rev1 );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_2 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      final TaskTaskKey lJic4Rev3 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC4" );
                  aJicDefn.setPreviousRevision( lJic4Rev2 );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_3 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      // Create a JIC5 with revision status, included in REQ
      final TaskTaskKey lJic5Rev1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC5" );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      // Create a JIC6 with revision status, still included in REQ
      final TaskTaskKey lJic6Rev1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC6" );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );
      final TaskTaskKey lJic6Rev2 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setCode( "JIC6" );
                  aJicDefn.setPreviousRevision( lJic6Rev1 );
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_2 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAcftRootConfigSlot );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );

                  aReqDefn.addJobCardDefinition( lJic1Rev1 );
                  aReqDefn.addJobCardDefinition( lJic2Rev2 );
                  aReqDefn.addJobCardDefinition( lJic3Rev1 );
                  aReqDefn.addJobCardDefinition( lJic4Rev1 );
                  aReqDefn.addJobCardDefinition( lJic4Rev2 );
                  aReqDefn.addJobCardDefinition( lJic5Rev1 );
                  aReqDefn.addJobCardDefinition( lJic6Rev1 );
                  aReqDefn.addJobCardDefinition( lJic6Rev2 );
               }
            } );

      int lCountToBeAdded = 0;
      int lCountToBeRemoved = 0;

      DataSet lResultDs = execute( lReqDefn );

      Assert.assertEquals( "Missing expected records", 6, lResultDs.getRowCount() );

      while ( lResultDs.next() ) {
         String lJicCodeName = lResultDs.getString( "jic_cd_name" );
         if ( lJicCodeName.contains( "(To Be Added)" ) ) {
            lCountToBeAdded++;
         } else if ( lJicCodeName.contains( "(To Be Removed)" ) ) {
            lCountToBeRemoved++;
         } else if ( lJicCodeName.contains( "JIC6" ) ) {
            String lJicSubTaskKeyString = lResultDs.getString( "jic_subtask_key" );
            Assert.assertTrue( "Does not point to the latest revision",
                  lJicSubTaskKeyString.contains( lJic6Rev2.toString() ) );
         }

      }

      Assert.assertEquals( "Missing expected to be added records", 2, lCountToBeAdded );
      Assert.assertEquals( "Missing expected to be removed records", 2, lCountToBeRemoved );

   }


   private DataSet execute( TaskTaskKey aTask ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }


   @Before
   public void setup() {
   }

}
