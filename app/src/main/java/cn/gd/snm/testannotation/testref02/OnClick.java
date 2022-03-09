package cn.gd.snm.testannotation.testref02;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 流程：注解相当于是把必要的信息放到注解中，使用时通过注解对象去获取出来。
 *
 * 思路:
 *  1. 通过反射的方式，获取到activity对象所持有的所有方法。
 *  2. 通过循环遍历方法，获取到方法上的注解对象。
 *  3. 通过注解对象，获取到注解中的值。
 *  4. 手动的去实现需要实现的过程，如setOnclickLis。
 *
 * 具体实现：
 *  正常的设置按键监听，需要的元素：activity.setOnclickListener(new View.OnClickListener)
 *     1. activity对象。
 *     2. setOnclickListener。
 *     3. new View.OnClickListener对象。
 *
 *  思路是通过反射去获取到这些对象，那么以上这三个元素，都需要给到处理注解的接口。
 *      1. activity通过方法参数给到处理接口。
 *      2. setOnclickListener，即然是个方法，还是属于activity中的方法，那么只要把名字定义在注解中，即可。
 *      后面就可以通过方法名，反射获取到这个方法。
 *      3. new View.OnClickListener对象：反射中如果要new出对象，那么需要给出class对象。这个也要在注解中提供,
 *      这里需要注意，View.OnClickListener本身是一个接口，这也意味着通过反射的newInstance是不能直接获取到对象的
 *      ，所以需要用到动态代理，来帮助初始化对象。
 *      4. 其实还有一步，new View.OnClickListener的onClick中去调用被注解的方法，但这个方法名字已经不重要的了，
 *      因为我们是通过遍历所有有注解的方法，获取到了当前method，自然也可以调用当前这个方法。
 *
 * @OnClick({R.id.btn1,R.id.btn2})
 * public void onclick(View view){
 *    Toast.makeText(this, "按下1", Toast.LENGTH_SHORT).show();
 * }
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnClickListener"
        ,listenerType = View.OnClickListener.class)
public @interface OnClick {
    int[] value() default -1;
}
