/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.solon.extend.schedule;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.LifecycleIndex;
import org.noear.solon.core.Plugin;

/**
 * solon.extend.schedule 相对于 cron4j-solon-plugin 的区别：
 *
 * getInterval 和 getThreads 可动态控制；例，夜间或流量小时弹性变小数值.
 *
 * */
public class XJobPluginImp implements Plugin {
    @Override
    public void start(AppContext context) {
        context.lifecycle(LifecycleIndex.PLUGIN_BEAN_USES, () -> {
            context.beanForeach((v) -> {
                if (v.raw() instanceof IJob) {
                    JobManager.register(new JobEntity(v.name(), v.raw()));
                }
            });
        });

        context.lifecycle(Integer.MAX_VALUE, () -> {
            JobManager.run(JobRunner.global);
        });
    }
}