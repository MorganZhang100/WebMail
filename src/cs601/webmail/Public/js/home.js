if(location.hash == "") window.location = "home#inbox/0";

$("#check_mail_button").click(
    function() {
        $("#unread_button").remove();

        $.post(
            "HomeCheckEmailPost",
            {},
            function(result)
            {
                $("#down_right_big").empty();
                var i;
                for(i=0; i<result.mailAmount; i++) {
                    $("#down_right_big").prepend("<div class=\"row\"><a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" id=\"detail_" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6 email_brief_span\" >" + result.mailsBrief[i].body + "</span></div></a></div>");
                }

                $("#pre_button").attr("href","#inbox/0");
                $("#aft_button").attr("href","#inbox/1");
            },
            "json"
        );
    }
);

window.onhashchange = function() {

    $("#unread_button").remove();

    var hashStr = location.hash.replace("#","");
    var hashKey = hashStr.split("/")[0];
    var hashValue = hashStr.split("/")[1];

    if(hashKey == "unread") {
        $.post(
            "HomeUnReadPost",
            {
                mail_id : hashValue
            },
            function(result)
            {},
            "json"
        );
    }

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

                $("#mid_right_big_left_buttons").prepend("<a href=\"#unread/" + hashValue + "\" class=\"btn btn-default mid_right_buttons\" id=\"unread_button\">UnRead</a>");
            },
            "json"
        );
    }

    if(hashKey == "inbox") {
        if(hashValue == undefined) {
            hashValue = 0;
        }
        prePageNumber = hashValue > 0 ? hashValue -1 : 0;
        aftPageNumber = parseInt(hashValue) + 1;

        $.post(
            "HomeInboxPost",
            {
                pageNumber: hashValue
            },
            function(result)
            {
                $("#down_right_big").empty();
                var i;
                for(i=0; i<result.mailAmount; i++) {
                    $("#down_right_big").prepend("<div class=\"row\"><a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" id=\"detail_" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6 email_brief_span\" >" + result.mailsBrief[i].body + "</span></div></a></div>");

                    if(result.mailsBrief[i].read_flag == 0) $("#detail_" + result.mailsBrief[i].mail_id).addClass("unread");
                }

                $("#pre_button").attr("href","#inbox/" + prePageNumber);
                $("#aft_button").attr("href","#inbox/" + aftPageNumber);

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
        $("#compose_detail").prepend("<div class=\"col-lg-12 compose_down\"><a class=\"btn btn-primary col-lg-2\" href=\"#send\" id=\"send_button\">Send</a></div>");
        $("#compose_detail").prepend("<textarea class=\"compose_body col-lg-12 form-control\" rows=\"20\" id=\"body\"/></textarea>");
        $("#compose_detail").prepend("<input type=\"text\" class=\"compose_head col-lg-12 form-control\" placeholder=\"Subject:\" id=\"subject\"/>");
        $("#compose_detail").prepend("<input type=\"text\" class=\"compose_head col-lg-12 form-control\" placeholder=\"To:\" id=\"toAddress\"/>");
    }

    if(hashKey == "send") {
        $.post(
            "HomeComposePost",
            {
                subject : $("#subject").val(),
                toAddress : $("#toAddress").val(),
                body : $("#body").val()
            },
            function(result)
            {
                alert(result.success);
            },
            "json"
        );
    }
}