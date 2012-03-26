/**
 * 
 */
package org.jenkinsci.plugins.managedscripts;

import hudson.Extension;

import java.util.ArrayList;
import java.util.List;

import jenkins.model.Jenkins;

import org.jenkinsci.lib.configprovider.AbstractConfigProviderImpl;
import org.jenkinsci.lib.configprovider.model.Config;
import org.jenkinsci.lib.configprovider.model.ContentType;
import org.jenkinsci.plugins.configfiles.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author domi
 * 
 */
public class ScriptConfig extends Config {

    public final List<Arg> args;

    @DataBoundConstructor
    public ScriptConfig(String id, String name, String comment, String content, List<Arg> args) {
        super(id, name, comment, content);

        if (args != null) {
            List<Arg> filteredArgs = new ArrayList<ScriptConfig.Arg>();
            for (Arg arg : args) {
                if (arg.name != null && arg.name.trim().length() > 0) {
                    filteredArgs.add(arg);
                }
            }
            this.args = filteredArgs;
        } else {
            this.args = null;
        }
    }

    public static class Arg {
        public final String name;

        @DataBoundConstructor
        public Arg(final String name) {
            this.name = name;
        }
    }

    @Extension(ordinal = 70)
    public static class ScriptConfigProvider extends AbstractConfigProviderImpl {

        public ScriptConfigProvider() {
            load();
        }

        @Override
        public ContentType getContentType() {
            return ContentType.DefinedType.HTML;
        }

        @Override
        public String getDisplayName() {
            return Messages.buildstep_provider_name();
        }

        @Override
        public Config newConfig() {
            String id = getProviderId() + System.currentTimeMillis();
            return new ScriptConfig(id, "Build Step", "", "echo \"hello world\"", null);
        }

        @Override
        protected String getXmlFileName() {
            return "buildstep-config-files.xml";
        }

        // ======================
        // start stuff for backward compatibility
        protected transient String ID_PREFIX;

        @Override
        public boolean isResponsibleFor(String configId) {
            return super.isResponsibleFor(configId) || configId.startsWith("ScriptBuildStepConfigProvider.");
        }

        static {
            Jenkins.XSTREAM.alias("org.jenkinsci.plugins.managedscripts.ScriptBuildStepConfigProvider", ScriptConfigProvider.class);
        }
        // end stuff for backward compatibility
        // ======================

    }

}
