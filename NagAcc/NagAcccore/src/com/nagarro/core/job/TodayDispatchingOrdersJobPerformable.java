package com.nagarro.core.job;

import com.nagarro.core.constants.NagAccCoreConstants;
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
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class TodayDispatchingOrdersJobPerformable extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TodayDispatchingOrdersJobPerformable.class);
	private static final String FORMATTED_TODAY_DATE = "formattedTodayDate";
	private static final String FORMATTED_TOMORROW_DATE = "formattedTomorrowDate";
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
						LOGGER.info("{} orders found for site {} that are dispatching today", orderModels.size(),
								baseSiteModel.getUid());
						var cmsSiteModel = (CMSSiteModel) baseSiteModel;
						cmsSiteModel.setTodayDispatchingOrders(orderModels);
						modelService.save(cmsSiteModel);
					}
				});

		return performResult;
	}

	private List<OrderModel> findOrdersDispatchingToday()
	{
		var query = "select {" + OrderModel.PK + "} " +
				"from {" + OrderModel._TYPECODE + "} " +
				"where {" + OrderModel.DISPATCHDATE + "}>=?" + FORMATTED_TODAY_DATE
				+ " and {" + OrderModel.DISPATCHDATE + "}<?" + FORMATTED_TOMORROW_DATE;
		var fQuery = new FlexibleSearchQuery(query);
		DateFormat dateFormat = new SimpleDateFormat(NagAccCoreConstants.SHORT_DATE_PATTERN);
		Date todayDate = new Date();
		String formattedTodayDate = dateFormat.format(todayDate);
		String formattedTomorrowDate = dateFormat.format(DateUtils.addDays(todayDate, 1));
		fQuery.addQueryParameter(FORMATTED_TODAY_DATE, formattedTodayDate);
		fQuery.addQueryParameter(FORMATTED_TOMORROW_DATE, formattedTomorrowDate);
		List<OrderModel> orderModels = flexibleSearchService.<OrderModel> search(fQuery).getResult();
		LOGGER.info("{} orders found with dispatch date as {}", orderModels.size(), formattedTodayDate);
		return orderModels;
	}

	@Override
	public boolean isAbortable()
	{
		return Boolean.TRUE;
	}
}
