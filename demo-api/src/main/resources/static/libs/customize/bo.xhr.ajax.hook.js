
/****************************************
 * 统一拦截xhr请求  ajax请求
 * 0：  对象没有完成初始化
 * 1： 对象开始发送请求
 * 2： 对象的请求发送完成
 * 3： 对象开始读取服务器响应
 * 4： 对象读取服务器响应结束
 ********************************************/
;(function(open) {
    XMLHttpRequest.prototype.open =function(method, url, async, user, pass) {
        // this.addEventListener("progress", updateProgress);
        // this.addEventListener("load", transferComplete);
        // this.addEventListener("error", _transferFailed);
        // this.addEventListener("abort", transferCanceled);
        this.addEventListener("readystatechange",function() {  //onreadystatechange
            if(this.readyState == 4){
                if(this.status==401){
                    console.error("身份认证已过期，请重新登陆");
                    window.location.href = $.baseUrl()+'/logout';
                }
                if(this.status==403){
                    console.error("您无此权限，权限验证失败");
                    window.location.href = $.baseUrl()+'/home#noPermission';
                }
            }
        },false);
        open.call(this, method, url, async, user, pass);
        //在这添加自定义数据
        // this.setRequestHeader("Authorization","Token 123")
    };
})(XMLHttpRequest.prototype.open);
;(function ($) {
    $.ajaxSetup({
        beforeSend:function(xhr) {
            // xhr.setRequestHeader('Authorization','Token 123')
        },
        error: function(response, textStatus, errorThrown) {
            console.error("reqAjax请求ERROR...:"+textStatus+"| 状态码："+response.readyState+"| "+errorThrown.toString());

            if(response.status == '401'){
                console.error("身份认证已过期，请重新登陆");
                window.location.href = $.baseUrl+'/logout';
                return;
            }
            if(response.status == '403'){
                console.error("您无此权限，权限验证失败");
                window.location.href = $.baseUrl+'/home#noPermission';
            }
        },
        complete: function(XMLHttpRequest) {
        }
    });
})(jQuery);

