package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefPartStatusKey;


/**
 * This class performs unit testing for findInventoriesByAlternativePartNoAndSerialNo query.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class FindInventoriesByAlternativePartNoAndSerialNoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String SERIAL_NO_OEM = "ABC";
   private static final ManufacturerKey MANUFACTURER = new ManufacturerKey( 0, "IFS" );

   private PartNoKey iReceivedPart;
   private PartNoKey iAlternatePart1;
   private PartNoKey iAlternatePart2;
   private PartNoKey iAlternatePart3;
   private PartNoKey iAlternatePart4;
   private PartNoKey iOtherPart;

   private DataSet iDs;


   /**
    * Tests that the query returns no archived inventory when the received part doesn't have an
    * alternate part
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testGivenReceivedPartHasNoAlterPartWhenReceiveThenNoArchivednvReturned()
         throws Exception {

      createInventory( iOtherPart, RefInvCondKey.ARCHIVE );

      execute();

      assertResult( 0 );
   }


   /**
    * Tests that the query returns one archived inventory when the received part has an alternate
    * part
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testGivenReceivedPartHasAlterPartWhenReceiveThenOneArchivednvReturned()
         throws Exception {

      createInventory( iAlternatePart1, RefInvCondKey.ARCHIVE );
      createInventory( iOtherPart, RefInvCondKey.ARCHIVE );

      execute();

      assertResult( 1 );
   }


   /**
    * Tests that the query returns multiple archived inventory when the received part has multiple
    * alternate parts
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void
         testGivenReceivedPartHasMultipleAlterPartsWhenReceiveThenMultipleArchivednvsReturned()
               throws Exception {

      createInventory( iAlternatePart1, RefInvCondKey.ARCHIVE );
      createInventory( iAlternatePart2, RefInvCondKey.ARCHIVE );
      createInventory( iOtherPart, RefInvCondKey.ARCHIVE );

      execute();

      assertResult( 2 );
   }


   /**
    * Tests that the query returns multiple archived and multiple active inventory records when the
    * received part has multiple alternate parts
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void
         testGivenReceivedPartHasMultipleAlterPartsWhenReceiveThenMultipleArchivedAndMultipleActivenvsReturned()
               throws Exception {

      createInventory( iAlternatePart1, RefInvCondKey.INSPREQ );
      createInventory( iAlternatePart2, RefInvCondKey.INSPREQ );
      createInventory( iAlternatePart3, RefInvCondKey.ARCHIVE );
      createInventory( iAlternatePart4, RefInvCondKey.ARCHIVE );
      createInventory( iOtherPart, RefInvCondKey.ARCHIVE );

      execute();

      assertResult( 4 );
   }


   @Before
   public void loadData() throws Exception {

      // create receive part group and three parts under the same part group
      PartGroupKey lReceivedPartGroup = new PartGroupDomainBuilder( "TESTGROUP1" ).build();
      iReceivedPart = createPart( "RCVD_PART", lReceivedPartGroup );
      iAlternatePart1 = createPart( "ALT_PART1", lReceivedPartGroup );
      iAlternatePart2 = createPart( "ALT_PART2", lReceivedPartGroup );
      iAlternatePart3 = createPart( "ALT_PART3", lReceivedPartGroup );
      iAlternatePart4 = createPart( "ALT_PART4", lReceivedPartGroup );

      // create another part group and another part
      PartGroupKey lOtherPartGroup = new PartGroupDomainBuilder( "TESTGROUP2" ).build();
      iOtherPart = createPart( "OTR_PART", lOtherPartGroup );

   }


   private PartNoKey createPart( String aPartNoOem, PartGroupKey aPartGroup ) {

      return new PartNoBuilder().withOemPartNo( aPartNoOem )
            .withInventoryClass( RefInvClassKey.SER ).manufacturedBy( MANUFACTURER )
            .withStatus( RefPartStatusKey.ACTV ).isAlternateIn( aPartGroup ).build();
   }


   private void createInventory( PartNoKey aPartNo, RefInvCondKey aInvCondKey ) {
      new InventoryBuilder().withClass( RefInvClassKey.SER ).withPartNo( aPartNo )
            .withSerialNo( SERIAL_NO_OEM ).withCondition( aInvCondKey ).build();
   }


   private void execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iReceivedPart, "aPartNoDbId", "aPartNoId" );
      lArgs.add( "aSerialNoOEM", SERIAL_NO_OEM );

      iDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.findInventoriesByAlternativePartNoAndSerialNo",
            lArgs );
   }


   private void assertResult( int aExpected ) {
      assertEquals( "Number of retrieved rows", aExpected, iDs.getRowCount() );
   }

}
