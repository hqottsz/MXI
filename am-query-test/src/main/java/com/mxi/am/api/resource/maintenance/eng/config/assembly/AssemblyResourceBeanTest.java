package com.mxi.am.api.resource.maintenance.eng.config.assembly;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.impl.AssemblyResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for Assembly core API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class AssemblyResourceBeanTest extends ResourceBeanTest {

   private static final String ASSEMBLY_ID = "17FBB18D95F347A38C8B887ADD4D635E";
   private static final String ASSEMBLY_NAME = "Falcon 2000";
   private static final String ASSEMBLY_CODE = "F-2000";
   private static final String ASSEMBLY_CLASS = "ACFT";
   private static final String NON_EXISTENT_ASSEMBLY_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String INVALID_FORMAT_ASSEMBLY_ID = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Inject
   AssemblyResourceBean assemblyResourceBean;


   @Before
   public void setUp() throws MxException {

      InjectorContainer.get().injectMembers( this );
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testGetAssemblyByIdSuccess() throws AmApiResourceNotFoundException {
      Assembly assembly = assemblyResourceBean.get( ASSEMBLY_ID );
      assertAssemblyObject( getAssemblyObject(), assembly );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetAssemblyByNonExistentId() throws AmApiResourceNotFoundException {
      assemblyResourceBean.get( NON_EXISTENT_ASSEMBLY_ID );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testGetAssemblyByInvalidFormatId() throws AmApiResourceNotFoundException {
      assemblyResourceBean.get( INVALID_FORMAT_ASSEMBLY_ID );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testGetAssemblyByEmptyId() throws AmApiResourceNotFoundException {
      assemblyResourceBean.get( null );
   }


   private void assertAssemblyObject( Assembly expectedAssembly, Assembly actualAssembly ) {
      assertEquals( expectedAssembly.getId(), actualAssembly.getId() );
      assertEquals( expectedAssembly.getName(), actualAssembly.getName() );
      assertEquals( expectedAssembly.getCode(), actualAssembly.getCode() );
      assertEquals( expectedAssembly.getClassCode(), actualAssembly.getClassCode() );
   }


   private Assembly getAssemblyObject() {
      Assembly assembly = new Assembly();
      assembly.setId( ASSEMBLY_ID );
      assembly.setName( ASSEMBLY_NAME );
      assembly.setCode( ASSEMBLY_CODE );
      assembly.setClassCode( ASSEMBLY_CLASS );
      return assembly;
   }

}
