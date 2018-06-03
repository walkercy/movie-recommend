$('#signin').click(function () {
    var data = {
        "username" : $('#inputEmail').val(),
        "password" : $('#inputPassword').val()
    }
    console.log(data)
    $.ajax({
        url : "/api/user/sign_up",
        type : "post",
        data : data,
        success : function (res) {
            if (res.code == 200) {
                alert("注册成功")
                window.location.href("/")
            } else {
                alert("注册失败")
            }
        }
    })
})

$('#login').click(function () {
    var data = {
        "username" : $('#inputEmail').val(),
        "password" : $('#inputPassword').val()
    }
    console.log(data)
    $.ajax({
        url : "/api/user/login",
        type : "post",
        data : data,
        success : function (res) {

        }
    })
})

$('#logout').click(function () {
    $.ajax({
        url : "/api/user/logout",
        type : "get",
        success : function () {
            
        }
    })
})