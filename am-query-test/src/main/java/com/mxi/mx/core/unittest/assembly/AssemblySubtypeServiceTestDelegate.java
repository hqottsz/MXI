
package com.mxi.mx.core.unittest.assembly;

import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.core.dao.assembly.AssemblySubtypeDaoImpl;
import com.mxi.mx.core.dao.assembly.AssemblySubtypeDaoTestStub;
import com.mxi.mx.core.facility.MxDaoFactory;
import com.mxi.mx.core.facility.MxFacilityAliases.DaoAlias;
import com.mxi.mx.core.facility.MxFacilityLocator;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AssemblySubtypeKey;
import com.mxi.mx.core.model.assembly.AssemblySubtype;
import com.mxi.mx.core.services.assembly.AssemblySubtypeService;
import com.mxi.mx.core.services.assembly.AssemblySubtypeServiceImpl;
import com.mxi.mx.core.services.assembly.AssemblySubtypeTO;
import com.mxi.mx.core.services.assembly.CannotDeleteAssemblySubTypeException;
import com.mxi.mx.core.services.assembly.DuplicateAssemblySubtypeCodeException;


/**
 * AssemblySubtypeService Test Delegate.
 *
 * @author hmuradyan
 */
public class AssemblySubtypeServiceTestDelegate {

   private MxDaoFactory iDaoFactory = MxFacilityLocator.getInstance().getDaoFactory();

   private AssemblySubtypeService iSubtypeService;


   /**
    * Creates a new AssemblySubtypeServiceTestDelegate object.
    */
   public AssemblySubtypeServiceTestDelegate() {
      iDaoFactory.setFacility( DaoAlias.ASSEMBLY_SUBTYPE, new AssemblySubtypeDaoTestStub() );

      iSubtypeService = new AssemblySubtypeServiceImpl();
   }


   /**
    * Creates an assembly subtype with specified properties
    *
    * @param aAssemblyKey
    *           assembly key.
    * @param aCode
    *           code of the new assembly subtype.
    * @param aName
    *           name of the new assembly subtype.
    *
    * @return the key of the new assembly subtype.
    *
    * @throws Exception
    *            if something goes wrong
    */
   public AssemblySubtypeKey create( AssemblyKey aAssemblyKey, String aCode, String aName )
         throws Exception {
      AssemblySubtypeTO lSubtypeTo = new AssemblySubtypeTO();
      lSubtypeTo.setCode( aCode );
      lSubtypeTo.setName( aName );

      AssemblySubtypeKey lNewSubtype = iSubtypeService.create( aAssemblyKey, lSubtypeTo );

      return lNewSubtype;
   }


   /**
    * Deletes an assembly subtype.
    *
    * @param aSubtypeKey
    *           the assembly subtype key.
    *
    * @throws CannotDeleteAssemblySubTypeException
    *            if assembly subtype is used by an extraction rule.
    */
   public void delete( AssemblySubtypeKey aSubtypeKey )
         throws CannotDeleteAssemblySubTypeException {
      iSubtypeService.delete( aSubtypeKey );
   }


   /**
    * Retrieves assembly subtype by it's key.
    *
    * @param aSubtypeKey
    *           assembly subtype key.
    *
    * @return assembly subtype.
    */
   public AssemblySubtype get( AssemblySubtypeKey aSubtypeKey ) {
      return iSubtypeService.get( aSubtypeKey );
   }


   /**
    * Sets new code and name to the assembly subtype.
    *
    * @param aSubtypeKey
    *           the key of the assembly subtype.
    * @param aCode
    *           the code of the assembly subtype.
    * @param aName
    *           the name of the assembly subtype.
    *
    * @throws StringTooLongException
    *            if the subtype name or code is too long.
    * @throws MandatoryArgumentException
    *            if the subtype name or code is null or empty.
    * @throws DuplicateAssemblySubtypeCodeException
    *            if the code alreday exists.
    */
   public void set( AssemblySubtypeKey aSubtypeKey, String aCode, String aName )
         throws StringTooLongException, MandatoryArgumentException,
         DuplicateAssemblySubtypeCodeException {
      AssemblySubtypeTO lSubtypeTo = new AssemblySubtypeTO();
      lSubtypeTo.setCode( aCode );
      lSubtypeTo.setName( aName );

      iSubtypeService.set( aSubtypeKey, lSubtypeTo );
   }


   /**
    * Sets new code to the assemblysubtype.
    *
    * @param aSubtypeKey
    *           the key of the assembly subtype.
    * @param aCode
    *           the code of the subtype.
    *
    * @throws StringTooLongException
    *            if the subtype name or code is too long.
    * @throws MandatoryArgumentException
    *            if the subtype name or code is null or empty.
    * @throws DuplicateAssemblySubtypeCodeException
    *            if the code alreday exists.
    */
   public void setCode( AssemblySubtypeKey aSubtypeKey, String aCode )
         throws StringTooLongException, MandatoryArgumentException,
         DuplicateAssemblySubtypeCodeException {
      AssemblySubtype lSubtype = new AssemblySubtypeDaoImpl().get( aSubtypeKey );
      AssemblySubtypeTO lSubtypeTo = new AssemblySubtypeTO();
      lSubtypeTo.setCode( aCode );
      lSubtypeTo.setName( lSubtype.getDescription() );

      iSubtypeService.set( aSubtypeKey, lSubtypeTo );
   }


   /**
    * Sets new name to the assembly subtype.
    *
    * @param aSubtypeKey
    *           the key of the assembly subtype.
    * @param aName
    *           the name of the subtype.
    *
    * @throws StringTooLongException
    *            if the subtype name or code is too long.
    * @throws MandatoryArgumentException
    *            if the subtype name or code is null or empty.
    * @throws DuplicateAssemblySubtypeCodeException
    *            if the code alreday exists.
    */
   public void setName( AssemblySubtypeKey aSubtypeKey, String aName )
         throws StringTooLongException, MandatoryArgumentException,
         DuplicateAssemblySubtypeCodeException {
      AssemblySubtype lSubtype = new AssemblySubtypeDaoImpl().get( aSubtypeKey );
      AssemblySubtypeTO lSubtypeTo = new AssemblySubtypeTO();
      lSubtypeTo.setCode( lSubtype.getCode() );
      lSubtypeTo.setName( aName );

      iSubtypeService.set( aSubtypeKey, lSubtypeTo );
   }
}
