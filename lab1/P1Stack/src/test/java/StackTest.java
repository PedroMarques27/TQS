import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;


import java.util.NoSuchElementException;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class StackTest extends TestCase {
    private Stack stack;
    private Stack stack3elements;


    @BeforeEach
    public void setUp() throws Exception{
        stack = new Stack();
        stack3elements = new Stack();
        stack3elements.push("TEST1");
        stack3elements.push("TEST2");
        stack3elements.push("TEST3");
    }
    @Test
    @DisplayName("Stack empty on creation")
    public void testEmptyOnCreation(){
        assertEquals(true, stack.isEmpty());
    }
    @Test
    @DisplayName("Stack has size 0 on creation")
    public void testSizeOnCreation(){
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("Pushing Object To Stack")
    public void testSizeAfterNPushes() {
        assertEquals(3, stack3elements.size());
    }

    @Test
    @DisplayName("Pop n Elements: Size = 0 and isEmpty")
    public void testPopAll() {
        stack3elements.pop();
        stack3elements.pop();
        stack3elements.pop();
        assertEquals(true, stack3elements.isEmpty());
        assertEquals(0, stack3elements.size());
    }

    @Test
    @DisplayName("Pop 1 Element")
    public void testPop() {
        assertEquals("TEST1", stack3elements.pop());
    }

    @Test
    @DisplayName("Peek Element")
    public void testPeek() {
        assertEquals("TEST1", stack3elements.peek());
    }

    @Test
    @DisplayName("Size after peek")
    public void testSizeAfterPeek() {
        stack3elements.peek();
        assertEquals(3, stack3elements.size());
    }

    @Test
    @DisplayName("Popping from empty does not throw NoSuchElementException")
    public void testThrownExceptionPopEmpty(){
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }
    @Test
    @DisplayName("Popping from empty does not throw NoSuchElementException")
    public void testThrownExceptionPeekEmpty(){
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }
}