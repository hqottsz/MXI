
package com.mxi.mx.core.query.labour;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.domain.builder.RefLabourSkillBuilder;
import com.mxi.am.domain.builder.WorkPackageSignatureRequirementBuilder;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.key.PrimaryKeyServiceStub;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;


/**
 * Query tests for GetESigReqBoolFromSchedWorkPackageSign.qrx
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetESigReqBoolFromSchedWorkPackageSignTest {

   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      PrimaryKeyService.setSingleton( new PrimaryKeyServiceStub() );
      QuerySetFactory.setInstance( QuerySetFactory.getInstance() );
      MxDataAccess.setInstance( MxDataAccess.getInstance() );
      EjbFactory.setSingleton( new EjbFactoryStub() );
   }


   @After
   public void tearDown() {
      PrimaryKeyService.setSingleton( null );
      QuerySetFactory.setInstance( null );
      MxDataAccess.setInstance( null );
      EjbFactory.setSingleton( null );
   }


   @Test
   public void testEsigRequiredByOrgSettings() {
      // Need org_hr entry
      HumanResourceKey lHrKey = createUser( "dummy_user_1" );

      // Need 2 org_org entries
      OrgKey lOrg1Key = createOrganization( "DUMMY_1" );
      OrgKey lOrg2Key = createOrganization( "DUMMY_2" );

      // Need 2 org_org_hr entries
      assignUserToOrg( lHrKey, lOrg1Key, true );
      assignUserToOrg( lHrKey, lOrg2Key, false );

      // Need ref_labour_skill entry
      RefLabourSkillKey lSkillKey = createLabourSkill( "DUMMY", true );

      // Need 2 org_labour_skill_map entries
      assignSkillToOrg( lSkillKey, lOrg1Key, true );
      assignSkillToOrg( lSkillKey, lOrg2Key, false );

      // Need sched_wp_sign_req entry
      SchedWPSignReqKey lSchedWPSignReqKey = createWPSignRequirement( lSkillKey );

      // Execute the query
      QuerySet lQs = execute( lHrKey, lSchedWPSignReqKey );

      // We expect esignature to be required, therefore a result should exist
      Assert.assertTrue( lQs.first() );
   }


   @Test
   public void testEsigNotRequiredByOrgSettings() {
      // Need org_hr entry
      HumanResourceKey lHrKey = createUser( "dummy_user_1" );

      // Need 2 org_org entries
      OrgKey lOrg1Key = createOrganization( "DUMMY_1" );
      OrgKey lOrg2Key = createOrganization( "DUMMY_2" );

      // Need 2 org_org_hr entries
      assignUserToOrg( lHrKey, lOrg1Key, true );
      assignUserToOrg( lHrKey, lOrg2Key, false );

      // Need ref_labour_skill entry
      RefLabourSkillKey lSkillKey = createLabourSkill( "DUMMY", true );

      // Need 2 org_labour_skill_map entries
      assignSkillToOrg( lSkillKey, lOrg1Key, false );
      assignSkillToOrg( lSkillKey, lOrg2Key, false );

      // Need sched_wp_sign_req entry
      SchedWPSignReqKey lSchedWPSignReqKey = createWPSignRequirement( lSkillKey );

      // Execute the query
      QuerySet lQs = execute( lHrKey, lSchedWPSignReqKey );

      // We don't expect esignature to be required, therefore a result should not exist
      Assert.assertFalse( lQs.first() );
   }


   @Test
   public void testEsigRequiredNoOrgSettings() {
      // Build the Labour Skill
      RefLabourSkillKey lSkillKey = createLabourSkill( "DUMMY", true );

      // Build the SchedWPSignReqKey
      SchedWPSignReqKey lSchedWPSignReqKey = createWPSignRequirement( lSkillKey );

      // Build the HumanResourceKey
      HumanResourceKey lHrKey = createUser( "dummy_user_1" );

      // Execute the query
      QuerySet lQs = execute( lHrKey, lSchedWPSignReqKey );

      // We expect esignature to be required, therefore a result should exist
      Assert.assertTrue( lQs.first() );
   }


   @Test
   public void testEsigNotRequiredNoOrgSettings() {
      // Build the Labour Skill
      RefLabourSkillKey lSkillKey = createLabourSkill( "DUMMY", false );

      // Build the SchedWPSignReqKey
      SchedWPSignReqKey lSchedWPSignReqKey = createWPSignRequirement( lSkillKey );

      // Build the HumanResourceKey
      HumanResourceKey lHrKey = createUser( "dummy_user_1" );

      // Execute the query
      QuerySet lQs = execute( lHrKey, lSchedWPSignReqKey );

      // We don't expect esignature to be required, therefore a result should not exist
      Assert.assertFalse( lQs.first() );
   }


   /**
    * Executes the query to be tested with its required parameters
    *
    * @param aHr
    * @param aSchedWPSignReqKey
    * @return
    */
   private QuerySet execute( HumanResourceKey aHr, SchedWPSignReqKey aSchedWPSignReqKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHr, "aHrDbId", "aHrId" );
      lArgs.add( aSchedWPSignReqKey, "aSignReqDbId", "aSignReqId" );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.query.labour.GetESigReqBoolFromSchedWorkPackageSign", lArgs );
   }


   /**
    * Creates a Scheduled Work Package Sign Requirement and returns its unique key
    *
    * @param aSkillKey
    * @return
    */
   private SchedWPSignReqKey createWPSignRequirement( RefLabourSkillKey aSkillKey ) {
      return new WorkPackageSignatureRequirementBuilder().withLabourSkill( aSkillKey ).build();
   }


   /**
    *
    * Assigns the labour skill specified to the organization specified
    *
    * @param aSkillKey
    * @param aOrg1Key
    * @param aESigRequired
    */
   private void assignSkillToOrg( RefLabourSkillKey aSkillKey, OrgKey aOrgKey,
         boolean aESigRequired ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aSkillKey, "labour_skill_db_id", "labour_skill_cd" );
      lArgs.add( aOrgKey, "org_db_id", "org_id" );
      lArgs.add( "esig_req_bool", ( aESigRequired ) ? 1 : 0 );

      MxDataAccess.getInstance().executeInsert( "org_labour_skill_map", lArgs );
   }


   /**
    *
    * Creates an organization with the code specified and returns its unique key
    *
    * @param aCode
    * @return
    */
   private OrgKey createOrganization( String aCode ) {
      return new OrganizationDomainBuilder().withCode( aCode ).withDescription( aCode )
            .withType( RefOrgTypeKey.MRO ).build();
   }


   /**
    *
    * Creates a user with the name specified and returns its unique key
    *
    * @param aName
    * @return
    */
   private HumanResourceKey createUser( String aName ) {
      return new HumanResourceDomainBuilder().withUsername( aName ).build();
   }


   /**
    *
    * Assigns the user specified to the organization specified
    *
    * @param aHr
    * @param aOrg
    * @param aDefault
    */
   private void assignUserToOrg( HumanResourceKey aHr, OrgKey aOrg, boolean aDefault ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHr, "hr_db_id", "hr_id" );
      lArgs.add( aOrg, "org_db_id", "org_id" );
      lArgs.add( "default_org_bool", ( aDefault ) ? 1 : 0 );

      MxDataAccess.getInstance().executeInsert( "org_org_hr", lArgs );
   }


   /**
    *
    * Creates a labour skill with the code specified and the esignature setting specified and
    * returns its unique key
    *
    * @param aCode
    * @param aESigRequired
    * @return
    */
   private RefLabourSkillKey createLabourSkill( String aCode, boolean aESigRequired ) {
      return new RefLabourSkillBuilder().withCode( aCode ).withESigRequired( aESigRequired )
            .build();
   }

}
