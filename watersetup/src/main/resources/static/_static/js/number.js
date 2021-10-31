
//设置文本框只能输入数字和一个小数点
function RepFloat(obj){
    obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
    obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数
    if(obj.value.indexOf(".")< 0 && obj.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
        obj.value= parseFloat(obj.value);
    }
}
///设置文本框只能输入数字
function RepNumber(obj) {
    if(obj.value.length==1){
        obj.value=obj.value.replace(/[^1-9]/g,'')
    }else{
        obj.value=obj.value.replace(/\D/g,'')
    }
}