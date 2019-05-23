package com.mxi.am.api.resource.sys.refterm.faultsource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBContext;

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
import com.mxi.am.api.resource.sys.refterm.faultsource.impl.FaultSourceResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for FaultSource Resource Bean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class FaultSourceResourceBeanTest extends ResourceBeanTest {

   public static final String FAULT_SOURCE_CODE = "PILOT";
   public static final String FAULT_SOURCE_NAME = "Pilot ";
   public static final String FAULT_SOURCE_DESCRIPTION = "Pilot ";
   public static final String SPEC2K_FAULT_SOURCE_CODE = "PL";

   public static final String FAULT_SOURCE_CODE2 = "MECH";
   public static final String FAULT_SOURCE_NAME2 = "Mechanic";
   public static final String FAULT_SOURCE_DESCRIPTION2 = "Mechanic";
   public static final String SPEC2K_FAULT_SOURCE_CODE2 = "ML";

   public static final String FAULT_SOURCE_CODE3 = "MESSAGE";
   public static final String FAULT_SOURCE_NAME3 = "Automated Message System";
   public static final String FAULT_SOURCE_DESCRIPTION3 = "Automated Message System";
   public static final String SPEC2K_FAULT_SOURCE_CODE3 = "PL";

   public static final String FAULT_SOURCE_CODE4 = "CABIN";
   public static final String FAULT_SOURCE_NAME4 = "Cabin Crew";
   public static final String FAULT_SOURCE_DESCRIPTION4 = "Cabin Crew";
   public static final String SPEC2K_FAULT_SOURCE_CODE4 = "CL";

   private static final int FAULT_SOURCE_RECORD_COUNT = 4;
   private static final String NON_EXISTENT_FAULT_SOURCE_CODE = "XXX";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( FaultSourceResource.class ).to( FaultSourceResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   private FaultSourceResourceBean faultSourceResourceBean;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      faultSourceResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetFaultSourceByCodeSuccess() throws AmApiResourceNotFoundException {
      FaultSource faultSource = faultSourceResourceBean.get( FAULT_SOURCE_CODE );
      assertEquals( "Incorrect returned FaultSource: ", defaultFaultSourceBuilder(), faultSource );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllFaultSourceSuccess() {
      List<FaultSource> faultSourceList = faultSourceResourceBean.search();
      assertEquals( "Incorrect number of Fault Source records returned, expected "
            + FAULT_SOURCE_RECORD_COUNT, FAULT_SOURCE_RECORD_COUNT, faultSourceList.size() );
      assertTrue( "Incorrect Fault Source list returned",
            faultSourceList.containsAll( defaultFaultSourceListBuilder() ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetFaultSourceByNonExistentCodeFailure() throws AmApiResourceNotFoundException {
      faultSourceResourceBean.get( NON_EXISTENT_FAULT_SOURCE_CODE );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetFaultSourceByNullCodeFailure() throws AmApiResourceNotFoundException {
      faultSourceResourceBean.get( null );
   }


   private FaultSource defaultFaultSourceBuilder() {
      FaultSource faultSource = new FaultSource();
      faultSource.setCode( FAULT_SOURCE_CODE );
      faultSource.setName( FAULT_SOURCE_NAME );
      faultSource.setDescription( FAULT_SOURCE_DESCRIPTION );
      faultSource.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE );
      return faultSource;
   }


   private Collection<FaultSource> defaultFaultSourceListBuilder() {
      List<FaultSource> faultSourceList = new ArrayList<FaultSource>();

      FaultSource faultSource1 = new FaultSource();
      faultSource1.setCode( FAULT_SOURCE_CODE );
      faultSource1.setName( FAULT_SOURCE_NAME );
      faultSource1.setDescription( FAULT_SOURCE_DESCRIPTION );
      faultSource1.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE );

      FaultSource faultSource2 = new FaultSource();
      faultSource2.setCode( FAULT_SOURCE_CODE2 );
      faultSource2.setName( FAULT_SOURCE_NAME2 );
      faultSource2.setDescription( FAULT_SOURCE_DESCRIPTION2 );
      faultSource2.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE2 );

      FaultSource faultSource3 = new FaultSource();
      faultSource3.setCode( FAULT_SOURCE_CODE3 );
      faultSource3.setName( FAULT_SOURCE_NAME3 );
      faultSource3.setDescription( FAULT_SOURCE_DESCRIPTION3 );
      faultSource3.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE3 );

      FaultSource faultSource4 = new FaultSource();
      faultSource4.setCode( FAULT_SOURCE_CODE4 );
      faultSource4.setName( FAULT_SOURCE_NAME4 );
      faultSource4.setDescription( FAULT_SOURCE_DESCRIPTION4 );
      faultSource4.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE4 );

      faultSourceList.add( faultSource1 );
      faultSourceList.add( faultSource2 );
      faultSourceList.add( faultSource3 );
      faultSourceList.add( faultSource4 );

      return faultSourceList;
   }
}
