package cn.gd.snm.testannotation.testapt02;

import android.app.Activity;

import cn.gd.snm.testannotation.IBinder;

public class ViewBinding {
    public static void bind(Activity activity){
        String name=activity.getClass().getName()+"_ViewBinding";
        try{
            Class<?> aClass= Class.forName(name);
            IBinder iBinder=(IBinder)aClass.newInstance();
            iBinder.bind(activity);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
