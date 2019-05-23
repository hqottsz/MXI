
package com.mxi.mx.core.services.inventory.oper;

import static com.mxi.mx.core.key.RefInvCondKey.ARCHIVE;
import static com.mxi.mx.core.key.RefInvCondKey.CONDEMN;
import static com.mxi.mx.core.key.RefInvCondKey.INREP;
import static com.mxi.mx.core.key.RefInvCondKey.QUAR;
import static com.mxi.mx.core.key.RefInvCondKey.SCRAP;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Ensure that {@link AircraftOperationalService} works as intended
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AircraftOperationalServiceTest {

   private static final AircraftKey AIRCRAFT = new AircraftKey( 4650, 1 );

   private AircraftOperationalServiceStub iService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensure the condition can be updated
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testChangeStatus() throws Exception {
      whenOldConditionIs( INREP );

      calculateAircraftStatus();

      assertThat( condition(), is( set() ) );
   }


   /**
    * * Ensures the condition is not changed when the old condition is <code>ARCHIVE</code>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDontChangeStatusWhenArchived() throws Exception {
      whenOldConditionIs( ARCHIVE );

      calculateAircraftStatus();

      assertThat( condition(), is( not( set() ) ) );
   }


   /**
    * Ensures the condition is not changed when the old condition is <code>CONDEMN</code>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDontChangeStatusWhenCondemned() throws Exception {
      whenOldConditionIs( CONDEMN );

      calculateAircraftStatus();

      assertThat( condition(), is( not( set() ) ) );
   }


   /**
    * Ensures the condition is not changed when the old condition is <code>QUAR</code>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDontChangeStatusWhenQuarantined() throws Exception {
      whenOldConditionIs( QUAR );

      calculateAircraftStatus();

      assertThat( condition(), is( not( set() ) ) );
   }


   /**
    * Ensures the condition is not changed when the old condition is <code>SCRAP</code>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDontChangeStatusWhenScrapped() throws Exception {
      whenOldConditionIs( SCRAP );

      calculateAircraftStatus();

      assertThat( condition(), is( not( set() ) ) );
   }


   /**
    * Calculates the aircraft status
    *
    * @throws Exception
    *            if an error occurs
    */
   private void calculateAircraftStatus() throws Exception {
      iService.calculateOperatingStatusWork( AIRCRAFT );
   }


   /**
    * The new aircraft condition
    *
    * @return the condition
    */
   private RefInvCondKey condition() {
      return iService.iCondition;
   }


   /**
    * A matcher that determines if a condition is set
    *
    * @return a matcher
    */
   private Matcher<RefInvCondKey> set() {
      return describedAs( "set", not( nullValue( RefInvCondKey.class ) ) );
   }


   /**
    * Sets the aircraft condition to specified value
    *
    * @param aCondition
    *           if an error occurs
    */
   private void whenOldConditionIs( RefInvCondKey aCondition ) {
      InvInvTable lAircraftTable = InvInvTable.findByPrimaryKey( AIRCRAFT.getInventoryKey() );
      lAircraftTable.setInvCond( aCondition );
      lAircraftTable.update();
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iService = new AircraftOperationalServiceStub();
   }


   /**
    * Stub out auxillary functions
    */
   private static class AircraftOperationalServiceStub extends AircraftOperationalService {

      RefInvCondKey iCondition = null;


      @Override
      protected void setCondition( AircraftKey aAircraft, InventoryStatus aStatus, Date aDate ) {
         iCondition = aStatus.getNewCondition();
      }


      @Override
      protected void setOperatingStatus( AircraftKey aAircraft, InventoryStatus aStatus,
            Date aDate ) {
      }
   }
}
