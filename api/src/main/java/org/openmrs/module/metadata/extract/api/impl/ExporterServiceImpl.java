/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.metadata.extract.api.impl;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptSet;
import org.openmrs.OpenmrsObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.metadata.extract.api.ExporterService;
import org.openmrs.module.metadata.extract.api.concept.ConceptLineExporter;
import org.openmrs.module.metadata.extract.api.concept.ConceptNumericExporter;
import org.openmrs.module.metadata.extract.api.export.BaseLineExporter;
import org.openmrs.module.metadata.extract.api.export.CsvExporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;

public class ExporterServiceImpl implements ExporterService {
	
	@Override
	public <T extends OpenmrsObject> void export(Collection<T> instances, List<BaseLineExporter<T>> chain, Domain domain,
	        File outDir, String fileName) throws IOException {
		new CsvExporter<>(chain, domain).writeCsv(instances, outDir, fileName);
	}
	
	@Override
	public void exportConcepts(Collection<Concept> seeds, File outDir, String fileName) throws IOException {
		Collection<Concept> concepts;
		if (seeds == null || seeds.isEmpty()) {
			concepts = Context.getConceptService().getAllConcepts();
		} else {
			concepts = collectClosure(seeds);
		}
		
		List<BaseLineExporter<Concept>> chain = Arrays.asList(new ConceptLineExporter(), new ConceptNumericExporter());
		export(concepts, chain, Domain.CONCEPTS, outDir, fileName);
	}
	
	private Collection<Concept> collectClosure(Collection<Concept> seeds) {
		LinkedHashMap<String, Concept> visited = new LinkedHashMap<>();
		Deque<Concept> queue = new ArrayDeque<>(seeds);
		
		while (!queue.isEmpty()) {
			Concept concept = queue.poll();
			if (concept == null || visited.containsKey(concept.getUuid())) {
				continue;
			}
			visited.put(concept.getUuid(), concept);
			
			for (ConceptAnswer answer : concept.getAnswers()) {
				if (answer.getAnswerConcept() != null) {
					queue.add(answer.getAnswerConcept());
				}
			}
			for (ConceptSet member : concept.getConceptSets()) {
				if (member.getConcept() != null) {
					queue.add(member.getConcept());
				}
			}
		}
		return visited.values();
	}
}
