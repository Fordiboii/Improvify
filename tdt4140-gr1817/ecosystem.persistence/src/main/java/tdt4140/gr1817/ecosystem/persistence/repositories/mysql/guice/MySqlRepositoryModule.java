package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice;

import com.google.inject.AbstractModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.PeriodPlanRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.TrainerRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlGoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlRestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlUserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlWeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlWorkoutSessionRepository;

public class MySqlRepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        // TODO: add all bindings here
        // any call to todo() just throws an exception

        bind(RestingHeartRateRepository.class).to(MySqlRestingHeartRateRepository.class);
        bind(ServiceProviderPermissionsRepository.class).to(todo());
        bind(ServiceProviderRepository.class).to(todo());
        bind(UserRepository.class).to(MySqlUserRepository.class);
        bind(WeightRepository.class).to(MySqlWeightRepository.class);
        bind(WorkoutSessionRepository.class).to(MySqlWorkoutSessionRepository.class);

        bind(PeriodPlanRepository.class).to(todo());
        bind(TrainerRepository.class).to(todo());
        bind(WorkoutPlanRepository.class).to(todo());
        bind(GoalRepository.class).to(MySqlGoalRepository.class);
    }

    /**
     * Hack to keep guice happy while keeping all needed classes in code.
     *
     * TODO: remove this once all bindings are satisfied.
     * @param <T>
     * @throws UnsupportedOperationException
     */
    private static <T extends Class> T todo() {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
