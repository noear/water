package demo;

import org.junit.jupiter.api.Test;

/**
 * @author noear 2023/2/21 created
 */
public class ClassTest {
    @Test
    public void xxx(){
        Object tmp = ClassTest.class;

        if(tmp == null){
            return;
        }
    }
}
