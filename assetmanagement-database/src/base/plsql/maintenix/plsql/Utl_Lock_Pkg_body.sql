--liquibase formatted sql


--changeSet Utl_Lock_Pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   Package:       UtlLockPkg
   Description:   This package is used to manipulate the dbms_lock functions

   Author:        Chris Daley
   Created Date:  May 11th, 2010
*/
CREATE OR REPLACE PACKAGE BODY Utl_Lock_Pkg IS

	/**
		Procedure:	RequestLock

		Arguments:	aLockName   		The name for the lock
                    aResult     		The lock handle

		Description:	Determine the lock handle for the given lock name

      Author:  Cdaley
      Create:  May 12th, 2010
	*/
	PROCEDURE RequestLockHandle(
		aLockName		IN		VARCHAR2,
		aResult			OUT		VARCHAR2
	)
	AS
	BEGIN
		dbms_lock.allocate_unique(aLockName,aResult);
	END RequestLockHandle;

   /**
      Procedure:     RequestLock

      Arguments:     aLockHandle 		The unique lock handle
                     aLockMode   		The lock mode
					 aLockTimeout		The amount of time for the request to wait for timeout
					 aReleaseOnCommit	If the lock should release automagically when the transaction commits
                     aResult     		The result of the request

      Description:   Request a lock for the given lock name in the specified mode

      Author:  Cdaley
      Create:  May 11th, 2010
   */
   PROCEDURE RequestLock(
      aLockHandle   	IN    	VARCHAR2,
      aLockMode   		IN    	NUMBER,
	  aLockTimeout		IN		NUMBER,
	  aReleaseOnCommit	IN		BOOLEAN,
      aResult     		OUT   	NUMBER)
   AS
   BEGIN
      aResult := dbms_lock.request(aLockHandle, aLockMode, aLockTimeout, aReleaseOnCommit);
   END RequestLock;

   /**
      Procedure:     RequestLock

      Arguments:     aLockHandle The unique lock handle
                     aLockMode   The lock mode
                     aResult     The result of the request

      Description:   Request a lock for the given lock handle

      Author:  Cdaley
      Create:  May 11th, 2010
   */
   PROCEDURE RequestLock(
      aLockHandle   	IN    	VARCHAR2,
      aLockMode   		IN    	NUMBER,
      aResult     		OUT   	NUMBER)
   AS
   BEGIN
      aResult := dbms_lock.request(aLockHandle, aLockMode, 0, FALSE);

   END RequestLock;


   /**
      Procedure:     ReleaseLock

      Arguments:     aLockHandle    The lock handle
                     aResult        The result of the release

      Description:   Release the specified lock

      Author:  Cdaley
      Create:  May 11th, 2010
   */
   PROCEDURE ReleaseLock(
      aLockHandle   IN  VARCHAR2,
      aResult       OUT NUMBER)
   AS
   BEGIN
      aResult := dbms_lock.release(aLockHandle);

   END ReleaseLock;

END Utl_Lock_Pkg;
/