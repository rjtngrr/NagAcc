package com.nagarro.facades.populators;

import com.nagarro.core.model.SellerModel;
import com.nagarro.facades.seller.dto.SellerData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;


public class NagarroOrderEntryPopulator
		implements Populator<AbstractOrderEntryModel, OrderEntryData>
{
	@Resource
	private Converter<SellerModel, SellerData> sellerConverter;

	@Override
	public void populate(AbstractOrderEntryModel source, OrderEntryData target)
	{
		addSellerDetails(source.getSeller(), target);
	}

	private void addSellerDetails(SellerModel seller, OrderEntryData target)
	{
		target.setSeller(sellerConverter.convert(seller));
	}
}
