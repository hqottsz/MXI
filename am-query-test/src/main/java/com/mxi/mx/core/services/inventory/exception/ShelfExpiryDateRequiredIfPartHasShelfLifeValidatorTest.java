package com.mxi.mx.core.services.inventory.exception;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.services.inventory.validator.ShelfExpiryDateRequiredIfPartHasShelfLifeValidator;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;


/**
 * This class tests the validate method in ShelfExpiryDateRequiredIfPartHasShelfLifeValidator
 *
 */
public class ShelfExpiryDateRequiredIfPartHasShelfLifeValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   ShelfExpiryDateRequiredIfPartHasShelfLifeValidator validator =
         new ShelfExpiryDateRequiredIfPartHasShelfLifeValidator();

   private PartNoKey partNoKey;


   /**
    *
    * Test validate throws exception when no shelf expiry date provided and part has no shelf life
    *
    * @throws Exception
    */
   @Test( expected = ShelfExpiryDateRequiredIfPartHasShelfLifeException.class )
   public void testValidate_PartHasShelfLife() throws Exception {

      // set the shelf life for part
      EqpPartNoTable eqpPartNoTable = EqpPartNoTable.findByPrimaryKey( partNoKey );
      eqpPartNoTable.setShelfLifeQt( 5.0 );
      eqpPartNoTable.setShelfLifeUnit( new RefEngUnitKey( "MONTH" ) );
      eqpPartNoTable.update();

      validator.validate( partNoKey, null );
   }


   /**
    *
    * Test validate throws no exception when no shelf expiry date provided and part has shelf life
    *
    * @throws Exception
    */
   @Test( expected = Test.None.class )
   public void testValidate_PartHasNoShelfLife() throws Exception {

      validator.validate( partNoKey, null );

   }


   /**
    *
    * Test validate throws no exception when shelf expiry date provided and part has shelf life
    *
    * @throws Exception
    */
   @Test( expected = Test.None.class )
   public void testValidateShelfExpiryProvided_PartHasShelfLife() throws Exception {

      // set the shelf life for part
      EqpPartNoTable eqpPartNoTable = EqpPartNoTable.findByPrimaryKey( partNoKey );
      eqpPartNoTable.setShelfLifeQt( 5.0 );
      eqpPartNoTable.setShelfLifeUnit( new RefEngUnitKey( "MONTH" ) );
      eqpPartNoTable.update();

      validator.validate( partNoKey, new Date() );

   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {

      partNoKey = new PartNoBuilder().withOemPartNo( "PART1" ).withStatus( RefPartStatusKey.ACTV )
            .withDefaultPartGroup().build();

   }

}
