<!DOCTYPE HTML>
<html>
<head>
  <title>${app} - 登录</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
  <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
  <link rel="stylesheet" href="${css}/main.css" />
  <script src="/_session/domain.js"></script>
  <script type="text/javascript" src="${js}/jtadmin.js" ></script>
  <style type="text/css">

    section{width: 400px; padding: 50px 60px 50px 50px; left: calc(50vw - 200px); top: calc(50vh - 5vh - 150px); position: absolute; background: #ddd;}
    section table {width: 100%; }
    section table th{text-align: right;}
    section table td{padding-top: 5px; padding-bottom: 5px;}
    section table td img{height: 26px; float: left;}
    section table input{height: 30px!important; width: 100%;}
    section table button{color: #000; height: 30px; width: 120px;}

    main > p{margin: 10px;line-height: 30px;}
  </style>
  <script type="text/javascript">
    function checkClick(){
      $.ajax({
        url:"/login/ajax/check",
        data:$("form").serialize(),
        success:function(data){
          if(data.code==1){
            location.href=data.url;
          }
          else
            alert(data.msg);
        }
      });
      return false;
    }
    function checkKey() {
      if (window.event.keyCode == 13)
        checkClick();
    }

  </script>
</head>
<body onkeydown="checkKey()">
<main>
  <p>${title}</p>
  <br />
  <section>
    <form>
      <table>
        <tr><th width="70">账号：</th>
          <td colspan="2"><input type="text" name="userName" placeholder="用户名"/></td>
        </tr>
        <tr><th>密码：</th>
          <td colspan="2"> <input type="password" name="passWord" placeholder="密码"/></td>
        </tr>
        <tr><th>验证码：</th>
          <td><input type="text" name="captcha"/></td>
          <td style="width: 60px; padding-left: 10px;">
            <img src="/login/validation/img"
                 onclick="this.src='/login/validation/img?d='+Math.random();"/></td>
        </tr>

        <tr>
          <th></th>
          <td colspan="2">
            <br />
            <button type="button" onclick="checkClick()" >登录</button>
          </td>
        </tr>
      </table>
    </form>
  </section>
</main>

</body>
</html>
