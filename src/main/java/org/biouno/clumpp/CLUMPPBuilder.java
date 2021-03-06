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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.AbortException;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;

/**
 * CLUMPP builder.
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class CLUMPPBuilder extends Builder {
	private final String clumppInstallationName;
	// populations
	private final String populationDatafile;
	private final String numberOfPopulations;
	private final String numberOfPopulationsRuns;
	private final String populationsOutfile;
	// individuals
	private final Boolean individual;
	private final String individualDatafile;
	private final String numberOfIndividuals;
	private final String numberOfIndividualsRuns;
	private final String individualsOutputFile;
	// general
	private final String miscfile;
	private final String numberOfClusters; // or populations, or K
	private final String method;
	private final String weight;
	private final String similarityStatistic;
	// additional options for greedy algorithm
	private final String greedyOption;
	private final String repeats;
	private final String permutationFile;
	// optional outputs
	private final String printPermutedData;
	private final String permutedDatafile;
	private final String printEveryPermutationTested;
	private final String everyPermutationTestedFile;
	private final String printRandomInputOrder;
	private final String randomInputOrderFile;
	// advanced options
	private final String overrideWarnings;
	private final String orderByRun;
	
	@Extension
	public static final CLUMPPBuilderDescriptor DESCRIPTOR = new CLUMPPBuilderDescriptor();
	private static final String POPULATIONS_PARAMFILE_NAME = "paramfile_population";
	private static final String INDIVIDUALS_PARAMFILE_NAME = "paramfile_individual";
	
	enum AnalysisType {
		POPULATIONS(1), 
		INDIVIDUALS(0);
		
		int value;
		
		AnalysisType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	@DataBoundConstructor
	public CLUMPPBuilder(
			String clumppInstallationName, 
			String populationDatafile, 
			String numberOfPopulations, 
			String numberOfPopulationsRuns,
			String populationsOutfile,
			Boolean individual, 
			String individualDatafile,
			String numberOfIndividuals,
			String numberOfIndividualsRuns, 
			String individualsOutputFile,
			String miscfile,
			String numberOfClusters, 
			String method, 
			String weight,
			String similarityStatistic, 
			String greedyOption, 
			String repeats,
			String permutationFile, 
			String printPermutedData,
			String permutedDatafile, 
			String printEveryPermutationTested,
			String everyPermutationTestedFile, 
			String printRandomInputOrder, 
			String randomInputOrderFile, 
			String overrideWarnings, 
			String orderByRun) {
		super();
		this.clumppInstallationName = clumppInstallationName;
		this.populationDatafile = populationDatafile;
		this.numberOfPopulations = numberOfPopulations;
		this.numberOfPopulationsRuns = numberOfPopulationsRuns;
		this.populationsOutfile = populationsOutfile;
		this.individual = individual;
		this.individualDatafile = individualDatafile;
		this.numberOfIndividuals = numberOfIndividuals;
		this.numberOfIndividualsRuns = numberOfIndividualsRuns;
		this.individualsOutputFile = individualsOutputFile;
		this.miscfile = miscfile;
		this.numberOfClusters = numberOfClusters;
		this.method = method;
		this.weight = weight;
		this.similarityStatistic = similarityStatistic;
		this.greedyOption = greedyOption;
		this.repeats = repeats;
		this.permutationFile = permutationFile;
		this.printPermutedData = printPermutedData;
		this.permutedDatafile = permutedDatafile;
		this.printEveryPermutationTested = printEveryPermutationTested;
		this.everyPermutationTestedFile = everyPermutationTestedFile;
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
	 * @return the populationsOutfile
	 */
	public String getPopulationsOutfile() {
		return populationsOutfile;
	}

	/**
	 * @return the individualsOutputFile
	 */
	public String getIndividualsOutputFile() {
		return individualsOutputFile;
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
	public String getNumberOfClusters() {
		return numberOfClusters;
	}

	/**
	 * @return the numberOfIndividuals
	 */
	public String getNumberOfIndividuals() {
		return numberOfIndividuals;
	}
	
	public String getNumberOfPopulations() {
		return numberOfPopulations;
	}

	/**
	 * @return the numberOfPopulationsRuns
	 */
	public String getNumberOfPopulationsRuns() {
		return numberOfPopulationsRuns;
	}

	/**
	 * @return the numberOfIndividualsRuns
	 */
	public String getNumberOfIndividualsRuns() {
		return numberOfIndividualsRuns;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @return the similarityStatistic
	 */
	public String getSimilarityStatistic() {
		return similarityStatistic;
	}

	/**
	 * @return the greedyOption
	 */
	public String getGreedyOption() {
		return greedyOption;
	}

	/**
	 * @return the repeats
	 */
	public String getRepeats() {
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
	public String getPrintPermutedData() {
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
	public String getPrintEveryPermutationTested() {
		return printEveryPermutationTested;
	}
	
	/**
	 * @return the everyPermutationTestedFile
	 */
	public String getEveryPermutationTestedFile() {
		return everyPermutationTestedFile;
	}

	/**
	 * @return the printRandomInputOrder
	 */
	public String getPrintRandomInputOrder() {
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
	public String getOverrideWarnings() {
		return overrideWarnings;
	}

	/**
	 * @return the orderByRun
	 */
	public String getOrderByRun() {
		return orderByRun;
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		
		listener.getLogger().println("CLUMPP builder.");
		
		final EnvVars envVars = build.getEnvironment(listener);
		envVars.overrideAll(build.getBuildVariables());
		
		// Get the CLUMPP installation used
		final CLUMPPInstallation clumppInstallation = DESCRIPTOR.getInstallationByName(this.clumppInstallationName);
		if (clumppInstallation == null) {
			throw new AbortException("Invalid CLUMPP installation");
		}
		
		final FilePath workspace = build.getWorkspace();
		final Map<String, String> env = build.getEnvironment(listener);
		
		// Spit out the mainparams for populations
		listener.getLogger().println("Running CLUMPP for populations");
		String paramfilePopuplationsContent = this.getPopulationsParamfileContent(workspace);
		paramfilePopuplationsContent = Util.replaceMacro(paramfilePopuplationsContent, env);
		FilePath paramfilePopulations = new FilePath(workspace, POPULATIONS_PARAMFILE_NAME);
		listener.getLogger().println("Writing paramfile");
		paramfilePopulations.write(paramfilePopuplationsContent, "UTF-8");
		
		// run CLUMPP
		ArgumentListBuilder args = new ArgumentListBuilder();
		args.add(clumppInstallation.getPathToCLUMPP());
		args.add(paramfilePopulations.getRemote());
		listener.getLogger().println("Executing CLUMPP. Command args: " + args.toStringWithQuote());
		Integer exitCode = launcher.launch().cmds(args).envs(env).stdout(listener).pwd(build.getModuleRoot()).join();
		
		if (this.getIndividual()) {
			// Spit out the mainparams for individuals
			listener.getLogger().println("Running CLUMPP for individuals");
			String paramfileIndividualsContent = this.getIndividualsParamfileContent(workspace);
			paramfileIndividualsContent = Util.replaceMacro(paramfileIndividualsContent, env);
			FilePath paramfileIndividuals = new FilePath(workspace, INDIVIDUALS_PARAMFILE_NAME);
			listener.getLogger().println("Writing paramfile");
			paramfileIndividuals.write(paramfileIndividualsContent, "UTF-8");
			
			// run CLUMPP
			args = new ArgumentListBuilder();
			args.add(clumppInstallation.getPathToCLUMPP());
			args.add(paramfileIndividuals.getRemote());
			listener.getLogger().println("Executing CLUMPP. Commands args: " + args.toStringWithQuote());
			exitCode = launcher.launch().cmds(args).envs(env).stdout(listener).pwd(build.getModuleRoot()).join();
		}
		
		// collect results
		if (exitCode != 0) {
			listener.getLogger().println("Error executing CLUMPP. Exit code: " + exitCode);
			return Boolean.FALSE;
		} else {
			
			return Boolean.TRUE;
		}
	}
	
	private String getPopulationsParamfileContent(FilePath workspace) {
		String delta = "POPFILE " + this.getPopulationDatafile() + "\n";
		return this.getBaseParamFile(delta, AnalysisType.POPULATIONS);
	}
	
	private String getIndividualsParamfileContent(FilePath workspace) {
		String delta = "INDFILE " + this.getIndividualDatafile() + "\n";
		return this.getBaseParamFile( delta, AnalysisType.INDIVIDUALS);
	}

	public String getBaseParamFile(String delta, AnalysisType analysisType) {
		StringBuilder params = new StringBuilder();
		params.append("DATATYPE " + analysisType.getValue() + "\n"); // 1 is population, 0 individual
		params.append(delta);
		if (StringUtils.isNotBlank(this.getMiscfile())) params.append("MISCFILE " + this.getMiscfile() + "\n");
		params.append("K " + this.getNumberOfClusters() + "\n");
		if (analysisType == AnalysisType.INDIVIDUALS) {
			params.append("OUTFILE " + this.getIndividualsOutputFile() + ".indivq" + "\n");
			params.append("C " + this.getNumberOfIndividuals() + "\n");
			params.append("R " + this.getNumberOfIndividualsRuns() + "\n");
		} else if (analysisType == AnalysisType.POPULATIONS) {
			params.append("OUTFILE " + this.getPopulationsOutfile() + ".indivq" + "\n");
			params.append("C " + this.getNumberOfPopulations() + "\n");
			params.append("R " + this.getNumberOfPopulationsRuns() + "\n");
		} else {
			throw new RuntimeException("Invalid analysis type: " + analysisType);
		}
		params.append("M " + this.getMethod() + "\n");
		params.append("W " + this.getWeight() + "\n");
		params.append("S " + this.getSimilarityStatistic() + "\n");
		// additional
		params.append("GREEDY_OPTION " + this.getGreedyOption() + "\n");
		params.append("REPEATS " + this.getRepeats() + "\n");
		if (StringUtils.isNotBlank(this.getPermutationFile())) params.append("PERMUTATIONFILE " + this.getPermutationFile() + "\n");
		// optional
		params.append("PRINT_PERMUTED_DATA " + this.getPrintPermutedData() + "\n");
		if (StringUtils.isNotBlank(this.getPermutedDatafile())) params.append("PERMUTED_DATAFILE " + this.getPermutedDatafile() + "\n");
		if (null != this.getPrintEveryPermutationTested()) params.append("PRINT_EVERY_PERM " + this.getPrintEveryPermutationTested() + "\n");
		if (null != this.getEveryPermutationTestedFile()) params.append("PERMUTATIONFILE " + this.getEveryPermutationTestedFile() + "\n"); 
		params.append("PRINT_RANDOM_INPUTORDER " + this.getPrintRandomInputOrder() + "\n");
		params.append("RANDOM_INPUTORDERFILE " + this.getRandomInputOrderFile() + "\n");
		// advanced
		params.append("OVERRIDE_WARNINGS " + this.getOverrideWarnings() + "\n");
		params.append("ORDER_BY_RUN " + this.getOrderByRun() + "\n");
		return params.toString();
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
