package cn.gd.snm.spi;

import android.util.Log;

/**
 * 具体的实现类。
 */
public class SpiStandarImp implements SpiStandar{
    @Override
    public void init() {
        System.out.println("SpiStandarImp... init");
    }
}
