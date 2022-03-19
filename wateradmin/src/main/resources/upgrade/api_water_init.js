if(__global.water && __global.water.ver==1){
    return {msg:'old'};
}

// 不带 var 开头的变量为引擎全局变量
__global.water = {ver:2};

water = __global.water;

WaterClient = Java.type('org.noear.water.WaterClient');
WaterProxy  = Java.type('org.noear.water.WaterProxy');

water.client = WaterClient;
water.proxy  = WaterProxy;

water.cfg = function(tagKey){return WaterClient.Config.getByTagKey(tagKey)};
water.db  = function(tagKey){return water.cfg(tagKey).getDb()};
water.rd  = function(tagKey,i){return water.cfg(tagKey).getRd(i)};
water.mg  = function(tagKey,c){return water.cfg(tagKey).getMg(c)};
water.updateCache = function(tags){WaterClient.Notice.updateCache(tags)};

water.job = function(service,job,args){if(!args){args={}} var rst = WaterProxy.runJob(service,job,args); XUtil.log("Job return: "+rst); if('OK' != rst){throw "Job return: "+rst;}else{return rst;}}
water.getStatus = function(addrees){return WaterProxy.runStatus(addrees);}


water.call = function(service,fun,args){if(!args){args={}} return WaterProxy.call(service,fun,args);};

water.faas = function(path,args){if(!args){args={}} return WaterProxy.paas(path,args)};
water.raas = function(path,args){var args2={};if(args){for(var k in args){var v=args[k];if(v){if(v instanceof Object){args2[k]=JSON.stringify(v)}else{args2[k]=v}}}}return WaterProxy.raas(path,args2)};
water.paas = function(path,args){ return water.faas(path, args);};

water.logTrace = function(logger,data){ return WaterProxy.logTrace(logger, data);};
water.logDebug = function(logger,data){ return WaterProxy.logDebug(logger, data);};
water.logInfo  = function(logger,data){ return WaterProxy.logInfo(logger, data); };
water.logWarn  = function(logger,data){ return WaterProxy.logWarn(logger, data); };
water.logError = function(logger,data){ return WaterProxy.logError(logger, data);};


water.heihei = function(target,msg,sign){return WaterClient.Notice.heihei(target, msg,sign);};

water.sendMessage = function(topic,message){ return WaterClient.Message.sendMessage('',topic, message); }

LocalDate = Java.type('java.time.LocalDate');
LocalTime = Java.type('java.time.LocalTime');
LocalDateTime = Java.type('java.time.LocalDateTime');


return 'OK';