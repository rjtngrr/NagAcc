package com.nagarro.core.strategies.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class NagAccCommerceAddToCartStrategy extends DefaultCommerceAddToCartStrategy
{
	@Override
	protected void mergeEntry(@Nonnull CommerceCartModification modification, @Nonnull CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("modification", modification);
		if (modification.getEntry() == null || Objects.equals(modification.getEntry().getQuantity(), 0L))
		{
			// nothing to merge
			return;
		}
		ServicesUtil.validateParameterNotNullStandardMessage("parameter", parameter);
		if (parameter.isCreateNewEntry())
		{
			return;
		}
		final AbstractOrderModel cart = modification.getEntry().getOrder();
		if (cart == null)
		{
			// The entry is not in cart (most likely it's a stub)
			return;
		}
		final AbstractOrderEntryModel mergeTarget = getEntryMergeStrategy().getEntryToMerge(cart.getEntries(),
				modification.getEntry());
		if (mergeTarget == null)
		{
			if (parameter.getEntryNumber() != CommerceCartParameter.DEFAULT_ENTRY_NUMBER)
			{
				throw new CommerceCartModificationException(
						"The new entry can not be merged into the entry #" + parameter.getEntryNumber() + ". Give a correct value or "
								+ CommerceCartParameter.DEFAULT_ENTRY_NUMBER + " to accept any suitable entry.");
			}
			modification.getEntry().getProduct().getSellers().stream().findAny().ifPresent(seller -> modification.getEntry().setSeller(seller));
		}
		else
		{
			// Merge the original entry into the merge target and remove the original entry.
			final Map<Integer, Long> entryQuantities = new HashMap<>(NumberUtils.INTEGER_TWO);
			entryQuantities.put(mergeTarget.getEntryNumber(),
					modification.getEntry().getQuantity() + mergeTarget.getQuantity());
			entryQuantities.put(modification.getEntry().getEntryNumber(), NumberUtils.LONG_ZERO);
			getCartService().updateQuantities(parameter.getCart(), entryQuantities);
			modification.setEntry(mergeTarget);
		}

	}
}
