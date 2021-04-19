package com.github.commander.module;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class SPIModuleManagerTest {

    @Test
    void should_get_module() {
        ModuleManager manager = new SPIModuleManager();

        assertThat(manager.getModule(TestModule.class), instanceOf(TestModule.class));
        assertThat(manager.getModule(TestModule1.class), instanceOf(TestModule1.class));
    }

    @Test
    void should_throw_exception_when_get_not_exists_module() {
        ModuleManager manager = new SPIModuleManager();
        try {
            manager.getModule(NotExistModule.class);
            fail("should throw exception.");
        } catch (Exception e) {
            assertThat(e, instanceOf(ModuleException.class));
            assertThat(e.getMessage(), is("no such module 'com.github.commander.module.NotExistModule'"));
        }
    }

}
