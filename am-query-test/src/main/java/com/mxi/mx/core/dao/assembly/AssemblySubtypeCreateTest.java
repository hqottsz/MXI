
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
 * Tests the assembly subtype create method.
 *
 * @author hmuradyan
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblySubtypeCreateTest {

   private AssemblySubtypeServiceTestDelegate iSubtypeService;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Create assembly subtype test.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testCreate() throws Exception {

      // Set the code and name to the maximum string length
      String lCode = "ASTCODE";
      String lName = "ASSEMBLY NAME";

      // Create the new organization
      AssemblySubtypeKey lNewSubtypeKey =
            iSubtypeService.create( new AssemblyKey( 4650, "101" ), lCode, lName );

      AssemblySubtype lNewSubtype = iSubtypeService.get( lNewSubtypeKey );

      assertEquals( lCode, lNewSubtype.getCode() );
      assertEquals( lName, lNewSubtype.getDescription() );
      assertEquals( new AssemblyKey( 4650, "101" ), lNewSubtype.getAssemblyKey() );
   }


   /**
    * Test Create assembly subtype with duplicate code.
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testCreateWithDuplicateCode() throws Exception {

      // Set the code and name to the maximum string length
      String lCode = "ASTCode";
      String lName = "Assembly sub type name";

      // Create the new organization
      iSubtypeService.create( new AssemblyKey( 4650, "101" ), lCode, lName );

      try {
         iSubtypeService.create( new AssemblyKey( 4650, "101" ), lCode, lName );
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
   }

}
