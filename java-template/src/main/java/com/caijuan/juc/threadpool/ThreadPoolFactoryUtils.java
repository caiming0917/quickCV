package com.caijuan.juc.threadpool;

import com.caijuan.utils.SmallTool;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.PropertyConfigurator;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;

import static java.lang.System.out;


@Slf4j
public final class ThreadPoolFactoryUtils {
    public static void setLogConfig() {
        // 参数存储
        Properties properties =new Properties();
        // 清楚原有的所有参数
        properties.clear();
        // 将所有在log4j中的配置全部set到properties中
        properties.setProperty("log4j.rootLogger","INFO,stdout,R");
        properties.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");
        properties.setProperty("log4j.appender.stdout.layout","org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%d{yyyy-MM-dd HH:mm:ss,SSS}][%c]%m%n");
        properties.setProperty("log4j.appender.R","org.apache.log4j.DailyRollingFileAppender");
        properties.setProperty("log4j.appender.R.File","D://mainLog.info");
        properties.setProperty("log4j.appender.R.Encoding","UTF-8");
        properties.setProperty("log4j.appender.R.Append","true");
        properties.setProperty("log4j.appender.R.Threshold","DEBUG");
        properties.setProperty("log4j.appender.R.layout","org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.R.layout.ConversionPattern","%d{yyyy-MM-dd HH:mm:ss,SSS}][%c]%m%n");
        properties.list(out);
        // 获取一下我们存进去的信息看看对不对
        Set<Object> set = properties.keySet();
        set.forEach( key -> out.println(key+"="+properties.get(key.toString())));
        // 将我们辛苦的参数放在配置中 这是最重要的
        PropertyConfigurator.configure(properties);
    }


    // static final Logger info = LoggerFactory.getLogger(ThreadPoolFactoryUtils.class);

    /**
     * 通过 threadNamePrefix 来区分不同线程池（我们可以把相同 threadNamePrefix 的线程池看作是为同一业务场景服务）。
     * key: threadNamePrefix
     * value: threadPool
     */
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    private ThreadPoolFactoryUtils() {

    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix) {
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(customThreadPoolConfig, threadNamePrefix, false);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, CustomThreadPoolConfig customThreadPoolConfig) {
        return createCustomThreadPoolIfAbsent(customThreadPoolConfig, threadNamePrefix, false);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(CustomThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix,
                k -> createThreadPool(customThreadPoolConfig, threadNamePrefix, daemon));
        // 如果 threadPool 被 shutdown 的话就重新创建一个
        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadNamePrefix);
            threadPool = createThreadPool(customThreadPoolConfig, threadNamePrefix, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
        return threadPool;
    }

    /**
     * shutDown 所有线程池
     */
    public static void shutDownAllThreadPool() {
        SmallTool.info("call shutDownAllThreadPool method");
        THREAD_POOLS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            SmallTool.info(String.format("shut down thread pool [{%s}] [{%s}]", entry.getKey(), executorService.isTerminated()));
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                SmallTool.info("Thread pool never terminated");
                executorService.shutdownNow();
            }
        });
    }

    private static ExecutorService createThreadPool(CustomThreadPoolConfig customThreadPoolConfig,
                                                    String threadNamePrefix, Boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(
                customThreadPoolConfig.getCorePoolSize(),
                customThreadPoolConfig.getMaximumPoolSize(),
                customThreadPoolConfig.getKeepAliveTime(),
                customThreadPoolConfig.getUnit(),
                customThreadPoolConfig.getWorkQueue(),
                threadFactory);
    }

    /**
     * 创建 ThreadFactory 。如果threadNamePrefix不为空则使用自建ThreadFactory，否则使用defaultThreadFactory
     *
     * @param threadNamePrefix 作为创建的线程名字的前缀
     * @param daemon           指定是否为 Daemon Thread(守护线程)
     * @return ThreadFactory
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(daemon).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }

    /**
     * 打印线程池的状态
     *
     * @param threadPool 线程池对象
     */
    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-thread-pool-status", false));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            SmallTool.info("============ThreadPool Status=============");
            SmallTool.info(String.format("ThreadPool Size: [{%d}]", threadPool.getPoolSize()));
            SmallTool.info(String.format("Active Threads: [{%d}]", threadPool.getPoolSize()));
            SmallTool.info(String.format("Number of Tasks : [{%d}]", threadPool.getPoolSize()));
            SmallTool.info(String.format("Number of Tasks in Queue: [{%d}]", threadPool.getPoolSize()));
            SmallTool.info("===========================================");
        }, 0, 10, TimeUnit.SECONDS);
    }

}
