
// 模型类（基于prototype实现）
//
this.MM_beauty_num_risk_model = function (c) {
    this._$build(c);
};
this.MM_beauty_num_risk_model.prototype = {
    _$_mobile: function () {
        if (this._mobile) {
            return this._mobile;
        } else {
            return water.db("angel.sponge_angel").table("user")
                .where("open_id = ?", this.open_id())
                .select("mobile")
                .caching(cache)
                .getValue();
        }
    },
    mobile: function () {
        if (!this._$mx.mobile) {
            this._$mx.mobile = 1;
            this._mobile = this._$md.mobile = this._$_mobile();
        };
        return this._mobile;
    },
    open_id: function () {
        return this._$md.open_id = this._open_id;
    },
    _$bind: function (d) {
        if (d) {
            for (var k in d) {
                this['_' + k] = d[k];
            };
        };
    },
    _$build: function (c) {
        this._$md = {};
        this._$mx = {};
        this._$mc = c;
        this._$bind(c.args());
        if (this._$init) {
            var d = db2json(this._$init());
            this._$bind(d);
        };
    }
};

// 实例化模型
//
"new MM_beast_user_state(JTAPI.getData('6f14141d82974c0aa838525073e35582'));"

//模型运行器
//
this.MM_run = function(c){
	var m = c.model();
    m._$is_debug = c.is_debug;

	for(var f in m){
		if(!(f.startsWith('_'))){
			try {
                m[f]();
            }catch (err){
                throw f+' error:'+err;
			}
		};
	};
};

//字段运行器
this.MM_run_item = function(c,key){
    var m = c.model();
    m._$is_debug=c.is_debug;
	m[key]();
};
