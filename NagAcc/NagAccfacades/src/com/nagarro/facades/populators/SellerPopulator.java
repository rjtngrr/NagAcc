package com.nagarro.facades.populators;

import com.nagarro.core.model.SellerModel;
import com.nagarro.facades.seller.dto.SellerData;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;
import java.util.Optional;


public class SellerPopulator implements Populator<SellerModel, SellerData>
{
	@Resource
	private Converter<MediaModel, MediaData> mediaModelConverter;

	@Override
	public void populate(SellerModel sellerModel, SellerData sellerData) throws ConversionException
	{
		sellerData.setSellerName(sellerModel.getSellerName());
		sellerData.setSellerBanner(
				Optional.ofNullable(sellerModel.getSellerBanner()).map(mediaModelConverter::convert).orElse(null));
		sellerData.setLeadTime(sellerModel.getLeadTime().toString());
	}
}
