package com.mxi.mx.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.domain.Id;
import com.mxi.mx.domain.part.Part;


public class PartRepositoryTest {

   private static final PartNoKey EXISTING_PART_NO_KEY = new PartNoKey( 4650, 1 );
   private static final String EXISTING_PART_NUMBER = "ABC";
   private static final String EXISTING_PART_MANUFACTURER = "DEF";
   private static final Id<Part> PART_ID = new Id<>( "00000000000000000000000000000001" );
   private static final Part EXISTING_PART = new Part( PART_ID, EXISTING_PART_NUMBER,
         EXISTING_PART_MANUFACTURER, RefInvClassKey.BATCH_CD );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   @Test
   public void findByNaturalKey_returnsPartWhenPartExists() {
      Optional<Part> lOptionalPart =
            PartRepository.findByNaturalKey( EXISTING_PART_NUMBER, EXISTING_PART_MANUFACTURER );
      assertTrue( lOptionalPart.isPresent() );
      assertEquals( EXISTING_PART, lOptionalPart.get() );
   }


   @Test
   public void findByNaturalKey_isNotCaseSensitiveOnManufacturer() {
      Optional<Part> lOptionalPart = PartRepository.findByNaturalKey( EXISTING_PART_NUMBER,
            EXISTING_PART_MANUFACTURER.toLowerCase() );
      assertTrue( lOptionalPart.isPresent() );
      assertEquals( EXISTING_PART, lOptionalPart.get() );
   }


   @Ignore( "Unclear if this test should pass or not. See OPER-17584" )
   @Test
   public void findByNaturalKey_returnsPartWhenPartExistsAndPartNumberCaseDoesNotMatch() {
      Optional<Part> lOptionalPart = PartRepository
            .findByNaturalKey( EXISTING_PART_NUMBER.toLowerCase(), EXISTING_PART_MANUFACTURER );
      assertTrue( lOptionalPart.isPresent() );
      assertEquals( EXISTING_PART, lOptionalPart.get() );
   }


   @Test
   public void findByNaturalKey_returnsEmptyWhenPartNumberDoesNotExist() {
      Optional<Part> lPart =
            PartRepository.findByNaturalKey( "DOES_NOT_EXIST", EXISTING_PART_MANUFACTURER );
      assertFalse( lPart.isPresent() );
   }


   @Test
   public void findByNaturalKey_returnsEmptyWhenPartManufacturerDoesNotExist() {
      Optional<Part> lPart =
            PartRepository.findByNaturalKey( EXISTING_PART_NUMBER, "DOES_NOT_EXIST" );
      assertFalse( lPart.isPresent() );
   }


   @Test
   public void findByPrimaryKey_returnsPartWhenPartExists() {
      Optional<Part> lOptionalPart = PartRepository.findByPrimaryKey( EXISTING_PART_NO_KEY );
      assertTrue( lOptionalPart.isPresent() );
      assertEquals( EXISTING_PART, lOptionalPart.get() );
   }


   @Test
   public void findByPrimaryKey_returnsEmptyWhenPartNoKeyDoesNotExist() {
      Optional<Part> lOptionalPart = PartRepository.findByPrimaryKey( new PartNoKey( 1234, 1234 ) );
      assertFalse( lOptionalPart.isPresent() );
   }


   @Test
   public void get_returnsPartWhenPartExists() {
      Part lPart = PartRepository.get( EXISTING_PART_NO_KEY );
      assertEquals( EXISTING_PART, lPart );
   }


   @Test( expected = IllegalStateException.class )
   public void get_throwsExceptionWhenPartNoKeyDoesNotExist() {
      PartRepository.get( new PartNoKey( 1234, 1234 ) );
   }
}
