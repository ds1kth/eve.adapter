package com.chargev.eve.adapter;

import org.springframework.context.ConfigurableApplicationContext;

public class AppContext {

    private static ConfigurableApplicationContext context;

    private AppContext() {
    }

    /**
     * ApplicationContext 를 반환한다.
     *
     * @return ApplicationContext
     */
    public static ConfigurableApplicationContext getContext() {
        return AppContext.context;
    }

    /**
     * ApplicationContext 값을 설정한다. 애플리케이션 기동시에 주입해준다.
     *
     * @param context 설정할 ApplicationContext
     */
    public static void setContext(ConfigurableApplicationContext context) {
        AppContext.context = context;
    }

    /**
     * ApplicationContext 에서 특정 Bean 을 구한다.
     *
     * @param requiredType 찾을 빈 클래스 객체
     * @param <T> 찾을 빈 클래스 타입
     * @return 찾은 빈
     */
    public static <T> T getBean(Class<T> requiredType) {
        return getContext().getBean(requiredType);
    }

    /**
     * ApplicationContext 에서 특정 Bean 을 구한다.
     *
     * @param name 찾을 빈 이름
     * @param requiredType 찾을 빈 클래스 객체
     * @param <T> 찾을 빈 클래스 타입
     * @return 찾은 빈
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        if (getContext() == null) {
            throw new IllegalArgumentException("getContext() is null");
        }
        return getContext().getBean(name, requiredType);
    }
}
