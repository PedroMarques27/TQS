import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Stack {
    private LinkedList<Object> stack;

    public Stack(){
        stack = new LinkedList<Object>();
    }


    public void push(Object x){
        stack.push(x);
    }

    public Object pop(){
        return stack.pop();
    }

    public Object peek(){
        return stack.peek();
    }

    public int size(){
        return stack.size();
    }
    public boolean isEmpty(){
        return stack.isEmpty();
    }


}
