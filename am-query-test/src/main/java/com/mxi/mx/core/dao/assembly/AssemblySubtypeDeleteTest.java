
package com.mxi.mx.core.dao.assembly;

import static org.junit.Assert.fail;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AssemblySubtypeKey;
import com.mxi.mx.core.unittest.assembly.AssemblySubtypeServiceTestDelegate;


/**
 * Tests the assembly subtype delete method
 *
 * @author hmuradyan
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblySubtypeDeleteTest {

   private AssemblySubtypeKey iNewSubtypeKey;

   private AssemblySubtypeServiceTestDelegate iSubtypeService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests to see if an assembly subtype not linked with extraction rule can be deleted.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteAssemblySubtype() throws Exception {

      Assert.assertNotNull( iNewSubtypeKey );

      try {

         // Delete the organization
         iSubtypeService.delete( iNewSubtypeKey );
      } catch ( Exception e ) {
         fail( "Delete assembly sub type has failed" );
      }
   }


   /**
    * Constructor for the test case.
    *
    * @exception Exception
    *               if an unexpected error occurs.
    */
   @Before
   public void setUp() throws Exception {
      iSubtypeService = new AssemblySubtypeServiceTestDelegate();
      iNewSubtypeKey = iSubtypeService.create( new AssemblyKey( 4650, "101" ), "ASTCode",
            "Assembly sub type name" );
   }

}
