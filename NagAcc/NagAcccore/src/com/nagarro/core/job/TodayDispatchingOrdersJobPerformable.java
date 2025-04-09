package com.nagarro.core.job;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class TodayDispatchingOrdersJobPerformable extends AbstractJobPerformable<CronJobModel>
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;

	@Override
	public PerformResult perform(CronJobModel cronJobModel)
	{
		var performResult = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		findOrdersDispatchingToday().stream().collect(Collectors.groupingBy(OrderModel::getSite))
				.forEach((baseSiteModel, orderModels) -> {
					if (baseSiteModel instanceof CMSSiteModel)
					{
						var cmsSiteModel = (CMSSiteModel) baseSiteModel;
						cmsSiteModel.setTodayDispatchingOrders(orderModels);
						modelService.save(cmsSiteModel);
					}
				});

		return performResult;
	}

	private List<OrderModel> findOrdersDispatchingToday()
	{
		var query = "select {pk} from {order} where {" + OrderModel.DISPATCHDATE + "=?" + OrderModel.DISPATCHDATE;
		var fQuery = new FlexibleSearchQuery(query);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		fQuery.addQueryParameter(OrderModel.DISPATCHDATE, dateFormat.format(new Date()));
		return flexibleSearchService.<OrderModel> search(fQuery).getResult();
	}

	@Override
	public boolean isAbortable()
	{
		return Boolean.TRUE;
	}
}
