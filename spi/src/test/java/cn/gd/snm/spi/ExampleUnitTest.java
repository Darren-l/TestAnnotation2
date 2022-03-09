package cn.gd.snm.spi;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test(){
        ServiceLoader<SpiStandar> service = ServiceLoader.load(SpiStandar.class);
        Iterator<SpiStandar> iterator = service.iterator();
        while (iterator.hasNext()){
            SpiStandar next = iterator.next();
            next.init();
        }
    }
}