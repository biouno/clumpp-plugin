package org.biouno.clumpp;

import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

public class CLUMPPInstallation implements Serializable {

	private static final long serialVersionUID = 5602439696929031402L;
	private final String name;
	private final String pathToCLUMPP;
	@DataBoundConstructor
	public CLUMPPInstallation(String name, String pathToCLUMPP) {
		this.name = name;
		this.pathToCLUMPP = pathToCLUMPP;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the pathToCLUMPP
	 */
	public String getPathToCLUMPP() {
		return pathToCLUMPP;
	}
	
}
