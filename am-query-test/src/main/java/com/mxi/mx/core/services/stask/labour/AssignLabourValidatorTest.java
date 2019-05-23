package com.mxi.mx.core.services.stask.labour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.services.stask.labour.exception.CannotAssignLabourException;
import com.mxi.mx.core.services.stask.labour.validation.AssignLabourValidator;


@RunWith( BlockJUnit4ClassRunner.class )
public class AssignLabourValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final SchedLabourKey HISTORIC_LABOUR_ROW = new SchedLabourKey( 4650, 1 );
   private static final SchedLabourKey IN_WORK_TECH_COMPLETE_LABOUR_ROW =
         new SchedLabourKey( 4650, 2 );
   private static final SchedLabourKey ACTIVE_LABOUR_ROW = new SchedLabourKey( 4650, 3 );
   private static final SchedLabourKey IN_WORK_LABOUR_ROW = new SchedLabourKey( 4650, 4 );

   private AssignLabourValidator iValidator;


   @Before
   public void setup() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iValidator = new AssignLabourValidator();
   }


   @Test
   public void validate_historic() throws Exception {
      try {
         iValidator.validate( HISTORIC_LABOUR_ROW );
         fail( "Expected CannotAssignLabourException with reason 'HISTORIC'" );
      } catch ( CannotAssignLabourException aE ) {
         assertEquals( CannotAssignLabourException.Reason.HISTORIC, aE.getReason() );
      }
   }


   @Test
   public void validate_inWorkAndTechComplete() throws Exception {
      try {
         iValidator.validate( IN_WORK_TECH_COMPLETE_LABOUR_ROW );
         fail( "Expected CannotAssignLabourException to be thrown with reason 'TECH_COMPLETE'" );
      } catch ( CannotAssignLabourException aE ) {
         assertEquals( CannotAssignLabourException.Reason.TECH_COMPLETE, aE.getReason() );
      }
   }


   @Test
   public void validate_valid() throws Exception {
      iValidator.validate( ACTIVE_LABOUR_ROW );
      iValidator.validate( IN_WORK_LABOUR_ROW );
   }

}
