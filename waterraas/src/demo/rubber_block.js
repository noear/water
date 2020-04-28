//数据块单例对象
//
this.DD_beast_beast_auto_buy =  {
    name:"tag/r001",
    name_display: "风控城市列表",
    block_id:1,
    block_key:"block_1",
    db:"tag/name",
    tb:"",
    $:function () {
        
    },
    _$_scan:function (x,cmd) {
        return this.$().where("").exists();
    },
    scan:function (x,cmd) {
        try {
            return this._$_scan(x,cmd);
        } catch (err) {
            throw new Error('D:' + this.name + ' ' + err);
        }
    }
};


//数据块运行器
//
this.DD_run = function (dd,arg) {
    return dd.scan(arg.x, arg.cmd);
};

//数据块调用函数
//
this.$dblock = function (b,x,cmd) {
    var fk = 'DD_'+b.replace('/','_');
    var fo = this[fk];
    if(!fo) {
        include('dd:' + b);
        this[fk].scan(x,cmd);
    }else{
        return fo.scan(x,cmd);
    }
};

//示例
$dblock('tag/r001',x);
$dblock('tag/r001',x,'map');