this.MM_beast_user_state = function(c) {
	var m = {
		_$md: {},
		_$mc: c,
		_$_age: function() {
			var idc = this.idcard();
			if (idc) {
				return 2018 - parseInt(idc.substr(6, 4));
			}

			return -1;

		},
		age: function() {
			if (!this._$md.age) {
				this._age = this._$md.age = this._$_age();
			};
			return this._age;
		},
		app_name: function() {
			return this._$md.app_name = this._app_name;
		},
		audit_status: function() {
			return this._$md.audit_status = this._audit_status;
		},
		auth_fulltime: function() {
			return this._$md.auth_fulltime = this._auth_fulltime;
		},
		auth_status: function() {
			return this._$md.auth_status = this._auth_status;
		},
		bs_score: function() {
			return this._$md.bs_score = this._bs_score;
		},
		_$_ccard_num: function() {
			var mob = this.mobile();

			if (mob) {
				var db = water.db('hold.rock_user');
				var uid = db.table('user').where('mobile=?', mob).caching(cache).select('user_id').getValue();
				if (uid > 0) {
					return db.table('user_ex_ccard').where('user_id=?', uid).caching(cache).count();
				}
			}

			return -1;
		},
		ccard_num: function() {
			if (!this._$md.ccard_num) {
				this._ccard_num = this._$md.ccard_num = this._$_ccard_num();
			};
			return this._ccard_num;
		},
		city: function() {
			return this._$md.city = this._city;
		},
		city_code: function() {
			return this._$md.city_code = this._city_code;
		},
		contact_mobole: function() {
			return this._$md.contact_mobole = this._contact_mobole;
		},
		create_fulltime: function() {
			return this._$md.create_fulltime = this._create_fulltime;
		},
		day7_ious_end_num: function() {
			return this._$md.day7_ious_end_num = this._day7_ious_end_num;
		},
		_$_eval_score: function() {
			return this._$mc.score();
		},
		eval_score: function() {
			return this._eval_score = this._$md.eval_score = this._$_eval_score();
		},
		face_matched: function() {
			return this._$md.face_matched = this._face_matched;
		},
		history_ious_end_num: function() {
			return this._$md.history_ious_end_num = this._history_ious_end_num;
		},
		idcard: function() {
			return this._$md.idcard = this._idcard;
		},
		_$_is_pass: function() {
			return 1 - this._is_pass;
		},
		is_pass: function() {
			if (!this._$md.is_pass) {
				this._is_pass = this._$md.is_pass = this._$_is_pass();
			};
			return this._is_pass;
		},
		mobile: function() {
			return this._$md.mobile = this._mobile;
		},
		_$_mobile_use_time: function() {
			return parseInt(this._mobile_use_time / 30);
		},
		mobile_use_time: function() {
			if (!this._$md.mobile_use_time) {
				this._mobile_use_time = this._$md.mobile_use_time = this._$_mobile_use_time();
			};
			return this._mobile_use_time;
		},
		real_name: function() {
			return this._$md.real_name = this._real_name;
		},
		role: function() {
			return this._$md.role = this._role;
		},
		similar_status: function() {
			return this._$md.similar_status = this._similar_status;
		},
		status: function() {
			return this._$md.status = this._status;
		},
		td_score: function() {
			return this._$md.td_score = this._td_score;
		},
		th3_delinquency_num: function() {
			return this._$md.th3_delinquency_num = this._th3_delinquency_num;
		},
		th3_lending_num: function() {
			return this._$md.th3_lending_num = this._th3_lending_num;
		},
		user_id: function() {
			return this._$md.user_id = this._user_id;
		},
		zhima_a_score: function() {
			return this._$md.zhima_a_score = this._zhima_a_score;
		},
		zhima_score: function() {
			return this._$md.zhima_score = this._zhima_score;
		},
		bind: function(d) {
			if (d) {
				for (var k in d) {
					this['_' + k] = d[k];
				};
			};
		},
		build: function(args) {
			this.bind(args);
			if (this.init) {
				var d = db2json(this.init(), 1);
				this.bind(d);
			};
			return this;
		},
		toJSON: function() {
			return JSON.stringify(this._$md);
		},
		init: function() {
			return water.db('beast.beast_user').table('user_info').where('user_id=?', this._user_id).select('*').getDataItem();
		}
	};
	return m.build(c.args());
};