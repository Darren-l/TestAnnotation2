package cn.gd.snm.testannotation;

/**
 * 用于绑定activity     Butterknife.bind(this)
 * @param <T>
 */
public interface IBinder<T> {
    void bind(T target);
}
