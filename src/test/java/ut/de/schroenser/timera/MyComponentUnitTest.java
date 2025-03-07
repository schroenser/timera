package ut.de.schroenser.timera;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.schroenser.timera.api.MyPluginComponent;
import de.schroenser.timera.impl.MyPluginComponentImpl;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}
