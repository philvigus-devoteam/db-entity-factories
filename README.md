# Database Entity Factories

This library allows you to easily create and persist large numbers of entities with random but logical attributes. It
also allows you to override these properties if you require an attribute to be set to a specific value or generally have
a different set of creation rules.

## Static/Singleton factories

- need to extend @Component to create an @EntityFactory annotation so that factories are managed as beans. This means
  they will automatically be created as singletons without having to do anything else.

## One to many relations/one to one relationships

Parent entity

- Collection of children, nullable or not nullable

Parent repository
Parent factory

- Do we need to check the type of the attribute to make sure it is a collection?

Child entity
Child repository
Child factory


