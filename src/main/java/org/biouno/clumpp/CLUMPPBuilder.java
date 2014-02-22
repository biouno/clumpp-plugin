/*
 * The MIT License
 *
 * Copyright (c) <2014> <Bruno P. Kinoshita>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.biouno.clumpp;

import java.io.IOException;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.tasks.Builder;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * CLUMPP builder.
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class CLUMPPBuilder extends Builder {
	private final String clumppInstallationName;
	// main params
	private final Boolean individual;
	private final String individualDatafile;
	private final String populationDatafile;
	private final String outfile;
	private final String miscfile;
	private final Integer numberOfClusters; // or populations, or K
	private final Integer numberOfIndividuals;
	private final Integer numberOfRuns;
	private final Integer method;
	private final Integer weight;
	private final Integer similarityStatistic;
	// additional options for greedy algorithm
	private final Integer greedyOption;
	private final Long repeats;
	private final String permutationFile;
	// optional outputs
	private final Integer printPermutedData;
	private final String permutedDatafile;
	private final Integer printEveryPermutationTested;
	private final Integer printRandomInputOrder;
	private final String randomInputOrderFile;
	// advanced options
	private final Integer overrideWarnings;
	private final Integer orderByRun;
	
	@Extension
	public static final CLUMPPBuilderDescriptor DESCRIPTOR = new CLUMPPBuilderDescriptor();
	
	@DataBoundConstructor
	public CLUMPPBuilder(String clumppInstallationName, Boolean individual, String individualDatafile,
			String populationDatafile, String outfile, String miscfile,
			Integer numberOfClusters, Integer numberOfIndividuals,
			Integer numberOfRuns, Integer method, Integer weight,
			Integer similarityStatistic, Integer greedyOption, Long repeats,
			String permutationFile, Integer printPermutedData,
			String permutedDatafile, Integer printEveryPermutationTested,
			Integer printRandomInputOrder, String randomInputOrderFile,
			Integer overrideWarnings, Integer orderByRun) {
		super();
		this.clumppInstallationName = clumppInstallationName;
		this.individual = individual;
		this.individualDatafile = individualDatafile;
		this.populationDatafile = populationDatafile;
		this.outfile = outfile;
		this.miscfile = miscfile;
		this.numberOfClusters = numberOfClusters;
		this.numberOfIndividuals = numberOfIndividuals;
		this.numberOfRuns = numberOfRuns;
		this.method = method;
		this.weight = weight;
		this.similarityStatistic = similarityStatistic;
		this.greedyOption = greedyOption;
		this.repeats = repeats;
		this.permutationFile = permutationFile;
		this.printPermutedData = printPermutedData;
		this.permutedDatafile = permutedDatafile;
		this.printEveryPermutationTested = printEveryPermutationTested;
		this.printRandomInputOrder = printRandomInputOrder;
		this.randomInputOrderFile = randomInputOrderFile;
		this.overrideWarnings = overrideWarnings;
		this.orderByRun = orderByRun;
	}


	/**
	 * @return the individual
	 */
	public Boolean getIndividual() {
		return individual;
	}

	/**
	 * @return the individualDatafile
	 */
	public String getIndividualDatafile() {
		return individualDatafile;
	}

	/**
	 * @return the populationDatafile
	 */
	public String getPopulationDatafile() {
		return populationDatafile;
	}

	/**
	 * @return the outfile
	 */
	public String getOutfile() {
		return outfile;
	}

	/**
	 * @return the miscfile
	 */
	public String getMiscfile() {
		return miscfile;
	}

	/**
	 * @return the numberOfClusters
	 */
	public Integer getNumberOfClusters() {
		return numberOfClusters;
	}

	/**
	 * @return the numberOfIndividuals
	 */
	public Integer getNumberOfIndividuals() {
		return numberOfIndividuals;
	}

	/**
	 * @return the numberOfRuns
	 */
	public Integer getNumberOfRuns() {
		return numberOfRuns;
	}

	/**
	 * @return the method
	 */
	public Integer getMethod() {
		return method;
	}

	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * @return the similarityStatistic
	 */
	public Integer getSimilarityStatistic() {
		return similarityStatistic;
	}

	/**
	 * @return the greedyOption
	 */
	public Integer getGreedyOption() {
		return greedyOption;
	}

	/**
	 * @return the repeats
	 */
	public Long getRepeats() {
		return repeats;
	}

	/**
	 * @return the permutationFile
	 */
	public String getPermutationFile() {
		return permutationFile;
	}

	/**
	 * @return the printPermutedData
	 */
	public Integer getPrintPermutedData() {
		return printPermutedData;
	}

	/**
	 * @return the permutedDatafile
	 */
	public String getPermutedDatafile() {
		return permutedDatafile;
	}

	/**
	 * @return the printEveryPermutationTested
	 */
	public Integer getPrintEveryPermutationTested() {
		return printEveryPermutationTested;
	}

	/**
	 * @return the printRandomInputOrder
	 */
	public Integer getPrintRandomInputOrder() {
		return printRandomInputOrder;
	}

	/**
	 * @return the randomInputOrderFile
	 */
	public String getRandomInputOrderFile() {
		return randomInputOrderFile;
	}

	/**
	 * @return the overrideWarnings
	 */
	public Integer getOverrideWarnings() {
		return overrideWarnings;
	}

	/**
	 * @return the orderByRun
	 */
	public Integer getOrderByRun() {
		return orderByRun;
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		
		listener.getLogger().println("CLUMPP builder.");
		
		// Spit out the mainparams
		
		// run CLUMPP
		
		// collect results
		
		// add action
		
		return Boolean.TRUE;
	}
	
	@Override
	public Descriptor<Builder> getDescriptor() {
		return (CLUMPPBuilderDescriptor) super.getDescriptor();
	}

	/**
	 * @return the clumppInstallationName
	 */
	public String getClumppInstallationName() {
		return clumppInstallationName;
	}

}
