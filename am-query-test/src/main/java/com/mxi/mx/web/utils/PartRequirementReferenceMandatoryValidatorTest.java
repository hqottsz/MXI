package com.mxi.mx.web.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ExternalReferenceItemKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefControlMethodKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.web.exception.PartRequirementReferenceMandatoryException;


public class PartRequirementReferenceMandatoryValidatorTest {

   private static final int USER_ID = 0;

   private TaskKey taskKey;
   private PartNoBuilder partNoBuilder;
   private PartNoKey partNo;
   private GlobalParametersStub configParms = new GlobalParametersStub( "LOGIC" );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUserRule = new OperateAsUserRule( USER_ID, "SYSTEM" );


   @Before
   public void setUp() {

      configParms.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_BOM_PART_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_TASK_CD", false );
      configParms.setBoolean( "SHOW_PART_REQUIREMENT_REFERENCE", true );
      configParms.setString( "PART_REQUIREMENT_REFERENCE_MANDATORY_INV_CLASSES", "BATCH" );
      configParms.setString( "PART_REQUIREMENT_REFERENCE_MANDATORY_ACTIONS", "REQ" );

      GlobalParameters.setInstance( configParms );

      taskKey = new TaskBuilder().build();
      partNoBuilder =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).withDefaultPartGroup();
      partNo = partNoBuilder.build();
   }


   @Test
   public void validate_withControlMethodNull() throws MxException {
      TaskPartKey taskPartKey = new PartRequirementDomainBuilder( taskKey )
            .withInstallPart( partNo ).forPartGroup( partNoBuilder.getDefaultPartGroup() )
            .withRequestAction( RefReqActionKey.REQ )
            .withReference( new ExternalReferenceItemKey( 0, 1 ) ).build();

      PartRequirementReferenceMandatoryValidator.validate( taskPartKey );
   }


   @Test
   public void validate_withControlMethodBaseline() throws MxException {
      TaskPartKey taskPartKey = new PartRequirementDomainBuilder( taskKey )
            .withInstallPart( partNo ).forPartGroup( partNoBuilder.getDefaultPartGroup() )
            .withRequestAction( RefReqActionKey.REQ )
            .withControlMethod( RefControlMethodKey.BASELINE )
            .withReference( new ExternalReferenceItemKey( 0, 1 ) ).build();

      PartRequirementReferenceMandatoryValidator.validate( taskPartKey );
   }


   @Test( expected = PartRequirementReferenceMandatoryException.class )
   public void validate_withControlMethodManual() throws MxException {
      TaskPartKey taskPartKey = new PartRequirementDomainBuilder( taskKey )
            .withInstallPart( partNo ).forPartGroup( partNoBuilder.getDefaultPartGroup() )
            .withRequestAction( RefReqActionKey.REQ )
            .withControlMethod( RefControlMethodKey.MANUAL )
            .withReference( new ExternalReferenceItemKey( 0, 1 ) ).build();

      PartRequirementReferenceMandatoryValidator.validate( taskPartKey );
   }


   /**
    * Creates a part requirement configured to have an optional reference, then it validates that
    * the reference is mandatory after new part request is created.
    */
   @Test( expected = PartRequirementReferenceMandatoryException.class )
   public void validate_existingPartReqAddingNewPartRequest() throws MxException {
      TaskPartKey taskPartKey = new PartRequirementDomainBuilder( taskKey )
            .withInstallPart( partNo ).forPartGroup( partNoBuilder.getDefaultPartGroup() )
            .withRequestAction( RefReqActionKey.INKIT )
            .withControlMethod( RefControlMethodKey.MANUAL ).build();

      Domain.createPartRequest( partReq -> {
         partReq.partRequirement( new TaskInstPartKey( taskPartKey, 1 ) );
      } );

      PartRequirementReferenceMandatoryValidator.validate( taskPartKey );
   }


   /**
    * Creates a part requirement configured to have optional references, then it validates that the
    * reference is still optional even after new part request is created because REQ action is not
    * configured as mandatory.
    */
   @Test
   public void validate_existingPartReqWhenAddingNewPartRequest_optionalREQAction()
         throws MxException {

      configParms.setString( "PART_REQUIREMENT_REFERENCE_MANDATORY_ACTIONS", "" );
      configParms.setString( "PART_REQUIREMENT_REFERENCE_MANDATORY_ACTIONS", "SER" );

      TaskPartKey taskPartKey = new PartRequirementDomainBuilder( taskKey )
            .withInstallPart( partNo ).forPartGroup( partNoBuilder.getDefaultPartGroup() )
            .withRequestAction( RefReqActionKey.INKIT )
            .withControlMethod( RefControlMethodKey.MANUAL ).build();

      Domain.createPartRequest( partReq -> {
         partReq.partRequirement( new TaskInstPartKey( taskPartKey, 1 ) );
      } );

      PartRequirementReferenceMandatoryValidator.validate( taskPartKey );
   }

}
