package com.alexandria.papyrus

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.Architectures.onionArchitecture
import org.springframework.transaction.annotation.Transactional

@AnalyzeClasses(packages = ["com.alexandria.papyrus"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureTest {
    @ArchTest
    fun `onion architecture validation`(appClasses: JavaClasses) {
        onionArchitecture().domainModels("com.alexandria.papyrus.domain.model..")
            .domainServices("com.alexandria.papyrus.domain.services..", "com.alexandria.papyrus.domain.repositories..")
            .applicationServices("com.alexandria.papyrus.application..")
            .adapter("persistence", "com.alexandria.papyrus.adapters.repositories..")
            .adapter("rest", "com.alexandria.papyrus.adapters.rest..")
            .adapter("config", "com.alexandria.papyrus.config..").check(appClasses)
    }

    @ArchTest
    fun `domain layer should not contain repository implementations`(appClasses: JavaClasses) {
        classes().that().resideInAPackage("com.alexandria.papyrus.domain.repositories..").should().beInterfaces()
            .because("repositories implementations should be in infrastructure layer").check(appClasses)
    }

    @ArchTest
    fun `use case classes should be annotated with @Transactional`(appClasses: JavaClasses) {
        classes().that().resideInAPackage("com.alexandria.papyrus.application..")
            .and().doNotHaveSimpleName("Companion")
            .should()
            .beAnnotatedWith(Transactional::class.java)
            .because("every use case occurs in a transaction. we only use spring's @Transactional annotations - not jakarta's")
            .check(appClasses)
    }
}
