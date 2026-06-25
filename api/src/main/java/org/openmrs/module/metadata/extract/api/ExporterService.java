/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.metadata.extract.api;

import org.openmrs.Concept;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.metadata.extract.api.export.BaseLineExporter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ExporterService {
	
	<T extends OpenmrsObject> void export(Collection<T> instances, List<BaseLineExporter<T>> chain, Domain domain,
	        File outDir, String fileName) throws IOException;
	
	void exportConcepts(Collection<Concept> seeds, File outDir, String fileName) throws IOException;
}
