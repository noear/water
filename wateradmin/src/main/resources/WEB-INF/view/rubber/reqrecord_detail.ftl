<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 请求记录详情</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        detail{padding: 0px;}
        header td{padding: 0px!important;height: 20px;}
        datagrid td{padding: 4px!important;}

        tr.t5{text-decoration:line-through}
        tr a{text-decoration:none}
        tr.ex,tr.ex a{color: red!important;}
    </style>
</head>
<body>
<block class="blockquote">
    <div class="center mar10-b">
        <a onclick="history.back(-1)" class="btn w70">返回</a>
        <a onclick="f1.submit()" class="btn w70 mar10-l">重试</a>
        <a onclick="f2.submit()" class="btn w70 mar10-l">调试</a>
    </div>

    <table class="100p">
        <tr>
            <td width="80px"><b>请求ID：</b></td>
            <td><b>${log.request_id}（No.${log.log_id}）</b></td>
        </tr>
        <tr>
            <td>请求时间：</td>
            <td>${log.start_fulltime}（${log.timespan}ms）</td>
        </tr>
        <tr>
            <td>请求参数：</td>
            <td class="break">${log.args_json!}</td>
        </tr>
        <tr>
            <td>回调地址：</td>
            <td>${log.callback!}</td>
        </tr>
        <tr>
            <td>记算方案：</td>
            <td>${log.scheme_tagname!}</td>
        </tr>
        <tr>
            <td>处理策略：</td>
            <td>${log.policy}</td>
        </tr>
    </table>
</block>

<main>
    <detail>
        <article>
            <#if matcher??>
            <block>
                <div>匹配报告：${matcher.value} / ${matcher.total}</div>
                <datagrid>
                    <table>
                        <thead>
                        <tr>
                            <td width="40px">序号</td>
                            <td width="180px">计算方案</td>
                            <td >结果</td>
                            <td width="70px">处理时间</td>
                            <td width="110px">规则数</td>
                            <td width="70px">匹配数</td>
                            <td width="70px">规则关系</td>
                        </tr>
                        </thead>
                        <tbody>
                        <#list matcher.details?values as m>
                            <tr>
                                <td>${m_index + 1}</td>
                                <td class="left">${m.s}</td>
                                <td class="left">
                                    <#if m.ok==1>
                                        true
                                    </#if>
                                    <#if m.ok==0>
                                        false
                                    </#if>
                                </td>
                                <td class="right">${m.ts}</td>
                                <td class="right">${m.t}</td>
                                <td class="right">${m.v}</td>
                                <td class="right">${m.r}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </datagrid>
            </block>
            <block>
                <div>评估报告：${evaluation.score} - ${log.getEvaluationEnum(evaluation.advise)} (异常:${evaluation.exception})</div>
                <datagrid>
                    <table>
                        <thead>
                        <tr>
                            <td width="40px">序号</td>
                            <td width="180px">计算方案</td>
                            <td >规则</td>
                            <td width="70px">处理时间</td>
                            <td width="110px">触发值</td>
                            <td width="70px">评估分值</td>
                            <td width="70px">评估建议</td>
                        </tr>
                        </thead>
                        <tbody>
                        <#list evaluation.details as m>
                            <#if m.m==false>
                                <#if m.v?contains('null')>
                                    <tr class="t5 ex">
                                <#else>
                                    <tr class="t5">
                                </#if>
                            <#else>
                              <tr>
                            </#if>
                                <td>${m_index+1}</td>
                                <td class="left">${m.s}</td>
                                <td class="left break">
                                    <a target="_blank" href="/rubber/scheme/rule/edit?rule_id=${m.id}&debug_args=${log.args_json_str()}">
                                        ${m.r}
                                    </a>
                                </td>
                                <td class="right">${m.ts}</td>
                                <td class="right break">${m.v}</td>
                                <td class="right">${m.n}</td>
                                <td class="right<#if (m.a<0)> t4</#if>">${log.getEvaluationEnum(m.a)}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </datagrid>
            </block>

            <block>
                <div>会话数据：</div>
                <br/>
                <div style="background: #fff;padding: 5px;" class="break">
                    ${log.session_json!}
                </div>
            </block>
            <block>
                <div>数据模型：</div>
                <br/>
                <div style="background: #fff;padding: 5px;" class="break">
                    ${log.model_json!}
                </div>
            </block>
            </#if>
        </article>
    </detail>
    <form id="f1" action="${raas_uri}/release" target="_blank" method="get">
        <input type="hidden" name="scheme" value="${log.scheme_tagname}">
        <input type="hidden" name="args" value='${log.args_json}'>
        <input type="hidden" name="policy" value="2001">
        <input type="hidden" name="request_id" value="${log.request_id}">
        <input type="hidden" name="callback" value="x">
    </form>
    <form id="f2" action="${raas_uri}/debug" target="_blank" method="get">
        <input type="hidden" name="scheme" value="${log.scheme_tagname}">
        <input type="hidden" name="args" value='${log.args_json}'>
        <input type="hidden" name="policy" value="2001">
    </form>
</main>
<script>

</script>
</body>
</html>