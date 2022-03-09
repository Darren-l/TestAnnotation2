package cn.gd.snm.asm_test;


import org.junit.Test;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * 测试Asm。
 *
 */
public class Main {

    @Test
    public static void main(String[] args) {
        //TODO:正常调用，生成class。
//        TestAsm testAsm = new TestAsm();
//        testAsm.testMethod();

        //TODO:开始插桩。
        testAsm();
    }

    private static void testAsm() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\Project\\Android" +
                    "\\AndroidTestPro\\TestAnnotation2\\asm-test\\src\\testclass\\TestAsm.class");

            //TODO:获取读/写的class分析器。
            ClassReader classReader = new ClassReader(fileInputStream);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            //TODO:通过classReader获取到class的访问器，里面包含操作类成员及方法。
            //TODO:这里需要注意，构造方法是带classWriter。
            classReader.accept(new ClassVisitor(Opcodes.ASM7,classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                 String signature, String[] exceptions) {
                    //TODO:该方法会被循环调用，直至类方法全部访问完毕。
                    //TODO:可以通过该方法，操作class类成员或方法属性。
                    System.out.println("name=" + name);

                    //TODO：如果要操作方法体中的内容，则需要重写这里返回的MethodVisitor对象。
//                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                    MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor,
                            signature, exceptions);
                    return new SpecClassVisitor(api,methodVisitor,access,name,
                            descriptor);
                }

                @Override
                public FieldVisitor visitField(int access, String name, String descriptor,
                                               String signature, Object value) {
                    return super.visitField(access, name, descriptor, signature, value);
                }

            },ClassReader.EXPAND_FRAMES);

            //TODO:输出新的class。
            byte[] bytes = classWriter.toByteArray();
            FileOutputStream fileOutputStream=new FileOutputStream("D:\\Project\\Android" +
                    "\\AndroidTestPro\\TestAnnotation2\\asm-test\\src\\testclass\\TestAsm2.class");

            fileOutputStream.write(bytes);
            fileOutputStream.close();
            fileInputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义一个方法访问器，这里集成的是AdviceAdapter，MethodVisitor的子类。
     * 该子类方法更丰富。
     *
     * 注意，每一个方法实际都对应着一个SpecClassVisitor。
     *
     */
    public static class SpecClassVisitor extends AdviceAdapter {

        private MethodVisitor methodVisitor;
        protected SpecClassVisitor(int api, MethodVisitor methodVisitor,
                                   int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
            this.methodVisitor = methodVisitor;
        }

        //TODO:执行方法体前。
        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();

            if(!isNeedInject){
                return;
            }
            //TODO:开始向class类的方法中，进行插桩代码。
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(19, label0);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
                    "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("test...ams start");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                    "println", "(Ljava/lang/String;)V", false);
        }

        //TODO:执行方法体后。
        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);

            if(!isNeedInject){
                return;
            }

            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(21, label2);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System",
                    "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("test...ams end");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                    "println", "(Ljava/lang/String;)V", false);
        }

        private boolean isNeedInject = false;
        //TODO:访问注解。
        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            System.out.println("visitAnnotation:" + descriptor);

            if("Lcn/gd/snm/asm_test/AsmTag;".equals(descriptor)){
                isNeedInject=true;
            }
            return super.visitAnnotation(descriptor, visible);
        }
    }
}
