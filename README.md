# Database Entity Factories

This library allows you to easily create and persist large numbers of entities with random but logical attributes using
Spring Boot and Hibernate. It also allows you to override these properties if you require an attribute to be set to a
specific value or generally have a different set of creation rules.

It's intended to be used for tests, when you need to create an instance of a model but don't necessarily care exactly what
most of the attributes are for, or for quickly populating a database with test data without having to individually specify
each entity's attributes.

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

Whenever this factory is used to `create` or `persist` entities, each entity will have
a long attribute with a random value between 1 and 5, and a string attribute with a value set to a random sentence.

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

Custom attributes can be specified using the `withAttributes()` function. In the example below, each entity will have
`myLongAttribute` set to `12`, overriding the default attribute creation rule specified in the factory itself:

```java
public class EntityCreator {
    @Autowired
    BasicEntityFactory basicEntityFactory;

    public void CreateEntities() {
        List<BasicEntity> basicEntity = basicEntityFactory
                .withCustomAttributes(new CustomAttribute<>(BasicEntityFactory.LONG_ATTRIBUTE_NAME, () -> 12L))
                .persist(5);
    }
}
```

## Unique Attribute Values

The uniqueness of an attribute is specified by the third parameter to the attribute's constructor. If left out, then it
defaults to false.

For factories to correctly handle unique attribute values, you must allow Spring Boot to manage their lifecycle by annotating
the factory with `@EntityFactory`. You then use the factory with dependency injection rather its constructor.

A set number of attempts will be made to generate each unique value, after which it will throw an exception:

```java
@EntityFactory
public class BasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public static final String LONG_ATTRIBUTE_NAME = "myLongAttribute";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";

    private static final Faker faker = new Faker();

    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository, Map.of(
                // All myLongAttribute values are guaranteed to be unique. If this is not possible, an exception will be thrown
                BasicEntityFactory.LONG_ATTRIBUTE_NAME,
                new DefaultAttribute<>(BasicEntityFactory.LONG_ATTRIBUTE_NAME, () -> AbstractBaseEntityFactory.faker.number().numberBetween(1L, 5L), true),
                BasicEntityFactory.STRING_ATTRIBUTE_NAME,
                new DefaultAttribute<>(BasicEntityFactory.STRING_ATTRIBUTE_NAME, () -> AbstractBaseEntityFactory.faker.lorem().sentence())
        ));
    }
}
```

## One-to-many and many-to-many relationships

Say you have two entities, parent and child. A parent can have between zero and many children, while a child must have a parent.
This type of relationship can easily be handled so that when a factory creates a child, it also creates and links to a
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
