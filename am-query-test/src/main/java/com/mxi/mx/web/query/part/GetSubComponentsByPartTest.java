
package com.mxi.mx.web.query.part;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Ensures <code>GetSubComponentsByPart</code> query functions properly
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetSubComponentsByPartTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetSubComponentsByPartTest.class );
   }


   private static final PartNoKey ACFT = new PartNoKey( 4650, 1 );
   private static final PartNoKey TRK1_WITH_SUB_COMP = new PartNoKey( 4650, 2 );
   private static final PartNoKey TRK2_WITHOT_SUB_COMP = new PartNoKey( 4650, 3 );
   private static final PartNoKey ASSY1_WITHOT_SUB_COMP = new PartNoKey( 4650, 4 );
   private static final PartNoKey ASSY2_WITH_SUB_COMP = new PartNoKey( 4650, 5 );
   private static final PartNoKey TRK3_WITHOT_SUB_COMP = new PartNoKey( 4650, 6 );


   /**
    * Ensure the ACFT has non-SYS sub components
    */
   @Test
   public void testACFTHasSubComponents() {
      assertThat( hasSubComponents( ACFT ), is( true ) );
   }


   /**
    * Ensure the ASSY1 has no non-SYS sub components
    */
   @Test
   public void testASSY1HasNoSubComponents() {
      assertThat( hasSubComponents( ASSY1_WITHOT_SUB_COMP ), is( false ) );
   }


   /**
    * Ensure the ASSY2 has non-SYS sub components
    */
   @Test
   public void testASSY2HasSubComponents() {
      assertThat( hasSubComponents( ASSY2_WITH_SUB_COMP ), is( true ) );
   }


   /**
    * Ensure the TRK1 has non-SYS sub components
    */
   @Test
   public void testTRK1HasSubComponents() {
      assertThat( hasSubComponents( TRK1_WITH_SUB_COMP ), is( true ) );
   }


   /**
    * Ensure the TRK2 has no sub components
    */
   @Test
   public void testTRK2HasNoSubComponents() {
      assertThat( hasSubComponents( TRK2_WITHOT_SUB_COMP ), is( false ) );
   }


   /**
    * Ensure the TRK3 has no sub components
    */
   @Test
   public void testTRK3HasNoSubComponents() {
      assertThat( hasSubComponents( TRK3_WITHOT_SUB_COMP ), is( false ) );
   }


   /**
    * Check the query if the part has non-SYS sub components
    *
    * @param aPartNo
    *           the part number key to be checked
    *
    * @return true if the part has non-SYS sub components
    */
   private boolean hasSubComponents( PartNoKey aPartNo ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNo, "aPartNoDbId", "aPartNoId" );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.part.getSubComponentsByPart", lArgs );

      if ( lDs.isEmpty() ) {
         return false;
      }

      return true;
   }
}
