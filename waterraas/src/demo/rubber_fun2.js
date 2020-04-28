
//计算方案单例对象
//
this.SR_beauty_rule_1 = {
    name: "beauty/rule_1",
    name_display: "产品准入",
    total: 3,
    relation: 0,
    model: "beauty/pro_acc_model",
    rule: {
        r36: {
            id: 36,
            check: function (c, m) {
                c.v(m.idcard_city());
            },
            test: function (c, m) {
                return DD_angel_risk_city.scan(m.idcard_city());
            }
        },
        r37: {
            id: 37,
            check: function (c, m) {
                c.v(m.idcard_effective());
            },
            test: function (c, m) {
                return m.idcard_effective() == 1;
            }
        },
        r100: {
            id: 100,
            check: function (c, m) {
                c.v(m.idcard_province());
            },
            test: function (c, m) {
                return DD_angel_risk_province.scan(m.idcard_province());
            }
        }
    },
    event: function (c, m, sm) {}
};

//规则测试器
//
this.SR_test = function (sr,r,c,m) {
    var st = c.t();
    var xr = false;
    if(c.is_debug){
        try {
            xr = r.check(c, m);
        }catch (err){
            throw new Error('S:'+sr.name+'/r'+r.id +' '+err);
        }
    }

    try {
        if(c.is_debug == false){
            xr = r.check(c,m);
        }

        if(xr) {
            return st.stop(r.test(c, m), false);
        }else{
            return st.stop(false, true);
        }
    }catch(err){
        c.e(sr.name_display, r.id, err.message);
        return st.stop(false, true);
    }
};

//计算方案运行器
//
this.SR_run = function (c,sr) {
	var m = c.model(sr.model);

    var sm = c.start('R', sr.name, sr.name_display, sr.relation, sr.total);

    for(var k in sr.rule){
        var r = sr.rule[k];

        if (c.judge(sm, r.id, SR_test(sr,r,c,m))) {
            break;
        };
    }

    c.end('R', sm);

    sr.event(c,m,sm);
};

//规则运行器
//
this.SR_run_rule_all = function (c,sr) {
    var m = c.model(sr.model);

    var sm = c.start('R', sr.name, sr.name_display, sr.relation, sr.total);

    for(var k in sr.rule){
        var r = sr.rule[k];
        if (c.judge(sm, r.id, SR_test(sr,r,c,m))) {
            break;
        };
    }

    c.end('R', sm);
};

//单项规则运行器
//
this.SR_run_rule_one = function (c,sr,key) {
    var m = c.model(sr.model);
    m._$is_debug=c.is_debug;
	var r = sr.rule[key];

	if(r) {
        var sm = c.start('R', sr.name, sr.name_display, sr.relation, 1);

        c.judge(sm, r.id, SR_test(sr,r,c,m));

        c.end('R', sm);
    }else{
	    c.session.error="doesn't exist R:"+key;
    }
};

//事件运行器
//
this.SR_run_event = function (c,sr) {
    var m = c.model(sr.model);

    var sm = c.start('R', sr.name, sr.name_display, sr.relation, 0);

    sr.event(c,m,sm);

    c.end('R', sm);
};