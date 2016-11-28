package cn.howardliu.gear.springEx;

import cn.howardliu.gear.zk.discovery.ServiceDescription;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <br>created at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SpringMvcServiceScanner implements ApplicationContextAware, ServletContextAware {
    private static final Logger logger = LoggerFactory.getLogger(SpringMvcServiceScanner.class);
    private ApplicationContext ctx;
    private String contextPath = "";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.contextPath = servletContext.getContextPath();
    }

    public Collection<ServiceDescription> serviceList() throws Exception {
        Map<String, ServiceDescription> services = new HashMap<>();
        String serviceRoot = "context-path";
        services.put(serviceRoot, new ServiceDescription(serviceRoot, this.contextPath));

        // 获取所有 Controller 和 RestController
        Map<String, Object> beansWithAnnotation = ctx.getBeansWithAnnotation(Controller.class);
        beansWithAnnotation.putAll(ctx.getBeansWithAnnotation(RestController.class));
        for (Object controller : beansWithAnnotation.values()) {
            // 通过反射获取controller类对象
            Class<?> controllerClass = ClassUtils.getUserClass(controller);

            String className = controllerClass.getName();

            // 获取Controller的RequestMapping的value
            RequestMapping[] classRequestMappings = controllerClass.getAnnotationsByType(RequestMapping.class);
            Set<String> uriPres = new HashSet<>();
            if (classRequestMappings != null) {
                for (RequestMapping classRequestMapping : classRequestMappings) {
                    uriPres.addAll(Arrays.asList(classRequestMapping.value()));
                }
            }

            Set<String> serviceNamePres = new HashSet<>();
            // 获取Controller的ServiceRegister的value
            ServiceRegister[] classServiceRegisters = controllerClass.getAnnotationsByType(ServiceRegister.class);
            if (classServiceRegisters != null) {
                for (ServiceRegister classServiceRegister : classServiceRegisters) {
                    serviceNamePres.addAll(Arrays.asList(classServiceRegister.value()));
                }
            }

            Method[] allMethods = controllerClass.getMethods();
            // 为了避免多层嵌套循环，将有RequestMapping及ServiceRegister注解的方法抽取出来
            List<Method> serviceRegisterMethods = Lists.newArrayList();
            for (Method method : allMethods) {
                ServiceRegister[] methodServiceRegisters = method.getAnnotationsByType(ServiceRegister.class);
                if (methodServiceRegisters != null && methodServiceRegisters.length > 0) {
                    RequestMapping[] methodRequestMappings = method.getAnnotationsByType(RequestMapping.class);
                    if (methodRequestMappings != null && methodRequestMappings.length > 0) {
                        serviceRegisterMethods.add(method);
                    }
                }
            }

            for (Method method : serviceRegisterMethods) {
                String methodName = method.getName();

                Set<String> uriParts = new HashSet<>();
                RequestMapping[] methodRequestMappings = method.getAnnotationsByType(RequestMapping.class);
                for (RequestMapping methodRequestMapping : methodRequestMappings) {
                    uriParts.addAll(Arrays.asList(methodRequestMapping.value()));
                }

                Set<String> serviceUris = new HashSet<>();
                for (String uriPart : uriParts) {
                    if (uriPres.isEmpty()) {
                        String uri = this.contextPath + "/" + uriPart;
                        serviceUris.add(uri.replaceAll("//+", "/").replaceAll("/$", "").replaceAll("^/", "") + ".json");
                    } else {
                        for (String uriPre : uriPres) {
                            String uri = this.contextPath + "/" + uriPre + "/" + uriPart;
                            serviceUris.add(uri.replaceAll("//+", "/").replaceAll("/$", "")
                                    .replaceAll("^/", "") + ".json");
                        }
                    }
                }

                Set<String> servicePartNames = new HashSet<>();
                ServiceRegister[] methodServiceRegisters = method.getAnnotationsByType(ServiceRegister.class);
                for (ServiceRegister methodServiceRegister : methodServiceRegisters) {
                    servicePartNames.addAll(Arrays.asList(methodServiceRegister.value()));
                }

                Set<String> serviceNames = new HashSet<>();
                for (String servicePartName : servicePartNames) {
                    if (serviceNamePres.isEmpty()) {
                        serviceNames.add(servicePartName);
                    } else {
                        for (String serviceNamePre : serviceNamePres) {
                            String serviceName = serviceNamePre + "-" + servicePartName;
                            serviceNames.add(serviceName);
                        }
                    }
                }

                for (String serviceName : serviceNames) {
                    String fullName = className + "." + methodName;
                    if (services.containsKey(serviceName)) {
                        ServiceDescription duplicateServiceDescription = services.get(serviceName);
                        throw new DuplicateServiceNameException(
                                String.format("%s与%s的服务名%s重复，请检查!", fullName,
                                        duplicateServiceDescription.getFullName(), serviceName)
                        );
                    }
                    for (String serviceUri : serviceUris) {
                        ServiceDescription description = new ServiceDescription(serviceName, serviceUri, fullName);
                        services.put(serviceName, description);
                    }
                }
            }
        }
        return services.values();
    }
}
