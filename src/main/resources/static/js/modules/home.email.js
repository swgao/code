define(function(require, exports, module) {
    require("validate");
    require("bootstrap")
    $("#pf").validate({
        debug:true, //只验证，不提交
        rules : {
            email: {
                required: true,
                email: true
            },
            messages: {
                email: {
                    required: "请输入邮箱",
                    email: '邮箱格式不正确'
                }
            },
            errorPlacement: function (error, element) {
                element.popover('destroy');
                element.popover({
                    content: error[0].innerHTML
                });
                element.click();
                element.closest('div').removeClass('has-success').addClass('has-error');
            },
            success: function (a, b) {
                $(b).parent().removeClass('has-error').addClass('has-success');
                // $(b).popover('destroy');
            },
            submitHandler: function (form) { //验证通过执行这里
            }
        }
    });

});