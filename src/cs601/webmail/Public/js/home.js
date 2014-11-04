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
                    $("#down_right_big").prepend("<a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6\" >" + result.mailsBrief[i].body + "</span></div></a>");
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
                $("#down_right_big").prepend("<div class=\"email_body\" >" + result.body + "</div>");
                $("#down_right_big").prepend("<div class=\"email_head\" ><span class=\"col-lg-6\" >" + result.from_name + "<" + result.from_address + "> </span><span class=\"col-lg-6\" > To me <" + result.to_address + "></span></div>");
            },
            "json"
        );
    }

}