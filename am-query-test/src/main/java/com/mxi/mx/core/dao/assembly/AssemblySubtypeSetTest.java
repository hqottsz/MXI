
package com.mxi.mx.core.dao.assembly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AssemblySubtypeKey;
import com.mxi.mx.core.model.assembly.AssemblySubtype;
import com.mxi.mx.core.services.assembly.DuplicateAssemblySubtypeCodeException;
import com.mxi.mx.core.unittest.assembly.AssemblySubtypeServiceTestDelegate;


/**
 * Tests assembly subtype set method.
 *
 * @author hmuradyan
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblySubtypeSetTest {

   private AssemblySubtypeKey iNewSubtypeKey;

   private AssemblySubtypeServiceTestDelegate iSubtypeService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Set assembly subtype test.
    *
    * @throws Exception
    */
   @Test
   public void testSet() throws Exception {
      String lCode = "NEW_CODE";
      String lName = "NEW_NAME";
      iSubtypeService.set( iNewSubtypeKey, lCode, lName );

      AssemblySubtype lNewSubtype = iSubtypeService.get( iNewSubtypeKey );

      assertEquals( lCode, lNewSubtype.getCode() );
      assertEquals( lName, lNewSubtype.getDescription() );
   }


   /**
    * Test Create assembly subtype with duplicate code.
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testSetWithDuplicateCode() throws Exception {

      // Set the code and name to the maximum string length
      String lCode = "SomeCode";
      String lName = "Assembly sub type name";

      // Create the new assembly sub typ
      AssemblySubtypeKey lSubtypeKey =
            iSubtypeService.create( new AssemblyKey( 4650, "101" ), lCode, lName );

      try {
         iSubtypeService.setCode( lSubtypeKey, "ASTCode" );
         fail( "Duplicate code not reported." );
      } catch ( DuplicateAssemblySubtypeCodeException e ) {
         // Expected behaviour.
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
