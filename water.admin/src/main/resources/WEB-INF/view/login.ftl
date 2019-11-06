<!DOCTYPE HTML>
<html>
<head>
  <title>${app} - 登录</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
  <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
  <link rel="stylesheet" href="${css}/main.css" />
  <script type="text/javascript" src="${js}/lib.js" ></script>
  <style type="text/css">
    #login{width: 320px; margin:auto; padding: 40px; margin-top: 100px;  background: #ddd;}
    #login table {width: 100%; }
    #login table th{text-align: right;width: 75px;}
    #login table td{padding-top: 5px; padding-bottom: 5px;}
    #login table td img{height: 30px; float: left;}
    #login table input{height: 30px; width: 100%;}
    #login table button{color: #000; height: 30px; width: 120px;}

    main > p{margin: 10px;}


    row{display: table !important;}
    row > * ,cell{display: table-cell !important; vertical-align: middle;}
    row > right{text-align: right !important;}
    row > left{text-align: left !important;}
  </style>
  <script type="text/javascript">
      function checkClick(){
          $.ajax({
              url:"/login/ajax/check",
              data:$("#login form").serialize(),
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
  <div id="login">
    <form method="post">
      <table>
        <tr><th>账号：</th>
          <td colspan="2"><input type="text" name="userName" placeholder="用户名"/></td>
        </tr>
        <tr><th>密码：</th>
            <td colspan="2"> <input type="password" name="passWord" placeholder="密码"/></td>
        </tr>
        <tr><th>验证码：</th>
            <td><input type="text" name="validationCode"/></td>
            <td style="width: 62px; padding-left: 10px;"><img src="/login/validation/img" onclick="reloadimg(this)"/></td>
        </tr>

        <tr>
          <th></th>
          <td colspan="2">
            <br />
            <button type="button" onclick="checkClick()">登录</button>
          </td>
        </tr>
      </table>
    </form>
  </div>
</main>

<@footer />
</body>
</html>
<script type="text/javascript">
  function reloadimg(img) {
    $(img).attr("src","/login/validation/img?date"+Date());
  }
</script>
