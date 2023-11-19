package com.alexandria.papyrus

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.Architectures.onionArchitecture


@AnalyzeClasses(packages = ["com.alexandria.papyrus"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureTest {

    @ArchTest
    fun `onion architecture validation`(appClasses: JavaClasses) {
        onionArchitecture().domainModels("com.alexandria.papyrus.domain.model..")
            .domainServices("com.alexandria.papyrus.domain.services..", "com.alexandria.papyrus.domain.repositories..")
            .applicationServices("com.alexandria.papyrus.application..")
            .adapter("persistence", "com.alexandria.papyrus.infrastructure.repositories..")
            .adapter("api", "com.alexandria.papyrus.infrastructure.api..")
            .adapter("config", "com.alexandria.papyrus.config..")
            .check(appClasses)
    }

    @ArchTest
    fun `domain layer should not contain repository implementations`(appClasses: JavaClasses) {
        classes().that().resideInAPackage("com.alexandria.papyrus.domain.repositories..").should().beInterfaces()
            .because("repositories implementations should be in infrastructure layer")
            .check(appClasses)
    }
}
