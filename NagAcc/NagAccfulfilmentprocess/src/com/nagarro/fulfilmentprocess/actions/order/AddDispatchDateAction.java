package com.nagarro.fulfilmentprocess.actions.order;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.DateUtil;

import java.util.Comparator;
import java.util.Objects;


public class AddDispatchDateAction extends AbstractProceduralAction<OrderProcessModel>
{
	@Override
	public void executeAction(OrderProcessModel orderProcessModel) throws Exception
	{
		var max = orderProcessModel.getOrder().getEntries()
				.stream()
				.filter(entry -> Objects.nonNull(entry.getSeller()))
				.map(entry -> entry.getSeller().getLeadTime())
				.max(Comparator.comparingInt(Integer::intValue))
				.orElse(NumberUtils.INTEGER_ZERO);
		orderProcessModel.getOrder().setDispatchDate(DateUtils.addDays(DateUtil.now(), max));
		getModelService().save(orderProcessModel.getOrder());
	}
}
