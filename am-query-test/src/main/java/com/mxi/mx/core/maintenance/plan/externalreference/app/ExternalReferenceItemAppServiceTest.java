package com.mxi.mx.core.maintenance.plan.externalreference.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.builder.PartRequirementBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.ExternalReferenceItemKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.maintenance.plan.externalreference.domain.ExternalReferenceItemRepository;
import com.mxi.mx.core.table.sched.SchedPartTable;


public class ExternalReferenceItemAppServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private ExternalReferenceItemAppService externalReferenceItemAppService;

   private ExternalReferenceItemRepository externalReferenceItemRepository;
   private PartRequirement partRequirement;
   private TaskPartKey partRequirementKey;
   private static final String NEW_REFERENCE = "NEW REFERENCE";


   @Before
   public void setUp() {
      externalReferenceItemAppService =
            InjectorContainer.get().getInstance( ExternalReferenceItemAppService.class );
      externalReferenceItemRepository =
            InjectorContainer.get().getInstance( ExternalReferenceItemRepository.class );

      partRequirement = new PartRequirement();
      partRequirement.setTaskKey( new TaskKey( "4650:1" ) );
      partRequirementKey = PartRequirementBuilder.build( partRequirement );
   }


   @Test( expected = NullPointerException.class )
   public void updatePartRequirementReference_nullPartRequirementKey() throws TriggerException {
      externalReferenceItemAppService.updatePartRequirementReference( null, "a" );
   }


   @Test( expected = IllegalArgumentException.class )
   public void updatePartRequirementReference_invalidPartRequirementKey() throws TriggerException {
      TaskPartKey invalidKey = new TaskPartKey( 4650, 2000, 1 );
      externalReferenceItemAppService.updatePartRequirementReference( invalidKey, "a" );
   }


   @Test
   public void updatePartRequirementReference_oldReferenceChangedToNull() throws TriggerException {

      final String referenceItemName = "IPC: 52-48-10";

      ExternalReferenceItemKey key = externalReferenceItemRepository.save( referenceItemName );
      SchedPartTable partRequirementRow = SchedPartTable.findByPrimaryKey( partRequirementKey );
      partRequirementRow.setExternalReferenceItemKey( key );
      partRequirementRow.update();

      assertEquals( key, partRequirementRow.getExternalReferenceItemKey() );

      // update the part requirement to not have a reference anymore
      externalReferenceItemAppService.updatePartRequirementReference( partRequirementKey, null );

      SchedPartTable updatedPartRequirementRow =
            SchedPartTable.findByPrimaryKey( partRequirementKey );
      assertEquals( null, updatedPartRequirementRow.getExternalReferenceItemKey() );
   }


   @Test
   public void updatePartRequirementReference_oldReferenceChangedToReferenceThatDoesNotExist()
         throws TriggerException {

      Optional<ExternalReferenceItemKey> invalidKey =
            externalReferenceItemRepository.getByReferenceItemName( NEW_REFERENCE );
      assertFalse( invalidKey.isPresent() );

      // update the part requirement to have a new reference which did not exist previously in the
      // db.
      externalReferenceItemAppService.updatePartRequirementReference( partRequirementKey,
            NEW_REFERENCE );

      Optional<ExternalReferenceItemKey> keyOptional =
            externalReferenceItemRepository.getByReferenceItemName( NEW_REFERENCE );
      SchedPartTable updatedPartRequirementRow =
            SchedPartTable.findByPrimaryKey( partRequirementKey );

      assertEquals( keyOptional.get(), updatedPartRequirementRow.getExternalReferenceItemKey() );
   }


   @Test
   public void updatePartRequirementReference_oldReferenceChangedToReferenceThatDoesExist()
         throws TriggerException {

      // save the reference to the database
      ExternalReferenceItemKey referenceKey = externalReferenceItemRepository.save( NEW_REFERENCE );

      SchedPartTable partRequirementRow = SchedPartTable.findByPrimaryKey( partRequirementKey );
      ExternalReferenceItemKey oldKey = partRequirementRow.getExternalReferenceItemKey();

      // update the part requirement to use the reference that exists in the db already
      externalReferenceItemAppService.updatePartRequirementReference( partRequirementKey,
            NEW_REFERENCE );

      SchedPartTable updatedPartRequirementRow =
            SchedPartTable.findByPrimaryKey( partRequirementKey );

      assertEquals( referenceKey, updatedPartRequirementRow.getExternalReferenceItemKey() );
      assertNotEquals( oldKey, updatedPartRequirementRow.getExternalReferenceItemKey() );
   }

}
