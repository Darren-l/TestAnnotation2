package cn.gd.snm.testannotation.testref02;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 对注解的处理。
 *
 */
public class InjectUtils {
    public static void inject(Object context) {
        injectClick(context);
    }

    private static void injectClick(Object context) {
        //TODO:通过反射，获取到cls对象。
        Class<?> clazz = context.getClass();
        //TODO:获取到对象的方法数组。
        Method[] methods = clazz.getDeclaredMethods();

        //TODO:开始遍历方法的数组。
        for (Method method : methods) {
            //TODO:获取当前方法中所使用的注解。
            Annotation[] annotations = method.getAnnotations();
            //TODO:循环遍历该方法的所有注解。
            for (Annotation annotation : annotations) {
                Class<?> annotationClass = annotation.annotationType();
                //TODO:判断有没有EventBase的注解。
                EventBase eventBase = annotationClass.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }

                //TODO:没有带注解的方法直接被返回了。接下来来实现setOnclickLis
                //btn.setOnClickListener（new View.OnClickListener() {
                //@Override
                //public void onClick(View v) {
                //
                //}
                //                });

                //TODO:获取到setOnClickListener，也就是执行的那个方法。
                //String listenerSetter();
                String listenerSetter = eventBase.listenerSetter();
                //TODO:获取到new View.OnClickListener() 对象。
                //Class<?> listenerType();
                Class<?> listenerType = eventBase.listenerType();


                //TODO:开始处理代码执行。
                Method valueMethod = null;
                try {
                    //TODO: 获取注解中的参数value：@OnClick({R.id.btn1,R.id.btn2})
                    valueMethod = annotationClass.getDeclaredMethod("value");
                    int[] viewId = (int[]) valueMethod.invoke(annotation);
                    //TODO:遍历参数
                    for (int id : viewId) {
                        //TODO:通过class对象 找到findview的方法对象。
                        Method findViewById = clazz.getMethod("findViewById", int.class);

                        //TODO: 执行findview。
                        View view = (View) findViewById.invoke(context, id);
                        if (view == null) {
                            continue;
                        }

                        //TODO:找到view后 要调用view.setOnclickLis。
                        //代理的是activity中的方法。public void abc(View view)
                        //context===activity   click=method
                        ListenerInvocationHandler listenerInvocationHandler =
                                new ListenerInvocationHandler(context, method);

                        //TODO：获取到代理对象。
                        //new View.OnClickListener()
                        Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class[]{listenerType}, listenerInvocationHandler);


                        //TODO：即然是为了获取到View.OnClickListener对象，为什么不直接用反射new？
                        // 因为View.OnClickListener对象是接口类型，无法直接new生成，所以需要用代理套一层
                        // ，目的也是为了获取对象。
//                        Object oncliLis = listenerType.newInstance();


                        //TODO:通过反射获取到setOnclickLis的方法，通过clas对象和方法名字获取到对应的method对象。
                        //view.setOnClickListener（new View.OnClickListener()）
                        Method onClickMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        //TODO:方法类的反射需要接受一个对象和参数,第一个参数是对象，第二个是该方法的参数。
                        onClickMethod.invoke(view, proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
    }
}













