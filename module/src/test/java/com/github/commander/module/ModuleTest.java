package com.github.commander.module;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class ModuleTest {

    @Test
    public void should_get_module_name() {
        TestModule testModule = new TestModule();
        String name = testModule.name();

        assertThat(name, is("test"));
    }

    @Test
    public void should_get_component() {
        TestModule testModule = new TestModule();
        testModule.init(new TestModuleConfig());

        Component component = testModule.getComponent(TestComponent.class);
        assertThat(component, instanceOf(TestComponent.class));
        assertThat(component.name(), is("testComponent"));

        component = testModule.getComponent(TestComponent1.class);
        assertThat(component, instanceOf(TestComponent1.class));
        assertThat(component.name(), is("testComponent1"));
    }

    @Test
    public void should_throw_exception_when_get_not_exists_component() {
        TestModule testModule = new TestModule();
        testModule.init(new TestModuleConfig());

        try {
            testModule.getComponent(NotExistsComponent.class);
            fail("should throw exception.");
        } catch (Exception e) {
            assertThat(e, instanceOf(ModuleException.class));
            assertThat(e.getMessage(), is("component 'com.github.commander.module.NotExistsComponent' not found in module 'test'"));
        }
    }
}
