package com.mxi.mx.core.maintenance.plan.externalreference.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ExternalReferenceItemKey;
import com.mxi.mx.core.maintenance.plan.externalreference.domain.ExternalReferenceItemRepository;


public class JdbcExternalReferenceItemRepositoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   // object under test
   ExternalReferenceItemRepository externalReferenceItemRepository;

   ExternalReferenceItemDao externalReferenceItemDao;


   @Before
   public void setUp() {

      externalReferenceItemRepository =
            InjectorContainer.get().getInstance( JdbcExternalReferenceItemRepository.class );
      externalReferenceItemDao =
            InjectorContainer.get().getInstance( JdbcExternalReferenceItemDao.class );

   }


   @Test
   public void save() {

      // ASSEMBLE
      final String referenceItemName = "IPC: 52-48-10";

      // ACT
      ExternalReferenceItemKey extRefItemKey =
            externalReferenceItemRepository.save( referenceItemName );

      // ASSERT
      assertNotNull( extRefItemKey );

   }


   @Test
   public void save_changesToUpperCase() {

      // ASSEMBLE
      final String referenceItemName = "ipc: 52-48-10";

      // ACT
      ExternalReferenceItemKey extRefItemKey =
            externalReferenceItemRepository.save( referenceItemName );

      // ASSERT
      assertNotNull( extRefItemKey );
      ExternalReferenceItemTableRow row =
            externalReferenceItemDao.findByPrimaryKey( extRefItemKey );
      assertEquals( referenceItemName.toUpperCase(), row.getReferenceItemName() );

   }


   @Test( expected = NullPointerException.class )
   public void save_nullReference() {

      // ACT
      try {
         externalReferenceItemRepository.save( null );
         fail( "The expected NullPointerException was not thrown." );
      } catch ( NullPointerException e ) {
         assertTrue( e.getMessage().equals(
               "No reference item name was specified when saving an external reference item." ) );
         throw e;
      }

   }


   @Test
   public void getByReferenceItemName_exists() {

      // ASSEMBLE
      final String referenceItemName = "IPC: 52-48-20";

      ExternalReferenceItemKey extRefItemKey =
            externalReferenceItemRepository.save( referenceItemName );
      assertNotNull( extRefItemKey );

      // ACT
      Optional<ExternalReferenceItemKey> extRefItemKeyOptional = externalReferenceItemRepository
            .getByReferenceItemName( referenceItemName.toLowerCase() );

      // ASSERT
      assertTrue( extRefItemKeyOptional.isPresent() );
      assertTrue( extRefItemKey.equals( extRefItemKeyOptional.get() ) );

   }


   @Test
   public void getByReferenceItemName_doesNotExist() {

      // ASSEMBLE
      final String referenceItemName = "IPC: 52-48-30";

      // ACT
      Optional<ExternalReferenceItemKey> extRefItemKeyOptional =
            externalReferenceItemRepository.getByReferenceItemName( referenceItemName );

      // ASSERT
      assertFalse( extRefItemKeyOptional.isPresent() );

   }


   @Test
   public void getByReferenceItemName_nullReference() {

      // ACT
      Optional<ExternalReferenceItemKey> extRefItemKeyOptional =
            externalReferenceItemRepository.getByReferenceItemName( null );

      // ASSERT
      assertFalse( extRefItemKeyOptional.isPresent() );

   }

}
