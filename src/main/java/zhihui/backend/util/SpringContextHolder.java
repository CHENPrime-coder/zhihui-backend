package zhihui.backend.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import zhihui.backend.exception.BeanNotFoundException;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> cLass) throws BeanNotFoundException {
        checkApplicationContext();
        if (applicationContext.getBean(cLass) instanceof Class) {
            return applicationContext.getBean(cLass);
        }

        throw new BeanNotFoundException("Bean未找到");
    }

    public static <T> T getBean(String cLassName) throws BeanNotFoundException {
        checkApplicationContext();
        return (T) applicationContext.getBean(cLassName);
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext 未注入成功");
        }
    }
}
