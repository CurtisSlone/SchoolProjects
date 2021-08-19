$(function(){
    var timerId = 0;
    var passRegex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W){12,}/g;
    var safePass = false;
    var safeEmail = false;
    //Use AJAX to get /check-email and recieve dictisonary/JSON object for client-side comparison of email
    $('#email').focus(function(){
        var emailList = {};
        $('#formSuccess').hide();
        $('#formError').hide();
        $.ajax({
            type : 'GET',
            url : '/email-check',
            success: function (data){
                for(key in data){
                    emailList[key] = data[key]
                }
            }
        });  
        timerId = setInterval(function(){
            console.log(emailList);
            for(key in emailList){
                console.log(emailList[key]);
                console.log($('#email').val());
                console.log(emailList[key].localeCompare($('#email').val()));
                if(emailList[key].localeCompare($('#email').val()) == 0 || $('#email').val().length === 0 || !$('#email').val().match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/g) ){
                    $('#formError').text("Email is not available").slideDown(700); 
                    $('#formSuccess').hide();
                    break;
                } else {
                    $('#formSuccess').text("Email is available").slideDown(700); 
                    $('#formError').hide();
                    safeEmail = true;
                }
            }
        },1000);
    });
    $('#email').blur(function(){
        clearInterval(timerId);
        $('#formError').hide();
        $('#formSuccess').hide();
    });
    // End email check
    //password check
    $('#pass').focus(function(){
        $('#pass').val();
        $('#formInfo').text("Password must include 1 digit, 1 capital letter and 1 special character. [-’/`~!#*$@_%+=.,^&(){}[\]|;:”<>?\\]").show();
        timerId = setInterval(function(){
            $('#form_button').hide();
            console.log(passRegex.test($('#pass').val()));
            if(passRegex.test($('#pass').val())){
                console.log("exectued")
                safePass = true;
            }
        },2000);
    });
    $('#pass').blur(function(){
        $('#formInfo').hide();
        clearInterval(timerId);
    });
    setInterval(function(){
        if(safePass == true && safeEmail == true)
        {
            $('#form_button').slideDown(700); 
        }
    },1500);
    //open sign up form
    $('#signup_form').click(function(){
        $('#login-box').hide();
        $('#sign-up-box').slideDown(700); 
        //Show Sign-up how to
    setTimeout(function(){
        $('#sign-up-how-to').slideDown(700);
    },5500);
    });

    $('#goto_signup').click(function(){
        $('#login-box').hide();
        $('#sign-up-box').slideDown(700);  
    });
    //open login form
    $('#goto_login').click(function(){
        $('#sign-up-box').hide();
        $('#login-box ').slideDown(700);  
    });
    
    (function () {
        setTimeout(function(){
            $('.quick-info').slideDown(700);
        },3000);
    })();
    
    
});