package com.mxi.mx.core.query.order;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.services.order.OrderService;


/**
 *
 * This class tests the IsPromisedByDateSetForAllLinesInRO query.
 *
 * @author IndunilW
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class IsPromisedByDateSetForAllLinesInROTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private PurchaseOrderKey iRepairOrder = null;
   private Date iPromisedByDate = new Date( new Date().getTime() + ( 24 * 60 * 60 * 1000 ) );


   @Before
   public void setup() {

      iRepairOrder = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
      } );

      // create line with promised by date.
      Domain.createPurchaseOrderLine( aLine -> {
         aLine.orderKey( iRepairOrder );
         aLine.lineType( RefPoLineTypeKey.REPAIR );
         aLine.promisedBy( iPromisedByDate );
      } );

      // create line with null promised by date.
      Domain.createPurchaseOrderLine( aLine -> {
         aLine.orderKey( iRepairOrder );
         aLine.lineType( RefPoLineTypeKey.REPAIR );
         aLine.promisedBy( null );
      } );

   }


   /**
    *
    * GIVEN a active RO with one line without promised by date WHEN run
    * IsPromisedByDateSetForAllLinesInRO query THEN should return false.
    */
   @Test
   public void testIsPromisedByDateSetForAllLinesInRO() {
      Boolean lActualResult = new OrderService().isPromisedByDateSetForAllLinesInRO( iRepairOrder );
      assertEquals( false, lActualResult );
   }
}
