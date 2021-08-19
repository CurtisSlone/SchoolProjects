$(function(){
    $('#login_button').click(function(){
        $.ajax({
            type :'POST',
            url : '/login',
            cache : false,
            data : {
                email : $('#login-email').val(),
                pass : $('#login-pass').val()
            },
            success : function(res){
                if("success" in res){
                $('#loginSuccess').text(res.success).slideDown(500);
                $('#loginError').hide();
                setTimeout(function(){
                    window.location.reload();
                },2500);
                } else {
                    $('#loginError').text(res.error).slideDown(500);
                    $('#loginSuccess').hide();
                }
            }
        });
    });
    
    $('#form_button').click(function(){
        
        $.ajax({
            type : 'POST',
            url : '/sign-up',
            cache: false,
            data : {
                name : $('#user_name').val(),
                pass : $('#pass').val(),
                email : $('#email').val()
            },
            success: function(res){
                $('#formSuccess').text(res).show();
                $('#formError').hide();
                setTimeout(function(){
                    $('#sign-up-box').hide();
                    $('#login-box').show();
                    $('#formSuccess').hide();
                },1500);
                

				
			},
			error: function(error){
				$('#formError').text(error).show();
                $('#formSuccess').hide();
			}
           
        })
        console.log("executed");
    });
});