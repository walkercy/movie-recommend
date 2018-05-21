$(document).ready(function () {
    var baseUrl = "https://api.douban.com/v2/movie"
    $.ajax({
        url : baseUrl + "/in_theaters",
        type : "get",
        success : function (data) {
            console.log(data)
        }
    })
})