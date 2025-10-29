package de.seuhd.campuscoffee;

import de.seuhd.campuscoffee.domain.model.Pos;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import de.seuhd.campuscoffee.domain.ports.PosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Load initial data into the list via the list service from the business layer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
class LoadInitialData implements InitializingBean {
    private final PosService posService;

    @Override
    public void afterPropertiesSet() {
        log.info("Deleting existing data...");
        posService.clear();
        log.info("Loading initial data...");
        List<Pos> posList = TestFixtures.createPos(posService);
        log.info("Created {} POS.", posList.size());
        log.info("Initial data loaded successfully.");
    }
}