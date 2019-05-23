package com.mxi.mx.core.query.part;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EqpPartVendorRepKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.part.PartNoService;
import com.mxi.mx.core.table.eqp.EqpPartVendorRepTable;


/**
 *
 * This class tests the RepairVendorDetailsByPartAndVendor query.
 *
 * @author IndunilW
 */
public class RepairVendorDetailsByPartAndVendorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private PartNoKey iPartNokey = null;
   private VendorKey iVendorKey = null;
   private EqpPartVendorRepKey iEqpPartVendorRepKey = null;
   private BigDecimal iRepairCost = new BigDecimal( 12.00 );
   private Float iLeadTime = new Float( 1.25 );

   private PartNoService partNoService;


   @Before
   public void setup() {

      this.partNoService = new PartNoService();

      iPartNokey = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
      } );

      iVendorKey = Domain.createVendor( aVendor -> {
         aVendor.addPart( iPartNokey );
      } );

      iEqpPartVendorRepKey = new EqpPartVendorRepKey( iPartNokey, iVendorKey );
      EqpPartVendorRepTable lEqpPartVendorRepTable = new EqpPartVendorRepTable();
      lEqpPartVendorRepTable.insert( iEqpPartVendorRepKey );

      EqpPartVendorRepTable lTable = EqpPartVendorRepTable.findByPrimaryKey( iEqpPartVendorRepKey );
      lTable.setPartVendorPrice( iRepairCost );
      lTable.setLeadTime( iLeadTime );
      lTable.update();

   }


   /**
    *
    * GIVEN a vendor with a repair cost for a part WHEN run RepairVendorDetailsByPartAndVendor query
    * THEN should return repair cost mapped with the vendor and part the inventory.
    */
   @Test
   public void testRepairVendorPriceByPartTest() {
      QuerySet lQs = partNoService.getRepairVendorDetailsByPartAndVendor( iVendorKey, iPartNokey );

      assertEquals( 1.0, lQs.getRowCount(), 0 );

      while ( lQs.next() ) {
         assertEquals( iRepairCost, lQs.getBigDecimal( "repair_cost" ) );
         assertEquals( iLeadTime, lQs.getFloatObj( "lead_time" ) );
      }

   }
}
