package cn.gd.snm.asm_test;


/**
 * 测试类。
 *
 * 目标是往testMethod中插入两行打印。
 *
 */
public class TestAsm {
    public void testMethod(){
        int a=0;
        int b = 1;
        int c = a + b;
        System.out.println("log... testMethod...");
    }

    @AsmTag
    public void testMethod2(){
//        System.out.println("test..." + "ams start");
        System.out.println("log... testMethod2..." + "test");
//        System.out.println("test..." + "ams end");
    }

    public void testMethod3(){
        System.out.println("log... testMethod3..." + "test");
    }
}
