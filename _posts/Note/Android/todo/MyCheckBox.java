package com.acadsoc.apps.view;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyCheckBox extends AppCompatCheckBox {
    public MyCheckBox(Context context) {
        this(context, null);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Object getValueOfField(String name) {
        //拿到父类的Class对象
        Class<?> clazz = this.getClass().getSuperclass().getSuperclass().getSuperclass();
        try {
            //找到父类的属性
            Field field = clazz.getDeclaredField(name);
            //让属性可以被访问
            field.setAccessible(true);
            //给属性设置新的值
            return field.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setField(String name, boolean value) {
        //拿到父类的Class对象
        Class<?> clazz = this.getClass().getSuperclass().getSuperclass().getSuperclass();
        try {
            //找到父类的属性
            Field field = clazz.getDeclaredField(name);
            //让属性可以被访问
            field.setAccessible(true);
            //给属性设置新的值
            field.set(this, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void invokeMethod(String name, Object... value) {
        Class<?> clazz = this.getClass().getSuperclass().getSuperclass().getSuperclass();
        try {
            Method field = clazz.getDeclaredMethod(name);
            field.setAccessible(true);
            field.invoke(this, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@link android.widget.CompoundButton#mCheckedFromResource}
     * {@link android.widget.CompoundButton#mChecked}
     * {@link android.widget.CompoundButton#mBroadcasting}
     * 为什么这里 mCheckedFromResource 拿不到而 mChecked mBroadcasting 能拿到
     * 他们都在同一个地方
     */
    @Override
    public void setChecked(boolean checked) {

        if (isChecked() != checked) {

            //n
            setField("mCheckedFromResource", false);

            //y
            setField("mChecked", checked);

            refreshDrawableState();
            //n
            invokeMethod("notifyViewAccessibilityStateChangedIfNeeded",
                    AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);

            // Avoid infinite recursions if setChecked() is called from a listener
            //y
            if ((Boolean) getValueOfField("mBroadcasting")) {
                return;
            }
            //y
            setField("mBroadcasting", true);

            //y
            OnCheckedChangeListener mOnCheckedChangeListener =
                    (OnCheckedChangeListener) getValueOfField("mOnCheckedChangeListener");
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, checked);
            }

            //y
            OnCheckedChangeListener mOnCheckedChangeWidgetListener =
                    (OnCheckedChangeListener) getValueOfField("mOnCheckedChangeWidgetListener");
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, checked);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                final AutofillManager afm = getContext().getSystemService(AutofillManager.class);
                if (afm != null) {
                    afm.notifyValueChanged(this);
                }
            }

            //y
            setField("mBroadcasting", false);
        }
    }
}
