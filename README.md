# Database Entity Factories

This library allows you to easily create and persist large numbers of entities with random but logical attributes using
Spring Boot and Hibernate. It also allows you to override these properties if you require an attribute to be set to a
specific value or generally have a different set of creation rules.

It's intended to be used for tests, when you need to create an instance of a model but don't necessarily care exactly
what
most of the attributes are for, or for quickly populating a database with test data without having to individually
specify
each entity's attributes.

The examples in this repository use the [datafaker](https://www.datafaker.net/) package to create random attributes as
required.

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
public class BasicEntityFactory extends BaseEntityFactory<BasicEntity> {
    public static final String LONG_ATTRIBUTE_NAME = "myLongAttribute";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";
    
    private static final Faker faker = new Faker();

    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository);
    }
    
    // Overriding this method allows you to set up any default attribute values for entities produced by this factory
    // Entities can have fixed or, in this case, random values generated each time an entity is created
    // If you don't override it then the entity will be produced with none of its values set
    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(
            BaseEntityFactory<?>... dependentFactories
    ) {
        return toAttributeMap(
                new DefaultAttribute<>(
                        BasicEntityFactory.LONG_ATTRIBUTE_NAME,
                        () -> BasicEntityFactory.faker.number().numberBetween(1L, 5L)
                ),
                new DefaultAttribute<>(
                        BasicEntityFactory.STRING_ATTRIBUTE_NAME,
                        () -> BasicEntityFactory.faker.lorem().sentence()
                )
        );
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

Custom attributes can be specified using the `withCustomAttributes()` function. In the example below, each entity will
have
`myLongAttribute` set to `12`, overriding the default attribute creation rule specified in the factory itself:

```java
public class EntityCreator {
    @Autowired
    BasicEntityFactory basicEntityFactory;

    public void CreateEntities() {
        List<BasicEntity> basicEntity = basicEntityFactory
                .withCustomAttributes(new CustomAttribute<>("myLongAttribute", () -> 12L))
                .persist(5);
    }
}
```

## Unique Attribute Values

The uniqueness of an attribute is specified by the third parameter to the attribute's constructor. If left out, then it
defaults to false.

For factories to correctly handle unique attribute values, you must allow Spring Boot to manage their lifecycle by
annotating the factory with `@EntityFactory` and injecting instances with dependency injection.

A set number of attempts will be made to generate each unique value, after which it will throw an exception:

```java

@EntityFactory
public class BasicEntityFactory extends BaseEntityFactory<BasicEntity> {
    public static final String LONG_ATTRIBUTE_NAME = "myLongAttribute";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";
    
    private static final Faker faker = new Faker();

    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(
            BaseEntityFactory<?>... dependentFactories
    ) {
        return toAttributeMap(
                // All myLongAttribute values are guaranteed to be unique. If this is not possible, an exception will be thrown
                new DefaultAttribute<>(
                        BasicEntityFactory.LONG_ATTRIBUTE_NAME,
                        () -> BaseEntityFactory.faker.number().numberBetween(1L, 5L), true
                ),
                new DefaultAttribute<>(
                        BasicEntityFactory.STRING_ATTRIBUTE_NAME,
                        () -> BaseEntityFactory.faker.lorem().sentence()
                )
        );
    }
}
```

## One-to-many and many-to-many relationships

Say you have two entities, parent and child. A parent can have between zero and many children, while a child must have a
parent. This type of relationship can easily be set up so that when a factory creates a child, it also creates and links
to a
child's parent entity.

```java
import org.springframework.beans.factory.annotation.Autowired;

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
public class ParentEntityFactory extends BaseEntityFactory<ParentEntity> {
    public static final String STRING_ATTRIBUTE_NAME = "stringAttribute";

    @Autowired
    public ParentEntityFactory(final JpaRepository<ParentEntity, Long> repository) {
        super(ParentEntity.class, repository);
    }
}

@EntityFactory
public class ChildEntityFactory extends BaseEntityFactory<ChildEntity> {
    public static final String PARENT_ATTRIBUTE_NAME = "parent";

    @Autowired
    public ChildEntityFactory(
            final JpaRepository<ChildEntity, Long> repository,
            final ParentEntityFactory parentEntityFactory
    ) {
        // the parent entity factory is passed in as it's needed during the creation of the child entity
        // the constructor can handle any number of factories being passed in here
        super(ChildEntity.class, repository, parentEntityFactory);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(
            BaseEntityFactory<?>... dependentFactories
    ) {
        return toAttributeMap(
                // grab the parent entity factory from the dependentFactories and use it to persist a parent.
                // Then use that as the parent for this child entity when it is created
                new DefaultAttribute<>(ChildEntityFactory.PARENT_ATTRIBUTE_NAME, dependentFactories[0]::persist)
        );
    }
}
```

## Examples

### Easily create and persist dummy users to a database

```java
@Entity
@Getter
@Setter
public class NewUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String address;

    private String email;

    private String age;

    private String phoneNumber;

    @Override
    public String toString() {
        return "NewUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", age='" + age + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
```

```java
public interface NewUserRepository extends JpaRepository<NewUser, Long> {
}
```

```java
@EntityFactory
public class NewUserFactory extends BaseEntityFactory<NewUser> {
    private static final Faker faker = new Faker();

    public NewUserFactory(final NewUserRepository repository) {
        super(NewUser.class, repository);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(
            BaseEntityFactory<?>... dependentFactories
    ) {
        return toAttributeMap(
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
```

```java
public class EntityCreator {
    @Autowired
    NewUserFactory newUserFactory;

    public void CreateEntities() {
        List<NewUser> savedUsers = newUserFactory.persist(20);

        savedUsers.forEach(System.out::println);
    }
}
```

Example output from EntityCreator:

```
NewUser{id=1, username='jeraldine.corkery', firstName='Earl', lastName='Ziemann', address='560 Lind Trail, Port Rogertown, SD 32013', email='tyree.nolan@hotmail.com', age='22', phoneNumber='(308) 228-8767'}
NewUser{id=2, username='kelly.emmerich', firstName='Israel', lastName='Wuckert', address='289 Pouros Brook, Kassulkechester, AZ 29686', email='chere.howe@gmail.com', age='75', phoneNumber='(409) 919-8977'}
NewUser{id=3, username='jacqulyn.koepp', firstName='Evelina', lastName='Wehner', address='469 Cory Vista, Mantebury, NC 11215', email='augustus.champlin@hotmail.com', age='25', phoneNumber='(970) 281-9638'}
NewUser{id=4, username='philomena.okeefe', firstName='Kelsi', lastName='Brekke', address='35712 Kris Meadows, Port Eltonhaven, MN 32154', email='harlan.brakus@hotmail.com', age='27', phoneNumber='1-731-316-4455'}
NewUser{id=5, username='glynis.stanton', firstName='Hong', lastName='West', address='Suite 079 9584 Bogan Throughway, Lake Magen, ND 22146', email='kristle.raynor@hotmail.com', age='69', phoneNumber='386-740-9692'}
NewUser{id=6, username='felipa.graham', firstName='Jolynn', lastName='Smitham', address='Apt. 224 90154 Wiegand Meadows, Bartolettiville, OH 08563', email='melvin.kozey@hotmail.com', age='69', phoneNumber='1-714-563-3402'}
NewUser{id=7, username='ike.hagenes', firstName='Tamela', lastName='Johns', address='663 Angel Villages, North Colton, SD 77125', email='harriette.lockman@hotmail.com', age='36', phoneNumber='215.808.6764'}
NewUser{id=8, username='jeana.mclaughlin', firstName='Margarette', lastName='Hills', address='Apt. 714 683 Graham Field, Fumikohaven, TN 89943', email='mariano.sipes@yahoo.com', age='30', phoneNumber='(630) 317-8846'}
NewUser{id=9, username='colby.schamberger', firstName='Lakenya', lastName='King', address='Suite 987 4044 Dortha Mission, Lake Leonia, CO 44427', email='jaime.wolf@yahoo.com', age='88', phoneNumber='770.828.5974'}
NewUser{id=10, username='judson.collier', firstName='Myong', lastName='Hessel', address='7958 Matthew Plain, Rinaton, TX 96198', email='coral.berge@yahoo.com', age='45', phoneNumber='760.912.0884'}
NewUser{id=11, username='stevie.stamm', firstName='Delmar', lastName='Aufderhar', address='Suite 036 432 Harvey Crossroad, Maragretshire, NE 68910', email='september.wolff@yahoo.com', age='42', phoneNumber='(205) 206-5994'}
NewUser{id=12, username='grace.bruen', firstName='Trena', lastName='Klocko', address='52356 Carter Shoals, Lake Trinidadville, OR 43169', email='joe.daniel@hotmail.com', age='82', phoneNumber='903.720.0310'}
NewUser{id=13, username='refugia.skiles', firstName='Nicki', lastName='Mueller', address='Apt. 666 13443 Forrest Extensions, Exieton, KY 33870', email='marcelo.veum@yahoo.com', age='41', phoneNumber='1-512-913-7307'}
NewUser{id=14, username='galen.pfeffer', firstName='Truman', lastName='Olson', address='0708 Eloisa Shoal, East Paulettaberg, OK 94161', email='robt.bartoletti@hotmail.com', age='26', phoneNumber='618.557.9916'}
NewUser{id=15, username='otha.homenick', firstName='Filiberto', lastName='Watsica', address='83276 Metz Fields, Lake Shera, MI 73280', email='clifton.bernhard@hotmail.com', age='78', phoneNumber='1-509-270-3797'}
NewUser{id=16, username='lacey.leuschke', firstName='Hosea', lastName='Blick', address='Suite 709 58822 Crona Islands, Bethelhaven, FL 19750', email='jamison.kautzer@gmail.com', age='22', phoneNumber='901.601.3891'}
NewUser{id=17, username='edmond.lindgren', firstName='Elvin', lastName='Renner', address='5166 Lou Rest, Zulaufhaven, CO 44269', email='tyler.farrell@yahoo.com', age='45', phoneNumber='1-941-718-1600'}
NewUser{id=18, username='livia.hettinger', firstName='Robyn', lastName='Kassulke', address='660 Yong Ville, Port Cecilside, ID 20730', email='lorene.simonis@yahoo.com', age='40', phoneNumber='1-971-551-8499'}
NewUser{id=19, username='bryce.hermiston', firstName='Alejandro', lastName='Moore', address='Suite 557 459 Cornell Walks, East Kyleemouth, AK 75087', email='jan.cassin@gmail.com', age='45', phoneNumber='1-947-520-4615'}
NewUser{id=20, username='jarod.gutmann', firstName='Odell', lastName='Bosco', address='9130 Parker Estates, O'Reillyberg, AZ 18142', email='izetta.koelpin@hotmail.com', age='76', phoneNumber='339-818-3753'}
```

