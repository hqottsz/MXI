package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.ComponentWorkPackage;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.CarrierBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;


/**
 * Tests the OpenChecksByInventory query.
 */
public final class OpenChecksByInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ACFT_ASSEMBLY_CODE = "ACFT_ASY";
   private static final String ACFT_CONFIG_SLOT_CODE = "ACFT_CS";
   private static final String ACFT_PART_GROUP_CODE = "ACFT_PG";
   private static final String TRK_CONFIG_SLOT_CODE = "TRK_CS";
   private static final String TRK_PART_GROUP_CODE = "TRK_PG";
   private static final String TRK_ASSEMBLY_CODE = "TRK_AC";

   private AssemblyKey iAcftAssembly;
   private ConfigSlotKey iAcftConfigSlot;
   private ConfigSlotPositionKey iAcftConfigSlotPos;
   private CarrierKey iOperator;
   private ConfigSlotKey iTrkConfigSlot;
   private InventoryKey iAircraft;


   @Test
   public void execute_showAircraftAndComponentWorkpackages() throws Exception {
      assertEquals( 2, execute( iAircraft, false ) );
   }


   @Test
   public void execute_showAircraftWorkpackages_hideComponentWorkpacakges() throws Exception {
      assertEquals( 1, execute( iAircraft, true ) );
   }


   @Before
   public void dataSetUp() throws Exception {

      iOperator = new CarrierBuilder().build();
      iAcftAssembly = new AssemblyBuilder( ACFT_ASSEMBLY_CODE ).build();
      iAcftConfigSlot = new ConfigSlotBuilder( ACFT_CONFIG_SLOT_CODE )
            .withClass( RefBOMClassKey.ROOT ).withRootAssembly( iAcftAssembly ).build();
      iAcftConfigSlotPos = new ConfigSlotPositionKey( iAcftConfigSlot, 1 );

      PartGroupKey lAcftPartGroup = new PartGroupDomainBuilder( ACFT_PART_GROUP_CODE )
            .withConfigSlot( iAcftConfigSlot ).build();

      iAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT ).withOperator( iOperator )
            .withConfigSlotPosition( iAcftConfigSlotPos ).withPartGroup( lAcftPartGroup )
            .withOriginalAssembly( iAcftAssembly ).withSerialNo( "testAC" ).build();

      AssemblyKey lTrkAssembly = new AssemblyBuilder( TRK_ASSEMBLY_CODE ).build();

      // Config slot under the aircraft config slot.
      iTrkConfigSlot = new ConfigSlotBuilder( TRK_CONFIG_SLOT_CODE ).withClass( RefBOMClassKey.TRK )
            .withRootAssembly( iAcftAssembly ).build();

      ConfigSlotPositionKey lTrkConfigSlotPos =
            new ConfigSlotPositionBuilder().withConfigSlot( iTrkConfigSlot ).build();

      PartNoKey lTrkPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();
      PartGroupKey lTrkPartGroup =
            new PartGroupDomainBuilder( TRK_PART_GROUP_CODE ).withConfigSlot( iTrkConfigSlot )
                  .withInventoryClass( RefInvClassKey.TRK ).withPartNo( lTrkPartNo ).build();

      // Create the TRK component under the aircraft.
      final InventoryKey subInventory =
            new InventoryBuilder().withClass( RefInvClassKey.TRK ).withOperator( iOperator )
                  .withConfigSlotPosition( lTrkConfigSlotPos ).withPartGroup( lTrkPartGroup )
                  .withPartNo( lTrkPartNo ).withOriginalAssembly( lTrkAssembly )
                  .withAssemblyInventory( iAircraft ).withParentInventory( iAircraft ).build();

      Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

         @Override
         public void configure( ComponentWorkPackage aBuilder ) {
            aBuilder.setInventory( subInventory );
            aBuilder.setStatus( RefEventStatusKey.ACTV );
         }

      } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setAircraft( iAircraft );
            aBuilder.setStatus( RefEventStatusKey.ACTV );
         }

      } );

   }


   private int execute( InventoryKey aAircraft, boolean aHideComponentChecks ) throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, "aInvNoDbId", "aInvNoId" );
      lArgs.add( "aDayCount", -1 ); // no date limit
      lArgs.add( "aHideComponentChecks", aHideComponentChecks ); // Hide component Checks

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs ).getRowCount();
   }

}
