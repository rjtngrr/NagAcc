package com.nagarro.initialdata.setup.service;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;


public class NagAccSampleDataImportService extends SampleDataImportService
{
	@Override
	protected void importProductCatalog(final String extensionName, final String productCatalogName)
	{
		super.importProductCatalog(extensionName, productCatalogName);
		// Load Sellers
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/sellers.impex", extensionName,
						productCatalogName), false);

	}
}
