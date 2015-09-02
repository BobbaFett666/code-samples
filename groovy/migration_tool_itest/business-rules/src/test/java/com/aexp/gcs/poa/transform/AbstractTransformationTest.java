package com.aexp.gcs.poa.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.aexp.gcs.schema.sendcorr.ArchivalIndexDetailsType;
import com.aexp.gcs.schema.sendcorr.ArchivalType;
import com.aexp.gcs.schema.sendcorr.ArchivalType.ArchivalSystem;
import com.aexp.gcs.schema.sendcorr.BodyType;
import com.aexp.gcs.schema.sendcorr.BounceBackSORType;
import com.aexp.gcs.schema.sendcorr.BounceBackType;
import com.aexp.gcs.schema.sendcorr.CommunicationInformationType;
import com.aexp.gcs.schema.sendcorr.CommunicationVariablesType;
import com.aexp.gcs.schema.sendcorr.ContactDetailsType;
import com.aexp.gcs.schema.sendcorr.CustomerPIIType;
import com.aexp.gcs.schema.sendcorr.DataAugmentationType;
import com.aexp.gcs.schema.sendcorr.DataAugmentationType.SOR;
import com.aexp.gcs.schema.sendcorr.DeliveryInformationType;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;
import com.aexp.gcs.schema.sendcorr.FeederDetailsType;
import com.aexp.gcs.schema.sendcorr.NameDetailsType;
import com.aexp.gcs.schema.sendcorr.NonRecipientContactDetailsType;
import com.aexp.gcs.schema.sendcorr.OtherFeaturesType;
import com.aexp.gcs.schema.sendcorr.PhysicalAddressType;
import com.aexp.gcs.schema.sendcorr.RecipientContactDetailsType;
import com.aexp.gcs.schema.sendcorr.ReportingType;
import com.aexp.gcs.schema.sendcorr.ReportingType.ReportingSystem;
import com.aexp.gcs.schema.sendcorr.RequestType;
import com.aexp.gcs.schema.sendcorr.ServiceDetailsType;
import com.aexp.gcs.schema.sendcorr.ServiceFeaturesType;

public abstract class AbstractTransformationTest<T extends Transformation, INTERFASE> {

	protected T rule;
	protected INTERFASE interfase;

	@Mock
	protected EnvelopeType mockEnvelopeType;
	@Mock
	protected BodyType mockBodyType;
	@Mock
	protected RequestType mockRequestType;
	@Mock
	protected FeederDetailsType mockFeederDetailsType;
	@Mock
	protected CommunicationInformationType mockCommunicationInformationType;
	@Mock
	protected CommunicationVariablesType mockCommunicationVariablesType;
	@Mock
	protected CustomerPIIType mockCustomerPIIType;
	@Mock
	protected ContactDetailsType mockContactDetailsType;
	@Mock
	protected NameDetailsType mockNameDetailsType;
	@Mock
	protected PhysicalAddressType mockPhysicalAddressType;
	@Mock
	protected ServiceDetailsType mockServiceDetailsType;
	@Mock
	protected NonRecipientContactDetailsType mockNonRecipientContactDetailsType;
	@Mock
	protected RecipientContactDetailsType mockRecipientContactDetailsType;
	@Mock
	protected ServiceFeaturesType mockServiceFeaturesType;
	@Mock
	protected DataAugmentationType mockDataAugmentation;
	@Mock
	protected SOR mockSor;
	@Mock
	protected BounceBackType mockBounceBackType;
	@Mock
	protected BounceBackSORType mockHardBounceBack;
	@Mock
	protected BounceBackSORType mockSoftBounceBack;
	@Mock
	protected ArchivalType mockArchivalType; 
	@Mock
	protected ArchivalSystem mockArchivalSystem; 
	@Mock
	protected ArchivalIndexDetailsType mockArchivalIndexDetailsType;
	@Mock
	protected ReportingType mockReportingType;
	@Mock
	protected ReportingSystem mockReportingSystem;
	@Mock
	protected OtherFeaturesType mockOtherFeaturesType;
	@Mock
	protected DeliveryInformationType mockDelieveryInfomration;
	
	protected abstract void wireInterfaceMockBehaviors(INTERFASE interfase);

	@SuppressWarnings("unchecked")
	protected T instantiateRule() throws InstantiationException,
			IllegalAccessException {
		return ((Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0])
				.newInstance();
	}

