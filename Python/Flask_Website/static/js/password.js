$(function(){
    $('#password_button').click(function(){
        $.ajax({
            type :'POST',
            url : '/password-check',
            cache : false,
            data : {
                old_password : $('#old-pass').val(),
                new_password : $('#new-pass').val(),
                verify_new_pass : $('#verify-new-pass').val()
            },
            success : function(res){
                if("success" in res){
                    $('#passSuccess').text(res.success).slideDown(500);
                    $('#passError').hide();
                    console.log("success");
                } else {
                    $('#passError').text(res.error).slideDown(500);
                    $('#passSuccess').hide();
                    console.log("error");
                }
            }
        });
    });
    

});