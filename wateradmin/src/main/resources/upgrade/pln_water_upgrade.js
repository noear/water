let upgrade_tag = 'water_upgrade_tag';

//1.版本检测
let ver_old_str = XUtil.cfgGet(upgrade_tag);
if(!ver_old_str){
    ver_old_str = '0';
}

let ver_new = 1;
let ver_old = parseInt(ver_old_str);

if(ver_old >= ver_new){
    return 'No upgrade';
}

//2.开始更新

//for 2.5.8
if(ver_old == 0){
    let db2 = water.db('water/water');
    //::规则计算
    let idList = db2.table('grit_resource').whereEq('resource_pid',117).selectArray('resource_id');
    if(idList.size() > 0){
        db2.table('grit_resource_linked').whereIn('resource_id',idList).delete();
        db2.table('grit_resource').whereEq('resource_pid',117).delete();
        db2.table('grit_resource').whereEq('resource_id',117).delete();
    }

    //::Ops
    let rst = db2.table('grit_resource').set('link_uri','/cfg/server').set('resource_pid',9).set('order_index',21)
        .whereEq('resource_id',129).andNeq('resource_pid',9)
        .update();

    if(rst){ //如果更新成功
        let tmpMap = db2.table('grit_resource').whereEq('resource_id',129).selectMap('*');

        tmpMap.remove("resource_id");

        db2.table('grit_resource')
            .setMap(tmpMap).set('display_name','$').set('order_index',20).set('link_uri','').set("guid", XUtil.guid())
            .usingNull(false)
            .usingExpr(false)
            .insert();
    }

    idList = db2.table('grit_resource').whereEq('resource_pid',13).selectArray('resource_id');
    if(idList.size() > 0){
        db2.table('grit_resource_linked').whereIn('resource_id',idList).delete();
        db2.table('grit_resource').whereEq('resource_pid',13).delete();
        db2.table('grit_resource').whereEq('resource_id',13).delete();
    }

    //::Dev
    db2.table('grit_resource').set('display_name','开发助手').whereEq('resource_id',12).update();
}

//3.设定版本
XUtil.cfgSet(upgrade_tag, ver_new+'');

return 'OK';