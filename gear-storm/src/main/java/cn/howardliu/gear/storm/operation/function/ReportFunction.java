package cn.howardliu.gear.storm.operation.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

/**
 * <br/>create at 16-7-12
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
