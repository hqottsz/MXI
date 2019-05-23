package com.mxi.mx.web.services.inventory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;


public class AssociatedInventoryServiceTest {

   private static final Integer ASSOCIATION_ID = 1;

   private static final String[] EXPECTED_COLUMNS = { "INV_KEY", "PART_NO_KEY", "PART_NO_OEM",
         "PART_NO_SDESC", "SERIAL_NO_OEM", "RECEIVED_DT", "KIT_COMPLETE_BOOL", "REF_INV_COND_KEY",
         "INV_CLASS_KEY", "COMPLETE_BOOL", "REF_INV_COND_USER_CD", "LOC_KEY", "LOC_CD" };

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that GetAssociatedInventoryRecords returns a DataSet that includes expected columns
    */
   @Test
   public void testGetAssociatedInventoryRecordsReturnsExpectedColumns() {
      final InventoryKey lInventoryA = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setAssociation( ASSOCIATION_ID );
      } );

      DataSet lAssociatedInventoryRecords =
            new AssociatedInventoryService( lInventoryA ).getAssociatedInventoryRecords();

      assertThat( Arrays.asList( lAssociatedInventoryRecords.getColumnNames() ),
            contains( EXPECTED_COLUMNS ) );
   }


   /**
    * Test that GetAssociatedInventoryRecords returns associated inventory records
    *
    * <pre>
    * Given an inventory A that has an association with two other inventory items
    * When retrieving inventory association records that are associated to inventory A
    * Then only inventory association records corresponding to the other two inventory items are returned
    * </pre>
    */
   @Test
   public void testGetAssociatedInventoryRecordsReturnsOtherAssociatedInventoryRecords() {
      final InventoryKey lInventoryA = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryB = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryC = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setAssociation( ASSOCIATION_ID );
      } );

      DataSet lAssociatedInventoryRecords =
            new AssociatedInventoryService( lInventoryA ).getAssociatedInventoryRecords();

      assertThat( lAssociatedInventoryRecords.getRowCount(), equalTo( 2 ) );

      // Extract the returned inventory keys
      List<InventoryKey> lInventoryKeys = lAssociatedInventoryRecords.getRows().stream()
            .map( aRecord -> new InventoryKey( aRecord.getString( "INV_KEY" ) ) )
            .collect( Collectors.toList() );

      assertThat( lInventoryKeys, contains( lInventoryB, lInventoryC ) );
   }
}
