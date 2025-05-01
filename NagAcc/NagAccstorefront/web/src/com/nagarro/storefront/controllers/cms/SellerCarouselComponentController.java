package com.nagarro.storefront.controllers.cms;

import com.nagarro.core.model.SellerCarouselComponentModel;
import com.nagarro.core.model.SellerModel;
import com.nagarro.facades.seller.dto.SellerData;
import com.nagarro.storefront.controllers.ControllerConstants;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller("SellerCarouselComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.SellerCarouselComponent)

public class SellerCarouselComponentController extends AbstractAcceleratorCMSComponentController<SellerCarouselComponentModel>
{
	public static final String FIND_SELLERS_WITH_MEDIA = "Select " +
			SellerModel.PK +
			" from {" +
			SellerModel._TYPECODE +
			"} where {" +
			SellerModel.SELLERBANNER +
			"} is not null";

	public static final String SELLERS = "sellers";
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private Converter<SellerModel, SellerData> sellerConverter;

	@Override
	protected void fillModel(HttpServletRequest request, Model model, SellerCarouselComponentModel component)
	{
		var sellersWithMedia = flexibleSearchService.<SellerModel> search(
				new FlexibleSearchQuery(FIND_SELLERS_WITH_MEDIA)).getResult();
		model.addAttribute(SELLERS, Converters.convertAll(sellersWithMedia, sellerConverter));
	}
}
