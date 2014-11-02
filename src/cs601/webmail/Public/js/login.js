$("#login_button").click(
    function() {
        $.post(
            "LoginPost",
            {
                name: $('#loginName').val(),
                pwd: $('#Password').val()
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