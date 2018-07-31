package cn.howardliu.gear.spring.boot.autoconfigure.registrable;

import cn.howardliu.gear.springEx.SpringMvcRegisterWrapper;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * <br>created at 17-3-16
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ApplicationStartupListener implements ApplicationListener<ServletWebServerInitializedEvent> {
    private SpringMvcRegisterWrapper registerWrapper;

    public ApplicationStartupListener(SpringMvcRegisterWrapper registerWrapper) {
        this.registerWrapper = registerWrapper;
    }

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        registerWrapper.setLocalPort(event.getApplicationContext().getWebServer().getPort());
        try {
            registerWrapper.regist();
        } catch (Exception e) {
            throw new RuntimeException("an error occured when registering service", e);
        }
    }
}
