package ut.de.schroenser.timera;

import org.junit.Test;
import de.schroenser.timera.api.MyPluginComponent;
import de.schroenser.timera.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {
    @Test
    public void testMyName() {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}