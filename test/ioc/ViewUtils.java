package com.test.ioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewUtils {


    //目前
    public static void  inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }
    public static void  inject(View view){
        inject(new ViewFinder(view),view);
    }

    public static void  inject(View view,Object object){
        inject(new ViewFinder(view),object);
    }
    //兼容 上面三个方法 object --> 反射需要执行的类
    private static void inject(ViewFinder finder,Object object){
        injectFiled(finder,object);
        injectEvent(finder,object);
    }

    /**
     * 注入属性
     * @param finder
     * @param object
     */
    private static void injectFiled(ViewFinder finder, Object object) {

        //1. 获取类里面所有的属性

        Class<?> clazz=object.getClass();
        //获取所有属性包括私有和公有
        Field[] fields=clazz.getDeclaredFields();

        //2. 获取ViewById练的value值
        for(Field field:fields){
            ViewById viewById=field.getAnnotation(ViewById.class);
            if(viewById !=null){
                //获取注解里面的id值，--> R.id.test_tv
                int viewId=viewById.value();
                //3.findViewById 找View
                View view=finder.findViewById(viewId);

                if(view !=null) {
                    //都够注入所有修饰符 private  public
                    field.setAccessible(true);
                    //4.动态注入找到的view
                    try {
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectEvent(ViewFinder finder, Object object) {
        //1. 获取类里面所有的方法
        Class<?> clazz=object.getClass();
        Method[] methods=clazz.getDeclaredMethods();

        //2. 获取OnClick里面的value值

        for(Method method:methods){
            OnClick onClick=method.getAnnotation(OnClick.class);
            if(onClick!=null){
                int[] viewIds=onClick.value();
                for(int viewId:viewIds){
                    //3. findViewById 找到View
                   View view= finder.findViewById(viewId);
                   if(view !=null){
                        //4 view.setOnclickListener
                       view.setOnClickListener(new DeclaredOnClickListener(method,object));
                   }
                }
            }
        }

    }

    private static class DeclaredOnClickListener implements  View.OnClickListener{
        private Object mObject;
        private Method mMethod;
        public DeclaredOnClickListener(Method method,Object object){
            this.mObject=object;
            this.mMethod=method;
        }

        @Override
        public void onClick(View v) {
            //点击会调用该方法
            try {
                //所有方法都可以 包括私有、公有
                mMethod.setAccessible(true);
                //5. 反射执行方法
                mMethod.invoke(mObject, v);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
