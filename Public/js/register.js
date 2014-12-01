$("#register_button").click(
    function() {
        $.post(
            "RegisterPost",
            {
                name: $("#loginName").val(),
                pwd: $("#Password").val(),
                nickName: $("#NickName").val(),
                realEmailAddress: $("#YourRealEmailAddress").val(),
                realEmailPwd: $("#YourRealEmailPwd").val(),
                popServerAddress: $("#POPServerAddress").val(),
                popServerPort: $("#POPServerPort").val(),
                smtpServerAddress: $("#SMTPServerAddress").val(),
                smtpServerPort: $("#SMTPServerPort").val()
            },
            function(result)
            {
                switch(result.state) {
                    case "success":
                        window.location = "home";
                        break;
                    case "fail":
                        alert(result.message);
                        break;
                }
            },
            "json"
        );
    }
)