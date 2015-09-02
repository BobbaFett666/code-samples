/*********************************************************************************************************************
 * Modification Log:
 * -------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         Description
 * -------------------------------------------------------------------------------------------------------------------
 * 0.00         Jun 03, 2014    Chris X Genrich     Initial Version
 * -------------------------------------------------------------------------------------------------------------------
 ********************************************************************************************************************/

package com.aexp.gcs.poa.loader;

import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import com.aexp.gcs.poa.dto.Template;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * This class is used for the time being to get the transaction info from SVN.
 * 
 * As soon as we have all this information in DB we are going to delete this
 * class and remove the dependency on template-transaction-info jar from POM.
 * 
 */
@Component
public class TransactionDataLoader {

	private Jaxb2Marshaller marshaller;
	private final LoadingCache<Entry<String, String>, Template> cache;

	@SuppressWarnings("unused")
	private TransactionDataLoader() {
		throw new UnsupportedOperationException("This is private for a reason.  Don't use it.");
	}

	@Autowired
	public TransactionDataLoader(@Qualifier("marshaller") Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
		cache = CacheBuilder.newBuilder().build(new CacheLoader<Entry<String, String>, Template>() {

			@Override
			public Template load(Entry<String, String> key) throws Exception {
				return find(key.getKey(), key.getValue());
			}

		});
	}

	/**
	 * Method will create the Template Object retrieving information based on
	 * Template Id and FeederSystemId
	 * 
	 * @param templateID
	 * @param systemID
	 * @return Template Object
	 * @throws Throwable
	 */
	public Template getTemplate(String templateID, String systemID) throws Throwable {
		try {
			return cache.get(new SimpleEntry<String, String>(templateID, systemID));
		} catch (ExecutionException e) {
			throw e.getCause();
		}
	}

	private Template find(String templateID, String systemID) {
		InputStream input = null;

		String filePath = "/transactionInfo/" + systemID + "/" + templateID
				+ ".xml";
		input = this.getClass().getResourceAsStream(filePath);

		return (Template) marshaller.unmarshal(new StreamSource(input));
	}
}
