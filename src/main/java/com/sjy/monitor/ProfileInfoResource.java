package com.sjy.monitor;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjy.config.DefaultProfileUtil;

/**
 * Resource to return information about the currently running Spring profiles.
 */
@RestController
@RequestMapping("/api")
public class ProfileInfoResource {

	private final Environment env;

	public ProfileInfoResource(Environment env) {
		this.env = env;
	}

	@GetMapping("/profile-info")
	public ProfileInfoVM getActiveProfiles() {
		String[] activeProfiles = DefaultProfileUtil.getActiveProfiles(env);
		return new ProfileInfoVM(activeProfiles);
	}

	class ProfileInfoVM {

		private String[] activeProfiles;

		ProfileInfoVM(String[] activeProfiles) {
			this.activeProfiles = activeProfiles;
		}

		public String[] getActiveProfiles() {
			return activeProfiles;
		}
	}
}
