package com.alexandria.papyrus

import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.Architectures.onionArchitecture
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.transaction.annotation.Transactional

@AnalyzeClasses(packages = ["com.alexandria.papyrus"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureTest {
    @ArchTest
    fun `onion architecture validation`(appClasses: JavaClasses) {
        onionArchitecture()
            .domainModels("com.alexandria.papyrus.domain.model..")
            .domainServices("com.alexandria.papyrus.domain.services..", "com.alexandria.papyrus.domain.repositories..")
            .applicationServices("com.alexandria.papyrus.application..")
            .adapter("rest", "com.alexandria.papyrus.adapters.exposition.rest..")
            .adapter("graphql", "com.alexandria.papyrus.adapters.exposition.graphql..")
            .adapter("inbound-messaging", "com.alexandria.papyrus.adapters.exposition.messaging..")
            .adapter("outbound-messaging", "com.alexandria.papyrus.adapters.integration.messaging..")
            .adapter("persistence", "com.alexandria.papyrus.adapters.integration.repositories..")
            .adapter("config", "com.alexandria.papyrus.config..")
            .ignoreDependency(
                resideInAPackage("com.alexandria.papyrus.adapters.."),
                resideInAPackage("com.alexandria.papyrus.config").and(annotatedWith(ConfigurationProperties::class.java)),
            )
            .because(
                """we enforce the onion architecture:
                    |
                    |domain layer -> should not depend on any other layer
                    |
                    |application -> layer should only depend on domain layer
                    |
                    |adapters -> should only depend on application and domain layers
                    |adapters -> should not depend on each other:
                    |    - for instance: rest layer should not depend on inbound-messaging layer
                    |    - for instance: persistence layer should not depend on outbound-messaging layer
                    |    - simply put: adapters should not depend on each other!
                    |
                    |    note: an exception is made for @ConfigurationProperties classes. they are allowed to be used other adapter layers.
                    |
                    |config layer -> is just where we put our spring configuration classes
                    |
                    |if this tests fails, you likely:
                    |    - broke one of those rules
                    |    - or you added a new package in the adapters that you need to declare here. (make sure it's in the right spot though!)
                """.trimMargin(),
            )
            .check(appClasses)
    }

    @ArchTest
    fun `domain layer should not contain repository implementations`(appClasses: JavaClasses) {
        classes()
            .that().resideInAPackage("com.alexandria.papyrus.domain.repositories..")
            .should().beInterfaces()
            .because("repositories implementations should be in infrastructure layer")
            .check(appClasses)
    }

    @ArchTest
    fun `use case classes should be annotated with @Transactional`(appClasses: JavaClasses) {
        classes().that().resideInAPackage("com.alexandria.papyrus.application..").and().doNotHaveSimpleName("Companion")
            .should().beAnnotatedWith(Transactional::class.java)
            .because("every use case occurs in a transaction. we only use spring's @Transactional annotations - not jakarta's")
            .check(appClasses)
    }
}
