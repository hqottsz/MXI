package com.mxi.am.api.resource.sys.refterm.partstatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;
import javax.persistence.NonUniqueResultException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.partstatus.impl.PartStatusResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for PartStatusResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class PartStatusResourceBeanTest extends ResourceBeanTest {

   private static final String PART_STATUS_CODE1 = "BUILD";
   private static final String PART_STATUS_NAME1 = "Unapproved Part";
   private static final String PART_STATUS_DESCRIPTION1 =
         "The part has been created but not approved";

   private static final String PART_STATUS_CODE2 = "ACTV";
   private static final String PART_STATUS_NAME2 = "Approved Part";
   private static final String PART_STATUS_DESCRIPTION2 =
         "The part has been approved and can be used for installations";

   private static final String PART_STATUS_CODE3 = "OBSLT";
   private static final String PART_STATUS_NAME3 = "Reject Part";
   private static final String PART_STATUS_DESCRIPTION3 =
         "The part is rejected and cannot be used for installations";

   private static final String PART_STATUS_CODE4 = "BUILD";
   private static final String PART_STATUS_NAME4 = "Unapproved Part1";
   private static final String PART_STATUS_DESCRIPTION4 =
         "The part has been created but not approved yet";

   private static final int PART_STATUS_RECORD_COUNT = 4;
   private static final String INVALID_PART_STATUS_CODE = "BUILDX";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( PartStatusResource.class ).to( PartStatusResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   private PartStatusResourceBean partStatusResourceBean;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      partStatusResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_success() {
      List<PartStatus> PartStatusList = partStatusResourceBean.search();
      assertEquals( "Expected 4 part status found in the database.", PART_STATUS_RECORD_COUNT,
            PartStatusList.size() );
      assertTrue( "Incorrect part status list returned.",
            PartStatusList.containsAll( defaultPartStatusBuilder() ) );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void get_success_validPartStatusCode() throws AmApiResourceNotFoundException {
      PartStatus partStatus = partStatusResourceBean.get( PART_STATUS_CODE2 );
      assertEquals( "Incorrect returned PartStatus: ", defaultPartStatusBuilder().get( 1 ),
            partStatus );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_nonExistPartStatusCode() throws AmApiResourceNotFoundException {
      partStatusResourceBean.get( INVALID_PART_STATUS_CODE );

   }


   @Test( expected = NonUniqueResultException.class )
   public void get_failure_duplicatePartStatusCode() throws AmApiResourceNotFoundException {
      partStatusResourceBean.get( PART_STATUS_CODE1 );

   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_nullPartStatusCode() throws AmApiResourceNotFoundException {
      partStatusResourceBean.get( null );

   }


   private List<PartStatus> defaultPartStatusBuilder() {
      List<PartStatus> partStatusList = new ArrayList<PartStatus>();

      PartStatus partStatus1 = new PartStatus();
      partStatus1.setCode( PART_STATUS_CODE1 );
      partStatus1.setName( PART_STATUS_NAME1 );
      partStatus1.setDescription( PART_STATUS_DESCRIPTION1 );
      partStatusList.add( partStatus1 );

      PartStatus partStatus2 = new PartStatus();
      partStatus2.setCode( PART_STATUS_CODE2 );
      partStatus2.setName( PART_STATUS_NAME2 );
      partStatus2.setDescription( PART_STATUS_DESCRIPTION2 );
      partStatusList.add( partStatus2 );

      PartStatus partStatus3 = new PartStatus();
      partStatus3.setCode( PART_STATUS_CODE3 );
      partStatus3.setName( PART_STATUS_NAME3 );
      partStatus3.setDescription( PART_STATUS_DESCRIPTION3 );
      partStatusList.add( partStatus3 );

      PartStatus partStatus4 = new PartStatus();
      partStatus4.setCode( PART_STATUS_CODE4 );
      partStatus4.setName( PART_STATUS_NAME4 );
      partStatus4.setDescription( PART_STATUS_DESCRIPTION4 );
      partStatusList.add( partStatus4 );

      return partStatusList;
   }

}
