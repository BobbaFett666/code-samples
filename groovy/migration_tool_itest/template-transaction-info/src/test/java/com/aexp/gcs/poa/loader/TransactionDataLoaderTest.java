/*********************************************************************************************************************
 * Modification Log:
 * -------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         Description
 * -------------------------------------------------------------------------------------------------------------------
 * 0.00         Jun 06, 2014    Chris X Genrich     Initial Version
 * -------------------------------------------------------------------------------------------------------------------
 ********************************************************************************************************************/
package com.aexp.gcs.poa.loader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.aexp.gcs.poa.dto.FeederSystem;
import com.aexp.gcs.poa.dto.GenericTemplateDetailsGroup;
import com.aexp.gcs.poa.dto.Template;
import com.aexp.gcs.poa.dto.TemplateDetails;
import com.aexp.gcs.poa.dto.TemplateTransactionalDataList;
import com.aexp.gcs.poa.dto.TransactionalData;

/**
 * Test call for TransactionDataLoader
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionDataLoaderTest {

	@Mock
	private Jaxb2Marshaller marshaller;

	private TransactionDataLoader loader;
	
	private Template template;

	
	/**
	 * This is not the greatest test either.
	 * 
	 * @throws Throwable
	 *             JAXBException hopefully
	 */
	@Test(expected = JAXBException.class)
	public void testGetTransactionInfoException() throws Throwable {
		when(marshaller.unmarshal((Source) any())).thenThrow(
				new UnmarshallingFailureException("test caused error intentionally.", new JAXBException(
						new RuntimeException())));
		try {
			template = loader.getTemplate("ABCABCXY0001001", "ABC");
		} catch (Throwable t) {
			throw t.getCause().getCause();
		}

	}

	@Before
	public void setUp() throws Exception {
		template = new Template();
		loader = new TransactionDataLoader(marshaller);
		template.setFeederSystem(feederSystem());
		template.setTemplateDesc("templateDesc");
		template.setTemplateID("templateID");
		template.setGenericTemplateDetailsGroup(genericTemplateDetailsGroup());
		HashMap<String, TemplateTransactionalDataList> map = new HashMap<String, TemplateTransactionalDataList>();
		map.put("es_ES,PAPER",templateTransactionalDataList());
		template.setSpecificTemplateDetailsGroup(map);
		when(marshaller.unmarshal((Source) any())).thenReturn(template);
	}
	
	private GenericTemplateDetailsGroup genericTemplateDetailsGroup() {

		GenericTemplateDetailsGroup genericTemplateDetailsGroup = new GenericTemplateDetailsGroup();
		TemplateDetails templateDetails = new TemplateDetails();

		templateDetails.setTemplateTransactionalDataList(templateTransactionalDataList());

		return genericTemplateDetailsGroup;

	}

	private TransactionalData createTTD(String name, String value) {
		TransactionalData item = new TransactionalData();
		item.setVariableName(name);
		item.setVariableValue(value);
		return item;
	}

	private FeederSystem feederSystem() {
		FeederSystem ret = new FeederSystem();
		ret.setFeederSystemID("GNA");
		ret.setFeederSystemName("GlobalNA");
		return ret;
	}

	

	private TemplateTransactionalDataList templateTransactionalDataList() {

		TemplateTransactionalDataList templateTransactionalDataList = new TemplateTransactionalDataList();
		templateTransactionalDataList.getTransactionalData().add(createTTD("name", "value"));
		templateTransactionalDataList.getTransactionalData().add(createTTD("projectId", "0"));

		return templateTransactionalDataList;
	}

}
