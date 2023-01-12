package com.philvigus.dbentityfactories.examples.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.examples.entities.NewUser;
import com.philvigus.dbentityfactories.examples.repositories.NewUserRepository;
import net.datafaker.Faker;

@EntityFactory
public class NewUserFactory extends AbstractBaseEntityFactory<NewUser> {
    private static final Faker faker = new Faker();

    public NewUserFactory(final NewUserRepository repository) {
        super(NewUser.class, repository,
                new DefaultAttribute<>("username", ()-> NewUserFactory.faker.name().username()),
                new DefaultAttribute<>("firstName", () -> NewUserFactory.faker.name().firstName()),
                new DefaultAttribute<>("lastName", () -> NewUserFactory.faker.name().lastName()),
                new DefaultAttribute<>("address", () -> NewUserFactory.faker.address().fullAddress()),
                new DefaultAttribute<>("email", () -> NewUserFactory.faker.internet().emailAddress()),
                new DefaultAttribute<>("age", () -> NewUserFactory.faker.number().numberBetween(18, 90)),
                new DefaultAttribute<>("phoneNumber", () -> NewUserFactory.faker.phoneNumber().cellPhone())
        );
    }
}
