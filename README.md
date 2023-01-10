# Database Entity Factories

This library allows you to easily create and persist large numbers of entities with random but logical attributes. It
also allows you to override these properties if you require an attribute to be set to a specific value or generally have
a different set of creation rules.

The examples in this repository use the [datafaker](https://www.datafaker.net/) package to create random attributes as required.

Functionality borrows heavily from the implementation of factories in Laravel.

## Simple Entities

For a simple entity with no relationships:

```java
public class BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long myLongAttribute;

    private String myStringAttribute;

    // ...Getters, Setters
}
```

You define a factory as follows:

```java
@EntityFactory
public class BasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public static final String LONG_ATTRIBUTE_NAME = "myLongAttribute";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";
    
    private static final Faker faker = new Faker();

    private final String longAttributeName = "myLongAttribute";
    private final String stringAttributeName = "myStringAttribute";
    
    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository, Map.of(
                BasicEntityFactory.LONG_ATTRIBUTE_NAME,
                new DefaultAttribute<>(BasicEntityFactory.LONG_ATTRIBUTE_NAME, () -> BasicEntityFactory.faker.number().numberBetween(1L, 5L)),
                BasicEntityFactory.STRING_ATTRIBUTE_NAME,
                new DefaultAttribute<>(BasicEntityFactory.STRING_ATTRIBUTE_NAME, () -> BasicEntityFactory.faker.lorem().sentence())
        ));
    }
}
```

Entities can then be made with attributes based on the generators specified in the factory:

```java
public class EntityCreator {
    @Autowired
    BasicEntityFactory basicEntityFactory;

    public void CreateEntities() {
        // create and persist 5 entities to the database
        List<BasicEntity> savedEntities = basicEntityFactory.persist(5);

        // create but don't persist them
        List<BasicEntity> createdEntities = basicEntityFactory.create(5);
    }
}
```

## Overriding Default Attributes
```java
public class EntityCreator {
    @Autowired
    BasicEntityFactory basicEntityFactory;
    
    private final Map<String, CustomAttribute<?>> customAttributes = Map.of(
        customLongName, new CustomAttribute<>(customLongName, () -> 12L),
        customStringName, new CustomAttribute<>(customStringName, () -> "A custom string value")
    );

    public void CreateEntities() {
        List<BasicEntity> basicEntity = basicEntityFactory.withAttributes(customAttributes).persist(5);
    }
}
```
## Unique Attributes
For factories to correctly handle unique attributes, you must allow Spring Boot to manage their lifecycle by annotating
the class with `@EntityFactory` and using dependency injection. You must not explicitly usw a factory's constructor to
create an instance.
```java
@EntityFactory
public class BasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public static final String LONG_ATTRIBUTE_NAME = "myLongAttribute";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";

    private static final Faker faker = new Faker();

    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository, Map.of(
                // All myLongAttribute values are guaranteed to be unique
                BasicEntityFactory.LONG_ATTRIBUTE_NAME,
                new DefaultAttribute<>(BasicEntityFactory.LONG_ATTRIBUTE_NAME, () -> AbstractBaseEntityFactory.faker.number().numberBetween(1L, 5L), true),
                BasicEntityFactory.STRING_ATTRIBUTE_NAME,
                new DefaultAttribute<>(BasicEntityFactory.STRING_ATTRIBUTE_NAME, () -> AbstractBaseEntityFactory.faker.lorem().sentence())
        ));
    }
}
```
A set number of attempts will be made to generate each unique value, after which it will throw an exception.

## One-to-many and many-to-many relationships
Say you have two entities, parent and child. A child cannot exist without its corresponding parent entity. This can
easily be handled with factories so that when a factory creates a child, it also handles the creation and linking to the
child's parent entity.
```java
public class ParentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChildEntity> children = new ArrayList<>();

    public void addChild(ChildEntity childEntity) {
        this.children.add(childEntity);

        childEntity.setParent(this);
    }
}

public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_id")
    private ParentEntity parent;
}

@EntityFactory
public class ParentEntityFactory extends AbstractBaseEntityFactory<ParentEntity> {
    public static final String STRING_ATTRIBUTE_NAME = "stringAttribute";

    public ParentEntityFactory(final JpaRepository<ParentEntity, Long> repository) {
        super(ParentEntity.class, repository);
    }
}

@EntityFactory
public class ChildEntityFactory extends AbstractBaseEntityFactory<ChildEntity> {
    public static final String PARENT_ATTRIBUTE_NAME = "parent";
    
    @Autowired
    public ChildEntityFactory(final JpaRepository<ChildEntity, Long> repository, ParentEntityFactory parentEntityFactory) {
        super(
                ChildEntity.class,
                repository,
                // will automatically create and save the parent entity when the factory is used to create a child entity
                Map.of(
                        ChildEntityFactory.PARENT_ATTRIBUTE_NAME,
                        new DefaultAttribute<>(ChildEntityFactory.PARENT_ATTRIBUTE_NAME, parentEntityFactory::create)
                )
        );
    }
}
```
