package cn.howardliu.gear.storm.operation.function;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>create at 16-7-12
 *
 * @author liuxh
 * @since 1.0.2
 */
public class ReportFunction extends BaseFunction {
    private static final Logger logger = LoggerFactory.getLogger(ReportFunction.class);

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        logger.info("输入数据：{}", tuple);
        collector.emit(tuple);
    }
}
