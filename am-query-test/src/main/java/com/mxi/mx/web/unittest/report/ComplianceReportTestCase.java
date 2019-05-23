
package com.mxi.mx.web.unittest.report;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.ref.RefTaskClass;
import com.mxi.mx.web.report.compliance.ComplianceReport;


/**
 * Base test for {@link ComplianceReport}.
 */
public abstract class ComplianceReportTestCase {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public final FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   protected static final RefTaskClassKey REF = new RefTaskClassKey( 10, "REF" );

   protected InventoryKey iAcftInventory;

   protected InventoryKey iAssyInventory_Pos_1;

   protected InventoryKey iAssyInventory_Pos_2;

   protected ConfigSlotKey iConfigSlot_ACFT;
   protected ConfigSlotPositionKey iConfigSlot_ACFT_Pos_1;

   protected ConfigSlotKey iConfigSlot_ASSY;
   protected ConfigSlotPositionKey iConfigSlot_ASSY_Pos_1;
   protected ConfigSlotKey iConfigSlot_SUBASSY;
   protected ConfigSlotPositionKey iConfigSlot_SUBASSY_Pos_1;

   protected ConfigSlotPositionKey iConfigSlot_SUBASSY_Pos_2;

   protected HumanResourceKey iHr;


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iHr = new HumanResourceDomainBuilder().build();

      // Create configuration slots
      AssemblyKey lAcft = new AssemblyBuilder( "ACFT" ).build();
      iConfigSlot_ACFT = new ConfigSlotBuilder( "ACFT" ).withRootAssembly( lAcft ).build();
      iConfigSlot_ACFT_Pos_1 =
            new ConfigSlotPositionBuilder().withConfigSlot( iConfigSlot_ACFT ).build();
      iConfigSlot_SUBASSY = new ConfigSlotBuilder( "SUBASSY" ).withRootAssembly( lAcft )
            .withClass( RefBOMClassKey.SUBASSY ).build();
      iConfigSlot_SUBASSY_Pos_1 =
            new ConfigSlotPositionBuilder().withConfigSlot( iConfigSlot_SUBASSY ).build();
      iConfigSlot_SUBASSY_Pos_2 =
            new ConfigSlotPositionBuilder().withConfigSlot( iConfigSlot_SUBASSY ).build();

      AssemblyKey lAssy = new AssemblyBuilder( "ASSY" ).build();
      iConfigSlot_ASSY = new ConfigSlotBuilder( "ASSY" ).withRootAssembly( lAssy ).build();
      iConfigSlot_ASSY_Pos_1 =
            new ConfigSlotPositionBuilder().withConfigSlot( iConfigSlot_ASSY ).build();

      // Create parts
      PartNoKey lAcftPart = new PartNoBuilder().build();
      PartNoKey lAssyPart = new PartNoBuilder().build();

      // Associate parts to configuration slots
      PartGroupKey lAcftPartGroup = new PartGroupDomainBuilder( "ACFT" ).withPartNo( lAcftPart )
            .withConfigSlot( iConfigSlot_ACFT ).build();
      PartGroupKey lSubassyPartGroup = new PartGroupDomainBuilder( "SUBASSY" )
            .withPartNo( lAssyPart ).withConfigSlot( iConfigSlot_SUBASSY ).build();
      new PartGroupDomainBuilder( "ASSY" ).withPartNo( lAssyPart )
            .withConfigSlot( iConfigSlot_ASSY ).build();

      // Create inventory
      iAcftInventory = new InventoryBuilder().withPartGroup( lAcftPartGroup )
            .withPartNo( lAcftPart ).withConfigSlotPosition( iConfigSlot_ACFT_Pos_1 )
            .withOriginalAssembly( lAcft ).build();
      iAssyInventory_Pos_1 =
            new InventoryBuilder().withPartGroup( lSubassyPartGroup ).withPartNo( lAssyPart )
                  .withClass( RefInvClassKey.ASSY ).withAssemblyInventory( iAcftInventory )
                  .withHighestInventory( iAcftInventory ).withParentInventory( iAcftInventory )
                  .withConfigSlotPosition( iConfigSlot_SUBASSY_Pos_1 ).withOriginalAssembly( lAssy )
                  .build();
      iAssyInventory_Pos_2 =
            new InventoryBuilder().withPartGroup( lSubassyPartGroup ).withPartNo( lAssyPart )
                  .withClass( RefInvClassKey.ASSY ).withAssemblyInventory( iAcftInventory )
                  .withHighestInventory( iAcftInventory ).withParentInventory( iAcftInventory )
                  .withConfigSlotPosition( iConfigSlot_SUBASSY_Pos_2 ).withOriginalAssembly( lAssy )
                  .build();

      RefTaskClass lRefTaskClass = RefTaskClass.create();
      lRefTaskClass.setClassModeCd( "REF" );
      lRefTaskClass.insert( REF );
   }


   /**
    * Asserts the report row
    *
    * @param aDataSet
    *           the dataset
    * @param aAssembly
    *           the inventory's assembly
    * @param aTaskRevision
    *           the task revision
    */
   protected void assertRow( DataSet aDataSet, InventoryKey aAssembly, TaskTaskKey aTaskRevision ) {
      Assert.assertTrue( aDataSet.next() );

      Assert.assertEquals( "inventory", aAssembly,
            aDataSet.getKey( InventoryKey.class, "inventory_key" ) );

      Assert.assertEquals( "task revision", aTaskRevision,
            aDataSet.getKey( TaskTaskKey.class, "task_key" ) );
   }


   /**
    * Asserts the report row
    *
    * @param aDataSet
    *           the dataset
    * @param aInventory
    *           the inventory
    * @param aTaskRevision
    *           the task revision
    * @param aLastTaskKey
    *           the last task
    * @param aNextTaskKey
    *           the next task
    */
   protected void assertRow( DataSet aDataSet, InventoryKey aInventory, TaskTaskKey aTaskRevision,
         TaskKey aLastTaskKey, TaskKey aNextTaskKey ) {
      assertRow( aDataSet, aInventory, aTaskRevision );

      Assert.assertEquals( "last task", aLastTaskKey,
            aDataSet.getKey( TaskKey.class, "last_task_key" ) );
      Assert.assertEquals( "next task", aNextTaskKey,
            aDataSet.getKey( TaskKey.class, "next_task_key" ) );
   }
}
