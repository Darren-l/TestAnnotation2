package cn.gd.snm.testannotation.testref02;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class ListenerInvocationHandler implements InvocationHandler {

    //需要在onClick中执行activity.click();
    private Object activity;
    private Method activityMethod;

    public ListenerInvocationHandler(Object activity, Method activityMethod) {
        this.activity = activity;
        this.activityMethod = activityMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在这里去调用被注解了的click();
        return activityMethod.invoke(activity,args);
    }










}



