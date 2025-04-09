/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.nagarro.core.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.nagarro.core.constants.NagAccCoreConstants;
import com.nagarro.core.setup.CoreSystemSetup;


/**
 * Do not use, please use {@link CoreSystemSetup} instead.
 * 
 */
public class NagAccCoreManager extends GeneratedNagAccCoreManager
{
	public static final NagAccCoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NagAccCoreManager) em.getExtension(NagAccCoreConstants.EXTENSIONNAME);
	}
}
