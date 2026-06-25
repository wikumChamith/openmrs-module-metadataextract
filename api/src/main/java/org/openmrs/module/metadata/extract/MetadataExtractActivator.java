/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.metadata.extract;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.metadata.extract.api.impl.ExporterServiceImpl;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.PrivilegeConstants;

import java.io.File;

@Slf4j
public class MetadataExtractActivator extends BaseModuleActivator {
	
	@Override
	public void started() {
		File outDir = new File(OpenmrsUtil.getApplicationDataDirectory(), "metadata_extract");
		try {
			Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
			new ExporterServiceImpl().exportConcepts(null, outDir, "concepts.csv");
			log.info("Metadata Extract: exported concepts to {}", outDir.getAbsolutePath());
		}
		catch (Exception e) {
			log.error("Metadata Extract: failed to export concepts on startup", e);
		}
		finally {
			Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
		}
	}
}
