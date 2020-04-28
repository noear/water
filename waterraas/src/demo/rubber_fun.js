this.SR_beast_beast_auto_buy = function(c) {
	var m = c.model();
	var sr = {
		"name": "自动购买资料",
		"total": 8,
		"value": 0
	};
	c.start('R', sr.name, sr.total);
	if (c.judge(sr.name, 28, (c.v(m.td_score()) <= rubber_text_val(c, m)))) {
		return;
	};
	if (c.judge(sr.name, 72, (rubber_test_check(c, c.v(m.ccard_num()))))) {
		return;
	};
	if (c.judge(sr.name, 116, (c.v(m.age()) > 22 && c.v(m.audit_status()) == 1))) {
		return;
	};
	if (c.judge(sr.name, 27, (c.v(m.zhima_score()) >= 625))) {
		return;
	};
	if (c.judge(sr.name, 29, (c.v(m.bs_score()) <= 80))) {
		return;
	};
	if (c.judge(sr.name, 30, (c.v(m.age()) >= 22 && c.v(m.age()) <= 40))) {
		return;
	};
	if (c.judge(sr.name, 31, (c.v(m.mobile_use_time()) >= 12))) {
		return;
	};
	if (c.judge(sr.name, 115, (c.v(m.eval_score()) > 50))) {
		return;
	};
	c.session.xx = 2;
	c.end('R', sr.name);
};