package com.mxi.mx.core.quicktext.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.quicktext.model.QuickText;
import com.mxi.mx.core.quicktext.model.QuickTextId;
import com.mxi.mx.core.quicktext.model.QuickTextType;


public abstract class QuickTextRepositoryBaseTest<T extends QuickTextRepository> {

   protected static final QuickTextType TEST_TYPE_A = QuickTextType.of( "MX_TEST_A" );
   protected static final QuickTextType TEST_TYPE_B = QuickTextType.of( "MX_TEST_B" );

   protected T repository;


   @Before
   public void setUp() {
      repository = getInstance();
   }


   protected abstract T getInstance();


   @Test
   public void findByType_emptyListWhenNoExistingRecords() {
      List<QuickText> quickTexts = repository.findByType( TEST_TYPE_A );

      assertTrue( quickTexts.isEmpty() );
   }


   @Test
   public void findByType_findsQuickTextRecordsThatMatchType() {
      repository.create( QuickText.builder().type( TEST_TYPE_A ).value( "Test 1." ).build() );
      QuickTextId test2 =
            repository.create( QuickText.builder().type( TEST_TYPE_B ).value( "Test 2." ).build() );

      List<QuickText> quickTexts = repository.findByType( TEST_TYPE_B );

      assertEquals( 1, quickTexts.size() );
      assertEquals( test2, quickTexts.get( 0 ).getId() );
   }


   @Test( expected = IllegalStateException.class )
   public void create_withPopulatedIdIsNotAllowed() {
      QuickText quickTextWithPopulatedId = QuickText.builder().type( TEST_TYPE_A )
            .value( "Test Value" ).id( QuickTextId.of( "1234:5678" ) ).build();

      repository.create( quickTextWithPopulatedId );
   }


   @Test
   public void create_newQuickText() {
      QuickText newQuickText =
            QuickText.builder().type( TEST_TYPE_A ).value( "Test Value" ).build();

      QuickTextId savedQuickText = repository.create( newQuickText );

      assertNotNull( savedQuickText );
      assertEquals( savedQuickText, newQuickText.getId() );
   }


   @Test
   public void create_duplicateValueIsAllowedOnDifferentType() {
      QuickTextId newQuickText = repository
            .create( QuickText.builder().type( TEST_TYPE_A ).value( "Same Value" ).build() );
      QuickTextId sameValueOnDifferentType = repository
            .create( QuickText.builder().type( TEST_TYPE_B ).value( "Same Value" ).build() );

      assertNotNull( sameValueOnDifferentType );
      assertNotEquals( newQuickText, sameValueOnDifferentType );
   }


   @Test
   public void find_byId_emptyWhenNoMatchFound() {
      Optional<QuickText> nonExisting = repository.find( QuickTextId.of( "1:4650" ) );

      assertFalse( nonExisting.isPresent() );
   }


   @Test
   public void find_byId_retrievesQuickTextById() {
      QuickText quickText = QuickText.builder().type( TEST_TYPE_A ).value( "A Value" ).build();
      QuickTextId existingId = repository.create( quickText );

      Optional<QuickText> existing = repository.find( existingId );

      assertTrue( existing.isPresent() );
      assertEquals( quickText, existing.get() );
   }


   @Test
   public void find_byTypeAndValue_emptyWhenNoMatchFound() {
      repository.create( QuickText.builder().type( TEST_TYPE_A ).value( "Value 1" ).build() );
      repository.create( QuickText.builder().type( TEST_TYPE_B ).value( "Value 2" ).build() );

      Optional<QuickText> quickText = repository.find( TEST_TYPE_A, "Value 2" );

      assertFalse( quickText.isPresent() );
   }


   @Test
   public void find_byTypeAndValue_findsValueWhenBothMatch() {
      QuickText existingQuickText =
            QuickText.builder().type( TEST_TYPE_A ).value( "Value 1" ).build();
      repository.create( existingQuickText );

      Optional<QuickText> quickText = repository.find( TEST_TYPE_A, "Value 1" );

      assertTrue( quickText.isPresent() );
      assertEquals( existingQuickText, quickText.get() );
   }


   @Test
   public void update_modifiesExistingQuickText() {
      QuickText quickText = QuickText.builder().type( TEST_TYPE_A ).value( "Value 1" ).build();
      repository.create( quickText );
      quickText.setValue( "Updated Value 1" );

      repository.update( quickText );

      Optional<QuickText> updatedQuickText = repository.find( quickText.getId() );
      assertTrue( updatedQuickText.isPresent() );
      assertEquals( "Updated Value 1", updatedQuickText.get().getValue() );
   }


   @Test
   public void delete_deletesExistingRecordById() {
      QuickTextId existingQuickText = repository
            .create( QuickText.builder().type( TEST_TYPE_A ).value( "Same Value" ).build() );

      boolean wasDeleted = repository.delete( existingQuickText );

      Optional<QuickText> deletedQuickText = repository.find( existingQuickText );
      assertFalse( deletedQuickText.isPresent() );
      assertTrue( wasDeleted );
   }


   @Test
   public void delete_returnsFalseWhenNoRecordToDelete() {
      QuickTextId nonExistingId = QuickTextId.of( "12:4650" );
      boolean wasDeleted = repository.delete( nonExistingId );

      assertFalse( wasDeleted );
   }

}
