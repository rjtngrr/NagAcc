/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.nagarro.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.nagarro.fulfilmentprocess.constants.NagAccFulfilmentProcessConstants;

public class NagAccFulfilmentProcessManager extends GeneratedNagAccFulfilmentProcessManager
{
	public static final NagAccFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NagAccFulfilmentProcessManager) em.getExtension(NagAccFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
