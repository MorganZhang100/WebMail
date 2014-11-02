$("#logout").click(
    function() {
        $.post(
            "LogoutPost",
            {},
            function(result)
            {
                window.location = "/";
            },
            "json"
        );
    }
)