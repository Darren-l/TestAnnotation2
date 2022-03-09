package cn.gd.snm.testannotation;

import androidx.annotation.IntDef;

import org.w3c.dom.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Test {
    public static void main(String[] args) {
        //TODO:使用普通方式。
//        setDay(Days.MON);

        //TODO:使用注解方式。
        setDayByAnno(123);
    }

    /**
     * 使用枚举的类型声明变量。
     */
    private static Days mdays;
    public static void setDay(Days days){
        mdays = days;

    }
    enum Days{
        MON,TUE
    }


    /**
     * 接下来使用注解的方式声明。
     */
    private static final int SUN = 0x001;
    private static final int MON = 0x002;
    @IntDef({SUN,MON})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    @interface DaysByAnno{
    }

    private static int mdatasAnno;
    public static void setDayByAnno(@DaysByAnno int day){
        mdatasAnno = day;
        System.out.println("####" + day);
    }
}

