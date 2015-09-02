/*********************************************************************************************************************

 * Modification Log:

 * -------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         	Description
 * -------------------------------------------------------------------------------------------------------------------
 * 0.00         Sep 27, 2014    Vijay Kumar     		Initial Version
 * -------------------------------------------------------------------------------------------------------------------
 ********************************************************************************************************************/

package com.aexp.gcs.poa.custom.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom Marker Interface for fields moving to Feeder system
 *******************************************************************************/

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented

public @interface DeprecatedPleaseMoveToFeeder {

}
