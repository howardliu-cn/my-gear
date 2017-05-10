package cn.howardliu.gear.monitor.custom.modeler;

import javax.management.MBeanParameterInfo;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class MethodInfo extends FeatureInfo {
    protected ReadWriteLock parametersLock = new ReentrantReadWriteLock();
    protected ParameterInfo[] parameters = new ParameterInfo[0];

    public ParameterInfo[] getSignature() {
        parametersLock.readLock().lock();
        try {
            return this.parameters;
        } finally {
            parametersLock.readLock().unlock();
        }
    }

    public MethodInfo addParameter(ParameterInfo parameter) {
        parametersLock.writeLock().lock();
        try {
            ParameterInfo[] results = new ParameterInfo[this.parameters.length + 1];
            System.arraycopy(this.parameters, 0, results, 0, this.parameters.length);
            results[this.parameters.length] = parameter;
            this.parameters = results;
            this.info = null;
        } finally {
            parametersLock.writeLock().unlock();
        }
        return this;
    }

    protected MBeanParameterInfo[] getMBeanParameterInfos() {
        ParameterInfo[] params = getSignature();
        MBeanParameterInfo[] parameters = new MBeanParameterInfo[params.length];
        for (int i = 0; i < params.length; i++) {
            parameters[i] = params[i].getInfo();
        }
        return parameters;
    }
}
