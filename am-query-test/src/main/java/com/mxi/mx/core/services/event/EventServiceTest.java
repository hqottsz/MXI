
package com.mxi.mx.core.services.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.EventBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;


/**
 * This class tests the {@link EventService} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class EventServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that when the event given does not have status CFACTV, an InvalidStatusException is
    * thrown.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatCompleteEventThrowsInvalidStatusException() throws Exception {
      EventKey lEvent = new EventBuilder().build();

      EditEventTO lEventTO = new EditEventTO();
      lEventTO.setEvent( lEvent );

      try {
         EventService.completeEvent( lEventTO, null );

         fail( "Expected InvalidStatusException" );
      } catch ( InvalidStatusException e ) {
         assertEquals( "Message Key", "core.err.30214", e.getMessageKey() );
         assertEquals( "Event Status", "CFACTV", e.getMessageArgument( 0 ) );
         assertEquals( "Event Type", i18n.get( "core.lbl.EVENT" ), e.getMessageArgument( 1 ) );
      }
   }

}
