package com.mxi.am.api.resource.sys.refterm.labourskill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.erp.hr.user.UserAccount;
import com.mxi.am.api.resource.erp.hr.user.UserAccountSearchParameters;
import com.mxi.am.api.resource.erp.hr.user.impl.UserAccountResourceBean;
import com.mxi.am.api.resource.sys.refterm.labourskill.impl.LabourSkillResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for LabourSkill ResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class LabourSkillResourceBeanTest extends ResourceBeanTest {

   private static final String LABOUR_SKILL_CODE = "ENG";
   private static final String LABOUR_SKILL_NAME = "Engineering";
   private static final String LABOUR_SKILL_DESCRIPTION = "Engineering";
   private static final double ESTIMATED_HOURLY_COST = 75.0;
   private static final boolean ESIGNATURE_REQUIRED = true;

   private static final String LABOUR_SKILL_CODE2 = "PILOT";
   private static final String LABOUR_SKILL_NAME2 = "Pilot";
   private static final String LABOUR_SKILL_DESCRIPTION2 = "Pilot";
   private static final double ESTIMATED_HOURLY_COST2 = 100.0;
   private static final boolean ESIGNATURE_REQUIRED2 = true;

   private static final String LABOUR_SKILL_CODE3 = "LBR";
   private static final String LABOUR_SKILL_NAME3 = "Laborer";
   private static final String LABOUR_SKILL_DESCRIPTION3 = "Laborer";
   private static final double ESTIMATED_HOURLY_COST3 = 75.0;
   private static final boolean ESIGNATURE_REQUIRED3 = true;

   private static final String LABOUR_SKILL_CODE4 = "TEST01";
   private static final String LABOUR_SKILL_NAME4 = "TEST_CODE";
   private static final String LABOUR_SKILL_DESCRIPTION4 = "Labour Skill code";
   private static final double ESTIMATED_HOURLY_COST4 = 10.0;
   private static final boolean ESIGNATURE_REQUIRED4 = true;

   private static final String INVALID_LABOUR_SKILL_CODE = "INS";
   private static final String NULL_LABOUR_SKILL_CODE = null;
   private static final int LABOUR_SKILL_RECORD_COUNT = 4;
   private static final int LABOUR_SKILL_RECORD_COUNT2 = 3;

   private static final String USERNAME = "mxintegration";

   LabourSkill actualLabourSkill = new LabourSkill();
   List<LabourSkill> labourSkillObjList = new ArrayList<LabourSkill>();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( LabourSkillResource.class ).to( LabourSkillResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   private LabourSkillResourceBean labourSkillResourceBean;

   @Inject
   private UserAccountResourceBean userAccountResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      labourSkillResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      constructLabourSkillObject();
      constructLabourSkillList();
   }


   /**
    *
    * Test correct Labour skill object returns for a valid labour skill code success path-
    * Response-200
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void get_success_validLabourSkillCode() throws AmApiResourceNotFoundException {
      LabourSkill labourSkill = labourSkillResourceBean.get( LABOUR_SKILL_CODE );
      assertEquals( actualLabourSkill, labourSkill );
   }


   /**
    *
    * Test all the Labour skill objects returns success path- Response-200
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_success() throws AmApiResourceNotFoundException {
      LabourSkillSearchParameters labourSkillSearchParameters = new LabourSkillSearchParameters();
      List<LabourSkill> labourSkillList =
            labourSkillResourceBean.search( labourSkillSearchParameters );
      assertEquals( "Labour Skill count mismatched", LABOUR_SKILL_RECORD_COUNT,
            labourSkillList.size() );
      assertTrue( "Incorrect Labour skills list returned",
            labourSkillObjList.containsAll( labourSkillList ) );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_success_validApplicableUserId() throws AmApiResourceNotFoundException {
      LabourSkillSearchParameters labourSkillSearchParameters = new LabourSkillSearchParameters();
      UserAccountSearchParameters userAccountSearchParameters = new UserAccountSearchParameters();
      List<UserAccount> userAccount =
            userAccountResourceBean.search( userAccountSearchParameters.username( USERNAME ) );
      labourSkillSearchParameters.setApplicableUserId( userAccount.get( 0 ).getId() );
      List<LabourSkill> labourSkillList =
            labourSkillResourceBean.search( labourSkillSearchParameters );
      assertEquals( "Labour Skill count mismatched", LABOUR_SKILL_RECORD_COUNT2,
            labourSkillList.size() );
      labourSkillObjList.remove( 3 );
      assertTrue( "Incorrect Labour skills list returned",
            labourSkillObjList.containsAll( labourSkillList ) );
   }


   /**
    *
    * Test get method with invalid labour skill code - status 404
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_invalidLabourSkillCode() throws AmApiResourceNotFoundException {
      labourSkillResourceBean.get( INVALID_LABOUR_SKILL_CODE );
   }


   /**
    *
    * Test get method with null labour skill code - status 404
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_nullLabourSkillCode() throws AmApiResourceNotFoundException {
      labourSkillResourceBean.get( NULL_LABOUR_SKILL_CODE );
   }


   public void constructLabourSkillObject() {
      actualLabourSkill.setCode( LABOUR_SKILL_CODE );
      actualLabourSkill.setName( LABOUR_SKILL_NAME );
      actualLabourSkill.setDescription( LABOUR_SKILL_DESCRIPTION );
      actualLabourSkill.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST );
      actualLabourSkill.setEsignatureRequired( ESIGNATURE_REQUIRED );
   }


   public void constructLabourSkillList() {

      LabourSkill labourSkill1 = new LabourSkill();
      labourSkill1.setCode( LABOUR_SKILL_CODE );
      labourSkill1.setName( LABOUR_SKILL_NAME );
      labourSkill1.setDescription( LABOUR_SKILL_DESCRIPTION );
      labourSkill1.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST );
      labourSkill1.setEsignatureRequired( ESIGNATURE_REQUIRED );

      LabourSkill labourSkill2 = new LabourSkill();
      labourSkill2.setCode( LABOUR_SKILL_CODE2 );
      labourSkill2.setName( LABOUR_SKILL_NAME2 );
      labourSkill2.setDescription( LABOUR_SKILL_DESCRIPTION2 );
      labourSkill2.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST2 );
      labourSkill2.setEsignatureRequired( ESIGNATURE_REQUIRED2 );

      LabourSkill labourSkill3 = new LabourSkill();
      labourSkill3.setCode( LABOUR_SKILL_CODE3 );
      labourSkill3.setName( LABOUR_SKILL_NAME3 );
      labourSkill3.setDescription( LABOUR_SKILL_DESCRIPTION3 );
      labourSkill3.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST3 );
      labourSkill3.setEsignatureRequired( ESIGNATURE_REQUIRED3 );

      LabourSkill labourSkill4 = new LabourSkill();
      labourSkill4.setCode( LABOUR_SKILL_CODE4 );
      labourSkill4.setName( LABOUR_SKILL_NAME4 );
      labourSkill4.setDescription( LABOUR_SKILL_DESCRIPTION4 );
      labourSkill4.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST4 );
      labourSkill4.setEsignatureRequired( ESIGNATURE_REQUIRED4 );

      labourSkillObjList.add( labourSkill1 );
      labourSkillObjList.add( labourSkill2 );
      labourSkillObjList.add( labourSkill3 );
      labourSkillObjList.add( labourSkill4 );

   }

}
