$("#inbox_button").click(
    function() {
        $.post(
            "HomeInboxPost",
            {

            },
            function(result)
            {
                for(var i=0; i<result.mailAmount; i++) {
                    alert(result.mailsBrief[i].subject);
                }
            },
            "json"
        );
    }
)