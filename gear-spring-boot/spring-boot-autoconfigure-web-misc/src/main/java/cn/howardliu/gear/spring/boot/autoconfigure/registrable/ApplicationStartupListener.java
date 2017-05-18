package cn.howardliu.gear.spring.boot.autoconfigure.registrable;

import cn.howardliu.gear.springEx.SpringMvcRegisterWrapper;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * <br>created at 17-3-16
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ApplicationStartupListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    private SpringMvcRegisterWrapper registerWrapper;

    public ApplicationStartupListener(SpringMvcRegisterWrapper registerWrapper) {
        this.registerWrapper = registerWrapper;
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        registerWrapper.setLocalPort(event.getEmbeddedServletContainer().getPort());
        try {
            registerWrapper.regist();
        } catch (Exception e) {
            throw new RuntimeException("an error occured when registering service", e);
        }
    }
}
