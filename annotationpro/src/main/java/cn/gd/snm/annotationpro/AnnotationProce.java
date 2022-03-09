package cn.gd.snm.annotationpro;

import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AnnotationProce extends AbstractProcessor {
    /**
     * 设置支持版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 选择需要处理的注解
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set <String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    /**
     * 获取一个filer对象，该对象能生产APT目录下的文件。
     */
    Filer filer;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    /**
     * 动态生成代码类文件。
     *
     * 思路：
     *  1. 首先要找到所有有用到注解的地方。
     *  2. 获取到这个注解对象，解析出里面的内容及注解内容。
     *  3. 根据解析的内容，使用filer动态去生产出类文件。
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                "##### darren"+set);

        /**
         * 1. 首先要找到app中，所有用到了BindView注解的对象，这个方法会返回一个set集合，
         * 集合中的item元素包含了带注解的信息体。
         */
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment
                .getElementsAnnotatedWith(BindView.class);

        /**
         *
         * 2. 获取到集合后，就要遍历这个集合，拿到每一个对象带注解的信息对象，根据类名进行分类，
         * 存储在map中。
         *
         * 以key-value的形式，把所有有标注注解的属性，全部存储在我们自己的map中，key为activity
         * 的名字，value为注解信息，注意value是一个list集合，因为一个类中可能存在多个注解。
         *
         * 类：TypeElement
         * 方法：ExecutableElement
         * 属性：VariableElement
         */

        //TODO:声明一个map，key就是类名，value是当前类带有注解的属性集合
        Map<String, List<VariableElement>> map = new HashMap<>();

        for (Element element : elementsAnnotatedWith) {
            //TODO:开始遍历，获取到对象中，带注解的属性。
            VariableElement variableElement = (VariableElement) element;

            //TODO：获取activity名字。
            String activityName = variableElement.getEnclosingElement().getSimpleName()
                    .toString();
            //TODO: 演示---也可以获取到对应的类。
            Class aClass = variableElement.getEnclosingElement().getClass();

            //TODO:以类名的方式，给带注解的属性进行分类，同一个类名下的属性放到一个list中。
            List<VariableElement> variableElements = map.get(activityName);
            if (variableElements == null) {
                variableElements = new ArrayList<>();
                map.put(activityName, variableElements);
            }
            variableElements.add(variableElement);
        }

        if (map.size() > 0) {
            Writer writer = null;
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                //TODO:开始处理之前map存储的信息，首先获取到activity的名字。
                String activityName = iterator.next();
                //TODO:根据名字得到当前activity所有注解属性的list。
                List<VariableElement> variableElements = map.get(activityName);

                TypeElement enclosingElement = (TypeElement) variableElements.get(0).
                        getEnclosingElement();
                //TODO: 根据这个属性信息对象，获取到当前属性的包名。
                String packageName = processingEnv.getElementUtils().getPackageOf(enclosingElement)
                        .toString();

                try {
                    //TODO:开始以文件的方式，创建类。
                    JavaFileObject sourceFile = filer.createSourceFile(packageName +
                            "." + activityName + "_ViewBinding");
                    writer = sourceFile.openWriter();
                    //        package com.example.dn_butterknife;
                    writer.write("package " + packageName + ";\n");

                    //        import com.example.dn_butterknife.IBinder;
                    writer.write("import " + packageName + ".IBinder;\n");

                    //TODO:这里需要注意:类名_ViewBinding，且是IBinder的父类。
                    //        public class MainActivity_ViewBinding implements IBinder<
                    //        com.example.dn_butterknife.MainActivity>{
                    writer.write("public class " + activityName + "_ViewBinding implements " +
                            "IBinder<" + packageName + "." + activityName + ">{\n");

                    //            public void bind(com.example.dn_butterknife.MainActivity target) {
                    writer.write(" @Override\n" +
                            " public void bind(" + packageName + "." + activityName + " target){");

                    //target.tvText=(android.widget.TextView)target.findViewById(2131165325);
                    for (VariableElement variableElement : variableElements) {
                        //TODO:开始遍历当前类的属性list，获取到每一个属性，并写入findview相关代码。
                        //TODO：获取到属性对应在类中的名称 -- 实际就是tvText
                        String variableName = variableElement.getSimpleName().toString();

                        //TODO:获取到属性对应的id号 -- 实际就是R.id.~，这个是我们在注解的时候传值的。
                        int id = variableElement.getAnnotation(BindView.class).value();

                        //TODO:获取到属性的类型 -- 实际对应的就是TextView
                        TypeMirror typeMirror = variableElement.asType();
                        writer.write("target." + variableName + "=(" + typeMirror + ")target." +
                                "findViewById(" + id + ");\n");
                    }

                    writer.write("\n}}");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return false;
    }
}
