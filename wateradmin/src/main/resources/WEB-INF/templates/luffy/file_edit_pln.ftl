<!doctype html>
<html class="frm10">
<head>
    <title>${app} - 定时任务</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/laydate/laydate.js"></script>
    <script>
        window.postReview = function(vm){
            if(vm.path.startsWith("/" + vm.tag) == false){
                alert("路径必须以/{tag}开始");
                return false;
            }
            return true;
        }

        function jt_goback() {
            let tag = $('#tag').val().trim();
            let state = $('#is_disabled').prop('checked') ? 1 : 0;

            parent.location = './home?tag_name=' + tag + '&state=' + state;
        }
    </script>
</head>
<body>
<datalist id="content_type_list">
    <option value="text/plain"></option>
    <option value="text/css"></option>
    <option value="text/xml"></option>
    <option value="text/html"></option>
    <option value="text/javascript"></option>
    <option value="image/jpeg"></option>
    <option value="image/png"></option>
    <option value="image/gif"></option>
    <option value="application/javascript"></option>
    <option value="application/octet-stream"></option>
    <option value="application/json"></option>
    <option value="code/internal">内部代码</option>
</datalist>

<datalist id="edit_mode_list">
    <option value="javascript"></option>
    <option value="groovy"></option>
    <option value="python"></option>
    <option value="ruby"></option>
    <option value="lua"></option>
    <option value="graaljs"></option>
    <option value="sh"></option>

    <option value="freemarker"></option>
    <option value="velocity"></option>
    <option value="thymeleaf"></option>
    <option value="beetl"></option>
    <option value="enjoy"></option>

    <option value="xml"></option>
    <option value="html"></option>
    <option value="css"></option>
    <option value="mysql"></option>
    <option value="markdown"></option>
    <option value="json"></option>
</datalist>



<toolbar class="blockquote">
    <left class="ln30">
        <h2><a href="javascript:history.back()" class="noline">定时任务</a></h2> / 设置
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" jt-post="./ajax/save"
                jt-goback="jt_goback()" jt-ctls>保存</button>
        <#if ((m1.path!'')?length > 1)>
            <button type="button" class="minor" jt-post="./ajax/del?id=${id}"
                    jt-goback="jt_goback()" jt-confirm="del">删除</button>
        </#if>
    </right>
</toolbar>

<detail>
    <form>
        <input type="hidden" id="id" value="${id}" />
        <table>
            <tbody>
            <tr>
                <th width="100">tag</th><td><input type="text" id="tag"  value="${m1.tag!tag}" required jt-alert="请输入包" />*
                    <n>（字母或数字或_组成）</n>
                </td>
            </tr>
            <tr>
                <th>路径</th>
                <td><input type="text" class="longtxt" id="path" value="${m1.path!('/'+tag+'/')}" required jt-alert="请输入路径"/>*
                    <n-l>例：/{tag}/xxx 或 /{tag}_xxx/xxx</n-l>
                </td>
            </tr>
            <tr>
                <th>编辑模板</th>
                <td><input type="text" id="edit_mode" value="${m1.edit_mode!'javascript'}" list="edit_mode_list" autocomplete="off" required jt-alert="请选择编辑模式" />*</td>
            </tr>

            <tr>
                <th>备注</th><td><input type="text" id="note" value="${m1.note!}"/></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <checkbox>
                        <label class="mar10-r"><input type="checkbox" id="is_disabled" ${m1.disabled()?string("checked","")} /><a>禁止使用</a></label>
                    </checkbox>
                </td>
            </tr>
            </tbody>
            <tbody id="task_pop">
            <tr>
                <td colspan="2"><hr/></td>
            </tr>
            <tr>
                <th>任务开始时间</th><td><input type="text" id="plan_begin_time"
                                          value="${(m1.plan_begin_time?string("yyyy-MM-dd HH:mm:ss"))!}"
                                          jt-laydate="datetime"
                                          placeholder="yyyy-MM-dd HH:mm:ss"
                                          autocomplete="off"/>
                </td>
            </tr>
            <tr>
                <th>任务执行间隔</th><td><input type="text" id="plan_interval" value="${m1.plan_interval!'1h'}"/><span class="t5">（s秒，m分钟，h小时，d天 或 Cron7 或 Cron7+时区）</span></td>
            </tr>
            <tr>
                <th>任务执行次数</th><td><input type="text" id="plan_max" value="${m1.plan_max!0}"/><span class="t5">（0为不限次数）</span></td>
            </tr>
            <tr>
                <th><span class="t5">最后执行时间</span></th><td><input type="text" id="plan_last_time"
                                          value="${(m1.plan_last_time?string("yyyy-MM-dd HH:mm:ss"))!}"
                                          jt-laydate="datetime"
                                          placeholder="yyyy-MM-dd HH:mm:ss"
                                          autocomplete="off"/>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
    </detail>
</body>
</html>