package com.codemonk.common.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureQualityTest_1 {

    private static final String BASE_PACKAGE = "com.codemonk.common";
    private static final String DTO_PACKAGE = BASE_PACKAGE + ".dto";
    private static final String EXCEPTION_PACKAGE = BASE_PACKAGE + ".exception";

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter().importPackages(BASE_PACKAGE);
    }

    @Test
    void dtoClassesShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that()
                .resideInAPackage(DTO_PACKAGE)
                .and()
                .areNotInterfaces()
                .should()
                .haveSimpleNameEndingWith("Response")
                .orShould()
                .haveSimpleNameEndingWith("Dto")
                .orShould()
                .haveSimpleNameEndingWith("Request");

        rule.check(classes);
    }

    @Test
    void exceptionClassesShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that()
                .resideInAPackage(EXCEPTION_PACKAGE)
                .should()
                .haveSimpleNameEndingWith("Exception");

        rule.check(classes);
    }

    @Test
    void exceptionClassesShouldExtendThrowable() {
        ArchRule rule = classes()
                .that()
                .resideInAPackage(EXCEPTION_PACKAGE)
                .should()
                .beAssignableTo(Throwable.class);

        rule.check(classes);
    }

    @Test
    void exceptionClassesShouldNotDependOnDtoClasses() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage(EXCEPTION_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAPackage(DTO_PACKAGE);

        rule.check(classes);
    }

    @Test
    void dtoClassesPublicAPI() {
        ArchRule rule = classes()
                .that()
                .resideInAPackage(DTO_PACKAGE)
                .should()
                .bePublic();

        rule.check(classes);
    }
}
