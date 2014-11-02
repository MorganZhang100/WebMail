$("#register_button").click(
    function() {
        $.post(
            "RegisterPost",
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