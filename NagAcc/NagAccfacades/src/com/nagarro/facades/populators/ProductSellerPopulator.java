package com.nagarro.facades.populators;

import com.nagarro.core.model.SellerModel;
import com.nagarro.facades.seller.dto.SellerData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;


public class ProductSellerPopulator implements Populator<ProductModel, ProductData>
{
	@Resource
	Converter<SellerModel, SellerData> sellerConverter;

	@Override
	public void populate(ProductModel productModel, ProductData productData) throws ConversionException
	{
		productData.setSellers(sellerConverter.convertAll(productModel.getSellers()));
	}
}
