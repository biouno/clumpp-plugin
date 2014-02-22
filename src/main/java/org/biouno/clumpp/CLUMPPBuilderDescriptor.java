package org.biouno.clumpp;

import hudson.CopyOnWrite;
import hudson.model.Descriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class CLUMPPBuilderDescriptor  extends Descriptor<Builder> {
	public final Class<CLUMPPBuilder> builderType = CLUMPPBuilder.class;
	private static final String DISPLAY_NAME = "Invoke CLUMPP";
	@CopyOnWrite
	private volatile CLUMPPInstallation[] installations = new CLUMPPInstallation[0];
	/**
	 * No args constructor to ensure the descriptor pattern.
	 */
	public CLUMPPBuilderDescriptor() {
		super(CLUMPPBuilder.class);
		load();
	}
	/* (non-Javadoc)
	 * @see hudson.model.Descriptor#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return DISPLAY_NAME;
	}
	public CLUMPPInstallation[] getInstallations() {
		return this.installations;
	}
	public CLUMPPInstallation getInstallationByName(String name) {
		CLUMPPInstallation found = null;
		for(CLUMPPInstallation installation : this.installations) {
			if (StringUtils.isNotEmpty(installation.getName())) {
				if(name.equals(installation.getName())) {
					found = installation;
					break;
				}
			}
		}
		return found;
	}
	/* (non-Javadoc)
	 * @see hudson.model.Descriptor#configure(org.kohsuke.stapler.StaplerRequest, net.sf.json.JSONObject)
	 */
	@Override
	public boolean configure(StaplerRequest req, JSONObject json)
			throws hudson.model.Descriptor.FormException {
		this.installations = req.bindParametersToList(CLUMPPInstallation.class, "CLUMPP.").toArray(new CLUMPPInstallation[0]);
		save();
		return Boolean.TRUE;
	}
	@Override
	public Builder newInstance(StaplerRequest arg0, JSONObject arg1)
			throws hudson.model.Descriptor.FormException {
		return super.newInstance(arg0, arg1);
	}
	/**
	 * Validates required fields.
	 * @param value the value
	 * @return FormValidation
	 */
	public FormValidation doRequired(@QueryParameter String value) {
		FormValidation returnValue = FormValidation.ok();
		if(StringUtils.isBlank(value)) {
			returnValue = FormValidation.error("This parameter is required");
		}
		return returnValue;
	}
	/**
	 * Validates required long fields.
	 * @param value the value
	 * @return FormValidation
	 */
	public FormValidation doLongRequired(@QueryParameter String value) {
		FormValidation returnValue = FormValidation.ok();
		if(StringUtils.isNotBlank(value)) {
			try {
				Long.parseLong(value);
			} catch ( NumberFormatException nfe ) {
				returnValue = FormValidation.error("This value must be an integer");
			}
		}
		return returnValue;
	}
	/**
	 * Validates required double fields.
	 * @param value the value
	 * @return FormValidation
	 */
	public FormValidation doDoubleRequired(@QueryParameter String value) {
		FormValidation returnValue = FormValidation.ok();
		if(StringUtils.isNotBlank(value)) {
			try {
				Double.parseDouble(value);
			} catch ( NumberFormatException nfe ) {
				returnValue = FormValidation.error("This value must be an float");
			}
		}
		return returnValue;
	}

}
