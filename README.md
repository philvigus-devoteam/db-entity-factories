# Database Entity Factories

This library allows you to easily create and persist large numbers of entities with random but logical attributes. It
also allows you to override these properties if you require an attribute to be set to a specific value or generally have
a different set of creation rules.

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
    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository, Map.of(
                "myLongAttribute", new DefaultAttribute<>("myLongAttribute", () -> AbstractBaseEntityFactory.faker.number().numberBetween(1L, 5L)),
                "myStringAttribute", new DefaultAttribute<>("myStringAttribute", () -> AbstractBaseEntityFactory.faker.lorem().sentence())
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

    public void CreateEntities() {
        // customLongName and customStringName will be set to 12 and "A custom string value"
        // for all created entities
        List<BasicEntity> basicEntity = basicEntityFactory.withAttributes(
            Map.of(
                customLongName, new CustomAttribute<>(customLongName, () -> 12L),
                customStringName, new CustomAttribute<>(customStringName, () -> "A custom string value")
            )
        ).persist(5);
    }
}
```
## Unique Attributes
```java
@EntityFactory
public class BasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository, Map.of(
                // All myLongAttribute values are guaranteed to be unique
                "myLongAttribute", new DefaultAttribute<>("myLongAttribute", () -> AbstractBaseEntityFactory.faker.number().numberBetween(1L, 5L), true),
                "myStringAttribute", new DefaultAttribute<>("myStringAttribute", () -> AbstractBaseEntityFactory.faker.lorem().sentence())
        ));
    }
}
```
A set number of attempts will be made to generate each unique value, after which it will throw an exception.
## Parent-child relationships
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
@DependsOn("parentEntityFactory")
public class ChildEntityFactory extends AbstractBaseEntityFactory<ChildEntity> {
    @Autowired
    public ChildEntityFactory(final JpaRepository<ChildEntity, Long> repository, ParentEntityFactory parentEntityFactory) {
        // will automatically create and save the parent entity when the factory is used to create a child entity
        super(ChildEntity.class, repository, Map.of("parent", new DefaultAttribute<>("parent", parentEntityFactory::create)));
    }
}
```
