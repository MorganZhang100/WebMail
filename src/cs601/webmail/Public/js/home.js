$("#inbox_button").click(
    function() {
        $.post(
            "HomeInboxPost",
            {

            },
            function(result)
            {
                $("#down_right_big").empty();
                var i;
                for(i=0; i<result.mailAmount; i++) {
                    //
                    $("#down_right_big").prepend("<div class=\"row\"><a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6\" >" + result.mailsBrief[i].body + "</span></div></a></div>");
                }
            },
            "json"
        );
    }
)

window.onhashchange = function() {
    var hashStr = location.hash.replace("#","");
    var hashKey = hashStr.split("/")[0];
    var hashValue = hashStr.split("/")[1];

    if(hashKey == "detail") {
        $.post(
            "HomeEmailDetail",
            {
                mail_id : hashValue
            },
            function(result)
            {
                $("#down_right_big").empty();
                $("#down_right_big").prepend("<div class=\"row\" id=\"email_detail\" ></div>");
                $("#email_detail").prepend("<div class=\"email_body\" >" + result.body + "</div>");
                $("#email_detail").prepend("<div class=\"email_head\" ><span class=\"col-lg-6\" >" + result.from_name + "&lt;" + result.from_address + "&gt; </span><span class=\"col-lg-6\" > To me &lt;" + result.to_address + "&gt;</span></div>");
                $("#email_detail").prepend("<div class=\"email_subject\" ><span class=\"col-lg-12\" >" + result.subject + "</span></div>");
            },
            "json"
        );
    }

    if(hashKey == "logout") {
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

    if(hashKey == "compose") {
        $("#down_right_big").empty();
        $("#down_right_big").prepend("<div class=\"row\" id=\"compose_detail\" ></div>");
        $("#compose_detail").prepend("<div class=\"col-lg-12 compose_down\"><a class=\"btn btn-primary col-lg-2\" id=\"send_button\">Send</a></div>");
        $("#compose_detail").prepend("<textarea class=\"compose_body col-lg-12 form-control\" rows=\"20\"/></textarea>");
        $("#compose_detail").prepend("<input type=\"text\" class=\"compose_head col-lg-12 form-control\" placeholder=\"Subject:\"/>");
        $("#compose_detail").prepend("<input type=\"text\" class=\"compose_head col-lg-12 form-control\" placeholder=\"To:\"/>");
    }
}