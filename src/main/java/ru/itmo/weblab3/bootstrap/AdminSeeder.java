package ru.itmo.weblab3.bootstrap;

import ru.itmo.weblab3.config.ConfigKeys;
import ru.itmo.weblab3.config.ConfigProvider;
import ru.itmo.weblab3.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class AdminSeeder {
    private static final Logger LOG = Logger.getLogger(AdminSeeder.class.getName());

    @Inject
    UserService userService;

    void onStart(@Observes Startup event) {
        String username = ConfigProvider.getOrDefault(ConfigKeys.ADMIN_USERNAME, null);
        String password = ConfigProvider.getOrDefault(ConfigKeys.ADMIN_PASSWORD, null);
        String email = ConfigProvider.getOrDefault(ConfigKeys.ADMIN_EMAIL, null);

        if (username == null || password == null || email == null) {
            LOG.info("Admin seeding skipped: ADMIN_* not set");
            return;
        }
        try {
            userService.createAdminIfNotExists(username, email, password);
            LOG.info("Admin user ensured: " + username);
        } catch (Exception ex) {
            LOG.warning("Admin seeding failed: " + ex.getMessage());
        }
    }
}