	@SuppressWarnings("unchecked")
	protected Class<INTERFASE> getDynamicVariableInterface() {
		return (Class<INTERFASE>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@Before
	public void setUp() throws InstantiationException, IllegalAccessException {
		interfase = mock(getDynamicVariableInterface());
		when(mockEnvelopeType.getBody()).thenReturn(mockBodyType);
		when(mockBodyType.getRequest()).thenReturn(mockRequestType);
		when(mockRequestType.getCommunicationInformation()).thenReturn(
				mockCommunicationInformationType);
		when(mockCommunicationInformationType.getCommunicationVariables())
				.thenReturn(mockCommunicationVariablesType);
		when(
				mockCommunicationVariablesType
						.getDto(getDynamicVariableInterface())).thenReturn(
				interfase);
		when(mockCommunicationInformationType.getCustomerPII()).thenReturn(
				mockCustomerPIIType);
		when(mockCommunicationInformationType.getNonRecipientContactDetails())
				.thenReturn(mockNonRecipientContactDetailsType);
		when(mockNonRecipientContactDetailsType.getContactDetails())
				.thenReturn(Arrays.asList(mockContactDetailsType));
		when(mockCustomerPIIType.getRecipientContactDetails()).thenReturn(
				mockRecipientContactDetailsType);
		when(mockRecipientContactDetailsType.getContactDetails()).thenReturn(
				mockContactDetailsType);
		when(mockContactDetailsType.getNameDetails()).thenReturn(
				mockNameDetailsType);
		when(mockContactDetailsType.getPhysicalAddress()).thenReturn(
				mockPhysicalAddressType);
		when(mockRequestType.getFeederDetails()).thenReturn(
				mockFeederDetailsType);
		when(mockRequestType.getServiceDetails()).thenReturn(
				mockServiceDetailsType);
		when(mockServiceDetailsType.getServiceFeatures()).thenReturn(mockServiceFeaturesType);
		when(mockServiceFeaturesType.getDataAugmentation()).thenReturn(mockDataAugmentation);
		when(mockDataAugmentation.getSOR()).thenReturn(Arrays.asList(mockSor));
		when(mockServiceFeaturesType.getBounceBack()).thenReturn(mockBounceBackType);
		when(mockBounceBackType.getHardBounceBack()).thenReturn(mockHardBounceBack);
		when(mockBounceBackType.getSoftBounceBack()).thenReturn(mockSoftBounceBack);
		when(mockServiceFeaturesType.getArchival()).thenReturn(mockArchivalType);
		when(mockArchivalType.getArchivalSystem()).thenReturn(mockArchivalSystem);
		when(mockArchivalType.getArchivalIndexDetails()).thenReturn(mockArchivalIndexDetailsType);
		when(mockServiceFeaturesType.getReporting()).thenReturn(mockReportingType);
		when(mockReportingType.getReportingSystem()).thenReturn(Arrays.asList(mockReportingSystem));
		when(mockServiceFeaturesType.getOtherFeatures()).thenReturn(mockOtherFeaturesType);
		when(mockCommunicationInformationType.getDeliveryInformation()).thenReturn(mockDelieveryInfomration);
		
		
		rule = instantiateRule();
		wireInterfaceMockBehaviors(interfase);
	}

	@Test
	public void nullEnvelopeTest() {
		mockEnvelopeType = null;
		rule.execute(mockEnvelopeType);
		// not expecting any exceptions
	}

	@Test
	public void nullBodyTest() {
		when(mockEnvelopeType.getBody()).thenReturn(null);
		rule.execute(mockEnvelopeType);
		// not expecting any exceptions
	}

	@Test
	public void nullRequestTest() {
		when(mockBodyType.getRequest()).thenReturn(null);
		rule.execute(mockEnvelopeType);
		// not expecting any exceptions
	}

	@Test
	public void nullServiceDetailsType() {
		when(mockRequestType.getServiceDetails()).thenReturn(null);
		rule.execute(mockEnvelopeType);
		// not expecting any exceptions
	}
	
	@Test
	public void nullServiceFeaturesType() {
		when(mockServiceDetailsType.getServiceFeatures()).thenReturn(null);
		rule.execute(mockEnvelopeType);
		// not expecting any exceptions
	}
	
	@Test
	public void nullDataAugmentationType() {
		when(mockServiceFeaturesType.getDataAugmentation()).thenReturn(null);
		rule.execute(mockEnvelopeType);
		// not expecting any exceptions
	}
	
	@Test
	public void nullGetSOR() {
		when(mockDataAugmentation.getSOR()).thenReturn(null);
		rule.execute(mockEnvelopeType);
		// not expecting any exceptions
	}
	
	@Test
	public void nullGetBounceBackType() {
		when(mockServiceFeaturesType.getBounceBack()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	
	@Test
	public void nullHardBounceBackSORType() {
		when(mockBounceBackType.getHardBounceBack()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullSoftBounceBackSORType() {
		when(mockBounceBackType.getSoftBounceBack()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullArchivalType() {
		when(mockServiceFeaturesType.getArchival()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullArchivalSystem() {
		when(mockArchivalType.getArchivalSystem()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullArchivalIndexDetailsType() {
		when(mockArchivalType.getArchivalIndexDetails()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullIndexDescription() {
		when(mockArchivalIndexDetailsType.getIndexDescription()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullReportingType() {
		when(mockServiceFeaturesType.getReporting()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}

	@Test
	public void nullReportingSystem() {
		when(mockReportingType.getReportingSystem()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullOtherFeaturesType() {
		when(mockServiceFeaturesType.getOtherFeatures()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	
	@Test
	public void nullFeederDetailsType() {
		when(mockRequestType.getFeederDetails()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullCommunicationInformation() {
		when(mockRequestType.getCommunicationInformation()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullDeliveryInformationType() {
		when(mockCommunicationInformationType.getDeliveryInformation()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullCustomerPII() {
		when(mockCommunicationInformationType.getCustomerPII()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullRecipientContactDetails() {
		when(mockCustomerPIIType.getRecipientContactDetails()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullNonRecipientContactDetails() {
		when(mockCommunicationInformationType.getNonRecipientContactDetails()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullRecipientContactNameDetails() {
		when(mockRecipientContactDetailsType.getContactDetails()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
	
	@Test
	public void nullNameDetails() {
		when(mockContactDetailsType.getNameDetails()).thenReturn(null);
		rule.execute(mockEnvelopeType);
	}
}
